package model;

public class Name {
	private String name;
	
	public Name() {
		this.name = "";
	}
	public Name(String arg) {
		this.setName(arg);
	}

	//accessor & mutator
	public void setName(String arg) {
		this.name = arg;
	}
	public String getName() {
		return name;
	}
	
	
	
}
