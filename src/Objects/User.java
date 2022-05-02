package Objects;

import javafx.scene.image.Image;
import mssqlHandlers.MSSQL.EmploYouDB;

public class User 
{
	private String name;
	private int id;
	private String profilePicUrl;
	private String password;
	private String dateJoined;
	private int permLevel;
	
	public User(String Name,int Id,String ProfilePicUrl)
	{
		this.name=Name;
		this.id=Id;
		this.profilePicUrl=ProfilePicUrl;
		this.password="";
		this.dateJoined = "";
		this.permLevel = 0;
	}

	public User(String Name,int Id,String ProfilePicUrl,String Password,String DateJoined,int PermLevel)
	{
		this.name=Name;
		this.id=Id;
		this.profilePicUrl=ProfilePicUrl;
		this.password=Password;
		this.dateJoined = DateJoined;
		this.permLevel = PermLevel;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDateJoined() {
		return dateJoined;
	}

	public void setDateJoined(String dateJoined) {
		this.dateJoined = dateJoined;
	}

	public int getPermLevel() {
		return permLevel;
	}

	public void setPermLevel(int permLevel) {
		this.permLevel = permLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProfilePicUrl() {
		return profilePicUrl;
	}

	public void setProfilePicUrl(String profilePicUrl) {
		this.profilePicUrl = profilePicUrl;
	}

	public Image getPicAsImage()
	{
		Image image = new Image(EmploYouDB.getUserPicture(name));
		return image;
	}

}

