package model;

public class CategoryCSVReader implements ReadData<Category> {

	
	public Category parseValue(String[] parts) {
		String name = parts[0];
		return new Category(name);
	}
	
	


}
