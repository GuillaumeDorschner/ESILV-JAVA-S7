package application.Modele;

public class Accessories extends Product{

  public Accessories(String name, double price, int nbItems) {
    super(name, price, nbItems);
  }

  @Override
  public String toString() {
    return "Accessories{"+ super.toString()+"}";
  }

  @Override
  public String getCategory() {
      return "Accessories";
  }
  
  public void applyDiscount() {
    this.setPrice(this.getPrice()*(1-DISCOUNT_ACCESSORIES));
  }
  
}
