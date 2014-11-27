package tests;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import dataanalysis.DataAnalysisController;
import display.DisplayController;

import proj1.DatabaseController;
import proj1.HTMLBuilder;
import proj1.HomePageServlet;

import search.SearchController;
import security.SecurityController;
import usermanagement.LoginServlet;
import usermanagement.RegistrationController;

public class TestCases {

	public static void main(String[] args) {
		
		int total_tests = 9, tests_passed = 0;
		
		/*tests_passed += TestCases.TestDBC();
		
		tests_passed += TestCases.TestLoginPost();
		
		tests_passed += TestCases.TestHomePageGet();
		
		tests_passed += TestCases.TestHTMLFromFile();
		
		tests_passed += TestCases.TestRegistrationNewUser();
		
		tests_passed += TestCases.TestIsMemberOf();
		
		tests_passed += TestCases.TestGroupCreate();
		
		tests_passed += TestCases.TestAddHit();
		
		tests_passed += TestCases.TestAnalysis();*/
		
		try
		{
			TestCases.TestSearch();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Tests passed: "+tests_passed+"/"+total_tests);
	}
	
	public static int TestSearch() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		SearchController sc = null;
		sc = new SearchController();
		
		try
		{
			sc.SearchMain("xyz", "Neither", "", "");
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public static int TestAnalysis()
	{
		DataAnalysisController ac = null;
		
		
		ResultSet rset = null;
		ResultSetMetaData rsmd = null;
		
		try {
			ac = new DataAnalysisController();
			//rset = ac.dataAnalysis("users", "03-MAR-1992", "24-NOV-2014", "weekly");
			rsmd = rset.getMetaData();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		int i = 1; String last_time_period = "";
		System.out.println("<table border = \"1\">");
		try{
			while (rset != null && rset.next())
			{
				if (!last_time_period.equals(rset.getString(1)))
					System.out.println("<tr><td style = \"min-width:100px;height:50px;\">"+rset.getString(1)+"</td>");
				else
					System.out.println("<tr><td style = \"min-width:100px;height:50px;\"></td>");
				for (i = 2; i <= rsmd.getColumnCount(); i++)
				{
					System.out.println("<td style = \"min-width:200px;\">");
					System.out.println(rset.getString(i));
					System.out.println("</td>");
				}
				last_time_period = rset.getString(1);

				System.out.println("</tr>");
			}
		}
		catch (SQLException e)
		{
			System.out.println("rset exception: "+e.getMessage()+" index: "+i);
		}
		return 1;
	}
	public static int TestAddHit()
	{
		DisplayController dc = null;
		
		try{
			dc = new DisplayController();
			dc.addHit("admin", 51);
			dc.close();
		} catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
		
		
		return 1;
	}
	
	public static int TestGroupCreate()
	{
		SecurityController sc = null;
		
		try
		{
			sc = new SecurityController();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			sc.createGroup("test", "admin");
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
		
	}
	
	public static int TestDBC() {
		DatabaseController dbc = null;
		try {
			dbc = new DatabaseController();
		}
		catch (Exception e)
		{
			// fail test
			e.printStackTrace();
			System.out.println("Exception: " + e.getMessage());
			System.out.println("testDBC(): test failed.");
			return 0;
		}
		
		try
		{
			dbc.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("testDBC(): test passed!");
		return 1;
		
	}
	
	public static int TestLoginPost() {
		
		TestHttpServletRequest req = new TestHttpServletRequest();
		TestHttpServletResponse res = new TestHttpServletResponse();
		LoginServlet ls = new LoginServlet();
		
		req.setParameter("USERID", "test_username");
		req.setParameter("PASSWD", "test_password");
		try {
			ls.doPost(req, res);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return 1;
	}
	
	public static int TestHomePageGet() {
		TestHttpServletRequest req = new TestHttpServletRequest();
		TestHttpServletResponse res = new TestHttpServletResponse();
		HomePageServlet ls = new HomePageServlet();
		
		try
		{
			ls.doGet(req, res);
		} catch (ServletException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
		
		return 1;
	}
	
	public static int TestHTMLFromFile() {
		String expected_out = "<h1>test</h1>";
		
		HTMLBuilder html = new HTMLBuilder();
		try
		{
			html.buildFromFile("test.html");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(html.grabBundle());
		if (expected_out.equals(html.grabBundle()))
		{
			System.out.println(html.grabBundle());
			return 1;
		} else {
			return 0;
		}
	}

	public static int TestRegistrationNewUser() {
		RegistrationController rc = null;
		try
		{
			rc = new RegistrationController();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ArrayList<String> fields = new ArrayList<String>();
		fields.add("ryham");
		fields.add("password");
		fields.add("password");
		fields.add("ry");
		fields.add("ham");
		fields.add("9819 72 st");
		fields.add("ham@r.c");
		fields.add("7808882222");
		
		
		try
		{
			rc.addPersonAndUser(fields);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// now delete
		try
		{
			rc.deleteUser("ryham");
		} catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try
		{
			rc.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return 1;
	}
	
	public static int TestIsMemberOf()

	{
		String username = "j.bieber"; int id = 11;
		
		SecurityController sc = null;
		try
		{
			sc = new SecurityController();
		} catch (Exception e)
		{
			e.printStackTrace();
		} 
		
		HttpSession sesh = new TestHttpSession();
		sesh.setAttribute("username", username);
		
		boolean rval = false;
		try
		{
			rval = sc.isOwnerOf(sesh, id);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try
		{
			sc.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (rval){
			return 1;
		} else {
			System.out.println("TestMemberOf Failed.");
			return 0;
		}
	}


}
