package proj1;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RawSQLServlet extends HttpServlet
{
	private static final long serialVersionUID = -7189552794396164530L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
	  {
		  response.setContentType("text/html");
		  // Display the login page
		  RequestDispatcher view = request.getRequestDispatcher("/html/sql_input.html");
		  view.forward(request, response);
	  }

	  public void doPost(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException
	  {	
		  PrintWriter out = response.getWriter();
		  ResultSet rset = null;
		  String search = request.getParameter("search");
		  /*
		 try {
			 //rset = sbc.executeSQLTextStatement(search);
		 } catch (SQLException e) {
			  out.println(e.getMessage());
		}*/
		
		  displayResultSet(out, rset);
	  }
	public static void displayResultSet( PrintWriter out, ResultSet rset ) {

		out.println("<table border = 1 alian>");
		String value = null;
		Object o = null;
		int type;
		Blob image;
	
		/* 
		 *  to generate the column labels
		 */
		try {
	
		    ResultSetMetaData rsetMetaData = rset.getMetaData();
		    int columnCount = rsetMetaData.getColumnCount();
	
		    out.println("<tr valign = \"top\">");
	
		    for ( int column = 1; column <= columnCount; column++) {
			value = rsetMetaData.getColumnLabel(column);
			out.print("<td>" + value + "</td>");
		    }
		    out.println("</tr>");
	
		    /*
		     *   generate answers, one tuple at a time
		     */
		    while (rset.next() ) {
			out.println("<tr valign = \"top\">");
			for ( int index = 1; index <= columnCount; index++) {
			    type= rsetMetaData.getColumnType(index);
	
			    if (type==Types.LONGVARBINARY||
				type==Types.BLOB||type==Types.CLOB) {
	
				out.println("<img src=\"/yuan/servlet/GetOnePic\"></a>");
				/*
				image= rset.getBlob(index);
				rese.setContentType("image/gif");
				InputStream input = rset.getBinaryStream(index);
				int imageByte;
				while((imageByte = input.read()) != -1) {
				    out.write(imageByte);
				}
				input.close();
				*/
	
			    }
			    else {
				o = rset.getObject(index);
				if (o != null )
				    value = o.toString();
				else 
				    value = "null";
				out.print("<td>" + value + "</td>");
			    }
			}
			out.println("</tr>");
		    }
		} catch ( Exception io ){ out.println(io.getMessage()); }
	
		out.println("</table>");
    }

}


