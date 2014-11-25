package dataanalysis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import proj1.DatabaseController;


public class DataAnalysisController extends DatabaseController 
{
	public DataAnalysisController() throws SQLException, ClassNotFoundException,
	InstantiationException, IllegalAccessException
	{

		super();
// TODO Auto-generated constructor stub
	}
	
	public ResultSet dataAnalysis(String filter, String from, String to, String period) throws SQLException
	{
		boolean subject = filter.equals("subject");
		boolean users = filter.equals("users");
		boolean yearly = period.equals("yearly");
		boolean monthly = period.equals("monthly");
		boolean weekly = period.equals("weekly");
		boolean fromExists = !from.isEmpty();
		boolean toExists = !to.isEmpty();

		String query = null;

		Statement stmt = null; ResultSet rset = null;
		if (subject)
			{	
				if (yearly)
				{
					if (toExists && fromExists)
					{
						query = ("SELECT  EXTRACT(year from timing), subject, COUNT(*) " +
								"FROM images " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') AND timing >= '"+from+"' " +
								"GROUP BY EXTRACT(year from timing), subject " +
								"ORDER BY EXTRACT(year from timing) ");
					}
					if (!fromExists){
						query = ("SELECT  EXTRACT(year from timing), subject, COUNT(*) " +
								"FROM images " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') " +
								"GROUP BY EXTRACT(year from timing), subject " +
								"ORDER BY EXTRACT(year from timing) ");
					}
					if (!toExists){
						query = ("SELECT  EXTRACT(year from timing), subject, COUNT(*) " +
								"FROM images " +
								"WHERE timing >= '"+from+"' " +
								"GROUP BY EXTRACT(year from timing), subject " +
								"ORDER BY EXTRACT(year from timing) ");
					}
				}
				if (monthly)
				{
					if (toExists && fromExists)
					{
						query = ("SELECT  EXTRACT(month from timing), subject, COUNT(*) " +
								"FROM images " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') AND timing >= '"+from+"' " +
								"GROUP BY EXTRACT(month from timing), subject " +
								"ORDER BY EXTRACT(month from timing) ");
					}
					if (!fromExists){
						query = ("SELECT  EXTRACT(month from timing)), subject, COUNT(*) " +
								"FROM images " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') " +
								"GROUP BY EXTRACT(month from timing), subject " +
								"ORDER BY EXTRACT(month from timing) ");
					}
					if (!toExists){
						query = ("SELECT  EXTRACT(month from timing), subject, COUNT(*) " +
								"FROM images " +
								"WHERE timing >= '"+from+"' " +
								"GROUP BY EXTRACT(month from timing), subject " +
								"ORDER BY EXTRACT(month from timing) ");
					}
				}
				if (weekly)
				{
					if (toExists && fromExists)
					{
						query = ("SELECT  to_char(timing, 'IW'), subject, COUNT(*) " +
								"FROM images " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') AND timing >= '"+from+"' " +
								"GROUP BY to_char(timing, 'IW'), subject " +
								"ORDER BY to_char(timing, 'IW') ");
					}
					if (!fromExists){
						query = ("SELECT  to_char(timing, 'IW'), subject, COUNT(*) " +
								"FROM images " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') " +
								"GROUP BY to_char(timing, 'IW'), subject " +
								"ORDER BY to_char(timing, 'IW') ");
					}
					if (!toExists){
						query = ("SELECT  to_char(timing, 'IW'), subject, COUNT(*) " +
								"FROM images " +
								"WHERE timing >= '"+from+"' " +
								"GROUP BY to_char(timing, 'IW'), subject " +
								"ORDER BY to_char(timing, 'IW') ");
					}
				}
					
			}
		if (users)
		{	
			if (yearly)
			{
				if (toExists && fromExists)
				{
					query = ("SELECT  EXTRACT(year from timing), owner_name, COUNT(*) " +
							"FROM images " +
							"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') AND timing >= '"+from+"' " +
							"GROUP BY EXTRACT(year from timing), owner_name " +
							"ORDER BY EXTRACT(year from timing) ");
				}
				if (!fromExists){
					query = ("SELECT  EXTRACT(year from timing), owner_name, COUNT(*) " +
							"FROM images " +
							"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') " +
							"GROUP BY EXTRACT(year from timing), owner_name " +
							"ORDER BY EXTRACT(year from timing) ");
				}
				if (!toExists){
					query = ("SELECT  EXTRACT(year from timing), owner_name, COUNT(*) " +
							"FROM images " +
							"WHERE timing >= '"+from+"' " +
							"GROUP BY EXTRACT(year from timing), owner_name " +
							"ORDER BY EXTRACT(year from timing) ");
				}
			}
			if (monthly)
			{
				if (toExists && fromExists)
				{
					query = ("SELECT  EXTRACT(month from timing), owner_name, COUNT(*) " +
							"FROM images " +
							"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') AND timing >= '"+from+"' " +
							"GROUP BY EXTRACT(month from timing), owner_name " +
							"ORDER BY EXTRACT(month from timing) ");
				}
				if (!fromExists){
					query = ("SELECT  EXTRACT(month from timing), owner_name, COUNT(*) " +
							"FROM images " +
							"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') " +
							"GROUP BY EXTRACT(month from timing), owner_name " +
							"ORDER BY EXTRACT(month from timing) ");
				}
				if (!toExists){
					query = ("SELECT  EXTRACT(month from timing), owner_name, COUNT(*) " +
							"FROM images " +
							"WHERE timing >= '"+from+"' " +
							"GROUP BY EXTRACT(month from timing), owner_name " +
							"ORDER BY EXTRACT(month from timing) ");
				}
			}
			if (weekly)
			{
				if (toExists && fromExists)
				{
					query = ("SELECT  to_char(timing, 'IW'), owner_name, COUNT(*) " +
							"FROM images " +
							"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') AND timing >= '"+from+"' " +
							"GROUP BY to_char(timing, 'IW'), owner_name " +
							"ORDER BY to_char(timing, 'IW') ");
				}
				if (!fromExists){
					query = ("SELECT  to_char(timing, 'IW'), owner_name, COUNT(*) " +
							"FROM images " +
							"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') " +
							"GROUP BY to_char(timing, 'IW'), owner_name " +
							"ORDER BY to_char(timing, 'IW') ");
				}
				if (!toExists){
					query = ("SELECT  to_char(timing, 'IW'), owner_name, COUNT(*) " +
							"FROM images " +
							"WHERE timing >= '"+from+"' " +
							"GROUP BY to_char(timing, 'IW'), owner_name " +
							"ORDER BY to_char(timing, 'IW') ");
				}
			}
				
		}	
		
		System.out.println(query);
    	stmt = conn.createStatement();
    	rset = stmt.executeQuery(query);
    	

		return rset;
		
		
	}
	
	
	
	
	
	
	
	
}
