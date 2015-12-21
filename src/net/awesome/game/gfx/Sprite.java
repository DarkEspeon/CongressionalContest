package net.awesome.game.gfx;

public class Sprite {
	protected int pixels[];
	protected int width, height;
	public Sprite(int width, int height, int pixels[]){
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
		this.pixels = pixels;
	}
	public void render(int x, int y, boolean fixed){
		
	}
}
