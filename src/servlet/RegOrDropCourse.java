package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import util.ClassDiscuzDB;

/**
 * Servlet implementation class RegCourse
 */
@WebServlet("/regordrop")
public class RegOrDropCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegOrDropCourse() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int courseId = 0;
		int studentId = 0;
		try {
			courseId = Integer.parseInt(request.getParameter("courseId"));
			studentId = Integer.parseInt(request.getParameter("studentId"));
		} catch (NumberFormatException e) {
        	JsonObject jsonObject = new JsonObject();
        	jsonObject.addProperty("result", "1");
        	String json = jsonObject.toString();
        	response.getWriter().append(json);
        	return;
		}
        ClassDiscuzDB db = new ClassDiscuzDB(this.getServletContext());
        String json = null;
        JsonObject jsonObject = new JsonObject();
        try {
	        if (!db.connectDB()) {
	        	jsonObject.addProperty("result", "1");
	        	json = jsonObject.toString();
	        } else {
				if (db.isReg(courseId, studentId))
					db.dropCourse(courseId,studentId);
				else
					db.regCourse(courseId,studentId);
	        	
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
