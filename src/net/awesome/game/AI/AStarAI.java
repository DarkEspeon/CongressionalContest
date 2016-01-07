package net.awesome.game.AI;

import java.util.ArrayList;
import java.util.List;

public class AStarAI implements AI {
	int xa = 0, ya = 0;
	List<Node> path = new ArrayList<>();
	public static class Node {
		public final int x, y;
		public Node(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	public int getXA() {
		return xa;
	}
	public int getYA() {
		return xa;
	}
	public void findPath(int x, int y, int goalx, int goaly) {
		if(path.size() > 0){
			this.xa = path.get(0).x;
			this.ya = path.get(0).y;
			path.remove(0);
		} else {
			this.xa = 0;
			this.ya = 0;
			List<Node> open = new ArrayList<>();
			List<Node> closed = new ArrayList<>();
			open.add(new Node(x, y));
			Node current = null;
			boolean done = false;
			while(!done){
				
			}
		}
	}
}
