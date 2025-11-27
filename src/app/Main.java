package app;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import model.DBManager;
import model.Product;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        //CHECK DATABASE +LOAD PRODUCTS
        List<Product> initialProducts;

        try {
            initialProducts = DBManager.loadProducts();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("DATABASE ERROR");
            alert.setHeaderText("Cannot start WomenShop");
            alert.setContentText("Error loading database:\n" + e.getMessage());
            alert.showAndWait();
            System.exit(1);
            return;
        }

        //Load UI
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Mainview.fxml"));
            Scene scene = new Scene(loader.load());

            //Pass loaded products to controller
            controller.StoreControl controller = loader.getController();
            controller.initializeWithProducts(initialProducts);

            primaryStage.setTitle("Store Management");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
