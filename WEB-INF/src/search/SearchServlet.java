package search;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.*;
import javax.servlet.http.*;


import proj1.HTMLBuilder;


import security.SecurityController;
import search.SearchController;

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
			  html.makeMenu(false);
			  html.appendHTML("You are not logged in");
		  }
		  else
		  {
			  html.makeMenu(true);
			  
			  ServletContext context = getServletContext();
			  String path = context.getRealPath("/html/search.html");
			  
			  html.buildFromFile(path);
		  }
		  
		  html.makeFooter();
		  html.putInResponse(response);
		  
      }
	  public void doPost(HttpServletRequest request, HttpServletResponse response)
			  throws ServletException, IOException
			  {
		  PrintWriter out = response.getWriter();
		  String keywords = request.getParameter("Keywords");
		  String mostrecent = request.getParameter("Most-Recent");
		  String leastrecent = request.getParameter("Least-Recent");
		  String neither = request.getParameter("Neither");
		  String from = request.getParameter("From");
		  String to = request.getParameter("To");
		  String timebias = "";
		  
		  if ((mostrecent != "") || (mostrecent != null))
				  timebias = mostrecent;
		  else
			  if ((leastrecent != "") || (leastrecent != null))
			  		timebias = leastrecent;
			  else
				     timebias = neither;
		  
		  response.setContentType("text/html");
	      
	      SearchController sbc = null;
	      try {
	    	  sbc = new SearchController();
	      } catch (Exception e) {
	    	  out.println(e.getMessage());
	    	  return;
	      }
	      HttpSession session = request.getSession(true);
	      int results = 0;
	      try
	      {
	    	results = sbc.SearchMain(keywords, timebias, from, to);
	      } catch (Exception e)
	      {
			out.println("Exception: "+e.getMessage());
			return;
	      }
	      
			  }
			  	
		 }
		 
		