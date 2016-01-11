package net.awesome.game.entities.components;

public class CollisionComponent implements Component{
	public int xMin = 0, xMax = 0, yMin = 0, yMax = 0;
	public int xEMin = 0, xEMax = 0, yEMin = 0, yEMax = 0;
	public CollisionComponent(int xMin, int xMax, int yMin, int yMax){
		this.xMin = this.xEMin = xMin;
		this.xMax = this.xEMax = xMax;
		this.yMin = this.yEMin = yMin;
		this.yMax = this.yEMax = yMax;
	}
	public CollisionComponent(int xMin, int xMax, int yMin, int yMax, int xEMin, int xEMax, int yEMin, int yEMax){
		this(xMin, xMax, yMin, yMax);
		this.xEMin = xEMin;
		this.xEMax = xEMax;
		this.yEMin = yEMin;
		this.yEMax = yEMax;
	}
}
