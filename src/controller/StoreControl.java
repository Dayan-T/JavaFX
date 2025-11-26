package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

public class StoreControl {

    // =======================
    // UI Components
    // =======================

    @FXML private TableView<Product> tableProducts;

    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colType;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Double> colBuyPrice;
    @FXML private TableColumn<Product, Integer> colStock;
    @FXML private TableColumn<Product, String> colSpecific;
    @FXML private TableColumn<Product, Double> colDiscPrice;

    @FXML private Label lblCapital;
    @FXML private Label lblIncome;
    @FXML private Label lblCost;

    @FXML private Button btnDiscount;

    // =======================
    // Store State (dummy)
    // =======================

    private ObservableList<Product> products = FXCollections.observableArrayList();

    private double capital = 1000;
    private double income = 0;
    private double cost = 0;
    private boolean discountOn = false;


    // ================================================================
    // INITIALIZATION — executes automatically when FXML is loaded
    // ================================================================
    @FXML
    public void initialize() {

        // Table columns binding
        colName.setCellValueFactory(c -> c.getValue().nameProperty());
        colType.setCellValueFactory(c -> c.getValue().typeProperty());
        colPrice.setCellValueFactory(c -> c.getValue().sellPriceProperty().asObject());
        colBuyPrice.setCellValueFactory(c -> c.getValue().purPriceProperty().asObject());
        colStock.setCellValueFactory(c -> c.getValue().stockProperty().asObject());
        colSpecific.setCellValueFactory(c -> c.getValue().specificProperty());
        colDiscPrice.setCellValueFactory(c -> c.getValue().discPriceProperty().asObject());

        tableProducts.setItems(products);

        updateLabels();
    }



    // ================================================================
    // BUTTON: Load Demo
    // ================================================================
    @FXML
    private void handleLoadDemo() {

        products.clear();

        products.add(new Shoes("Nike AirMax", 70, 99, 10, 42));
        products.add(new Clothes("T-Shirt Zebra", 15, 29.99, 25, "S"));
        products.add(new Accessories("Gold Necklace", 100, 199, 3, "Necklace"));
        products.add(new Shoes("Adidas Run", 55, 89, 13, 41));
        products.add(new Clothes("Dress", 20, 49.99, 8, "M"));

        updateLabels();
    }



    // ================================================================
    // BUTTON: Buy Stock (increase stock)
    // ================================================================
    @FXML
    private void handleBuyStock() {
        Product p = tableProducts.getSelectionModel().getSelectedItem();
        if (p == null) {
            alert("Select a product before buying stock.");
            return;
        }

        p.set
