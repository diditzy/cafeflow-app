package com.cafeflow;

import com.cafeflow.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Testing untuk logika bisnis CafeFlow.
 * Penerapan materi: Unit Testing dengan JUnit 5.
 * 
 * @author Radit Alfa Anugerah Bombing - L0124116
 */
class OrderTest {
    
    private Coffee kopiMahal;
    private Coffee kopiMurah;
    private Food nasiGoreng;
    private Order order;
    
    @BeforeEach
    void setUp() {
        // Inisialisasi objek untuk testing
        kopiMahal = new Coffee("Premium Latte", 25000, "Normal");
        kopiMurah = new Coffee("Kopi Hitam", 10000, "Normal");
        nasiGoreng = new Food("Nasi Goreng Spesial", 30000, true);
        order = new Order("Kasir Test", "Customer Test");
    }

    @Test
    @DisplayName("Test: Kopi mahal harus mendapat diskon 10%")
    void testHitungDiskonKopiMahal() {
        // Skenario: Kopi harga 25.000 harus dapat diskon 10% (2.500)
        double diskon = kopiMahal.hitungDiskon();
        
        assertEquals(2500.0, diskon, 0.01, 
            "Diskon harus 10% (Rp 2.500) jika harga > Rp 20.000");
        
        // Test persentase diskon
        assertEquals(10.0, kopiMahal.getPersenDiskon(), 
            "Persentase diskon harus 10%");
    }

    @Test
    @DisplayName("Test: Kopi murah tidak mendapat diskon")
    void testKopiMurahTidakDiskon() {
        // Skenario: Kopi harga 10.000 tidak dapat diskon
        double diskon = kopiMurah.hitungDiskon();
        
        assertEquals(0.0, diskon, 0.01, 
            "Harga < Rp 20.000 tidak boleh ada diskon");
        
        assertEquals(0.0, kopiMurah.getPersenDiskon(), 
            "Persentase diskon harus 0%");
    }
    
    @Test
    @DisplayName("Test: Polymorphism - Interface Discountable")
    void testPolymorphismDiscountable() {
        // Test apakah Coffee implements Discountable
        assertTrue(kopiMahal instanceof Discountable, 
            "Coffee harus implement interface Discountable");
        
        // Test polymorphism
        Discountable item = kopiMahal;
        assertTrue(item.hitungDiskon() > 0, 
            "Polymorphism: harus bisa memanggil hitungDiskon()");
    }
    
    @Test
    @DisplayName("Test: Inheritance - MenuItem sebagai parent class")
    void testInheritance() {
        // Test apakah Coffee dan Food extends MenuItem
        assertTrue(kopiMahal instanceof com.cafeflow.model.MenuItem, 
            "Coffee harus extends MenuItem");
        assertTrue(nasiGoreng instanceof com.cafeflow.model.MenuItem, 
            "Food harus extends MenuItem");
    }
    
    @Test
    @DisplayName("Test: Abstract method getInfoPenyajian()")
    void testAbstractMethod() {
        // Test method abstract yang di-override
        String infoKopi = kopiMahal.getInfoPenyajian();
        String infoFood = nasiGoreng.getInfoPenyajian();
        
        assertNotNull(infoKopi, "Info penyajian kopi tidak boleh null");
        assertNotNull(infoFood, "Info penyajian food tidak boleh null");
        assertTrue(infoKopi.length() > 0, "Info penyajian harus ada isinya");
        assertTrue(infoFood.length() > 0, "Info penyajian harus ada isinya");
    }
    
    @Test
    @DisplayName("Test: Order - Tambah item ke order")
    void testTambahItemKeOrder() {
        // Test menambah item ke order
        assertEquals(0, order.getItems().size(), 
            "Order baru harus kosong");
        
        order.addItem(kopiMahal, 2);
        assertEquals(1, order.getItems().size(), 
            "Setelah addItem, harus ada 1 jenis item");
        
        order.addItem(nasiGoreng, 1);
        assertEquals(2, order.getItems().size(), 
            "Setelah addItem lagi, harus ada 2 jenis item");
    }
    
