package controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import java.net.URL;
import java.util.ResourceBundle;

public class StoreControl implements Initializable {

    // Liens avec le FXML (@FXML est indispensable pour SceneBuilder)
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

    private DBManager dbManager;
    private ObservableList<Product> productList;

    private double currentCapital;
    private double currentIncome;
    private double currentCost;
    private boolean isDiscountActive = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbManager = new DBManager();

        // Configuration des colonnes
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBuyPrice.setCellValueFactory(new PropertyValueFactory<>("purprice"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("sellprice"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("nbitems"));

        // Affichage dynamique du Type (Polymorphisme)
        colType.setCellValueFactory(cell -> {
            Product p = cell.getValue();
            if (p instanceof Clothes) return new SimpleStringProperty("Clothing");
            if (p instanceof Shoes) return new SimpleStringProperty("Shoes");
            return new SimpleStringProperty("Accessory");
        });

        // Affichage du prix soldé
        colDiscPrice.setCellValueFactory(cell -> {
            double dp = cell.getValue().getDiscprice();
            return new SimpleObjectProperty<>(dp > 0 ? dp : 0.0);
        });

        // Affichage de la taille (Détails spécifiques)
        colSpecific.setCellValueFactory(cell -> {
            Product p = cell.getValue();
            if (p instanceof Clothes) return new SimpleStringProperty("Size: " + ((Clothes)p).getSize());
            if (p instanceof Shoes) return new SimpleStringProperty("Size: " + ((Shoes)p).getShoeSize());
            return new SimpleStringProperty("-");
        });

        loadData();
    }

    private void loadData() {
        productList = FXCollections.observableArrayList(dbManager.loadProducts());

        if (isDiscountActive) {
            for (Product p : productList) p.applyDiscount();
        }

        tableProducts.setItems(productList);

        double[] stats = dbManager.loadFinancials();
        currentCapital = stats[0];
        currentIncome = stats[1];
        currentCost = stats[2];
        updateLabels();
    }

    private void updateLabels() {
        lblCapital.setText(String.format("%.2f €", currentCapital));
        lblIncome.setText(String.format("%.2f €", currentIncome));
        lblCost.setText(String.format("%.2f €", currentCost));
    }

    // --- ACTIONS UTILISATEUR ---

    @FXML public void handleLoadDemo() {
        dbManager.resetDemoData();
        isDiscountActive = false;
        btnDiscount.setText("ACTIVATE DISCOUNTS");
        btnDiscount.setStyle("");
        loadData();
        showAlert("Succès", "Données du scénario chargées !");
    }

    @FXML public void handleBuyStock() {
        handleTransaction(true);
    }

    @FXML public void handleSellItem() {
        handleTransaction(false);
    }

    private void handleTransaction(boolean isBuy) {
        Product selected = tableProducts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Veuillez sélectionner un produit.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle(isBuy ? "Achat Stock" : "Vente Article");
        dialog.setHeaderText((isBuy ? "Acheter : " : "Vendre : ") + selected.getName());
        dialog.setContentText("Quantité :");

        dialog.showAndWait().ifPresent(val -> {
            try {
                int qty = Integer.parseInt(val);
                if (qty <= 0) throw new IllegalArgumentException("La quantité doit être positive");

                if (isBuy) {
                    double cost = qty * selected.getPurprice();
                    if (cost > currentCapital) throw new IllegalArgumentException("Budget insuffisant !");

                    selected.setNbitems(selected.getNbitems() + qty);
                    currentCapital -= cost;
                    currentCost += cost;
                } else {
                    if (qty > selected.getNbitems()) throw new IllegalArgumentException("Stock insuffisant !");

                    selected.setNbitems(selected.getNbitems() - qty);
                    double income = qty * selected.getEffectivePrice();
                    currentCapital += income;
                    currentIncome += income;
                }

                dbManager.updateStock(selected);
                dbManager.saveFinancials(currentCapital, currentIncome, currentCost);
                loadData();
            } catch (Exception e) {
                showAlert("Erreur", e.getMessage());
            }
        });
    }

    @FXML public void handleToggleDiscounts() {
        isDiscountActive = !isDiscountActive;
        if (isDiscountActive) {
            for (Product p : productList) p.applyDiscount();
            btnDiscount.setText("STOP DISCOUNTS");
            btnDiscount.setStyle("-fx-background-color: #ffcccc;");
        } else {
            for (Product p : productList) p.unApplyDiscount();
            btnDiscount.setText("ACTIVATE DISCOUNTS");
            btnDiscount.setStyle("");
        }
        tableProducts.refresh();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
