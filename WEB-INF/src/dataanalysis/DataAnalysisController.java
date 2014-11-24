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
					query = ("SELECT COUNT(*)" +
								"FROM images i" +
								"WHERE timing >='"+to+"' AND timing <= '"+from+"'" +
								"GROUP BY subjects" +
								"ORDER BY datepart(year, timing)");
						
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
