package com.example.womenstore.model;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // write your code here
        try {
            Product p1 = new Shoes("s1", 10, 10,38);
            Product p2= new Clothes("c1", 20, 20, 36);

            Product [] tab= new Product[2];
            tab[0]=p1;
            tab[1]=p2;
            for(Product p:tab) {
                System.out.println(p);
            }

            p1.sell(9);
            p2.purchase(10);

            System.out.println(Product.getIncome());

            p1.applyDiscount();
            p2.applyDiscount();

           for(Product p:tab) {
                System.out.println(p);
            }

            System.out.println();
            List<Product> productList= new ArrayList<>();
            productList.add(p2);
            productList.add(p1);

            System.out.println(productList);

            productList.sort(null);
          System.out.println(productList);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}