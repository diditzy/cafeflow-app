-- =====================================================
-- QUICK SETUP untuk CafeFlow Database
-- Jalankan script ini di SSMS untuk setup database
-- =====================================================

-- 1. CREATE DATABASE
USE master;
GO

IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'CafeFlowDB')
BEGIN
    CREATE DATABASE CafeFlowDB;
    PRINT 'Database CafeFlowDB berhasil dibuat!';
END
ELSE
BEGIN
    PRINT 'Database CafeFlowDB sudah ada.';
END
GO

-- 2. USE DATABASE
USE CafeFlowDB;
GO

-- 3. CREATE TABLES
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
    PRINT 'Tabel orders berhasil dibuat!';
END
ELSE
BEGIN
    PRINT 'Tabel orders sudah ada.';
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
    PRINT 'Tabel order_items berhasil dibuat!';
END
ELSE
BEGIN
    PRINT 'Tabel order_items sudah ada.';
END
GO

-- 4. VERIFIKASI
PRINT '';
PRINT '========================================';
PRINT 'DATABASE SETUP SELESAI!';
PRINT '========================================';
PRINT '';
PRINT 'Database: CafeFlowDB';
PRINT 'Tabel yang tersedia:';

SELECT 
    TABLE_NAME as 'Nama Tabel',
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = t.TABLE_NAME) as 'Jumlah Kolom'
FROM INFORMATION_SCHEMA.TABLES t
WHERE TABLE_TYPE = 'BASE TABLE'
ORDER BY TABLE_NAME;

PRINT '';
PRINT 'Aplikasi CafeFlow siap digunakan!';
PRINT 'Jalankan: mvn clean compile exec:java';
GO
