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
import model.Location;
import model.LocationCSVReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LocationSearch extends VBox implements LayoutHelper {
    private HashMap<String, Location> locations = new HashMap<>();
    private ObservableList<String> locationList = FXCollections.observableArrayList();
    private final String file = "location.csv";

    private ArrayList<HBox> layout = new ArrayList<>();
    private final String title = "Search by Location";
    private final String line1 = "Location: ";

    public LocationSearch() {
        super(30); // spacing parameter 30
        super.setPadding(new Insets(40, 40, 40, 40));

        try {
            LocationCSVReader locationReader = new LocationCSVReader();
            locations = locationReader.readData(file);
            locationList.addAll(locations.keySet());
        } catch (IOException e) {
            e.printStackTrace(); 
        }

        layout.add(createTitle(title));
        layout.add(createDropdownList(line1, locationList));
        layout.add(searchLastLine());
       

		clearButtonAction(layout, 1);
        buttonAction(layout);
        
        initialize(this, layout);
    }

    private void buttonAction(ArrayList<HBox> arg) {
        ((Button) arg.get(arg.size() - 1).getChildren().get(0)).setOnAction(e -> {
        	Location locationToSearch = locations.get(((ComboBox<String>) layout.get(1).lookup("#choice")).getValue());
            String locationName = locationToSearch.getName();
            if (locationName.isEmpty()) {
				// Show an error message if the name is empty
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setContentText("Location can not be empty!");
				alert.showAndWait();
            }
                
            
        });
    }
}