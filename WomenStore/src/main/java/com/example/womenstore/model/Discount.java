package com.example.womenstore.model;

public interface Discount {
  double DISCOUNT_CLOTHES=0.3;
  double DISCOUNT_SHOES=0.2;
  double DISCOUNT_ACCESSORIES= 0.5;

  void applyDiscount();

  void stopDiscount();

}

