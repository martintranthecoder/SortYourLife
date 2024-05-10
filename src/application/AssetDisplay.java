package application;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Asset;
import model.AssetCSVReader;

import java.io.IOException;
import java.time.format.DateTimeFormatter;


public class AssetDisplay extends VBox implements LayoutHelper{
	/**
	 * This page is VBox object. All page like this will put in StackPane.
	 * Navigation page on the left could change visibility of page on the right.
	 */
	private Asset asset = new Asset();
	
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
	
	
	private final String file = "asset.csv";
	
	private ArrayList<HBox> layout;
	private final String title = "Asset Display";
	private final String line1 = "Asset's Name: ";
	private final String line2 = "Category: ";
	private final String line3 = "Location: ";
	private final String line4 = "Purchase date: ";
	private final String line5 = "Description: ";
	private final String line6 = "Purchased Value: ";
	private final String line7 = "Warranty Exp Date";
	

	public AssetDisplay(Asset existingAsset) {
	    super(30); // spacing parameter 30
	    super.setPadding(new Insets(40, 40, 40, 40));

	    layout = new ArrayList<HBox>();
	    
	    //Initial asset will have data shown 
	    this.asset = existingAsset;
	    
//	    try {
//            AssetCSVReader assetReader = new AssetCSVReader();
//            this.asset = assetReader.parseValue(existingAsset);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
	    
	    // Add layout elements to the ArrayList
	    layout.add(createTitle(title));
	    layout.add(createText(line1, asset.getName()));
	    layout.add(createText(line2, asset.getCategory().getName()));
	    layout.add(createText(line3, asset.getLocation().getName()));
	    layout.add(createText(line4, asset.getPurchaseDate() != null ? asset.getPurchaseDate().format(dateFormatter) : "N/A"));
	    layout.add(createText(line5, asset.getDescription().toString()));
	    layout.add(createText(line6, String.format("$%.2f", asset.getPurchaseValue())));
	    layout.add(createText(line7, asset.getWarrantyExpDate() != null ? asset.getWarrantyExpDate().format(dateFormatter) : "N/A"));
	   
	  
	    
	    initialize(this, layout);
	   
	    
	}


}