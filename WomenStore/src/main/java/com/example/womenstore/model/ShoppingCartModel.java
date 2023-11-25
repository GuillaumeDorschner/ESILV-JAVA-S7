package com.example.womenstore.model;

import com.example.womenstore.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import com.example.womenstore.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ShoppingCartModel {

    private List<Product> products; //list of ALL items
    private List<Product> basket; //list of your shopping

    public ShoppingCartModel() {
        products = new ArrayList<>();
        basket = new ArrayList<>();
        initializeProducts();
    }

    private void initializeProducts() {
        fetchProductsFromDatabase();
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Product> getBasket() {
        return basket;
    }

    public void readDB(String f){

    }

    public double calculateTotalPrice() {
        double totalPrice = 0;
        for (Product product : basket) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }

    public Product checkID(int id){
        for(Product p : products){
            if(p.getId()==id){
                return p;
            }
        }
        return null;
    }

    public void addToBasket(Product product) {
        basket.add(product);
    }

    
    public void sellShoppingCart(){
        for(Product p : basket){
            for(Product p2 : products){
                if(p.getId() == p2.getId()){
                    p2.setNbItems(p2.getNbItems()-p.getNbItems());
                }
            }
        }
        basket.clear();
    }

    public void addToProducts(String type, String name, double price, int quantity, int size){

        if(type.equals("Shoes")){
            Product shoes = new Shoes(name,price,quantity,size);
            this.products.add(shoes);
        }
        else if(type.equals("Clothes")){
            Product clothes = new Clothes(name,price,quantity,size);
            this.products.add(clothes);
        }
        else{
            Product accessories = new Accessories(name,price,quantity);
            this.products.add(accessories);
        }

        addProductToDB(products.get(products.size()-1));
    }

    public void modifyProduct(int id, double price, int quantity){
        for(Product p : products){
            if(p.getId() == id){
                if(price != 0){
                    p.setPrice(price);
                }
                if(quantity != 0){
                    p.setNbItems(quantity);
                }
                modifyProductToDB(p);
            }
        }
    }
    
    public void removeProduct(Product p){
        products.remove(p);
        deleteProductFromDB(p);
    }

    public void addProductToDB(Product p) {

        System.out.println("Adding product to DB");
        System.out.println(p);

        String sql = "INSERT INTO Product (name, price, nbItems, category, size) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, p.getName());
            pstmt.setDouble(2, p.getPrice());
            pstmt.setInt(3, p.getNbItems());
            pstmt.setString(4, p.getCategory());

            switch (p.getCategory()) {
                case "Clothes":
                    pstmt.setInt(5, ((Clothes) p).getSize());
                    break;
                case "Shoes":
                    pstmt.setInt(5, ((Shoes) p).getShoeSize());
                    break;
                default:
                    pstmt.setNull(5, java.sql.Types.INTEGER);
            }

            pstmt.executeUpdate();
            // fetchProductsFromDatabase();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void modifyProductToDB(Product p) {

        System.out.println("Modifying product to DB");
        System.out.println(p);

        String sql = "UPDATE Product SET name = ?, price = ?, nbItems = ?, category = ?, size = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, p.getName());
            pstmt.setDouble(2, p.getPrice());
            pstmt.setInt(3, p.getNbItems());
            pstmt.setString(4, p.getCategory());

            switch (p.getCategory()) {
                case "Clothes":
                    pstmt.setInt(5, ((Clothes) p).getSize());
                    break;
                case "Shoes":
                    pstmt.setInt(5, ((Shoes) p).getShoeSize());
                    break;
                default:
                    pstmt.setNull(5, java.sql.Types.INTEGER);
            }

            pstmt.setInt(6, p.getId()); // Setting the ID for WHERE clause
            pstmt.executeUpdate();
            // fetchProductsFromDatabase();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteProductFromDB(Product p) {

        System.out.println("Deleting product from DB");
        System.out.println(p);

        String sql = "DELETE FROM Product WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, p.getId()); // Setting the ID for WHERE clause
            pstmt.executeUpdate();
            // fetchProductsFromDatabase();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void fetchProductsFromDatabase() {
        String sql = "SELECT * FROM Product";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("nbItems");
                String type = resultSet.getString("type");
                int size = resultSet.getInt("size");

                Product product = null;
                switch (type) {
                    case "Accessories":
                        product = new Accessories(name, price, quantity);
                        break;
                    case "Clothes":
                        product = new Clothes(name, price, quantity, size);
                        break;
                    case "Shoes":
                        product = new Shoes(name, price, quantity, size);
                        break;
                    default:

                }
                products.add(product);

            }
            } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
