package main.java;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import javax.swing.JProgressBar;

public class doggoUI{
	private static final int TWEETS_TO_SEARCH = 100;
	private static final int WIDTH = 480;
	private static final int HEIGHT = 360;
	private static TwitterFactory tf;
	private static JFrame frame;
	private JTextField textField;
	private static JLabel message;
    private static Watson httpTest = new Watson();
    private JTextField txtOauthconsumerkey;
    private JTextField txtOauthconsumersecret;
    private JTextField txtOauthaccesstoken;
    private JTextField txtOauthaccesstokensecret;
    private JLabel lblEnterDevInfo;

	
	public static void main(String[] args) {
		try {
			setup();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (TwitterException e1) {
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					doggoUI window = new doggoUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public doggoUI() {
		initialize();
	}

	private void initialize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		frame = new JFrame("DoggoBot");
		frame.setBounds((int) screenSize.getWidth() / 2 - WIDTH / 2, (int) screenSize.getHeight() / 2 - HEIGHT / 2, WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.getContentPane().setLayout(null);

		message = new JLabel("Enter in a Twitter handle");
		message.setBounds(30, 62, 200, 50);
		message.setFont(new Font("Helvetica Neue", Font.BOLD, 15));
		message.setHorizontalAlignment(JLabel.CENTER);
		frame.getContentPane().add(message);

		JLabel title = new JLabel("DoggoBot");
		title.setBounds(90, 5, 300, 60);
		title.setFont(new Font("Helvetica Neue", Font.ITALIC, 50));
		title.setHorizontalAlignment(JLabel.CENTER);
		frame.getContentPane().add(title);

		JPanel divider = new JPanel();
		divider.setBounds(239, 78, 2, 234);
		divider.setBackground(Color.BLACK);
		frame.getContentPane().add(divider);
		
		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
		
		textField = new JTextField();
		textField.setText("potus");
		textField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		frame.getContentPane().add(textField);
		textField.setBounds(39, 163, 180, 25);
		textField.setColumns(16);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setBorder(border);

		JButton btnCheckUserFor = new JButton("Check user for harassment");
		btnCheckUserFor.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
		btnCheckUserFor.setBackground(new Color(0,172,237));
		btnCheckUserFor.setBounds(WIDTH / 2 - 210, 260, 200, 50);
		btnCheckUserFor.setForeground(Color.WHITE);
		btnCheckUserFor.setOpaque(true);
		btnCheckUserFor.setBorderPainted(false);
		frame.getContentPane().add(btnCheckUserFor);
		btnCheckUserFor.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					message.setText("Finding User...");
					processStatuses(textField.getText());
					// checkBrowser(textField.getText());
					e.consume();
				}
			}
			// unused methods
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		
		txtOauthconsumerkey = new JTextField();
		txtOauthconsumerkey.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		txtOauthconsumerkey.setToolTipText("OAuthConsumerKey");
		txtOauthconsumerkey.setText("0HQ9kKmdu43yV0Hg99qSsquuo");
		txtOauthconsumerkey.setBounds(265, 120, 170, 25);
		frame.getContentPane().add(txtOauthconsumerkey);
		txtOauthconsumerkey.setColumns(10);
		txtOauthconsumerkey.setBorder(border);
		
		txtOauthconsumersecret = new JTextField();
		txtOauthconsumersecret.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		txtOauthconsumersecret.setToolTipText("OAuthConsumerSecret");
		txtOauthconsumersecret.setText("JPcmp7hmoHSgVIxp5sS1WAGa3HaXp1HeRKFK88yZO7vOEkRBb8");
		txtOauthconsumersecret.setBounds(265, 150, 170, 25);
		frame.getContentPane().add(txtOauthconsumersecret);
		txtOauthconsumersecret.setColumns(10);
		txtOauthconsumersecret.setBorder(border);
		
		txtOauthaccesstoken = new JTextField();
		txtOauthaccesstoken.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		txtOauthaccesstoken.setToolTipText("OAuthAccessToken");
		txtOauthaccesstoken.setText("845713505810493440-uEsWf3v1bA4vmmOTUbJLHEmvp5v0ZTf");
		txtOauthaccesstoken.setBounds(265, 180, 170, 25);
		frame.getContentPane().add(txtOauthaccesstoken);
		txtOauthaccesstoken.setColumns(10);
		txtOauthaccesstoken.setBorder(border);
		
		txtOauthaccesstokensecret = new JTextField();
		txtOauthaccesstokensecret.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		txtOauthaccesstokensecret.setToolTipText("OAuthAccessTokenSecret");
		txtOauthaccesstokensecret.setText("CBPqQCJyntm2UOERpc2tDIyMA9HjckoH3awcV6Ir522vo");
		txtOauthaccesstokensecret.setBounds(265, 210, 170, 25);
		frame.getContentPane().add(txtOauthaccesstokensecret);
		txtOauthaccesstokensecret.setColumns(10);
		txtOauthaccesstokensecret.setBorder(border);
		
		JButton btnCheckMentions = new JButton("Check if user was harassed");
		btnCheckMentions.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
		btnCheckMentions.setBackground(new Color(0,172,237));
		btnCheckMentions.setBounds(250, 260, 200, 50);
		btnCheckMentions.setForeground(Color.WHITE);
		btnCheckMentions.setBorderPainted(false);
		btnCheckMentions.setOpaque(true);
		frame.getContentPane().add(btnCheckMentions);
		
		lblEnterDevInfo = new JLabel("Or enter DevID Key info");
		lblEnterDevInfo.setBounds(250, 70, 200, 30);
		lblEnterDevInfo.setHorizontalAlignment(JLabel.CENTER);
		lblEnterDevInfo.setFont(new Font("Helvetica Neue", Font.BOLD, 15));
		frame.getContentPane().add(lblEnterDevInfo);
		
		JPanel bar = new JPanel();
		bar.setForeground(Color.LIGHT_GRAY);
		bar.setBounds(0, 322, 480, 10);
		frame.getContentPane().add(bar);
		
		btnCheckMentions.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					
					processMentions(txtOauthconsumerkey.getText(), txtOauthconsumersecret.getText(), txtOauthaccesstoken.getText(), txtOauthaccesstokensecret.getText());
					// checkBrowser(textField.getText());
					e.consume();
				}
			}
			// unused methods
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
	}

	public static void processStatuses(String handle) {
		List<Status> statuses = null;
		try {
			statuses = getTweets(handle, TWEETS_TO_SEARCH);
		} catch (TwitterException e1) {
			message.setText("That user does not exist.");
		}
        if(statuses.size() == 0) {
        	message.setText(handle + " has not yet tweeted.");
        	return;
        }
        else {
        	message.setText("User found.");
        }
        ArrayList<Tweet> negativeTweets = new ArrayList<Tweet>();
        
 	   	for(Status status : statuses){
 	   		double hIndex = getHIndex(status.getText());
 	   		if(hIndex<-0.75){
 	 	   		negativeTweets.add(new Tweet(status.getUser().getName(), status.getText(), hIndex));
 	   		}
 			System.out.println(status.getText());
 		}

 	   	HarasserProfileFrame resultsFrame = new HarasserProfileFrame(statuses.get(0).getUser(), negativeTweets, TWEETS_TO_SEARCH);
	}
	
	public static void processMentions(String oauthConsumerKey, String oauthConsumerSecret, String oauthAccessToken, String oauthAccessTokenSecret){
		try {
			setup(oauthConsumerKey, oauthConsumerSecret, oauthAccessToken, oauthAccessTokenSecret);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		List<Status> mentions = null;
		List<DirectMessage> DMs = null;
		try {
			mentions = getMentions(TWEETS_TO_SEARCH/2);
			DMs = getDMs(TWEETS_TO_SEARCH/2);
		} catch (TwitterException e1) {
			message.setText("Your account does not exist.");
		}
        if(mentions.size() == 0) {
        	message.setText("No mentions of you found.");
        	return;
        }
        else {
        	message.setText("User found.");
        }
        ArrayList<Tweet> negativeTweets = new ArrayList<Tweet>();
 	   	for(Status status : mentions){
 	   		double hIndex = getHIndex(status.getText());
 	   		if(hIndex<-0.75){
 	 	   		negativeTweets.add(new Tweet(status.getUser().getScreenName(), status.getText(), hIndex));
 	   		}
 			System.out.println(status.getText());
 		}
 	   	if(DMs != null){
	 	   	for(DirectMessage DM : DMs){
	 	   		double hIndex = getHIndex(DM.getText());
	 	   		if(hIndex<-0.75){
	 	 	   		negativeTweets.add(new Tweet(DM.getSenderScreenName(), DM.getText(), hIndex));
	 	   		}
	 			System.out.println(DM.getText());
	 		}
 	   	}
 	   	
		HarassedFrame resultsFrame = new HarassedFrame(mentions.get(0).getUser(), negativeTweets, TWEETS_TO_SEARCH);
	}
	

	public static void checkBrowser(String handle) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop()
						.browse(new URI("https://twitterbotharassments.mybluemix.net/twitters?q=" + handle));
			} catch (IOException e) {
				System.out.println("IOException Error");
			} catch (URISyntaxException e) {
				System.out.println("URL Syntax Error");
			}
		}
	}

	public static void setup() throws IllegalStateException, TwitterException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("0HQ9kKmdu43yV0Hg99qSsquuo")
				.setOAuthConsumerSecret("JPcmp7hmoHSgVIxp5sS1WAGa3HaXp1HeRKFK88yZO7vOEkRBb8")
				.setOAuthAccessToken("845713505810493440-uEsWf3v1bA4vmmOTUbJLHEmvp5v0ZTf")
				.setOAuthAccessTokenSecret("CBPqQCJyntm2UOERpc2tDIyMA9HjckoH3awcV6Ir522vo");
		tf = new TwitterFactory(cb.build());

	}
	
	public static void setup(String oauthConsumerKey, String oauthConsumerSecret, String oauthAccessToken, String oauthAccessTokenSecret) throws IllegalStateException, TwitterException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(oauthConsumerKey)
				.setOAuthConsumerSecret(oauthConsumerSecret)
				.setOAuthAccessToken(oauthAccessToken)
				.setOAuthAccessTokenSecret(oauthAccessTokenSecret);
		tf = new TwitterFactory(cb.build());

	}

	// http://stackoverflow.com/questions/2943161/get-tweets-of-a-public-twitter-profile
	public static List<Status> getTweets(String username, int tweets) throws TwitterException {
		System.out.print("Attempting first " + tweets + " tweets of ");
		if (username.charAt(0) != '@') {
			System.out.print("@");
		}
		System.out.println(username);

		Twitter twitter = tf.getInstance();
		int n = 1;
		List<Status> statuses = new ArrayList<Status>();
		do {
			statuses.addAll((twitter.getUserTimeline(username, new Paging(n, tweets))));
			tweets -= 100;
			n++;
		} while (tweets > 0);
		return statuses;
	}

	public static List<Status> getMentions(int numMentions) throws TwitterException {
		System.out.println("Attempting first " + numMentions + " mentions of you");

		Twitter twitter = tf.getInstance();
		int n = 1;
		List<Status> mentions = new ArrayList<Status>();
		do {
			mentions.addAll(twitter.getMentionsTimeline(new Paging(n, numMentions)));
			numMentions -= 100;
			n++;
		} while (numMentions > 0);
		return mentions;
	}
	
	public static List<DirectMessage> getDMs(int numDMs) throws TwitterException {
		System.out.println("Attempting first " + numDMs + " DMs to you");
		Twitter twitter = tf.getInstance();
		int n = 1;
		List<DirectMessage> DMs = new ArrayList<DirectMessage>();
		do {
			DMs.addAll(twitter.getDirectMessages(new Paging(n, numDMs)));
			numDMs -= 100;
			n++;
		} while (numDMs > 0);
		return DMs;
	}
	
	public static double getHIndex(String message){
        double positivity = 0;
        double anger = 0;
        double disgust = 0;
        double sadness = 0;
        
			String tweetText = message;
	    	try {
	    		String toneJSON = httpTest.getToneJSON(tweetText);
	        	anger = httpTest.getToneElement(toneJSON, Watson.TONE_ANGER);
	        	disgust = httpTest.getToneElement(toneJSON, Watson.TONE_DISGUST);
	        	sadness = httpTest.getToneElement(toneJSON, Watson.TONE_SADNESS);
				positivity = httpTest.getNegativity(tweetText);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    System.out.println(positivity + 2 * (anger + disgust + sadness));
        System.out.println(.6*positivity +.5*positivity* (anger + disgust + sadness));
        System.out.println(positivity + "" + anger + "" + disgust + "" + sadness);
		return .6*positivity +.5*positivity* (anger + disgust + sadness);
	}
}

