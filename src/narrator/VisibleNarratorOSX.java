package narrator;

import java.util.LinkedList;
import java.util.Queue;

import ca.weblite.objc.Client;
import ca.weblite.objc.NSObject;
import ca.weblite.objc.Proxy;
import ca.weblite.objc.annotations.Msg;

public class VisibleNarratorOSX extends NSObject implements VisibleNarrator {
  private final Proxy synth = Client.getInstance().sendProxy("NSSpeechSynthesizer", "alloc", new Object[0]);
  
  private boolean speaking;
  
  private Queue<String> speakingQueue = new LinkedList<String>();
  
  public VisibleNarratorOSX() {
    super("NSObject");
	this.synth.send("init", new Object[0]);
	this.synth.send("setDelegate:", new Object[] { this });
	//Daniel is a British voice that sounds pretty good, switch to Alex for American voice
	this.synth.send("setVoice:","com.apple.speech.synthesis.voice.daniel");
  }
  
  private void startSpeaking(String message) {
    this.synth.send("startSpeakingString:", new Object[] { message });
  }
  
  @Msg(selector = "speechSynthesizer:didFinishSpeaking:", signature = "v@:B")
  public void didFinishSpeaking(boolean naturally) {
	  if(speakingQueue.isEmpty()) {
		  speaking = (boolean) this.synth.sendBoolean("isSpeaking");
	  }
	  else {
		  startSpeaking(speakingQueue.poll());
	  }
  }
  
  public boolean isSpeaking() {
	  if(speakingQueue.isEmpty()) {
		  return speaking;
	  }
	  else {
		  return true;
	  }
  }
  
  public void say(String msg) {
	if(msg.length() > 0) {
		if(speakingQueue.isEmpty()) {
	      speaking = true;
	      startSpeaking(msg);
		}
		else {
			speakingQueue.add(msg);
		}
	}
  }
}