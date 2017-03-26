package main.java;

public class Tweet {
	public String username;
	public String tweet;
	public double hIndex;
	public Tweet(String username, String tweet, double hindex)
	{
		this.username = username;
		this.tweet = tweet;
		this.hIndex = hindex;
	}
}