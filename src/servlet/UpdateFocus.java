package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.Course;
import util.ClassDiscuzDB;

/**
 * Servlet implementation class UpdateFocus
 */
@WebServlet("/updatefocus")
public class UpdateFocus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateFocus() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int studentId = Integer.parseInt(request.getParameter("studentId"));
		int focus = Integer.parseInt(request.getParameter("focus"));
		
        ClassDiscuzDB db = new ClassDiscuzDB(this.getServletContext());
		
        String json = null;
        JsonObject jsonObject = new JsonObject();
        try{
	        if (!db.connectDB()) {
	        	jsonObject.addProperty("result", "1");
	        	json = jsonObject.toString();
	        } else {
	        	db.updateFocus(studentId, focus);
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
