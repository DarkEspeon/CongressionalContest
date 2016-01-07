package net.awesome.game.entities.components;

public class CollisionComponent implements Component{
	public int xMin = 0, xMax = 0, yMin = 0, yMax = 0;
	public CollisionComponent(int xMin, int xMax, int yMin, int yMax){
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}
}
