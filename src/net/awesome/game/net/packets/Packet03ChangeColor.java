package net.awesome.game.net.packets;

import java.awt.Color;

import net.awesome.game.Game;
import net.awesome.game.entities.Entity;
import net.awesome.game.entities.Player;
import net.awesome.game.entities.PlayerMP;
import net.awesome.game.net.GameClient;
import net.awesome.game.net.GameServer;

public class Packet03ChangeColor extends Packet {
	
	private Player p;
	private Color[] colors = new Color[5];
	
	public Packet03ChangeColor(byte[] data) {
		super(03);
		String[] dataArray = readData(data).split(",");
		for(Entity e : Game.game.level.getPlayers()){
			if(e != null && e instanceof PlayerMP && ((PlayerMP)e).getUsername().equals(dataArray[0])){
				p = (PlayerMP)e;
			}
		}
		colors[0] = (dataArray[1] != "") ? new Color(Integer.parseInt(dataArray[1])) : new Color(0,0,0);
		colors[1] = (dataArray[2] != "") ? new Color(Integer.parseInt(dataArray[2])) : new Color(0,0,0);
		colors[2] = (dataArray[3] != "") ? new Color(Integer.parseInt(dataArray[3])) : new Color(0,0,0);
		colors[3] = (dataArray[4] != "") ? new Color(Integer.parseInt(dataArray[4])) : new Color(0,0,0);
		colors[4] = (dataArray[5] != "") ? new Color(Integer.parseInt(dataArray[5])) : new Color(0,0,0);
	}
	
	public Packet03ChangeColor(Player player, Color[] c) {
		super(03);
		p = player;
		colors = c;
	}
	
	public void writeData(GameClient gc) {
		gc.sendData(readData());
	}

	public void writeData(GameServer gs) {
		gs.sendDataToAllClients(readData());
	}

	public byte[] readData() {
		return ("03" + p.getUsername() + "," + colors[0].getRGB() + "," + colors[1].getRGB() + "," + colors[2].getRGB() + "," + colors[3].getRGB() + "," + colors[4].getRGB()).getBytes();
	}
	public Player getPlayer(){return p;}
	public Color[] getColorArray(){return colors;}
}
