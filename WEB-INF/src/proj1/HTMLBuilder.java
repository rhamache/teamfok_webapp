package proj1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletResponse;

public class HTMLBuilder
{
	private StringWriter bundle;
	private PrintWriter pw;
	
	
	
	
	public HTMLBuilder(){
		bundle = new StringWriter();
		pw = new PrintWriter(bundle);
	}
	
	
	public void makeHeader(){
		pw.println("<!DOCTYPE html>" + "<html>");
	}
	
	public void appendHTML(String text)
	{
		pw.println("<p>"+text+"</p>");
	}
	
	public void makeHeader(String text){
		pw.println("<!DOCTYPE html>" + "<html><p>"+text+"</p>");
	}
	
	public void makeMenu(boolean loggedIn) {
		pw.println("<style>");
		pw.println("ul {");
		pw.println("list-style-type: none;");
		pw.println("margin: 0;");
		pw.println("padding: 0;");
		pw.println("}");
		pw.println("</style>");
		pw.println("</head>");
		pw.println("<ul>");
		pw.println("<li><a href=\"/proj1\">Home</a></li>");
		if (loggedIn)
		{
			pw.println("<li><a href=\"logout.html\">Logout</a></li>");
			pw.println("<li><a href=\"search.html\">Search</a></li>");
			pw.println("<li><a href=\"groups.html\">Groups</a></li>");
		} else {
			pw.println("<li><a href=\"login.html\">Login</a></li>");
		}
		pw.println("</ul>");
		pw.println("<hr>");
	}
		
		
	public void makeBody(String bodyText){
				this.makeHeader();
                pw.println("<body>" + bodyText + "</body>");
                this.makeFooter();
        }

	public void makeFooter(){
		pw.println("</html>");
	}
	
	public String grabBundle(){
		return bundle.getBuffer().toString();
	}
	
	public void buildFromFile(String path) throws IOException
	{
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = null;
		try {
		/*BufferedReader*/ reader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) { pw.println(System.getProperty("user.dir")); return; }
		char[] buf = new char[1024];
		int bytes_read = 0;
		while ((bytes_read = reader.read(buf)) != -1) {
			String data = String.valueOf(buf, 0, bytes_read);
			fileData.append(data);
			buf = new char[1024];
		}
		
		reader.close();
		String str = fileData.toString();
		pw.print(str);
		
	}
	
	public void buildCompleteFromFile(String path, boolean loggedIn) {
		this.makeHeader();
		this.makeMenu(loggedIn);
		try
		{
			this.buildFromFile(path);
		} catch (IOException e)
		{
			pw.println(e.getMessage());
		}
		this.makeFooter();
	}
	
	public void putInResponse(HttpServletResponse res) throws IOException
	{
		PrintWriter out = res.getWriter();
		out.println(bundle.getBuffer());
	}
}