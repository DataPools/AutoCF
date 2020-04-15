package ui;

import java.io.File;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import config.Config;
import config.ConfigHandler;
import reddithandler.ProfanityFilter;
import reddithandler.RedditThread;

public class ACFRenderer extends BasicGame {
	
	public static final int gameWidth = 1440;
	public static final int gameHeight = 720;
	private static RedditThreadRenderer threadRenderer;
	private static File configFile = new File("config.json");
	public static final Config config = new ConfigHandler(configFile).getConfig();

	public ACFRenderer() {
		super("AutoCF");
	}
	
	public static void main(String[] args) {
		try
        {
            AppGameContainer app = new AppGameContainer(new ACFRenderer());
            app.setDisplayMode(gameWidth, gameHeight, false);
            app.setTargetFrameRate(60);
            app.setVSync(true);
            app.setShowFPS(false);
            app.start();
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }

	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		threadRenderer = new RedditThreadRenderer(new RedditThread(config.getPostId()));
		ProfanityFilter.loadProfanityList();
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		
	}
	
	@Override
	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		arg1.setColor(new Color(31, 31, 32));
		arg1.fillRect(0, 0, arg0.getScreenWidth(),arg0.getScreenHeight());
	    threadRenderer.render(arg1, 200, 100);
	}

}
