package helpers;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import mssqlHandlers.MSSQL.EmploYouDB;

public class inputCheck 
{
	public static class Check
	{
		public static boolean checkLoginInput(TextField usernameField,TextField passwordField)
		{
			String username = usernameField.getText();
			String password = passwordField.getText();
			
			if(username == "" || username.length() < 4)
			{
				Alert alert = new Alert(Alert.AlertType.ERROR); alert.setContentText("The username can't be empty or less than 4 symbols!"); alert.show();
				return false;
			}	
			if(password == "" || password.length() < 8)
			{
				Alert alert = new Alert(Alert.AlertType.ERROR); alert.setContentText("The password can't be empty or less than 8 symbols!"); alert.show();
				return false;
			}
			return true;
		}
		
		public static boolean checkRegisterInput (TextField signUpUsername,TextField signUpPasswordField,TextField signUpPasswordField2,TextField signUpUserImage )
		{
			String username = signUpUsername.getText();
			String password = signUpPasswordField.getText();
			String password2 = signUpPasswordField2.getText();
			String imageURL = signUpUserImage.getText();
			
			if(username == "" || username.length() < 4)
			{
				Alert alert = new Alert(Alert.AlertType.ERROR); alert.setContentText("The username can't be empty or less than 4 symbols!"); alert.show();
				return false;
			}	
			if(password == "" || password.length() < 8 || password2 == "" || password2.length() < 8 || !(password.equals(password2)))
			{
				Alert alert = new Alert(Alert.AlertType.ERROR); alert.setContentText("The password can't be empty or less than 8 symbols and both passwords must match!"); alert.show();
				return false;
			}
			if(!imageExists(imageURL))
			{Alert alert = new Alert(Alert.AlertType.ERROR); alert.setContentText("Image URL is not valid!"); alert.show();return false; }
			if(EmploYouDB.userExist(username))
			{Alert alert = new Alert(Alert.AlertType.ERROR); alert.setContentText("User already exists!"); alert.show();return false;}
			return true;
		}
		
		private static boolean imageExists(String URL)
		{
			try 
			{  
		        BufferedImage image = ImageIO.read(new URL(URL));
		        if (image != null){return true;} 
		        else {return false;}
			}
			catch(Exception e)
			{return false;}
		}
		
		public static boolean requestInputIsValid(TextField ticketName,TextField ticketUser,TextField ticketDescription)
		{
			if(ticketName.getText().length()<10)
			{
				Alert alert = new Alert(Alert.AlertType.ERROR); alert.setContentText("The ticket name has to be longer than 10 symbols!"); alert.show();
				return false;
			}
			
			if(ticketUser.getText().length()<4)
			{
				Alert alert = new Alert(Alert.AlertType.ERROR); alert.setContentText("The ticket requester name has to be longer than 4 symbols!"); alert.show();
				return false;
			}
			
			if(ticketDescription.getText().length()<25)
			{
				Alert alert = new Alert(Alert.AlertType.ERROR); alert.setContentText("The ticket description has to be longer than 25 symbols!"); alert.show();
				return false;
			}
			return true;
		}
	}
}
