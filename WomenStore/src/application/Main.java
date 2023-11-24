package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import application.Controleur.*;
import application.Vue.*;
import application.Modele.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize model, view, and controller
    	ShoppingCartModel model = new application.Modele.ShoppingCartModel();
    	ShoppingCartView view = new application.Vue.ShoppingCartView(primaryStage);
    	ShoppingCartController controller = new application.Controleur.ShoppingCartController(model, view);

    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
