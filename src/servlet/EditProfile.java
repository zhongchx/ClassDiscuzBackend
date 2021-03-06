package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonObject;

import util.ClassDiscuzDB;

/**
 * Servlet implementation class EditProfile
 */
@WebServlet("/editprofile")
public class EditProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditProfile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        String name = request.getParameter("name");
        String college = request.getParameter("college");
        String major = request.getParameter("major");
        String imageStr = request.getParameter("avatar");
//		byte[] image = Base64.decodeBase64(imageStr);
        ClassDiscuzDB db = new ClassDiscuzDB(this.getServletContext());
        String json = null;
        JsonObject jsonObject = new JsonObject();
        try {
	        if(!db.connectDB()) {
	        	jsonObject.addProperty("result", "1");
	        	json = jsonObject.toString();
	        } else {
	        	db.editUser(studentId,name,imageStr.getBytes(),college,major);
	        	jsonObject.addProperty("result", "0");
	        	json = jsonObject.toString();
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
