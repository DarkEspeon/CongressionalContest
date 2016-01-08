package net.awesome.game.AI;

import net.awesome.game.Vector2d;

public interface AI {
	public int getXA();
	public int getYA();
	public Vector2d getDirection(int curx, int cury);
	public void findPath(int x, int y, int goalx, int goaly);
}
