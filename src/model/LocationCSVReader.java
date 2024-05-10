package model;

public class LocationCSVReader implements ReadData<Location> {
	public Location parseValue(String[] parts) {
		String name = parts[0].trim();
		String description = "";
		
		if (parts.length > 1) description = parts[1];
		
		return new Location(name, description);
	}

}
