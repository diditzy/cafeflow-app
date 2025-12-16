package com.cafeflow.model;

/**
 * Representasi menu makanan.
 * Penerapan materi: Inheritance, Polymorphism, Method Overriding.
 */
public class Food extends MenuItem {
    private boolean isPedas;
    private String porsi; // Regular, Large
    private int waktuMasak; // dalam menit

    public Food(String nama, double harga, boolean isPedas) {
        super(nama, harga, "Makanan");
        this.isPedas = isPedas;
        this.porsi = "Regular";
        this.waktuMasak = 15;
    }
    
    public Food(String nama, double harga, boolean isPedas, String porsi, int waktuMasak) {
        super(nama, harga, "Makanan");
        this.isPedas = isPedas;
        this.porsi = porsi;
        this.waktuMasak = waktuMasak;
    }

    // Getter & Setter
    public boolean isPedas() { return isPedas; }
    public void setPedas(boolean pedas) { isPedas = pedas; }
    
    public String getPorsi() { return porsi; }
    public void setPorsi(String porsi) { this.porsi = porsi; }
    
    public int getWaktuMasak() { return waktuMasak; }
    public void setWaktuMasak(int waktuMasak) { this.waktuMasak = waktuMasak; }

    @Override
    public String getInfoPenyajian() {
        String info = isPedas ? "Disajikan dengan saus sambal ekstra" : "Disajikan original";
        return String.format("%s, Porsi %s, Waktu Masak ~%d menit", info, porsi, waktuMasak);
    }
    
    @Override
    public String getDeskripsi() {
        return super.getDeskripsi() + " | " + getInfoPenyajian();
    }
}
