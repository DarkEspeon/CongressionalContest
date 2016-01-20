package net.awesome.game.entities.components;

import net.awesome.game.Game;
import net.awesome.game.level.Level;


public class PositionComponent implements Component{
	public int x, y;
	public Level level;
	public PositionComponent(){
		this(0, 0, null);
	}
	public PositionComponent(int x, int y){
		this(x, y, null);
	}
	public PositionComponent(int x, int y, Level level){
		this.x = x * 8;
		this.y = y * 8;
		this.level = level;
	}
}
