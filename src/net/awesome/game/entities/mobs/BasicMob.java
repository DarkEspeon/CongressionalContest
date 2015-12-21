package net.awesome.game.entities.mobs;

import net.awesome.game.entities.Mob;
import net.awesome.game.entities.mobs.ai.MobAI;
import net.awesome.game.gfx.Screen;
import net.awesome.game.level.Level;

public class BasicMob extends Mob{
	protected MobAI ai;
	public BasicMob(Level level, String name, int x, int y, int speed, MobAI ai) {
		super(level, name, x * 8, y * 8, speed);
		this.ai = ai;
		if(this.ai != null){
			this.ai.setControlled(this);
		}
	}
	public BasicMob(BasicMob m, int x, int y, MobAI ai){
		this(m.level, m.name, x, y, m.speed, ai);
	}

	@Override
	public boolean hasCollided(int xa, int ya) {
		return false;
	}

	@Override
	public void tick() {
		if(ai != null){
			ai.runAI();
			move(ai.getX(), ai.getY());
		}
	}

	@Override
	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 28;
		int walkingSpeed = 3;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBot = (numSteps >> walkingSpeed) & 1;
		int modifier = 8 * scale;
		int xOffset = x - modifier / 2;
		int yOffset = (y - modifier / 2) - 4;
		screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, flipTop, scale);
		screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, flipTop, scale);
		screen.render(xOffset + (modifier * flipBot), yOffset + modifier, xTile + (yTile + 1) * 32, flipBot, scale);
		screen.render(xOffset + modifier - (modifier * flipBot), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, flipBot, scale);
	}

}
