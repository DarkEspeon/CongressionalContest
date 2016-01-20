package net.awesome.game.entities.system;

import java.awt.Rectangle;

import net.awesome.game.Game;
import net.awesome.game.entities.EntitySystem;
import net.awesome.game.entities.components.CollisionComponent;
import net.awesome.game.entities.components.ColorComponent;
import net.awesome.game.entities.components.MovementComponent;
import net.awesome.game.entities.components.PositionComponent;
import net.awesome.game.entities.components.RenderComponent;

public class RenderSystem extends ComponentSystem {

	public RenderSystem(EntitySystem es) {
		super(es);
		addRequired(RenderComponent.class);
		addRequired(PositionComponent.class);
		addOptional(MovementComponent.class);
		addOptional(ColorComponent.class);
		addOptional(CollisionComponent.class);
	}
	public void onProcess(float delta) {
		RenderComponent rc = getRequired(RenderComponent.class);
		PositionComponent pc = getRequired(PositionComponent.class);
		MovementComponent mc = getOptional(MovementComponent.class);
		ColorComponent cc = getOptional(ColorComponent.class);
		CollisionComponent colc = getOptional(CollisionComponent.class);
		if(mc != null){
			int flipTop = (mc.numSteps >> rc.walkingSpeed) & 1;
			int flipBot = (mc.numSteps >> rc.walkingSpeed) & 1;
			int xTile = rc.xTile;
			int yTile = rc.yTile;
			if(rc.movingDir == 1){
				xTile += 2;
			} else if(rc.movingDir > 1){
				xTile += 4 + ((mc.numSteps >> rc.walkingSpeed) & 1) * 2;
				flipTop = (rc.movingDir - 1) % 2;
			}
			int modifier = 8 * rc.scale;
			int xOffset = pc.x - modifier / 2;
			int yOffset = (pc.y - modifier / 2) - 4;
		
			if(mc.isSwimming){
				int xFrame = 0;
				yOffset += 4;
				if(rc.tickCount % 60 < 15){
					xFrame = 0;
				} else if(rc.tickCount % 60 >= 15 && rc.tickCount % 60 < 30){
					yOffset -= 1;
					xFrame = 1;
				} else if(rc.tickCount % 60 >= 30 && rc.tickCount % 60 < 45){
					xFrame = 2;
				} else if(rc.tickCount % 60 >= 45){
					xFrame = 1;
				}
				Game.game.screen.render(xOffset, yOffset + 3, xFrame + 27 * 32, 0, 1);
				Game.game.screen.render(xOffset + 8, yOffset + 3, xFrame + 27 * 32, 0x1, 1);
			}
			if(cc == null){
				Game.game.screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, flipTop, rc.scale);
				Game.game.screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, flipTop, rc.scale);
				if(!mc.isSwimming){
					Game.game.screen.render(xOffset + (modifier * flipBot), yOffset + modifier, xTile + (yTile + 1) * 32, flipBot, rc.scale);
					Game.game.screen.render(xOffset + modifier - (modifier * flipBot), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, flipBot, rc.scale);
				}
			} else {
				Game.game.screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, flipTop, rc.scale, cc.colorSwaps);
				Game.game.screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, flipTop, rc.scale, cc.colorSwaps);
				if(!mc.isSwimming){
					Game.game.screen.render(xOffset + (modifier * flipBot), yOffset + modifier, xTile + (yTile + 1) * 32, flipBot, rc.scale, cc.colorSwaps);
					Game.game.screen.render(xOffset + modifier - (modifier * flipBot), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, flipBot, rc.scale, cc.colorSwaps);
				}
			}
		} else {
			int modifier = 8 * rc.scale;
			int xOffset = pc.x - modifier / 2;
			int yOffset = (pc.y - modifier / 2) - 4;
			if(rc.tickCount % 80 >= 0 && rc.tickCount % 80 < 20){
				yOffset--;
			} else if(rc.tickCount % 80 >= 40 && rc.tickCount % 80 < 60){
				yOffset++;
			}
			if(cc == null){
				Game.game.screen.render(xOffset, yOffset, (rc.xTile) + (rc.yTile) * 32, 0, rc.scale);
				Game.game.screen.render(xOffset + modifier, yOffset, (rc.xTile + 1) + (rc.yTile) * 32, 0, rc.scale);
				Game.game.screen.render(xOffset, yOffset + modifier, (rc.xTile) + (rc.yTile + 1) * 32, 0, rc.scale);
				Game.game.screen.render(xOffset + modifier, yOffset + modifier, (rc.xTile + 1) + (rc.yTile + 1) * 32, 0, rc.scale);
			} else {
				Game.game.screen.render(xOffset, yOffset, (rc.xTile) + (rc.yTile) * 32, 0, rc.scale, cc.colorSwaps);
				Game.game.screen.render(xOffset + modifier, yOffset, (rc.xTile + 1) + (rc.yTile) * 32, 0, rc.scale, cc.colorSwaps);
				Game.game.screen.render(xOffset, yOffset + modifier, (rc.xTile) + (rc.yTile + 1) * 32, 0, rc.scale, cc.colorSwaps);
				Game.game.screen.render(xOffset + modifier, yOffset + modifier, (rc.xTile + 1) + (rc.yTile + 1) * 32, 0, rc.scale, cc.colorSwaps);
			}
		}
	}
}
