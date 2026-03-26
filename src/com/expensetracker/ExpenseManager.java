package com.expensetracker;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages the collection of expenses.
 * Handles CRUD operations, filtering, summary stats, and CSV persistence.
 */
public class ExpenseManager {

    private static final String DATA_FILE = "expenses.csv";

    private List<Expense> expenses;

    public ExpenseManager() {
        expenses = new ArrayList<>();
        loadFromFile();
    }

    // ------------------------------------------------------------------ CRUD

    public void addExpense(Expense e) {
        expenses.add(e);
        saveToFile();
    }

    public boolean updateExpense(int id, String title, double amount,
                                 String category, LocalDate date, String note) {
        for (Expense e : expenses) {
            if (e.getId() == id) {
                e.setTitle(title);
                e.setAmount(amount);
                e.setCategory(category);
                e.setDate(date);
                e.setNote(note);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    public boolean deleteExpense(int id) {
        boolean removed = expenses.removeIf(e -> e.getId() == id);
        if (removed) saveToFile();
        return removed;
    }

    public Optional<Expense> getExpenseById(int id) {
        return expenses.stream().filter(e -> e.getId() == id).findFirst();
    }

    // --------------------------------------------------------------- Queries

    /** Returns all expenses (defensive copy). */
    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses);
    }

    /** Filters expenses by category (case-insensitive). */
    public List<Expense> getByCategory(String category) {
        return expenses.stream()
                .filter(e -> e.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /** Filters expenses within a date range (inclusive). */
    public List<Expense> getByDateRange(LocalDate from, LocalDate to) {
        return expenses.stream()
                .filter(e -> !e.getDate().isBefore(from) && !e.getDate().isAfter(to))
                .collect(Collectors.toList());
    }

    /** Returns the total amount for a given list of expenses. */
    public double getTotalAmount(List<Expense> list) {
        return list.stream().mapToDouble(Expense::getAmount).sum();
    }

    /** Returns a map of category -> total amount spent. */
    public Map<String, Double> getCategorySummary() {
        Map<String, Double> summary = new LinkedHashMap<>();
        for (Expense e : expenses) {
            summary.merge(e.getCategory(), e.getAmount(), Double::sum);
        }
        return summary;
    }

    /** Returns all distinct categories present in the data. */
    public List<String> getAllCategories() {
        return expenses.stream()
                .map(Expense::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------- Persistence

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Expense e : expenses) {
                writer.write(e.toCsv());
                writer.newLine();
            }
        } catch (IOException ex) {
            System.err.println("Error saving data: " + ex.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        expenses.add(Expense.fromCsv(line));
                    } catch (Exception ex) {
                        System.err.println("Skipping malformed line: " + line);
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Error loading data: " + ex.getMessage());
        }
    }
}
