package net.awesome.game;

public class Maths {
	public static float distance(int x1, int y1, int x2, int y2){
		return (float)Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
	}
}
