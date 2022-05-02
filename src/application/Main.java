package application;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import helpers.errMSG.showError;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import mssqlHandlers.MSSQL.EmploYouDB;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application 
{
	public static String curUser = "";
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			 Parent root = FXMLLoader.load(getClass().getResource("loginView.fxml"));
	         Scene mainScene= new Scene(root);
	         primaryStage.setResizable(false);
	         primaryStage.setScene(mainScene);
	         Image icon = new Image(getClass().getResourceAsStream("icon.png"));
	         primaryStage.getIcons().add(icon);
	         primaryStage.titleProperty().set("EmploYou Login");
	         primaryStage.show();
	         isInternetConnected(primaryStage);
	         EmploYouDB.Connect();
		} 
		catch(Exception e) 
		{e.printStackTrace();}
	}
	
	public static void isInternetConnected(Stage stage)
	{
			
		try 
		{
	         URL url = new URL("http://www.google.com");
	         URLConnection connection = url.openConnection();
	         connection.connect();
	    } 
		
		catch (MalformedURLException e) 
		{showError.show("The application needs internet connection.Check your connection and try again!"); stage.close();} 
		
		catch (IOException e) 
		{showError.show("The application needs internet connection.Check your connection and try again!"); stage.close();}
	   
	}
	
	public static String getCurUser()
	{return curUser;}
	
	public static void setCurUser(String user)
	{curUser = user;}
	
	public static void main(String[] args) 
	{launch();}
}
