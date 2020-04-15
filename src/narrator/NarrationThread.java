package narrator;

public class NarrationThread implements Runnable {
	
	private String toSay;
	private VisibleNarrator n;
	private boolean speaking = false;
	
	public NarrationThread(String toSay, VisibleNarrator n) {
		this.n = n;
		this.toSay = toSay;
	}

	@Override
	public void run() {
		speaking = true;
		n.say(toSay);
		while(n.isSpeaking()) {
			//This is to prevent this from looping too fast, which for some reason will glitch out the narrator
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		speaking = false;
	}
	public boolean isSpeaking() {
		return speaking;
	}

}
