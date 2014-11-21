package proj1;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import security.SecurityController;



public class HomePageServlet extends HttpServlet
{
	private static final long serialVersionUID = 5508402861095873224L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException
		      {
					HTMLBuilder html = new HTMLBuilder();
					html.makeHeader();
					
		  			if (SecurityController.isLoggedIn(request.getSession()))
		  			{
		  				html.appendHTML("Welcome "+request.getSession().getAttribute("username")+"!");
		  				html.makeMenu(true);
		  			} else {
		  				html.makeMenu(false);
		  			}
		  			
		  			html.makeFooter();
		  			html.putInResponse(response);
		      }
}
