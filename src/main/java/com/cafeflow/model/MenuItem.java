package com.cafeflow.model;

/**
 * Kelas abstrak untuk semua menu cafe.
 * Penerapan materi: Abstract Class, Encapsulation.
 */
public abstract class MenuItem {
    protected String nama;
    protected double harga;
    protected String kategori;
    
    public MenuItem(String nama, double harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    // Getter methods (Encapsulation)
    public String getNama() { 
        return nama; 
    }
    
    public double getHarga() { 
        return harga; 
    }
    
    public String getKategori() { 
        return kategori; 
    }

    // Setter methods
    public void setNama(String nama) { 
        this.nama = nama; 
    }
    
    public void setHarga(double harga) { 
        this.harga = harga; 
    }

    /**
     * Method abstract yang wajib di-override anak kelas
     * @return Informasi cara penyajian menu
     */
    public abstract String getInfoPenyajian();
    
    /**
     * Method untuk mendapatkan deskripsi lengkap menu
     * @return Deskripsi menu
     */
    public String getDeskripsi() {
        return String.format("%s - Rp %.0f (%s)", nama, harga, kategori);
    }
    
    @Override
    public String toString() {
        return getDeskripsi();
    }
}
