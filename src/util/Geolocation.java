package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class Geolocation {
	public static String getGeometry(String address) throws IOException {
		String noSpace = address.replaceAll("\\s+","+");
		String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+noSpace;
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		
		JsonElement jelement = new JsonParser().parse(response.toString());
		JsonObject  jobject = jelement.getAsJsonObject();
		JsonElement  jobject2 = jobject.getAsJsonArray("results").get(0);
		JsonObject  jobject3 = jobject2.getAsJsonObject();
		JsonObject geo = jobject3.getAsJsonObject("geometry");
		JsonObject location = geo.getAsJsonObject("location");
		JsonPrimitive lat = location.getAsJsonPrimitive("lat");
		JsonPrimitive lng = location.getAsJsonPrimitive("lng");
		System.out.println(lat.toString()+","+lng.toString());
		return lat.toString()+","+lng.toString();
	}
}
