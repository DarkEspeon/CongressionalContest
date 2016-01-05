package net.awesome.game.entities.mobs.ai;

import java.util.Random;

import net.awesome.game.entities.OldEntity;

public abstract class MobAI {
	protected OldEntity controlled;
	protected static Random r = new Random();
	protected int x, y;
	public void setControlled(OldEntity c){
		controlled = c;
	}
	public abstract void runAI();
	public int getX() {int x = this.x; this.x = 0; return x;}
	public int getY() {int y = this.y; this.y = 0; return y;}
}
