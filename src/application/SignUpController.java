package application;

import java.io.IOException;

import helpers.inputCheck.Check;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mssqlHandlers.MSSQL.EmploYouDB;

public class SignUpController 
{
	@FXML private TextField signUpUsername;
	@FXML private TextField signUpPasswordField;
	@FXML private TextField signUpPasswordField2;
	@FXML private TextField signUpUserImage;
	
	public void register(ActionEvent e) throws IOException
	{
		if(Check.checkRegisterInput(signUpUsername, signUpPasswordField, signUpPasswordField2,signUpUserImage))
		{
			EmploYouDB.addNewUser(signUpUsername,signUpPasswordField,signUpUserImage);
			Main.setCurUser(signUpUsername.getText());
			MainViewController mainViewController = new MainViewController();
			mainViewController.drawMainViewPage((Stage)((Node)e.getSource()).getScene().getWindow());
		}
	}
	
	public void drawSignInPage(Stage st) throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource("signUpView.fxml"));
		Stage stage = st;
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();		
	}
	
	public void backToLogin(ActionEvent e)
	{
		LogInController logInController = new LogInController();
		try {logInController.drawLoginPage((Stage)((Node)e.getSource()).getScene().getWindow());} 
		catch (IOException e1) {System.out.print(e1.getMessage());}
	}
	
}
