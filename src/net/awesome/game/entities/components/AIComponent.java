package net.awesome.game.entities.components;

import net.awesome.game.AI.AI;

public class AIComponent implements Component{
	public AI aiModule;
	public AIComponent(AI aiModule){
		this.aiModule = aiModule;
	}
}
