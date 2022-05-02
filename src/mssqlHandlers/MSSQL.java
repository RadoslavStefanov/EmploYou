package mssqlHandlers;

import java.sql.Connection;

import java.util.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import Objects.Interest;
import Objects.Trail;
import Objects.User;
import application.Main;
import helpers.errMSG.showError;
import javafx.scene.control.TextField;

public class MSSQL 
{	
	public static class EmploYouDB
	{
		static String dbName = "EmploYouDB"; 
		static String dbConString = "jdbc:sqlserver://DESKTOP-MSD66P5;databaseName=EMPLOYOU"; 
		static String dbUserName = "java"; 
		static String dbPassword = "java1234"; 
		static Boolean connected = false;
		static Connection connection;
		static Boolean isConnected = false;
		
		public static boolean getIsConnected()
		{return isConnected;}		
		private static void setIsConnected(Boolean status)
		{isConnected = status;}
		
		
		public static void Connect()
		{
			new Thread(() -> 
			{
					try 
					{
						connection = DriverManager.getConnection(dbConString,dbUserName,dbPassword);
						setIsConnected(true);
					}
					catch(Exception e)
					{setIsConnected(false);}
				
			}).start();
			
		}
		
		public static boolean authenticateUser(TextField usernameField,TextField passwordField)
		{
			String username = usernameField.getText();
			String password = passwordField.getText();
			
			boolean logHimIn = false;
			
			if(!isConnected){showError.show("The application is not connected with the Database!"); return false;}
			
			ExecutorService executor = Executors.newSingleThreadExecutor();
		    Callable<Integer> callable = new Callable<Integer>() {
		        @Override
		        public Integer call() 
		        {
		        	try
					{
						String sqlQuery = "SELECT TOP(1) Password FROM Users AS u WHERE u.Username = '"+username+"'";
						Statement statement = connection.createStatement();
						ResultSet result = statement.executeQuery(sqlQuery);
						
						String resultPass = "";
						while (result.next()) {resultPass = result.getString("Password");}
						
						if(resultPass.equals(password)) {return 1;}
						return 0;
					}
					catch(Exception e)
					{e.printStackTrace(); return 0;}
		        }
		    };
		    Future<Integer> future = executor.submit(callable);
		    executor.shutdown();
		    try {if(future.get()==1) {logHimIn=true;}}
		    catch(Exception e) {System.out.print(e);}
			
			return logHimIn;
		}
		
		public static void addNewUser(TextField signUpUsername,TextField signUpPasswordField,TextField signUpUserImage)
		{
			String username = signUpUsername.getText();
			String password = signUpPasswordField.getText();
			String imageURL = signUpUserImage.getText();
			
			new Thread(() -> 
			{
				try
				{
					String sqlQuery = "INSERT INTO USERS(Username,Password,DateJoined,PermissionLevel,ProfilePictureURL) "
							+ "VALUES ('"+username+"','"+password+"',GETDATE(),1,'"+imageURL+"')";
					Statement statement = connection.createStatement();
					statement.execute(sqlQuery);
				}
				catch(Exception e)
				{e.printStackTrace();}
			}).start();
		}
		
		public static boolean userExist(String username)
		{
			System.out.print("Checking if "+username+" exists in the database!@");
			try
			{
				String sqlQuery = "SELECT TOP(1) Username FROM Users AS u WHERE u.Username = '"+username+"'";
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(sqlQuery);
				
				String resultPass = "";
				while (result.next()) {resultPass = result.getString("Username");}
				
				if(resultPass.equals("")) {return false;}
				return true;
			}
			catch(Exception e)
			{e.printStackTrace(); return false;}
		}
	
