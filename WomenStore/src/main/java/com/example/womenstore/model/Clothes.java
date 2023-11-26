package com.example.womenstore.model;

public class Clothes extends Product{

  private int size;
  private double originalPrice;

  public Clothes(String name, double price, int nbItems, int size) {
    super(name, price, nbItems);
    setSize(size);
    originalPrice=price;
  }

  public int getSize() {
    return size;
  }

  @Override
  public String getCategory() {
    return "Clothes";
  }

  public void setSize(int size) throws IllegalArgumentException {
    if(size>=36 && size <=50){
      this.size = size;
    }else throw new IllegalArgumentException("Size is not valid");
  }

  @Override
  public String toString() {
    return "Clothes{" +super.toString()+
            " size=" + size +
            '}';
  }

  @Override
  public void applyDiscount() {
    originalPrice = getPrice();
    setPrice(getPrice()*(1-DISCOUNT_CLOTHES));
  }

  @Override
  public void stopDiscount() {
    setPrice(originalPrice);
  }


}
