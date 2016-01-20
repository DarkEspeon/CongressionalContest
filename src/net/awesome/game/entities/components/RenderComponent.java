package net.awesome.game.entities.components;

public class RenderComponent implements Component {
	public boolean isMoving = false;
	public int movingDir = 1;
	public int scale = 1;
	public int walkingSpeed = 3;
	public int xTile = 0, yTile = 0;
	public float tickCount = 0;
	public RenderComponent(int movingDir, int scale, int xTile, int yTile){
		this.movingDir = movingDir;
		this.scale = scale;
		this.xTile = xTile;
		this.yTile = yTile;
	}
	public RenderComponent(int xTile, int yTile){
		this(1, 1, xTile, yTile);
	}
}
