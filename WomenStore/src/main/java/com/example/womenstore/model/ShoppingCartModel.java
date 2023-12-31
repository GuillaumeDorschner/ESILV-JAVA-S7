package com.example.womenstore.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.womenstore.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.util.Objects;

public class ShoppingCartModel {

    private ObservableList<Product> products;
    private final ObservableList<Transactions> transactions;
    private final DoubleProperty totalIncomeProperty = new SimpleDoubleProperty();
    private final DoubleProperty totalOutcomeProperty = new SimpleDoubleProperty();
    private final DoubleProperty capitalProperty = new SimpleDoubleProperty();

    public ShoppingCartModel() {
        products = FXCollections.observableArrayList();
        transactions = FXCollections.observableArrayList();
        initializeProducts();

        capitalProperty.set(20000);

        totalIncomeProperty.addListener((observable, oldValue, newValue) -> updateCapital());
        totalOutcomeProperty().addListener((observable, oldValue, newValue) -> updateCapital());

        updateCapital();
    }

    private void initializeProducts() {
        fetchProductsFromDatabase();
    }

    public ObservableList<Product> getProducts() {
        return products;
    }
    public ObservableList<Transactions> getTransactions() {
        return transactions;
    }

    /*******************/

    public DoubleProperty totalIncomeProperty() {
        return totalIncomeProperty;
    }

    public double getTotalOutcome() {
        return totalOutcomeProperty.get();
    }

    public DoubleProperty totalOutcomeProperty() {
        return totalOutcomeProperty;
    }

    public DoubleProperty capitalProperty() {
        return capitalProperty;
    }

    private void updateCapital() {
        double capital = capitalProperty.get() + totalIncomeProperty.get() - totalOutcomeProperty.get();
        capitalProperty.set(capital);
    }

    /*******************/

    public Product checkID(int id){
        for(Product p : products){
            if(p.getId()==id){
                return p;
            }
        }
        return null;
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

    /********DATABSE********/

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
                    pstmt.setInt(5, ((Shoes) p).getSize());
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
                    pstmt.setInt(5, ((Shoes) p).getSize());
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
        ObservableList<Product> newProduct = FXCollections.observableArrayList();
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
        double totalSaleAmount = getTotalOutcome();
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
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fetchTransactionsFromDatabase();
    }

    private void updateProductsInDatabase() {
        for(Transactions p : transactions){
            String updateProductSql = "UPDATE Product SET nbItems = nbItems - ? WHERE id = ?";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(updateProductSql)) {

                preparedStatement.setInt(1, p.getProduct().getNbItems());
                preparedStatement.setInt(2, p.getProduct().getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void fetchTransactionsFromDatabase() {
        ObservableList<Transactions> newTransactions = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Transaction";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            newTransactions.clear();

            while (resultSet.next()) {
                String type = resultSet.getString("type");
                int product_id = resultSet.getInt("product_id"); // Ajout de la colonne product_id

                for (Product p : products){
                    if(p.getId()==product_id){
                        Transactions transaction = new Transactions(p,type ); /* Ajoutez d'autres informations du produit ici */
                        transactions.add(transaction);
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /****************/

    public void applyDiscount(Product p) {
        p.applyDiscount();
    }

    public void stopDiscount(Product p) {
        p.stopDiscount();
    }


    /********Buying and selling =>basket list********/

    public void buying(String type, String name, double price, int quantity, int size, String typeT){
        if(type.equals("Shoes")){
            Product shoes = new Shoes(name,price,quantity,size);
            shoes.purchase(quantity);

            Transactions tr = new Transactions(shoes, typeT);
            transactions.add(tr);
        }
        else if(type.equals("Clothes")){
            Product clothes = new Clothes(name,price,quantity,size);
            clothes.purchase(quantity);

            Transactions tr = new Transactions(clothes, typeT);
            transactions.add(tr);
        }
        else{
            Product accessories = new Accessories(name,price,quantity);
            accessories.purchase(quantity);

            Transactions tr = new Transactions(accessories, typeT);
            transactions.add(tr);
        }
    }

    public void selling(int id, int quantity, String transaction) {
        Iterator<Product> iterator = products.iterator();

        while (iterator.hasNext()) {
            Product p = iterator.next();

            if (p.getId() == id) {
                p.sell(quantity);//la liste product
                Transactions tr = new Transactions(p, transaction);
                tr.getProduct().setNbItems(quantity);//la liste transaction
                transactions.add(tr);
            }
        }

        sellFromDatabase();
    }


    public void confirmTransaction() {
        for (Transactions t : transactions) {
            if (Objects.equals(t.getType(), "Buy")) {
                totalOutcomeProperty.set(totalOutcomeProperty.get() + t.getProduct().getPrice()*t.getProduct().getNbItems());
                System.out.println("Buy Transaction - New Total Outcome: " + totalOutcomeProperty.get());
            }
            else {
                totalIncomeProperty.set(totalIncomeProperty.get() + t.getProduct().getPrice()*t.getProduct().getNbItems());
                System.out.println("Sell Transaction - New Total Income: " + totalIncomeProperty.get());
            }
        }
        transactions.clear();//nettoyer la liste quand c'est confirmer pour ne pas repeter
    }



}
