package Registration;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("name");
		String useremail = req.getParameter("email");
		String userpassword = req.getParameter("pass");
		String resetpassword = req.getParameter("re_pass");
		String usermobile = req.getParameter("contact");
		
		RequestDispatcher dispatcher = null;
		Connection con = null;
		
		if(username == null || username.equals("")) {
			req.setAttribute("status", "Invalid Username");
			dispatcher = req.getRequestDispatcher("registration.jsp");
			dispatcher.forward(req, resp);
		}
		
		if(useremail == null || useremail.equals("")) {
			req.setAttribute("status", "Invalid Email");
			dispatcher = req.getRequestDispatcher("registration.jsp");
			dispatcher.forward(req, resp);
		}
		
		if(userpassword == null || userpassword.equals("")) {
			req.setAttribute("status", "Invalid Password");
			dispatcher = req.getRequestDispatcher("registration.jsp");
			dispatcher.forward(req, resp);
		} else if(!userpassword.equals(resetpassword)) {
			req.setAttribute("status", "Invalid Confirm Password");
			dispatcher = req.getRequestDispatcher("registration.jsp");
			dispatcher.forward(req, resp);
		}
		
		if( usermobile== null || usermobile.equals("")) {
			req.setAttribute("status", "Invalid Mobile");
			dispatcher = req.getRequestDispatcher("registration.jsp");
			dispatcher.forward(req, resp);
		} else if(usermobile.length() < 10) {
			req.setAttribute("status", "Invalid Mobile Length");
			dispatcher = req.getRequestDispatcher("registration.jsp");
			dispatcher.forward(req, resp);
		}
		
		PrintWriter out = resp.getWriter();
		out.print(username);
		out.print(useremail);
		out.print(userpassword);
		out.print(usermobile);
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login?useSSL=false", "root", "06101997");
			
			PreparedStatement statement = con.prepareStatement("insert into users(username, usermail, userpassword, usermobile) values (?,?,?,?) ");
			statement.setString(1, username);
			statement.setString(2, useremail);
			statement.setString(3, userpassword);
			statement.setString(4, usermobile);
			
			int rowCount = statement.executeUpdate();
			dispatcher = req.getRequestDispatcher("registration.jsp");
			if(rowCount > 0) {
				req.setAttribute("status", "success");
				
			} else {
				req.setAttribute("status", "failed");
			}
			dispatcher.forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
