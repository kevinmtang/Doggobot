package main.java;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class HarasserProfileFrame {
	public HarasserProfileFrame(final User user, final ArrayList<Tweet> negativeTweets, int totalTweetCount){
		int negativeTweetCount = negativeTweets.size();
		
		//Window Frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final JFrame results = new JFrame("Tweet search results");    
		results.setBounds(screenSize.width/2 - 1920/2, screenSize.height/2 - 1080/2, 1920, 1080 + 50);
		results.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		results.getContentPane().setLayout(null);
		results.setVisible(true);
		results.getContentPane().setBackground(new Color(0, 172, 237));
		
		//Scroll List Set Up
		final DefaultListModel listModel = new DefaultListModel<String>();
		JList<String> tweetList = new JList<String>(listModel);
		Collections.sort(negativeTweets, new Comparator<Tweet>(){
			public int compare(Tweet t1, Tweet t2){
				return Double.compare(t1.hIndex, t2.hIndex);
			}
		});
		tweetList.setFont(new Font("Helvetica Neue", Font.PLAIN, 30));
		
		//Text Wrapper
		class MyCellRenderer extends DefaultListCellRenderer {
			   public static final String HTML_1 = "<html><body style='width: ";
			   public static final String HTML_2 = "px'>";
			   public static final String HTML_3 = "</html>";
			   private int width;

			   public MyCellRenderer(int width) {
			      this.width = width;
			   }
			   
			   public Component getListCellRendererComponent(JList list, Object value,
			         int index, boolean isSelected, boolean cellHasFocus) {
			      String text = HTML_1 + String.valueOf(width) + HTML_2 + value.toString()
			            + HTML_3;
			      return super.getListCellRendererComponent(list, text, index, isSelected,
			            cellHasFocus);
			   }
		}
		
		//Main Scroll Area
		JScrollPane scrollPane = new JScrollPane(tweetList);
		MyCellRenderer cellRenderer = new MyCellRenderer(1150);
		tweetList.setCellRenderer(cellRenderer);
		scrollPane.setBounds(50, 50 + 50, 1536, 980);
		results.getContentPane().add(scrollPane);

		//Percent Number on the Right
		JTextField percentNegativeTweets = new JTextField();
		percentNegativeTweets.setBounds(1636, 48 + 50, 234, 85);
		percentNegativeTweets.setBackground(new Color(50, 80, 109));
		percentNegativeTweets.setFont(new Font("Helvetica Neue", Font.PLAIN, 40));
		percentNegativeTweets.setForeground(Color.WHITE);
		percentNegativeTweets.setEditable(false);
		percentNegativeTweets.setText(100.0 * negativeTweetCount / totalTweetCount + "%");
		percentNegativeTweets.setHorizontalAlignment(JLabel.CENTER);
		results.getContentPane().add(percentNegativeTweets);
		
		JLabel percentLabel1 = new JLabel();
		percentLabel1.setBounds(1636, 150 + 50, 270, 50);
		percentLabel1.setText("of recent tweets ");
		percentLabel1.setFont(new Font("Helvetica Neue", Font.BOLD, 30));
		percentLabel1.setForeground(Color.WHITE);
		percentLabel1.setHorizontalAlignment(JLabel.LEFT);
		results.getContentPane().add(percentLabel1);
		
		JLabel percentLabel2 = new JLabel();
		percentLabel2.setBounds(1636, 200 + 50, 234, 50);
		percentLabel2.setText("were negative");
		percentLabel2.setFont(new Font("Helvetica Neue", Font.BOLD, 30));
		percentLabel2.setForeground(Color.WHITE);
		percentLabel1.setHorizontalAlignment(JLabel.LEFT);
		results.getContentPane().add(percentLabel2);
		
		JLabel tweetLabel = new JLabel("Negative Tweets");
		if (negativeTweets.size() != 0) tweetLabel.setText("Doggo found bad tweets!");
		else tweetLabel.setText("Doggo couldn't find bad tweets!");
		tweetLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 50));
		tweetLabel.setHorizontalAlignment(SwingConstants.LEFT);
		tweetLabel.setForeground(Color.WHITE);
		tweetLabel.setBounds(50, 30, 800, 60);
		results.getContentPane().add(tweetLabel);

		//Report Button
		JButton btnReport = new JButton("Report User");
		btnReport.setBounds(1636, 972 + 50, 234, 58);;
		btnReport.setFont(new Font("Helvetica Neue", Font.PLAIN, 40));
		btnReport.addMouseListener(new MouseListener() {
			@SuppressWarnings("static-access")
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					JOptionPane optionPane = new JOptionPane();
					results.getContentPane().add(optionPane);

					if(optionPane.showConfirmDialog(results, "Do you want to report " + user.getName() + "?", "Report confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						try {
							TwitterFactory.getSingleton().reportSpam(user.getId());
						} catch (TwitterException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					e.consume();
				}
			}
			// unused methods
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		results.getContentPane().add(btnReport);
		
		//Adds Tweets for Display
        for(Tweet negativeTweet : negativeTweets){
        	listModel.addElement("<html>" + negativeTweet.username + ":\t \t" + Math.abs(Math.round(negativeTweet.hIndex * 1000.0) / 1000.0) + "<br>" + negativeTweet.tweet);
        }
		
	}
}
