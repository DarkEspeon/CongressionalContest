package net.awesome.game.entities;

import java.util.HashMap;
import java.util.Map;

public class EntitySystem {
	private Map<String, Entity> entities = new HashMap<>();
	public Entity addEntity(String name){ Entity e = new Entity(name); entities.put(name, e); return e; }
	public Map<String, Entity> getEntities(){ return entities; }
	public void removeEntity(String name){ entities.remove(name); }
	public Entity getEntity(String name){ return entities.get(name); }
}
