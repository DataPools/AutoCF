package ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class TextRenderer {
	private UnicodeFont unicodefont;
	
	@SuppressWarnings("unchecked")
	public TextRenderer(int size) {
		try {
			unicodefont = new UnicodeFont("assets/benton-sans-regular.ttf", size, false, false);
			unicodefont.addAsciiGlyphs();
			unicodefont.getEffects().add(new ColorEffect());
			unicodefont.loadGlyphs();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void renderText(String text, Graphics g, float x, float y, Color color) {
		text = sanitizeText(text);
		g.setFont(unicodefont);
		unicodefont.drawString(x, y, text, color);
	}
	private String sanitizeText(String text) {
		//Replace mac apostrophes/quotes because the font cannot render them
		return text.replace("’", "'")
				.replace("“", "\"")
				.replace("”", "\"");
	}
	public int getLineHeight() {
		return unicodefont.getLineHeight();
	}
	public int getLineWidth(String line) {
		return unicodefont.getWidth(line);
	}

}
