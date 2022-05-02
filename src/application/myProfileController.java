package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Objects.Interest;
import Objects.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import mssqlHandlers.MSSQL.EmploYouDB;
import javafx.scene.control.Label;



public class myProfileController 
{
    @FXML private Label interests;
    @FXML private Label joinedTrails;
	@FXML private Label dateJoined;
	@FXML private Label mainUserName;
	@FXML private AnchorPane pJoinedInterestsContainer;
	@FXML private AnchorPane pJoinedTrailsContainer;
	@FXML private Label secondaryUserName;
	@FXML private Circle userImage;
	@FXML private Label userRole;
	private int refreshLimiter = 0;
	
	public void drawMyProfile() throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource("myProfileView.fxml"));
		Stage stage = new Stage();
		stage.setResizable(false);
		
		Image icon = new Image(getClass().getResourceAsStream("icon.png"));
		stage.getIcons().add(icon);
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.titleProperty().set("My Profile");
	}
	
	public void displayUser()
	{
		if(refreshLimiter==0)
		{
			User curUser = EmploYouDB.getUserObject(Main.getCurUser());
			
			mainUserName.setText(curUser.getName());
			userImage.setFill(new ImagePattern(curUser.getPicAsImage()));
			secondaryUserName.setText("Username: "+ curUser.getName());
			dateJoined.setText("DateJoined: "+curUser.getDateJoined());
			userRole.setText("User-Role: "+getRole(curUser.getPermLevel()));
			
			List<String> trails = new ArrayList<String>();
			EmploYouDB.getOpenTrails().stream().forEach(x->
			{
				if(x.getUsers().stream().anyMatch(y->y.getName().equals(curUser.getName())))
				{trails.add(x.getName());}
			});
			
			if(trails.size()<=0)
			{joinedTrails.setText("You haven't joined any trails yet!");}
			else
			{
				trails.stream().forEach(x->
				{joinedTrails.setText(joinedTrails.getText()+x+"\n");});
			}
			
			
			List<Interest> userInterests = EmploYouDB.getInterests();			
			if(userInterests.stream().filter(x->x.getUserId()==curUser.getId()).count()<=0)
			{interests.setText("You haven't pointed out any interests yet!");}
				else
				{
					userInterests.stream().filter(x->x.getUserId()==curUser.getId()).forEach(y->
					{interests.setText(interests.getText()+"["+y.getName()+"]\n  "+y.getDescription()+"\n");});
				}
			refreshLimiter++;
		}
	}
	
	public void requestChange() throws IOException
	{
		requestViewController requestViewController = new requestViewController();
		requestViewController.drawRequestView();
	}
	
	private String getRole(int roleNumber)
	{
		switch(roleNumber)
		{
			case(1): return "User";
			case(2): return "Employer";
			case(3): return "Premium-Employer";
			case(4): return "Teacher";
			case(9): return "Administrator";
			default: return "Unknown";
		}
	}
}
