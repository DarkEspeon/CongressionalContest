package net.awesome.game.entities;

import java.util.HashMap;
import java.util.Map;

import net.awesome.game.entities.components.Component;

public class Entity {
	private boolean processing = false;
	private Map<Class<? extends Component>, Component> components = new HashMap<>();
	public Entity(){}
	
	public boolean isProcessing(){ return processing; }
	public void setProcessing(boolean processing){ this.processing = processing; }
	public boolean hasComponent(Class<? extends Component> comp) { return components.containsKey(comp); }
	public <T extends Component> T getComponent(Class<T> clzz){ return (T)components.get(clzz); }
	public void addComponent(Component comp){ components.put(comp.getClass(), comp); }
	public void removeComponent(Class<? extends Component> comp){ components.remove(comp); }
}