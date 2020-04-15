package ui;

import java.util.ArrayList;

import org.davidmoten.text.utils.WordWrap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import narrator.NarrationThread;
import narrator.VisibleNarrator;
import reddithandler.RedditThread;

public class RedditThreadRenderer {
	private ArrayList<PostRenderer> postRenderList;
	private PostRenderer currentPost;
	private int index = 0;
	private String title;
	private NarrationThread narratorthread;
	private Timer timer;
	private boolean saidEndingText = false;
	private TextRenderer textrenderer;
	//timeLimit in seconds, currently set at 5 min (300 s)
	private static final int timeLimit = ACFRenderer.config.getVideoLength();
	//Font size for title/ending screen
	private final int fontSize = 30;
	private static int lineHeight;
	//50px is the margin between title text and edge of screen
	private static final int lineWidth = ACFRenderer.gameWidth-50;
	
	public RedditThreadRenderer(RedditThread redditthread) {
		textrenderer = new TextRenderer(fontSize);
		lineHeight = textrenderer.getLineHeight();
		postRenderList = new ArrayList<PostRenderer>();
		while(redditthread.hasNextPost()) {
			postRenderList.add(new PostRenderer(redditthread.getNextPost(), false));
		}
		currentPost = getNextPost();
		title = redditthread.getTitle();
		timer = new Timer(timeLimit);
		sayTitleText();
	}
	
	public PostRenderer getNextPost() {
		PostRenderer postrenderer = postRenderList.get(index);
		index++;
		return postrenderer;
	}
	
	public boolean hasNextPost() {
		return index < postRenderList.size() && !timer.isItTime();
	}
	public void render(Graphics g, float x, float y) {
		if(!titleIsRendered()) {
			renderTitle(g);
		}
		else {
			if(!currentPost.postCompleted()) {
				currentPost.render(g, x, y);
			}
			else if(hasNextPost()) {
				currentPost = getNextPost();
			}
			else {
				renderEnding(g);
			}
		}
	}
	public boolean titleIsRendered() {
		return !narratorthread.isSpeaking();
	}
	private void renderTitle(Graphics g) {
		float yStart = (ACFRenderer.gameHeight/2)-lineHeight;
		String[] wrappedTitle = wrapWords(title);
		float xStart = (ACFRenderer.gameWidth/2)-(textrenderer.getLineWidth(wrappedTitle[0])/2);
		for(int i=0;i<wrappedTitle.length;i++) {
			textrenderer.renderText(wrappedTitle[i], g, xStart, yStart+i*lineHeight, Color.white);
		}
	}
	private void sayTitleText() {
		VisibleNarrator n = VisibleNarrator.getNarrator();
		narratorthread = new NarrationThread(title, n);
		Thread thread = new Thread(narratorthread);
		thread.start();
	}
	private void renderEnding(Graphics g) {
		if(!saidEndingText) {
			sayText("Thanks for watching and remember to subscribe for more reddit videos.");
			saidEndingText = true;
		}
		float yStart = ACFRenderer.gameHeight/2-lineHeight;
		float xStart = (ACFRenderer.gameWidth/2)-(textrenderer.getLineWidth("Thanks for watching!")/2);
		textrenderer.renderText("Thanks for watching!", g, xStart, yStart, Color.white);
		textrenderer.renderText("Remember to subscribe!", g, xStart, yStart+lineHeight, Color.white);
	}
	private void sayText(String text) {
		VisibleNarrator n = VisibleNarrator.getNarrator();
		NarrationThread narthread = new NarrationThread(text, n);
		Thread thread = new Thread(narthread);
		thread.start();
	}
	private String[] wrapWords(String text) {
		String wrapped = WordWrap.from(text).maxWidth(lineWidth).stringWidth(s -> textrenderer.getLineWidth(s.toString())).wrap();
		return wrapped.split("\\n");
	}

}
