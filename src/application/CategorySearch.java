package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Category;
import model.CategoryCSVReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CategorySearch extends VBox implements LayoutHelper {
    private HashMap<String, Category> categories = new HashMap<>();
    private ObservableList<String> categoryList = FXCollections.observableArrayList();
    private final String file = "category.csv";

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
            }
                
            
        });
    }
}