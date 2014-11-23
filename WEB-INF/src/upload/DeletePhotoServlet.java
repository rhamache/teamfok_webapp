package upload;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import proj1.HTMLBuilder;
import security.SecurityController;


public class DeletePhotoServlet extends HttpServlet
{
	private static final long serialVersionUID = -8476558041967831765L;

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
		
		int id_to_del = Integer.parseInt(request.getQueryString());
		UploadController uc = null;
		
		try {
			uc = new UploadController();
			uc.deletePhoto(id_to_del);
		} catch (Exception e)
		{
			response.getWriter().println(e.getMessage());
			return;
		}
		
		
		response.sendRedirect("/proj1/display/myphotos");
	}
}
