# 🚀 Quick Start - CafeFlow

## Langkah Cepat

### 1. Setup Database
```
Buka SSMS → Jalankan: database_setup.sql
```

### 2. Jalankan Aplikasi

**Menggunakan Maven:**
```bash
cd "project-folder"
mvn clean compile
mvn exec:java
```

**Menggunakan IDE:**
- IntelliJ: Klik kanan MainApp.java → Run
- Eclipse: Right-click MainApp.java → Run As → Java Application
- NetBeans: Right-click MainApp.java → Run File

---

## ✅ Requirements

- Java 17+
- Maven 3.9+
- SQL Server 2019+
- SSMS

---

## 🧪 Test

```bash
mvn test
```

Expected output:
```
Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## 📁 File Penting

| File | Fungsi |
|------|--------|
| `pom.xml` | Maven configuration |
| `MainApp.java` | Entry point aplikasi |
| `MainFrame.java` | GUI utama |
| `OrderTest.java` | Unit testing |
| `cafeflow.db` | Database (auto-generated) |

---

## ⚡ Shortcut

**Compile + Run (1 command):**
```bash
mvn clean compile exec:java -Dexec.mainClass="com.cafeflow.MainApp"
```

**Clean + Test + Run:**
```bash
mvn clean test && mvn exec:java -Dexec.mainClass="com.cafeflow.MainApp"
```

---

## 🆘 Emergency Help

**Error saat compile?**
```bash
mvn clean install -U
```

**Database error?**
```bash
del cafeflow.db  # Windows
rm cafeflow.db   # Linux/Mac
```

**Dependencies error?**
```bash
mvn dependency:purge-local-repository
mvn clean install
```

---

## 📞 Support

Jika ada masalah:
1. Baca `TUTORIAL.md` untuk troubleshooting detail
2. Baca `README.md` untuk dokumentasi lengkap
3. Check error di console/terminal

---

**Selamat Menggunakan CafeFlow! ☕**
