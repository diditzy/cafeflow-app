package com.cafeflow;

import com.cafeflow.gui.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

/** 
 * CafeFlow POS System
 * @author L0124116 - Radit Alfa Anugerah Bombing
 */
public class MainApp {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf: " + ex.getMessage());
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("==============================================");
                System.out.println("   CafeFlow POS System - Starting...         ");
                System.out.println("   Developed by: Radit Alfa Anugerah Bombing ");
                System.out.println("   NIM: L0124116                             ");
                System.out.println("==============================================");
                
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
