package security;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import proj1.HTMLBuilder;


public class GroupCreationServlet extends HttpServlet
{
	private static final long serialVersionUID = -9195255043268361100L;
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		HTMLBuilder html = new HTMLBuilder();
		html.makeHeader();
		if (!SecurityController.isLoggedIn(request.getSession()))
		{
			html.makeMenu(false);
			html.appendHTML("Can't create a group if not logged in.");
			html.makeFooter();
			html.putInResponse(response);
			return;
		}
		
		html.makeMenu(true);
		ServletContext context = getServletContext();
		String path = context.getRealPath("/html/groupcreation.html");
		html.buildFromFile(path);
		html.makeFooter();
		html.putInResponse(response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		HTMLBuilder html = new HTMLBuilder();
		html.makeHeader();
		if (!SecurityController.isLoggedIn(request.getSession()))
		{
			html.makeMenu(false);
			html.appendHTML("Can't create a group if not logged in.");
			html.makeFooter();
			html.putInResponse(response);
			return;
		}
		
		html.makeMenu(true);
		
		SecurityController sc = null;
		String gname = request.getParameter("GNAME");
		
		try
		{
			sc = new SecurityController();
			if (gname != null)
				sc.createGroup(gname, request.getSession().getAttribute("username").toString());
			sc.close();
			
		} catch (Exception e)
		{
			html.appendHTML(e.getMessage());
			html.makeFooter();
			html.putInResponse(response);
			return;
		}
		
		response.sendRedirect("/proj1/groups.html");
	}
}