		public static String getUserPicture(String username)
		{
			ExecutorService executor = Executors.newSingleThreadExecutor();
		    Callable<String> callable = new Callable<String>() {
		        @Override
		        public String call() 
		        {
		        	try
					{
						String sqlQuery = "SELECT TOP(1) ProfilePictureURL FROM Users AS u WHERE u.Username = '"+username+"'";
						Statement statement = connection.createStatement();
						ResultSet result = statement.executeQuery(sqlQuery);
						
						String resultPass = "";
						while (result.next()) {resultPass = result.getString("ProfilePictureURL");}
						
						return resultPass;
					}
					catch(Exception e)
					{e.printStackTrace(); return "https://img-16.ccm2.net/_SqzzXVDSG50FWb_UBrCl3XwV78=/440x/1685e17045e747a899925aa16189c7c6/ccm-encyclopedia/99776312_s.jpg";}
		        }
		    };
		    Future<String> future = executor.submit(callable);
		    executor.shutdown();
			
			try {return future.get();} 
			catch (InterruptedException | ExecutionException e) 
			{e.printStackTrace();return "https://img-16.ccm2.net/_SqzzXVDSG50FWb_UBrCl3XwV78=/440x/1685e17045e747a899925aa16189c7c6/ccm-encyclopedia/99776312_s.jpg";}
			
		}
		
		public static void addHistoryEntry(String action)
		{
			new Thread(() -> 
			{
				try
				{
					String sqlQuery = "INSERT INTO History(UserId,Action,ActionDate) VALUES ('"+getUserId(Main.getCurUser())+"','"+action+"',GETDATE())";
					Statement statement = connection.createStatement();
					statement.execute(sqlQuery);
				}
				catch(Exception e)
				{e.printStackTrace();}
			}).start();
		}
		public static void addHistoryEntry(String action,String username)
		{
			new Thread(() -> 
			{
				try
				{
					String sqlQuery = "INSERT INTO History(UserId,Action,ActionDate) VALUES ('"+getUserId(username)+"','"+action+"',GETDATE())";
					Statement statement = connection.createStatement();
					statement.execute(sqlQuery);
				}
				catch(Exception e)
				{e.printStackTrace();}
			}).start();
		}
		
		public static Integer getUserId(String username)
		{
			ExecutorService executor = Executors.newSingleThreadExecutor();
		    Callable<Integer> callable = new Callable<Integer>() {
		        @Override
		        public Integer call() 
		        {
		        	try
					{
						String sqlQuery = "SELECT Id FROM Users AS u WHERE u.Username = '"+username+"'";
						Statement statement = connection.createStatement();
						ResultSet result = statement.executeQuery(sqlQuery);
						
						int resultPass = 0;
						while (result.next()) {resultPass = Integer.parseInt(result.getString("Id"));}
						
						return resultPass;
					}
					catch(Exception e)
					{System.out.print("Get User Method ERROR "+e.getMessage()); return 0;}
		        }
		    };
		    Future<Integer> future = executor.submit(callable);
		    executor.shutdown();
			
			try {return future.get();} 
			catch (InterruptedException | ExecutionException e) 
			{e.printStackTrace(); return 0;}
			
		}
		
		public static String getUserNameById(int id)
		{
			ExecutorService executor = Executors.newSingleThreadExecutor();
		    Callable<String> callable = new Callable<String>() {
		        @Override
		        public String call() 
		        {
		        	try
					{
						String sqlQuery = "SELECT TOP(1) * FROM Users AS u WHERE u.Id = '"+id+"'";
						Statement statement = connection.createStatement();
						ResultSet result = statement.executeQuery(sqlQuery);
						
						String resultPass = "";
						while (result.next()) {resultPass = result.getString("Username");}
						
						return resultPass;
					}
					catch(Exception e)
					{e.printStackTrace();return"Error";}
		        }
		    };
		    Future<String> future = executor.submit(callable);
		    executor.shutdown();
			
			try {return future.get();} 
			catch (InterruptedException | ExecutionException e) 
			{e.printStackTrace(); return "Error in finding the user!";}
		}
	
