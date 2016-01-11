package net.awesome.game;

public final class Timer {
	private static long startTime = 0;
	private static boolean started = false;
	private static Object lockObject = new Object();
	private Timer(){}
	public static void startTimer(){
		if(started){
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			startTime = System.currentTimeMillis();
			started = true;
		}
	}
	public static void stopTimer(String timed){
		if(!started) return;
		else {
			System.out.println("[" + timed + "]: took " + (System.currentTimeMillis() - startTime) + " ms");
			startTime = 0;
			started = false;
			synchronized(lockObject){
				lockObject.notifyAll();
			}
		}
	}
}
