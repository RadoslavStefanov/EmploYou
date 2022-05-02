package application;

import java.io.IOException;

import helpers.inputCheck.Check;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mssqlHandlers.MSSQL.EmploYouDB;

public class requestViewController 
{
		@FXML private TextField ticketDescription;
	    @FXML private TextField ticketName;
	    @FXML private TextField ticketRequester;
	
	public void drawRequestView() throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource("requestView.fxml"));
		Stage stage = new Stage();
		stage.setResizable(false);
		
		Image icon = new Image(getClass().getResourceAsStream("icon.png"));
		stage.getIcons().add(icon);
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.titleProperty().set("Create a request");
	}
	
	public void cancel(ActionEvent e)
	{
		Stage st = (Stage)((Node)e.getSource()).getScene().getWindow();
		st.close();
	}
	
	public void send(ActionEvent e)
	{
			if(Check.requestInputIsValid(ticketName, ticketRequester, ticketDescription))	
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION); alert.setContentText("The ticket has been sent!"); alert.show();
				EmploYouDB.addHistoryEntry("Created a ticket["+ticketName.getText()+"]", Main.getCurUser());
				EmploYouDB.createRequest(ticketName, ticketName, ticketDescription, EmploYouDB.getUserObject(Main.getCurUser()).getId());
				System.out.print("Implement ticket creation to MSSQL");
				Stage st = (Stage)((Node)e.getSource()).getScene().getWindow();
				st.close();
			}
	}
}
