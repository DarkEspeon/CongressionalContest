package net.awesome.game.entities.system;

import java.util.List;

import net.awesome.game.Game;
import net.awesome.game.Maths;
import net.awesome.game.Vector2d;
import net.awesome.game.entities.Entity;
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

	public void onProcess(float delta) {
		MovementComponent mc = getRequired(MovementComponent.class);
		PositionComponent posc = getRequired(PositionComponent.class);
		RenderComponent rc = getOptional(RenderComponent.class);
		PlayerComponent playc = getOptional(PlayerComponent.class);
		AIComponent aic = getOptional(AIComponent.class);
		int xa = 0, ya = 0;
		if(rc != null) rc.tickCount++;
		if(playc == null && aic != null){
			if(aic.aiModule != null && Maths.distance(posc.x >> 3, posc.y >> 3, Game.playerX >> 3, Game.playerY >> 3) < aic.agroDistance){
				aic.aiModule.findPath(posc.x, posc.y, Game.playerX, Game.playerY);
				Vector2d dir = aic.aiModule.getDirection(posc.x, posc.y);
				xa = dir.getX();
				ya = dir.getY();
				//System.out.println("MovementMove{X: " + xa + ", Y: " + ya + "}");
				aic.time += delta;
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
			posc.x += xa * mc.speed;
			posc.y += ya * mc.speed;
			if(aic != null){
				aic.x += xa * mc.speed;
				aic.y += ya * mc.speed;
			}
		} else {
			//System.out.println("Collided");
		}
	}
	private boolean hasCollided(int xa, int ya){
		CollisionComponent cc = getRequired(CollisionComponent.class);
		PositionComponent posc = getRequired(PositionComponent.class);
		List<Entity> entities = getEntitiesWith(CollisionComponent.class, PositionComponent.class);
		if(cc == null) return false;
		for(int x = cc.xMin; x < cc.xMax; x++){
			if(isSolidTile(xa, ya, x, cc.yMin)) return true;
			if(isSolidTile(xa, ya, x, cc.yMax)) return true;
		}
		for(int y = cc.yMin; y < cc.yMax; y++){
			if(isSolidTile(xa, ya, cc.xMin, y)) return true;
			if(isSolidTile(xa, ya, cc.xMax, y)) return true;
		}
		int xMinLine = posc.x + xa + cc.xEMin;
		int xMaxLine = posc.x + xa + cc.xEMax;
		int yMinLine = posc.y + ya + cc.yEMin;
		int yMaxLine = posc.y + ya + cc.yEMax;
		for(Entity e : entities){
			CollisionComponent otherCC = e.getComponent(CollisionComponent.class);
			PositionComponent otherPosC = e.getComponent(PositionComponent.class);
			int oXMinLine = otherPosC.x + otherCC.xEMin;
			int oXMaxLine = otherPosC.x + otherCC.xEMax;
			int oYMinLine = otherPosC.y + otherCC.yEMin;
			int oYMaxLine = otherPosC.y + otherCC.yEMax;
			if(xMinLine <= oXMaxLine && xMinLine >= oXMinLine && yMinLine <= oYMaxLine && yMinLine >= oYMinLine){
				System.out.println("xMin + yMin");
				return true;
			}
			if(xMinLine <= oXMaxLine && xMinLine >= oXMinLine && yMaxLine >= oYMinLine && yMaxLine <= oYMaxLine){
				System.out.println("xMin + yMax");
				return true;
			}
			if(xMaxLine >= oXMinLine && xMaxLine <= oXMaxLine && yMinLine <= oYMaxLine && yMinLine >= oYMinLine){
				System.out.println("xMax + yMin");
				return true;
			}
			if(xMaxLine >= oXMinLine && xMaxLine <= oXMaxLine && yMaxLine >= oYMinLine && yMaxLine <= oYMaxLine){
				System.out.println("xMax + yMax");
				return true;
			}
		}
		return false;
	}
	private boolean isSolidTile(int xa, int ya, int x, int y){
		PositionComponent posc = getRequired(PositionComponent.class);
		if(posc == null) return false;
		Tile lastTile = Game.currentLevel.getTile((posc.x + x) >> 3, (posc.y + y) >> 3);
		Tile newTile = Game.currentLevel.getTile((posc.x + x + xa) >> 3, (posc.y + y + ya) >> 3);
		if(!lastTile.equals(newTile) && newTile.isSolid()) return true;
		return false;
	}
}
