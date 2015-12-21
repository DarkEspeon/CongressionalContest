package net.awesome.game.entities;

import java.net.InetAddress;
import net.awesome.game.classes.Class;
import net.awesome.game.InputHandler;
import net.awesome.game.level.Level;

public class PlayerMP extends Player {

	public InetAddress ipAddress;
	public int port;
	
	public PlayerMP(Level level, int x, int y, InputHandler input, String username, InetAddress ipAddress, int port) {
		super(level, x, y, input, username, Class.warrior);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public PlayerMP(Level level, int x, int y, String username, InetAddress ipAddress, int port) {
		super(level, x, y, null, username, Class.warrior);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public void tick(){
		super.tick();
	}

}
