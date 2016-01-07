package net.awesome.game.entities.components;

import java.util.HashMap;
import java.util.Map;

public class ColorComponent implements Component{
	public Map<Integer, Integer> colorSwaps = new HashMap<>();
	public ColorComponent(int... val){
		for(int i = 0; i < val.length; i += 2){
			colorSwaps.put(val[i], val[i + 1]);
		}
		
	}
}
