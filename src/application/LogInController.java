package application;

import helpers.inputCheck.Check;
import java.io.IOException;
import helpers.errMSG.showError;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import mssqlHandlers.MSSQL.EmploYouDB;

public class LogInController 
{
	
	@FXML private TextField usernameField;
	@FXML private TextField passwordField;
	@FXML private ImageView testImage;
	
	public void logIn(ActionEvent e) throws IOException
	{
		
		if(Check.checkLoginInput(usernameField, passwordField))
		{
			if(EmploYouDB.authenticateUser(usernameField,passwordField))
			{
				Main.setCurUser(usernameField.getText());
				MainViewController mainViewController = new MainViewController();
				mainViewController.drawMainViewPage((Stage)((Node)e.getSource()).getScene().getWindow());
			}
			else
			{
				usernameField.clear();passwordField.clear();
				showError.show("The Username or Password does't match!");
			}
		}
		else{usernameField.clear();passwordField.clear();}
	}
	
	public void signUp(ActionEvent e)
	{
		SignUpController signUpController = new SignUpController();
		try {signUpController.drawSignInPage((Stage)((Node)e.getSource()).getScene().getWindow());} 
		catch (IOException e1) {e1.printStackTrace();}
	}
	
	public void drawLoginPage(Stage st) throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource("loginView.fxml"));
		Stage stage = st;
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
}
