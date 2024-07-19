package com.example.myapplication.entity;

import java.util.ArrayList;
import java.util.List;

public class CartEntity {
    private static CartEntity instance;
    private List<ProductEntity> cartProducts;

    private CartEntity() {
        cartProducts = new ArrayList<>();
    }

    public static CartEntity getInstance() {
        if (instance == null) {
            instance = new CartEntity();
        }
        return instance;
    }

    public List<ProductEntity> getCartProducts() {
        return cartProducts;
    }

    public void addProduct(ProductEntity product) {
        cartProducts.add(product);
    }
}


