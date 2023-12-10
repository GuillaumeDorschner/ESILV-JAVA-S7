package com.example.womenstore.model;

public class Accessories extends Product{

  private double originalPrice=0;

  public Accessories(String name, double price, int nbItems) {
    super(name, price, nbItems);
  }

  public Accessories(int id, String name, double price, int nbItems) {
    super(id, name, price, nbItems);
  }

  @Override
  public String toString() {
    return "Accessories{"+ super.toString()+"}";
  }

  @Override
  public String getCategory() {
    return "Accessories";
  }

  @Override
  public void applyDiscount() {
    originalPrice = getPrice();
    System.out.println(originalPrice);
    setPrice(getPrice() * (1 - DISCOUNT_ACCESSORIES));
  }

  @Override
  public void stopDiscount() {
    setPrice(originalPrice);
    originalPrice=0;
  }

  public boolean isDiscountApplied(){
    if(originalPrice!=0){
      return true;
    }
    return false;
  }

}
