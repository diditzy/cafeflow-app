package com.cafeflow.model;

/**
 * Representasi menu kopi/minuman.
 * Penerapan materi: Inheritance, Polymorphism, Interface Implementation.
 */
public class Coffee extends MenuItem implements Discountable {
    private String tipeGula; // Normal, Less, No Sugar
    private String ukuran;   // Small, Medium, Large
    private boolean isHot;   // Panas atau Dingin

    public Coffee(String nama, double harga, String tipeGula) {
        super(nama, harga, "Minuman");
        this.tipeGula = tipeGula;
        this.ukuran = "Medium";
        this.isHot = true;
    }
    
    public Coffee(String nama, double harga, String tipeGula, String ukuran, boolean isHot) {
        super(nama, harga, "Minuman");
        this.tipeGula = tipeGula;
        this.ukuran = ukuran;
        this.isHot = isHot;
    }

    // Getter & Setter
    public String getTipeGula() { return tipeGula; }
    public void setTipeGula(String tipeGula) { this.tipeGula = tipeGula; }
    
    public String getUkuran() { return ukuran; }
    public void setUkuran(String ukuran) { this.ukuran = ukuran; }
    
    public boolean isHot() { return isHot; }
    public void setHot(boolean hot) { isHot = hot; }

    @Override
    public String getInfoPenyajian() {
        String suhu = isHot ? "Panas" : "Dingin";
        return String.format("Disajikan %s, Ukuran %s, Gula: %s", suhu, ukuran, tipeGula);
    }

    @Override
    public double hitungDiskon() {
        // Logika: Jika harga > 20000, diskon 10%
        if (getHarga() > 20000) {
            return getHarga() * 0.1;
        }
        return 0;
    }
    
    @Override
    public double getPersenDiskon() {
        return getHarga() > 20000 ? 10 : 0;
    }
    
    @Override
    public String getDeskripsi() {
        return super.getDeskripsi() + " | " + getInfoPenyajian();
    }
}
