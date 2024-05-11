package application;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

public class Main extends Application {
	final private String appName = "SortYourLife";
	
	private String file = "asset.csv";

	private StackPane rightSection = new StackPane();

	private WelcomePage welcomePage = new WelcomePage();
	private NewCategory newCategory = new NewCategory();
	private NewLocation newLocation = new NewLocation();
	private NewAsset newAsset = new NewAsset();
	private SearchAsset search = new SearchAsset();
	private CategorySearch cat = new CategorySearch();
	private LocationSearch loc = new LocationSearch();
	private ExpiredWarrantyAssets expiredWarrantyAssets = new ExpiredWarrantyAssets();
	private Stage primaryStage;
	//Testing Page for single element
	//private TestPage test = new TestPage();
	
	
	private void showExpiredWarrantyWarning() {
	    List<Asset> expiredAssets = getExpiredAssets();
	    if (!expiredAssets.isEmpty()) {
	    	Stage dialogStage = new Stage();
	        dialogStage.setTitle("Alert!");

	        VBox alertContent = new VBox();
			alertContent.setSpacing(10);
			
			Text titleText = new Text("You have expired warranties!");
	        alertContent.getChildren().add(titleText);

	        HBox twoButtons = new HBox();
	        twoButtons.setSpacing(10);
	        
	        Button okButton = new Button("OK");
	        okButton.setOnAction(event -> {
	            // Close the dialog stage (navigate to Home)
	            dialogStage.close();
	        });
	        
	        Button showMeButton = new Button("Show Me");
	        showMeButton.setOnAction(event -> {
	            // Hide the dialog stage
	            dialogStage.close();
	            
	            // Make "Expired Warranty Assets" page visible
	            expiredWarrantyAssets.setVisible(true);
	            homeNavigator.setVisible(true);
	            
	        });
	        twoButtons.getChildren().addAll(okButton, showMeButton);
	        
	        alertContent.getChildren().add(twoButtons);

	        Scene alertScene = new Scene(alertContent, 300, 200);
	        dialogStage.setScene(alertScene);
	        dialogStage.initModality(Modality.APPLICATION_MODAL);
	        dialogStage.showAndWait();
	    }
	}

	private List<Asset> getExpiredAssets() {
	    List<Asset> expiredAssets = new ArrayList<>();
	    try {
	        AssetCSVReader assetReader = new AssetCSVReader();
	        HashMap<String, Asset> assetMap = assetReader.readData(file);

	        // Iterate over the assets and add expired ones to the list
	        for (Asset asset : assetMap.values()) {
	            LocalDate warrantyExpDate = asset.getWarrantyExpDate();
	            if (warrantyExpDate != null && warrantyExpDate.isBefore(LocalDate.now())) {
	                expiredAssets.add(asset);
	            }
	        }
	    } catch (IOException e) {
	        // Handle file reading error
	        System.err.println("Failed to read the file: " + e.getMessage());
	    }
	    return expiredAssets;
	}
	
	

	private HomeNavigator homeNavigator = new HomeNavigator(choice -> {
		welcomePage.setVisible("Welcome Page".equals(choice));
		newCategory.setVisible("New Category Page".equals(choice));
		newLocation.setVisible("New Location Page".equals(choice));
		newAsset.setVisible("New Asset".equals(choice));
		search.setVisible("Search Page".equals(choice));
		cat.setVisible("Search by Category Page".equals(choice));
		loc.setVisible("Search by Location Page".equals(choice));
		expiredWarrantyAssets.setVisible("Expired Warranty Assets".equals(choice));
		//test page
		//test.setVisible("Reports Page".equals(choice));
	});

	private NavigationMenu navigationMenu = new NavigationMenu(choice -> {
		welcomePage.setVisible("Welcome Page".equals(choice));
		newCategory.setVisible("New Category Page".equals(choice));
		newLocation.setVisible("New Location Page".equals(choice));
		newAsset.setVisible("New Asset Page".equals(choice));
		search.setVisible("Search Page".equals(choice));
		cat.setVisible("Search by Category Page".equals(choice));
		loc.setVisible("Search by Location Page".equals(choice));
		expiredWarrantyAssets.setVisible("Expired Warranty Assets".equals(choice));
		homeNavigator.setVisible(!welcomePage.isVisible());
		//test page
		//test.setVisible("Reports Page".equals(choice));
	});

	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		rightSection.getChildren().addAll(welcomePage, newCategory, newLocation, newAsset, search, cat, loc, expiredWarrantyAssets, homeNavigator);
		initializePage();

//		rightSection.getChildren().add(test);
//		test.setVisible(false);
		
		HBox mainBkgd = new HBox();
		mainBkgd.getChildren().addAll(navigationMenu, rightSection);

		Scene scene = new Scene(mainBkgd, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.setTitle(appName);
		primaryStage.show();
		
		showExpiredWarrantyWarning();

	}

	public void initializePage() {
		rightSection.setAlignment(Pos.TOP_LEFT);
		// initialize with welcome page
		rightSection.getChildren().get(0).setVisible(true);
		// other page not visible
		rightSection.getChildren().get(1).setVisible(false);
		rightSection.getChildren().get(2).setVisible(false);
		rightSection.getChildren().get(3).setVisible(false);
		// home navigator
		rightSection.getChildren().get(4).setVisible(false);
		rightSection.getChildren().get(5).setVisible(false);
		//Test page visible
		rightSection.getChildren().get(6).setVisible(false);
		rightSection.getChildren().get(7).setVisible(false);
		rightSection.getChildren().get(8).setVisible(false);
	}

	public static void main(String[] args) {

		launch(args);
	}
}