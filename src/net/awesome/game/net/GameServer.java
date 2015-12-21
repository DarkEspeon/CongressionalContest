package net.awesome.game.net;

import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import net.awesome.game.Game;
import net.awesome.game.entities.PlayerMP;
import net.awesome.game.net.packets.Packet;
import net.awesome.game.net.packets.Packet.PacketType;
import net.awesome.game.net.packets.Packet00Login;
import net.awesome.game.net.packets.Packet01Disconnect;
import net.awesome.game.net.packets.Packet02Move;
import net.awesome.game.net.packets.Packet03ChangeColor;

public class GameServer extends Thread{
	private DatagramSocket socket;
	private Game game;
	private List<PlayerMP> connectedPlayers = new ArrayList<>();
	
	public GameServer(Game game){
		this.game = game;
		try {
			this.socket = new DatagramSocket(1331);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(true){
			byte[] data = new byte[1024];
			DatagramPacket dataPacket = new DatagramPacket(data, data.length);
			try {
				socket.receive(dataPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			parsePacket(dataPacket.getData(), dataPacket.getAddress(), dataPacket.getPort());
//			String message = new String(dataPacket.getData());
//			if(message.trim().equalsIgnoreCase("ping")){
//				System.out.println("CLIENT > " + message);
//				sendData("pong".getBytes(), dataPacket.getAddress(), dataPacket.getPort());
			}
		}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketType p = Packet.lookupPacket(message.substring(0, 2));
		Packet packet = null;
		switch(p){
		case LOGIN:
			packet = new Packet00Login(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "]" + ((Packet00Login)packet).getUsername() + " has connected");
			PlayerMP player = new PlayerMP(game.level, 100, 100, ((Packet00Login)packet).getUsername(), address, port);;
			this.addConnection(player, (Packet00Login)packet);
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "]" + ((Packet01Disconnect)packet).getUsername() + " has disconnected");
			this.removeConnection((Packet01Disconnect)packet);
			break;
		case MOVE:
			packet = new Packet02Move(data);
			this.handleMove((Packet02Move) packet);
			break;
		case CHANGECOLOR:
			packet = new Packet03ChangeColor(data);
			handleColorChange((Packet03ChangeColor) packet);
			break;
		case INVALID:
			break;
		default:
			break;
		}
	}

	private void handleColorChange(Packet03ChangeColor packet) {
		PlayerMP player = (PlayerMP) packet.getPlayer();
		Color[] c = packet.getColorArray();
		if(player == null){
			System.out.println("NULL PLAYER");
		}
		player.setEyeColor(c[0]);
		player.setHairColor(c[1]);
		player.setShirtColor(c[2]);
		player.setSkinColor(c[3]);
		player.setPantsColor(c[4]);
		
		packet.writeData(this);
	}
	
	private void handleMove(Packet02Move packet) {
		if(getPlayer(packet.getUsername()) != null){
			PlayerMP player = getPlayer(packet.getUsername());
			player.x = packet.getX();
			player.y = packet.getY();
			player.setMoving(packet.isMoving());
			player.setNumSteps(packet.getNumSteps());
			player.setMovingDir(packet.getMovingDir());
			packet.writeData(this);
		}
	}

	public void addConnection(PlayerMP player, Packet00Login packet) {
		boolean alreadyConnected = false;
		for(PlayerMP p : this.connectedPlayers){
			if(player.getUsername().equalsIgnoreCase(p.getUsername())){
				if(p.ipAddress == null){
					p.ipAddress = player.ipAddress;
				}
				if(p.port == -1){
					p.port = player.port;
				}
				alreadyConnected = true;
			} else {
				sendData(packet.readData(), p.ipAddress, p.port);
				Packet00Login packetNew = new Packet00Login(p.getUsername(), p.x, p.y);
				sendData(packetNew.readData(), player.ipAddress, player.port);
				Packet03ChangeColor ccpacket = new Packet03ChangeColor(p, new Color[]{p.getEyeColor(), p.getHairColor(), p.getShirtColor(), p.getSkinColor(), p.getPantsColor()});
				sendData(ccpacket.readData(), player.ipAddress, player.port);
			}
		}
		if(!alreadyConnected){
			connectedPlayers.add(player);
		}
	}
	
	public void removeConnection(Packet01Disconnect packet) {
		this.connectedPlayers.remove(getPlayerIndex(packet.getUsername()));
		packet.writeData(this);
	}
	
	public PlayerMP getPlayer(String username){
		for(PlayerMP player : connectedPlayers){
			if(player.getUsername().equals(username)){
				return player;
			}
		}
		return null;
	}
	
	public int getPlayerIndex(String username){
		for(int index = 0; index < connectedPlayers.size(); index++){
			if(connectedPlayers.get(index).getUsername().equals(username)) return index;
		}
		return 0;
	}
	
	public void sendData(byte[] data, InetAddress ipAddress, int port){
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] readData) {
		for(PlayerMP p : connectedPlayers){
			sendData(readData, p.ipAddress, p.port);
		}
	}
	
}
