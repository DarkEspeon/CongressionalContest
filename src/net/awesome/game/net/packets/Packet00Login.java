package net.awesome.game.net.packets;

import net.awesome.game.net.GameClient;
import net.awesome.game.net.GameServer;

public class Packet00Login extends Packet {
	
	private String username;
	private int x, y;
	
	public Packet00Login(byte[] data) {
		super(00);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);
	}
	
	public Packet00Login(String username, int x, int y) {
		super(00);
		this.username = username;
		this.x = x;
		this.y = y;
	}
	
	public void writeData(GameClient gc) {
		gc.sendData(readData());
	}

	public void writeData(GameServer gs) {
		gs.sendDataToAllClients(readData());
	}

	public byte[] readData() {
		return ("00" + this.username + "," + this.x + "," + this.y).getBytes();
		
	}
	
	public String getUsername() {return username;}
	public int getX() {return this.x;}
	public int getY() {return this.y;}
}