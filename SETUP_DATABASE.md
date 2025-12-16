# 🔧 Setup SQL Server untuk CafeFlow - Quick Guide

## ⚠️ PENTING: Aplikasi Sekarang HANYA Menggunakan SQL Server!

SQLite telah dihapus. Anda **HARUS setup SQL Server** sebelum menjalankan aplikasi.

---

## 🚀 Setup Cepat (5 Langkah)

### 1️⃣ **Install SQL Server Express**
Download dan install:
```
https://www.microsoft.com/en-us/sql-server/sql-server-downloads
```
- Pilih **Express** edition (gratis)
- Pilih **Basic Installation**

### 2️⃣ **Install SSMS (SQL Server Management Studio)**
Download:
```
https://aka.ms/ssmsfullsetup
```

### 3️⃣ **Buat Database**
Buka **SSMS** dan pilih salah satu cara:

#### CARA 1: Menggunakan SQL Script (RECOMMENDED ⭐)
1. Di SSMS, klik **File** → **Open** → **File...**
2. Pilih file **`setup_quick.sql`** dari folder project
3. Klik **Execute** (atau tekan F5)
4. Tunggu sampai muncul pesan "✓ DATABASE SETUP SELESAI!"

**Optional - Insert Sample Data untuk Testing:**
1. Buka file **`sample_data.sql`**
2. Klik **Execute** (F5)
3. Lihat sample orders yang ter-insert

#### CARA 2: Manual Query
Jalankan query ini:
```sql
CREATE DATABASE CafeFlowDB;
GO
```

### 4️⃣ **Pastikan SQL Server Service Running**
- Buka **Services** (tekan Win+R, ketik `services.msc`)
- Cari **SQL Server (SQLEXPRESS)** atau **SQL Server (MSSQLSERVER)**
- Pastikan status: **Running**
- Jika belum, klik kanan → **Start**

### 5️⃣ **Jalankan Aplikasi**
```powershell
mvn clean compile exec:java
```

---

## ✅ Verifikasi

Jika berhasil, Anda akan melihat:
```
✓ Using Windows Authentication
✓ Database: CafeFlowDB on localhost
✓ Database tables ready!
Application started successfully!
```

---

## ❌ Troubleshooting

### Error: "Login failed for user"

**Solusi 1: Gunakan Windows Authentication (Default)**
File `db.properties` sudah di-set ke Windows Auth:
```properties
db.integratedSecurity=true
db.username=
db.password=
```

**Solusi 2: Gunakan SQL Server Authentication**
Edit `src/main/resources/db.properties`:
```properties
db.integratedSecurity=false
db.username=sa
db.password=YourActualPassword
```

### Error: "Cannot open database 'CafeFlowDB'"

**Solusi:**
Buat database dulu di SSMS:
```sql
CREATE DATABASE CafeFlowDB;
```

### Error: "The TCP/IP connection to the host localhost, port 1433 has failed"

**Solusi:**
1. Buka **SQL Server Configuration Manager**
2. Expand **SQL Server Network Configuration**
3. Klik **Protocols for SQLEXPRESS**
4. Klik kanan **TCP/IP** → **Enable**
5. Restart SQL Server service

### Error: "This driver is not configured for integrated authentication"

**Solusi untuk Windows Authentication:**
Download file DLL:
```
https://github.com/microsoft/mssql-jdbc/releases
```
- Download `mssql-jdbc_auth-xxx-x64.dll`
- Copy ke `C:\Windows\System32\`

**Atau gunakan SQL Authentication:**
```properties
db.integratedSecurity=false
db.username=sa
db.password=YourPassword
```

---

## 📝 Konfigurasi Database

File: `src/main/resources/db.properties`

### Default (Windows Authentication - Recommended):
```properties
db.server=localhost
db.port=1433
db.database=CafeFlowDB
db.username=
db.password=
db.integratedSecurity=true
db.encrypt=true
db.trustServerCertificate=true
```

### Named Instance (SQLEXPRESS):
```properties
db.server=localhost\\SQLEXPRESS
db.port=1433
db.database=CafeFlowDB
db.integratedSecurity=true
```

### SQL Server Authentication:
```properties
db.server=localhost
db.port=1433
db.database=CafeFlowDB
db.username=sa
db.password=YourPassword123
db.integratedSecurity=false
db.encrypt=true
db.trustServerCertificate=true
```

---

## 🗄️ Data Persistence

✅ **Semua data order akan tersimpan di SQL Server**
- Database: `CafeFlowDB`
- Tabel: `orders` dan `order_items`
- Data **PERMANEN** (tidak hilang saat aplikasi ditutup)

### Cara Melihat Data di SSMS:
```sql
USE CafeFlowDB;

-- Lihat semua orders
SELECT * FROM orders ORDER BY order_time DESC;

-- Lihat detail items dari sebuah order
SELECT * FROM order_items WHERE order_id = 1;

-- Lihat total penjualan hari ini
SELECT COUNT(*) as total_orders, SUM(grand_total) as total_revenue
FROM orders
WHERE CAST(order_time AS DATE) = CAST(GETDATE() AS DATE);
```

---

## 🔍 Cek Status Koneksi

Saat aplikasi berjalan, perhatikan console output:

**✅ SUCCESS:**
```
✓ Using Windows Authentication
✓ Database: CafeFlowDB on localhost
✓ Database tables ready!
Application started successfully!
```

**❌ FAILED:**
```
❌ Database Error: Login failed for user...
💡 Please check:
   1. SQL Server is running
   2. Database 'CafeFlowDB' exists
   3. Check db.properties configuration
```

---

## 📊 Database Schema

Aplikasi akan otomatis membuat tabel ini jika belum ada:

### Table: `orders`
```sql
id              INT IDENTITY(1,1) PRIMARY KEY
order_number    NVARCHAR(50) NOT NULL UNIQUE
order_time      DATETIME2 NOT NULL
kasir           NVARCHAR(100) NOT NULL
customer        NVARCHAR(100)
total_harga     DECIMAL(18,2) NOT NULL
total_diskon    DECIMAL(18,2) NOT NULL
grand_total     DECIMAL(18,2) NOT NULL
status          NVARCHAR(50) NOT NULL
payment_method  NVARCHAR(50) NOT NULL DEFAULT 'CASH'
```

### Table: `order_items`
```sql
id            INT IDENTITY(1,1) PRIMARY KEY
order_id      INT NOT NULL (FK to orders.id)
menu_name     NVARCHAR(100) NOT NULL
menu_category NVARCHAR(50) NOT NULL
quantity      INT NOT NULL
price         DECIMAL(18,2) NOT NULL
subtotal      DECIMAL(18,2) NOT NULL
```

---

## ⚡ Quick Commands

```powershell
# Compile
mvn clean compile

# Run
mvn exec:java

# Compile & Run
mvn clean compile exec:java

# Run Tests
mvn test
```

---

## 📞 Bantuan Lebih Lanjut

Jika masih ada masalah:
1. Cek file log di console
2. Pastikan SQL Server service running
3. Test koneksi dengan SSMS dulu
4. Cek file `db.properties` sudah benar
5. Lihat dokumentasi lengkap di `SQL_SERVER_SETUP.md`

---

**🎉 Setelah setup SQL Server, aplikasi siap digunakan!**

Data order akan tersimpan **permanen** di database SQL Server.
