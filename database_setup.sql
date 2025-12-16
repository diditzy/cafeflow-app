-- =====================================================
-- CAFEFLOW DATABASE SETUP
-- Setup lengkap database untuk CafeFlow POS System
-- Jalankan script ini di SSMS
-- =====================================================

USE master;
GO

-- 1. CREATE DATABASE
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'CafeFlowDB')
BEGIN
    CREATE DATABASE CafeFlowDB;
    PRINT ' Database CafeFlowDB dibuat';
END
ELSE
BEGIN
    PRINT 'ℹ Database CafeFlowDB sudah ada';
END
GO

USE CafeFlowDB;
GO

-- 2. CREATE TABLES
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'orders')
BEGIN
    CREATE TABLE orders (
        id INT IDENTITY(1,1) PRIMARY KEY,
        order_number NVARCHAR(50) NOT NULL UNIQUE,
        order_time DATETIME2 NOT NULL,
        kasir NVARCHAR(100) NOT NULL,
        customer NVARCHAR(100),
        total_harga DECIMAL(18,2) NOT NULL,
        total_diskon DECIMAL(18,2) NOT NULL,
        grand_total DECIMAL(18,2) NOT NULL,
        status NVARCHAR(50) NOT NULL,
        payment_method NVARCHAR(50) NOT NULL DEFAULT 'CASH'
    );
    PRINT ' Tabel orders dibuat';
END
ELSE
BEGIN
    PRINT 'ℹ Tabel orders sudah ada';
END
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'order_items')
BEGIN
    CREATE TABLE order_items (
        id INT IDENTITY(1,1) PRIMARY KEY,
        order_id INT NOT NULL,
        menu_name NVARCHAR(100) NOT NULL,
        menu_category NVARCHAR(50) NOT NULL,
        quantity INT NOT NULL,
        price DECIMAL(18,2) NOT NULL,
        subtotal DECIMAL(18,2) NOT NULL,
        FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
    );
    PRINT ' Tabel order_items dibuat';
END
ELSE
BEGIN
    PRINT 'ℹ Tabel order_items sudah ada';
END
GO

-- 3. FIX CONSTRAINT (hapus constraint pada status jika ada)
DECLARE @constraintName NVARCHAR(200);
SELECT @constraintName = name
FROM sys.check_constraints
WHERE parent_object_id = OBJECT_ID('orders')
AND name LIKE '%status%';

IF @constraintName IS NOT NULL
BEGIN
    DECLARE @sql NVARCHAR(500);
    SET @sql = 'ALTER TABLE orders DROP CONSTRAINT ' + @constraintName;
    EXEC sp_executesql @sql;
    PRINT ' Constraint status dihapus';
END
GO

-- 4. INSERT SAMPLE DATA (optional - hapus jika tidak perlu)
DELETE FROM order_items WHERE order_id IN (
    SELECT id FROM orders WHERE order_number LIKE 'ORD-SAMPLE-%'
);
DELETE FROM orders WHERE order_number LIKE 'ORD-SAMPLE-%';

INSERT INTO orders (order_number, order_time, kasir, customer, total_harga, total_diskon, grand_total, status, payment_method)
VALUES 
    ('ORD-SAMPLE-001', GETDATE(), 'Admin', 'Customer 1', 50000.00, 5000.00, 45000.00, 'COMPLETED', 'CASH'),
    ('ORD-SAMPLE-002', GETDATE(), 'Kasir01', 'Customer 2', 75000.00, 0.00, 75000.00, 'COMPLETED', 'QRIS');

DECLARE @orderId1 INT = (SELECT id FROM orders WHERE order_number = 'ORD-SAMPLE-001');
DECLARE @orderId2 INT = (SELECT id FROM orders WHERE order_number = 'ORD-SAMPLE-002');

INSERT INTO order_items (order_id, menu_name, menu_category, quantity, price, subtotal)
VALUES 
    (@orderId1, 'Kopi Latte', 'Minuman', 2, 15000.00, 30000.00),
    (@orderId1, 'Nasi Goreng', 'Makanan', 1, 20000.00, 20000.00),
    (@orderId2, 'Cappuccino', 'Minuman', 3, 25000.00, 75000.00);

PRINT ' Sample data inserted';
GO

-- 5. VERIFIKASI
PRINT '';
PRINT '========================================';
PRINT '  DATABASE SETUP SELESAI!';
PRINT '========================================';
PRINT '';
SELECT 
    TABLE_NAME as 'Tabel',
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = t.TABLE_NAME) as 'Kolom'
FROM INFORMATION_SCHEMA.TABLES t
WHERE TABLE_TYPE = 'BASE TABLE'
ORDER BY TABLE_NAME;

PRINT '';
PRINT 'Total Orders: ' + CAST((SELECT COUNT(*) FROM orders) AS NVARCHAR(10));
PRINT 'Total Items: ' + CAST((SELECT COUNT(*) FROM order_items) AS NVARCHAR(10));
PRINT '';
PRINT 'Aplikasi siap digunakan!';
GO
