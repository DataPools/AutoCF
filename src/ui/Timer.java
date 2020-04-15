package ui;

public class Timer {
	private int secondsToWait;
	private long startingTime;
	
	public Timer(int secondsToWait) {
		this.secondsToWait = secondsToWait;
		startingTime = System.currentTimeMillis();
	}

	public boolean isItTime() {
		return ((System.currentTimeMillis()-startingTime)/1000) >= secondsToWait;
	}

}
