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
			
			ServletContext context = getServletContext();
			String path = context.getRealPath("/html/upload.html");
			
			html.buildFromFile(path);
			
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
	    

            pic_id = udbc.getPicId();
       
            ArrayList<String> infoBundle = new ArrayList<String>();
            infoBundle.add(request.getSession().getAttribute("username").toString());
            infoBundle.add("1");
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
