package search;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import proj1.DatabaseController;
import javax.servlet.*;




public class SearchController extends DatabaseController
{

	public SearchController() throws SQLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException
	{

		super();
		// TODO Auto-generated constructor stub
	}

	

	public Set<String> keywordsearch(String keywords, String timebias, String timespace) throws SQLException
	{
		String[] keywordarr = keywords.toLowerCase().split(" ");
		String[] photoargs  = {"photo_id","owner_name","subject","place","description"};
		Set<String> photoIDarr =  new HashSet<String>();

		for (String keyword : keywordarr)
		{
			for (String arg : photoargs)
			{	
				String query = "select photo_id from IMAGES where lower("+arg+") LIKE '%"+keyword+"%'";
				System.out.println(query);
				Statement stmt = null; ResultSet rset = null;
	        	stmt = conn.createStatement();
	        	rset = stmt.executeQuery(query);
	        	
	        	while (rset.next()) 
	        	         photoIDarr.add(rset.getString(1));
	      	
			}
		}
			
		
		// TODO Auto-generated method stub
		return photoIDarr;
	}
}
