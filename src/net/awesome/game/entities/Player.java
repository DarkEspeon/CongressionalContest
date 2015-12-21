package net.awesome.game.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import net.awesome.game.Game;
import net.awesome.game.InputHandler;
import net.awesome.game.classes.Class;
import net.awesome.game.gfx.Font;
import net.awesome.game.gfx.Screen;
import net.awesome.game.level.Level;

public class Player extends Mob {
	
	private InputHandler input;
	private int scale = 1;
	protected boolean isSwimming = false;
	private int tickCount = 0;
	private String username;
	private Dictionary<String, Color> charColors = new Hashtable<String, Color>();
	
	public Player(Level level, int x, int y, InputHandler input, String username) {
		super(level, "player", x, y, 1);
		this.input = input;
		this.username = username;
		charColors.put("EyeColor", new Color(-6325839));
		charColors.put("HairColor", new Color(-3688873));
		charColors.put("ShirtColor", new Color(-16711681));
		charColors.put("PantsColor", new Color(-65536));
		charColors.put("SkinColor", new Color(-863813));
	}

	public void tick() {
		tickCount++;
		int xa = 0;
		int ya = 0;
		if(input != null){
			if(input.up.isPressed()){ya--;}
			if(input.down.isPressed()){ya++;}
			if(input.left.isPressed()){xa--;}
			if(input.right.isPressed()){xa++;}
		}
		if(xa != 0 || ya != 0){
			move(xa, ya);
			isMoving = true;
			
		}
		else {
			isMoving = false;
		}
		if(level.getTile((this.x >> 3), (this.y >> 3)).getID() == 3) isSwimming = true;
		else isSwimming = false;
	}
	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 28;
		int walkingSpeed = 3;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBot = (numSteps >> walkingSpeed) & 1;
		
		if(movingDir == 1){
			xTile += 2;
		} else if(movingDir > 1){
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
		}
		
		int modifier = 8 * scale;
		int xOffset = x - modifier / 2;
		int yOffset = (y - modifier / 2) - 4;
		
		if(isSwimming){
			int xFrame = 0;
			yOffset += 4;
			if(tickCount % 60 < 15){
				xFrame = 0;
			} else if(tickCount % 60 >= 15 && tickCount % 60 < 30){
				yOffset -= 1;
				xFrame = 1;
			} else if(tickCount % 60 >= 30 && tickCount % 60 < 45){
				xFrame = 2;
			} else if(tickCount % 60 >= 45){
				xFrame = 1;
			}
			screen.render(xOffset, yOffset + 3,xFrame + 27 * 32, 0, 1);
			screen.render(xOffset + 8, yOffset + 3,xFrame + 27 * 32, 0x01, 1);
		}
		
		screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, flipTop, scale, this);
		screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, flipTop, scale, this);
		if(!isSwimming){
			screen.render(xOffset + (modifier * flipBot), yOffset + modifier, xTile + (yTile + 1) * 32, flipBot, scale, this);
			screen.render(xOffset + modifier - (modifier * flipBot), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, flipBot, scale, this);
		}
		if(username != null){
			Font.render(username, screen, xOffset - ((username.length() - 1) / 2 * 8), yOffset - 10, 1);
		}
	}
	public void render(Graphics g, int x, int y){
		BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
		try {
			image = ImageIO.read(Player.class.getResourceAsStream("/Player.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int tx = 0; tx < image.getWidth(); tx++){
			for(int ty = 0; ty < image.getHeight(); ty++){
				if(image.getRGB(tx, ty) == 0xFF3C3C3C){
					image.setRGB(tx, ty, this.getHairColor().getRGB());
				} else if(image.getRGB(tx, ty) == 0xFF5A5A5A){
					image.setRGB(tx, ty, this.getSkinColor().getRGB());
				} else if(image.getRGB(tx, ty) == 0xFF787878){
					image.setRGB(tx, ty, this.getEyeColor().getRGB());
				} else if(image.getRGB(tx, ty) == 0xFF969696){
					image.setRGB(tx, ty, this.getShirtColor().getRGB());
				} else if(image.getRGB(tx, ty) == 0xFFB4B4B4){
					image.setRGB(tx, ty, this.getPantsColor().getRGB());
				} else if(image.getRGB(tx, ty) == 0xFFFF00FF){
					image.setRGB(tx, ty, 0x00000000);
				}
			}
		}
		g.drawImage(image, x, y, image.getWidth() * 4, image.getHeight() * 4, null);
	}
	public boolean hasCollided(int xa, int ya) {
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;
		for(int x = xMin; x < xMax; x++){
			if(isSolidTile(xa, ya, x, yMin)) return true;
		}
		for(int x = xMin; x < xMax; x++){
			if(isSolidTile(xa, ya, x, yMax)) return true;
		}
		for(int y = yMin; y < yMax; y++){
			if(isSolidTile(xa, ya, xMin, y)) return true;
		}
		for(int y = yMin; y < yMax; y++){
			if(isSolidTile(xa, ya, xMax, y)) return true;
		}
		return false;
	}
	public String getUsername() {return username;}
	public Color getHairColor() {return charColors.get("HairColor");}
	public Color getEyeColor() {return charColors.get("EyeColor");}
	public Color getShirtColor() {return charColors.get("ShirtColor");}
	public Color getPantsColor() {return charColors.get("PantsColor");}
	public Color getSkinColor() {return charColors.get("SkinColor");}
	
	public void setHairColor(Color c) {charColors.put("HairColor", c);}
	public void setEyeColor(Color c) {charColors.put("EyeColor", c);}
	public void setShirtColor(Color c) {charColors.put("ShirtColor", c);}
	public void setPantsColor(Color c) {charColors.put("PantsColor", c);}
	public void setSkinColor(Color c) {charColors.put("SkinColor", c);}
}
