package reddithandler;

import java.util.ArrayList;

import ui.ACFRenderer;

public class RedditThread extends RedditPost {
	
	private String title;
	private ArrayList<RedditPost> postsList;
	private int currentIndex = 0;
	//Remember that each post has a comment too
	private static final int maxPosts = ACFRenderer.config.getMaxPosts();

	public RedditThread(String id) {
		super(id);
		postsList = new ArrayList<RedditPost>();
		//Preprocessing removes swearing, escapes, etc.
		title = RedditPost.preprocessText(redditclient.submission(id).inspect().getTitle());
		ArrayList<RedditPost> superPosts = this.getComments();
		for(int i=0;i<maxPosts && i<superPosts.size();i++) {
			RedditPost p = superPosts.get(i);
			postsList.add(p);
		}
	}
	public String getTitle() {
		return title;
	}
	public RedditPost getNextPost() {
		if(hasNextPost()) {
			RedditPost toreturn = postsList.get(currentIndex);
			currentIndex++;
			return toreturn;
		}
		else {
			return null;
		}
	}
	public boolean hasNextPost() {
		return currentIndex < postsList.size();
	}
}
