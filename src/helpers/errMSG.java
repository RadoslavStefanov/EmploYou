package helpers;

import javafx.scene.control.Alert;

public class errMSG 
{
	public static class showError
	{
		public static void show(String msg)
		{
			 Alert alert = new Alert(Alert.AlertType.ERROR);
		     alert.setContentText(msg);
		     alert.show();
		}
	}
}
