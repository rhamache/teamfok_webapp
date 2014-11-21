package usermanagement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import proj1.HTMLBuilder;

import security.SecurityController;



public class RegistrationServlet extends HttpServlet
{
	private static final long serialVersionUID = -8917381672795327238L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
	  {
			HTMLBuilder html = new HTMLBuilder();
			html.makeHeader();
		
		  // Display the req page
		  if (!SecurityController.isLoggedIn(request.getSession())) {
			  ServletContext context = getServletContext();
			  String path = context.getRealPath("/html/registration.html");
			  
			  html.makeMenu(false);
			  html.buildFromFile(path);
		  } else {
			  html.appendHTML("<h2>You are already logged in, "+request.getSession().getAttribute("username")+"</h2>");
			  html.appendHTML("<p><a href = \"logout.html\">Logout</a> before creating a new account.</p>");
		  }
		  
		  html.makeFooter();
		  html.putInResponse(response);
	  }
	  
	  public void doPost(HttpServletRequest request, HttpServletResponse response)
	  throws IOException 
	  {
		  HTMLBuilder html = new HTMLBuilder();
		  
		  if (SecurityController.isLoggedIn(request.getSession()))
		  {
			  html.makeHeader();
			  html.makeMenu(true);
			  html.appendHTML("You can't register a new account while logged in. Logout first.");
			  html.makeFooter();
			  html.putInResponse(response);
			  return;
		  }
		  
		  ArrayList<String> fields = new ArrayList<String>(8);
		  
		  // grab all fields
		  fields.add(request.getParameter("USERID"));
		  fields.add(request.getParameter("PASSWD"));
		  fields.add(request.getParameter("RP_PASSWD"));
		  fields.add(request.getParameter("FNAME"));
		  fields.add(request.getParameter("LNAME"));
		  fields.add(request.getParameter("ADDRESS"));
		  fields.add(request.getParameter("EMAIL"));
		  fields.add(request.getParameter("PHONE"));
		  
		  PrintWriter out = response.getWriter();
		  RegistrationController rc =  null;
		  try
		  {
			  rc = new RegistrationController();
		  } catch (Exception e)
		  {
			  out.println(e.getMessage());
			  return;
		  }
		  
		  boolean isValid;
		  try
		  {
			  isValid = rc.clean_fields(fields);
		  } catch (SQLException e)
		  {
			  out.println(e.getMessage());
			  return;
		  }
		  
		  HttpSession session = request.getSession(true);
		  
		  if (isValid)
		  {
			  try
			{
				rc.addPersonAndUser(fields);
			} catch (SQLException e)
			{
				out.println(e.getMessage());
				return;
			}
		      session.setAttribute("username", fields.get(0));
		      response.sendRedirect("home");
		  } else {
			  ServletContext context = getServletContext();
			  String path = context.getRealPath("/html/registration.html");
			  html.makeHeader();
			  html.makeMenu(false);
			  html.appendHTML(rc.getFieldError());
			  html.buildFromFile(path);
			  html.putInResponse(response);
		  }
		  
		  try
		  {
			  rc.close();
		  } catch (SQLException e)
		  {
			  out.println(e.getMessage());
		  }


		  
	  }
}
