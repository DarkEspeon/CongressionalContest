package net.awesome.game.entities.components;

public class MovementComponent implements Component {
	public float speed = 10;
	public int numSteps = 0;
	public boolean isSwimming = false;
	public MovementComponent(float speed){
		this.speed = speed;
	}
}
