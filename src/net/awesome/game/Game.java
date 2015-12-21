package net.awesome.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.awesome.game.entities.Player;
import net.awesome.game.entities.PlayerMP;
import net.awesome.game.entities.mobs.BasicMob;
import net.awesome.game.gfx.Screen;
import net.awesome.game.gfx.SpriteSheet;
import net.awesome.game.gui.ImageButton;
import net.awesome.game.level.Level;
import net.awesome.game.net.GameClient;
import net.awesome.game.net.GameServer;
import net.awesome.game.net.packets.Packet00Login;
import net.awesome.game.net.packets.Packet03ChangeColor;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 160;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 4;
	public static final String TITLE = "Game";
	
	public static Game game;
	
	public JFrame frame;
	public ImageButton pauseMenu1;
	public ImageButton pauseMenu2;
	public ImageButton pauseMenu3;
	public ImageButton colorMenu1;
	public ImageButton colorMenu2;
	public JSlider r;
	public JSlider g;
	public JSlider b;
	//public JColorChooser JCC;
	public JComboBox<String> ColorOptions;
	public boolean cboxsliders = false;
	
	public boolean running = false;
	public int tickCount = 0;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	private SpriteSheet spriteSheet = new SpriteSheet("/sprite_sheet.png");
	
	private Screen screen;
	public static InputHandler input;
	public WindowHandler window;
	public Level level;
	public Player player;
	public Player fakePlayer = new Player(null, 0, 0, null, "Faker", null);
	
	public static HashMap<String, BasicMob> mobTemplates = new HashMap<>();
	
	public GameClient client;
	public GameServer server = null;
	public boolean escButtonReleased = true;
	public GameState gs;
	
	public Game(){
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		frame = new JFrame(TITLE);
		
		Icon i = new ImageIcon(PreLoadTransparancy("/gui/ResumeBtn.png", 1));
		
		pauseMenu1 = new ImageButton((WIDTH * SCALE) / 2 - 128, 50, 256, 64);
		pauseMenu2 = new ImageButton((WIDTH * SCALE) / 2 - 128, HEIGHT * SCALE - 64 - 50, 256, 64);
		pauseMenu3 = new ImageButton((WIDTH * SCALE) / 2 - 128, HEIGHT * SCALE - 128 - 50, 256, 64);
		
		colorMenu1 = new ImageButton((WIDTH * SCALE) - 306, HEIGHT * SCALE - 64 - 50, 256, 64);
		colorMenu2 = new ImageButton((WIDTH * SCALE) - 306, HEIGHT * SCALE - 64 - 150, 256, 64);
		//JCC = new JColorChooser();
		r = new JSlider(0, 255, 5);
		g = new JSlider(0, 255, 5);
		b = new JSlider(0, 255, 5);
		ColorOptions = new JComboBox<>(new String[]{"EyeColor", "HairColor", "ShirtColor", "PantsColor", "SkinColor"});
		
		
		pauseMenu1.setAction(new DarkAction(){
			public void actionPerformed(ActionEvent arg0) {
				game.gs = GameState.Playing;
				toggleMenuButtons(false);
				game.requestFocus();
			}});
		pauseMenu1.setText("Resume Game");
		
		pauseMenu2.setAction(new DarkAction(){
			public void actionPerformed(ActionEvent arg0){
				game.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		pauseMenu2.setText("Exit Game");
		
		pauseMenu3.setAction(new DarkAction(){
			public void actionPerformed(ActionEvent arg0){
				game.gs = GameState.ColorOptions;
				toggleMenuButtons(false);
				toggleColorOptions(true);
			}
		});
		pauseMenu3.setText("Character Colors");
		
		colorMenu1.setAction(new DarkAction(){
			public void actionPerformed(ActionEvent arg0){
				game.gs = GameState.Paused;
				toggleMenuButtons(true);
				toggleColorOptions(false);
			}
		});
		colorMenu1.setText("Back");
		
		colorMenu2.setAction(new DarkAction(){
			public void actionPerformed(ActionEvent e) {
				game.handleColorAccept();
				game.gs = GameState.Playing;
				toggleMenuButtons(false);
				toggleColorOptions(false);
				game.requestFocus();
			}
		});
		colorMenu2.setText("Accept");
		
		r.setSize(256, 32);
		r.setLocation(50, 50);
		r.setMajorTickSpacing(25);
		r.setMinorTickSpacing(5);
		r.setPaintTicks(true);
		r.setValue(fakePlayer.getEyeColor().getRed());
		
		g.setSize(256, 32);
		g.setLocation(50, 100);
		g.setMajorTickSpacing(25);
		g.setMinorTickSpacing(5);
		g.setPaintTicks(true);
		g.setValue(fakePlayer.getEyeColor().getGreen());
		
		b.setSize(256, 32);
		b.setLocation(50, 150);
		b.setMajorTickSpacing(25);
		b.setMinorTickSpacing(5);
		b.setPaintTicks(true);
		b.setValue(fakePlayer.getEyeColor().getBlue());
		
		r.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
				if(!cboxsliders) game.HandleColorSliders();
			}
		});
		g.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
				if(!cboxsliders) game.HandleColorSliders();
			}
		});
		b.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
				if(!cboxsliders) game.HandleColorSliders();
			}
		});
		
		ColorOptions.setSize(128, 32);
		ColorOptions.setLocation(WIDTH * SCALE - 250, 50);
		ColorOptions.setSelectedIndex(0);
		ColorOptions.setEditable(false);
		ColorOptions.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == ItemEvent.SELECTED){
					String choice = (String)arg0.getItem();
					cboxsliders = true;
					switch(choice){
					case "EyeColor":
						game.r.setValue(fakePlayer.getEyeColor().getRed());
						game.g.setValue(fakePlayer.getEyeColor().getGreen());
						game.b.setValue(fakePlayer.getEyeColor().getBlue());
						break;
					case "HairColor":
						game.r.setValue(fakePlayer.getHairColor().getRed());
						game.g.setValue(fakePlayer.getHairColor().getGreen());
						game.b.setValue(fakePlayer.getHairColor().getBlue());
						break;
					case "ShirtColor":
						game.r.setValue(fakePlayer.getShirtColor().getRed());
						game.g.setValue(fakePlayer.getShirtColor().getGreen());
						game.b.setValue(fakePlayer.getShirtColor().getBlue());
						break;
					case "PantsColor":
						game.r.setValue(fakePlayer.getPantsColor().getRed());
						game.g.setValue(fakePlayer.getPantsColor().getGreen());
						game.b.setValue(fakePlayer.getPantsColor().getBlue());
						break;
					case "SkinColor":
						game.r.setValue(fakePlayer.getSkinColor().getRed());
						game.g.setValue(fakePlayer.getSkinColor().getGreen());
						game.b.setValue(fakePlayer.getSkinColor().getBlue());
						break;
					}
					game.r.repaint();
					game.g.repaint();
					game.b.repaint();
					cboxsliders = false;
				}
			}
		});
		
		toggleMenuButtons(false);
		toggleColorOptions(false);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(pauseMenu1);
		frame.add(pauseMenu2);
		frame.add(pauseMenu3);
		frame.add(r);
		frame.add(g);
		frame.add(b);
		frame.add(colorMenu1);
		frame.add(colorMenu2);
		frame.add(ColorOptions);
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public static BufferedImage PreLoadTransparancy(String path, int scale){
		BufferedImage newim = new BufferedImage(128 * scale, 32 * scale, BufferedImage.TYPE_INT_ARGB);
		try {
			newim.getGraphics().drawImage(ImageIO.read(Game.class.getResource(path)), 0, 0, 128 * scale, 32 * scale, null);
		} catch (Exception e){
			e.printStackTrace();
		}
		BufferedImage returnim = null;
		if (newim != null) {
			returnim = new BufferedImage(newim.getWidth() * scale, newim.getHeight() * scale, BufferedImage.TYPE_INT_ARGB);
			for(int x = 0; x < newim.getWidth(); x++){
				for(int y = 0; y < newim.getHeight(); y++){
					if(newim.getRGB(x, y) == 0xFFFF00FF){
						newim.setRGB(x, y, 0x00000000);
					}
				}
			}
		}
		returnim.getGraphics().drawImage(newim, 0, 0, returnim.getWidth(), returnim.getHeight(), null);
		return returnim;
	}
	protected void handleColorAccept() {
		int red = r.getValue();
		int green = g.getValue();
		int blue = b.getValue();
		if(red == 255 && green == 0 && blue == 255){
			blue--;
		}
		player.setEyeColor(fakePlayer.getEyeColor());
		player.setHairColor(fakePlayer.getHairColor());
		player.setShirtColor(fakePlayer.getShirtColor());
		player.setSkinColor(fakePlayer.getSkinColor());
		player.setPantsColor(fakePlayer.getPantsColor());
		
		Packet03ChangeColor packet = new Packet03ChangeColor(player, new Color[]{player.getEyeColor(), player.getHairColor(), player.getShirtColor(), player.getSkinColor(), player.getPantsColor()});
		packet.writeData(client);
	}
	protected void HandleColorSliders() {
		int red = r.getValue();
		int green = g.getValue();
		int blue = b.getValue();
		String choice = (String)ColorOptions.getSelectedItem();
		if(red == 255 && green == 0 && blue == 255){
			blue--;
		}
		switch(choice){
		case "EyeColor":
			fakePlayer.setEyeColor(new Color(red, green, blue));
			break;
		case "HairColor":
			fakePlayer.setHairColor(new Color(red, green, blue));
			break;
		case "ShirtColor":
			fakePlayer.setShirtColor(new Color(red, green, blue));
			break;
		case "PantsColor":
			fakePlayer.setPantsColor(new Color(red, green, blue));
			break;
		case "SkinColor":
			fakePlayer.setSkinColor(new Color(red, green, blue));
			break;
		}
	}
	public void init(){
		screen = new Screen(WIDTH, HEIGHT, spriteSheet);
		input = new InputHandler(this);
		window = new WindowHandler(this);
		level = new Level("/level/WaterTest.png");
		player = new PlayerMP(level, 100, 100, input, JOptionPane.showInputDialog("Please input a username"), null, -1);
		level.addPlayer(player);
		//client.sendData("ping".getBytes());
		Packet00Login loginPacket = new Packet00Login(player.getUsername(), player.x, player.y);
		if(server != null){
			server.addConnection((PlayerMP)player, loginPacket);
		}
		loginPacket.writeData(client);
	}
	public synchronized void start() {
		running = true;
		gs = GameState.Playing;
		requestFocus();
		if(JOptionPane.showConfirmDialog(this, "Do you want to run the server?") == 0){
			server = new GameServer(this);
			server.start();
		} else {
			String ip = JOptionPane.showInputDialog("What is the IP of the server?");
			client = new GameClient(this, ip);
		}
		if (client == null){
			client = new GameClient(this, "localhost");
		}
		client.start();
		init();
		new Thread(this, "Game").start();
	}
	public synchronized void stop() {
		running = false;
	}
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000d / 60d;
		int ticks = 0;
		int frames = 0;
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		while(running){
			long now = System.nanoTime();
			delta += ((now - lastTime) / nsPerTick);
			lastTime = now;
			boolean shouldRender = true;
			while(delta >= 1){
				delta--;
				ticks++;
				tick();
				shouldRender = true;
			}
			if(shouldRender){
				frames++;
				render();
			}
			if(System.currentTimeMillis() - lastTimer > 1000){
				lastTimer+= 1000;
				frame.setTitle("FPS: " + frames + ", UPS: " + ticks);
				ticks = 0;
				frames = 0;
			}
		}
	}
	public void tick(){
		tickCount++;
		if(gs == GameState.Playing || level.getPlayers().size() > 1){
			GameHelpers.SpawnMobs();
			level.tick();
		}
		if(input.esc.isPressed() && gs != GameState.Paused && escButtonReleased){
			gs = GameState.Paused;
			escButtonReleased = false;
			toggleMenuButtons(true);
		} else if(!input.esc.isPressed() && (gs == GameState.Paused || gs == GameState.Playing)){
			escButtonReleased = true;
		} else if(input.esc.isPressed() && (gs == GameState.Paused) && escButtonReleased){
			gs = GameState.Playing;
			escButtonReleased = false;
			toggleMenuButtons(false);
		}
	}
	private void toggleMenuButtons(boolean b){
		pauseMenu1.setEnabled(b);
		pauseMenu1.setVisible(b);
		pauseMenu2.setEnabled(b);
		pauseMenu2.setVisible(b);
		pauseMenu3.setEnabled(b);
		pauseMenu3.setVisible(b);
	}
	private void toggleColorOptions(boolean b){
		colorMenu1.setEnabled(b);
		colorMenu1.setVisible(b);
		colorMenu2.setEnabled(b);
		colorMenu2.setVisible(b);
		r.setVisible(b);
		r.setEnabled(b);
		g.setVisible(b);
		g.setEnabled(b);
		this.b.setEnabled(b);
		this.b.setVisible(b);
		//JCC.setEnabled(b);
		//JCC.setVisible(b);
		ColorOptions.setEnabled(b);
		ColorOptions.setVisible(b);
	}
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		
		int xOffset = player.x - (screen.width / 2);
		int yOffset = player.y - (screen.height / 2);
		
		level.renderTiles(screen, xOffset, yOffset);
		level.renderEntities(screen);
		player.render(screen);
		for(int y = 0; y < screen.height; y++){
			for(int x = 0; x < screen.width; x++){
				pixels[x + y * WIDTH] = screen.pixels[x + y * screen.width];
			}
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		if(gs == GameState.Paused){
			g.setColor(new Color(0, 0, 0, 100));
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		if(gs == GameState.ColorOptions){
			g.setColor(new Color(0, 0, 0, 100));
			g.fillRect(0, 0, getWidth(), getHeight());
			fakePlayer.render(g, WIDTH * SCALE - 75, 11);
		}
		g.dispose();
		bs.show();
	}
	public static void main(String[] args){
		game = new Game();
		game.start();
	}
	public enum GameState {
		Playing, Paused, ColorOptions;
	}
}
