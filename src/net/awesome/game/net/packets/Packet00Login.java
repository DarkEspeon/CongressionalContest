package net.awesome.game.net.packets;

import java.awt.Color;

import net.awesome.game.net.GameClient;
import net.awesome.game.net.GameServer;

public class Packet00Login extends Packet {
	
	private String username;
	private int x, y;
	private Color[] colors = new Color[5];
	
	public Packet00Login(byte[] data) {
		super(00);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);

		colors[0] = (dataArray[3] != "") ? new Color(Integer.parseInt(dataArray[3])) : new Color(0,0,0);
		colors[1] = (dataArray[4] != "") ? new Color(Integer.parseInt(dataArray[4])) : new Color(0,0,0);
		colors[2] = (dataArray[5] != "") ? new Color(Integer.parseInt(dataArray[5])) : new Color(0,0,0);
		colors[3] = (dataArray[6] != "") ? new Color(Integer.parseInt(dataArray[6])) : new Color(0,0,0);
		colors[4] = (dataArray[7] != "") ? new Color(Integer.parseInt(dataArray[7])) : new Color(0,0,0);
	}
	
	public Packet00Login(String username, int x, int y, Color[] c) {
		super(00);
		this.username = username;
		this.x = x;
		this.y = y;
		this.colors = c;
	}
	
	public void writeData(GameClient gc) {
		gc.sendData(readData());
	}

	public void writeData(GameServer gs) {
		gs.sendDataToAllClients(readData());
	}

	public byte[] readData() {
		return ("00" + this.username + "," + this.x + "," + this.y + "," + colors[0].getRGB() + "," + colors[1].getRGB() + "," + colors[2].getRGB() + "," + colors[3].getRGB() + "," + colors[4].getRGB()).getBytes();
		
	}
	
	public String getUsername() {return username;}
	public int getX() {return this.x;}
	public int getY() {return this.y;}
	public Color[] getColorArray(){ return colors; }
}
