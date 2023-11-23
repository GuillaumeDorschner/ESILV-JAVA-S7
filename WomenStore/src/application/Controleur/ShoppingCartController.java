package application.Controleur;


import application.Modele.Product;
import application.Modele.ShoppingCartModel;
import application.Vue.ShoppingCartView;


public class ShoppingCartController {
    private ShoppingCartModel model;
    private ShoppingCartView view;

    public ShoppingCartController(ShoppingCartModel model, ShoppingCartView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        view.setController(this);
    }

    public void onProductSelected(Product product) {
        double discountedPrice = product.getPrice();
        view.showProductDetails(product, discountedPrice);
        model.addToBasket(product);
    }
}