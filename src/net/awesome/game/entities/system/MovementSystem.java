package net.awesome.game.entities.system;

import net.awesome.game.Game;
import net.awesome.game.Maths;
import net.awesome.game.entities.EntitySystem;
import net.awesome.game.entities.components.AIComponent;
import net.awesome.game.entities.components.CollisionComponent;
import net.awesome.game.entities.components.MovementComponent;
import net.awesome.game.entities.components.PlayerComponent;
import net.awesome.game.entities.components.PositionComponent;
import net.awesome.game.entities.components.RenderComponent;
import net.awesome.game.level.tiles.Tile;

public class MovementSystem extends ComponentSystem {
	public MovementSystem(EntitySystem es) {
		super(es);
		addRequired(MovementComponent.class);
		addRequired(PositionComponent.class);
		addRequired(CollisionComponent.class);
		addOptional(RenderComponent.class);
		addOptional(PlayerComponent.class);
		addOptional(AIComponent.class);
	}

	public void onProcess() {
		MovementComponent mc = getRequired(MovementComponent.class);
		PositionComponent posc = getRequired(PositionComponent.class);
		RenderComponent rc = getOptional(RenderComponent.class);
		PlayerComponent playc = getOptional(PlayerComponent.class);
		AIComponent aic = getOptional(AIComponent.class);
		int xa = 0, ya = 0;
		if(rc != null) rc.tickCount++;
		if(playc == null && aic != null){
			if(aic.aiModule != null && Maths.distance(aic.x, aic.y, Game.playerX, Game.playerY) < aic.agroDistance){
				aic.aiModule.findPath(aic.x, aic.y, Game.playerX, Game.playerY);
				xa = aic.aiModule.getXA();
				ya = aic.aiModule.getYA();
			}
		} else if(aic == null && playc != null) {
			if(playc.input != null){
				if(playc.input.up.isPressed()) ya--;
				if(playc.input.down.isPressed()) ya++;
				if(playc.input.left.isPressed()) xa--;
				if(playc.input.right.isPressed()) xa++;
			}
		} else return;
		if(xa != 0 || ya != 0){
			move(xa, ya);
			if(rc != null) rc.isMoving = true;
		} else {
			if(rc != null) rc.isMoving = false;
		}
		if(posc != null && posc.level != null && posc.level.getTile((posc.x >> 3), (posc.y >> 3)).getID() == 3){
			if(mc != null) mc.isSwimming = true;
		} else {
			if(mc != null) mc.isSwimming = false;
		}
	}
	private void move(int xa, int ya){
		MovementComponent mc = getRequired(MovementComponent.class);
		PositionComponent posc = getRequired(PositionComponent.class);
		RenderComponent rc = getOptional(RenderComponent.class);
		AIComponent aic = getOptional(AIComponent.class);
		if(xa != 0 && ya != 0){
			move(xa, 0);
			move(0, ya);
			mc.numSteps--;
			return;
		}
		mc.numSteps++;
		if(ya < 0) rc.movingDir = 0;
		if(ya > 0) rc.movingDir = 1;
		if(xa < 0) rc.movingDir = 2;
		if(xa > 0) rc.movingDir = 3;
		if(!hasCollided(xa, ya)){
			if(aic != null){
				aic.x += xa * mc.speed;
				aic.y += xa * mc.speed;
			}
			posc.x += xa * mc.speed;
			posc.y += ya * mc.speed;
		}
	}
	private boolean hasCollided(int xa, int ya){
		CollisionComponent cc = getRequired(CollisionComponent.class);
		if(cc == null) return false;
		for(int x = cc.xMin; x < cc.xMax; x++){
			if(isSolidTile(xa, ya, x, cc.yMin)) return true;
			if(isSolidTile(xa, ya, x, cc.yMax)) return true;
		}
		for(int y = cc.yMin; y < cc.yMax; y++){
			if(isSolidTile(xa, ya, cc.xMin, y)) return true;
			if(isSolidTile(xa, ya, cc.xMax, y)) return true;
		}
		return false;
	}
	private boolean isSolidTile(int xa, int ya, int x, int y){
		PositionComponent posc = getRequired(PositionComponent.class);
		if(posc == null || posc.level == null) return false;
		Tile lastTile = posc.level.getTile((posc.x + x) >> 3, (posc.y + y) >> 3);
		Tile newTile = posc.level.getTile((posc.x + x + xa) >> 3, (posc.y + y + ya) >> 3);
		if(!lastTile.equals(newTile) && newTile.isSolid()) return true;
		return false;
	}
}
