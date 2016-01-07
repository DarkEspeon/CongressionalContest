package net.awesome.game.level;

import java.awt.image.BufferedImage;
//import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.awesome.game.gfx.Screen;
import net.awesome.game.level.tiles.Tile;

public class Level {
	private byte[] tiles;
	public int width;
	public int height;
//	private List<OldEntity> entities = new ArrayList<>();
//	private List<Player> players = new ArrayList<>();
	private String imagePath;
	private BufferedImage image;
	
	public Level(String pathname){
		if(pathname == null){
			tiles = new byte[64 * 64];
			this.width = 64;
			this.height = 64;
			this.generateLevel();
		} else {
			this.imagePath = pathname;
			this.loadLevelFromFile();
		}
		
	}
	
	private void loadLevelFromFile(){
		try {
			this.image = ImageIO.read(Level.class.getResource(this.imagePath));
			this.width = this.image.getWidth();
			this.height = this.image.getHeight();
			tiles = new byte[width * height];
			this.loadTiles();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void loadTiles(){
		int[] tileColors = this.image.getRGB(0, 0, width, height, null, 0, width);
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				tileCheck: for(Tile t : Tile.tiles){
					if(t != null && t.getColor() == tileColors[x + y * width]){
						this.tiles[x + y * width] = t.getID();
						break tileCheck;
					}
					
				}
			}
		}
	}
	
	/*private void saveLevelToFile(){
		try{
			ImageIO.write(image, "png", new File(Level.class.getResource(this.imagePath).getFile()));
		} catch (IOException e){
			e.printStackTrace();
		}
	}*/
	
	public void alterTile(int x, int y, Tile newTile){
		this.tiles[x + y * width] = newTile.getID();
		image.setRGB(x, y, newTile.getColor());
	}
	
	public void generateLevel(){
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
					if(x * y % 10 < 7) tiles[x + y * width] = Tile.GRASS.getID();
					else tiles[x + y * width] = Tile.STONE.getID();
			}
		}
	}
	
//	public synchronized List<OldEntity> getEntities(){
//		return this.entities;
//	}
//	public synchronized List<Player> getPlayers(){
//		return this.players;
//	}
	
	public void tick(){
//		for(OldEntity e : getEntities()){
//			e.tick();
//		}
//		for(Player p : getPlayers()){
//			p.tick();
//		}
		for(Tile t : Tile.tiles){
			if(t == null) break;
			else t.tick();
		}
	}
	
	public void renderTiles(Screen screen, int xOffset, int yOffset){
		if(xOffset < 0) xOffset = 0;
		if(xOffset > ((width << 3) - screen.width)) xOffset = ((width << 3) - screen.width);
		if(yOffset < 0) yOffset = 0;
		if(yOffset > ((height << 3) - screen.height)) yOffset = ((height << 3) - screen.height); 
	
		screen.setOffset(xOffset, yOffset);
		for(int y = (yOffset >> 3); y < (yOffset + screen.height >> 3) + 1; y++){
			for(int x = (xOffset >> 3); x < (xOffset + screen.width >> 3) + 1; x++){
				getTile(x, y).render(screen, this, x << 3, y << 3);
			}
		}
	}
	
	public void renderEntities(Screen screen){
//		for(OldEntity e : getEntities()){
//				e.render(screen);
//		}
//		for(Player p : getPlayers()){
//			if(!p.equals(Game.game.player)){
//				p.render(screen);
//			}
//		}
	}

	public Tile getTile(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height) return Tile.VOID;
		return Tile.tiles[tiles[x + y * width]];
	}

//	public void addEntity(OldEntity e) {
//		this.getEntities().add(e);
//	}
//	public void addPlayer(Player p){
//		this.getPlayers().add(p);
//	}
}
