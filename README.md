# ☕ CafeFlow - Sistem POS Modern untuk Cafe

> Aplikasi Point of Sale (POS) berbasis Java Swing dengan database SQL Server

**Dibuat oleh:** Radit Alfa Anugerah Bombing  
**NIM:** L0124116  
**Mata Kuliah:** Pemrograman Berorientasi Objek (PPBO)

---

## 📋 Deskripsi

CafeFlow adalah aplikasi kasir modern untuk cafe yang menerapkan konsep Object-Oriented Programming (OOP). Aplikasi ini menyediakan fitur lengkap untuk manajemen pemesanan, perhitungan otomatis, simulasi dapur dengan multithreading, dan penyimpanan data ke SQL Server.

---

## ✨ Fitur Utama

### 🎯 Fitur Fungsional
- ✅ **Menu Management** - 8+ menu minuman dan makanan
- ✅ **Shopping Cart** - Keranjang belanja interaktif
- ✅ **Auto Discount** - Diskon otomatis untuk kopi >Rp 20.000
- ✅ **Real-time Calculation** - Perhitungan subtotal, diskon, grand total
- ✅ **Kitchen Simulation** - Simulasi proses memasak dengan multithreading
- ✅ **Database Integration** - Penyimpanan transaksi ke SQL Server
- ✅ **Order History** - Riwayat transaksi lengkap
- ✅ **Receipt Printing** - Struk pembayaran digital
- ✅ **Sales Report** - Laporan penjualan harian real-time
- ✅ **Modern UI** - Interface modern dengan custom icons

### 🎓 Penerapan Konsep OOP

1. **Inheritance (Pewarisan)**
   - `Coffee` dan `Food` mewarisi dari `MenuItem`
   - Hierarki class yang terstruktur

2. **Polymorphism (Polimorfisme)**
   - Interface `Discountable` untuk sistem diskon
   - Method overriding `getInfoPenyajian()`

3. **Encapsulation (Enkapsulasi)**
   - Private attributes dengan getter/setter
   - Data hiding untuk keamanan

4. **Abstraction (Abstraksi)**
   - Abstract class `MenuItem`
   - Interface untuk contract

5. **Multithreading**
   - `KitchenTask` extends `Thread`
   - Simulasi proses dapur tanpa blocking UI

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.9+
- SQL Server 2019+ (Express/Developer/Standard)
- SQL Server Management Studio (SSMS)

### Setup Database

1. **Install SQL Server & SSMS**
   - Download SQL Server Express: https://aka.ms/sqlexpress
   - Download SSMS: https://aka.ms/ssmsfullsetup

2. **Setup Database**
   ```
   - Buka SSMS
   - Login dengan SQL Server Authentication
   - Jalankan file: database_setup.sql (ALL-IN-ONE)
   ```
   
   **File SQL yang tersedia:**
   - `database_setup.sql` - **File utama** (gabungan semua setup + sample data)
   - `setup_quick.sql` - Setup database saja (opsional, jika tidak ingin sample data)
   - `sample_data.sql` - Sample data saja (opsional)
   - `fix_constraint.sql` - Fix constraint jika ada masalah (opsional)

3. **Konfigurasi Connection**
   
   Edit file `src/main/resources/db.properties`:
   ```properties
   db.server=localhost
   db.port=1433
   db.database=CafeFlowDB
   db.username=YourUsername
   db.password=YourPassword
   db.integratedSecurity=false
   db.encrypt=false
   ```

### Run Aplikasi

```bash
# Compile project
mvn clean compile

# Run aplikasi
mvn exec:java
```

**Atau menggunakan batch file:**
```bash
# Windows
build-and-run.bat
```

---

## 📁 Struktur Project

```
CafeFlow/
├── src/main/java/com/cafeflow/
│   ├── MainApp.java              # Entry point aplikasi
│   ├── gui/
│   │   ├── MainFrame.java        # Main GUI (600+ lines → 550 lines cleaned)
│   │   ├── IconHelper.java       # Custom icon renderer
│   │   └── KitchenTask.java      # Multithreading untuk simulasi dapur
│   └── model/
│       ├── MenuItem.java         # Abstract class untuk menu
│       ├── Coffee.java           # Class untuk menu kopi
│       ├── Food.java             # Class untuk menu makanan
│       ├── Discountable.java     # Interface untuk diskon
│       ├── Order.java            # Class untuk transaksi
│       ├── OrderItem.java        # Class untuk item dalam order
│       └── DatabaseManager.java  # Singleton untuk database
├── src/main/resources/
│   └── db.properties             # Konfigurasi database
├── setup_quick.sql               # SQL script untuk setup database
├── sample_data.sql               # Sample data untuk testing
├── fix_constraint.sql            # Fix untuk constraint issues
└── pom.xml                       # Maven dependencies
```

