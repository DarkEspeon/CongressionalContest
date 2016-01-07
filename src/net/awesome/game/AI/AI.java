package net.awesome.game.AI;

public interface AI {
	public int getXA();
	public int getYA();
	public void findPath(int x, int y, int goalx, int goaly);
}
