package display;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import oracle.jdbc.OracleResultSet;
import oracle.sql.BLOB;

import proj1.DatabaseController;
import proj1.Photo;


public class DisplayController extends DatabaseController
{

	public DisplayController() throws SQLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException
	{
		super();
	}

	public ArrayList<Photo> getPhotos(String username) throws SQLException
	{
		String query = "SELECT group_id FROM group_lists WHERE friend_id = '"+username+"'";
		
		Statement stmt = null; ResultSet rset = null;
		stmt = conn.createStatement();
		
		rset = stmt.executeQuery(query);
		
		ArrayList<Integer> permitted_groups = new ArrayList<Integer>();
		
		while (rset != null && rset.next())
		{
			permitted_groups.add(rset.getInt(1));
		}
		
		ArrayList<Photo> photos = new ArrayList<Photo>();
		
		photos.addAll(getPublicPhotos());
		photos.addAll(getPrivatePhotos(username));
		
		for (Integer i : permitted_groups)
			photos.addAll(getGroupPhotos(i.intValue()));
		
		return photos;
	}
	
	private ArrayList<Photo> getGroupPhotos(int id) throws SQLException
	{
		String query = "SELECT * FROM images WHERE permitted = "+id;
		Statement stmt = null; ResultSet rset = null;
		stmt = conn.createStatement();
		
		rset = stmt.executeQuery(query);
		ArrayList<Photo> photos = new ArrayList<Photo>();
		
		while (rset != null && rset.next())
		{
			photos.add(new Photo(rset.getInt(1), rset.getString(2), rset.getInt(3), 
								rset.getString(4), rset.getString(5), rset.getDate(6), 
								rset.getString(7), ((OracleResultSet) rset).getBLOB(8), ((OracleResultSet) rset).getBLOB(9)) );
		}
		
		return photos;
		
	}

	public ArrayList<Photo> getPublicPhotos() throws SQLException
	{
		String query = "SELECT * FROM images WHERE permitted = 1";
		
		Statement stmt = null; ResultSet rset = null;
		stmt = conn.createStatement();
		
		rset = stmt.executeQuery(query);
		ArrayList<Photo> photos = new ArrayList<Photo>();
		
		while (rset != null && rset.next())
		{
			photos.add(new Photo(rset.getInt(1), rset.getString(2), rset.getInt(3), 
								rset.getString(4), rset.getString(5), rset.getDate(6), 
								rset.getString(7), ((OracleResultSet) rset).getBLOB(8), ((OracleResultSet) rset).getBLOB(9)) );
		}
		
		return photos;
	}
	
	public ArrayList<Photo> getPrivatePhotos(String username) throws SQLException
	{
		String query = "SELECT * FROM images WHERE permitted = 2 AND owner_name = '"+username+"'";
		Statement stmt = null; ResultSet rset = null;
		stmt = conn.createStatement();
		
		rset = stmt.executeQuery(query);
		ArrayList<Photo> photos = new ArrayList<Photo>();
		
		while (rset != null && rset.next())
		{
			photos.add(new Photo(rset.getInt(1), rset.getString(2), rset.getInt(3), 
								rset.getString(4), rset.getString(5), rset.getDate(6), 
								rset.getString(7), ((OracleResultSet) rset).getBLOB(8), ((OracleResultSet) rset).getBLOB(9)) );
		}
		
		return photos;
		
	}
	public ArrayList<Photo> getOwnedPhotos(String username) throws SQLException
	{
		String query = "SELECT * FROM images WHERE owner_name = '"+username+"'";
		Statement stmt = null; ResultSet rset = null;
		stmt = conn.createStatement();
		
		rset = stmt.executeQuery(query);
		ArrayList<Photo> photos = new ArrayList<Photo>();
		
		while (rset != null && rset.next())
		{
			photos.add(new Photo(rset.getInt(1), rset.getString(2), rset.getInt(3), 
								rset.getString(4), rset.getString(5), rset.getDate(6), 
								rset.getString(7), ((OracleResultSet) rset).getBLOB(8), ((OracleResultSet) rset).getBLOB(9)) );
		}
		
		return photos;
		
	}

