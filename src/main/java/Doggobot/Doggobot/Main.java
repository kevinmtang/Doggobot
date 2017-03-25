package Doggobot.Doggobot;


import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Hello world!
 *
 */
public class Main
{
	static TwitterFactory tf;
    public static void main(String[] args) throws IllegalStateException, TwitterException
    {
       setup();
       
       List<Status> statuses = getTweets("potus", 15);
	   	for(Status status : statuses){
			System.out.println(status.getText());
			System.out.println();
		}
    }
    
    public static void setup() throws IllegalStateException, TwitterException{
    	ConfigurationBuilder cb = new ConfigurationBuilder();
    	cb.setDebugEnabled(true)
    	  .setOAuthConsumerKey("JfcC0vrS0iuqiolDei56yZ1lg")
    	  .setOAuthConsumerSecret("dM9j3Vroi1ke8Ff2bzYO2XP9qgkoY4c0kDEhNWBdhLPTKUYxp5")
    	  .setOAuthAccessToken("845736778585714688-eWsDjCovLRGrVZ5RmqQR0CfNWuWnlCW")
    	  .setOAuthAccessTokenSecret("pW2SGrzqbeZO9xaGoNxzqiSB9sAeayhaJxa7qFcZ3uSxE");
    	tf = new TwitterFactory(cb.build());
    	System.out.println("Connected as " + tf.getInstance().getScreenName());
    }
    
    //http://stackoverflow.com/questions/2943161/get-tweets-of-a-public-twitter-profile
    public static List<Status> getTweets(String username, int tweets) throws TwitterException{
    	System.out.println("First " + tweets + " tweets of @" + username);
    	// The factory instance is re-useable and thread safe.
    	Twitter twitter = tf.getInstance();
    	//First param of Paging() is the page number, second is the number per page (this is capped around 200.
    	Paging paging = new Paging(1, Math.min(100, tweets));
    	
    	List<Status> statuses = twitter.getUserTimeline(username,paging);
    	return statuses;
    }
}