---

## 🛠️ Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Java | 17 |
| Build Tool | Maven | 3.9+ |
| UI Framework | Swing + FlatLaf | 3.2.5 |
| Database | Microsoft SQL Server | 2019+ |
| JDBC Driver | mssql-jdbc | 12.4.2 |

---

## 📖 Cara Penggunaan

### 1. Tambah Pesanan
1. Klik menu yang diinginkan (Kopi/Makanan)
2. Masukkan jumlah
3. Item masuk ke keranjang

### 2. Proses Pembayaran
1. Klik "Bayar & Proses Pesanan"
2. Pilih metode pembayaran (Cash/QRIS/Transfer)
3. Konfirmasi
4. Tunggu progress bar (simulasi dapur)
5. Struk pembayaran ditampilkan

### 3. Lihat Riwayat
- Klik "Lihat Riwayat Order" untuk melihat semua transaksi

---

## 🎨 Screenshots

### Main Interface
- Panel menu dengan 8 item (Coffee & Food)
- Keranjang belanja real-time
- Ringkasan pembayaran (Total, Diskon, Grand Total)

### Features
- Custom icons (no emoji dependency)
- Progress bar untuk simulasi dapur
- Sales report hari ini
- Struk pembayaran lengkap

---

## 🐛 Troubleshooting

### Error: "The TCP/IP connection failed"
**Solusi:**
- Pastikan SQL Server service running
- Check di Services → SQL Server (MSSQLSERVER) → Start

### Error: "Login failed for user"
**Solusi:**
- Gunakan SQL Server Authentication
- Set `db.integratedSecurity=false` di `db.properties`
- Isi username dan password yang benar

### Error: "Cannot find the database CafeFlowDB"
**Solusi:**
- Jalankan `setup_quick.sql` di SSMS

### Error: "CHECK constraint violation"
**Solusi:**
- Jalankan `fix_constraint.sql` di SSMS

**Dokumentasi lengkap:** Lihat `SETUP_DATABASE.md` dan `PERFORMANCE_FIX.md`

---

## 📊 Database Schema

### Table: `orders`
```sql
id INT PRIMARY KEY IDENTITY
order_number NVARCHAR(50) UNIQUE
order_time DATETIME2
kasir NVARCHAR(100)
customer NVARCHAR(100)
total_harga DECIMAL(18,2)
total_diskon DECIMAL(18,2)
grand_total DECIMAL(18,2)
status NVARCHAR(50)
payment_method NVARCHAR(50)
```

### Table: `order_items`
```sql
id INT PRIMARY KEY IDENTITY
order_id INT FOREIGN KEY
menu_name NVARCHAR(100)
menu_category NVARCHAR(50)
quantity INT
price DECIMAL(18,2)
subtotal DECIMAL(18,2)
```

---

## 🚀 Performance Optimizations

### Database Connection
- ✅ Fast mode dengan `encrypt=false`
- ✅ Connection timeout 3 detik
- ✅ Query timeout 5 detik
- ✅ 10x lebih cepat dari sebelumnya

### Code Quality
- ✅ Clean architecture dengan separation of concerns
- ✅ Clear comments untuk setiap method
- ✅ Efficient code structure
- ✅ 700+ lines → 550 lines (22% reduction)

---

## 📝 Development Notes

### Build Commands
```bash
# Clean build
mvn clean compile

# Run aplikasi
mvn exec:java

# Package JAR
mvn package
```

### Testing
```bash
# Run tests (jika ada)
mvn test
```

---

## ✅ Checklist Setup

- [ ] Java 17+ installed
- [ ] Maven installed
- [ ] SQL Server 2019+ installed
- [ ] SSMS installed
- [ ] SQL Server service running
- [ ] Database CafeFlowDB created (run `setup_quick.sql`)
- [ ] `db.properties` configured
- [ ] Build success (`mvn clean compile`)
- [ ] Application runs (`mvn exec:java`)

---

## 📄 License

Project ini dibuat untuk keperluan akademik - Responsi 2 PPBO.

---

## 👨‍💻 Author

**Radit Alfa Anugerah Bombing**  
NIM: L0124116  
Program Studi: [Your Program]  
Universitas: [Your University]

---

## 📮 Support

Untuk pertanyaan atau issues:
1. Cek dokumentasi di folder project
2. Review `SETUP_DATABASE.md` untuk setup
3. Review `PERFORMANCE_FIX.md` untuk troubleshooting
4. Cek `QUICKSTART.md` untuk panduan cepat

---