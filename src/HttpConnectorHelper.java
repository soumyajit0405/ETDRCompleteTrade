

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpConnectorHelper {
	private static final String USER_AGENT = "Mozilla/5.0";

	private static final String GET_URL = "https://localhost:9090/SpringMVCExample";

	private static final String POST_URL = "http://168.61.22.57:6380/createuser";

	
	public static void main(String[] args) throws IOException, JSONException {

		JSONObject input = new JSONObject();
		input.put("meterId", 6);
		input.put("timestamp", "2020-06-07 21:15:00");
		// sendGET();
		System.out.println("GET DONE");
		 new HttpConnectorHelper().sendPostWithToken("http://14.139.98.213:4012/agent/fetchTransactionData",input);
		System.out.println("POST DONE");
	}

	
	public JSONObject sendPostWithToken(String url, JSONObject params) throws IOException, JSONException {
		JSONObject jsonObject1 = null;
		try {
		HashMap<String,String> map = new HashMap<>();
		JSONArray jsonObject = null;
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		//con.setRequestProperty("Authorization", "Bearer "+token);
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		System.out.println(params.toString());
		os.write(params.toString().getBytes());	
		os.flush();
		os.close();
		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				jsonObject1 = new JSONObject(response.toString());
				 jsonObject = (JSONArray)(jsonObject1.get("meterData"));
				 if(jsonObject.length()== 0) {
					 return new JSONObject();
				 } else {
				 jsonObject1 =(JSONObject)jsonObject.getJSONObject(0);
				 }
				 
				in.close();
				con.disconnect();
				
				// print result
				System.out.println(response.toString());
			} 
			
		else {
				System.out.println("POST request not worked");
			}
			
			System.out.println("POST request not worked");
		}
		catch(Exception e) {
			return jsonObject1;
		}
		return jsonObject1;
	}

}