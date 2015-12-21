package net.awesome.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.awesome.game.entities.mobs.BasicMob;

public class GameHelpers {
	private static Random r = new Random();
	private static int MobCoolDownTimer = 0;
	private static final int mobCoolDown = 300;
	private static final int mobLimit = 30;
	
	//Mob Type Declarations Here(used in copy constructor)
	public static BasicMob a = new BasicMob(Game.game.level, "", 0, 0, 1, null);
	
	public static void SpawnMobs(){
		if(Game.game.server != null){
			MobCoolDownTimer++;
			if(MobCoolDownTimer >= mobCoolDown){
				List<Vector2d> validSpawnTiles = new ArrayList<>();
				for(int x = 0; x < Game.game.level.width; x++){
					for(int y = 0; y < Game.game.level.height; y++){
						if(Game.game.level.getTile(x, y).isSolid() || (Game.game.level.getTile(x, y).getID() == 3)){
							continue;
						} else {
							validSpawnTiles.add(new Vector2d(x, y));
						}
					}
				}
				for(Vector2d v2d : validSpawnTiles){
					if(r.nextInt(100) < 1 && Game.game.level.getEntities().size() < mobLimit){
						Game.game.level.addEntity(new BasicMob(a, v2d.getX(), v2d.getY(), null));
					}
				}
				MobCoolDownTimer = 0;
			}
		} else {
			return;
		}
	}
}
