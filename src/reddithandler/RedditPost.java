package reddithandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.davidmoten.text.utils.WordWrap;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Comment;
import net.dean.jraw.models.CommentSort;
import net.dean.jraw.models.PublicContribution;
import net.dean.jraw.models.Submission;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.references.CommentsRequest;
import net.dean.jraw.tree.CommentNode;
import net.dean.jraw.tree.RootCommentNode;

public class RedditPost {
	
	private String username;
	private ArrayList<RedditPost> comments;
	private String text;
	private int index;
	
	protected static RedditClient redditclient;
	private static final int indexCutoff = 2;
	private static final int lineWidth = 100;
	//Maximum length that links can be before they are cutoff and abridged
	private static final int maxLinkLength = 25;
	
	static {
		UserAgent userAgent = new UserAgent("bot", "com.example.usefulbot", "v0.1", "mattbdean");
		Credentials credentials = Credentials.script("minedrome", "Admin123@","MDwbBBr7pxMexQ", "9UXWtdMQQwZHvfHEhi_J-Lz48c8");
		NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);
		redditclient = OAuthHelper.automatic(adapter, credentials);
		redditclient.setLogHttp(false);
	}
	
	public RedditPost(String id) {
		index = 0;
		getInfo(id);
		comments = fetchComments(id);
	}
	private RedditPost(CommentNode<Comment> comment, int index) {
		this.index = index;
		this.username = comment.getSubject().getAuthor();
		this.text = comment.getSubject().getBody();
		if(index <= indexCutoff) {
			comments = fetchComments(comment);
		}
	}
	private void getInfo(String id) {
		Submission s = redditclient.submission(id).inspect();
		this.username = s.getAuthor();
		this.text = s.getBody();
	}
	private ArrayList<RedditPost> fetchComments(CommentNode<Comment> comment) {
		ArrayList<RedditPost> redditThread = new ArrayList<RedditPost>();
		List<CommentNode<Comment>> root = comment.getReplies();
		Iterator<CommentNode<Comment>> it = root.iterator();
		while(it.hasNext()) {
			CommentNode<Comment> next = it.next();
			PublicContribution<?> reply = next.getSubject();
			if(reply.getAuthor().equals("AutoModerator")) {
				//Absolutely nothing
				continue;
			}
			else {
				RedditPost post = new RedditPost(next, this.index+1);
				redditThread.add(post);
			}
		}
		return redditThread;
	}
	private ArrayList<RedditPost> fetchComments(String id) {
		ArrayList<RedditPost> redditThread = new ArrayList<RedditPost>();
		CommentsRequest req = new CommentsRequest.Builder().sort(CommentSort.TOP).limit(100).build();
		RootCommentNode root = redditclient.submission(id).comments(req);
		Iterator<CommentNode<Comment>> it = root.iterator();
		while(it.hasNext()) {
			CommentNode<Comment> next = it.next();
			PublicContribution<?> reply = next.getSubject();
			if(reply.getAuthor().equals("AutoModerator")) {
				//Absolutely nothing
				continue;
			}
			else {
				RedditPost post = new RedditPost(next, this.index+1);
				redditThread.add(post);
			}
		}
		return redditThread;
	}
	public String[] getPostLines() {
		String textToUse = preprocessText(text);
		if(textToUse != null) {
			String wrapped = WordWrap.from(textToUse).maxWidth(lineWidth).wrap();
			return wrapped.split("\\n");
		}
		return null;
	}
	public static String preprocessText(String textInput) {
		//remove all profanity
		textInput = ProfanityFilter.sanitizeText(textInput);
		//Remove underscores, used by reddit markdown for italics
		textInput = textInput.replace(" _", " ");
		textInput = textInput.replace("_ ", " ");
		//Unescape HTML, &/>/< are the most common unescaped characters
		textInput = textInput.replace("&amp;", "&");
		textInput = textInput.replace("&gt;", ">");
		textInput = textInput.replace("&lt;", "<");
		//Replace markdown links with the link's text
		String regexPattern = "(\\[)([^\\]()#\\n]+)\\]\\(([^\\]()#\\n]+)\\)";
		Pattern p = Pattern.compile(regexPattern); 
		Matcher m = p.matcher(textInput);
		while(m.find()) {
			String linkText = m.group(0).split("\\[")[1].split("\\]")[0];
			textInput = textInput.replaceFirst(regexPattern, linkText);
			m = p.matcher(textInput);
		}
		//Find and truncates overly long links
		String linkRegex = "(?:(?:https?):\\/\\/|\\b(?:[a-z\\d]+\\.))(?:(?:[^\\s()<>]+|\\((?:[^\\s()<>]+|(?:\\([^\\s()<>]+\\)))?\\))+(?:\\((?:[^\\s()<>]+|(?:\\(?:[^\\s()<>]+\\)))?\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’]))?";
		Pattern linkPattern = Pattern.compile(linkRegex);
		m = linkPattern.matcher(textInput);
		while(m.find()) {
			String firstLink = m.group(0);
			if(firstLink.length() > maxLinkLength) {
				String replacementString = firstLink.substring(0,maxLinkLength)+"...";
				textInput = textInput.replace(firstLink, replacementString);
				m = linkPattern.matcher(textInput);
			}
		}
		return textInput;
	}
	public String getUsername() {
		return username;
	}
	public RedditPost getTopComment() {
		if(comments.size() > 0) {
			return comments.get(0);
		}
		return null;
	}
	protected ArrayList<RedditPost> getComments() {
		return comments;
	}
	public int getIndex() {
		return index;
	}
	
}
