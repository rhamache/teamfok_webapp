package upload;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;

import oracle.jdbc.OracleResultSet;
import oracle.sql.BLOB;
import proj1.DatabaseController;

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
	    BufferedImage thumbNail = shrink(img, 10);
		
		String sql = "INSERT INTO images VALUES("+id+", '" + bundle.get(0)+"', "+Integer.parseInt(bundle.get(1))+", '"+bundle.get(2)+"', '"+bundle.get(3)+"', sysdate, '"+bundle.get(4)+"',empty_blob(),empty_blob())";
		String query = "SELECT * FROM images WHERE photo_id = "+id+" FOR UPDATE";
		Statement stmt = null; ResultSet rset = null;

		stmt = conn.createStatement();
		stmt.executeUpdate(sql);
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

	
	public static BufferedImage shrink(BufferedImage image, int n) {

        int w = image.getWidth() / n;
        int h = image.getHeight() / n;

        BufferedImage shrunkImage =
            new BufferedImage(w, h, image.getType());

        for (int y=0; y < h; ++y)
            for (int x=0; x < w; ++x)
                shrunkImage.setRGB(x, y, image.getRGB(x*n, y*n));

        return shrunkImage;
    }

}

