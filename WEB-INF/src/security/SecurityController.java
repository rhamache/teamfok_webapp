package security;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpSession;

import proj1.DatabaseController;



public class SecurityController extends DatabaseController
{
	public SecurityController() throws SQLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException
	{
		super();
	}

	/* returns true is a user is logged in, false if not */
	public static boolean isLoggedIn(HttpSession sesh)
	{
		  if (sesh.getAttribute("username") == null) {
			  return false;
		  } else {
			  return true;
		  }
	}
	
	public boolean isMemberOf(HttpSession sesh, int groupID) throws SQLException
	{
		if (!SecurityController.isLoggedIn(sesh))
		{
			return false;
		}
		
		String query = "SELECT COUNT(*) FROM groups WHERE user_name = '"+sesh.getAttribute("username")+"' AND group_id = "+groupID;
		Statement stmt = null; ResultSet rset = null;
		
        stmt = conn.createStatement();
	    rset = stmt.executeQuery(query);
	    
	    int count = 0;
	    
    	while(rset != null && rset.next()) {
        	count = rset.getInt(1);
    	}
    	
    	if (count > 0)
    	{
    		return true;
    	} else {
    		return false;
    	}
	}
}
