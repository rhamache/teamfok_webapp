package security;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import proj1.HTMLBuilder;



public class GroupsServlet extends HttpServlet
{
	private static final long serialVersionUID = -1951953130336993286L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		HTMLBuilder html = new HTMLBuilder();
		html.makeHeader("Groups Page");
		
		if(SecurityController.isLoggedIn(request.getSession()))
		{
			html.makeMenu(true);
			ServletContext context = getServletContext();
			String path = context.getRealPath("/html/groups.html");
			
			html.buildFromFile(path);
			
			GroupsController gc = null;
			try
			{
				gc = new GroupsController();
			} catch (Exception e)
			{
				html.appendHTML(e.getMessage());
				html.makeFooter();
				html.putInResponse(response);
				return;
			} 
			
			Map<Integer, String> owned_groups = null;
			
			try
			{
				owned_groups = gc.getGroupsOwnedBy((String) request.getSession().getAttribute("username"));
				gc.close();
			} catch (SQLException e)
			{
				html.appendHTML(e.getMessage());
				html.makeFooter();
				html.putInResponse(response);
				return;
			}

			if (owned_groups != null)
			{
				Iterator it = owned_groups.entrySet().iterator();
				while (it.hasNext())
				{
					Map.Entry pairs = (Map.Entry) it.next(); 
					html.appendHTML("<a href = \"groups/"+pairs.getKey()+"\">"+pairs.getValue()+"</a>");
					it.remove();
				}
			}
			
			
			
			
		}
		else
		{
			html.makeMenu(false);
			html.appendHTML("You must <a href = \"login.html\">login</a> to access this page.");
		}
		
		
		html.makeFooter();
		html.putInResponse(response);
	}

}
