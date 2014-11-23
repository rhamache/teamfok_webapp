package display;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import proj1.HTMLBuilder;
import proj1.Photo;

import security.SecurityController;


public class DisplayServlet extends HttpServlet
{
	private static final long serialVersionUID = -3470464065209140353L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		HTMLBuilder html = new HTMLBuilder();
		html.makeHeader();
		
		int page = Integer.parseInt(request.getQueryString());
		
		if (!SecurityController.isLoggedIn(request.getSession())) 
		{
			html.makeMenu(false);
			html.appendHTML("Please <a href = \"/proj1/login.html\">login</a> to view photos.");
			html.makeFooter();
			html.putInResponse(response);
			return;
		}
		
		html.makeMenu(true);
		String user = request.getSession().getAttribute("username").toString();
		
		DisplayController dc = null;
		
		try
		{
			dc = new DisplayController();
		} catch (Exception e)
		{
			html.appendHTML(e.getMessage());
			html.putInResponse(response);
			return;
		}
		
		ArrayList<Photo> photos_to_disp = new ArrayList<Photo>();
		ArrayList<Photo> pop_photos = new ArrayList<Photo>();
		
		try
		{
			photos_to_disp = dc.getPhotos(user);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			pop_photos = dc.getFiveMostPopularPhotos();
		} catch (SQLException e)
		{
			html.appendHTML(e.getMessage());
		}
		
		html.appendHTML("<h1>Most Popular Photos</h1>");
		html.appendHTML(dc.createHTML(pop_photos, 0, user));
		
		
		
		Collections.sort(photos_to_disp, Collections.reverseOrder());
		html.appendHTML("<h1>Latest Photos (10 per page)</h1>");
		html.appendHTML(dc.createHTML(photos_to_disp, page*10, user));
		
		
		try
		{
			dc.close();
		} catch (SQLException e)
		{
			html.appendHTML(e.getMessage());
		}
		if ( (page) * 10 > 0)
		{
			html.appendHTML("<a href=\"display?"+(page-1)+"\">Previous Page</a>");
		}
		
		if ( (page+1) * 10 < photos_to_disp.size())
		{
			html.appendHTML("<a href=\"display?"+(page+1)+"\">Next Page</a>");
		}
		html.makeFooter();
		html.putInResponse(response);
		
	}

}
