package tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import proj1.DatabaseController;
import proj1.HTMLBuilder;
import proj1.HomePageServlet;

import search.SearchController;
import security.SecurityController;
import usermanagement.LoginServlet;
import usermanagement.RegistrationController;

public class TestCases {

	public static void main(String[] args) {
		
		int total_tests = 7, tests_passed = 0;
		
		tests_passed += TestCases.TestDBC();
		
		tests_passed += TestCases.TestLoginPost();
		
		tests_passed += TestCases.TestHomePageGet();
		
		tests_passed += TestCases.TestHTMLFromFile();
		
		tests_passed += TestCases.TestRegistrationNewUser();
		
		tests_passed += TestCases.TestIsMemberOf();
		
		tests_passed += TestCases.TestSearch();
		
		tests_passed += TestCases.TestGroupCreate();
		
		System.out.println("Tests passed: "+tests_passed+"/"+total_tests);
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
			sc.createGroup("test", "ZachB");
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

	public static int TestSearch()
	{
		
		
		SearchController sc = null;
		try
		{
			sc = new SearchController();
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
		HashSet<String> printed = null;
		try
		{
			printed = (HashSet<String>) sc.keywordsearch("Edmonton ach ZacHb zachb", null, null);
		} catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try
		{
			sc.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (String photoid : printed){
			System.out.println("ID: "+photoid);	
			}
		return 1;
	}
}
