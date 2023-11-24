package com.example.womenstore.controller;

import com.example.womenstore.model.Product;
import com.example.womenstore.model.ShoppingCartModel;
import com.example.womenstore.view.ShoppingCartView;
import java.util.ArrayList;
import java.util.List;


public class ShoppingCartController {
    private ShoppingCartModel model;
    private ShoppingCartView view;

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

    public void addProduct() {

    }

    public void deleteProduct() {

    }

    public void modifyProduct() {

    }



}