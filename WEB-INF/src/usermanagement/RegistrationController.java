package usermanagement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import proj1.DatabaseController;


public class RegistrationController extends DatabaseController
{
	
	private String CurrentError;

	public RegistrationController() throws SQLException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException
	{

		super();
		CurrentError = "No Error";
	}
	
	public String getFieldError()
	{
		return CurrentError;
	}
	
	/* returns true if fields are valid, false otherwise */
	public boolean clean_fields(ArrayList<String> fields) throws SQLException
	{
		Statement stmt = null; ResultSet rset = null;
		stmt = conn.createStatement();
		
		// check if all fields exits
		int i = 0;
		for (i = 0; i < fields.size(); i++)
		{
			if (fields.get(i).equals(""))
			{
				CurrentError = "One or more fields were left blank.";
				return false;
			}
		}
		
		/* Field indices should be: 0 - username
		 * 							1 - password
		 * 							2 - repeat password
		 *							3 - first name
		 *							4 - last name
		 *							5 - address
		 *							6 - email
		 *							7 - phone number
		 */
		
		
		// make sure username is unique and isnt more than 24 characters
		rset = stmt.executeQuery("SELECT COUNT(*) FROM USERS WHERE user_name = '"+fields.get(0)+"'");
		int count = 1;
		while (rset != null && rset.next()) {
			count = rset.getInt(1);
		}
		
		if (count > 0) { CurrentError = "Username already exists."; return false; }
		if (fields.get(0).length() > 24) { CurrentError = "Username not unique."; return false; } 
		
		// ensure passwords match and less than 24 characters
		if (!fields.get(1).equals(fields.get(2))) { CurrentError = "Passwords don't match."; return false; }
		if (fields.get(1).length() > 24) { CurrentError = "Password too long."; return false; }
		
		// ensure first name, last name less than 24 characters
		if (fields.get(3).length() > 24) { CurrentError = "First name too long"; return false; }
		if (fields.get(4).length() > 24) { CurrentError = "Last name too long."; return false; }
		
		// ensure address and email less than 128 characters
		if (fields.get(5).length() > 128) { CurrentError = "Address too long."; return false; }
		if (fields.get(6).length() > 128) { CurrentError = "Email too long."; return false; }
		
		// ensure phone number is EXACTLY 10 characters
		if (fields.get(7).length() != 10) { CurrentError = "Phone number too long."; return false; }
		
		return true;
		
	}
	
	public void addPersonAndUser(ArrayList<String> fields) throws SQLException
	{
		Statement stmt = null;
		stmt = conn.createStatement();
		
		String hashpass = "" + fields.get(1).hashCode();
		
		String sql1 = "INSERT INTO users VALUES('"+fields.get(0)+"', '"+hashpass+"', sysdate)";
		String sql2 = "INSERT INTO persons VALUES('"+fields.get(0)+"', '"+fields.get(3)+"', '"+fields.get(4)
													+"', '"+fields.get(5)+"', '"+fields.get(6)+"', "+fields.get(7)+")";
		
		stmt.executeUpdate(sql1);
		stmt.executeUpdate(sql2);
		stmt.executeUpdate("COMMIT");
	}
	
	public void deleteUser(String name) throws SQLException
	{
		Statement stmt = null;
		stmt = conn.createStatement();
		
		
		String sql = "DELETE FROM users WHERE user_name = '"+name+"'";
		String sql2 = "DELETE FROM persons WHERE user_name = '"+name+"'";
		
		stmt.executeUpdate(sql2);
		stmt.executeUpdate(sql);
		stmt.executeUpdate("COMMIT");
	}

}
