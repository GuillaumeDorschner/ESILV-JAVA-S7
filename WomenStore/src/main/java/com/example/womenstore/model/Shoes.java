package com.example.womenstore.model;

public class Shoes extends Product {

  private int shoeSize;
  private double originalPrice=0;

  public Shoes(String name, double price, int nbItems, int shoeSize) {
    super(name, price, nbItems);
    this.shoeSize = shoeSize;
    originalPrice=price;
  }

  public int getSize() {
    return shoeSize;
  }

  public void setShoeSize(int shoeSize) {
    this.shoeSize = shoeSize;
  }

  @Override
  public String toString() {
    return "Shoes{" + super.toString()+
            " shoeSize=" + shoeSize +
            '}';
  }

  @Override
  public String getCategory() {
    return "Shoes";
  }

  @Override
  public void applyDiscount() {
    originalPrice = getPrice();
    setPrice(getPrice()*(1-DISCOUNT_SHOES));
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
