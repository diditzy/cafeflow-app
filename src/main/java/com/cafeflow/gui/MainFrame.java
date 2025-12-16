package com.cafeflow.gui;

import com.cafeflow.model.Coffee;
import com.cafeflow.model.DatabaseManager;
import com.cafeflow.model.Discountable;
import com.cafeflow.model.Food;
import com.cafeflow.model.MenuItem;
import com.cafeflow.model.Order;
import com.cafeflow.model.OrderItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** 
 * Aplikasi CafeFlow POS - Kasir Cafe Modern
 * @author L0124116 - Radit Alfa Anugerah Bombing
 */
public class MainFrame extends JFrame {
    
    // Komponen GUI
    private JTable tableCart;
    private DefaultTableModel tableModel;
    private JLabel lblTotal, lblDiskon, lblGrandTotal;
    private JProgressBar progressBar;
    private JLabel lblStatus, lblDateTime, lblSales;
    private JTextField txtKasir, txtCustomer;
    private JButton btnBayar, btnClearCart, btnHistory;
    
    // Data dan logika bisnis
    private Order currentOrder;
    private DatabaseManager dbManager;
    private KitchenTask currentTask;
    
    public MainFrame() {
        dbManager = DatabaseManager.getInstance();
        initNewOrder();
        setupWindow();
        buildUI();
        startClock();
        setLocationRelativeTo(null);
    }
    
    private void setupWindow() {
        setTitle("CafeFlow POS System - L0124116 - Radit Alfa Anugerah Bombing");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
    }
    
    private void buildUI() {
        createTopPanel();
        createMenuPanel();
        createCartPanel();
        createBottomPanel();
    }
    
    private void initNewOrder() {
        String kasir = (txtKasir != null) ? txtKasir.getText() : "Kasir 1";
        String customer = (txtCustomer != null) ? txtCustomer.getText() : "Walk-in Customer";
        currentOrder = new Order(kasir, customer);
    }
    
    // Panel atas - Info kasir, customer, tanggal & sales
    private void createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        infoPanel.add(new JLabel("Kasir:"));
        txtKasir = new JTextField("Kasir 1", 15);
        infoPanel.add(txtKasir);
        
        infoPanel.add(new JLabel("Pelanggan:"));
        txtCustomer = new JTextField("Walk-in Customer", 15);
        infoPanel.add(txtCustomer);
        
        JPanel rightPanel = new JPanel(new GridLayout(2, 1));
        lblDateTime = new JLabel("", SwingConstants.RIGHT);
        lblDateTime.setFont(new Font("Arial", Font.BOLD, 12));
        
        lblSales = new JLabel("", SwingConstants.RIGHT);
        lblSales.setFont(new Font("Arial", Font.PLAIN, 11));
        updateSalesToday();
        
        rightPanel.add(lblDateTime);
        rightPanel.add(lblSales);
        
        topPanel.add(infoPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
    }
    
    // Panel menu cafe
    private void createMenuPanel() {
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBorder(BorderFactory.createTitledBorder("Menu Cafe"));
        
        JPanel gridPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        MenuItem[] menuItems = {
            new Coffee("Kopi Latte", 25000, "Normal"),
            new Coffee("Cappuccino", 28000, "Normal"),
            new Coffee("Es Kopi Susu", 22000, "Normal"),
            new Coffee("Americano", 20000, "Normal"),
            new Food("Nasi Goreng", 30000, false),
            new Food("Mie Goreng", 28000, false),
            new Food("Kentang Goreng", 18000, false),
            new Food("Roti Bakar", 15000, false)
        };
        
        for (MenuItem item : menuItems) {
            JButton btn = createMenuButton(item);
            gridPanel.add(btn);
        }
        
        menuPanel.add(gridPanel, BorderLayout.CENTER);
        add(menuPanel, BorderLayout.WEST);
    }
    
    private JButton createMenuButton(MenuItem item) {
        String displayText = String.format("<html><center>%s<br>Rp %,.0f", 
            item.getNama(), item.getHarga());
        
        if (item instanceof Discountable) {
            Discountable d = (Discountable) item;
            if (d.getPersenDiskon() > 0) {
                displayText += String.format("<br><font color='red'>(Diskon %.0f%%)</font>", 
                    d.getPersenDiskon());
            }
        }
        displayText += "</center></html>";
        
        JButton btn = new JButton(displayText);
        
        if (item instanceof Coffee) {
            btn.setIcon(IconHelper.getCoffeeIcon());
        } else {
            btn.setIcon(IconHelper.getFoodIcon());
        }
        
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setPreferredSize(new Dimension(180, 80));
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.addActionListener(e -> addToCart(item));
        
        return btn;
    }
    
