package search;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import proj1.DatabaseController;
import proj1.Photo;
import proj1.PhotoSort;



public class SearchController extends DatabaseController
{

	public SearchController() throws SQLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException
	{

		super();
		// TODO Auto-generated constructor stub
	}


	public ArrayList<Photo> SearchMain(String keywords, String timebias, String from, String to) throws SQLException, ParseException
	{	if (keywords == null)
				keywords = " ";
		String[] keywordarr = keywords.toLowerCase().split(" ");
		String[] photoargs  = {"owner_name","subject","place","description"};
		Set<Integer> photoIDarr =  new HashSet<Integer>();
		ArrayList<Photo> photoList = new ArrayList<Photo>();
		
		for (String keyword : keywordarr)				//For each keyword we will have to iterate through function
		{
			for (String arg : photoargs)				//For each argument we will have to iterate
			{	
				String query = null;
				if (!to.isEmpty() && !from.isEmpty()){
					query = "select photo_id from IMAGES where timing >= '"+from+"' AND timing <= '"+to+"' AND lower("+arg+") LIKE '%"+keyword+"%'";}
				if (!to.isEmpty() && from.isEmpty())
						query = "select photo_id from IMAGES where timing <= "+to+" AND lower("+arg+") LIKE '%"+keyword+"%'";
				if (to.isEmpty() && !from.isEmpty())
					query = "select photo_id from IMAGES where timing >= '"+from+"' AND lower("+arg+") LIKE '%"+keyword+"%'";
				if (to.isEmpty() && from.isEmpty())
					query = "select photo_id from IMAGES where lower("+arg+") LIKE '%"+keyword+"%'";
				
				Statement stmt = null; ResultSet rset = null;
	        	stmt = conn.createStatement();
	        	rset = stmt.executeQuery(query);

	        	while (rset != null && rset.next())
	        	{			
	        		boolean doesContain = photoIDarr.contains(new Integer(rset.getInt(1)));
	        		if (!doesContain)
	        		{	
	        			photoIDarr.add(new Integer(rset.getInt(1)));
	        			Photo photo = new Photo(new Integer(rset.getInt(1)));
	        			photoList.add(photo);
	        		}
	        	}
	        	
	        	int i;
		        for (i = 0; i < photoList.size(); i++)
		        	{	
		        		Photo photo = photoList.get(i);
		        		Integer rank = 0;
		        		query = "select "+arg+" from IMAGES where  photo_id = "+photo.id; //collect the argument from said photoID
		        		stmt = conn.createStatement();
		        		rset = stmt.executeQuery(query);
		        		rset.next();
		        		String relevArg = rset.getString(1);
		        		Pattern pattern = Pattern.compile(keyword); 
		        		Matcher matcher = pattern.matcher(relevArg);    //See how many times the keyword matches to said argument
		        		int count = 0;
		        		while (matcher.find()) 
		        			count++;
		        		
		        		
		        		if (arg.equals("subject"))
		        		{
		        			rank = 6*count;
		        		}
		        		else if (arg.equals("place"))
		        			rank = 3*count;
		        		else if (arg.equals("description"))
	      					rank = count;
		        		else if (arg.equals("owner_name"))
	      					rank = 0;
		        		
		        	query = "select timing from IMAGES where  photo_id = "+photo.id;
	        		stmt = conn.createStatement();
	        		rset = stmt.executeQuery(query);
	        		rset.next();	
	        		Date date = rset.getDate("timing");
	        		photo.setDate(date);
		        	int prevrank = photo.getRank();
		        	photo.setRank(prevrank+rank);
		        	
		        	photoList.set(i, photo);
		        	}
	        	}
	        }

		
		
		if (timebias.equals("Neither"))
			Collections.sort(photoList, PhotoSort.SEARCH_RANK_REVERSE);
		if (timebias.equals("Most-Recent"))
			Collections.sort(photoList, Collections.reverseOrder());
		if (timebias.equals("Least-Recent"))
			Collections.sort(photoList);
			
		
		return photoList;
        } 
}






		


