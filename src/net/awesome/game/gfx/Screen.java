package net.awesome.game.gfx;

import net.awesome.game.entities.Player;

public class Screen {
	public static final int MAP_WIDTH = 64;
	public static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;
	
	public static final int BIT_MIRROR_X = 0x01;
	public static final int BIT_MIRROR_Y = 0x02;
	
	public int[] pixels;
	
	public int xOffset = 0;
	public int yOffset = 0;
	
	public int width;
	public int height;
	
	public SpriteSheet sheet;
	
	public Screen(int width, int height, SpriteSheet sheet){
		this.width = width;
		this.height = height;
		this.sheet = sheet;
		pixels = new int[width * height];
	}
	public void render(int xPos, int yPos, int tile, int mirrorDir, int scale){
		xPos -= xOffset;
		yPos -= yOffset;
		boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
		boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;
		int scaleMap = scale - 1;
		int xTile = tile % 32;
		int yTile = tile / 32;
		int tileOffset = (xTile << 3) + (yTile << 3) * sheet.width;
		for(int y = 0; y < 8; y++){
			int ySheet = y;
			if(mirrorY) ySheet = 7 - y;
			int yPixel = y + yPos + (y * scaleMap) - ((scaleMap << 3) / 2);
			for(int x = 0; x < 8; x++){
				int xSheet = x;
				if(mirrorX) xSheet = 7 - x;
				int xPixel = x + xPos + (x * scaleMap) - ((scaleMap << 3) / 2);
				int col = sheet.pixels[xSheet + ySheet * sheet.width + tileOffset];
				for(int yScale = 0; yScale < scale; yScale++){
					if(yPixel + yScale < 0 || yPixel + yScale >= height) continue;
					for(int xScale = 0; xScale < scale; xScale++){
						if(xPixel + xScale < 0 || xPixel + xScale >= width) continue;
						if(col != 0xFFFF00FF)pixels[(xPixel + xScale) + (yPixel + yScale) * width] = col;
					}
				}
			}
		}
	}
	public void render(int xPos, int yPos, int tile, int mirrorDir, int scale, Player p){
		xPos -= xOffset;
		yPos -= yOffset;
		boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
		boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;
		int scaleMap = scale - 1;
		int xTile = tile % 32;
		int yTile = tile / 32;
		int tileOffset = (xTile << 3) + (yTile << 3) * sheet.width;
		for(int y = 0; y < 8; y++){
			int ySheet = y;
			if(mirrorY) ySheet = 7 - y;
			int yPixel = y + yPos + (y * scaleMap) - ((scaleMap << 3) / 2);
			for(int x = 0; x < 8; x++){
				int xSheet = x;
				if(mirrorX) xSheet = 7 - x;
				int xPixel = x + xPos + (x * scaleMap) - ((scaleMap << 3) / 2);
				int col = sheet.pixels[xSheet + ySheet * sheet.width + tileOffset];
				if(col == 0xFF3C3C3C) col = p.getHairColor().getRGB();
				if(col == 0xFF5A5A5A) col = p.getSkinColor().getRGB();
				if(col == 0xFF787878) col = p.getEyeColor().getRGB();
				if(col == 0xFF969696) col = p.getShirtColor().getRGB();
				if(col == 0xFFB4B4B4) col = p.getPantsColor().getRGB();
				for(int yScale = 0; yScale < scale; yScale++){
					if(yPixel + yScale < 0 || yPixel + yScale >= height) continue;
					for(int xScale = 0; xScale < scale; xScale++){
						if(xPixel + xScale < 0 || xPixel + xScale >= width) continue;
						if(col != 0xFFFF00FF) pixels[(xPixel + xScale) + (yPixel + yScale) * width] = col;
					}
				}
			}
		}
	}
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
}
