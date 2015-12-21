package net.awesome.game.level.tiles;

import net.awesome.game.gfx.Colors;

public class BasicSolidTile extends BasicTile{

	public BasicSolidTile(int id, int x, int y, int color) {
		super(id, x, y, color);
		this.solid = true;
	}

}
