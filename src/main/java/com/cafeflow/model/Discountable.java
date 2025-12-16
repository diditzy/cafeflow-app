package com.cafeflow.model;

/** Interface untuk item yang bisa didiskon (Polymorphism) */
public interface Discountable {
    double hitungDiskon();
    
    default double getPersenDiskon() {
        return 0;
    }
}
