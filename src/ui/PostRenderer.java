package ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import narrator.NarrationThread;
import narrator.VisibleNarrator;
import reddithandler.RedditPost;

public class PostRenderer {
	
	private RedditPost post;
	private int index = 1;
	private static int lineHeight;
	public static int indentWidth = 50;
	private VisibleNarrator n;
	private NarrationThread narratorthread;
	private boolean finished = false;
	private PostRenderer commentRenderer;
	private boolean isComment;
	private int startingIndex = 0;
	private TextRenderer textrenderer;
	private static Image arrows;
	
	private final int fontSize = 20;
	
	public PostRenderer(RedditPost post, boolean comment) {
		this.post = post;
		n = VisibleNarrator.getNarrator();
		textrenderer = new TextRenderer(fontSize);
		initializeImage();
		lineHeight = textrenderer.getLineHeight();
		this.isComment = comment;
		if(!isComment) {
			RedditPost commentPost = post.getTopComment();
			if(commentPost != null) {
				commentRenderer = new PostRenderer(post.getTopComment(),true);
			}
		}
	}
	public void render(Graphics g, float x, float y) {
		String[] lines = post.getPostLines();
		//Start narrator at beginning
		if(narratorthread == null) {
			startNarration(lines[0]);
		}
		arrows.draw(x-30, y);
		textrenderer.renderText(post.getUsername(),g,x,y, Color.blue);
		y+=lineHeight+10;
		for(int i=startingIndex;i<index && i<lines.length;i++) {
			float currentY = y+(i-startingIndex)*lineHeight;
			textrenderer.renderText(lines[i], g, x, currentY, Color.white);
			//If y is within 50px of game height, paginate
			if(currentY+50>ACFRenderer.gameHeight && i<lines.length) {
				startingIndex = i;
			}
		}
		//If narrator done speaking, add another line
		if(!narratorthread.isSpeaking() && !postCompleted() && index < lines.length) {
			startNarration(lines[index]);
			index++;
		}
		else if(!narratorthread.isSpeaking()) {
			if(isComment || commentRenderer == null || commentRenderer.postCompleted()) {
				finished = true;
			}
			else {
				commentRenderer.render(g, x+indentWidth, y+((lines.length-startingIndex)*lineHeight)+20);
			}
			
		}
	}
	private void initializeImage() {
		try {
			arrows = new Image("assets/arrows.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void startNarration(String line) {
		narratorthread = new NarrationThread(line, n);
		Thread thread = new Thread(narratorthread);
		thread.start();
	}
	public boolean postCompleted() {
		return finished;
	}

}