    // Panel keranjang belanja
    private void createCartPanel() {
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Keranjang Belanja"));
        
        String[] columnNames = {"No", "Nama Menu", "Kategori", "Qty", "Harga", "Subtotal"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableCart = new JTable(tableModel);
        tableCart.setRowHeight(25);
        tableCart.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableCart.getColumnModel().getColumn(1).setPreferredWidth(150);
        tableCart.getColumnModel().getColumn(2).setPreferredWidth(80);
        tableCart.getColumnModel().getColumn(3).setPreferredWidth(50);
        
        JScrollPane scrollPane = new JScrollPane(tableCart);
        cartPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel summaryPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblTotal = new JLabel("Total: Rp 0", SwingConstants.RIGHT);
        lblDiskon = new JLabel("Diskon: Rp 0", SwingConstants.RIGHT);
        lblGrandTotal = new JLabel("Grand Total: Rp 0", SwingConstants.RIGHT);
        
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblDiskon.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDiskon.setForeground(Color.RED);
        lblGrandTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblGrandTotal.setForeground(new Color(34, 139, 34));
        
        summaryPanel.add(lblTotal);
        summaryPanel.add(lblDiskon);
        summaryPanel.add(lblGrandTotal);
        
        cartPanel.add(summaryPanel, BorderLayout.SOUTH);
        add(cartPanel, BorderLayout.CENTER);
    }
    
    // Panel tombol dan progress bar
    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        
        btnBayar = new JButton("Bayar & Proses Pesanan", IconHelper.getPaymentIcon());
        btnClearCart = new JButton("Bersihkan Keranjang", IconHelper.getTrashIcon());
        btnHistory = new JButton("Lihat Riwayat Order", IconHelper.getChartIcon());
        
        btnBayar.setBackground(new Color(34, 139, 34));
        btnBayar.setForeground(Color.WHITE);
        btnBayar.setFont(new Font("Arial", Font.BOLD, 14));
        
        btnClearCart.setBackground(new Color(220, 53, 69));
        btnClearCart.setForeground(Color.WHITE);
        
        btnHistory.setBackground(new Color(0, 123, 255));
        btnHistory.setForeground(Color.WHITE);
        
        btnBayar.addActionListener(e -> processPayment());
        btnClearCart.addActionListener(e -> clearCart());
        btnHistory.addActionListener(e -> showOrderHistory());
        
        buttonPanel.add(btnBayar);
        buttonPanel.add(btnClearCart);
        buttonPanel.add(btnHistory);
        
        JPanel statusPanel = new JPanel(new BorderLayout(5, 5));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        
        lblStatus = new JLabel("Status: Siap", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 11));
        
        statusPanel.add(progressBar, BorderLayout.CENTER);
        statusPanel.add(lblStatus, BorderLayout.SOUTH);
        
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    // Tambah item ke keranjang
    private void addToCart(MenuItem item) {
        String qtyStr = JOptionPane.showInputDialog(
            this,
            "Masukkan jumlah untuk " + item.getNama() + ":",
            "Tambah ke Keranjang",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (qtyStr != null && !qtyStr.trim().isEmpty()) {
            try {
                int qty = Integer.parseInt(qtyStr.trim());
                if (qty > 0) {
                    currentOrder.addItem(item, qty);
                    updateCartDisplay();
                } else {
                    showError("Jumlah harus lebih dari 0!");
                }
            } catch (NumberFormatException ex) {
                showError("Jumlah harus berupa angka!");
            }
        }
    }
    
    // Update tampilan keranjang
    private void updateCartDisplay() {
        tableModel.setRowCount(0);
        
        int no = 1;
        for (OrderItem item : currentOrder.getItems()) {
            tableModel.addRow(new Object[]{
                no++,
                item.getMenu().getNama(),
                item.getMenu().getKategori(),
                item.getQuantity(),
                String.format("Rp %,d", (int)item.getMenu().getHarga()),
                String.format("Rp %,d", (int)item.getSubtotal())
            });
        }
        
        lblTotal.setText(String.format("Total: Rp %,d", (int)currentOrder.getTotalHarga()));
        lblDiskon.setText(String.format("Diskon: Rp %,d", (int)currentOrder.getTotalDiskon()));
        lblGrandTotal.setText(String.format("Grand Total: Rp %,d", (int)currentOrder.getGrandTotal()));
    }
    
    // Proses pembayaran
    private void processPayment() {
        if (currentOrder.getItems().isEmpty()) {
            showError("Keranjang masih kosong!");
            return;
        }
        
        String kasir = txtKasir.getText().trim();
        String customer = txtCustomer.getText().trim();
        
        if (kasir.isEmpty()) {
            showError("Nama kasir tidak boleh kosong!");
            return;
        }
        if (customer.isEmpty()) {
            showError("Nama pelanggan tidak boleh kosong!");
            return;
        }
        
        Order orderToProcess = new Order(kasir, customer);
        
        for (OrderItem item : currentOrder.getItems()) {
            orderToProcess.addItem(item.getMenu(), item.getQuantity());
        }
        
        String[] paymentOptions = {"CASH", "QRIS", "TRANSFER"};
        String[] paymentLabels = {
            "Cash - Tunai",
            "QRIS - Scan QR Code",
            "Transfer Bank"
        };
        
        int paymentChoice = JOptionPane.showOptionDialog(
            this,
            "Pilih metode pembayaran:",
            "Metode Pembayaran",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            paymentLabels,
            paymentLabels[0]
        );
        
        if (paymentChoice == -1) return;
        
        orderToProcess.setPaymentMethod(paymentOptions[paymentChoice]);
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            buildPaymentConfirmation(orderToProcess),
            "Konfirmasi Pembayaran",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            saveAndProcessOrder(orderToProcess);
        }
    }
    
