package application.Vue;

import application.Controleur.ShoppingCartController;
import application.Modele.Product;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ShoppingCartView {
	private Stage stage;
    private ListView<String> productListView;
    private VBox basketView;
    private Label totalPriceLabel;

    public ShoppingCartView(Stage stage) {
        this.stage = stage;
        productListView = new ListView<>();
        totalPriceLabel = new Label("Total Price: $0.0");
    }
    
    public void show(ShoppingCartController controller) {
        stage.setTitle("Management of the Shopping Cart App");

        BorderPane mainLayout = new BorderPane();

        mainLayout.setTop(createMenuBar());
        mainLayout.setLeft(createProductCategoryButtons(controller));

        // Create a VBox to stack the action buttons above the product list
        VBox centerLayout = new VBox();
        centerLayout.setAlignment(Pos.TOP_CENTER);
        centerLayout.setSpacing(10);

        // Add the action buttons
        centerLayout.getChildren().add(createProductActionButtons(controller));

        // Add the product list (you may replace this with your actual product list view)
        centerLayout.getChildren().add(createProductListView());

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
    /**************Buttons for different product categories****************/

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

    
    
    /*************Buying/selling*****************/
    private ListView<String> createProductListView() {
        productListView.setPrefWidth(200);
        return productListView;
    }

    private VBox createBasketView(ShoppingCartController controller) {
        // Implementation for basket view
        // ...

        VBox basketView = new VBox(new Label("Basket"), new Button("Checkout")); // Example button for checkout
        basketView.setSpacing(10);

        return basketView;
    }

    public void updateProductListView(List<String> productDetails) {
        productListView.getItems().setAll(productDetails);
    }

    public void updateTotalPriceLabel(double totalPrice) {
        totalPriceLabel.setText("Total Price: $" + totalPrice);
    }

}
