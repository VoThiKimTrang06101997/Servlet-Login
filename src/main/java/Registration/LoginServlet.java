package Registration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String useremail= req.getParameter("username");
		String userpassword = req.getParameter("password");
		
		HttpSession session = req.getSession();
		RequestDispatcher dispatcher = null;
		
		if(useremail == null || useremail.equals("")) {
			req.setAttribute("status", "Invalid Email");
			dispatcher = req.getRequestDispatcher("login.jsp");
			dispatcher.forward(req, resp);
		}
		
		if(userpassword == null || userpassword.equals("")) {
			req.setAttribute("status", "Invalid Password");
			dispatcher = req.getRequestDispatcher("login.jsp");
			dispatcher.forward(req, resp);
		}
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login?useSSL=false", "root", "06101997");
			
			PreparedStatement statement = con.prepareStatement("select * from users where usermail = ? and userpassword = ?");
			statement.setString(1, useremail);
			statement.setString(2, userpassword);
			
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()) {
				session.setAttribute("name", resultSet.getString("username"));
				dispatcher = req.getRequestDispatcher("index.jsp");
			} else {
				req.setAttribute("status", "failed");
				dispatcher = req.getRequestDispatcher("login.jsp");
			}
			dispatcher.forward(req, resp);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
