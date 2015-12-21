package net.awesome.game.net.packets;

import net.awesome.game.net.GameClient;
import net.awesome.game.net.GameServer;

public class Packet01Disconnect extends Packet {
	
	private String username;
	
	public Packet01Disconnect(byte[] data) {
		super(01);
		this.username = readData(data);
	}
	
	public Packet01Disconnect(String username) {
		super(01);
		this.username = username;
	}
	
	public void writeData(GameClient gc) {
		gc.sendData(readData());
	}

	public void writeData(GameServer gs) {
		gs.sendDataToAllClients(readData());
	}

	public byte[] readData() {
		return ("01" + this.username).getBytes();
		
	}
	
	public String getUsername() {return username;}
}
