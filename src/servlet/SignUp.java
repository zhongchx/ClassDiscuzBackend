package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.User;
import util.ClassDiscuzDB;

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

	        ClassDiscuzDB db = new ClassDiscuzDB();
	        db.connectDB();
	        String json = null;
	        if (db.isValidEmail(email)) {
	        	db.createUser(newUser);
	            User user = db.getUserByEmailPwd(email, password);
	            json = gson.toJson(user);
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
