package com.example.womenstore.model;

import com.example.womenstore.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import com.example.womenstore.model.*;


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
    private List<Transactions> transactions; //list of all transactions

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
        sellFromDatabase();
        // reload fonction total incom
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

    public void modifyProduct(int id, double price, int quantity) {
        List<Product> productsToModify = new ArrayList<>();

        for (Product p : products) {
            if (p.getId() == id) {
                productsToModify.add(p);
            }
        }

        for (Product p : productsToModify) {
            if (price != 0) {
                p.setPrice(price);
            }
            if (quantity != 0) {
                p.setNbItems(quantity);
            }
            modifyProductToDB(p);
        }
    }

    
    public void removeProduct(Product p){
        products.remove(p);
        deleteProductFromDB(p);
    }

    public void addProductToDB(Product p) {

        System.out.println("Adding product to DB");
        System.out.println(p);

        String sql = "INSERT INTO Product (name, price, nbItems, type, size) VALUES (?, ?, ?, ?, ?)";
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
            fetchProductsFromDatabase();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void modifyProductToDB(Product p) {

        System.out.println("Modifying product to DB");
        System.out.println(p);

        String sql = "UPDATE Product SET name = ?, price = ?, nbItems = ?, type = ?, size = ? WHERE id = ?";
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

            pstmt.setInt(6, p.getId());
            pstmt.executeUpdate();
            fetchProductsFromDatabase();
            
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

            pstmt.setInt(1, p.getId());
            pstmt.executeUpdate();
            fetchProductsFromDatabase();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void fetchProductsFromDatabase() {
        List<Product> newProduct = new ArrayList<>();
        String sql = "SELECT * FROM Product";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            products.clear();

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
                        product = new Accessories(id, name, price, quantity);
                        break;
                    case "Clothes":
                        product = new Clothes(id, name, price, quantity, size);
                        break;
                    case "Shoes":
                        product = new Shoes(id, name, price, quantity, size);
                        break;
                    default:

                }

                newProduct.add(product);
            }
            } catch (SQLException e) {
            e.printStackTrace();
        }
        products = newProduct;
    }

    
    public void sellFromDatabase() {
        double totalSaleAmount = calculateTotalPrice();
        if (totalSaleAmount == 0) return;

        String transactionSql = "INSERT INTO Transaction (total) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(transactionSql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setDouble(1, totalSaleAmount);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating transaction failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int transactionId = generatedKeys.getInt(1);
                    updateProductsInDatabase();
                    basket.clear();
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fecthTransactionsFromDatabase();
    }

    private void updateProductsInDatabase() {
        for(Product p : basket){
            String updateProductSql = "UPDATE Product SET nbItems = nbItems - ? WHERE id = ?";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(updateProductSql)) {

                preparedStatement.setInt(1, p.getNbItems());
                preparedStatement.setInt(2, p.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void fecthTransactionsFromDatabase() {
        List<Transactions> newTransactions = new ArrayList<>();
        String sql = "SELECT * FROM Transaction";

        try (Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {

            transactions.clear();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Timestamp time = resultSet.getTimestamp("time");
                double total = resultSet.getDouble("total");

                Transactions transaction = new Transactions(id, time, total);
                newTransactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        transactions = newTransactions;
    }

    
}