    private String buildPaymentConfirmation(Order order) {
        return String.format(
            "Konfirmasi Pembayaran:\n\n" +
            "Kasir: %s\n" +
            "Pelanggan: %s\n" +
            "Total Item: %d\n" +
            "Subtotal: Rp %,d\n" +
            "Diskon: Rp %,d\n" +
            "Grand Total: Rp %,d\n" +
            "Metode Bayar: %s\n\n" +
            "Proses pembayaran ini?",
            order.getKasir(),
            order.getCustomer(),
            order.getItems().size(),
            (int)order.getTotalHarga(),
            (int)order.getTotalDiskon(),
            (int)order.getGrandTotal(),
            order.getPaymentMethod()
        );
    }
    
    // Simpan order dan jalankan proses dapur
    private void saveAndProcessOrder(Order order) {
        order.setStatus("PROCESSING");
        
        boolean saved = dbManager.saveOrder(order);
        
        if (saved) {
            btnBayar.setEnabled(false);
            
            currentTask = new KitchenTask(
                progressBar,
                lblStatus,
                order.getOrderNumber(),
                () -> {
                    dbManager.updateOrderStatus(order.getOrderNumber(), "COMPLETED");
                    btnBayar.setEnabled(true);
                    
                    printReceipt(order);
                    clearCart();
                    updateSalesToday();
                }
            );
            currentTask.start();
            
        } else {
            showError("Gagal menyimpan order ke database!");
        }
    }
    
    // Tampilkan struk pembayaran
    private void printReceipt(Order order) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("========================================\n");
        receipt.append("       CAFEFLOW - STRUK PEMBAYARAN      \n");
        receipt.append("========================================\n");
        receipt.append(String.format("No Order : %s\n", order.getOrderNumber()));
        receipt.append(String.format("Tanggal  : %s\n", order.getFormattedOrderTime()));
        receipt.append(String.format("Kasir    : %s\n", order.getKasir()));
        receipt.append(String.format("Customer : %s\n", order.getCustomer()));
        receipt.append("========================================\n");
        receipt.append("ITEM PESANAN:\n");
        receipt.append("========================================\n");
        
        for (OrderItem item : order.getItems()) {
            receipt.append(String.format("%-22s\n", item.getMenu().getNama()));
            receipt.append(String.format("  %dx @ Rp %,10.0f = Rp %,10.0f\n",
                item.getQuantity(),
                item.getMenu().getHarga(),
                item.getSubtotal()
            ));
            
            if (item.getMenu() instanceof Discountable) {
                Discountable d = (Discountable) item.getMenu();
                if (d.getPersenDiskon() > 0) {
                    double itemDiskon = d.hitungDiskon() * item.getQuantity();
                    receipt.append(String.format("  (Diskon %.0f%% - Rp %,10.0f)\n", 
                        d.getPersenDiskon(), itemDiskon));
                }
            }
        }
        
        receipt.append("========================================\n");
        receipt.append(String.format("Subtotal       :     Rp %,11.0f\n", order.getTotalHarga()));
        receipt.append(String.format("Diskon         :     Rp %,11.0f\n", order.getTotalDiskon()));
        receipt.append("----------------------------------------\n");
        receipt.append(String.format("TOTAL BAYAR    :     Rp %,11.0f\n", order.getGrandTotal()));
        receipt.append("========================================\n");
        receipt.append(String.format("Metode Bayar   : %s\n", order.getPaymentMethod()));
        receipt.append("========================================\n");
        receipt.append("    Terima kasih atas kunjungan Anda!  \n");
        receipt.append("      Selamat menikmati hidangan!      \n");
        receipt.append("========================================\n");
        
        JTextArea textArea = new JTextArea(receipt.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 500));
        
        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Struk Pembayaran - " + order.getOrderNumber(),
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    // Bersihkan keranjang
    private void clearCart() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin mengosongkan keranjang?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            initNewOrder();
            updateCartDisplay();
        }
    }
    
    // Tampilkan riwayat order
    private void showOrderHistory() {
        java.util.List<String[]> orders = dbManager.getAllOrders();
        
        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Belum ada histori pesanan");
            return;
        }
        
        String[] columns = {"No Order", "Tanggal", "Kasir", "Customer", "Total", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        for (String[] orderData : orders) {
            model.addRow(new Object[]{
                orderData[0],
                orderData[1],
                orderData[2],
                orderData[3],
                String.format("Rp %,.0f", Double.parseDouble(orderData[4])),
                orderData[5]
            });
        }
        
        JTable table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        
        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Riwayat Pesanan",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void updateSalesToday() {
        double sales = dbManager.getTodaySales();
        lblSales.setText(String.format("Penjualan Hari Ini: Rp %,d", (int)sales));
    }
    
    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            lblDateTime.setText(now.format(formatter));
        });
        timer.start();
        
        Timer salesTimer = new Timer(10000, e -> updateSalesToday());
        salesTimer.start();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
