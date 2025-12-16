package com.cafeflow.model;

/** Base class untuk semua menu cafe (Abstract) */
public abstract class MenuItem {
    protected String nama;
    protected double harga;
    protected String kategori;
    
    public MenuItem(String nama, double harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama() { return nama; }
    
    public double getHarga() { return harga; }
    
    public String getKategori() { return kategori; }

    public void setNama(String nama) { this.nama = nama; }
    
    public void setHarga(double harga) { this.harga = harga; }

    public abstract String getInfoPenyajian();
    
    public String getDeskripsi() {
        return String.format("%s - Rp %.0f (%s)", nama, harga, kategori);
    }
    
    @Override
    public String toString() {
        return getDeskripsi();
    }
}