	public String createHTML(ArrayList<Photo> photos, int start, String username)
	{
		StringWriter html = new StringWriter();
		PrintWriter pw = new PrintWriter(html);
		
		int i;
		pw.println("<table border =\"1\">");
		for (i = start; (i < photos.size() && i < (start+10)); i++)
		{
			Photo p = photos.get(i);
			int hits;
			try
			{
				hits = getNumberOfHits(p.id);
			} catch (SQLException e)
			{
				pw.println(e.getMessage());
				return html.getBuffer().toString();
			}
            pw.println("<tr><td style = \"min-width:300px;height:300px\"><a href=\"/proj1/display/getpic?"+p.id+"\">");
            pw.println("<img src=\"/proj1/display/getpic?tmb"+p.id +"\"></a></td>");
            pw.println("<td style = \"min-width:600px;height:300px\"><p>");
            pw.println("<b>Owner:</b> "+p.getOwnerName());
            pw.println("<b>Subject:</b> "+p.getSubject());
            pw.println("<b>Place:</b> "+p.getPlace());
            pw.println("<b>Date:</b> "+p.getDate().toString());
            pw.println("<b>Description:</b> "+p.getDescription());
            pw.println("<b>Unique hits:</b> "+hits);
            if (p.getOwnerName().equals(username))
            {
            	pw.println("<b><a href = \"/proj1/display/delete?"+p.id+"\">Delete</a></b> ");
            }
            pw.println("</p></td></tr>");
		}
		pw.println("</table>");
		
		return html.getBuffer().toString();
		
	}
	
	
	public int getNumberOfHits(int id) throws SQLException
	{
		String query = "SELECT uniq_hits FROM hitcounts WHERE photo_id = "+id;
		Statement stmt = null; ResultSet rset = null;
		
		stmt = conn.createStatement();
		rset = stmt.executeQuery(query);
		
		int hits = 0;
		while(rset != null && rset.next())
		{
			hits = rset.getInt(1);
		}
		
		
		return hits;
	}
	
	public ArrayList<Photo> getFiveMostPopularPhotos() throws SQLException
	{
		ArrayList<Photo> photos = new ArrayList<Photo>();
		
		String query = "SELECT photo_id FROM hitcounts ORDER BY uniq_hits DESC";
		Statement stmt = null; ResultSet rset = null;
		
		stmt = conn.createStatement();
		rset = stmt.executeQuery(query);
		
		int count = 0;
		while(rset != null && rset.next() && count < 5)
		{
			photos.add(getPhoto(rset.getInt(1)));
			count++;
		}
		
		
		return photos;
	}
	
	public Photo getPhoto(int id) throws SQLException
	{
		String query = "SELECT * FROM images WHERE photo_id = "+id;
		Statement stmt = null; ResultSet rset = null;
		stmt = conn.createStatement();
		
		rset = stmt.executeQuery(query);
		Photo p = null;
		
		while (rset != null && rset.next())
		{
			p = new Photo(rset.getInt(1), rset.getString(2), rset.getInt(3), 
								rset.getString(4), rset.getString(5), rset.getDate(6), 
								rset.getString(7), ((OracleResultSet) rset).getBLOB(8), ((OracleResultSet) rset).getBLOB(9));
		}
		
		return p;
	}

	public void writeImage(PrintWriter writer, int picid) throws SQLException, IOException
	{
		String query = "SELECT photo FROM images WHERE photo_id = "+picid;
		Statement stmt = null; ResultSet rset = null;
		
		stmt = conn.createStatement();
		rset = stmt.executeQuery(query);
		
	    if ( rset.next() ) 
	    {
	    	InputStream input = rset.getBinaryStream(1);	    
	    	int imageByte;
	    	while((imageByte = input.read()) != -1) 
	    	{
	    		writer.write(imageByte);
	    	}
	    	input.close();
	    } 
	    else 
	    	writer.println("no picture available");
	}
	
	public void writeThumbnailImage(PrintWriter writer, int picid) throws SQLException, IOException
	{
		String query = "SELECT thumbnail FROM images WHERE photo_id = "+picid;
		Statement stmt = null; ResultSet rset = null;
		
		stmt = conn.createStatement();
		rset = stmt.executeQuery(query);
		
	    if ( rset.next() ) 
	    {
	    	InputStream input = rset.getBinaryStream(1);	    
	    	int imageByte;
	    	while((imageByte = input.read()) != -1) 
	    	{
	    		writer.write(imageByte);
	    	}
	    	input.close();
	    } 
	    else 
	    	writer.println("no picture available");
	}
	
	public void addHit(String user, int picid) throws SQLException
	{
		String query = "SELECT COUNT(*) FROM hits WHERE user_name = '"+user+"' AND photo_id = "+picid;
		Statement stmt = null; ResultSet rset = null;
		
		stmt = conn.createStatement();
		rset = stmt.executeQuery(query);
		
		int count = 1;
		while(rset != null && rset.next())
		{
			count = rset.getInt(1);
		}
		if (count > 0)
			// hit is not unique
			return;
		else
		{
			String sql = "INSERT INTO hits VALUES('"+user+"', "+picid+")";
			String query2 = "SELECT uniq_hits FROM hitcounts WHERE photo_id = "+picid;
			// get current hits
			rset = stmt.executeQuery(query2);
			count = 0;
			while(rset != null && rset.next())
			{
				count = rset.getInt(1);
			}
			String sql2;
			if (count == 0)
				sql2 = "INSERT INTO hitcounts VALUES("+picid+", 1)";
			else
			{
				count++;
				sql2 = "UPDATE hitcounts SET uniq_hits = "+count+" WHERE photo_id = "+picid;
			}

			stmt.executeUpdate(sql);
			stmt.executeUpdate(sql2);
			stmt.executeUpdate("COMMIT");
		}
	}

}
