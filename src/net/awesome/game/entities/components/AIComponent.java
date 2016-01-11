package net.awesome.game.entities.components;

import net.awesome.game.AI.AI;

public class AIComponent implements Component{
	public AI aiModule;
	public int x, y;
	public int agroDistance = 10;
	public float time = 0;
	public AIComponent(AI aiModule){
		this.aiModule = aiModule;
	}
	public AIComponent(AI aiModule, int agroDistance){
		this.aiModule = aiModule;
		this.agroDistance = agroDistance;
	}
}
