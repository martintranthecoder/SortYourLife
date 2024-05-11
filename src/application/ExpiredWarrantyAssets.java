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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
//                            openViewAssetPage(data);
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

//    private void openViewAssetPage(Asset asset) {
//        ViewAssetDetails viewAssetDetails = new ViewAssetDetails(asset);
//        Stage stage = new Stage();
//        stage.setScene(new Scene(viewAssetDetails, 700, 450)); // Set the size as per your requirement
//        stage.setTitle("View Asset Details");
//        stage.show();
//    }

    private void loadExpiredAssets() {
        new Thread(() -> {
            List<Asset> assets = new ArrayList<>();
            String line;  // Declare line outside of the try block
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                boolean isFirstLine = true;
                while ((line = br.readLine()) != null) {
                    String finalLine = line;  // Create a final copy of line to use in lambda
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }
                    String[] values = finalLine.split(",");
                    if (values.length < 7) continue;
                    try {
                        String name = values[0].trim();
                        LocalDate warrantyExpDate = LocalDate.parse(values[6].trim());
                        if (warrantyExpDate.isBefore(LocalDate.now())) {
                            Asset asset = new Asset();
                            asset.setName(name);
                            asset.setWarrantyExpDate(warrantyExpDate);
                            assets.add(asset);
                        }
                    } catch (Exception e) {
                        Platform.runLater(() -> System.err.println("Error processing line: " + finalLine));
                    }
                }
                Platform.runLater(() -> table.getItems().setAll(assets));
            } catch (IOException e) {
                Platform.runLater(() -> System.err.println("Failed to read the file: " + e.getMessage()));
            }
        }).start();
    }

}
