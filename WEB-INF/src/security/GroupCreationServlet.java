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
			html.makeMenu(request.getSession());
			html.appendHTML("Can't create a group if not logged in.");
			html.makeFooter();
			html.putInResponse(response);
			return;
		}
		
		html.makeMenu(request.getSession());
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
			html.makeMenu(request.getSession());
			html.appendHTML("Can't create a group if not logged in.");
			html.makeFooter();
			html.putInResponse(response);
			return;
		}
		
		html.makeMenu(request.getSession());
		
		SecurityController sc = null;
		String gname = request.getParameter("GNAME");
		
		try
		{
			sc = new SecurityController();
			if (!gname.isEmpty())
			{
				sc.createGroup(gname, request.getSession().getAttribute("username").toString());
			}
			else
			{
				html.appendHTML("Group name cannot be blank!");
				html.appendHTML("<a href = \"/proj1/groups.html\">Go back</a>");
				html.makeFooter();
				html.putInResponse(response);
				return;
			}
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
