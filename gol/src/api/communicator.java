package api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class communicator {
	public static String get_saved_object(String name) {
		URLConnection urlConn;
		DataOutputStream printout;
		URL url;
		StringBuffer response = new StringBuffer();
		try {
			url = new URL("http://local.gol/");
			urlConn = url.openConnection();
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);
			urlConn.setRequestProperty("Content-Type", "application/json");
			urlConn.connect();
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("action", "get");
			jsonParam.put("name", name);
			printout = new DataOutputStream(urlConn.getOutputStream ());
			printout.writeBytes((jsonParam.toString()));
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(urlConn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			printout.flush ();
			printout.close ();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		String asd = response.toString();
		return asd;
	}
	public static boolean save_object(String name, String data) {
		URLConnection urlConn;
		DataOutputStream printout;
		URL url;
		StringBuffer response = new StringBuffer();
		try {
			url = new URL("http://local.gol/");
			urlConn = url.openConnection();
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);
			urlConn.setRequestProperty("Content-Type", "application/json");
			urlConn.connect();
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("action", "save");
			jsonParam.put("name", name);
			jsonParam.put("data", data);
			printout = new DataOutputStream(urlConn.getOutputStream ());
			printout.writeBytes(jsonParam.toString());
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(urlConn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			printout.flush ();
			printout.close ();
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String asd = response.toString();
		return (asd.length()>0);
	}
}
