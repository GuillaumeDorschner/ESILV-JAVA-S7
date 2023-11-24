package com.example.womenstore;

import com.example.womenstore.model.ShoppingCartModel;
import com.example.womenstore.view.ShoppingCartView;
import com.example.womenstore.controller.ShoppingCartController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ShopApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Initialize model, view, and controller
        ShoppingCartModel model = new ShoppingCartModel();
        ShoppingCartView view = new ShoppingCartView(primaryStage);
        ShoppingCartController controller = new ShoppingCartController(model, view);

    }

    public static void main(String[] args) {
        launch(args);
    }
}