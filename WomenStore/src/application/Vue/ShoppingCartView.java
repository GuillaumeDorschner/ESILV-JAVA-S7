package application.Vue;

import application.Modele.*;
import application.Controleur.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ShoppingCartView {
	
	private Stage stage;
    private ShoppingCartController controller;

    public ShoppingCartView(Stage stage) {
        this.stage = stage;
    }
    
    public void setController(ShoppingCartController controller) {
        this.controller = controller;
    }
    
    public void show() {
        stage.setTitle("Shopping App");

        // Create main layout
        BorderPane mainLayout = new BorderPane();

        // Create menu bar
        MenuBar menuBar = createMenuBar();
        mainLayout.setTop(menuBar);

        // Create product view
        VBox productView = createProductView();
        mainLayout.setCenter(productView);

        // Create shopping basket view
        VBox basketView = createBasketView();
        mainLayout.setRight(basketView);

        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
    
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(event -> System.exit(0));
        fileMenu.getItems().add(exitMenuItem);
        menuBar.getMenus().add(fileMenu);
        return menuBar;
    }

    private VBox createProductView() {
        VBox productView = new VBox();

        // Create instances of Clothes, Shoes, and Accessories
        Clothes clothes = new Clothes("T-shirt", 19.99, 2, 40);
        Shoes shoes = new Shoes("Sneakers", 49.99, 6, 38);
        Accessories accessories = new Accessories("Sunglasses", 29.99, 4);

        // Add product items, each represented by a button or other UI components
        // Handle button clicks to view details or add to the basket
        Button viewDetailsButtonClothes = createProductButton(clothes);
        Button viewDetailsButtonShoes = createProductButton(shoes);
        Button viewDetailsButtonAccessories = createProductButton(accessories);
        productView.getChildren().addAll(viewDetailsButtonClothes, viewDetailsButtonShoes, viewDetailsButtonAccessories);

        // Add more products as needed
        return productView;
    }

    private Button createProductButton(Product product) {
        Button productButton = new Button("View Details");
        productButton.setOnAction(event -> controller.onProductSelected(product));
        return productButton;
    }

    private VBox createBasketView() {
        VBox basketView = new VBox();
        // Display the contents of the shopping basket
        // This might involve listing items, quantities, and providing options to remove items
        basketView.getChildren().add(new Label("Shopping Basket"));
        return basketView;
    }

    public void showProductDetails(Product product, double discountedPrice) {
        // Implement logic to show detailed information about the selected product
        // This might involve creating a new window or updating the current view
        System.out.println("View details clicked for " + product.toString() + ", Discounted Price: $" + discountedPrice);
    }


}
