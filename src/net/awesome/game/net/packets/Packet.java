package net.awesome.game.net.packets;

import net.awesome.game.net.GameClient;
import net.awesome.game.net.GameServer;

public abstract class Packet {
	public static enum PacketType {
		INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02), CHANGECOLOR(03);
		private int packetID;
		PacketType(int packetID){
			this.packetID = packetID;
		}
		public int getID() {return packetID;}
	}
	public byte packetid;
	
	public Packet(int packetid){
		this.packetid = (byte) packetid;
	}
	
	public abstract void writeData(GameClient gc);
	public abstract void writeData(GameServer gs);
	public abstract byte[] readData();
	
	public String readData(byte[] data){
		String message = new String(data).trim();
		return message.substring(2);
	}
	
	public static PacketType lookupPacket(String packet){
		try {
			return lookupPacket(Integer.parseInt(packet));
		} catch (NumberFormatException e){
			return PacketType.INVALID;
		}
	}
	
	public static PacketType lookupPacket(int id){
		for(PacketType p : PacketType.values()){
			if(p.getID() == id) return p;
		}
		return PacketType.INVALID;
	}
	
}
