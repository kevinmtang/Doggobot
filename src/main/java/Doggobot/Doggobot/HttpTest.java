package Doggobot.Doggobot;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class HttpTest {
	public String createPaste(String language, String user, String message){
		String link = null;
    	try{
			HttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost("http://paste.ee/api");
	
			// Request parameters and other properties.
			List<NameValuePair> params = new ArrayList<NameValuePair>(4);
			params.add(new BasicNameValuePair("key", "f39add6248d836e97c7298183585f0a5"));
			params.add(new BasicNameValuePair("description", user + "'s paste"));
			params.add(new BasicNameValuePair("language", language));
			params.add(new BasicNameValuePair("paste", message));
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
	
			//Execute and get the response.
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
	
			if (entity != null) {
			    InputStream instream = entity.getContent();
			    try {
			    	BufferedReader streamReader = new BufferedReader(new InputStreamReader(instream, "UTF-8")); 
			    	StringBuilder responseStrBuilder = new StringBuilder();
			    	String inputStr;
			    	while ((inputStr = streamReader.readLine()) != null){
			    	    responseStrBuilder.append(inputStr);
			    	}
			    	System.out.println(responseStrBuilder);
			    	JSONObject result= new JSONObject(responseStrBuilder.toString());
			    	if(result.getString("status").equals("success")){
			    		link = result.getJSONObject("paste").getString("link");
			    	}
			    } finally {
			        instream.close();
			    }
			}
		}catch(Exception e){
			e.printStackTrace();
		}
    	return link;
	}
}
