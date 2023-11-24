package application.Vue;

import application.Controleur.ShoppingCartController;
import application.Modele.Product;
import javafx.geometry.Insets;
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
        stage.setTitle("Shopping Cart App");

        BorderPane mainLayout = new BorderPane();

        mainLayout.setTop(createMenuBar());
        mainLayout.setLeft(createProductNavigation(controller));
        mainLayout.setCenter(createProductListView());
        mainLayout.setRight(createBasketView(controller));

        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
    
    private MenuBar createMenuBar() {

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(e -> stage.close());
        fileMenu.getItems().add(exitMenuItem);
        menuBar.getMenus().add(fileMenu);

        return menuBar;
    }
 /******************************/
    
    private VBox createProductNavigation(ShoppingCartController controller) {
        VBox productNavigation = new VBox();

        Button clothesButton = createCategoryButton("Clothes", controller);
        Button shoesButton = createCategoryButton("Shoes", controller);
        Button accessoriesButton = createCategoryButton("Accessories", controller);

        productNavigation.getChildren().addAll(clothesButton, shoesButton, accessoriesButton);

        return productNavigation;
    }

    private Button createCategoryButton(String category, ShoppingCartController controller) {
        Button button = new Button(category);
        button.setMinWidth(120); // Set a fixed width for all buttons
        VBox.setMargin(button, new Insets(10, 10, 0, 10)); // Add left and right margin

        // Set action for the button
        button.setOnAction(event -> controller.showProductCategory(category));

        return button;
    }

    
    /******************************/
    
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
