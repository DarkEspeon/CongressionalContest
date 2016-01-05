package net.awesome.game.entities.system;

import net.awesome.game.entities.EntitySystem;
import net.awesome.game.entities.components.AIComponent;
import net.awesome.game.entities.components.MovementComponent;
import net.awesome.game.entities.components.PlayerComponent;
import net.awesome.game.entities.components.PositionComponent;
import net.awesome.game.entities.components.RenderComponent;

public class MovementSystem extends ComponentSystem {
	public MovementSystem(EntitySystem es) {
		super(es);
		addRequired(MovementComponent.class);
		addRequired(PositionComponent.class);
		addOptional(RenderComponent.class);
		addExclusive(PlayerComponent.class, AIComponent.class);
	}

	public void onProcess() {
		MovementComponent mc = getRequired(MovementComponent.class);
		PositionComponent posc = getRequired(PositionComponent.class);
		RenderComponent rc = getOptional(RenderComponent.class);
		PlayerComponent playc = getOptional(PlayerComponent.class);
		AIComponent aic = getOptional(AIComponent.class);
		int xa = 0, ya = 0;
		if(playc == null && aic != null){
			// AI
		} else if(aic == null && playc != null) {
			if(playc.input != null){
				if(playc.input.up.isPressed()) ya--;
				if(playc.input.down.isPressed()) ya++;
				if(playc.input.left.isPressed()) xa--;
				if(playc.input.right.isPressed()) xa++;
			}
		}
		if(xa != 0 || ya != 0){
			move(xa, ya);
			rc.isMoving = true;
		} else {
			rc.isMoving = false;
		}
		if(posc.level != null && posc.level.getTile((posc.x >> 3), (posc.y >> 3)).getID() == 3){
			mc.isSwimming = true;
		} else {
			mc.isSwimming = false;
		}
	}
	private void move(int xa, int ya){
		MovementComponent mc = getRequired(MovementComponent.class);
		PositionComponent posc = getRequired(PositionComponent.class);
		RenderComponent rc = getOptional(RenderComponent.class);
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
		posc.x += xa * mc.speed;
		posc.y += ya * mc.speed;
	}
}
