package dataanalysis;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import proj1.HTMLBuilder;
import security.SecurityController;


public class AnalysisServlet extends HttpServlet
{
	private static final long serialVersionUID = -3782708227473400746L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		HTMLBuilder html = new HTMLBuilder();
	
		html.makeHeader();
		html.makeMenu(request.getSession());
		
		if(!SecurityController.isAdmin(request.getSession()))
		{
			html.appendHTML("Only the site admin can access this page.");
			html.makeFooter();
			html.putInResponse(response);
			return;
		}
		
		
		ServletContext context = getServletContext();
		String path = context.getRealPath("/html/analysis.html");
		html.buildFromFile(path);
		html.putInResponse(response);
	}
}
