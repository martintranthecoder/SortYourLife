package model;

public class Location extends General_Info implements DataProcess{
	 
	public Location() {
		super();
	}
	
	public Location(String argN, String argD) {
		super(argN,argD);
	}
	
	public void display() {
		System.out.println("Location: " + this.getName());
		System.out.println("Description: " + this.getDescription().replaceAll("\\\\n", "\n"));
	}

	public String saveToCsv() {
		String res;
		res = new String(this.getName() + "," + this.getDescription().replaceAll("\n", "\\\\n"));
		
		return res;
	}
}
