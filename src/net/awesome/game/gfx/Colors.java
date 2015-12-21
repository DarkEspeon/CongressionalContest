package net.awesome.game.gfx;


public class Colors {
	private int[] SSCol = new int[255];
	private int[] RCol = new int[255];
	
	/*public static int get(int color1, int color2, int color3, int color4){
		return (get(color4) << 24) + (get(color3) << 16) + (get(color2) << 8) + get(color1);
	}

	private static int get(int color) {
		if(0 > color) return 255;
		int r = (color / 100) % 10;
		int g = (color / 10) % 10;
		int b = (color) % 10;
		return r * 36 + g * 6 + b;
	}*/
	
	public Colors(int[] c1, int[] c2){
		System.arraycopy(c1, 0, SSCol, 0, c1.length);
		System.arraycopy(c2, 0, RCol, 0, c2.length);
	}
	
	public int getColor(int pixelCol){
		pixelCol = 0x00FFFFFF & pixelCol;
		for(int i = 0; i < SSCol.length; i++){
			if (pixelCol == SSCol[i]){
				return RCol[i];
			}
		}
		return 16711935;
	}
}
