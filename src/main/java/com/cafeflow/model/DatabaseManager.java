package com.cafeflow.model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Kelas untuk manajemen database SQL Server (SSMS).
 * Penerapan materi: Database Connection, CRUD Operations, Exception Handling.
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private String connectionUrl;
    
    private DatabaseManager() {
        loadConfiguration();
        initDatabase();
    }
    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Load konfigurasi database dari file properties
     */
    private void loadConfiguration() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.out.println("⚠️ File db.properties tidak ditemukan, menggunakan default SQL Server...");
                useDefaultConfiguration();
                return;
            }
            
            props.load(input);
            
            String server = props.getProperty("db.server", "localhost");
            String port = props.getProperty("db.port", "1433");
            String database = props.getProperty("db.database", "CafeFlowDB");
            String username = props.getProperty("db.username", "");
            String password = props.getProperty("db.password", "");
            String integratedSecurity = props.getProperty("db.integratedSecurity", "true");
            String encrypt = props.getProperty("db.encrypt", "false"); // false untuk speed
            String trustServerCertificate = props.getProperty("db.trustServerCertificate", "true");
            
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("jdbc:sqlserver://").append(server).append(":").append(port)
                      .append(";databaseName=").append(database)
                      .append(";encrypt=").append(encrypt)
                      .append(";trustServerCertificate=").append(trustServerCertificate)
                      .append(";loginTimeout=3")           // 3 detik timeout untuk login
                      .append(";socketTimeout=5000");      // 5 detik timeout untuk query
            
            if (integratedSecurity.equalsIgnoreCase("true")) {
                urlBuilder.append(";integratedSecurity=true");
                System.out.println("✓ Using Windows Authentication (Fast Mode)");
            } else if (!username.isEmpty()) {
                urlBuilder.append(";user=").append(username)
                          .append(";password=").append(password);
                System.out.println("✓ Using SQL Server Authentication (Fast Mode)");
            }
            
            connectionUrl = urlBuilder.toString();
            System.out.println("✓ Database: " + database + " on " + server);
            
        } catch (IOException e) {
            System.err.println("⚠️ Error loading configuration: " + e.getMessage());
            useDefaultConfiguration();
        }
    }
    
    /**
     * Gunakan konfigurasi default SQL Server (OPTIMIZED)
     */
    private void useDefaultConfiguration() {
        // Default: Windows Authentication ke localhost (FAST MODE)
        connectionUrl = "jdbc:sqlserver://localhost:1433;" +
                       "databaseName=CafeFlowDB;" +
                       "integratedSecurity=true;" +
                       "encrypt=false;" +              // Disable encryption untuk speed
                       "trustServerCertificate=true;" +
                       "loginTimeout=3;" +             // 3 detik max untuk login
                       "socketTimeout=5000;";          // 5 detik max untuk query
        System.out.println("✓ Using default: localhost\\CafeFlowDB (Windows Auth - Fast Mode)");
    }
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl);
    }
    
    /**
     * Inisialisasi database dan tabel-tabel yang diperlukan
     */
    private void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // SQL Server syntax
            String createOrdersTable = """
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
                    )
                END
            """;
            stmt.execute(createOrdersTable);
            
            String createOrderItemsTable = """
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
                        FOREIGN KEY (order_id) REFERENCES orders(id)
                    )
                END
            """;
            stmt.execute(createOrderItemsTable);
            
            System.out.println("✓ Database tables ready!");
            
        } catch (SQLException e) {
            System.err.println("❌ Database Error: " + e.getMessage());
            System.err.println("💡 Please check:");
            System.err.println("   1. SQL Server is running");
            System.err.println("   2. Database 'CafeFlowDB' exists");
            System.err.println("   3. Check db.properties configuration");
            e.printStackTrace();
        }
    }
    
    /**
     * Simpan order ke database
     */
    public boolean saveOrder(Order order) {
        String insertOrderSQL = """
            INSERT INTO orders (order_number, order_time, kasir, customer, 
                              total_harga, total_diskon, grand_total, status, payment_method)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        String insertItemSQL = """
            INSERT INTO order_items (order_id, menu_name, menu_category, 
                                    quantity, price, subtotal)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            
            // Insert order
            try (PreparedStatement pstmt = conn.prepareStatement(insertOrderSQL, 
                    Statement.RETURN_GENERATED_KEYS)) {
                
                pstmt.setString(1, order.getOrderNumber());
                pstmt.setString(2, order.getOrderTime().toString());
                pstmt.setString(3, order.getKasir());
                pstmt.setString(4, order.getCustomer());
                pstmt.setDouble(5, order.getTotalHarga());
                pstmt.setDouble(6, order.getTotalDiskon());
                pstmt.setDouble(7, order.getGrandTotal());
                pstmt.setString(8, order.getStatus());
                pstmt.setString(9, order.getPaymentMethod());
                
                pstmt.executeUpdate();
                
                // Get generated order ID
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int orderId = rs.getInt(1);
                    
                    // Insert order items
                    try (PreparedStatement itemStmt = conn.prepareStatement(insertItemSQL)) {
                        for (OrderItem item : order.getItems()) {
                            itemStmt.setInt(1, orderId);
                            itemStmt.setString(2, item.getMenu().getNama());
                            itemStmt.setString(3, item.getMenu().getKategori());
                            itemStmt.setInt(4, item.getQuantity());
                            itemStmt.setDouble(5, item.getMenu().getHarga());
                            itemStmt.setDouble(6, item.getSubtotal());
                            itemStmt.addBatch();
                        }
                        itemStmt.executeBatch();
                    }
                }
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error saving order: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Ambil semua order dari database
     */
    public List<String[]> getAllOrders() {
        List<String[]> orders = new ArrayList<>();
        String query = "SELECT * FROM orders ORDER BY order_time DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                String[] row = {
                    rs.getString("order_number"),
                    rs.getString("order_time"),
                    rs.getString("kasir"),
                    rs.getString("customer"),
                    String.format("%.0f", rs.getDouble("grand_total")),
                    rs.getString("status")
                };
                orders.add(row);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting orders: " + e.getMessage());
        }
        
        return orders;
    }
    
    /**
     * Ambil detail order berdasarkan order number
     */
    public List<String[]> getOrderDetails(String orderNumber) {
        List<String[]> items = new ArrayList<>();
        String query = """
            SELECT oi.* FROM order_items oi
            JOIN orders o ON oi.order_id = o.id
            WHERE o.order_number = ?
        """;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, orderNumber);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String[] row = {
                    rs.getString("menu_name"),
                    rs.getString("menu_category"),
                    String.valueOf(rs.getInt("quantity")),
                    String.format("%.0f", rs.getDouble("price")),
                    String.format("%.0f", rs.getDouble("subtotal"))
                };
                items.add(row);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting order details: " + e.getMessage());
        }
        
        return items;
    }
    
    /**
     * Update status order
     */
    public boolean updateOrderStatus(String orderNumber, String newStatus) {
        String updateSQL = "UPDATE orders SET status = ? WHERE order_number = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setString(2, orderNumber);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating order status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Dapatkan total penjualan hari ini
     */
    public double getTodaySales() {
        String query = """
            SELECT SUM(grand_total) as total 
            FROM orders 
            WHERE CAST(order_time AS DATE) = CAST(GETDATE() AS DATE)
            AND status != 'CANCELLED'
        """;
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting today's sales: " + e.getMessage());
        }
        
        return 0;
    }
}
