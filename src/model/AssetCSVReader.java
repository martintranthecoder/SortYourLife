package model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AssetCSVReader implements ReadData<Asset> {
	private CategoryCSVReader catRead = new CategoryCSVReader();
	private LocationCSVReader locRead = new LocationCSVReader();
	
	public Asset parseValue(String parts[]) {
		String name = parts[0];
		
		 // Parse category
        String categoryName = parts[1];
        Category category = new Category();
        try {
            category = getCategoryFromName(categoryName);
        } catch (IOException e) {
            // Handle the exception appropriately
            e.printStackTrace();
        }
        
		String locationName = parts[2];
		Location location = new Location();
		try {
            location = getLocationFromName(locationName);
        } catch (IOException e) {
            // Handle the exception appropriately
            e.printStackTrace();
        }

		
		String purchaseDate = parts[3];
		LocalDate purchaseD = null;
		if (!purchaseDate.equals("")) {
	        purchaseD = LocalDate.parse(purchaseDate, DateTimeFormatter.ISO_DATE);
	    }
		
		String description = parts[4];
		 
		String purchaseValue = parts[5];
		Double d = 0.0;
		if (!purchaseValue.equals("null")) d = Double.parseDouble(purchaseValue);
		
		String warranty = "";
		LocalDate warrantyD = null;
		
		if (parts.length > 6) {
			warranty = parts[6];
			
			if (!warranty.equals("")) {
	            warrantyD = LocalDate.parse(warranty, DateTimeFormatter.ISO_DATE);
	        }
		}
		
		return new Asset(name, description, category, location, purchaseD, d, warrantyD);
	}
	
	private Category getCategoryFromName(String name) throws IOException {
		return catRead.searchValue("category.csv", name);
	}
	
	private Location getLocationFromName(String name) throws IOException {
		return locRead.searchValue("location.csv", name);
	}

}
