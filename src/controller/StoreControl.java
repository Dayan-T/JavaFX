package controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import model.*;

import java.util.*;
import java.util.stream.Collectors;

public class StoreControl {

    @FXML private TableView<Product> tableProducts;

    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colType;
    @FXML private TableColumn<Product, Number> colPurPrice;
    @FXML private TableColumn<Product, Number> colSellPrice;
    @FXML private TableColumn<Product, Number> colDiscPrice;
    @FXML private TableColumn<Product, Number> colStock;
    @FXML private TableColumn<Product, String> colSize;
    @FXML private Label lblCapital;
    @FXML private Label lblIncome;
    @FXML private Label lblCost;
    @FXML private Label lblProfit;


    @FXML private TextField txtQuantity;

    private ObservableList<Product> masterList = FXCollections.observableArrayList();
    private ObservableList<Product> filteredList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colName.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getName()));

        colType.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(
                        (data.getValue() instanceof Shoes) ? "Shoes" :
                                (data.getValue() instanceof Clothes) ? "Clothes" :
                                        "Accessories"
                ));

        colPurPrice.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("purprice"));
        colSellPrice.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("sellprice"));
        colDiscPrice.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("discprice"));
        colStock.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nbitems"));
        colSize.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(
                        (data.getValue() instanceof Shoes s) ? String.valueOf(s.getShoeSize()) :
                                (data.getValue() instanceof Clothes c) ? String.valueOf(c.getSize()) :
                                        "-"
                ));

        tableProducts.setItems(filteredList);
        updateStats();
    }

    // DB Management

    private int getQuantity() {
        try {
            String t = txtQuantity.getText();
            if (t == null || t.isBlank()) return 1;
            return Integer.parseInt(t);
        } catch (Exception e) {
            return -1;
        }
    }

    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void updateStats() {
        lblCapital.setText("" + (Product.getCapital()+Product.getIncome()));
        lblIncome.setText("" + Product.getIncome());
        lblCost.setText("" + Product.getCost());
        lblProfit.setText("" + (Product.getIncome()-Product.getCost()));
    }

    // DB laoding

    public void initializeWithProducts(List<Product> products) {
        masterList.clear();
        masterList.addAll(products);
        filteredList.setAll(masterList);
        tableProducts.refresh();
        updateStats();
    }

    // buying/selling methods with number of items

    @FXML
    private void handleBuy() {
        Product p = tableProducts.getSelectionModel().getSelectedItem();
        if (p == null) { show("Select a product."); return; }

        int qty = getQuantity();
        if (qty <= 0) { show("Invalid quantity."); return; }

        p.buy(qty);
        DBManager.updateProduct(p);
        tableProducts.refresh();
        updateStats();
    }

    @FXML
    private void handleSell() {
        Product p = tableProducts.getSelectionModel().getSelectedItem();
        if (p == null) {
            show("Select a product.");
            return;
        }

        int qty = getQuantity();
        if (qty <= 0) {
            show("Invalid quantity.");
            return;
        }

        p.sell(qty);
        DBManager.updateProduct(p);
        tableProducts.refresh();
        updateStats();
    }

    // discount application

    @FXML
    private void handleApplyDiscounts() {
        for (Product p : masterList)
            p.applyDiscount();
        DBManager.updateAllProducts(masterList);
        tableProducts.refresh();
    }

    @FXML
    private void handleRemoveDiscounts() {
        for (Product p : masterList)
            p.unApplyDiscount();
        DBManager.updateAllProducts(masterList);
        tableProducts.refresh();
    }

    // Filters (shoes, clothes...)

    @FXML
    private void handleFilterAll() {
        filteredList.setAll(masterList);
    }

    @FXML
    private void handleFilterClothes() {
        filteredList.setAll(
                masterList.stream().filter(p -> p instanceof Clothes).collect(Collectors.toList())
        );
    }

    @FXML
    private void handleFilterShoes() {
        filteredList.setAll(
                masterList.stream().filter(p -> p instanceof Shoes).collect(Collectors.toList())
        );
    }

    @FXML
    private void handleFilterAccessories() {
        filteredList.setAll(
                masterList.stream().filter(p -> p instanceof Accessories).collect(Collectors.toList())
        );
    }

    // Sorting by price

    @FXML
    private void handleSortByPrice() {
        filteredList.sort(Comparator.comparing(Product::getSellprice));
    }

    @FXML
    private void handleEditProduct() {
        Product p = tableProducts.getSelectionModel().getSelectedItem();
        if (p == null) { show("Select a product."); return; }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Edit Product");
        dialog.setHeaderText("Editing: " + p.getName());
        dialog.setContentText("Enter new value (size/shoeSize):");

        Optional<String> input = dialog.showAndWait();
        if (input.isEmpty()) return;

        String value = input.get();
        try {
            int intVal = Integer.parseInt(value);

            if (p instanceof Clothes clothes) {
                clothes.setSize(intVal);
            } else if (p instanceof Shoes shoes) {
                shoes.setShoeSize(intVal);
            } else {
                show("This item has no size to edit.");
                return;
            }

            DBManager.updateProduct(p);
            tableProducts.refresh();

        } catch (Exception e) {
            show("Invalid value.");
        }
    }

    @FXML
    private void handleDisplayItem() {
        Product p = tableProducts.getSelectionModel().getSelectedItem();
        if (p == null) {
            show("Select a product to display.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product Details");
        alert.setHeaderText(p.getName());
        alert.setContentText(p.toString());
        alert.showAndWait();
    }

    @FXML
    private void handleDeleteProduct() {
        Product p = tableProducts.getSelectionModel().getSelectedItem();
        if (p == null) {
            show("Select a product.");
            return;
        }

        if (p.getNbitems() != 0) {
            show("Cannot delete: stock must be 0.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("Delete " + p.getName() + "?");
        confirm.setContentText("Are you sure?");
        Optional<ButtonType> r = confirm.showAndWait();

        if (r.isEmpty() || r.get() != ButtonType.OK) return;

        DBManager.deleteProduct(p.getId());
        masterList.remove(p);
        filteredList.remove(p);
        tableProducts.refresh();
    }
}
