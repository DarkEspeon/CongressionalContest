package net.awesome.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	
	public class Key{
		private boolean isPressed = false;
		public boolean isPressed(){return isPressed;}
		public void toggle(boolean pressed){
			isPressed = pressed;
		}
	}
	public class Mouse{
		public boolean LMB = false;
		public boolean MMB = false;
		public boolean RMB = false;
		public int x = 0;
		public int y = 0;
		public int mouseScroll = 0;
		public void update(boolean LMB, boolean MMB, boolean RMB, int x, int y, int mS){
			this.LMB = LMB;
			this.MMB = MMB;
			this.RMB = RMB;
			this.x = x;
			this.y = y;
			this.mouseScroll = mS;
		}
	}
	
	public InputHandler(Game game){
		game.addKeyListener(this);
	}
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key esc = new Key();
	public Mouse mouse = new Mouse();
	
	public void keyPressed(KeyEvent arg0) {
		toggle(arg0.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent arg0) {
		toggle(arg0.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent arg0) {
		
	}
	
	public void toggle(int keycode, boolean pressed){
		if(keycode == KeyEvent.VK_W || keycode == KeyEvent.VK_UP) up.toggle(pressed);
		if(keycode == KeyEvent.VK_S || keycode == KeyEvent.VK_DOWN) down.toggle(pressed);
		if(keycode == KeyEvent.VK_A || keycode == KeyEvent.VK_LEFT) left.toggle(pressed);
		if(keycode == KeyEvent.VK_D || keycode == KeyEvent.VK_RIGHT) right.toggle(pressed);
		if(keycode == KeyEvent.VK_ESCAPE) esc.toggle(pressed);
	}
	public void update(MouseEvent me){
		mouse.update(me.getButton() == MouseEvent.BUTTON1, me.getButton() == MouseEvent.BUTTON2, me.getButton() == MouseEvent.BUTTON3, me.getX(), me.getY(), 0);
	}
	public void update(MouseWheelEvent mwe){
		mouse.update(mwe.getButton() == MouseWheelEvent.BUTTON1, mwe.getButton() == MouseWheelEvent.BUTTON2, mwe.getButton() == MouseWheelEvent.BUTTON3, mwe.getX(), mwe.getY(), mwe.getScrollAmount());
	}
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		update(arg0);
	}

	public void mouseDragged(MouseEvent arg0) {
		update(arg0);
	}

	public void mouseMoved(MouseEvent arg0) {
		update(arg0);
	}

	public void mouseClicked(MouseEvent arg0) {
		update(arg0);
	}

	public void mouseEntered(MouseEvent arg0) {
		update(arg0);
	}

	public void mouseExited(MouseEvent arg0) {
		update(arg0);
	}

	public void mousePressed(MouseEvent arg0) {
		update(arg0);
	}

	public void mouseReleased(MouseEvent arg0) {
		update(arg0);
	}
}
