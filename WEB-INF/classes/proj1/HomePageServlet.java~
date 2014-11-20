package proj1;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import security.SecurityModule;



public class HomePageServlet extends HttpServlet
{
	  public void doGet(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException
		      {
		  			PrintWriter out = response.getWriter();
		  			if (SecurityModule.isLoggedIn(request.getSession()))
		  			{
		  			  out.print("<html><body><h1>391 Photoshare Project</h1><h3>Welcome, "+request.getSession().getAttribute("username")+"!</h3><p><ul><li><a href = \"logout.html\">Logout</a></p></li></ul></p></body></html>");
		  			} else {
		  				out.print("<html><body><h1>391 Photoshare Project</h1><h3>Please Login to Access Site Functionality<p><ul><li><a href = \"login.html\">Login</a></p></li></ul></p></body></html>");
		  			}
		      }
}
