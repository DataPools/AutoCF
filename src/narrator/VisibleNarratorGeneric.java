package narrator;

//This narrator does absolutely nothing and is a dummy until linux/windows support is added

public class VisibleNarratorGeneric implements VisibleNarrator {

	@Override
	public void say(String paramString) {
		
	}

	@Override
	public boolean isSpeaking() {
		return false;
	}

}
