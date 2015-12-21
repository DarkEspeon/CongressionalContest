package net.awesome.game.gui;

import java.awt.Color;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import net.awesome.game.Game;

public class ImageButton extends JButton {
	private static final long serialVersionUID = 1L;
	public ImageButton(int x, int y, int w, int h, String text, String path){
		ImageIcon icon = null;
		try {
			icon = new ImageIcon(ImageIO.read(Game.class.getResource(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(icon != null){
			setIcon(icon);
			setDisabledIcon(icon);
			setRolloverIcon(icon);
			setSize(icon.getIconWidth(), icon.getIconHeight());
		}
		//setBorder(BorderFactory.createEmptyBorder());
		setContentAreaFilled(false);
		setLocation(x, y);
		//setBackground(new Color(140, 111, 33));
	}
	public ImageButton(int x, int y, int w, int h){
		setSize(w, h);
		setLocation(x, y);
		setBackground(new Color(140, 111, 33));
	}
	public void repaint(){
		if(this.getIcon() != null && this.getGraphics() != null) this.getGraphics().drawImage(((ImageIcon)getIcon()).getImage(), getLocation().x, getLocation().y, getSize().width, getSize().height,null);
	}
}
