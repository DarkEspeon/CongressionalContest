package net.awesome.game.entities.components;

public class RenderComponent implements Component {
	public boolean isMoving = false;
	public int movingDir = 1;
	public int scale = 1;
	public RenderComponent(int movingDir, int scale){
		this.movingDir = movingDir;
		this.scale = scale;
	}
}
