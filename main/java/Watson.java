package main.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class Watson {
	public static final int TONE_ANGER = 0,
			TONE_DISGUST = 1,
			TONE_FEAR = 2,
			TONE_JOY = 3,
			TONE_SADNESS = 4;

	public double getNegativity(String message) throws Exception {
		message = message.replaceAll("[^A-Za-z0-9 ]", "");
		message = message.replaceAll(" ", "%20");
		StringBuilder result = new StringBuilder();
		URL url = new URL("https://twitterbotharassments.mybluemix.net/twitters?q=" + message);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		System.out.println("Negativity " + result);
		return Double.parseDouble(result.toString());
	}
	
	public String getToneJSON(String message) throws Exception {
		message = message.replaceAll("[^A-Za-z0-9 ]", "");
		message = message.replace(" ", "%20");
		StringBuilder result = new StringBuilder();
		URL url = new URL("https://twitterbotharassments.mybluemix.net/tone?q=" + message);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		
		//remove [ ] surrounding the json
		return result.toString().substring(1, result.length()-1);
		
   }
	
	public double getToneElement(String JSON, int toneElementID) throws JSONException{
    	JSONObject result= new JSONObject(JSON.toString());
    	
		return Double.parseDouble(result.getJSONArray("tones").getJSONObject(toneElementID).getString("score"));
				
	}

}
