package application.Modele;

public class ShoppingCartModel {
	
	private ShoppingCart shoppingCart;

    public ShoppingCartModel() {
        this.shoppingCart = new ShoppingCart();
    }

    public void addToBasket(Product product) {
        shoppingCart.addProduct(product);
    }

}
