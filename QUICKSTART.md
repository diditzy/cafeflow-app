# 🚀 Quick Start Guide - CafeFlow

## Langkah Cepat Menjalankan Aplikasi

### Option 1: Menggunakan Maven (Recommended)

```bash
# 1. Masuk ke folder project
cd "c:\Programming Code!\Smst 3\PBO\Responsi2_PPBO_L0124116_RaditAlfaAnugerahBombing"

# 2. Compile project
mvn clean compile

# 3. Jalankan aplikasi
mvn exec:java -Dexec.mainClass="com.cafeflow.MainApp"
```

### Option 2: Menggunakan IDE

**IntelliJ IDEA:**
1. File → Open → Pilih folder project
2. Wait Maven import selesai
3. Klik kanan `MainApp.java` → Run

**Eclipse:**
1. File → Import → Existing Maven Projects
2. Browse ke folder project
3. Right-click `MainApp.java` → Run As → Java Application

**NetBeans:**
1. File → Open Project
2. Wait Maven sync
3. Right-click `MainApp.java` → Run File

---

## ✅ Checklist Sebelum Run

- [ ] Java JDK 17+ terinstall (`java -version`)
- [ ] Maven terinstall (`mvn -version`)
- [ ] Internet aktif (untuk download dependencies)
- [ ] Port 8080 tidak terpakai (untuk database)

---

## 🧪 Run Unit Tests

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
