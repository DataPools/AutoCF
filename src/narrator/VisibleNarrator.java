package narrator;

import java.util.Locale;

public interface VisibleNarrator {
  void say(String paramString);
  
  boolean isSpeaking();
  
  static VisibleNarrator getNarrator() {
    String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
    if (osName.contains("mac")) {
      setJNAPath(":");
      return new VisibleNarratorOSX();
    }
    else {
       System.out.println("ERROR: Narrator is not supported on this platform.");
       return new VisibleNarratorGeneric();
    }
  }
  
  static void setJNAPath(String sep) {
    System.setProperty("jna.library.path", System.getProperty("jna.library.path") + sep + "./src/natives/resources/");
    System.setProperty("jna.library.path", System.getProperty("jna.library.path") + sep + System.getProperty("java.library.path"));
  }
}
