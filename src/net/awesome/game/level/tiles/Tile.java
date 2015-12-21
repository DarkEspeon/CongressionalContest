package net.awesome.game.level.tiles;

import net.awesome.game.gfx.Colors;
import net.awesome.game.gfx.Screen;
import net.awesome.game.level.Level;

public abstract class Tile {

	public static Tile[] tiles = new Tile[256];
	public static Tile VOID = new BasicSolidTile(0, 0, 0, 0xFF000000);
	public static Tile STONE = new BasicSolidTile(1, 1, 0, 0xFFA0A0A0);
	public static Tile GRASS = new BasicTile(2, 2, 0, 0xFF00FF21);
	public static Tile WATER = new AnimatedTile(3, new int[][] {{0, 5}, {1, 5}, {2, 5}, {1, 5}}, 0xFF0000FF, 500);
	
	protected byte id;
	protected boolean solid;
	protected boolean emiter;
	protected int color;
	
	public Tile(int id, boolean solid, boolean emiter, int color){
		this.id = (byte)id;
		if(tiles[id] != null) throw new RuntimeException("Duplicate Tile id on " + id);
		this.solid = solid;
		this.emiter = emiter;
		this.color = color;
		tiles[id] = this;
	}
	
	public byte getID() {return id;}
	public boolean isSolid() {return solid;}
	public boolean isEmiter() {return emiter;}
	public abstract void tick();
	public abstract void render(Screen screen, Level level, int x, int y);
	
	public int getColor() {
		return color;
	}
}
