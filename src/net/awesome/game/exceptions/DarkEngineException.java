package net.awesome.game.exceptions;

public class DarkEngineException extends Exception {
	private static final long serialVersionUID = 1L;
	public String errName;
	public String errDesc;
	public DarkEngineException(String errName, String errDesc){
		this.errName = errName;
		this.errDesc = errDesc;
	}
	@Override
	public String getMessage(){
		return "[" + errName + "] " + errDesc;
	}
}
