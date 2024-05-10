package model;

public class Category extends Name implements DataProcess{
	
	public Category() {
		super();
	}
	public Category(String arg) {
		super(arg);
	
	}
	public void display() {
		System.out.println("Category: " + this.getName());
	}
	public String saveToCsv() {
		String res;
		res = new String(this.getName());
		
		return res;
	}
	

}
