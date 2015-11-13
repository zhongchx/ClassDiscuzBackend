package util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import model.Course;
import model.User;

public class ClassDiscuzDB extends AbstractDBConnector{
	private final static String sqlProperties = Constants.SQL;
	private Properties prop;
	
	public ClassDiscuzDB () {
		prop = new Properties(); 
		try {
//			System.out.println(new File(".").getAbsolutePath());
//			InputStream in = this.getClass().getClassLoader().getResourceAsStream("sql.properties");
			
			//Have to use absolute path, need to figure it out
			FileInputStream in = new FileInputStream("/Users/zhongchuan/Documents/Workspace/ClassDiscuzBackend/sql.properties"); 
			
			prop.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void createUser(User newUser) {
		try {
			String sql = String.format(prop.getProperty("ADD_USER"),newUser.getEmail(),newUser.getPassword(),newUser.getName(),newUser.getFocus());
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isValidEmail(String email) {
		boolean result = false;
		try {
			String sql = String.format(prop.getProperty("GET_USER_BY_EMAIL"),email);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (!rs.next())
				result = true;
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public User getUserByEmailPwd(String email, String password) {
		User user = new User();
		try {
			String sql = String.format(prop.getProperty("GET_USER_BY_EMAIL_PWD"),email,password);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				Blob img = rs.getBlob("avatar");
				user.setAvatar(img.getBytes(1, (int)img.length()));
				img.free();
				user.setCollege(rs.getString("college"));
				user.setMajor(rs.getString("major"));
				user.setFocus(rs.getInt("focus"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
			} else {
				stmt.close();
				return null;
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
        List<Integer> regCoursesId = getUserCourseId(user.getId());
        List<Course> regCourses = new ArrayList<Course>();
        for (int c : regCoursesId) {
        	Course course = getCourseById(c);
        	regCourses.add(course);
        }
        user.setCourses(regCourses);
		
		return user;
	}
	
//	public User getUserById
	
	public List<Integer> getUserCourseId(int id) {
		List<Integer> result = new ArrayList<Integer>();
		try {
			String sql = String.format(prop.getProperty("GET_REG_COURSES_ID"),id);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				result.add(rs.getInt("c_id"));
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Course getCourseById(int id) {
		Course result = new Course();
		try {
			String sql = String.format(prop.getProperty("GET_COURSE_BY_ID"),id);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				result.setId(id);
				result.setName(rs.getString("name"));
				result.setNum(rs.getString("num"));
				result.setTime(rs.getString("time"));
			} else  {
				stmt.close();
				return null;
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void regCourse(int courseId, int studentId) {
		try {
			String sql = String.format(prop.getProperty("REG_COURSE"),courseId,studentId);
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Course> getAllCourses() {
		List<Course> courseList = new ArrayList<Course>();
		String sql = prop.getProperty("GET_ALL_COURSES");
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Course course = new Course();
				course.setId(rs.getInt("id"));
				course.setName(rs.getString("name"));
				course.setNum(rs.getString("num"));
				course.setTime(rs.getString("time"));
				courseList.add(course);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courseList;
	}

	public void editUser(int studentId, String name, byte[] avatar, String college, String major) {
		try{
			
			String sql = prop.getProperty("UPDATE_USER");
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setString(1, name);
            stmt.setBinaryStream(2, new ByteArrayInputStream(avatar), avatar.length);
            stmt.setString(3, college);
            stmt.setString(4, major);
            stmt.setInt(5, studentId);       

            stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getUserInSameCourse(int studentId) {
		
	}

}
