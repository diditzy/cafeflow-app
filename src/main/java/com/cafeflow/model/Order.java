package com.cafeflow.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Kelas untuk merepresentasikan satu transaksi pesanan.
 * Penerapan materi: Composition, Collections.
 */
public class Order {
    private static int orderCounter = 1;
    
    private int orderId;
    private String orderNumber;
    private LocalDateTime orderTime;
    private List<OrderItem> items;
    private String kasir;
    private String customer;
    private double totalHarga;
    private double totalDiskon;
    private String status; // PENDING, PROCESSING, COMPLETED, CANCELLED
    private String paymentMethod; // CASH, QRIS, TRANSFER
    
    public Order(String kasir, String customer) {
        this.orderId = orderCounter++;
        this.orderTime = LocalDateTime.now();
        this.orderNumber = generateOrderNumber();
        this.items = new ArrayList<>();
        this.kasir = kasir;
        this.customer = customer;
        this.status = "PENDING";
        this.paymentMethod = "CASH"; // default
        this.totalHarga = 0;
        this.totalDiskon = 0;
    }
    
    private String generateOrderNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "ORD-" + orderTime.format(formatter);
    }
    
    public void addItem(MenuItem menu, int quantity) {
        OrderItem item = new OrderItem(menu, quantity);
        items.add(item);
        hitungTotal();
    }
    
    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            hitungTotal();
        }
    }
    
    public void clearItems() {
        items.clear();
        hitungTotal();
    }
    
    private void hitungTotal() {
        totalHarga = 0;
        totalDiskon = 0;
        
        for (OrderItem item : items) {
            totalHarga += item.getSubtotal();
            
            // Hitung diskon jika menu implements Discountable
            if (item.getMenu() instanceof Discountable) {
                totalDiskon += ((Discountable) item.getMenu()).hitungDiskon() * item.getQuantity();
            }
        }
    }
    
    public double getGrandTotal() {
        return totalHarga - totalDiskon;
    }
    
    // Getters & Setters
    public int getOrderId() { return orderId; }
    public String getOrderNumber() { return orderNumber; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public List<OrderItem> getItems() { return items; }
    public String getKasir() { return kasir; }
    public String getCustomer() { return customer; }
    public double getTotalHarga() { return totalHarga; }
    public double getTotalDiskon() { return totalDiskon; }
    public String getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }
    
    public void setStatus(String status) { this.status = status; }
    public void setCustomer(String customer) { this.customer = customer; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getFormattedOrderTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return orderTime.format(formatter);
    }
    
    @Override
    public String toString() {
        return String.format("Order #%s - %s | Total: Rp %.0f | Status: %s", 
            orderNumber, customer, getGrandTotal(), status);
    }
}
