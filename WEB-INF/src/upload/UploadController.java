package upload;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import oracle.jdbc.OracleResultSet;
import oracle.sql.BLOB;
import proj1.DatabaseController;
import proj1.Photo;

public class UploadController extends DatabaseController
{

	public UploadController() throws SQLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException
	{
		super();
	}

	public boolean checkCredentials(String username, String password) throws SQLException {
		String query = "select PASSWORD from USERS where USER_NAME = '"+username+"'";
		Statement stmt = null; ResultSet rset = null;
		
        stmt = conn.createStatement();
	    rset = stmt.executeQuery(query);
    	
        String truepwd = "";
    	
    	while(rset != null && rset.next()) {
        	truepwd = (rset.getString(1)).trim();
    	}

        if(password.equals(truepwd))
	        return true;
    	else
        	return false;
	}
	
	public int getPicId() throws SQLException{
		ResultSet rset1 = executeSQLTextStatement("SELECT pic_id_seq.nextval FROM dual");
        rset1.next();
        int id = rset1.getInt(1);
		return id;
	}
	
	public void writeBlob(int id, ArrayList<String> bundle, InputStream instr) throws SQLException, IOException
	{
	    BufferedImage img = ImageIO.read(instr);
	    BufferedImage thumbNail = shrink(img);
		
		String sql = "INSERT INTO images VALUES("+id+", '" + bundle.get(0)+"', "+Integer.parseInt(bundle.get(1))+", '"+bundle.get(2)+"', '"+bundle.get(3)+"', sysdate, '"+bundle.get(4)+"',empty_blob(),empty_blob())";
		String query = "SELECT * FROM images WHERE photo_id = "+id+" FOR UPDATE";
		String sql2 = "INSERT INTO hitcounts VALUES("+id+", 0)";
		Statement stmt = null; ResultSet rset = null;

		stmt = conn.createStatement();
		stmt.executeUpdate(sql);
		stmt.executeUpdate(sql2);
		stmt.executeUpdate("COMMIT");
		rset = stmt.executeQuery(query);
		rset.next();
		
		BLOB theblob = ((OracleResultSet)rset).getBLOB(8);

	    OutputStream outstream = theblob.getBinaryOutputStream();
	    ImageIO.write(thumbNail, "jpg", outstream);
        
	    theblob = ((OracleResultSet)rset).getBLOB(9);
	    
	  	outstream = theblob.getBinaryOutputStream();
	    ImageIO.write(img, "jpg", outstream);
	    
	    stmt.executeUpdate("COMMIT");
	}

	
	public static BufferedImage shrink(BufferedImage image) {

		BufferedImage bigtmb;
		int size, diff, wstart = 0, hstart = 0;
		if (image.getHeight() > image.getWidth())
		{
			size = image.getHeight();
			diff = size - image.getWidth();
			wstart = diff/2;
			bigtmb = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		} else
		{
			size = image.getWidth();
			diff = size - image.getHeight();
			hstart = diff/2;
			bigtmb = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		}
		
		int i,j;
		for (i = 0; i < bigtmb.getHeight(); i++)
			for(j = 0; j < bigtmb.getWidth(); j++)
				bigtmb.setRGB(j, i, 0);
		
		int _w = 0, _h = 0;
		for (int w=wstart; w < size; ++w)
		{
			_h = 0;
			for (int h=hstart; h < size; ++h)
			{
				if (_w < image.getWidth() && _h < image.getHeight())
					bigtmb.setRGB(w, h, image.getRGB(_w, _h));
				_h++;
			}
			_w++;
		}
		
		BufferedImage thumbnail = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
		Image scaled = bigtmb.getScaledInstance(300, 300, Image.SCALE_FAST);
		thumbnail.createGraphics().drawImage(scaled, 0, 0, null);
		
		return thumbnail;
    }
	

	public void deletePhoto(int id) throws SQLException
	{
		String sql = "DELETE FROM images WHERE photo_id = "+id;
		String sql2 = "DELETE FROM hits WHERE photo_id = "+id;
		String sql3 = "DELETE FROM hitcounts WHERE photo_id = "+id;
		Statement stmt = null; 
		stmt = conn.createStatement();
		
		stmt.executeUpdate(sql3);
		stmt.executeUpdate(sql2);
		stmt.executeUpdate(sql);
		stmt.executeUpdate("COMMIT");
	}
	
	public ArrayList<Integer> gatherGroups(String username) throws SQLException{
		ArrayList<Integer> groupsInvolved = new ArrayList<Integer>();
		String query = "select group_id from group_lists where lower(friend_id) = '"+username+"';";
		Statement stmt = null; ResultSet rset = null;
	
		stmt = conn.createStatement();
		rset = stmt.executeQuery(query);
	
    	while(rset != null && rset.next()) {
    		groupsInvolved.add((rset.getInt(1)));
    	}
		
    	return groupsInvolved;
	}

}

