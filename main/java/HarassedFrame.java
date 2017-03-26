package main.java;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
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
import javax.swing.SwingUtilities;

import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class HarassedFrame {
	public HarassedFrame(final User user, final ArrayList<Tweet> negativeTweets, int totalTweetCount){
		int negativeTweetCount = negativeTweets.size();
		
		//Window Frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final JFrame results = new JFrame("Tweet search results");    
		results.setBounds(screenSize.width/2 - 1920/2, screenSize.height/2 - 1080/2, 1920, 1080 + 50);
		results.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		results.getContentPane().setLayout(null);
		results.setVisible(true);
		results.setResizable(false);
		
		//Scroll List Set Up
		final DefaultListModel listModel = new DefaultListModel<String>();
		final JList<String> tweetList = new JList<String>(listModel);
		Collections.sort(negativeTweets, new Comparator<Tweet>(){
			public int compare(Tweet t1, Tweet t2){
				return Double.compare(t1.hIndex, t2.hIndex);
			}
		});

		results.getContentPane().setBackground(new Color(0,172,237));
		
		
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
		
		final DefaultListModel nameList = new DefaultListModel<String>();
		JList<String> list = new JList<String>(nameList);
		list.setFont(new Font("Helvetica Neue", Font.PLAIN, 35));
		list.setBounds(1636, 50 + 50, 234, 711);
		list.addMouseListener( new MouseAdapter()
		{
		    public void mousePressed(MouseEvent e)
		    {
		        if ( SwingUtilities.isRightMouseButton(e) )
		        {
		            JList<?> list = (JList<?>)e.getSource();
		            int row = list.locationToIndex(e.getPoint());
		            list.setSelectedIndex(row);
		            System.out.println(list.getSelectedIndex());
		            nameList.remove(list.getSelectedIndex());
		        }
		    }

		});
		
		
		results.getContentPane().add(list);
		
	
		tweetList.addMouseListener( new MouseAdapter()
		{
		    public void mousePressed(MouseEvent e)
		    {
		        if ( SwingUtilities.isRightMouseButton(e) )
		        {
		            JList<?> list = (JList<?>)e.getSource();
		            int row = list.locationToIndex(e.getPoint());
		            list.setSelectedIndex(row);
		            System.out.println(list.getSelectedIndex());
		            
		            String originalString = tweetList.getModel().getElementAt(row);
		            originalString = originalString.substring(6,originalString.indexOf(":"));
		            if(!nameList.contains(originalString)){
		            	nameList.addElement(originalString);
		            }
		        }
		    }
		});
		
		
		
		//Main Scroll Area
		JScrollPane scrollPane = new JScrollPane(tweetList);
		MyCellRenderer cellRenderer = new MyCellRenderer(1150);
		tweetList.setCellRenderer(cellRenderer);
		scrollPane.setBounds(50, 50 + 50, 1536, 980);
		results.getContentPane().add(scrollPane);
		
		//Report Button
		JButton btnReport = new JButton("Report All");
		btnReport.setBounds(1636, 972 + 50, 234, 58);
		btnReport.setFont(new Font("Helvetica Neue", Font.PLAIN, 40));
		btnReport.addMouseListener(new MouseListener() {
			@SuppressWarnings("static-access")
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					JOptionPane optionPane = new JOptionPane();
					results.getContentPane().add(optionPane);
					if(optionPane.showConfirmDialog(results, "Do you want to report all?", "Report confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						for(int i=0; i< nameList.size(); i++){
							try {
								TwitterFactory.getSingleton().reportSpam(user.getId());
							} catch (TwitterException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
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
		
		JLabel tweetLabel = new JLabel("Negative Tweets");
		tweetLabel.setBounds(50, 30, 800, 60);
		if (negativeTweets.size() != 0) tweetLabel.setText("Doggo found bad tweets!");
		else tweetLabel.setText("Doggo couldn't find bad tweets!");
		tweetLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 50));
		tweetLabel.setForeground(Color.WHITE);
		results.getContentPane().add(tweetLabel);
		
		
		//Block button
		JButton btnBlock = new JButton("Block all");
		btnBlock.setBounds(1636, 885 + 50, 234, 58);
		btnBlock.setFont(new Font("Helvetica Neue", Font.PLAIN, 40));
		btnBlock.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					JOptionPane optionPane = new JOptionPane();
					results.getContentPane().add(optionPane);
					if(optionPane.showConfirmDialog(results, "Do you want to block all?", "Block confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {	
						for(int i=0; i< nameList.size(); i++){
							try {
								System.out.println((String) nameList.get(i));
								TwitterFactory.getSingleton().createBlock((String) nameList.get(i));
							} catch (TwitterException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
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
		results.getContentPane().add(btnBlock);
		
		//Mute button
		JButton btnMute = new JButton("Mute all");
		btnMute.setBounds(1636, 798 + 50, 234, 58);
		btnMute.setFont(new Font("Helvetica Neue", Font.PLAIN, 40));
		btnMute.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					JOptionPane optionPane = new JOptionPane();
					results.getContentPane().add(optionPane);
					if(optionPane.showConfirmDialog(results, "Do you want to mute all?", "Mute confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {	
						for(int i=0; i< nameList.size(); i++){
							try {
								System.out.println((String) nameList.get(i));
								TwitterFactory.getSingleton().createMute((String) nameList.get(i));
							} catch (TwitterException e1) {
								e1.printStackTrace();
							}
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
		results.getContentPane().add(btnMute);
		
		
		//Adds Tweets for Display
        for(Tweet negativeTweet : negativeTweets){
        	listModel.addElement("<html>" + negativeTweet.username + ":\t \t" + Math.abs(Math.round(negativeTweet.hIndex * 1000.0) / 1000.0) + "<br>" + negativeTweet.tweet);
        }
		
	}
}
