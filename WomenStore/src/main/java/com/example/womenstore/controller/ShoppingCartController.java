package com.example.womenstore.controller;

import com.example.womenstore.model.*;
import com.example.womenstore.view.ShoppingCartView;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import java.util.Map;


public class ShoppingCartController {
    private final ShoppingCartModel model;
    private final ShoppingCartView view;

    public ShoppingCartController(ShoppingCartModel model, ShoppingCartView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        view.show(this);
    }

    public void showProductCategory( String category ) {
        List<String> productDetails = new ArrayList<>();
        for (Product product : model.getProducts()) {
            if (product.getCategory().equals(category)) {
                productDetails.add(product.toString());
            }
        }
        view.updateProductListView(productDetails);
    }

    /********************************/
    public void addProduct() {
        String productType = view.askForProductType();

        // Step 2: Ask for product details based on the selected product type
        if (productType != null) {
            Map<String, String> productDetails = view.askForProductDetails(productType);

            // Step 3: Handle the returned product details
            if (productDetails != null) {
                // Add the product to the shopping cart or perform other actions
                System.out.println("Product details: " + productDetails);
                // Display a success message
                view.showAlert("Success", "Product added successfully!", Alert.AlertType.INFORMATION);

                String name = productDetails.get("Name");
                double price = Double.parseDouble(productDetails.get("Price"));
                int quantity = Integer.parseInt(productDetails.get("Quantity"));
                String type = productDetails.get("Type");
                int size = Integer.parseInt(productDetails.get("Size"));

                model.addToProducts(type,name,price,quantity,size);
                showProductCategory(type);
            } else {
                // Display a message indicating that the user canceled the input
                view.showAlert("Canceled", "Product addition canceled.", Alert.AlertType.WARNING);
            }
        }
    }

    /********************************/

    public Product checkIdProduct(int id){
        return model.checkID(id);
    }

    public void deleteProduct() {
        int id = view.askForProductID(this);
        model.removeProduct(checkIdProduct(id));
        showProductCategory(checkIdProduct(id).getCategory());
    }

    /********************************/

    public void modifyProduct() {
        int id = view.askForProductID(this);

        Map<String, String> productNewDetails = view.askForModificationDetails();

        if(productNewDetails!=null){
            double price = Double.parseDouble(productNewDetails.get("Price"));
            int quantity = Integer.parseInt(productNewDetails.get("Quantity"));
            model.modifyProduct(id,price,quantity);
            showProductCategory(checkIdProduct(id).getCategory());
        }
    }
}