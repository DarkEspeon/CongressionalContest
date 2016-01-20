package net.awesome.game.entities.system;

import net.awesome.game.entities.EntitySystem;
import net.awesome.game.entities.components.CrystalComponent;
import net.awesome.game.entities.components.RenderComponent;

public class CrystalSystem extends ComponentSystem {

	public CrystalSystem(EntitySystem es) {
		super(es);
		addRequired(CrystalComponent.class);
		addRequired(RenderComponent.class);
	}

	public void onProcess(float delta) {
		RenderComponent rc = getRequired(RenderComponent.class);
		rc.tickCount += delta;
	}

}
