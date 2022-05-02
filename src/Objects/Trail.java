package Objects;

import java.util.ArrayList;
import java.util.List;

public class Trail 
{
	private String name;
	private int id;
	private List<User> users;
	private String description;
	private int companyId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public int getId()
	{return id;}
	
	public Trail(int Id,String Name,String Description,int CompanyId,List<User> Users)
	{
		this.id = Id;
		this.name = Name;
		this.description = Description;
		this.companyId = CompanyId;
		this.users = Users;
	}
	public Trail(int Id,String Name,String Description,int CompanyId)
	{
		this.id = Id;
		this.name = Name;
		this.description = Description;
		this.companyId = CompanyId;
		this.users = new ArrayList<User>();
	}
	
	
	public String ToString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.name);
		this.users.stream().forEach(u->{
			sb.append("\n"+u.getName());
		});
		
		return sb.toString();
	}
	
	public void addUser(User u)
	{
		this.users.add(u);
	}
}


