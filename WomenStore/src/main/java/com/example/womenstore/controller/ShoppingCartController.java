package com.example.womenstore.controller;

import com.example.womenstore.model.*;
import com.example.womenstore.view.ShoppingCartView;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import java.util.Map;


public class ShoppingCartController{
    private final ShoppingCartModel model;
    private final ShoppingCartView view;

    public ShoppingCartController(ShoppingCartModel model, ShoppingCartView view) {
        this.model = model;
        this.view = view;
        initController();
    }


    public ShoppingCartModel getModel(){
        return model;
    }

    public ShoppingCartView getView(){
        return view;
    }

    private void initController() {
        view.show(this);
    }

    public void showProductCategory( String category ) {
        List<Product> productDetails = new ArrayList<>();
        for (Product product : model.getProducts()) {
            if (product.getCategory().equals(category)) {
                productDetails.add(product);
            }
        }
        view.updateProductListView(productDetails, this);
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
                int size;
                if(productDetails.get("Size")==null){
                    size=0;
                }
                else{
                    size = Integer.parseInt(productDetails.get("Size"));
                }

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

    /********************************/

    public void deleteProduct() {
        int id = view.askForProductID(this);
        String category = checkIdProduct(id).getCategory();
        model.removeProduct(checkIdProduct(id));
        showProductCategory(category);
        view.showAlert("Success", "Product deleted successfully!", Alert.AlertType.INFORMATION);

    }

    /********************************/

    public void modifyProduct() {
        int id = view.askForProductID(this);

        Map<String, String> productNewDetails = view.askForModificationDetails(this);

        if(productNewDetails!=null){
            double price = Double.parseDouble(productNewDetails.get("Price"));
            int quantity = Integer.parseInt(productNewDetails.get("Quantity"));
            model.modifyProduct(id,price,quantity);
            showProductCategory(checkIdProduct(id).getCategory());
            view.showAlert("Success", "Product modification successfully!", Alert.AlertType.INFORMATION);
        }
        else{
            view.showAlert("Canceled", "Product modification canceled.", Alert.AlertType.WARNING);
        }
    }

    /***************DISCOUNT APPLICATION*****************/

    public void applyDiscount(Product p) {
        model.applyDiscount(p);
    }

    public void stopDiscount(Product p){
        model.stopDiscount(p);
    }

    /********************************/

    public void showProductTransaction() {
        view.updateListTransaction(model.getTransactions(),this);
    }

    public void buyProduct(){
        String productType = view.askForProductType();

        if (productType != null) {
            Map<String, String> productDetails = view.handleBuyButtonClick(productType);

            if (productDetails != null) {

                System.out.println("Product details: " + productDetails);
                view.showAlert("Success", "Product Transaction successfully!", Alert.AlertType.INFORMATION);

                String name = productDetails.get("Name");
                double price = Double.parseDouble(productDetails.get("Price"));
                int quantity = Integer.parseInt(productDetails.get("Quantity"));
                String type = productDetails.get("Type");
                String transaction = productDetails.get("Transaction");
                int size;
                if(productDetails.get("Size")==null){
                    size=0;
                }
                else{
                    size = Integer.parseInt(productDetails.get("Size"));
                }

                model.buying(type,name,price,quantity,size,transaction);
                showProductTransaction();

            } else {
                view.showAlert("Canceled", "Product Transaction canceled.", Alert.AlertType.WARNING);
            }
        }
    }

    public void sellProduct(){
        Map<String, String> details = view.handleSellButtonClick(this);
        if (details != null) {

            int id = Integer.parseInt(details.get("ID"));
            int quantity = Integer.parseInt(details.get("Quantity"));
            String transaction = details.get("Transaction");

            if(checkIdProduct(id).getNbItems()>=quantity){
                model.selling(id,quantity,transaction);

                showProductTransaction();
                showProductCategory(checkIdProduct(id).getCategory());

                view.showAlert("Success", "Product Transaction successfully!", Alert.AlertType.INFORMATION);
            }
            else{
                view.showAlert("Canceled", "Product quantity not enough.", Alert.AlertType.WARNING);
            }


        } else {
            view.showAlert("Canceled", "Product Transaction canceled.", Alert.AlertType.WARNING);
        }
    }
}