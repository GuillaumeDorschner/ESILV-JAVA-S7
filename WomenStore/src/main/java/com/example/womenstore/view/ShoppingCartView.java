package com.example.womenstore.view;

import com.example.womenstore.controller.*;
import com.example.womenstore.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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

        // Combine left and center into a single VBox
        VBox leftAndCenterLayout = new VBox();
        leftAndCenterLayout.setAlignment(Pos.TOP_CENTER);
        leftAndCenterLayout.getChildren().addAll(createProductCategoryButtons(controller), createProductActionButtons(controller), createProductListView());

        mainLayout.setCenter(leftAndCenterLayout);

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

    private ComboBox<String> createProductCategoryButtons(ShoppingCartController controller) {
        ComboBox<String> productTypeComboBox = new ComboBox<>();
        productTypeComboBox.getItems().addAll("Clothes", "Shoes", "Accessories");
        productTypeComboBox.setPromptText("Select Product Type");

        // Set action for the ComboBox
        productTypeComboBox.setOnAction(event -> controller.showProductCategory(productTypeComboBox.getValue()));

        return productTypeComboBox;
    }

    public void updateProductListView(List<Product> productDetails, ShoppingCartController controller) {
        ObservableList<HBox> productListWithDiscountButtons = FXCollections.observableArrayList();

        for (Product productDetail : productDetails) {
            HBox productWithDiscountButton = new HBox();

            // Create labels for each attribute
            Label idLabel = new Label("ID: " + productDetail.getId() + " | ");
            Label nameLabel = new Label("Name: " + productDetail.getName() + " | ");
            Label sizeLabel=new Label();

            if(productDetail instanceof Shoes shoes){
                sizeLabel = new Label("Size: " + shoes.getSize() + " | ");
            }
            if(productDetail instanceof Clothes clothes){
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
        VBox.setMargin(productListView, new Insets(0, 10, 10, 10));
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

        // Create a custom dialog
        Dialog<Map<String, String>> buyDialog = new Dialog<>();
        buyDialog.setTitle("Restock " + productType);
        buyDialog.setHeaderText("Enter " + productType + " details for purchase:");

        // Set the button types
        ButtonType buyButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        buyDialog.getDialogPane().getButtonTypes().addAll(buyButtonType, ButtonType.CANCEL);

        // Create the form GridPane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Add form fields
        TextField nameField = new TextField();
        nameField.setPromptText(productType + " Name");

        TextField sizeField = new TextField();
        sizeField.setPromptText(productType + " Size");

        TextField priceField = new TextField();
        priceField.setPromptText(productType + " Price");

        TextField quantityField = new TextField();
        quantityField.setPromptText(productType + " Quantity");

        grid.add(new Label(productType + " Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        if ("Shoes".equals(productType) || "Clothes".equals(productType)) {
            grid.add(new Label(productType + " Size:"), 0, 1);
            grid.add(sizeField, 1, 1);
        }

        grid.add(new Label(productType + " Price:"), 0, 2);
        grid.add(priceField, 1, 2);

        grid.add(new Label(productType + " Quantity:"), 0, 3);
        grid.add(quantityField, 1, 3);

        // Enable/Disable button depending on whether all fields are filled
        Node buyButton = buyDialog.getDialogPane().lookupButton(buyButtonType);
        buyButton.setDisable(true);

        // Do some validation
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            buyButton.setDisable(newValue.trim().isEmpty());
        });

        buyDialog.getDialogPane().setContent(grid);

        // Convert the result to a map when the buy button is clicked
        buyDialog.setResultConverter(dialogButton -> {
            if (dialogButton == buyButtonType) {
                details.put("Type", productType);
                details.put("Name", nameField.getText());

                if ("Shoes".equals(productType) || "Clothes".equals(productType)) {
                    String sizeInput = sizeField.getText();
                    if (!isValidNumericInput(sizeInput)) {
                        showAlert("Invalid Input", "Please enter a valid numeric size.", Alert.AlertType.WARNING);
                        return null; // Stop processing if size is invalid
                    }

                    int size = Integer.parseInt(sizeInput);
                    if ("Clothes".equals(productType) && (size < 36 || size > 50)) {
                        showAlert("Invalid Size", "Size must be between 36 and 50 for Clothes.", Alert.AlertType.WARNING);
                        return null; // Stop processing if size is out of range
                    }

                    details.put("Size", String.valueOf(size));
                }

                details.put("Price", priceField.getText());
                details.put("Quantity", quantityField.getText());
            }
            return details;
        });

        // Show the dialog and wait for the user's input
        Optional<Map<String, String>> result = buyDialog.showAndWait();

        return result.orElse(new HashMap<>());
    }

    /*************Find product*****************/
    public int askForProductID(ShoppingCartController controller) {
        // Create a dialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Check Product");
        dialog.setContentText("Enter ID Product:");

        // Show the dialog and wait for the user's response
        Optional<String> idResult = dialog.showAndWait();

        if (idResult.isEmpty() || !isValidNumericInput(idResult.get()) || controller.checkIdProduct(Integer.parseInt(idResult.get())) == null) {
            return 0; // Default value in case of invalid input
        }

        return Integer.parseInt(idResult.get());
    }

    /*************Modify product =>Price & quantity*****************/

    public Map<String, String> askForModificationDetails(ShoppingCartController controller) {
        Map<String, String> details = new HashMap<>();

        // Ask for the product ID
        int productId = askForProductID(controller);
        if (productId == 0) {
            // User canceled or provided an invalid ID
            return null;
        }

        // Create a dialog
        Dialog<Map<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Product Modification");
        dialog.setHeaderText(null);

        // Set the button types
        ButtonType confirmButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // Create the layout and add components
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField quantityField = new TextField();
        TextField priceField = new TextField();

        grid.add(new Label("Quantity:"), 0, 0);
        grid.add(quantityField, 1, 0);

        grid.add(new Label("Price:"), 0, 1);
        grid.add(priceField, 1, 1);

        // Enable/Disable OK button depending on whether a quantity and price are entered
        Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

        // Validation logic
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            confirmButton.setDisable(!isValidNumericInput(newValue) || priceField.getText().trim().isEmpty());
        });

        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            confirmButton.setDisable(newValue.trim().isEmpty() || !isValidNumericInput(newValue) || quantityField.getText().trim().isEmpty());
        });

        // Set the dialog content
        dialog.getDialogPane().setContent(grid);

        // Request focus on the quantity field by default
        Platform.runLater(quantityField::requestFocus);

        // Convert the result to a key-value pair when the OK button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                details.put("Product ID", String.valueOf(productId));
                details.put("Quantity", quantityField.getText());
                details.put("Price", priceField.getText());
                return details;
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<Map<String, String>> result = dialog.showAndWait();

        return result.orElse(null);
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
        Label capitalLabel = new Label();
        Label totalIncomeLabel = new Label();
        Label totalOutcomeLabel = new Label();

        capitalLabel.textProperty().bind(controller.getModel().capitalProperty().asString("Capital: $%.2f"));
        totalIncomeLabel.textProperty().bind(controller.getModel().totalIncomeProperty().asString("Total Income: $%.2f"));
        totalOutcomeLabel.textProperty().bind(controller.getModel().totalOutcomeProperty().asString("Total Outcome: $%.2f"));

        Button buyButton = new Button("Buy Product");
        Button sellButton = new Button("Sell Product");

        HBox labelHBox = new HBox(20);
        labelHBox.getChildren().addAll(totalOutcomeLabel, totalIncomeLabel, capitalLabel);

        HBox buttonsHBox = new HBox(30);
        buttonsHBox.getChildren().addAll(buyButton, sellButton);

        buyButton.setMinWidth(100); // Set a fixed size for all buttons
        sellButton.setMinWidth(100); // Set a fixed size for all buttons

        buyButton.setOnAction(event -> controller.buyProduct());
        sellButton.setOnAction(event -> controller.sellProduct());

        ListView<String> listView = new ListView<>();

        // Set margins for the HBoxes
        Insets labelMargins = new Insets(20, 10, 10, 10);
        Insets buttonsMargins = new Insets(0, 10, 10, 10);

        VBox.setMargin(labelHBox, labelMargins);
        VBox.setMargin(buttonsHBox, buttonsMargins);

        // Ajouter le titre "Transactions"
        Label titleLabel = new Label("Basket");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        VBox.setMargin(titleLabel, new Insets(10, 0, 0, 0)); // Marge en bas du titre

        VBox basketView = new VBox();

        basketView.setAlignment(Pos.CENTER);
        basketView.getChildren().addAll(titleLabel,labelHBox, buttonsHBox);

        return basketView;
    }

    public void updateListTransaction(List<Transactions> listTransactions, ShoppingCartController controller) {
        ObservableList<HBox> transactionListWithButtons = FXCollections.observableArrayList();

        for (Transactions transaction : listTransactions) {
            HBox transactionWithButton = new HBox();
            Label typeLabel = new Label("Type: " + transaction.getType() + " | ");
            Label nameLabel = new Label("Name: " + transaction.getProduct().getName() + " | ");
            Label priceLabel = new Label("Price: " + transaction.getProduct().getPrice() +"$ | ");
            Label quantityLabel = new Label("Quantity: " + transaction.getProduct().getNbItems() + " | ");
            Label sizeLabel = new Label();

            if(transaction.getProduct() instanceof Shoes shoes){
                sizeLabel = new Label("Size: " + shoes.getSize() + " | ");
            }
            if(transaction.getProduct() instanceof Clothes clothes){
                sizeLabel = new Label("Size: " + clothes.getSize() + " | ");
            }

            if (transaction.getProduct() instanceof Accessories) {
                transactionWithButton.getChildren().addAll(typeLabel,nameLabel, priceLabel, quantityLabel);
            }
            else
            {
                transactionWithButton.getChildren().addAll(typeLabel,nameLabel, sizeLabel, priceLabel, quantityLabel);
            }

            transactionListWithButtons.add(transactionWithButton);
        }
        basketListView.getItems().setAll(transactionListWithButtons);
    }

    private ListView<HBox> createBasketListView(){
        VBox.setMargin(basketListView, new Insets(0, 10, 0, 10));
        basketListView.setPrefWidth(200);
        return basketListView;
    }

    private HBox buttonConfirm(ShoppingCartController controller) {

        HBox confirmButtonBox = new HBox();
        HBox.setMargin(confirmButtonBox, new Insets(10,10,10,10));

        Button confirmButton = new Button("Confirm transaction");
        Button cancelButton = new Button("Cancel transaction");

        confirmButton.setOnAction(event -> {
            controller.getModel().confirmTransaction();

        });
        cancelButton.setOnAction(actionEvent -> {
            basketListView.getItems().clear();
        });

        confirmButtonBox.setAlignment(Pos.CENTER);
        confirmButtonBox.getChildren().addAll(confirmButton,cancelButton);
        return confirmButtonBox;
    }


    /****************************/
    public Map<String, String> handleBuyButtonClick(String productType) {
        Map<String, String> details = new HashMap<>();

        // Create a custom dialog
        Dialog<Map<String, String>> buyDialog = new Dialog<>();
        buyDialog.setTitle("Buy " + productType);
        buyDialog.setHeaderText("Enter " + productType + " details for purchase:");

        // Set the button types
        ButtonType buyButtonType = new ButtonType("Buy", ButtonBar.ButtonData.OK_DONE);
        buyDialog.getDialogPane().getButtonTypes().addAll(buyButtonType, ButtonType.CANCEL);

        // Create the form GridPane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Add form fields
        TextField nameField = new TextField();
        nameField.setPromptText(productType + " Name");

        TextField sizeField = new TextField();
        sizeField.setPromptText(productType + " Size");

        TextField priceField = new TextField();
        priceField.setPromptText(productType + " Price");

        TextField quantityField = new TextField();
        quantityField.setPromptText(productType + " Quantity");

        grid.add(new Label(productType + " Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        if ("Shoes".equals(productType) || "Clothes".equals(productType)) {
            grid.add(new Label(productType + " Size:"), 0, 1);
            grid.add(sizeField, 1, 1);
        }

        grid.add(new Label(productType + " Price:"), 0, 2);
        grid.add(priceField, 1, 2);

        grid.add(new Label(productType + " Quantity:"), 0, 3);
        grid.add(quantityField, 1, 3);

        // Enable/Disable button depending on whether all fields are filled
        Node buyButton = buyDialog.getDialogPane().lookupButton(buyButtonType);
        buyButton.setDisable(true);

        // Do some validation
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            buyButton.setDisable(newValue.trim().isEmpty());
        });

        buyDialog.getDialogPane().setContent(grid);

        // Convert the result to a map when the buy button is clicked
        buyDialog.setResultConverter(dialogButton -> {
            if (dialogButton == buyButtonType) {
                details.put("Type", productType);
                details.put("Name", nameField.getText());

                if ("Shoes".equals(productType) || "Clothes".equals(productType)) {
                    String sizeInput = sizeField.getText();
                    if (!isValidNumericInput(sizeInput)) {
                        showAlert("Invalid Input", "Please enter a valid numeric size.", Alert.AlertType.WARNING);
                        return null; // Stop processing if size is invalid
                    }

                    int size = Integer.parseInt(sizeInput);
                    if ("Clothes".equals(productType) && (size < 36 || size > 50)) {
                        showAlert("Invalid Size", "Size must be between 36 and 50 for Clothes.", Alert.AlertType.WARNING);
                        return null; // Stop processing if size is out of range
                    }

                    details.put("Size", String.valueOf(size));
                }

                details.put("Price", priceField.getText());
                details.put("Quantity", quantityField.getText());
                details.put("Transaction", "Buy");
            }
            return details;
        });

        // Show the dialog and wait for the user's input
        Optional<Map<String, String>> result = buyDialog.showAndWait();

        return result.orElse(new HashMap<>());
    }

    public Map<String, String> handleSellButtonClick(ShoppingCartController controller) {
        Map<String, String> details = new HashMap<>();

        int productId = askForProductID(controller);
        if (productId == 0) {
            return null;
        }

        // Create a custom dialog
        Dialog<Map<String, String>> sellDialog = new Dialog<>();
        sellDialog.setTitle("Product Sale");
        sellDialog.setHeaderText("Enter product details for sale:");

        // Set the button types
        ButtonType sellButtonType = new ButtonType("Sell", ButtonBar.ButtonData.OK_DONE);
        sellDialog.getDialogPane().getButtonTypes().addAll(sellButtonType, ButtonType.CANCEL);

        // Create the form GridPane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Add form fields
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        grid.add(new Label("Quantity:"), 0, 0);
        grid.add(quantityField, 1, 0);

        // Enable/Disable button depending on whether a quantity is entered
        Node sellButton = sellDialog.getDialogPane().lookupButton(sellButtonType);
        sellButton.setDisable(true);

        // Do some validation
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            sellButton.setDisable(newValue.trim().isEmpty() || !isValidNumericInput(newValue) || Integer.parseInt(newValue) <= 0);
        });

        sellDialog.getDialogPane().setContent(grid);

        // Convert the result to a map when the sell button is clicked
        sellDialog.setResultConverter(dialogButton -> {
            details.put("ID", String.valueOf(productId));
            if (dialogButton == sellButtonType) {
                String quantityText = quantityField.getText();
                if (isValidNumericInput(quantityText)) {
                    int quantity = Integer.parseInt(quantityText);
                    if (quantity > 0) {
                        details.put("Quantity", quantityText);
                    } else {
                        showAlert("Invalid Input", "Please enter a quantity greater than 0.", Alert.AlertType.ERROR);
                        return null; // Don't proceed with the sale if quantity is not greater than 0
                    }
                } else {
                    showAlert("Invalid Input", "Please enter a valid quantity.", Alert.AlertType.ERROR);
                    return null; // Don't proceed with the sale if quantity is not a valid number
                }
            }
            details.put("Transaction", "Sale");
            return details;
        });

        Optional<Map<String, String>> result = sellDialog.showAndWait();

        return result.orElse(new HashMap<>());
    }


}