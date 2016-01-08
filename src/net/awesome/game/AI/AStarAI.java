package net.awesome.game.AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.awesome.game.Game;
import net.awesome.game.Maths;
import net.awesome.game.Vector2d;
import net.awesome.game.level.tiles.Tile;

public class AStarAI implements AI {
	private Comparator<Node> nodeSorter = new Comparator<Node>(){
		public int compare(Node arg0, Node arg1) {
			if(arg1.fCost < arg0.fCost) return 1;
			if(arg1.fCost > arg0.fCost) return -1;
			return 0;
		}
	};
	List<Node> path = new ArrayList<>();
	public static class Node {
		public Node parent;
		public double fCost, gCost, hCost;
		public final int x, y;
		public Node(int x, int y, Node parent, double gCost, double hCost){
			this.x = x;
			this.y = y;
			this.parent = parent;
			this.gCost = gCost;
			this.hCost = hCost;
			this.fCost = gCost + hCost;
		}
	}
	public int getXA() { return 0; }
	public int getYA() { return 0; }
	public Vector2d getDirection(int curx, int cury){
		int dxa = 0, dya = 0;
		if(path != null){
			if(path.size() > 0){
				int xa = path.get(path.size() - 1).x;
				int ya = path.get(path.size() - 1).y;
				if(curx < xa * (8 * Game.SCALE)) dxa--;
				if(curx > xa * (8 * Game.SCALE)) dxa++;
				if(cury < ya * (8 * Game.SCALE)) dya--;
				if(cury > ya * (8 * Game.SCALE)) dya++;
			}
		}
		return new Vector2d(dxa, dya);
	}
	public void findPath(int x, int y, int goalx, int goaly) {
		System.out.println("AStar Start");
		x /= (8 * Game.SCALE);
		y /= (8 * Game.SCALE);
		goalx /= (8 * Game.SCALE);
		goaly /= (8 * Game.SCALE);
		List<Node> open = new ArrayList<>();
		List<Node> closed = new ArrayList<>();
		Node current = new Node(x, y, null, 0, Maths.distance(x, y, goalx, goaly));
		open.add(current);
		while(open.size() > 0){
			Collections.sort(open, nodeSorter);
			current = open.get(0);
			if(current.x == goalx && current.y == goaly){
				while(current.parent != null){
					path.add(current);
					current = current.parent;
				}
				open.clear();
				closed.clear();
				break;
			}
			open.remove(current);
			closed.add(current);
			for(int i = 0; i < 9; i++){
				if(i == 4) continue;
				int tx = current.x;
				int ty = current.y;
				int xi = (i % 3) - 1;
				int yi = (i / 3) - 1;
				Tile t = Game.currentLevel.getTile(tx + xi, ty + yi);
				if(t == null) continue;
				if(t.isSolid()) continue;
				double gCost = current.gCost + Maths.distance(current.x, current.y, tx + xi, ty + yi);
				double hCost = Maths.distance(tx + xi, ty + yi, goalx, goaly);
				Node node = new Node(tx + xi, ty + yi, current, gCost, hCost);
				if(posInList(closed, tx + xi, ty + yi) && gCost >= current.gCost) continue;
				if(!posInList(open, tx + xi, ty + yi) || gCost < current.gCost) open.add(node);
			}
		}
		closed.clear();
		System.out.println("AStar Finish");
	}
	private boolean posInList(List<Node> nodes, int x, int y){
		for(Node n : nodes){
			if(n.x == x && n.y == y) return true;
		}
		return false;
	}
}
