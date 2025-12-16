package com.cafeflow.gui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import java.awt.*;

/** Helper untuk membuat icon custom */
public class IconHelper {
    
    private static final int DEFAULT_SIZE = 16;
    
    private static Icon createColoredIcon(String iconName, int size, Color color) {
        try {
            FlatSVGIcon icon = new FlatSVGIcon("com/formdev/flatlaf/extras/icons/" + iconName + ".svg");
            icon.setColorFilter(new FlatSVGIcon.ColorFilter(tint -> color));
            if (size != DEFAULT_SIZE) {
                icon = icon.derive(size, size);
            }
            return icon;
        } catch (Exception e) {
            return createTextIcon(getIconChar(iconName), size, color);
        }
    }
    
    private static Icon createTextIcon(String symbol, int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setFont(new Font("Segoe UI Symbol", Font.BOLD, size));
                
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(symbol);
                int textAscent = fm.getAscent();
                
                g2.drawString(symbol, x + (size - textWidth) / 2, y + (size + textAscent) / 2 - 2);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }
    
    private static String getIconChar(String iconName) {
        return switch (iconName) {
            case "coffee" -> "☕";
            case "food" -> "🍽";
            case "creditCard" -> "💳";
            case "delete" -> "🗑";
            case "chart" -> "📊";
            case "money" -> "💵";
            case "smartphone" -> "📱";
            case "bank" -> "🏦";
            case "check" -> "C";
            case "close" -> "X";
            default -> "•";
        };
    }
    
    public static Icon getCoffeeIcon() {
        return createTextIcon("☕", 18, new Color(139, 69, 19));
    }
    
    public static Icon getFoodIcon() {
        return createTextIcon("🍽", 18, new Color(70, 130, 180));
    }
    
    public static Icon getPaymentIcon() {
        return createTextIcon("💳", DEFAULT_SIZE, new Color(46, 204, 113));
    }
    
    public static Icon getTrashIcon() {
        return createTextIcon("🗑", DEFAULT_SIZE, new Color(231, 76, 60));
    }
    
    public static Icon getChartIcon() {
        return createTextIcon("📊", DEFAULT_SIZE, new Color(52, 152, 219));
    }
    
    public static Icon getCloseIcon() {
        return createTextIcon("✕", DEFAULT_SIZE, new Color(231, 76, 60));
    }
    
    public static Icon getCashIcon() {
        return createTextIcon("💵", DEFAULT_SIZE, new Color(39, 174, 96));
    }
    
    public static Icon getQRIcon() {
        return createTextIcon("📱", DEFAULT_SIZE, new Color(52, 152, 219));
    }
    
    public static Icon getBankIcon() {
        return createTextIcon("🏦", DEFAULT_SIZE, new Color(155, 89, 182));
    }
}
