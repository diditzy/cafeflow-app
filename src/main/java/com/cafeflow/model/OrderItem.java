package com.cafeflow.model;

/**
 * Kelas untuk merepresentasikan item dalam order.
 * Penerapan materi: Composition, Encapsulation.
 */
public class OrderItem {
    private MenuItem menu;
    private int quantity;
    private double subtotal;
    
    public OrderItem(MenuItem menu, int quantity) {
        this.menu = menu;
        this.quantity = quantity;
        this.subtotal = menu.getHarga() * quantity;
    }
    
    // Getters & Setters
    public MenuItem getMenu() { return menu; }
    public int getQuantity() { return quantity; }
    public double getSubtotal() { return subtotal; }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = menu.getHarga() * quantity;
    }
    
    public void addQuantity(int amount) {
        this.quantity += amount;
        this.subtotal = menu.getHarga() * quantity;
    }
    
    @Override
    public String toString() {
        return String.format("%s x%d = Rp %.0f", menu.getNama(), quantity, subtotal);
    }
}
