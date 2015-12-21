package net.awesome.game;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

public abstract class DarkAction implements Action{

	@Override
	public abstract void actionPerformed(ActionEvent e);
	public void addPropertyChangeListener(PropertyChangeListener listener) {
	}
	public Object getValue(String key) {
		return null;
	}
	public boolean isEnabled() {
		return false;
	}
	public void putValue(String key, Object value) {
	}
	public void removePropertyChangeListener(PropertyChangeListener listener) {
	}
	public void setEnabled(boolean b) {
	}
}
