package usermanagement;

import java.io.*;
import java.sql.SQLException;

import javax.servlet.*;
import javax.servlet.http.*;

import proj1.HTMLBuilder;

import security.SecurityController;


public class LoginServlet extends HttpServlet
{
	private static final long serialVersionUID = -6793869407295943400L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
      {
		  HTMLBuilder html = new HTMLBuilder();
		  
		  // Display the login page
		  if (!SecurityController.isLoggedIn(request.getSession())) {
			  ServletContext context = getServletContext();
			  String path = context.getRealPath("/html/login.html");
			  
			  html.buildCompleteFromFile(path, false);
		  } else {
			  html.makeHeader();
			  html.makeMenu(true);
			  html.appendHTML("<h2>You are already logged in, "+request.getSession().getAttribute("username")+"</h2>");
			  html.appendHTML("<p>Want to logout? Click <a href = \"/html/logout.html\">here.</a></p>");
			  html.makeFooter();
		  }
		  
		  html.putInResponse(response);
      }
	  
	  public void doPost(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
	  {
		  String username = request.getParameter("USERID");
		  String password = request.getParameter("PASSWD");
		  String passhash = "" + password.hashCode();
		  PrintWriter out = response.getWriter();
		  
		  
	      // Set response content type
	      response.setContentType("text/html");
	      
	      LoginController ldbc = null;
	      try {
	    	  ldbc = new LoginController();
	      } catch (Exception e) {
	    	  out.println(e.getMessage());
	    	  return;
	      }
	      
	      HttpSession session = request.getSession(true);

	      boolean good_credentials = false;
	      try
	      {
	    	  good_credentials = ldbc.checkCredentials(username, passhash);
	      } catch (SQLException e)
	      {
			out.println("Exception: "+e.getMessage());
			return;
	      }
	      
	      if (good_credentials)
	      {
		      session.setAttribute("username", username);
		      response.sendRedirect("home");
	      } else {
		      out.println("<h2>Incorrect login information. <a href = \"login.html\">Try Again.</a></h2>");
	      }
	      
	      try
	      {
	    	  ldbc.close();
	      } catch (SQLException e)
	      {
	    	  out.println("Exception: "+e.getMessage());
	      }
	  }
}
