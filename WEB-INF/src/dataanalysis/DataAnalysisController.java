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
	
	public ResultSet dataAnalysis(String user, String subject, String from, String to, String period) throws SQLException
	{
		boolean subjects = (!(subject == null));
		boolean users = (!(user == null));
		boolean yearly = period.equals("yearly");
		boolean monthly = period.equals("monthly");
		boolean weekly = period.equals("weekly");
		boolean fromExists = !from.isEmpty();
		boolean toExists = !to.isEmpty();

		String query = null;
		Statement stmt = null; ResultSet rset = null;
		String partitionQuery = null;
		String groupBy = null;
	

		if (!subjects){
			partitionQuery = "owner_name AS Username";
			groupBy = " owner_name ";}
		if (!users){
			partitionQuery = "subject AS Subject";
			groupBy = " subject ";
			}
		if (users && subjects){
			partitionQuery = "owner_name AS Username, subject AS Subject ";
			groupBy = " owner_name, subject ";}
		
		
		if (yearly)
				{
					if (toExists && fromExists)
					{
						query = ("SELECT  EXTRACT(year from timing) AS Year, "+ partitionQuery+ " , COUNT(*) AS Imagecount " +
								"FROM images " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') AND timing >= '"+from+"' " +
								"GROUP BY EXTRACT(year from timing),"+groupBy+"" +
								"ORDER BY EXTRACT(year from timing) ");
					}
					if (!fromExists){
						query = ("SELECT  EXTRACT(year from timing) AS Year, "+ partitionQuery+ " , COUNT(*) AS Imagecount " +
								"FROM images " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') " +
								"GROUP BY EXTRACT(year from timing),"+groupBy+"" +
								"ORDER BY EXTRACT(year from timing) ");
					}
					if (!toExists){
						query = ("SELECT  EXTRACT(year from timing) AS Year, "+ partitionQuery+ " , COUNT(*) AS Imagecount " +
								"FROM images " +
								"WHERE timing >= '"+from+"' " +
								"GROUP BY EXTRACT(year from timing),"+groupBy+"" +
								"ORDER BY EXTRACT(year from timing) ");
					}
				}
		if (monthly)
				{
					if (toExists && fromExists)
					{
						query = ("SELECT EXTRACT(year from timing) AS Year, EXTRACT(month from timing) AS Month, "+ partitionQuery+ " , COUNT(*) AS Imagecount " +
								"FROM images " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') AND timing >= '"+from+"' " +
								"GROUP BY EXTRACT(year from timing), EXTRACT(month from timing),"+groupBy+"" +
								"ORDER BY EXTRACT(year from timing) ");
					}
					if (!fromExists){
						query = ("SELECT EXTRACT(year from timing) AS Year, EXTRACT(month from timing) AS Month, "+ partitionQuery+ " , COUNT(*) AS Imagecount " +
								"FROM images " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') " +
								"GROUP BY EXTRACT(year from timing), EXTRACT(month from timing),"+groupBy+"" +
								"ORDER BY EXTRACT(year from timing) ");
					}
					if (!toExists){
						query = ("SELECT EXTRACT(year from timing) AS Year, EXTRACT(month from timing) AS Month, "+ partitionQuery+ " , COUNT(*) AS Imagecount " +
								"FROM images " +
								"WHERE timing >= '"+from+"' " +
								"GROUP BY EXTRACT(year from timing), EXTRACT(month from timing),"+groupBy+"" +
								"ORDER BY EXTRACT(year from timing) ");
					}
				}
		if (weekly)
				{
					if (toExists && fromExists)
					{
						query = ("SELECT  EXTRACT(year from timing) AS Year, to_char(timing, 'IW') as WeekNo, "+ partitionQuery+ " , COUNT(*) AS Imagecount " +
								"FROM images " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') AND timing >= '"+from+"' " +
								"GROUP BY EXTRACT(year from timing), to_char(timing, 'IW'),"+groupBy+"" +
								"ORDER BY EXTRACT(year from timing) ");
					}
					if (!fromExists){
						query = ("SELECT  EXTRACT(year from timing) AS Year, to_char(timing, 'IW') as WeekNo, "+ partitionQuery+ " , COUNT(*) AS Imagecount " +
								"FROM images " +
								"WHERE timing <= to_date('"+to+" 11:59 P.M.', 'DD-MON-YYYY HH:MI P.M.') " +
								"GROUP BY to_char(timing, 'IW'),"+groupBy+"" +
								"ORDER BY EXTRACT(year from timing) ");
					}
					if (!toExists){
						query = ("SELECT  EXTRACT(year from timing) AS Year, to_char(timing, 'IW') as WeekNo, "+ partitionQuery+ " , COUNT(*) AS Imagecount " +
								"FROM images " +
								"WHERE timing >= '"+from+"' " +
								"GROUP BY EXTRACT(year from timing), to_char(timing, 'IW'),"+groupBy+"" +
								"ORDER BY EXTRACT(year from timing) ");
					}
				}	
	
    	stmt = conn.createStatement();
    	rset = stmt.executeQuery(query);
    	

		return rset;
		
		
	}
	
	
	
	
	
	
	
	
}
