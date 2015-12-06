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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class QBHelper {

	public final static String QBToken = "37cc726ad4e81197ecf924de60989fc2f53d91ba"; 
	
	public static String createDialog(String courseName, int chatId) {
		String rtn = null;
		try {
            URL url = new URL("https://api.quickblox.com/chat/Dialog.json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            JsonObject params = new JsonObject();
            params.addProperty("type", "2");
            params.addProperty("name", courseName);
            params.addProperty("occupants_ids", String.valueOf(chatId));
            System.out.println(params.toString());
            
    		con.setRequestMethod("POST");
    		con.setRequestProperty("Content-Type", "application/json");
    		con.setRequestProperty("QB-Token", QBToken);

            con.setDoOutput(true);
            con.setDoInput(true);

            OutputStream out = new BufferedOutputStream(con.getOutputStream());
            out.write(params.toString().getBytes());
            out.flush();
            out.close();

            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = r.readLine()) != null) {
                sb.append(str);
            }
            in.close();
            String result = sb.toString();
            System.out.println(result);
            
            JsonElement jelement = new JsonParser().parse(result);
            JsonObject  jobject = jelement.getAsJsonObject();
            JsonPrimitive test = jobject.getAsJsonPrimitive("_id");
            rtn = test.toString();
            rtn = rtn.substring(1, rtn.length()-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
		return rtn;
	}

	public static void addDialogMember(String dialog, int chatId) {
		try {
            URL url = new URL("https://api.quickblox.com/chat/Dialog/"+dialog+".json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
           
            String params = "{\"push_all\": {\"occupants_ids\": ["+String.valueOf(chatId)+"]}}";
            System.out.println(params);
    		con.setRequestMethod("PUT");
    		con.setRequestProperty("Content-Type", "application/json");
    		con.setRequestProperty("QB-Token", QBToken);

            con.setDoOutput(true);
            con.setDoInput(true);

            OutputStream out = new BufferedOutputStream(con.getOutputStream());
            out.write(params.getBytes());
            out.flush();
            out.close();

            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = r.readLine()) != null) {
                sb.append(str);
            }
            in.close();
            String result = sb.toString();
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public static void removeDialogMember(String dialog, int chatId) {
		try {
            URL url = new URL("https://api.quickblox.com/chat/Dialog/"+dialog+".json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
           
            String params = "{\"pull_all\": {\"occupants_ids\": ["+String.valueOf(chatId)+"]}}";
            System.out.println(params);
    		con.setRequestMethod("PUT");
    		con.setRequestProperty("Content-Type", "application/json");
    		con.setRequestProperty("QB-Token", QBToken);

            con.setDoOutput(true);
            con.setDoInput(true);

            OutputStream out = new BufferedOutputStream(con.getOutputStream());
            out.write(params.getBytes());
            out.flush();
            out.close();

            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = r.readLine()) != null) {
                sb.append(str);
            }
            in.close();
            String result = sb.toString();
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public static int signUp(String email, String password,String name) {
		int rtn = 0;
		try {
            URL url = new URL("http://api.quickblox.com/users.json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            String params = "{\"user\": {\"login\": \""+email+"\", \"password\": \""+password+"\",\"full_name\": \""+name+"\"}";
            System.out.println(params);
            
    		con.setRequestMethod("POST");
    		con.setRequestProperty("Content-Type", "application/json");
    		con.setRequestProperty("QB-Token", QBToken);

            con.setDoOutput(true);
            con.setDoInput(true);

            OutputStream out = new BufferedOutputStream(con.getOutputStream());
            out.write(params.toString().getBytes());
            out.flush();
            out.close();

            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = r.readLine()) != null) {
                sb.append(str);
            }
            in.close();
            String result = sb.toString();
            System.out.println(result);
            
            JsonElement jelement = new JsonParser().parse(result);
            JsonObject  jobject = jelement.getAsJsonObject();
            JsonObject user = jobject.getAsJsonObject("user");
            JsonPrimitive userId = user.getAsJsonPrimitive("id");
            rtn = Integer.valueOf(userId.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
		return rtn;
	}
}
