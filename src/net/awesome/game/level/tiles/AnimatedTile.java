package net.awesome.game.level.tiles;

import net.awesome.game.gfx.Screen;
import net.awesome.game.level.Level;

public class AnimatedTile extends BasicTile{

	private int[][] animationCoords;
	private int currentFrame;
	private long lastIterTime;
	private int animationDelay;
	
	public AnimatedTile(int id, int[][] animationCoords, int tileColor, int animationDelay) {
		super(id, animationCoords[0][0], animationCoords[0][1], tileColor);
		this.animationCoords = animationCoords;
		this.currentFrame = 0;
		this.lastIterTime = System.currentTimeMillis();
		this.animationDelay = animationDelay;
	}
	public void tick(){
		if(System.currentTimeMillis() - this.lastIterTime >= this.animationDelay){
			this.lastIterTime = System.currentTimeMillis();
			currentFrame = (currentFrame + 1) % animationCoords.length;
			this.tileID = (animationCoords[currentFrame][0] + (animationCoords[currentFrame][1] * 32));
		}
	}
	public void render(Screen screen, Level level, int x, int y){
		screen.render(x, y, tileID, 0, 1);
	}
}
