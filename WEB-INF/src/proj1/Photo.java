package proj1;

import java.sql.Blob;
import java.sql.Date;

import oracle.sql.BLOB;


public class Photo implements Comparable<Photo>
{
	public final int id;
	private String owner_name;
	private int permitted;
	private String subject;
	private String place;
	private Date date;
	private String description;
	private BLOB thumbnail;
	private BLOB image;
	
	public Photo(int id)
	{
		this.id = id;
		this.setOwnerName(null);
		this.setPermitted(1);
		this.setSubject(null);
		this.setPlace(null);
		this.setDate(null);
		this.setDescription(null);
		this.setThumbnail(null);
		this.setImage(null);
	}
	
	public Photo(int id, String own, int perm, String sub, String pla, java.sql.Date date, String des, BLOB tmb, BLOB pic)
	{
		this.id = id;
		this.setOwnerName(own);
		this.setPermitted(perm);
		this.setSubject(sub);
		this.setPlace(pla);
		this.setDate(date);
		this.setDescription(des);
		this.setImage(pic);
		this.setThumbnail(tmb);
	}



	public String getOwnerName()
	{
		return owner_name;
	}

	public void setOwnerName(String owner_name)
	{
		this.owner_name = owner_name;
	}

	public int getPermitted()
	{

		return permitted;
	}

	public void setPermitted(int permitted)
	{

		this.permitted = permitted;
	}

	public String getSubject()
	{

		return subject;
	}

	public void setSubject(String subject)
	{

		this.subject = subject;
	}

	public String getPlace()
	{

		return place;
	}

	public void setPlace(String place)
	{

		this.place = place;
	}

	public String getDescription()
	{

		return description;
	}

	public void setDescription(String description)
	{

		this.description = description;
	}

	public Date getDate()
	{

		return date;
	}

	public void setDate(Date date)
	{

		this.date = date;
	}

	public BLOB getThumbnail()
	{

		return thumbnail;
	}

	public void setThumbnail(BLOB thumbnail)
	{

		this.thumbnail = thumbnail;
	}

	public BLOB getImage()
	{

		return image;
	}

	public void setImage(BLOB image)
	{

		this.image = image;
	}

	@Override
	public int compareTo(Photo p)
	{
		return this.getDate().compareTo(p.getDate());
	}
	
	
}
