-- =====================================================
-- SAMPLE DATA untuk Testing CafeFlow
-- Jalankan SETELAH setup_quick.sql berhasil
-- Script ini akan DELETE existing sample data dulu baru INSERT
-- =====================================================

USE CafeFlowDB;
GO

-- Hapus sample data lama jika ada (agar bisa run berkali-kali)
DELETE FROM order_items WHERE order_id IN (
    SELECT id FROM orders WHERE order_number LIKE 'ORD-20241216-%'
);
DELETE FROM orders WHERE order_number LIKE 'ORD-20241216-%';
GO

PRINT 'Existing sample data cleared.';
PRINT '';

-- Insert sample order 1
INSERT INTO orders (order_number, order_time, kasir, customer, total_harga, total_diskon, grand_total, status, payment_method)
VALUES ('ORD-20241216-001', GETDATE(), 'Admin', 'Radit', 50000.00, 5000.00, 45000.00, 'COMPLETED', 'CASH');

DECLARE @orderId1 INT = SCOPE_IDENTITY();

INSERT INTO order_items (order_id, menu_name, menu_category, quantity, price, subtotal)
VALUES 
    (@orderId1, 'Espresso', 'COFFEE', 2, 15000.00, 30000.00),
    (@orderId1, 'Croissant', 'FOOD', 1, 20000.00, 20000.00);

-- Insert sample order 2
INSERT INTO orders (order_number, order_time, kasir, customer, total_harga, total_diskon, grand_total, status, payment_method)
VALUES ('ORD-20241216-002', GETDATE(), 'Kasir01', 'Alfanugerah', 75000.00, 0.00, 75000.00, 'COMPLETED', 'QRIS');

DECLARE @orderId2 INT = SCOPE_IDENTITY();

INSERT INTO order_items (order_id, menu_name, menu_category, quantity, price, subtotal)
VALUES 
    (@orderId2, 'Cappuccino', 'COFFEE', 3, 25000.00, 75000.00);

-- Verifikasi
PRINT '';
PRINT '========================================';
PRINT 'SAMPLE DATA INSERTED!';
PRINT '========================================';
PRINT '';

SELECT 
    COUNT(*) as 'Total Orders' 
FROM orders;

SELECT 
    COUNT(*) as 'Total Order Items' 
FROM order_items;

PRINT '';
PRINT 'Detail Orders:';
SELECT 
    order_number as 'Order Number',
    kasir as 'Kasir',
    customer as 'Customer',
    FORMAT(grand_total, 'N0') as 'Grand Total',
    payment_method as 'Payment'
FROM orders
ORDER BY order_time DESC;

GO
