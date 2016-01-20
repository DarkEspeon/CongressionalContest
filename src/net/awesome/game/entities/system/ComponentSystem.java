package net.awesome.game.entities.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.awesome.game.collections.ArrayMap;
import net.awesome.game.collections.IMap;
import net.awesome.game.entities.Entity;
import net.awesome.game.entities.EntitySystem;
import net.awesome.game.entities.components.Component;

public abstract class ComponentSystem {
	public static boolean timeSystems = false;
	private List<Class<? extends Component>> requiredComponents = new ArrayList<>();
	private List<Class<? extends Component>> optionalComponents = new ArrayList<>();
	private List<Class<? extends Component>> bannedComponents = new ArrayList<>();
	protected IMap<Class<? extends Component>, Component> requiredComponentRef = new ArrayMap<>();
	protected IMap<Class<? extends Component>, Component> optionalComponentRef = new ArrayMap<>();
	protected EntitySystem es;
	Entity e = null;
	private Iterator<Entity> EntityIter = null;
	public ComponentSystem(EntitySystem es){
		this.es = es;
	}
	protected Iterator<Entity> iterator() {
		return new EntityIterator(es);
	}
	public void addRequired(Class<? extends Component> clss){ requiredComponents.add(clss); requiredComponentRef.add(clss, null); }
	public void addOptional(Class<? extends Component> clss){ optionalComponents.add(clss); optionalComponentRef.add(clss, null); }
	public void addBanned(Class<? extends Component> clss){ bannedComponents.add(clss); }
	
	public <T extends Component> T getRequired(Class<T> clss){ return (T)requiredComponentRef.get(clss); }
	public <T extends Component> T getOptional(Class<T> clss){ return (T)optionalComponentRef.get(clss); }
	
	public List<Entity> getEntitiesWith(Class<? extends Component>... clss){
		List<Entity> resList = new ArrayList<>();
		EntityLoop: for(Entity e : es.getEntities().values()){
			if(e.isProcessing()) continue;
			for(Class<? extends Component> cl : clss){
				if(!e.hasComponent(cl)) continue EntityLoop;
			}
			resList.add(e);
		}
		return resList;
	}
	
	private boolean NextEntity(){
		if(EntityIter == null) EntityIter = iterator();
		e = EntityIter.next();
		if (!EntityIter.hasNext() && e == null){ EntityIter = null; return false; }
		for(Class<? extends Component> reqC : requiredComponents){
			requiredComponentRef.add(reqC, e.getComponent(reqC));
		}
		for(Class<? extends Component> optC : optionalComponents){
			if(e.hasComponent(optC)) optionalComponentRef.add(optC, e.getComponent(optC));
			else optionalComponentRef.add(optC, null);
		}
		e.setProcessing(true);
		return true;
	}
	
	public void process(float delta){
		if(requiredComponents.size() == 0) throw new RuntimeException("System " + getClass().getSimpleName() + " class was called to process, but doesn't have any required components!");
		else {
			long time = 0;
			if(timeSystems)
				time = System.currentTimeMillis();
			while(NextEntity()){
				onProcess(delta);
				if(e != null) e.setProcessing(false);
			}
			if(timeSystems){
				System.out.println(getClass().getSimpleName() + ": Took " + (System.currentTimeMillis() - time) + " ms");
			}
		}
	}
	public abstract void onProcess(float delta);
	
	protected class EntityIterator implements Iterator<Entity>{
		private List<Entity> matches = new ArrayList<Entity>();
		private int index = 0;
		public EntityIterator(EntitySystem es){
			EntityLoop: for(Entity e : es.getEntities().values()){
				for(Class<? extends Component> banC : bannedComponents){
					if(e.hasComponent(banC)) continue EntityLoop;
				}
				for(Class<? extends Component> clss : requiredComponents) {
					if(e.hasComponent(clss)) continue;
					else continue EntityLoop;
				}
				matches.add(e);
			}
		}
		public boolean hasNext() {
			return matches != null && index < matches.size();
		}
		public Entity next() {
			return (index < matches.size()) ? matches.get(index++) : null;
		}
		
	}
}
