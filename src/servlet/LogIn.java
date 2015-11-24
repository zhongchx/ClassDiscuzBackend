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

/**
 * Servlet implementation class LogIn
 */
@WebServlet("/login")
public class LogIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogIn() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Gson gson = new Gson();
        String json = null;
        ClassDiscuzDB db = new ClassDiscuzDB(this.getServletContext());
        JsonObject jsonObject = new JsonObject();
        try {
	        if (!db.connectDB()) {
	        	jsonObject.addProperty("result", "1");
	        	json = jsonObject.toString();
	        } else {
	        	User user = null;
				user = db.getUserByEmailPwd(email, password);
				if (user != null) {
		            JsonElement jsonElement = gson.toJsonTree(user);
		            jsonElement.getAsJsonObject().addProperty("result", "0");
		           	json = gson.toJson(jsonElement);
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
