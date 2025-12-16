package com.cafeflow;

import com.cafeflow.gui.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

/**
 * Entry Point untuk aplikasi CafeFlow POS System.
 * Penerapan materi: Application Entry Point, Exception Handling.
 * 
 * @author Radit Alfa Anugerah Bombing - L0124116
 * @version 1.0
 */
public class MainApp {
    
    public static void main(String[] args) {
        // Setup Look and Feel modern menggunakan FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            
            // Customize UI properties
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf Look and Feel: " + ex.getMessage());
            // Fallback ke default Look and Feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Jalankan aplikasi di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("==============================================");
                System.out.println("   CafeFlow POS System - Starting...         ");
                System.out.println("   Developed by: Radit Alfa Anugerah Bombing ");
                System.out.println("   NIM: L0124116                             ");
                System.out.println("==============================================");
                
                // Buat dan tampilkan Main Frame
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                
                System.out.println("Application started successfully!");
                
            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();
                
                JOptionPane.showMessageDialog(
                    null,
                    "Terjadi kesalahan saat memulai aplikasi:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                
                System.exit(1);
            }
        });
    }
}
