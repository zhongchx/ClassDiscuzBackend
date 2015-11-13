package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.Course;
import model.User;
import util.ClassDiscuzDB;

/**
 * Servlet implementation class UserInSameCourse
 */
@WebServlet("/usersincourse")
public class UserInSameCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserInSameCourse() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int courseId = Integer.parseInt(request.getParameter("courseId"));
		
		Gson gson = new Gson();
        ClassDiscuzDB db = new ClassDiscuzDB();
        db.connectDB();
        List<User> userList = db.getUserInSameCourse(courseId);
        String json = gson.toJson(userList);
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
