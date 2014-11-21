package usermanagement;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import proj1.HTMLBuilder;

import security.SecurityController;


public class LogoutServlet extends HttpServlet
{
	private static final long serialVersionUID = -4389352604727557429L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException
		      {
		  			HTMLBuilder html = new HTMLBuilder();
	  				html.makeHeader();
	  				html.makeMenu(false);
		  			
		  			if(SecurityController.isLoggedIn(request.getSession()))
		  			{
		  				request.getSession().removeAttribute("username");
		  				html.makeBody("You have successfully logged out.");
		  			} else {
		  				html.makeBody("Error: you are not logged in!");
		  			}
		  			
		  			html.makeFooter();
		  			
		  			html.putInResponse(response);
		      }
}
