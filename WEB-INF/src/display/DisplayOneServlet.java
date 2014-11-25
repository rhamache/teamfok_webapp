package display;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import proj1.HTMLBuilder;


public class DisplayOneServlet extends HttpServlet
{
	private static final long serialVersionUID = -4999754823869442954L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{		
		String picid = request.getQueryString();

		DisplayController dc = null;
		
		try
		{
			dc = new DisplayController();
			int type_flag = 0;
			
			if ( picid.startsWith("tmb") )
			{
				type_flag = dc.getImageType(Integer.parseInt(picid.substring(3)));
				
				if (type_flag == 0) { response.setContentType("image/jpg"); }
				else if (type_flag == 1) { response.setContentType("image/gif"); }
				
				dc.writeThumbnailImage(response.getWriter(), Integer.parseInt(picid.substring(3))); 
			}
			else
			{
				type_flag = dc.getImageType(Integer.parseInt(picid));
				
				if (type_flag == 0) { response.setContentType("image/jpg"); }
				else if (type_flag == 1) { response.setContentType("image/gif"); }
				
				dc.writeImage(response.getWriter(), Integer.parseInt(picid));
				dc.addHit(request.getSession().getAttribute("username").toString(), Integer.parseInt(picid));
			}
			dc.close();
		} catch (Exception e)
		{
			response.getWriter().println(e.getMessage());
		}

	}

}
