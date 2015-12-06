package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.User;
import util.ClassDiscuzDB;
import util.QBHelper;

/**
 * Servlet implementation class SignUp
 */
@WebServlet("/signup")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//	        JSONArray array = (JSONArray)parser.parse(jsonStr);;
//	        JSONObject fields = (JSONObject) array.get(0);
	        User newUser = new User();
	        Gson gson = new Gson();
	        String email = request.getParameter("email");
	        String password = request.getParameter("password");
	        String name = request.getParameter("name");
	        
	        newUser.setEmail(email);
	        newUser.setPassword(password);
	        newUser.setName(name);
	        newUser.setFocus(0);

	        ClassDiscuzDB db = new ClassDiscuzDB(this.getServletContext());
	        String json = null;
	        JsonObject jsonObject = new JsonObject();
	        try{
		        if (!db.connectDB()) {
		        	jsonObject.addProperty("result", "1");
		        	json = jsonObject.toString();
		        } else {
			        if (db.isValidEmail(email)) {
			        	int chatId = QBHelper.signUp(email,password, name);
			        	if (chatId == 0) {
				        	jsonObject.addProperty("result", "4");
				        	json = jsonObject.toString();			        		
			        	} else {
							db.createUser(newUser, chatId);
							
				            User user = db.getUserByEmailPwd(email, password);
				            
				            JsonElement jsonElement = gson.toJsonTree(user);
				            jsonElement.getAsJsonObject().addProperty("result", "0");
				           	json = gson.toJson(jsonElement);			        		
			        	}

			        } else {
			        	jsonObject.addProperty("result", "3");
			        	json = jsonObject.toString();
			        }
		        }
			} catch (SQLException e) {
				e.printStackTrace();
	        	jsonObject.addProperty("result", "2");
	        	json = jsonObject.toString();
			}
			db.closeDB();
			response.getWriter().append(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
