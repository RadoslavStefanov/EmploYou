package application;

import java.io.IOException;
import java.util.List;
import Objects.Trail;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mssqlHandlers.MSSQL.EmploYouDB;

public class MainViewController 
{
	@FXML private Text userNameDisplay;
	@FXML private Label welcomeText;
	@FXML private Label userHistoryField;
	@FXML private Circle userMiniPicture;
	@FXML private VBox trailVBox;
	private int refreshLimiter = 0;

	
	public void displayUser()
	{
		if(refreshLimiter==0)
		{
			String user = Main.getCurUser();
			userNameDisplay.setText("User: "+user);
			welcomeText.setText("welcome back "+user);		
		
			userHistoryField.setText(EmploYouDB.getUserHistory(user));
			EmploYouDB.addHistoryEntry("User "+user+" logged in.");
			new Thread(() -> 
			{
				Image image = new Image(EmploYouDB.getUserPicture(user));
				userMiniPicture.setFill(new ImagePattern(image));
			}).start();
			
			drawOpenTrails(EmploYouDB.getOpenTrails());
			refreshLimiter++;
		}
		
	}
	
	public void highLight(MouseEvent e)
	{
		Bloom bloom = new Bloom();
		AnchorPane ap = (AnchorPane) e.getSource();
		ap.setEffect(bloom);
	}
	
	public void removeHighLight(MouseEvent e)
	{
		AnchorPane ap = (AnchorPane) e.getSource();
		ap.setEffect(null);
	}
	
	public void logOut(MouseEvent e) throws IOException
	{
		EmploYouDB.addHistoryEntry("User "+Main.getCurUser()+" logged out.",Main.getCurUser());
		Main.setCurUser("");
		LogInController logInController = new LogInController();
		logInController.drawLoginPage((Stage)((Node)e.getSource()).getScene().getWindow());
	}
	
	public void drawOpenTrails(List<Trail> lt)
	{
		lt.stream().forEach(x->
		{
			AnchorPane tempAnchor = new AnchorPane();
			
			Circle c = new Circle(15);
			c.layoutXProperty().set(20);
			c.layoutYProperty().set(25);
			
			Image fillImage = new Image(EmploYouDB.getUserPicture(EmploYouDB.getUserNameById(x.getCompanyId())));
			c.setFill(new ImagePattern(fillImage));
			
			Label trailName = new Label();
			String tempName = x.getName();
			trailName.setText(tempName);
			trailName.wrapTextProperty().set(true);
			trailName.layoutXProperty().set(40);
			trailName.layoutYProperty().set(17);
			
			
			tempAnchor.getChildren().add(c);
			tempAnchor.getChildren().add(trailName);
			
			tempAnchor.prefHeight(50);
			
			trailVBox.getChildren().add(tempAnchor);
			trailVBox.setSpacing(15);
		});
	}
	
	public void drawMainViewPage(Stage st) throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
		Stage stage = st;
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.titleProperty().set("EmploYou");
	}
	
	public void showRequestView(MouseEvent e) throws IOException
	{
		requestViewController requestViewController = new requestViewController();
		requestViewController.drawRequestView();
	}
	
	public void showProfile(MouseEvent e) throws IOException
	{
		myProfileController myProfileController = new myProfileController();
		myProfileController.drawMyProfile();
	}
	
	public void showTeachers(MouseEvent e) throws IOException
	{
		staffViewController staffViewController = new staffViewController();
		staffViewController.drawTeachersView();
	}
	
	public void showPartners(MouseEvent e) throws IOException
	{
		partnersController partnersController = new partnersController();
		partnersController.drawPartnersView();
	}
	
	public void showOpenTrails(MouseEvent e) throws IOException
	{
		openTrailsController openTrailsController = new openTrailsController();
		openTrailsController.drawOpenTrailsView();
	}
	
	public void showMyTrails(MouseEvent e) throws IOException
	{
		myTrailsController myTrailsController = new myTrailsController();
		myTrailsController.drawMyTrailsView();
	}
}
