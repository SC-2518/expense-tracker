package com.expensetracker;

import javax.swing.SwingUtilities;

/**
 * Entry point for the Personal Expense Tracker application.
 * Launches the Swing GUI on the Event Dispatch Thread.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
