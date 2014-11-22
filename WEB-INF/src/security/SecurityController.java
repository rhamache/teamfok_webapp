package security;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public boolean isOwnerOf(HttpSession sesh, int groupID) throws SQLException
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
	
	public List<String> getMembersOf(int group_id) throws SQLException
	{
		String query = "SELECT friend_id FROM group_lists WHERE group_id = "+group_id;
		Statement stmt = null; ResultSet rset = null;
		
		stmt = conn.createStatement();
		rset = stmt.executeQuery(query);
		
		List<String> member_names = new ArrayList<String>();
		
		while(rset != null && rset.next())
		{	
			member_names.add(rset.getString(1));
		}
		
		return member_names;
	}
	
	public String getGroupName(int group_id) throws SQLException
	{
		String query = "SELECT group_name FROM groups WHERE group_id = "+group_id;
		Statement stmt = null; ResultSet rset = null;
		
		stmt = conn.createStatement();
		rset = stmt.executeQuery(query);
		rset.next();
		
		return rset.getString(1);
	}
	
	public void deleteMemberFrom(String username, int group_id) throws SQLException
	{
		String sql = "DELETE FROM group_lists WHERE group_id = "+group_id+" AND friend_id = '"+username+"'";
		Statement stmt = null;
		
		stmt = conn.createStatement();
		stmt.executeUpdate(sql);
		stmt.executeUpdate("COMMIT");
	}
	
	public boolean addMemberTo(String username, int group_id, String notice) throws SQLException
	{
		String sql = "INSERT INTO group_lists VALUES("+group_id+", '"+username+"', sysdate, '"+notice+"')";
		Statement stmt = null; ResultSet rset = null;
		stmt = conn.createStatement();
		
		rset = stmt.executeQuery("SELECT COUNT(*) FROM USERS WHERE user_name = '"+username+"'");
		int count = 0;
		while (rset != null && rset.next()) {
			count = rset.getInt(1);
		}
		
		if (count < 1)
		{
			// user doesn't exist
			return false;
		}
		
		stmt.executeUpdate(sql);
		stmt.executeUpdate("COMMIT");
		return true;
	}
	
	public String getNoticeText(String username, int group_id) throws SQLException
	{
		String query = "SELECT notice FROM group_lists WHERE group_id = "+group_id+" AND friend_id = '"+username+"'";
		Statement stmt = null; ResultSet rset = null;
		
		stmt = conn.createStatement();
		rset = stmt.executeQuery(query);
		rset.next();
		
		return rset.getString(1);
	}

	public void deleteGroup(int group_id) throws SQLException
	{
		String sql = "DELETE FROM group_lists WHERE group_id = "+group_id;
		String sql2 = "DELETE FROM groups WHERE group_id = "+group_id;
		Statement stmt = null;
		
		stmt = conn.createStatement();
		stmt.executeUpdate(sql);
		stmt.executeUpdate(sql2);
		stmt.executeUpdate("COMMIT");
	}
	
	public void createGroup(String name, String owner) throws SQLException
	{
		String query = "SELECT group_id_seq.nextval FROM dual";
		Statement stmt = null; ResultSet rset = null;
		stmt = conn.createStatement();
		
		
		
		rset = stmt.executeQuery(query);
		
		int generated_id = 0;
		while (rset != null && rset.next()) {
			generated_id = rset.getInt(1);
		}
		if (generated_id == 0)
		{
			return;
		}
		
		String sql = "INSERT INTO groups VALUES("+generated_id+", '"+owner+"', '"+name+"', sysdate)";
		String sql2 = "INSERT INTO group_lists VALUES("+generated_id+", '"+owner+"', sysdate, 'Group Creator')";
		
		System.out.println(sql);
		System.out.println(sql2);
		
		stmt = conn.createStatement();
		stmt.executeUpdate(sql);
		stmt.executeUpdate(sql2);
		stmt.executeUpdate("COMMIT");
	}
}
