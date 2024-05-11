package application;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Asset;
import model.AssetCSVReader;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpiredWarrantyAssets extends VBox {

    private TableView<Asset> table = new TableView<>();
    private final String file = "asset.csv";

    public ExpiredWarrantyAssets() {
        super(10);
        setPadding(new Insets(10));
        
        // Add spacer region to move the table down
        Region spacer = new Region();
        spacer.setPrefHeight(50); // Adjust the height as needed
        
        setupTableColumns();
        loadExpiredAssets();
       
        
        
        this.getChildren().addAll(spacer, table);
    }

    private void setupTableColumns() {
        TableColumn<Asset, String> nameCol = new TableColumn<>("Asset Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setMinWidth(200);

        TableColumn<Asset, LocalDate> expiryDateCol = new TableColumn<>("Expiration Date");
        expiryDateCol.setCellValueFactory(new PropertyValueFactory<>("warrantyExpDate"));
        expiryDateCol.setMinWidth(200);

        TableColumn<Asset, Void> editCol = new TableColumn<>("Details");
        editCol.setCellFactory(new Callback<TableColumn<Asset, Void>, TableCell<Asset, Void>>() {
            @Override
            public TableCell<Asset, Void> call(final TableColumn<Asset, Void> param) {
                final TableCell<Asset, Void> cell = new TableCell<Asset, Void>() {
                    private final Button btn = new Button("Details");
                    {
                        btn.setOnAction((event) -> {
                            Asset data = getTableView().getItems().get(getIndex());
                            Stage assetDisplayStage = new Stage();
                            AssetDisplay assetDisplay = new AssetDisplay(data);
                            Scene scene = new Scene(assetDisplay, 700, 450);
                            assetDisplayStage.setScene(scene);
                            assetDisplayStage.show();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        });

        table.getColumns().addAll(nameCol, expiryDateCol, editCol);
    }

    private void loadExpiredAssets() {
        new Thread(() -> {
            List<Asset> assets = new ArrayList<>();
            try {
                AssetCSVReader assetReader = new AssetCSVReader();
                HashMap<String, Asset> assetMap = assetReader.readData(file);

                // Iterate over the assets and add expired ones to the list
                for (Asset asset : assetMap.values()) {
                    LocalDate warrantyExpDate = asset.getWarrantyExpDate();
                    if (warrantyExpDate != null && warrantyExpDate.isBefore(LocalDate.now())) {
                        assets.add(asset);
                    }
                }

                // Update the UI on the JavaFX application thread
                Platform.runLater(() -> table.getItems().setAll(assets));
            } catch (IOException e) {
                // Handle file reading error
                Platform.runLater(() -> System.err.println("Failed to read the file: " + e.getMessage()));
            }
        }).start();
    }

}
