package net.awesome.game.entities.components;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.awesome.game.InputHandler;

public class PlayerComponent implements Component{
	public InputHandler input;
	public Map<String, Color> charColors = new HashMap<>();
	public PlayerComponent(InputHandler input){
		this.input = input;
	}
}
