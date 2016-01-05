package net.awesome.game.entities;

import net.awesome.game.gfx.Screen;
import net.awesome.game.level.Level;

public abstract class OldEntity {
	public int x, y;
	protected Level level;
	public OldEntity(Level level){
		init(level);
	}
	public final void init(Level level){
		this.level = level;
	}
	public abstract void tick();
	public abstract void render(Screen screen);
}
