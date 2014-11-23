package upload;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import display.DisplayController;

import proj1.HTMLBuilder;
import proj1.Photo;

import security.SecurityController;


public class MyPhotosServlet extends HttpServlet
{
	private static final long serialVersionUID = 4778202974494951526L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		HTMLBuilder html = new HTMLBuilder();
		html.makeHeader();
		
		if (!SecurityController.isLoggedIn(request.getSession()))
		{
			html.makeMenu(false);
			html.appendHTML("Error: not logged in.");
			html.makeFooter();
			html.putInResponse(response);
			return;
		}
		
		html.makeMenu(true);
		String user = request.getSession().getAttribute("username").toString();
		
		DisplayController dc = null;
		ArrayList<Photo> myphotos = new ArrayList<Photo>();
		
		try
		{
			dc = new DisplayController();
			myphotos = dc.getOwnedPhotos(user);
		} catch (Exception e)
		{
			html.appendHTML(e.getMessage());
			html.makeFooter();
			html.putInResponse(response);
			return;
		}
		
		html.appendHTML(dc.createHTML(myphotos, 0, user));
		html.makeFooter();
		html.putInResponse(response);
	}
}
