package upload;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;


import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.*;

import oracle.sql.BLOB;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

import proj1.HTMLBuilder;
import security.SecurityController;
import upload.UploadController;

public class UploadServlet extends HttpServlet
{	
	private static final long serialVersionUID = 8305115960180976389L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		if (!SecurityController.isLoggedIn(request.getSession())) {
			RequestDispatcher view = request.getRequestDispatcher("/html/login.html");
			view.forward(request, response);
		} else {
			HTMLBuilder html = new HTMLBuilder();
			html.makeHeader();
			html.makeMenu(true);
			ArrayList<Integer> userGroups = new ArrayList<Integer>();
			UploadController udbc = null;
			try
			{
				udbc = new UploadController();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			} catch (ClassNotFoundException e1)
			{
				e1.printStackTrace();
			} catch (InstantiationException e1)
			{
				e1.printStackTrace();
			} catch (IllegalAccessException e1)
			{
				e1.printStackTrace();
			}
			SecurityController sdbc = null;
			try
			{
				sdbc = new SecurityController();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			} catch (ClassNotFoundException e1)
			{
				e1.printStackTrace();
			} catch (InstantiationException e1)
			{
				e1.printStackTrace();
			} catch (IllegalAccessException e1)
			{
				e1.printStackTrace();
			}
			
			ServletContext context = getServletContext();
			String path = context.getRealPath("/html/upload.html");
			html.buildFromFile(path);
			String uname = request.getSession().getAttribute("username").toString();
			
			try
			{
				userGroups = udbc.gatherGroups(uname);
			} catch (SQLException e)
			{
				e.printStackTrace();
				html.appendHTML("udbc exception");
			}
			


			html.makeFooter();
			html.putInResponse(response);
		}
	}
    
	public void doPost(HttpServletRequest request,HttpServletResponse response)
		throws ServletException, IOException {
		
		String image_path = null;
		String subject = null;
		String place = null;
		String description = null;
		String privacy = null;
		
		HTMLBuilder html = new HTMLBuilder();
		html.makeHeader();
		
		if (!SecurityController.isLoggedIn(request.getSession()))
		{
			html.makeMenu(false);
			html.appendHTML("Can't upload an image if not logged in.");
			html.makeFooter();
			html.putInResponse(response);
			return;
		}
		
		html.makeMenu(true);

		UploadController udbc = null;
		int pic_id;
		String response_message;
		
		try
		{
			udbc = new UploadController();
		} catch (Exception e)
		{
			e.printStackTrace();
		} 

		try {
			//Parse the HTTP request to get the image stream
			@SuppressWarnings("deprecation")
			DiskFileUpload fu = new DiskFileUpload();
			List FileItems = fu.parseRequest(request);
	    
			// Process the uploaded items, assuming only 1 image file uploaded
			Iterator i = FileItems.iterator();
			InputStream instream = null;
			
			
			while (i.hasNext()) {
		        FileItem item = (FileItem) i.next();
		        if (item.isFormField()) {
		            if (item.getFieldName().equals("SUB"))
		            	subject = item.getString();
		            if (item.getFieldName().equals("PLACE"))
		            	place = item.getString();
		            if (item.getFieldName().equals("DES"))
		            	description = item.getString();
		            if (item.getFieldName().equals("privacy"))
		            	privacy = item.getString();
		        } else if (item.getFieldName().equals("FILEP"))
		        {
		        	instream = item.getInputStream();
		        }

		    }
			
			if (instream == null)
			{
				html.appendHTML("You didn't specify an image!");
				html.makeFooter();
				html.putInResponse(response);
				return;
			}
			
			if (subject == null || subject.equals("")) { subject = "N/A"; }
			if (place == null || place.equals("")) { place = "N/A"; }
			if (description == null || description.equals("")) { description = "N/A"; }
	    

            pic_id = udbc.getPicId();
       
            ArrayList<String> infoBundle = new ArrayList<String>();
            infoBundle.add(request.getSession().getAttribute("username").toString());
            infoBundle.add(privacy);
            infoBundle.add(subject);
            infoBundle.add(place);
            infoBundle.add(description);        
       
            udbc.writeBlob(pic_id, infoBundle, instream);

	    	instream.close();

	    	
	    	udbc.executeSQLTextStatement("commit");
            response_message = " Upload OK!  ";
            udbc.close();

		} catch( Exception ex ) {
			//System.out.println( ex.getMessage());
			response_message="ex";
			ex.printStackTrace(response.getWriter());
		}

		//Output response to the client
		response.setContentType("text/html");
		html.makeBody(response_message);
		html.putInResponse(response);
		
		}
}
