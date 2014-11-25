package dataanalysis;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

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
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
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
		
		String filter = request.getParameter("filter-by");
		String from = request.getParameter("From");
		String to = request.getParameter("To");
		String period = request.getParameter("time-frame");
		
		
		DataAnalysisController ac = null;
		try
		{
			ac = new DataAnalysisController();
		} catch (Exception e)
		{
			html.appendHTML(e.getMessage());
		} 
		ResultSet rset = null;
		ResultSetMetaData rsmd = null;
		
		try {
			rset = ac.dataAnalysis(filter, from, to, period);
			rsmd = rset.getMetaData();
			ac.close();
		} catch (Exception e)
		{
			e.printStackTrace(response.getWriter());
		}
		
		int i = 1; String last_time_period = "";
		html.appendHTML("<table border = \"1\">");
		try{
			while (rset != null && rset.next())
			{
				if (!last_time_period.equals(rset.getString(1)))
					html.appendHTML("<tr><td style = \"min-width:100px;height:50px;\">"+rset.getString(1)+"</td>");
				else
					html.appendHTML("<tr><td style = \"min-width:100px;height:50px;\"></td>");
				for (i = 2; i <= rsmd.getColumnCount(); i++)
				{
					html.appendHTML("<td style = \"min-width:200px;\">");
					html.appendHTML(rset.getString(i));
					html.appendHTML("</td>");
				}
				last_time_period = rset.getString(1);

				html.appendHTML("</tr>");
			}
		}
		catch (SQLException e)
		{
			html.appendHTML("rset exception: "+e.getMessage()+" index: "+i);
		}
		
		html.appendHTML("</tr></table>");
		
		html.makeFooter();
		html.putInResponse(response);
		
		
	}
}
