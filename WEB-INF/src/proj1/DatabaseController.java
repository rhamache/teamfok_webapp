package proj1;

import java.sql.*;



public class DatabaseController{
	/* Oracle credentials */
    private final String Orc_Driver = "oracle.jdbc.driver.OracleDriver";
    private final String Orc_URL = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
    private final String Orc_Username = "hfok";
    private final String Orc_Passwd = "43KBSQL5";
    
    protected Connection conn;
    
    public DatabaseController() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
    	this.conn = null;
    	Class drvClass = Class.forName(Orc_Driver); 
    	DriverManager.registerDriver((Driver) drvClass.newInstance());
    	conn = DriverManager.getConnection(Orc_URL, Orc_Username,Orc_Passwd);
    	conn.setAutoCommit(false);

    }

    
    public void close() throws SQLException {
    	if (this.conn != null){
				conn.close();
    	}
    }
    
    public ResultSet executeSQLTextStatement(String statement) throws SQLException {
    	Statement stmt = null;
    	ResultSet rset = null;

	    stmt = conn.createStatement();
	    rset = stmt.executeQuery(statement);
	    
	    stmt.close();
		return rset;
    }
    
    
    
}
