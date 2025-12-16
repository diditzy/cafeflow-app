# 🖥️ Multi-OS Setup Guide - CafeFlow App

## 📋 Overview
CafeFlow App sekarang mendukung **multi-platform** (Windows, Linux, macOS) dengan sistem konfigurasi database yang fleksibel dan aman.

---

## 🔧 Cara Kerja

### Prioritas Loading Konfigurasi
Aplikasi akan otomatis memuat file konfigurasi dengan urutan prioritas:

1. **`db-local.properties`** *(tertinggi)* - Override lokal (tidak di-commit ke Git)
2. **`db-windows.properties`** atau **`db-linux.properties`** - Sesuai OS yang terdeteksi
3. **`db.properties`** *(terendah)* - Fallback/template

---

## 🪟 Windows Users

### Langkah Setup:
1. **Clone repository:**
   ```bash
   git clone https://github.com/diditzy/cafeflow-app2.git
   cd cafeflow-app2
   ```

2. **Konfigurasi sudah siap!**  
   File `db-windows.properties` sudah di-commit dengan konfigurasi default:
   - Server: `localhost:1433`
   - Database: `CafeFlowDB`
   - User: `cafeflow_admin`
   - Password: `CafeFlow2024!`

3. **Setup SQL Server:**
   - Jalankan SQL Server Management Studio (SSMS)
   - Execute script: `src/main/resources/database_setup.sql`
   - Atau gunakan command line:
     ```bash
     sqlcmd -S localhost -U cafeflow_admin -P CafeFlow2024! -i src\main\resources\database_setup.sql
     ```

4. **Compile & Run:**
   ```bash
   mvn clean compile
   mvn exec:java
   ```

### Override untuk Development Lokal:
Jika perlu ubah konfigurasi tanpa mengubah file yang di-commit:

1. Copy template:
   ```bash
   copy src\main\resources\db-local.properties.example src\main\resources\db-local.properties
   ```

2. Edit `db-local.properties` sesuai kebutuhan

3. File ini **tidak akan di-commit** ke Git (sudah ada di `.gitignore`)

---

## 🐧 Linux/macOS Users

### Langkah Setup:

1. **Clone repository:**
   ```bash
   git clone https://github.com/diditzy/cafeflow-app2.git
   cd cafeflow-app2
   ```

2. **Setup SQL Server di Docker:**
   ```bash
   docker run -e "ACCEPT_EULA=Y" \
              -e "MSSQL_SA_PASSWORD=CafeFlow2025!" \
              -p 1433:1433 \
              --name cafeflow-sqlserver \
              -d mcr.microsoft.com/mssql/server:2022-latest
   ```

3. **Copy dan edit konfigurasi:**
   ```bash
   cp src/main/resources/db-linux.properties.example src/main/resources/db-linux.properties
   ```
   
   Edit `db-linux.properties`:
   ```properties
   db.server=localhost
   db.port=1433
   db.database=CafeFlowDB
   db.username=sa
   db.password=CafeFlow2025!
   db.integratedSecurity=false
   db.encrypt=false
   db.trustServerCertificate=true
   ```

4. **Setup Database:**
   ```bash
   # Install sqlcmd tools (jika belum ada)
   # Ubuntu/Debian:
   curl https://packages.microsoft.com/keys/microsoft.asc | sudo apt-key add -
   sudo add-apt-repository "$(wget -qO- https://packages.microsoft.com/config/ubuntu/20.04/prod.list)"
   sudo apt-get update
   sudo apt-get install -y mssql-tools unixodbc-dev
   
   # Run database setup
   /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "CafeFlow2025!" -i src/main/resources/database_setup.sql
   ```

5. **Compile & Run:**
   ```bash
   mvn clean compile
   mvn exec:java
   ```

---

## 🔐 Security Best Practices

### ✅ DO:
- Gunakan `db-local.properties` untuk konfigurasi personal
- Ganti password default sebelum production
- Gunakan environment variables untuk credentials di production
- Review `.gitignore` sebelum commit

### ❌ DON'T:
- Jangan commit `db-local.properties`
- Jangan taruh password production di Git
- Jangan share credentials di chat/email

---

## 🐛 Troubleshooting

### Problem: "Cannot connect to database"
**Solution:**
1. Cek apakah SQL Server sudah running:
   - Windows: Services → SQL Server (MSSQLSERVER)
   - Linux: `docker ps | grep cafeflow`

2. Test koneksi:
   ```bash
   # Windows
   sqlcmd -S localhost -U cafeflow_admin -P CafeFlow2024!
   
   # Linux/Docker
   docker exec -it cafeflow-sqlserver /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "CafeFlow2025!"
   ```

3. Verifikasi file config dimuat:
   - Lihat output console saat aplikasi start
   - Harus muncul: `✓ Loading config: db-xxx.properties`

### Problem: "Login failed for user"
**Solution:**
1. Pastikan user sudah dibuat (jalankan `database_setup.sql`)
2. Verifikasi password di file properties
3. Cek SQL Server Authentication enabled:
   - SSMS → Server Properties → Security → SQL Server and Windows Authentication mode

### Problem: "Config file not found"
**Solution:**
1. Pastikan file ada di `src/main/resources/`
2. Rebuild project: `mvn clean compile`
3. Cek output console untuk tau file mana yang dicoba dimuat

---

## 📊 Deteksi OS dan Config Loading

Saat aplikasi start, akan muncul log seperti:

```
✓ Loading config: db-windows.properties (OS: windows 10)
  Auth: SQL Server Authentication (User: cafeflow_admin)
  Database: CafeFlowDB @ localhost:1433
```

Atau di Linux:
```
✓ Loading config: db-linux.properties (OS: linux)
  Auth: SQL Server Authentication (User: sa)
  Database: CafeFlowDB @ localhost:1433
```

Dengan local override:
```
✓ Loading config: db-local.properties (Local Override)
  Auth: SQL Server Authentication (User: my_custom_user)
  Database: CafeFlowDB @ my-server:1433
```

---

## 🔄 Migration dari Versi Lama

Jika sudah punya `db.properties` dengan konfigurasi lama:

1. **Backup dulu:**
   ```bash
   copy src\main\resources\db.properties src\main\resources\db.properties.backup
   ```

2. **Copy ke file OS-specific:**
   ```bash
   # Windows
   copy src\main\resources\db.properties src\main\resources\db-local.properties
   
   # Linux
   cp src/main/resources/db.properties src/main/resources/db-local.properties
   ```

3. **Edit sesuai kebutuhan** dan aplikasi akan otomatis pakai `db-local.properties`

---

## 📚 File Structure

```
src/main/resources/
├── db.properties                    # Template/info (jangan edit)
├── db-windows.properties            # Default Windows config (di-commit)
├── db-linux.properties              # Template Linux config (di-commit)
├── db-local.properties.example      # Example local override
├── db-local.properties              # Your local override (GITIGNORED)
└── database_setup.sql               # Database initialization script
```

---

## 💡 Tips

1. **Team Development:**
   - Setiap developer buat `db-local.properties` sendiri
   - Tidak akan conflict karena di-gitignore
   - Default config (db-windows/linux) tetap sync di Git

2. **Production Deployment:**
   - Gunakan environment variables
   - Atau mount `db-local.properties` dari secrets manager
   - Jangan hardcode credentials

3. **Testing:**
   - Buat `db-test.properties` untuk testing database
   - Copy ke `db-local.properties` saat run tests
   - Rollback setelah testing selesai

---

**Happy Coding! 🚀**