		public static String getUserHistory(String username)
		{			
			ExecutorService executor = Executors.newSingleThreadExecutor();
		    Callable<String> callable = new Callable<String>() {
		        @Override
		        public String call() 
		        {
		        	try
					{
						String sqlQuery = "SELECT TOP(20) Id,Action,ActionDate FROM History AS h WHERE h.UserId ="+getUserId(username)+" ORDER BY Id DESC";
						Statement statement = connection.createStatement();
						ResultSet result = statement.executeQuery(sqlQuery);
						
						String resultAction = "";
						String resultDate = "";
						StringBuilder sb = new StringBuilder();
						while (result.next()) 
						{
							resultAction = result.getString("Action");
							resultDate = result.getString("ActionDate");
							sb.append("\n");sb.append(resultAction+" - "+resultDate);
						}
						
						sb.trimToSize();
						return sb.toString();
					}
					catch(Exception e)
					{e.printStackTrace(); return "Error";}
		        }
		    };
		    Future<String> future = executor.submit(callable);
		    executor.shutdown();
			
			try {return future.get();} 
			catch (InterruptedException | ExecutionException e) 
			{e.printStackTrace();return "Error";}
			
		}
		
		public static List<Trail> getOpenTrails()
		{
			List<Trail> errorArr = new ArrayList<Trail>();
			
			ExecutorService executor = Executors.newSingleThreadExecutor();
		    Callable<List<Trail>> callable = new Callable<List<Trail>>() {
		        @Override
		        public List<Trail> call() 
		        {
		        	try
					{
						String sqlQuery = "SELECT t.Id,t.Name,t.Description,t.CompanyId,u.Id AS uId,u.Username,ProfilePictureURL FROM Trails AS t LEFT JOIN UsersTrails AS ut ON t.Id=ut.TrailId LEFT JOIN Users AS u ON ut.UserId = u.Id";
						Statement statement = connection.createStatement();
						ResultSet result = statement.executeQuery(sqlQuery);
						
						
						
						List<Trail> resultArr = new ArrayList<Trail>();
						
						while (result.next()) 
						{
							String curTrailName = result.getString("Name");
							if(resultArr.stream().anyMatch(x->x.getName().equals(curTrailName)))
							{
								String uId = result.getString("uId");
								if(uId!= null)
								{
									String userName = result.getString("Username");
									String picUrl = result.getString("ProfilePictureURL");
									resultArr.stream()
									.filter(x->x.getName().equals(curTrailName))
									.findFirst()
									.get()
									.addUser(new User(userName,Integer.parseInt(uId),picUrl));;
									
								}
							}
							else
							{
								List<User> userArr = new ArrayList<User>();
								
								String uId = result.getString("uId");
								if(uId!= null)
								{
									String userName = result.getString("Username");
									String picUrl = result.getString("ProfilePictureURL");
									userArr.add(new User(userName,Integer.parseInt(uId),picUrl));
								}
								
								
								
								
								
								int id = Integer.parseInt(result.getString("id"));
								String name = result.getString("Name");
								String description = result.getString("Description");
								int companyId = Integer.parseInt(result.getString("CompanyId"));
								resultArr.add(new Trail(id,name,description,companyId,userArr));
								
							}
						} 
						
						return resultArr;
					}
					catch(SQLException e)
		        	{e.printStackTrace(); return errorArr;}
					
		        }
		    };
		    Future<List<Trail>> future = executor.submit(callable);
		    executor.shutdown();
			
			try {return future.get();} 
			catch (InterruptedException | ExecutionException e) 
			{e.printStackTrace(); return errorArr;}
		}

		public static User getUserObject(String username)
		{
			ExecutorService executor = Executors.newSingleThreadExecutor();
		    Callable<User> callable = new Callable<User>() {
		        @Override
		        public User call() 
		        {
		        	try
					{
						String sqlQuery = "SELECT TOP(1) * FROM Users AS u WHERE u.Username = '"+username+"'";
						Statement statement = connection.createStatement();
						ResultSet result = statement.executeQuery(sqlQuery);
						
						User u = new User("",0,"");
						while (result.next()) 
						{
							String name = result.getString("Username");
							int id = Integer.parseInt(result.getString("Id"));
							String password = result.getString("Password");
							String dateJoined = result.getString("DateJoined");
							int permLevel = Integer.parseInt(result.getString("PermissionLevel"));
							String pic = result.getString("ProfilePictureURL");
							u = new User(name,id,pic,password,dateJoined,permLevel);
						}
						
						return u;
					}
					catch(Exception e)
					{e.printStackTrace();return null;}
					
		        }
		    };
		    Future<User> future = executor.submit(callable);
		    executor.shutdown();
			
			try {return future.get();} 
			catch (InterruptedException | ExecutionException e) 
			{e.printStackTrace(); return null;}
			
			
		}
	
