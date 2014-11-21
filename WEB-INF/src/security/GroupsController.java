package security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import proj1.DatabaseController;


public class GroupsController extends DatabaseController
{

	public GroupsController() throws SQLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException
	{

		super();
	}

	public Map<Integer, String> getGroupsOwnedBy(String username) throws SQLException
	{
		String query = "SELECT group_id, group_name FROM groups WHERE user_name = '"+username+"'";
		Statement stmt = null; ResultSet rset = null;
		
		stmt = conn.createStatement();
		rset = stmt.executeQuery(query);
		
		Map<Integer, String> groupIds = new HashMap<Integer, String>();
		
		while(rset != null && rset.next())
		{	
			groupIds.put(rset.getInt(1), rset.getString(2));
		}
		
		return groupIds;
	}

}
