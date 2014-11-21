package tests;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;


public class TestHttpSession implements HttpSession
{
	private String username;

	@Override
	public Object getAttribute(String arg0)
	{
		return username;
	}

	@Override
	public Enumeration getAttributeNames()
	{

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getCreationTime()
	{

		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getId()
	{

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getLastAccessedTime()
	{

		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxInactiveInterval()
	{

		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ServletContext getServletContext()
	{

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSessionContext getSessionContext()
	{

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(String arg0)
	{

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getValueNames()
	{

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate()
	{

		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNew()
	{

		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putValue(String arg0, Object arg1)
	{

		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAttribute(String arg0)
	{

		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeValue(String arg0)
	{

		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttribute(String arg0, Object arg1)
	{

		username = (String) arg1;
		
	}

	@Override
	public void setMaxInactiveInterval(int arg0)
	{

		// TODO Auto-generated method stub
		
	}

}
