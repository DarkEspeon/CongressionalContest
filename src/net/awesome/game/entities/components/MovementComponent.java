package net.awesome.game.entities.components;

public class MovementComponent implements Component {
	public int speed = 10, numSteps = 0;
	public boolean isSwimming = false;
	public MovementComponent(int speed){
		this.speed = speed;
	}
}
