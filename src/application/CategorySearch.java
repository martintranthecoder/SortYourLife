package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Asset;
import model.Category;
import model.CategoryCSVReader;
import model.AssetCSVReader;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class CategorySearch extends VBox implements LayoutHelper {
    private HashMap<String, Category> categories = new HashMap<>();
    private ObservableList<String> categoryList = FXCollections.observableArrayList();
    private final String file = "category.csv";
    
    private HashMap<String, Asset> assets = new HashMap<>();
    private ObservableList<String> assetList = FXCollections.observableArrayList();
    private final String asset_file = "asset.csv";

    private ArrayList<HBox> layout = new ArrayList<>();
    private final String title = "Search by Category";
    private final String line1 = "Category: ";

    public CategorySearch() {
        super(30); // spacing parameter 30
        super.setPadding(new Insets(40, 40, 40, 40));

        try {
            CategoryCSVReader categoryReader = new CategoryCSVReader();
            categories = categoryReader.readData(file);
            categoryList.addAll(categories.keySet());
        } catch (IOException e) {
            e.printStackTrace(); 
        }

        layout.add(createTitle(title));
        layout.add(createDropdownList(line1, categoryList));
        layout.add(searchLastLine());
       

		clearButtonAction(layout, 1);
        buttonAction(layout);
        
        initialize(this, layout);
    }

    private void buttonAction(ArrayList<HBox> arg) {
        ((Button) arg.get(arg.size() - 1).getChildren().get(0)).setOnAction(e -> {
            Category categoryToSearch = categories.get(((ComboBox<String>) layout.get(1).lookup("#choice")).getValue());
            String categoryName = categoryToSearch.getName();
            if (categoryName.isEmpty()) {
				// Show an error message if the name is empty
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setContentText("Category can not be empty!");
				alert.showAndWait();
            } else {
            	 showAssetsForCategory(categoryName);
            }
                
            
        });
    }
    
    
    private void showAssetsForCategory(String selectedCategory) {
        ObservableList<Asset> assetsForCategory = FXCollections.observableArrayList();
        
        try {
            AssetCSVReader assetReader = new AssetCSVReader();
            assets = assetReader.readData(asset_file);
            assetList.addAll(assets.keySet());
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        
        // Filter the list of assets to include only those belonging to the selected category
        for (Asset asset : assets.values()) {
            if (asset.getCategory().getName().equals(selectedCategory)) {
                assetsForCategory.add(asset);
            }
        }

        // Create a new stage to display the assets
        Stage assetsStage = new Stage();
        TableView<Asset> tableView = new TableView<>();
        TableColumn<Asset, String> assetNameColumn = new TableColumn<>("Asset Name");
        assetNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Asset, String> locationColumn = new TableColumn<>("Location");
		locationColumn.setCellValueFactory(cellData -> {
			Asset asset = cellData.getValue();
			return new SimpleStringProperty(asset.getLocation().getName());
		});

        TableColumn<Asset, LocalDate> warrantyExpColumn = new TableColumn<>("Warranty Expiration");
        warrantyExpColumn.setCellValueFactory(new PropertyValueFactory<>("warrantyExpDate"));

        TableColumn<Asset, Void> moreButtonColumn = new TableColumn<>("Details");
        moreButtonColumn.setCellFactory(param -> {
            TableCell<Asset, Void> cell = new TableCell<>() {
                private final Button moreButton = new Button("More...");

                {
                    moreButton.setOnAction(event -> {
                        // Handle "More..." button click
                        Asset asset = getTableView().getItems().get(getIndex());
                     // Create a new AssetDisplay stage with the selected Asset
                        Stage assetDisplayStage = new Stage();
                        AssetDisplay assetDisplay = new AssetDisplay(asset);
                        Scene scene = new Scene(assetDisplay);
                        assetDisplayStage.setScene(scene);
                        assetDisplayStage.show();
                        
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(moreButton);
                    }
                }
            };
            return cell;
        });

        tableView.getColumns().addAll(assetNameColumn, locationColumn, warrantyExpColumn, moreButtonColumn);
        tableView.setItems(assetsForCategory);
        
        
        // Calculate the total width required by the columns
        double totalWidth = assetNameColumn.getWidth() + locationColumn.getWidth() + warrantyExpColumn.getWidth() + moreButtonColumn.getWidth();
        
        // Add some padding to the calculated width
        double padding = 50; // Adjust this value as needed
        
        // Set the preferred width of the stage
        assetsStage.setMinWidth(totalWidth + padding);

        VBox root = new VBox(tableView);
        Scene scene = new Scene(root);
        assetsStage.setScene(scene);
        assetsStage.show();
    }

}