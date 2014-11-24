package search;

import java.io.*;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.servlet.*;
import javax.servlet.http.*;

import display.DisplayController;


import proj1.HTMLBuilder;
import proj1.Photo;

import security.*;


public class SearchServlet extends HttpServlet
{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
      {	  
		  		HTMLBuilder html = new HTMLBuilder();
		  		html.makeHeader();
		  		if (!SecurityController.isLoggedIn(request.getSession())) 
		  		{
		  			html.makeMenu(request.getSession());
		  			html.appendHTML("You are not logged in");
		  		}
		  		else
		  		{
		  			html.makeMenu(request.getSession());
			  
		  			ServletContext context = getServletContext();
		  			String path = context.getRealPath("/html/search.html");
			  
		  			html.buildFromFile(path);
		  		}
		  
		  		html.makeFooter();
		  		html.putInResponse(response);
		  
      }
	
	  public void doPost(HttpServletRequest request, HttpServletResponse response)
			  throws ServletException, IOException
			  {		HTMLBuilder html = new HTMLBuilder();
		  			html.makeHeader();
		  			
		  			if (!SecurityController.isLoggedIn(request.getSession())) 
		  			{
		  				html.makeMenu(request.getSession());
		  				html.appendHTML("You are not logged in");
		  				html.makeFooter();
		  				html.putInResponse(response);
		  				return;
		  			}
		  			html.makeMenu(request.getSession());
		  			
		  			ServletContext context = getServletContext();
		  			String path = context.getRealPath("/html/search.html");
			  
		  			html.buildFromFile(path);
		  			html.appendHTML("<hr>");
		  
		  			String user = request.getSession().getAttribute("username").toString();
		  			PrintWriter out = response.getWriter();
		  			String keywords = request.getParameter("query");
		  			String timebias = request.getParameter("Timebias");
		  			String from = request.getParameter("From");
		  			String to = request.getParameter("To");
		  			
		  			
		  			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		  			Date dfrom = null, dto = null;
		  			try
					{
		  				if (!from.isEmpty())
		  					dfrom = sdf.parse(from);
		  				if (!to.isEmpty())
		  					dto = sdf.parse(to);
					} catch (ParseException e)
					{
			  			html.appendHTML("Date in incorrect format.");
			  			html.makeFooter();
			  			html.putInResponse(response);
			  			return;
					}
		  			
		  			if (!to.isEmpty() && !from.isEmpty())
		  			{
		  				if (dfrom.compareTo(dto) > 0)
		  				{
				  			html.appendHTML("'From' date is after 'to' date.");
				  			html.makeFooter();
				  			html.putInResponse(response);
				  			return;
		  				}
		  			}
		  
		  			
	      
		  			SearchController sbc = null;
		  			DisplayController dc = null;
		  			SecurityController sc = null;
		  			
		  			try {
		  				sc = new SecurityController();
		  				} 	catch (Exception e1) {
		  					out.println("null");
		  					return;
		  				} 
		  			
		  			try {
		  				dc = new DisplayController();
		  				} 	catch (Exception e1) {
		  					out.println("null");
		  					return;
		  				} 
		  			try {
		  				sbc = new SearchController();
		  				}    catch (Exception e2) {
		  					out.println("null");
		  					return;
		  				}
		  			ArrayList<Photo> results = null;
		  			try
		  			{
		  				results = sbc.SearchMain(keywords, timebias, from, to);
		  			} catch (Exception e3)
		  			{
		  				out.println("Excep: "+e3.getMessage());
		  				return;
		  			}

		  			int i;
		  			boolean allowed = false;
		  			for (i = 0; i < results.size(); i++)
		  			{
		  				try {
		  						Photo photo = dc.getPhoto(results.get(i).id);
		  						results.set(i, photo);
		  						allowed = sc.userAllowedView(photo.getPermitted(), user, photo.getOwnerName());
		  						if (!allowed)
		  							results.remove(i);
		  						
		  						
		  					}
		  				
		  				catch (SQLException e4) {
							
		  						e4.printStackTrace();
		  					}
		  			}
		  			
		  			if ((!results.isEmpty())){
		  					html.appendHTML(dc.createHTML(results,0,user));
		  			}	else
		  					html.appendHTML("No Matches");
		  			
		  			response.setContentType("text/html");
		  			html.putInResponse(response);
			  }
			  	
}
		 
		