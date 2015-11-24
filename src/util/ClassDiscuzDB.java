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

import javax.servlet.ServletContext;

import model.Course;
import model.User;

public class ClassDiscuzDB extends AbstractDBConnector{
	private final static String sqlProperties = Constants.SQL;
	private Properties prop;
	
	public ClassDiscuzDB (ServletContext sc) {
		prop = new Properties(); 
		try {
			prop.load(sc.getResourceAsStream("/WEB-INF/sql.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createUser(User newUser) throws SQLException {
		String sql = String.format(prop.getProperty("ADD_USER"),newUser.getEmail(),newUser.getPassword(),newUser.getName(),newUser.getFocus());
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}
	
	public boolean isValidEmail(String email) throws SQLException {
		boolean result = false;
		String sql = String.format(prop.getProperty("GET_USER_BY_EMAIL"),email);
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (!rs.next())
			result = true;
		rs.close();
		stmt.close();
		return result;
	}
	
	public User getUserByEmailPwd(String email, String password) throws SQLException {
		User user = new User();
		String sql = String.format(prop.getProperty("GET_USER_BY_EMAIL_PWD"),email,password);
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			Blob img = rs.getBlob("avatar");
			if (img != null) {
				user.setAvatar(img.getBytes(1, (int)img.length()));
				img.free();
			}
			user.setCollege(rs.getString("college"));
			user.setMajor(rs.getString("major"));
			user.setFocus(rs.getInt("focus"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
		} else {
			rs.close();
			stmt.close();
			return null;
		}
		rs.close();
		stmt.close();
		return user;
	}
	
	public User getUserById(int id) throws SQLException {
		User user = null;
		String sql = String.format(prop.getProperty("GET_USER_BY_ID"),id);
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			user = new User();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
//			Blob img = rs.getBlob("avatar");
//			if (img != null) {
//				user.setAvatar(img.getBytes(1, (int)img.length()));
//				img.free();
//			}
			byte[] bytes = rs.getBytes("avatar");
			if (bytes != null) {
				user.setAvatar(bytes);
			}
			user.setCollege(rs.getString("college"));
			user.setMajor(rs.getString("major"));
			user.setFocus(rs.getInt("focus"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
		}
		rs.close();
		stmt.close();
		return user;
	}
	
	public List<Integer> getCourseIdByStuId(int stuId) throws SQLException {
		List<Integer> result = new ArrayList<Integer>();

		String sql = String.format(prop.getProperty("GET_REG_COURSES_ID"),stuId);
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			result.add(rs.getInt("c_id"));
		}
		rs.close();
		stmt.close();
		return result;
	}
	
	public Course getCourseById(int id) throws SQLException {
		Course result = new Course();

		String sql = String.format(prop.getProperty("GET_COURSE_BY_ID"),id);
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			result.setId(id);
			result.setName(rs.getString("name"));
			result.setInstructor(rs.getString("instructor"));
			result.setNum(rs.getString("num"));
			result.setTime(rs.getString("time"));
			result.setLocation(rs.getString("location"));
		} else  {
			rs.close();
			stmt.close();
			return null;
		}
		rs.close();
		stmt.close();
		return result;
	}
	
	public List<Course> getCourseByStuId(int stuId) throws SQLException {
		List<Integer> courseIdList = getCourseIdByStuId(stuId);
		List<Course> courseList = new ArrayList<Course>();
		for (int index:courseIdList) {
			courseList.add(getCourseById(index));
		}
		return courseList;
	}
	
	public List<User> getUserInSameCourse(int courseId) throws SQLException {
		List<User> result = new ArrayList<User>();
		String sql = String.format(prop.getProperty("GET_USERS_IN_SAME_COURSE"),courseId);
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		List<Integer> stuIdList = new ArrayList<Integer>();
		while (rs.next()) {
			stuIdList.add(rs.getInt("s_id"));
		}
		
		for (int stuId:stuIdList) {	
			User user = getUserById(stuId);
			result.add(user);
		}
		rs.close();
		stmt.close();
		return result;
	}

	public List<Course> getAllCourses() throws SQLException {
		List<Course> courseList = new ArrayList<Course>();
		String sql = prop.getProperty("GET_ALL_COURSES");
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			Course course = new Course();
			course.setId(rs.getInt("id"));
			course.setName(rs.getString("name"));
			course.setInstructor(rs.getString("instructor"));
			course.setNum(rs.getString("num"));
			course.setTime(rs.getString("time"));
			course.setLocation(rs.getString("location"));
			courseList.add(course);
		}
		rs.close();
		stmt.close();
		return courseList;
	}

	public void editUser(int studentId, String name, byte[] avatar, String college, String major) throws SQLException {
		String sql = prop.getProperty("UPDATE_USER");
        PreparedStatement stmt = con.prepareStatement(sql);
        
        stmt.setString(1, name);
        if (avatar != null)
        	stmt.setBinaryStream(2, new ByteArrayInputStream(avatar), avatar.length);
        else
        	stmt.setBinaryStream(2, null);
        stmt.setString(3, college);
        stmt.setString(4, major);
        stmt.setInt(5, studentId);       

        stmt.executeUpdate();
		stmt.close();
	}
	
	public void updateFocus(int studentId, int focus) throws SQLException {
		String sql = String.format(prop.getProperty("UPDATE_FOCUS"),focus,studentId);
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}

	public boolean isReg(int courseId, int studentId) throws SQLException {
		boolean result = false;
		String sql = String.format(prop.getProperty("IS_REG_THIS_COURSE"),courseId,studentId);
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next())
			result = true;
		rs.close();
		stmt.close();
		return result;
	}
	
	public void regCourse(int courseId, int studentId) throws SQLException {
		String sql = String.format(prop.getProperty("REG_COURSE"),courseId,studentId);
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}

	public void dropCourse(int courseId, int studentId) throws SQLException {
		String sql = String.format(prop.getProperty("DROP_COURSE"),courseId,studentId);
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}
}
