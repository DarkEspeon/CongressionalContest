package net.awesome.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.awesome.game.entities.Entity;
import net.awesome.game.entities.EntitySystem;
import net.awesome.game.entities.components.CollisionComponent;
import net.awesome.game.entities.components.ColorComponent;
import net.awesome.game.entities.components.CrystalComponent;
import net.awesome.game.entities.components.PositionComponent;
import net.awesome.game.entities.components.RenderComponent;
import net.awesome.game.level.Level;

public class GameHelpers {
	private static Random r = new Random();
	private static int MobCoolDownTimer = 0;
	private static final int mobCoolDown = 300;
	private static final int mobLimit = 30;
	
	//Mob Type Declarations Here(used in copy constructor)
//	public static BasicMob a = new BasicMob(Game.game.level, "", 0, 0, 1, null);
	
	public static void SpawnMobs(){
//			MobCoolDownTimer++;
//			if(MobCoolDownTimer >= mobCoolDown){
//				List<Vector2d> validSpawnTiles = new ArrayList<>();
//				for(int x = 0; x < Game.game.level.width; x++){
//					for(int y = 0; y < Game.game.level.height; y++){
//						if(Game.game.level.getTile(x, y).isSolid() || (Game.game.level.getTile(x, y).getID() == 3)){
//							continue;
//						} else {
//							validSpawnTiles.add(new Vector2d(x, y));
//						}
//					}
//				}
//				for(Vector2d v2d : validSpawnTiles){
//					if(r.nextInt(100) < 1 && Game.game.level.getEntities().size() < mobLimit){
//						Game.game.level.addEntity(new BasicMob(a, v2d.getX(), v2d.getY(), null));
//					}
//				}
//				MobCoolDownTimer = 0;
//		}
	}
	public static List<Vector2d> getSpawnLocations(Level l){
		List<Vector2d> list = new ArrayList<>();
		for(int x = 0; x < l.width; x++){
			for(int y = 0; y < l.height; y++){
				if(l.getTile(x, y).isSolid() || l.getTile(x, y).getID() == 3){
					continue;
				} else list.add(new Vector2d(x, y));
			}
		}
		return list;
	}
	public static void spawnBlueCrystal(EntitySystem es){
		List<Vector2d> spawnLocations = getSpawnLocations(Game.currentLevel);
		Entity crystal = es.addEntity("BlueCrystal");
		crystal.addComponent(new CollisionComponent(0, 8, -6, 12));
		crystal.addComponent(new CrystalComponent());
		crystal.addComponent(new RenderComponent(4, 25));
		Vector2d pos = spawnLocations.get(r.nextInt(spawnLocations.size()));
		crystal.addComponent(new PositionComponent(pos.getX(), pos.getY()));
		crystal.addComponent(new ColorComponent(0xFFC0C0C0, 0xFF0000C0, 0xffaeaeae, 0xff0000ae, 0xff9c9c9c, 0xff00009c, 0xff909090, 0xff000090, 0xff878787, 0xff000087, 0xff767676, 0xff000076));
	}
	public static void spawnRedCrystal(EntitySystem es){
		List<Vector2d> spawnLocations = getSpawnLocations(Game.currentLevel);
		Entity crystal = es.addEntity("RedCrystal");
		crystal.addComponent(new CollisionComponent(0, 8, -6, 12));
		crystal.addComponent(new CrystalComponent());
		crystal.addComponent(new RenderComponent(4, 25));
		Vector2d pos = spawnLocations.get(r.nextInt(spawnLocations.size()));
		crystal.addComponent(new PositionComponent(pos.getX(), pos.getY()));
		crystal.addComponent(new ColorComponent(0xffc0c0c0, 0xffc00000, 0xffaeaeae, 0xffae0000, 0xff9c9c9c, 0xff9c0000, 0xff909090, 0xff900000, 0xff878787, 0xff870000, 0xff767676, 0xff760000));
	}
	public static void spawnGreenCrystal(EntitySystem es){
		List<Vector2d> spawnLocations = getSpawnLocations(Game.currentLevel);
		Entity crystal = es.addEntity("GreenCrystal");
		crystal.addComponent(new CollisionComponent(0, 8, -6, 12));
		crystal.addComponent(new CrystalComponent());
		crystal.addComponent(new RenderComponent(4, 25));
		Vector2d pos = spawnLocations.get(r.nextInt(spawnLocations.size()));
		crystal.addComponent(new PositionComponent(pos.getX(), pos.getY()));
		crystal.addComponent(new ColorComponent(0xffc0c0c0, 0xff00c000, 0xffaeaeae, 0xff00ae00, 0xff9c9c9c, 0xff009c00, 0xff909090, 0xff009000, 0xff878787, 0xff008700, 0xff767676, 0xff007600));
	}
	public static void spawnGoldCrystal(EntitySystem es){
		List<Vector2d> spawnLocations = getSpawnLocations(Game.currentLevel);
		Entity crystal = es.addEntity("GoldCrystal");
		crystal.addComponent(new CollisionComponent(0, 8, -6, 12));
		crystal.addComponent(new CrystalComponent());
		crystal.addComponent(new RenderComponent(4, 25));
		Vector2d pos = spawnLocations.get(r.nextInt(spawnLocations.size()));
		crystal.addComponent(new PositionComponent(pos.getX(), pos.getY()));
		crystal.addComponent(new ColorComponent(0xffc0c0c0, 0xffc0c000, 0xffaeaeae, 0xffaeae00, 0xff9c9c9c, 0xff9c9c00, 0xff909090, 0xff909000, 0xff878787, 0xff878700, 0xff767676, 0xff767600));
	}
	public static void spawnCyanCrystal(EntitySystem es){
		List<Vector2d> spawnLocations = getSpawnLocations(Game.currentLevel);
		Entity crystal = es.addEntity("CyanCrystal");
		crystal.addComponent(new CollisionComponent(0, 8, -6, 12));
		crystal.addComponent(new CrystalComponent());
		crystal.addComponent(new RenderComponent(4, 25));
		Vector2d pos = spawnLocations.get(r.nextInt(spawnLocations.size()));
		crystal.addComponent(new PositionComponent(pos.getX(), pos.getY()));
		crystal.addComponent(new ColorComponent(0xffc0c0c0, 0xff00c0c0, 0xffaeaeae, 0xff00aeae, 0xff9c9c9c, 0xff009c9c, 0xff909090, 0xff009090, 0xff878787, 0xff008787, 0xff767676, 0xff007676));
	}
}
