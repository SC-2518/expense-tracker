package com.expensetracker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Modal dialog for adding or editing an Expense.
 * Reusable for both Add and Edit operations.
 */
public class ExpenseFormDialog extends JDialog {

    private JTextField titleField;
    private JTextField amountField;
    private JComboBox<String> categoryBox;
    private JTextField dateField;
    private JTextField noteField;

    private boolean confirmed = false;

    private static final String[] CATEGORIES = {
        "Food", "Transport", "Shopping", "Entertainment",
        "Health", "Education", "Utilities", "Rent", "Other"
    };

    public ExpenseFormDialog(Frame parent, String dialogTitle, Expense existing) {
        super(parent, dialogTitle, true);
        setSize(420, 340);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(16, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // --- Title ---
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Title:"), gbc);
        titleField = new JTextField(20);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(titleField, gbc);

        // --- Amount ---
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Amount (₹):"), gbc);
        amountField = new JTextField(20);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(amountField, gbc);

        // --- Category ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Category:"), gbc);
        categoryBox = new JComboBox<>(CATEGORIES);
        categoryBox.setEditable(true);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(categoryBox, gbc);

        // --- Date ---
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        dateField = new JTextField(20);
        dateField.setText(LocalDate.now().toString());
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(dateField, gbc);

        // --- Note ---
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Note:"), gbc);
        noteField = new JTextField(20);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(noteField, gbc);

        // --- Buttons ---
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(new Color(52, 152, 219));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);

        JButton cancelBtn = new JButton("Cancel");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);

        // Pre-fill if editing
        if (existing != null) {
            titleField.setText(existing.getTitle());
            amountField.setText(String.valueOf(existing.getAmount()));
            categoryBox.setSelectedItem(existing.getCategory());
            dateField.setText(existing.getDate().toString());
            noteField.setText(existing.getNote());
        }

        saveBtn.addActionListener(e -> {
            if (validateInputs()) {
                confirmed = true;
                dispose();
            }
        });
        cancelBtn.addActionListener(e -> dispose());

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private boolean validateInputs() {
        if (titleField.getText().trim().isEmpty()) {
            showError("Title cannot be empty.");
            return false;
        }
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showError("Please enter a valid positive amount.");
            return false;
        }
        try {
            LocalDate.parse(dateField.getText().trim());
        } catch (DateTimeParseException e) {
            showError("Date must be in YYYY-MM-DD format.");
            return false;
        }
        return true;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isConfirmed() { return confirmed; }

    public String getExpenseTitle()    { return titleField.getText().trim(); }
    public double getExpenseAmount()   { return Double.parseDouble(amountField.getText().trim()); }
    public String getExpenseCategory() { return ((String) categoryBox.getSelectedItem()).trim(); }
    public LocalDate getExpenseDate()  { return LocalDate.parse(dateField.getText().trim()); }
    public String getExpenseNote()     { return noteField.getText().trim(); }
}