    @Test
    @DisplayName("Test: Order - Hitung total dengan diskon")
    void testHitungTotalDenganDiskon() {
        // Tambah 2 kopi mahal (25.000 x 2 = 50.000, diskon 10% = 5.000)
        order.addItem(kopiMahal, 2);
        
        assertEquals(50000.0, order.getTotalHarga(), 0.01, 
            "Total harga harus Rp 50.000");
        assertEquals(5000.0, order.getTotalDiskon(), 0.01, 
            "Total diskon harus Rp 5.000");
        assertEquals(45000.0, order.getGrandTotal(), 0.01, 
            "Grand total harus Rp 45.000");
    }
    
    @Test
    @DisplayName("Test: Order - Hapus item dari order")
    void testHapusItemDariOrder() {
        order.addItem(kopiMahal, 1);
        order.addItem(nasiGoreng, 1);
        
        assertEquals(2, order.getItems().size());
        
        order.removeItem(0);
        assertEquals(1, order.getItems().size(), 
            "Setelah removeItem, harus ada 1 item tersisa");
    }
    
    @Test
    @DisplayName("Test: Order - Clear semua item")
    void testClearOrder() {
        order.addItem(kopiMahal, 1);
        order.addItem(nasiGoreng, 1);
        
        order.clearItems();
        assertEquals(0, order.getItems().size(), 
            "Setelah clear, order harus kosong");
        assertEquals(0.0, order.getGrandTotal(), 
            "Grand total harus 0 setelah clear");
    }
    
    @Test
    @DisplayName("Test: OrderItem - Quantity dan Subtotal")
    void testOrderItem() {
        OrderItem item = new OrderItem(kopiMahal, 3);
        
        assertEquals(3, item.getQuantity(), 
            "Quantity harus sesuai");
        assertEquals(75000.0, item.getSubtotal(), 0.01, 
            "Subtotal harus 25.000 x 3 = 75.000");
        
        // Test ubah quantity
        item.setQuantity(5);
        assertEquals(5, item.getQuantity());
        assertEquals(125000.0, item.getSubtotal(), 0.01, 
            "Subtotal harus update ke 25.000 x 5 = 125.000");
    }
    
    @Test
    @DisplayName("Test: Encapsulation - Getter dan Setter")
    void testEncapsulation() {
        // Test getter
        assertEquals("Premium Latte", kopiMahal.getNama());
        assertEquals(25000.0, kopiMahal.getHarga(), 0.01);
        assertEquals("Minuman", kopiMahal.getKategori());
        
        // Test setter
        kopiMahal.setNama("Super Premium Latte");
        assertEquals("Super Premium Latte", kopiMahal.getNama());
        
        kopiMahal.setHarga(30000);
        assertEquals(30000.0, kopiMahal.getHarga(), 0.01);
    }
    
    @Test
    @DisplayName("Test: Food - Property khusus")
    void testFoodProperties() {
        assertTrue(nasiGoreng.isPedas(), 
            "Nasi goreng harus pedas");
        
        Food kentang = new Food("Kentang Goreng", 15000, false);
        assertFalse(kentang.isPedas(), 
            "Kentang goreng tidak pedas");
        
        assertEquals("Regular", nasiGoreng.getPorsi(), 
            "Porsi default harus Regular");
    }
    
    @Test
    @DisplayName("Test: Coffee - Property khusus")
    void testCoffeeProperties() {
        assertEquals("Normal", kopiMahal.getTipeGula());
        assertEquals("Medium", kopiMahal.getUkuran());
        assertTrue(kopiMahal.isHot(), "Default harus panas");
        
        Coffee esCoffee = new Coffee("Es Kopi", 20000, "Less Sugar", "Large", false);
        assertFalse(esCoffee.isHot(), "Es kopi harus dingin");
        assertEquals("Large", esCoffee.getUkuran());
    }
    
    @Test
    @DisplayName("Test: Order Number Generation")
    void testOrderNumberGeneration() {
        Order order1 = new Order("Kasir1", "Customer1");
        Order order2 = new Order("Kasir2", "Customer2");
        
        assertNotNull(order1.getOrderNumber());
        assertNotNull(order2.getOrderNumber());
        assertNotEquals(order1.getOrderNumber(), order2.getOrderNumber(), 
            "Order number harus unik");
        assertTrue(order1.getOrderNumber().startsWith("ORD-"), 
            "Order number harus dimulai dengan ORD-");
    }
}
