package com.example.womenstore.view;

import com.example.womenstore.controller.*;
import com.example.womenstore.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class ShoppingCartView {

    private final TextInputDialog textInputDialog = new TextInputDialog();
    private final ChoiceDialog<String> choiceDialog = new ChoiceDialog<>();
    private Stage stage;
    private ListView<HBox> productListView;
    private ListView<HBox> basketListView;

    public ShoppingCartView(Stage stage) {
        this.stage = stage;
        productListView = new ListView<>();
        basketListView = new ListView<>();

        textInputDialog.setHeaderText(null);
        choiceDialog.setHeaderText(null);
    }

    public void show(ShoppingCartController controller) {
        stage.setTitle("Management of the Shopping Cart App");

        BorderPane mainLayout = new BorderPane();

        mainLayout.setTop(createMenuBar());
        mainLayout.setLeft(createProductCategoryButtons(controller));

        // Create a VBox to stack the action buttons above the product list
        VBox centerLayout = new VBox();
        centerLayout.setAlignment(Pos.TOP_CENTER);
        centerLayout.getChildren().addAll(createProductActionButtons(controller), createProductListView());

        mainLayout.setCenter(centerLayout);

        VBox rightLayout = new VBox();
        rightLayout.getChildren().addAll(createBasketView(controller), createBasketListView(), buttonConfirm(controller));
        mainLayout.setRight(rightLayout);

        Scene scene = new Scene(mainLayout, 900, 500);
        stage.setScene(scene);
        stage.show();
    }

    /******************************/

    private MenuBar createMenuBar() {

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(e -> stage.close());
        fileMenu.getItems().add(exitMenuItem);
        menuBar.getMenus().add(fileMenu);

        return menuBar;
    }

    /**************Buttons for product categories****************/

    private VBox createProductCategoryButtons(ShoppingCartController controller) {
        VBox productNavigation = new VBox();

        Button clothesButton = createProductCategoryButton("Clothes", controller);
        Button shoesButton = createProductCategoryButton("Shoes", controller);
        Button accessoriesButton = createProductCategoryButton("Accessories", controller);

        // Add buttons to the VBox
        productNavigation.getChildren().addAll(clothesButton, shoesButton, accessoriesButton);



        return productNavigation;
    }


    private Button createProductCategoryButton(String category, ShoppingCartController controller) {
        Button button = new Button(category);
        button.setMinWidth(120); // Set a fixed width for all buttons
        VBox.setMargin(button, new Insets(50, 10, 0, 10)); // Add left and right margin

        // Set action for the button
        button.setOnAction(event -> controller.showProductCategory(category));

        return button;
    }

    public void updateProductListView(List<Product> productDetails, ShoppingCartController controller) {
        ObservableList<HBox> productListWithDiscountButtons = FXCollections.observableArrayList();

        for (Product productDetail : productDetails) {
            HBox productWithDiscountButton = new HBox();

            // Create labels for each attribute
            Label idLabel = new Label("ID: " + productDetail.getId() + " | ");
            Label nameLabel = new Label("Name: " + productDetail.getName() + " | ");
            Label sizeLabel=new Label();

            if(productDetail instanceof Shoes){
                Shoes shoes = (Shoes)productDetail;
                sizeLabel = new Label("Size: " + shoes.getSize() + " | ");
            }
            if(productDetail instanceof Clothes){
                Clothes clothes = (Clothes)productDetail;
                sizeLabel = new Label("Size: " + clothes.getSize() + " | ");
            }

            Label priceLabel = new Label("Price: ");
            Label nbItemsLabel = new Label(" | Available Items: " + productDetail.getNbItems() + " | ");

            // Bind the price label to the product's priceProperty
            Label priceValueLabel = new Label();
            priceValueLabel.textProperty().bind(productDetail.priceProperty().asString("%.2f"));

            ToggleButton discountToggleButton = new ToggleButton("Apply Discount");

            // Add action event for the ToggleButton
            discountToggleButton.setOnAction(event -> {
                boolean applyDiscount = discountToggleButton.isSelected();
                if (applyDiscount) {
                    controller.applyDiscount(productDetail);
                } else {
                    controller.stopDiscount(productDetail);
                }
            });

            if (productDetails instanceof Accessories) {
                productWithDiscountButton.getChildren().addAll(idLabel, nameLabel, priceLabel, priceValueLabel, nbItemsLabel, discountToggleButton);
            }
            else {
                productWithDiscountButton.getChildren().addAll(idLabel, nameLabel, sizeLabel, priceLabel, priceValueLabel, nbItemsLabel, discountToggleButton);
            }


            productListWithDiscountButtons.add(productWithDiscountButton);
        }

        productListView.getItems().setAll(productListWithDiscountButtons);
    }

    private ListView<HBox> createProductListView() {
        productListView.setPrefWidth(200);
        return productListView;
    }

    /**************Choose your actions****************/

    private HBox createProductActionButtons(ShoppingCartController controller) {
        HBox categoryButtons = new HBox(10); // Horizontal layout for buttons with spacing
        categoryButtons.setAlignment(Pos.CENTER);

        Button addButton = createProductActionButton("Add", controller);
        Button deleteButton = createProductActionButton("Delete", controller);
        Button modifyButton = createProductActionButton("Modify", controller);

        categoryButtons.getChildren().addAll(addButton, deleteButton, modifyButton);

        return categoryButtons;
    }

    private Button createProductActionButton(String action, ShoppingCartController controller) {
        Button button = new Button(action);
        button.setMinWidth(100); // Set a fixed size for all buttons
        HBox.setMargin(button, new Insets(10, 0, 10, 0));

        // Set action for the button based on the action type
        switch (action) {
            case "Add":
                button.setOnAction(event -> controller.addProduct());
                break;
            case "Delete":
                button.setOnAction(event -> controller.deleteProduct());
                break;
            case "Modify":
                button.setOnAction(event -> controller.modifyProduct());
                break;
            default:
                break;
        }

        return button;
    }

    /*************Add product*****************/

    public String askForProductType() {
        List<String> productTypes = List.of("Clothes", "Shoes", "Accessories");

        choiceDialog.setTitle("Select Product Type");
        choiceDialog.setContentText("Choose the type of product to add:");
        choiceDialog.getItems().setAll(productTypes);

        Optional<String> result = choiceDialog.showAndWait();
        return result.orElse(null);
    }

    public Map<String, String> askForProductDetails(String productType) {
        Map<String, String> details = new HashMap<>();

        details.put("Type", productType);

        textInputDialog.setTitle("Enter " + productType + " Details");

        textInputDialog.setContentText("Enter " + productType + " Name:");
        Optional<String> nameResult = textInputDialog.showAndWait();
        if (nameResult.isEmpty()) {
            return null;
        }
        details.put("Name", nameResult.get());


        // Additional details for Shoes and Clothes
        if ("Shoes".equals(productType) || "Clothes".equals(productType)) {
            textInputDialog.setContentText("Enter " + productType + " Size:");
            Optional<String> sizeResult = textInputDialog.showAndWait();
            if (sizeResult.isEmpty() || !isValidNumericInput(sizeResult.get())) {
                // Handle invalid input, show a message, and ask the user to correct it
                return null;
            }
            if("Clothes".equals(productType) && (Integer.parseInt(sizeResult.get()) < 36 || Integer.parseInt(sizeResult.get()) > 50)){
                showAlert("Canceled", "Size is under 36 or above 50.", Alert.AlertType.WARNING);
                return null;
            }
            details.put("Size", String.valueOf(Integer.parseInt(sizeResult.get()) ) );
        }

        textInputDialog.setContentText("Enter " + productType + " Price:");
        Optional<String> priceResult = textInputDialog.showAndWait();
        if (priceResult.isEmpty() || !isValidNumericInput(priceResult.get())) {
            return null;
        }
        details.put("Price", String.valueOf(Double.parseDouble(priceResult.get())));

        textInputDialog.setContentText("Enter " + productType + " Quantity:");
        Optional<String> quantityResult = textInputDialog.showAndWait();
        if (quantityResult.isEmpty() || !isValidNumericInput(quantityResult.get())) {
            // Handle invalid input, show a message, and ask the user to correct it
            return null;
        }
        details.put("Quantity", String.valueOf(Integer.parseInt(quantityResult.get())));

        return details;
    }


    /*************Find product*****************/
    public int askForProductID(ShoppingCartController controller){
        textInputDialog.setTitle("Check Product");
        textInputDialog.setContentText("Enter ID Product:");

        Optional<String> idResult = textInputDialog.showAndWait();

        if (idResult.isEmpty() || !isValidNumericInput(idResult.get()) || controller.checkIdProduct(Integer.parseInt(idResult.get()) ) ==null) {
            showAlert("Invalid Input", "Please enter a valid ID for the product.", Alert.AlertType.ERROR);
            return 0; // Default value in case of invalid input
        }

        return Integer.parseInt(idResult.get());
    }

    /*************Modify product =>Price & quantity*****************/

    public Map<String, String> askForModificationDetails() {
        // Create a map to store the modified values
        Map<String, String> details = new HashMap<>();

        textInputDialog.setTitle("Product Modification");
        textInputDialog.setHeaderText(null);

        // Prompt the user for the new quantity
        textInputDialog.setContentText("Enter new quantity:");
        Optional<String> quantityResult = textInputDialog.showAndWait();
        if (!isValidNumericInput(quantityResult.get())) {
            showAlert("Invalid Input", "Please enter a valid quantity.", Alert.AlertType.ERROR);
            return null; // User canceled the modification or provided invalid input
        }
        details.put("Quantity", quantityResult.get());

        // Prompt the user for the new price
        textInputDialog.setContentText("Enter new price:");
        Optional<String> priceResult = textInputDialog.showAndWait();
        if (!isValidNumericInput(priceResult.get())) {
            showAlert("Invalid Input", "Please enter a valid price.", Alert.AlertType.ERROR);
            return null; // User canceled the modification or provided invalid input
        }
        details.put("Price", priceResult.get());

        return details;
    }

    /******************************/

    private boolean isValidNumericInput(String input) {
        // Use a suitable validation method based on your requirements
        try {
            // Try parsing as double first
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            try {
                // If parsing as double fails, try parsing as int
                Integer.parseInt(input);
                return true;
            } catch (NumberFormatException ex) {
                // Both parsing attempts failed, input is not a valid numeric value
                return false;
            }
        }
    }

    public void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /*************Buying/selling*****************/

    private VBox createBasketView(ShoppingCartController controller) {
        // Labels to display total income and total outcome
        Label totalIncomeLabel = new Label();
        Label totalOutcomeLabel = new Label();

        totalIncomeLabel.textProperty().bind(controller.getModel().totalIncomeProperty().asString("Total Income: $%.2f"));
        totalOutcomeLabel.textProperty().bind(controller.getModel().totalOutcomeProperty().asString("Total Outcome: $%.2f"));

        Button buyButton = new Button("Buy Product");
        Button sellButton = new Button("Sell Product");

        HBox labelHBox = new HBox(20);
        labelHBox.getChildren().addAll(totalOutcomeLabel, totalIncomeLabel);

        HBox buttonsHBox = new HBox(30);
        buttonsHBox.getChildren().addAll(buyButton, sellButton);

        buyButton.setMinWidth(100); // Set a fixed size for all buttons
        sellButton.setMinWidth(100); // Set a fixed size for all buttons

        buyButton.setOnAction(event -> controller.buyProduct());
        //sellButton.setOnAction(event -> handleSellButtonClick(controller));

        ListView<String> listView = new ListView<>();

        // Set margins for the HBoxes
        Insets labelMargins = new Insets(20, 10, 10, 10);
        Insets buttonsMargins = new Insets(0, 10, 10, 10);

        VBox.setMargin(labelHBox, labelMargins);
        VBox.setMargin(buttonsHBox, buttonsMargins);

        // Ajouter le titre "Transactions"
        Label titleLabel = new Label("Transactions");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        VBox.setMargin(titleLabel, new Insets(10, 0, 0, 0)); // Marge en bas du titre

        VBox basketView = new VBox();

        basketView.setAlignment(Pos.CENTER);
        basketView.getChildren().addAll(titleLabel,labelHBox, buttonsHBox);

        return basketView;
    }

    public void updateListTransaction(List<Product> listTransactions, ShoppingCartController controller) {
        ObservableList<HBox> transactionListWithButtons = FXCollections.observableArrayList();

        for (Product transaction : listTransactions) {
            HBox transactionWithButton = new HBox();

            Label nameLabel = new Label("Name: " + transaction.getName() + " | ");
            Label sizeLabel = new Label();
            if (transaction instanceof Shoes) {
                Shoes shoes = (Shoes) transaction;
                sizeLabel = new Label("Size: " + shoes.getSize() + " | ");
            } else if (transaction instanceof Clothes) {
                Clothes clothes = (Clothes) transaction;
                sizeLabel = new Label("Size: " + clothes.getSize() + " | ");
            }

            Label priceLabel = new Label("Price: " + transaction.getPrice() +" | ");
            Label quantityLabel = new Label("Quantity: " + transaction.getNbItems() + " | ");

            transactionWithButton.getChildren().addAll(nameLabel, sizeLabel, priceLabel, quantityLabel);
            transactionListWithButtons.add(transactionWithButton);
        }
        basketListView.getItems().setAll(transactionListWithButtons);
    }

    private ListView<HBox> createBasketListView(){
        VBox.setMargin(basketListView, new Insets(0, 10, 0, 10));
        basketListView.setPrefWidth(200);
        return basketListView;
    }

    private VBox buttonConfirm(ShoppingCartController controller) {

        VBox confirmButtonBox = new VBox();
        VBox.setMargin(confirmButtonBox, new Insets(10,10,10,10));
        Button confirmButton = new Button("Confirm transaction");
        confirmButton.setOnAction(event -> {
            // Handle confirming all transactions
        });
        confirmButtonBox.setAlignment(Pos.CENTER);
        confirmButtonBox.getChildren().addAll(confirmButton);
        return confirmButtonBox;
    }


    /****************************/
    public Map<String, String> handleBuyButtonClick(String productType) {
        Map<String, String> details = new HashMap<>();

        details.put("Type", productType);

        textInputDialog.setTitle("Enter " + productType + " Details");

        textInputDialog.setContentText("Enter " + productType + " Name:");
        Optional<String> nameResult = textInputDialog.showAndWait();
        if (nameResult.isEmpty()) {
            return null;
        }
        details.put("Name", nameResult.get());


        // Additional details for Shoes and Clothes
        if ("Shoes".equals(productType) || "Clothes".equals(productType)) {
            textInputDialog.setContentText("Enter " + productType + " Size:");
            Optional<String> sizeResult = textInputDialog.showAndWait();
            if (sizeResult.isEmpty() || !isValidNumericInput(sizeResult.get())) {
                // Handle invalid input, show a message, and ask the user to correct it
                return null;
            }
            if("Clothes".equals(productType) && (Integer.parseInt(sizeResult.get()) < 36 || Integer.parseInt(sizeResult.get()) > 50)){
                showAlert("Canceled", "Size is under 36 or above 50.", Alert.AlertType.WARNING);
                return null;
            }
            details.put("Size", String.valueOf(Integer.parseInt(sizeResult.get()) ) );
        }

        textInputDialog.setContentText("Enter " + productType + " Price:");
        Optional<String> priceResult = textInputDialog.showAndWait();
        if (priceResult.isEmpty() || !isValidNumericInput(priceResult.get())) {
            return null;
        }
        details.put("Price", String.valueOf(Double.parseDouble(priceResult.get())));

        textInputDialog.setContentText("Enter " + productType + " Quantity:");
        Optional<String> quantityResult = textInputDialog.showAndWait();
        if (quantityResult.isEmpty() || !isValidNumericInput(quantityResult.get())) {
            // Handle invalid input, show a message, and ask the user to correct it
            return null;
        }
        details.put("Quantity", String.valueOf(Integer.parseInt(quantityResult.get())));

        return details;
    }



}