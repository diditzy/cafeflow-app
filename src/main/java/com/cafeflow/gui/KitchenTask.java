package com.cafeflow.gui;

import javax.swing.*;

/** Simulasi proses memasak di dapur (Multithreading) */
public class KitchenTask extends Thread {
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private String orderNumber;
    private Runnable onComplete;

    public KitchenTask(JProgressBar progressBar, JLabel statusLabel, String orderNumber) {
        this.progressBar = progressBar;
        this.statusLabel = statusLabel;
        this.orderNumber = orderNumber;
    }
    
    public KitchenTask(JProgressBar progressBar, JLabel statusLabel, String orderNumber, Runnable onComplete) {
        this.progressBar = progressBar;
        this.statusLabel = statusLabel;
        this.orderNumber = orderNumber;
        this.onComplete = onComplete;
    }

    @Override
    public void run() {
        try {
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Status: Pesanan diterima dapur...");
                progressBar.setValue(0);
                progressBar.setString("0%");
            });
            
            Thread.sleep(1000);
            
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Status: Menyiapkan bahan-bahan...");
                progressBar.setValue(20);
                progressBar.setString("20%");
            });

            Thread.sleep(1500);
            
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Status: Sedang meracik minuman...");
                progressBar.setValue(40);
                progressBar.setString("40%");
            });
            
            Thread.sleep(1500);
            
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Status: Memasak makanan...");
                progressBar.setValue(60);
                progressBar.setString("60%");
            });

            Thread.sleep(2000);
            
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Status: Finishing touches...");
                progressBar.setValue(80);
                progressBar.setString("80%");
            });
            
            Thread.sleep(1000);
            
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Status: Pesanan Siap Disajikan!");
                progressBar.setValue(100);
                progressBar.setString("100% - Selesai");
            });
            
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                    null,
                    "Pesanan " + orderNumber + " Selesai!\n" +
                    "Silakan ambil di counter.",
                    "Pesanan Siap",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                if (onComplete != null) {
                    onComplete.run();
                }
            });
            
        } catch (InterruptedException e) {
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Status: Proses dibatalkan!");
                JOptionPane.showMessageDialog(
                    null,
                    "Proses memasak dibatalkan!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            });
            e.printStackTrace();
        }
    }
    
    /**
     * Method untuk menghentikan thread dengan aman
     */
    public void stopTask() {
        this.interrupt();
    }
}
