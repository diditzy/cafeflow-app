package com.cafeflow.model;

/**
 * Interface untuk item yang bisa didiskon.
 * Penerapan materi: Interface & Polymorphism.
 */
public interface Discountable {
    /**
     * Menghitung nilai diskon yang didapat
     * @return Nilai diskon dalam rupiah
     */
    double hitungDiskon();
    
    /**
     * Mendapatkan persentase diskon
     * @return Persentase diskon (0-100)
     */
    default double getPersenDiskon() {
        return 0;
    }
}
