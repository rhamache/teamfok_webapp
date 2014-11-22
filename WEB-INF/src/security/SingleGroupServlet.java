package security;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import proj1.HTMLBuilder;


public class SingleGroupServlet extends HttpServlet
{
	private static final long serialVersionUID = -924711916943362793L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		HTMLBuilder html = new HTMLBuilder();
		html.makeHeader();
		html.makeMenu(SecurityController.isLoggedIn(request.getSession()));
		
        String[] pathInfo = request.getPathInfo().split("/");

        Integer group_id = Integer.parseInt(pathInfo[1]);
        
        try
		{
			this.renderPage(html, request, response, group_id);
		} catch (Exception e)
		{
			html.appendHTML(e.getMessage());
		}
        
        html.makeFooter();
        html.putInResponse(response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		HTMLBuilder html = new HTMLBuilder();
		html.makeHeader();
		html.makeMenu(SecurityController.isLoggedIn(request.getSession()));
		
        String[] pathInfo = request.getPathInfo().split("/");

        Integer group_id = Integer.parseInt(pathInfo[1]);
        
        SecurityController sc = null;
        
        try
		{
			sc = new SecurityController();
		} catch (Exception e)
		{
			html.appendHTML(e.getMessage());
		} 

		String person_to_delete = request.getParameter("Delete");
		if (person_to_delete != null)
		{
			String curr_user = request.getSession().getAttribute("username").toString();
			if (curr_user.equals(person_to_delete))
			{
				html.appendHTML("Can't delete yourself from a group you own (delete the group instead)");
			}
			else
			{
			
				try
				{
					sc.deleteMemberFrom(person_to_delete, group_id.intValue());
					html.appendHTML(person_to_delete+" deleted.");
				} catch (SQLException e)
				{
					html.appendHTML(e.getMessage());
				}
			}
		} else {
			String person_to_add = request.getParameter("USER_TO_ADD");
			String notice = request.getParameter("NOTICE");
			if (person_to_add != null)
			{
				boolean rval = false;
				try
				{
					rval = sc.addMemberTo(person_to_add, group_id.intValue(), notice);
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
				if (rval)
					html.appendHTML(person_to_add+" added.");
				else
					html.appendHTML("Can't add "+person_to_add+". Username doesn't exist.");
			}
		}
		
        try
		{
			this.renderPage(html, request, response, group_id);
		} catch (Exception e)
		{
			html.appendHTML(e.getMessage());
			return;
		} 
        
		
		html.makeFooter();
		html.putInResponse(response);
	}
	
	private void renderPage(HTMLBuilder html, HttpServletRequest request, HttpServletResponse response, Integer group_id) 
	throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException
	{
        SecurityController sc = null;
        boolean isOwner = false; String gname = null;
        
		sc = new SecurityController();
		isOwner = sc.isOwnerOf(request.getSession(), group_id);
		gname = sc.getGroupName(group_id.intValue());
		
        
        if (!isOwner)
        {
        	html.appendHTML("You can't edit a group you don't own.");
        	html.makeFooter();
        	html.putInResponse(response);
        	return;
        }
        
        List<String> members = null;
        

		members = sc.getMembersOf(group_id.intValue());
	
        
        html.appendHTML("<h1>Members of group "+gname+"</h1>");
        for (String name : members)
        {
            String html_str = "<form action = \"\" method = \"post\"><button name = \"Delete\" value=\""+name+"\" type=\"submit\">Delete</button></form>";
        	html.appendHTML("Member username: "+name+html_str);
        	
        	html.appendHTML("Notice: ");
        	html.appendHTML(sc.getNoticeText(name, group_id.intValue()));
        	
        	html.appendHTML("<br>");
        }
        
        sc.close();
        
		ServletContext context = getServletContext();
		String path = context.getRealPath("/html/add_person_form.html");
        
        html.buildFromFile(path);
	}
}
