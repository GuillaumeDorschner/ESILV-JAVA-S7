package application.Modele;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Product> products;

    public ShoppingCart() {
        this.products = new ArrayList<Product>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }
    
    public double totalPrice() {
        double price = 0;

        if (products != null) {
            for (Product p : products) {
                price += p.getPrice();
            }
        }

        return price;
    }
    
    public void removeProduct(Product product) {
        if (products != null) {
            products.remove(product);
        }
    }
}
