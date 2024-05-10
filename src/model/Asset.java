package model;

import java.time.LocalDate;

public class Asset extends General_Info implements DataProcess {
	private Category category;
	private Location location;
	private LocalDate purchaseDate;
	private Double purchaseValue;
	private LocalDate warrantyExpDate;

	public Asset() {
		super();
		this.setCategory(new Category());
		this.setLocation(new Location());
		this.setPurchaseDate(null);
		this.setPurchaseValue(0.0);
		this.setWarrantyExpDate(null);

	}

	public Asset(String argN, String argD, Category argC, Location argL, LocalDate argP, Double argV, LocalDate argW) {
		super(argN, argD);
		this.setCategory(argC);
		this.setLocation(argL);
		this.setPurchaseDate(argP);
		this.setPurchaseValue(argV);
		this.setWarrantyExpDate(argW);

	}

	// accessor & mutator
	public void setCategory(Category arg) {
		category = arg;
	}

	public void setLocation(Location arg) {
		location = arg;
	}

	public Category getCategory() {
		return category;
	}

	public Location getLocation() {
		return location;
	}

	public void setPurchaseDate(LocalDate arg) {
		purchaseDate = arg;
	}

	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseValue(Double arg) {
		purchaseValue = arg;
	}

	public Double getPurchaseValue() {
		return purchaseValue;
	}

	public void setWarrantyExpDate(LocalDate arg) {
		warrantyExpDate = arg;
	}

	public LocalDate getWarrantyExpDate() {
		return warrantyExpDate;
	}
	
	private String getLocalDatetoString(LocalDate date) {
		if (date != null) return date.toString();
		else return "";
	}

	public void display() {
		System.out.println("Asset name: " + this.getName());
		System.out.println("Category name: " + this.getCategory().getName());
		System.out.println("Location name: " + this.getLocation().getName());
		System.out.println("Purchase Date: " + this.getPurchaseDate().toString());
		System.out.println("Purchase Value: " + this.getPurchaseValue());
		System.out.println("Description: " + this.getDescription());
		//System.out.println("Warranty Expiration Date: " + this.getWarrantyExpDate().toString());

	}

	public String saveToCsv() {
		String res;
		res = new String(this.getName() + "," + this.getCategory().getName() + ","+ this.getLocation().getName() 
				+ "," + this.getLocalDatetoString(getPurchaseDate()) + "," + this.getDescription() + "," +  this.getPurchaseValue()
				+ "," + (this.getLocalDatetoString(getWarrantyExpDate())));

		return res;
	}

}
