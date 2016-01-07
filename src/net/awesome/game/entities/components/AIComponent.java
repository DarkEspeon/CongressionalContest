package net.awesome.game.entities.components;

import net.awesome.game.AI.AI;

public class AIComponent implements Component{
	public AI aiModule;
	public int x, y;
	public int agroDistance = 10;
	public AIComponent(AI aiModule){
		this.aiModule = aiModule;
	}
}