		public static List<Interest> getInterests()
		{
			List<Interest> errorArr = new ArrayList<Interest>();
			
			ExecutorService executor = Executors.newSingleThreadExecutor();
		    Callable<List<Interest>> callable = new Callable<List<Interest>>() {
		        @Override
		        public List<Interest> call() 
		        {
		        	try
					{
						String sqlQuery = "SELECT i.Id AS iId,i.Name,i.Description,u.Id,u.Username FROM Users AS u LEFT JOIN InterestsUsers AS iu ON u.Id=iu.UserId LEFT JOIN Interests AS i ON iu.InterestId = i.Id\r\n";
						Statement statement = connection.createStatement();
						ResultSet result = statement.executeQuery(sqlQuery);
						
						
						
						List<Interest> resultArr = new ArrayList<Interest>();
						
						while (result.next()&&result.getString("iId")!=null) 
						{
								
								int id = Integer.parseInt(result.getString("iId"));
								String name = result.getString("Name");
								String description = result.getString("Description");
								int userId = Integer.parseInt(result.getString("Id"));
								String userName = result.getString("Username");
								resultArr.add(new Interest(id,name,description,userId,userName));
						} 
						
						return resultArr;
					}
					catch(SQLException e)
		        	{e.printStackTrace(); return errorArr;}
					
		        }
		    };
		    Future<List<Interest>> future = executor.submit(callable);
		    executor.shutdown();
			
			try {return future.get();} 
			catch (InterruptedException | ExecutionException e) 
			{e.printStackTrace(); return errorArr;}
		}
		
		public static void createRequest(TextField ticketName,TextField ticketUser,TextField ticketDescription,int UserId)
		{
			new Thread(() -> 
			{
				try
				{
					String sqlQuery = "INSERT INTO Requests(UserId,Name,Description,DateCreated)VALUES ("+UserId+",'"+ticketName.getText()+"','"+ticketDescription.getText()+"-"+ticketUser.getText()+"',GETDATE())";
					Statement statement = connection.createStatement();
					statement.execute(sqlQuery);
				}
				catch(Exception e)
				{e.printStackTrace();}
			}).start();
		}
		
		public static List<User> getAllUsers()
		{
			List<User> errorArr = new ArrayList<User>();
			
			ExecutorService executor = Executors.newSingleThreadExecutor();
		    Callable<List<User>> callable = new Callable<List<User>>() {
		        @Override
		        public List<User> call() 
		        {
		        	try
					{
						String sqlQuery = "SELECT * FROM Users";
						Statement statement = connection.createStatement();
						ResultSet result = statement.executeQuery(sqlQuery);
						
						
						
						List<User> resultArr = new ArrayList<User>();
						
						while (result.next()) 
						{
								
								int id = Integer.parseInt(result.getString("Id"));
								String name = result.getString("Username");
								String pass = result.getString("Password");
								String dateJoined = result.getString("DateJoined");
								int permLevel = Integer.parseInt(result.getString("PermissionLevel"));
								String picture = result.getString("ProfilePictureURL");
								resultArr.add(new User(name,id,picture,pass,dateJoined,permLevel));
						} 
						
						return resultArr;
					}
					catch(SQLException e)
		        	{e.printStackTrace(); return errorArr;}
					
		        }
		    };
		    Future<List<User>> future = executor.submit(callable);
		    executor.shutdown();
			
			try {return future.get();} 
			catch (InterruptedException | ExecutionException e) 
			{e.printStackTrace(); return errorArr;}
		}
	
		public static void addUserToTrail(int userId,int trailId)
		{
			new Thread(() -> 
			{
				try
				{
					String sqlQuery = "INSERT INTO UsersTrails(UserId,TrailId) VALUES ('"+userId+"','"+trailId+"')";
					Statement statement = connection.createStatement();
					statement.execute(sqlQuery);
				}
				catch(Exception e)
				{e.printStackTrace();}
			}).start();
		}
	}
}


