package net.awesome.game.entities;

import net.awesome.game.level.Level;
import net.awesome.game.level.tiles.Tile;

public abstract class Mob extends Entity {
	
	protected String name;
	protected int speed;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected int movingDir = 1;
	protected int scale = 1;
	
	public Mob(Level level, String name, int x, int y, int speed) {
		super(level);
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.name = name;
	}
	
	public void move(int xa, int ya){
		if(xa != 0 && ya != 0){
			move(xa, 0);
			move(0, ya);
			numSteps--;
			return;
		}
		numSteps++;
		if(ya < 0) movingDir = 0;
		if(ya > 0) movingDir = 1;
		if(xa < 0) movingDir = 2;
		if(xa > 0) movingDir = 3;
		if(!hasCollided(xa, ya)){
			x += xa * speed;
			y += ya * speed;
		}
	}

	public abstract boolean hasCollided(int xa, int ya);
	
	protected boolean isSolidTile(int xa, int ya, int x, int y){
		if(level == null) return false;
		Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
		Tile newTile = level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
		if(!lastTile.equals(newTile) && newTile.isSolid()) return true;
		return false;
	}
	
	public String getName(){return name;}

	public void setNumSteps(int numSteps) {
		this.numSteps = numSteps;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public void setMovingDir(int movingDir) {
		this.movingDir = movingDir;
	}
}
