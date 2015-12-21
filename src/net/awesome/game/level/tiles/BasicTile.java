package net.awesome.game.level.tiles;

import net.awesome.game.gfx.Colors;
import net.awesome.game.gfx.Screen;
import net.awesome.game.level.Level;

public class BasicTile extends Tile {
	
	protected int tileID;
	
	public BasicTile(int id, int x, int y, int color) {
		super(id, false, false, color);
		this.tileID = x + y * 32;
	}
	public void tick(){
		
	}
	public void render(Screen screen, Level level, int x, int y) {
		screen.render(x, y, tileID, 0, 1);
	}

}
