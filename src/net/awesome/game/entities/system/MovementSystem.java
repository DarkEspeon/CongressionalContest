package net.awesome.game.entities.system;

import java.awt.Rectangle;
import java.util.List;

import net.awesome.game.Game;
import net.awesome.game.Maths;
import net.awesome.game.Vector2d;
import net.awesome.game.entities.Entity;
import net.awesome.game.entities.EntitySystem;
import net.awesome.game.entities.components.AIComponent;
import net.awesome.game.entities.components.CollisionComponent;
import net.awesome.game.entities.components.CrystalComponent;
import net.awesome.game.entities.components.MovementComponent;
import net.awesome.game.entities.components.PlayerComponent;
import net.awesome.game.entities.components.PositionComponent;
import net.awesome.game.entities.components.RenderComponent;
import net.awesome.game.level.tiles.Tile;

public class MovementSystem extends ComponentSystem {
	public static enum CollisionResponse{
		SLIDING, POINTS, NOCOLLISION;
	}
	private static Entity collidedWith = null;
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
			float dist = Maths.distance(posc.x >> 3, posc.y >> 3, Game.playerX >> 3, Game.playerY >> 3);
			if(aic.aiModule != null && dist < aic.agroDistance && dist >= aic.minDistance){
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
		CollisionResponse cr = hasCollided(xa, ya);
		if(cr.equals(CollisionResponse.NOCOLLISION)){
			posc.x += xa * mc.speed;
			posc.y += ya * mc.speed;
			if(aic != null){
				aic.x += xa * mc.speed;
				aic.y += ya * mc.speed;
			}
		} else if(cr.equals(CollisionResponse.POINTS) && collidedWith != null){
			Game.points++;
			es.removeEntity(collidedWith.getName());
		}
	}
	private CollisionResponse hasCollided(int xa, int ya){
		collidedWith = null;
		CollisionComponent cc = getRequired(CollisionComponent.class);
		PositionComponent posc = getRequired(PositionComponent.class);
		List<Entity> entities = getEntitiesWith(CollisionComponent.class, PositionComponent.class);
		if(cc == null) return CollisionResponse.NOCOLLISION;
		for(int x = cc.xMin; x < cc.xMax; x++){
			if(isSolidTile(xa, ya, x, cc.yMin)) return CollisionResponse.SLIDING;
			if(isSolidTile(xa, ya, x, cc.yMax)) return CollisionResponse.SLIDING;
		}
		for(int y = cc.yMin; y < cc.yMax; y++){
			if(isSolidTile(xa, ya, cc.xMin, y)) return CollisionResponse.SLIDING;
			if(isSolidTile(xa, ya, cc.xMax, y)) return CollisionResponse.SLIDING;
		}
		Rectangle r = new Rectangle();
		r.x = posc.x + xa + cc.xEMin;
		r.width = cc.xEMax;
		r.y = posc.y + ya + cc.yEMin;
		r.height = cc.yEMax;
		for(Entity e : entities){
			CollisionComponent otherCC = e.getComponent(CollisionComponent.class);
			PositionComponent otherPosC = e.getComponent(PositionComponent.class);
			Rectangle or = new Rectangle();
			or.x = otherPosC.x + otherCC.xEMin;
			or.width = otherCC.xEMax;
			or.y = otherPosC.y + otherCC.yEMin;
			or.height = otherCC.yEMax;
			if(r.intersects(or)){
				if(e.hasComponent(CrystalComponent.class)){
					collidedWith = e;
					return CollisionResponse.POINTS;
				} else return CollisionResponse.SLIDING;
			}
		}
		return CollisionResponse.NOCOLLISION;
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
