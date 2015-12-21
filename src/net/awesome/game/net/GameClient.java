package net.awesome.game.net;

import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import net.awesome.game.Game;
import net.awesome.game.entities.PlayerMP;
import net.awesome.game.net.packets.Packet;
import net.awesome.game.net.packets.Packet.PacketType;
import net.awesome.game.net.packets.Packet00Login;
import net.awesome.game.net.packets.Packet01Disconnect;
import net.awesome.game.net.packets.Packet02Move;
import net.awesome.game.net.packets.Packet03ChangeColor;

public class GameClient extends Thread{
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Game game;
	
	public GameClient(Game game, String ip){
		this.game = game;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
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
			//System.out.println("SERVER > " + new String(dataPacket.getData()));
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketType p = Packet.lookupPacket(message.substring(0, 2));
		Packet packet = null;
		switch(p){
		case LOGIN:
			packet = new Packet00Login(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "]" + ((Packet00Login)packet).getUsername() + "has joined the game");
			this.handleLogin((Packet00Login)packet, address, port);
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "]" + ((Packet01Disconnect)packet).getUsername() + "has left the world");
			game.level.removePlayerMP(((Packet01Disconnect)packet).getUsername());
			break;
		case MOVE:
			packet = new Packet02Move(data);
			this.handleMove((Packet02Move)packet);
			break;
		case CHANGECOLOR:
			packet = new Packet03ChangeColor(data);
			this.handleColorChange((Packet03ChangeColor)packet);
			break;
		case INVALID:
			break;
		default:
			break;
		}
	}

	private void handleColorChange(Packet03ChangeColor packet) {
		PlayerMP player = (PlayerMP) game.level.getPlayers().get(game.level.getPlayerIndex(packet.getPlayer().getUsername()));
		Color[] c = packet.getColorArray();
		
		player.setEyeColor(c[0]);
		player.setHairColor(c[1]);
		player.setShirtColor(c[2]);
		player.setSkinColor(c[3]);
		player.setPantsColor(c[4]);
	}

	private void handleLogin(Packet00Login packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "]" + packet.getUsername() + "has joined the game");
		PlayerMP player = new PlayerMP(game.level, packet.getX(), packet.getY(), packet.getUsername(), address, port);
		Color[] c = packet.getColorArray();
		
		player.setEyeColor(c[0]);
		player.setHairColor(c[1]);
		player.setShirtColor(c[2]);
		player.setSkinColor(c[3]);
		player.setPantsColor(c[4]);

		game.level.addPlayer(player);
	}

	private void handleMove(Packet02Move packet) {
		this.game.level.movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getNumSteps(), packet.isMoving(), packet.getMovingDir());
	}

	public void sendData(byte[] data){
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
