package net.awesome.game.gfx;

import java.awt.Rectangle;
import java.util.Map;

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
	public void render(Rectangle r){
		r.x -= xOffset;
		r.y -= yOffset;
		for(int y = r.y; y < r.y + r.height; y++){
			if(y < 0 || y >= height) continue;
			for(int x = r.x; x < r.x + r.width; x++){
				if(x < 0 || x >= width) continue;
				pixels[x + y * width] = 0xFFFF00FF;
			}
		}
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
	public void render(int xPos, int yPos, int tile, int mirrorDir, int scale, Map<Integer, Integer> colorSwap){
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
				if(colorSwap.containsKey(col)) col = colorSwap.get(col);
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
