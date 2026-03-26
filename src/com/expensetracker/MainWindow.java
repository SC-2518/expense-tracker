package com.expensetracker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Main application window.
 * Contains the expense table, filter controls, action buttons, and a summary tab.
 */
public class MainWindow extends JFrame {

    private ExpenseManager manager;
    private DefaultTableModel tableModel;
    private JTable expenseTable;
    private JComboBox<String> filterCategory;
    private JTextField filterFrom;
    private JTextField filterTo;
    private SummaryPanel summaryPanel;

    private static final String[] COLUMNS = {"ID", "Title", "Amount (₹)", "Category", "Date", "Note"};

    public MainWindow() {
        manager = new ExpenseManager();

        setTitle("Personal Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(750, 450));

        // App icon color via title bar on some OSes
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        buildUI();
        refreshTable(manager.getAllExpenses());
    }

    private void buildUI() {
        JTabbedPane tabs = new JTabbedPane();

        // ---- Tab 1: Expenses ----
        JPanel expenseTab = new JPanel(new BorderLayout(6, 6));
        expenseTab.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top: Filter bar
        expenseTab.add(buildFilterPanel(), BorderLayout.NORTH);

        // Center: Table
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        expenseTable = new JTable(tableModel);
        expenseTable.setRowHeight(26);
        expenseTable.setFont(new Font("Arial", Font.PLAIN, 12));
        expenseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseTable.setAutoCreateRowSorter(true);

        // Hide ID column visually but keep data
        expenseTable.getColumnModel().getColumn(0).setMinWidth(0);
        expenseTable.getColumnModel().getColumn(0).setMaxWidth(0);
        expenseTable.getColumnModel().getColumn(0).setWidth(0);

        expenseTab.add(new JScrollPane(expenseTable), BorderLayout.CENTER);

        // Bottom: Action buttons
        expenseTab.add(buildButtonPanel(), BorderLayout.SOUTH);
        tabs.addTab("📋 Expenses", expenseTab);

        // ---- Tab 2: Summary ----
        summaryPanel = new SummaryPanel(manager);
        tabs.addTab("📊 Summary", summaryPanel);

        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 1) summaryPanel.refresh();
        });

        add(tabs);
    }

    private JPanel buildFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Filter"));

        panel.add(new JLabel("Category:"));
        filterCategory = new JComboBox<>();
        filterCategory.addItem("All");
        filterCategory.setPreferredSize(new Dimension(120, 26));
        panel.add(filterCategory);

        panel.add(new JLabel("From:"));
        filterFrom = new JTextField(10);
        filterFrom.setToolTipText("YYYY-MM-DD");
        panel.add(filterFrom);

        panel.add(new JLabel("To:"));
        filterTo = new JTextField(10);
        filterTo.setToolTipText("YYYY-MM-DD");
        panel.add(filterTo);

        JButton applyBtn = new JButton("Apply");
        applyBtn.addActionListener(e -> applyFilters());
        panel.add(applyBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> {
            filterCategory.setSelectedIndex(0);
            filterFrom.setText("");
            filterTo.setText("");
            refreshTable(manager.getAllExpenses());
        });
        panel.add(clearBtn);

        return panel;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));

        JButton addBtn    = makeButton("➕ Add",    new Color(46, 204, 113));
        JButton editBtn   = makeButton("✏ Edit",   new Color(52, 152, 219));
        JButton deleteBtn = makeButton("🗑 Delete", new Color(231, 76, 60));

        addBtn.addActionListener(e    -> openAddDialog());
        editBtn.addActionListener(e   -> openEditDialog());
        deleteBtn.addActionListener(e -> deleteSelected());

        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
        return panel;
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        return btn;
    }

    // ---------------------------------------------------------------- Actions

    private void openAddDialog() {
        ExpenseFormDialog dialog = new ExpenseFormDialog(this, "Add Expense", null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            Expense e = new Expense(
                dialog.getExpenseTitle(),
                dialog.getExpenseAmount(),
                dialog.getExpenseCategory(),
                dialog.getExpenseDate(),
                dialog.getExpenseNote()
            );
            manager.addExpense(e);
            refreshTableAndCategories();
        }
    }

    private void openEditDialog() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        manager.getExpenseById(selectedId).ifPresent(expense -> {
            ExpenseFormDialog dialog = new ExpenseFormDialog(this, "Edit Expense", expense);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                manager.updateExpense(selectedId,
                    dialog.getExpenseTitle(),
                    dialog.getExpenseAmount(),
                    dialog.getExpenseCategory(),
                    dialog.getExpenseDate(),
                    dialog.getExpenseNote()
                );
                refreshTableAndCategories();
            }
        });
    }

    private void deleteSelected() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this expense?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            manager.deleteExpense(selectedId);
            refreshTableAndCategories();
        }
    }

    private void applyFilters() {
        String category = (String) filterCategory.getSelectedItem();
        String fromText = filterFrom.getText().trim();
        String toText   = filterTo.getText().trim();

        List<Expense> results = manager.getAllExpenses();

        if (!"All".equals(category)) {
            results = manager.getByCategory(category);
        }

        if (!fromText.isEmpty() && !toText.isEmpty()) {
            try {
                LocalDate from = LocalDate.parse(fromText);
                LocalDate to   = LocalDate.parse(toText);
                final List<Expense> temp = results;
                results = manager.getByDateRange(from, to);
                // intersect if both filters active
                if (!"All".equals(category)) {
                    results.retainAll(temp);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Date format must be YYYY-MM-DD.", "Filter Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        refreshTable(results);
    }

    // ---------------------------------------------------------------- Helpers

    private int getSelectedId() {
        int viewRow = expenseTable.getSelectedRow();
        if (viewRow == -1) return -1;
        int modelRow = expenseTable.convertRowIndexToModel(viewRow);
        return (int) tableModel.getValueAt(modelRow, 0);
    }

    private void refreshTableAndCategories() {
        // Update category filter dropdown
        filterCategory.removeAllItems();
        filterCategory.addItem("All");
        for (String cat : manager.getAllCategories()) {
            filterCategory.addItem(cat);
        }
        refreshTable(manager.getAllExpenses());
    }

    private void refreshTable(List<Expense> list) {
        tableModel.setRowCount(0);
        for (Expense e : list) {
            tableModel.addRow(new Object[]{
                e.getId(),
                e.getTitle(),
                String.format("%.2f", e.getAmount()),
                e.getCategory(),
                e.getDate().toString(),
                e.getNote()
            });
        }
        // Also update category dropdown
        String current = (String) filterCategory.getSelectedItem();
        filterCategory.removeAllItems();
        filterCategory.addItem("All");
        for (String cat : manager.getAllCategories()) {
            filterCategory.addItem(cat);
        }
        if (current != null) filterCategory.setSelectedItem(current);
    }
}
