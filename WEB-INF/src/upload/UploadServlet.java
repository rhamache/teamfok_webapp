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
			html.makeMenu(request.getSession());
			
			UploadController udbc = null;
			try
			{
				udbc = new UploadController();
			} catch (Exception e1)
			{
				html.appendHTML(e1.getMessage());
				html.putInResponse(response);
				return;
			} 
	

			
			ServletContext context = getServletContext();
			String path = context.getRealPath("/html/upload.html");
			html.buildFromFile(path);
			html.appendHTML(udbc.createGroupSelector(request.getSession().getAttribute("username").toString()));

			html.appendHTML("<a href=\"display/myphotos\">My Uploaded Photos</a>");
			html.makeFooter();
			
			
			try
			{
				udbc.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			
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
		
		ServletContext context = getServletContext();
		String path = context.getRealPath("/html/upload.html");
		
		if (!SecurityController.isLoggedIn(request.getSession()))
		{
			html.makeMenu(request.getSession());
			html.appendHTML("Can't upload an image if not logged in.");
			html.makeFooter();
			html.putInResponse(response);
			return;
		}
		
		html.makeMenu(request.getSession());

		UploadController udbc = null;
		int pic_id;
		String response_message;
		
		try
		{
			udbc = new UploadController();
		} catch (Exception e)
		{
			html.appendHTML(e.getMessage());
			html.makeFooter();
			html.putInResponse(response);
			return;
		} 

		try {
			//Parse the HTTP request to get the image stream
			@SuppressWarnings("deprecation")
			DiskFileUpload fu = new DiskFileUpload();
			List FileItems = fu.parseRequest(request);
	    
			// Process the uploaded items, assuming only 1 image file uploaded
			Iterator i = FileItems.iterator();
			ArrayList<InputStream> jpg_instreams = new ArrayList<InputStream>(0);
			ArrayList<InputStream> gif_instreams = new ArrayList<InputStream>(0);
			
			
			while (i.hasNext()) {
		        FileItem item = (FileItem) i.next();
		        if (item.isFormField()) {
		            if (item.getFieldName().equals("SUB"))
		            	subject = item.getString();
		            if (item.getFieldName().equals("PLACE"))
		            	place = item.getString();
		            if (item.getFieldName().equals("DES"))
		            	description = item.getString();
		            if (item.getFieldName().equals("GROUP"))
		            	privacy = item.getString();
		        } else if (item.getFieldName().equals("FILEP"))
		        {
		        	if (!item.getString().isEmpty())
		        	{
		        		if (item.getContentType().equals("image/jpeg"))
		        			jpg_instreams.add(item.getInputStream());
		        		else if (item.getContentType().equals("image/gif"))
		        			gif_instreams.add(item.getInputStream());
		        		else
		        			html.appendHTML("<h2>You selected one or more files that is of a unsupported type (jpg or gif only).</h2>");
		        	}
		        }

		    }
			
			if (gif_instreams.size() == 0 && jpg_instreams.size() == 0)
			{
				html.buildFromFile(path);
				html.appendHTML(udbc.createGroupSelector(request.getSession().getAttribute("username").toString()));
				html.appendHTML("<h2>You didn't specify an image!</h2>");
				html.appendHTML("<a href=\"display/myphotos\">My Uploaded Photos</a>");
				html.makeFooter();
				html.putInResponse(response);
				return;
			}
			
			if (subject == null || subject.equals("")) { subject = "N/A"; }
			if (place == null || place.equals("")) { place = "N/A"; }
			if (description == null || description.equals("")) { description = "N/A"; }
	    
		
       
            ArrayList<String> infoBundle = new ArrayList<String>();
            infoBundle.add(request.getSession().getAttribute("username").toString());
            infoBundle.add(privacy);
            infoBundle.add(subject);
            infoBundle.add(place);
            infoBundle.add(description);        
       
            for (InputStream in : jpg_instreams)
            {
            	pic_id = udbc.getPicId();
            	
            	udbc.writeBlob(pic_id, infoBundle, in, 0);

	    		in.close();
            }
            
            for (InputStream in : gif_instreams)
            {
            	pic_id = udbc.getPicId();
            	
            	udbc.writeBlob(pic_id, infoBundle, in, 1);

	    		in.close();
            }

	    	
	    	udbc.executeSQLTextStatement("commit");
            response_message = "<h2>Upload OK!</h2>";

		} catch( Exception ex ) {
			//System.out.println( ex.getMessage());
			response_message="ex";
			ex.printStackTrace(response.getWriter());
		}

		//Output response to the client
		response.setContentType("text/html");
		html.buildFromFile(path);
		html.appendHTML(udbc.createGroupSelector(request.getSession().getAttribute("username").toString()));
		
		try
		{
			udbc.close();
		} catch (SQLException e)
		{
			html.appendHTML(e.getMessage());
		}
		
		html.makeBody(response_message);
		html.appendHTML("<a href=\"display/myphotos\">My Uploaded Photos</a>");
		html.putInResponse(response);
		
		}
}
