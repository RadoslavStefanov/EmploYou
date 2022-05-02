package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Objects.Trail;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import mssqlHandlers.MSSQL.EmploYouDB;

public class myTrailsController 
{
    @FXML private FlowPane flowPane;
    @FXML private ScrollPane scrollPane;
    private int refreshLimiter = 0;
    
    public void drawMyTrailsView() throws IOException
    {
    	Parent root = FXMLLoader.load(getClass().getResource("myTrailsView.fxml"));
		Stage stage = new Stage();
		stage.setResizable(false);
		
		Image icon = new Image(getClass().getResourceAsStream("icon.png"));
		stage.getIcons().add(icon);
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.titleProperty().set("My Trails");
    }
    
    public void load(MouseEvent e)
    {
    	if(refreshLimiter==0)
    	{
    		List<Trail> trails = new ArrayList<Trail>();
    		
        	EmploYouDB.getOpenTrails().stream().forEach(x->
    		{if((x.getUsers().stream().anyMatch(y->y.getName().equals(Main.getCurUser()))))
    		{trails.add(x);}});
        	        	
        	trails.forEach(x->
        	{
        		AnchorPane ap = new AnchorPane();
        		ap.prefWidth(600);
        		ap.prefHeight(70);
        			SplitPane sp = new SplitPane();
        			sp.setDividerPositions(0.22);
            			AnchorPane picture = new AnchorPane();
            				ImageView userPic = new ImageView();
            				userPic.setImage(EmploYouDB
            						.getAllUsers()
            						.stream().filter(y->y.getId()==x.getCompanyId())
            						.findFirst()
            						.get()
            						.getPicAsImage());
            			AnchorPane nameDescription = new AnchorPane();
            			nameDescription.setMaxHeight(70);
            				Label name = new Label();
            				name.setText(x.getName());
            				name.layoutXProperty().set(8);
            				name.layoutYProperty().set(6);            				
            				Label description = new Label();
            				description.setStyle("-fx-border-color: black");
            				description.layoutXProperty().set(8);
            				description.layoutYProperty().set(25);
            				description.setText(x.getDescription());
            				
            	
	            sp.setMaxHeight(70);
	            				
	            userPic.fitWidthProperty().set(100); 
	            userPic.fitHeightProperty().bind(nameDescription.heightProperty()); 
	            				
            	nameDescription.getChildren().add(name);
            	nameDescription.getChildren().add(description);
            	
            	picture.getChildren().add(userPic);      	
            	
            	sp.getItems().addAll(picture,nameDescription);
            	
            	ap.getChildren().add(sp);
            	
            	flowPane.getChildren().add(ap);
            	
        	});
        	scrollPane.setContent(flowPane);
        	refreshLimiter++;
    	}
    	
    }
}

