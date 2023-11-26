package com.example.womenstore.view;

import com.example.womenstore.controller.*;
import com.example.womenstore.model.Product;
import com.example.womenstore.model.ShoppingCartModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;
import java.util.List;


public class ShoppingCartView {

    private final TextInputDialog textInputDialog = new TextInputDialog();
    private final ChoiceDialog<String> choiceDialog = new ChoiceDialog<>();
    private Stage stage;
    private ListView<HBox> productListView;
    private VBox basketView;
    private Label totalPriceLabel;

    public ShoppingCartView(Stage stage) {
        this.stage = stage;
        productListView = new ListView<>();
        totalPriceLabel = new Label("Total Price: $0.0");

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

        // Add the action buttons
        centerLayout.getChildren().addAll(createProductActionButtons(controller), createProductListView());

        mainLayout.setCenter(centerLayout);


        mainLayout.setRight(createBasketView(controller));

        Scene scene = new Scene(mainLayout, 800, 600);
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

        productNavigation.getChildren().addAll(clothesButton, shoesButton, accessoriesButton);

        return productNavigation;
    }

    private Button createProductCategoryButton(String category, ShoppingCartController controller) {
        Button button = new Button(category);
        button.setMinWidth(120); // Set a fixed width for all buttons
        VBox.setMargin(button, new Insets(10, 10, 0, 10)); // Add left and right margin

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
            Label nameLabel = new Label("Name: " + productDetail.getName()+ " | ");
            Label priceLabel = new Label("Price: ");
            Label nbItemsLabel = new Label(" | Available Items: " + productDetail.getNbItems()+ " | ");

            // Bind the price label to the product's priceProperty
            Label priceValueLabel = new Label();
            priceValueLabel.textProperty().bind(productDetail.priceProperty().asString("%.2f"));

            // Add the Apply Discount button
            Button discountButton = new Button("Apply Discount");
            Button stopDiscountButton = new Button("Stop Discount");

            // Add action event for Apply Discount button
            discountButton.setOnAction(event -> {
                controller.applyDiscount(productDetail);
            });

            // Add action event for Stop Discount button
            stopDiscountButton.setOnAction(event -> {
                controller.stopDiscount(productDetail);
            });

            productWithDiscountButton.getChildren().addAll(idLabel, nameLabel, priceLabel, priceValueLabel, nbItemsLabel, discountButton, stopDiscountButton);

            // Add the product with discount button to the list
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
        HBox.setMargin(button, new Insets(10, 0, 0, 10));

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
        Button checkout = new Button("Checkout");
        Label basket= new Label("Basket");

        VBox basketView = new VBox(basket, checkout); // Example button for checkout

        checkout.setMinWidth(100);
        basketView.setMinWidth(120);

        basketView.setAlignment(Pos.TOP_CENTER);

        return basketView;
    }

    public void updateTotalPriceLabel(double totalPrice) {
        totalPriceLabel.setText("Total Price: $" + totalPrice);
    }

}