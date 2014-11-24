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
								"FROM images i " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') AND timing >= '"+from+"' " +
								"GROUP BY EXTRACT(year from timing), subject " +
								"ORDER BY EXTRACT(year from timing) ");
			        	stmt = conn.createStatement();
			        	rset = stmt.executeQuery(query);
			        	return rset;
					}
					if (!fromExists){
						query = ("SELECT  EXTRACT(year from timing), subject, COUNT(*) " +
								"FROM images i " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') " +
								"GROUP BY EXTRACT(year from timing), subject " +
								"ORDER BY EXTRACT(year from timing) ");
			        	stmt = conn.createStatement();
			        	rset = stmt.executeQuery(query);
			        	return rset;
					}
					if (!toExists){
						query = ("SELECT  EXTRACT(year from timing), subject, COUNT(*) " +
								"FROM images i " +
								"WHERE timing >= '"+from+"' " +
								"GROUP BY EXTRACT(year from timing), subject " +
								"ORDER BY EXTRACT(year from timing) ");
			        	stmt = conn.createStatement();
			        	rset = stmt.executeQuery(query);
			        	return rset;
					}
				}
				if (monthly)
				{
					if (toExists && fromExists)
					{
						query = ("SELECT  to_date(timing, 'IW'), subject, COUNT(*) " +
								"FROM images i " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') AND timing >= '"+from+"' " +
								"GROUP BY to_date(timing, 'IW'), subject " +
								"ORDER BY to_date(timing, 'IW') ");
			        	stmt = conn.createStatement();
			        	rset = stmt.executeQuery(query);
			        	return rset;
					}
					if (!fromExists){
						query = ("SELECT  to_date(timing, 'IW'), subject, COUNT(*) " +
								"FROM images i " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') " +
								"GROUP BY to_date(timing, 'IW'), subject " +
								"ORDER BY to_date(timing, 'IW') ");
			        	stmt = conn.createStatement();
			        	rset = stmt.executeQuery(query);
			        	return rset;
					}
					if (!toExists){
						query = ("SELECT  to_date(timing, 'IW'), subject, COUNT(*) " +
								"FROM images i " +
								"WHERE timing >= '"+from+"' " +
								"GROUP BY to_date(timing, 'IW'), subject " +
								"ORDER BY to_date(timing, 'IW') ");
			        	stmt = conn.createStatement();
			        	rset = stmt.executeQuery(query);
			        	return rset;
					}
				}
				if (weekly)
				{
					if (toExists && fromExists)
					{
						query = ("SELECT  to_date(timing, 'IW'), subject, COUNT(*) " +
								"FROM images i " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') AND timing >= '"+from+"' " +
								"GROUP BY to_date(timing, 'IW'), subject " +
								"ORDER BY to_date(timing, 'IW') ");
			        	stmt = conn.createStatement();
			        	rset = stmt.executeQuery(query);
			        	return rset;
					}
					if (!fromExists){
						query = ("SELECT  to_date(timing, 'IW'), subject, COUNT(*) " +
								"FROM images i" +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') " +
								"GROUP BY to_date(timing, 'IW'), subject " +
								"ORDER BY to_date(timing, 'IW') ");
			        	stmt = conn.createStatement();
			        	rset = stmt.executeQuery(query);
			        	return rset;
					}
					if (!toExists){
						query = ("SELECT  to_date(timing, 'IW'), subject, COUNT(*) " +
								"FROM images i " +
								"WHERE timing >= '"+from+"' " +
								"GROUP BY to_date(timing, 'IW'), subject " +
								"ORDER BY to_date(timing, 'IW') ");
			        	stmt = conn.createStatement();
			        	rset = stmt.executeQuery(query);
			        	return rset;
					}
				}
					
			}
		if (users)
		{	
			if (yearly)
			{
				if (toExists && fromExists)
				{
					query = ("SELECT  EXTRACT(year from timing), user, COUNT(*) " +
							"FROM images i " +
							"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') AND timing >= '"+from+"' " +
							"GROUP BY EXTRACT(year from timing), user " +
							"ORDER BY EXTRACT(year from timing) ");
		        	stmt = conn.createStatement();
		        	rset = stmt.executeQuery(query);
		        	return rset;
				}
				if (!fromExists){
					query = ("SELECT  EXTRACT(year from timing), user, COUNT(*) " +
							"FROM images i " +
							"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') " +
							"GROUP BY EXTRACT(year from timing), user " +
							"ORDER BY EXTRACT(year from timing) ");
		        	stmt = conn.createStatement();
		        	rset = stmt.executeQuery(query);
		        	return rset;
				}
				if (!toExists){
					query = ("SELECT  EXTRACT(year from timing), user, COUNT(*) " +
							"FROM images i " +
							"WHERE timing >= '"+from+"' " +
							"GROUP BY EXTRACT(year from timing), user " +
							"ORDER BY EXTRACT(year from timing) ");
		        	stmt = conn.createStatement();
		        	rset = stmt.executeQuery(query);
		        	return rset;
				}
			}
			if (monthly)
			{
				if (toExists && fromExists)
				{
					query = ("SELECT  to_date(timing, 'IW'), user, COUNT(*) " +
							"FROM images i " +
							"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') AND timing >= '"+from+"' " +
							"GROUP BY to_date(timing, 'IW'), user " +
							"ORDER BY to_date(timing, 'IW') ");
		        	stmt = conn.createStatement();
		        	rset = stmt.executeQuery(query);
		        	return rset;
				}
				if (!fromExists){
					query = ("SELECT  to_date(timing, 'IW'), user, COUNT(*) " +
							"FROM images i " +
							"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') " +
							"GROUP BY to_date(timing, 'IW'), user " +
							"ORDER BY to_date(timing, 'IW') ");
		        	stmt = conn.createStatement();
		        	rset = stmt.executeQuery(query);
		        	return rset;
				}
				if (!toExists){
					query = ("SELECT  to_date(timing, 'IW'), user, COUNT(*) " +
							"FROM images i " +
							"WHERE timing >= '"+from+"' " +
							"GROUP BY to_date(timing, 'IW'), user " +
							"ORDER BY to_date(timing, 'IW') ");
		        	stmt = conn.createStatement();
		        	rset = stmt.executeQuery(query);
		        	return rset;
				}
			}
			if (weekly)
			{
				if (toExists && fromExists)
				{
					query = ("SELECT  to_date(timing, 'IW'), user, COUNT(*) " +
							"FROM images i" +
							"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') AND timing >= '"+from+"' " +
							"GROUP BY to_date(timing, 'IW'), user " +
							"ORDER BY to_date(timing, 'IW') ");
		        	stmt = conn.createStatement();
		        	rset = stmt.executeQuery(query);
		        	return rset;
				}
				if (!fromExists){
					query = ("SELECT  to_date(timing, 'IW'), user, COUNT(*) " +
							"FROM images i " +
							"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') " +
							"GROUP BY to_date(timing, 'IW'), user " +
							"ORDER BY to_date(timing, 'IW') ");
		        	stmt = conn.createStatement();
		        	rset = stmt.executeQuery(query);
		        	return rset;
				}
				if (!toExists){
					query = ("SELECT  to_date(timing, 'IW'), user, COUNT(*) " +
							"FROM images i " +
							"WHERE timing >= '"+from+"' " +
							"GROUP BY to_date(timing, 'IW'), user " +
							"ORDER BY to_date(timing, 'IW') ");
		        	stmt = conn.createStatement();
		        	rset = stmt.executeQuery(query);
		        	return rset;
				}
			}
				
		}	
			
			
		if (users)
			return rset;
		
		return rset;
		
		
	}
	
	
	
	
	
	
	
	
}
