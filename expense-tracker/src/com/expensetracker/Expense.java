package com.expensetracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single expense entry.
 * Encapsulates all data related to one financial transaction.
 */
public class Expense {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static int idCounter = 1;

    private int id;
    private String title;
    private double amount;
    private String category;
    private LocalDate date;
    private String note;

    // Constructor for new expenses (auto-assigns ID)
    public Expense(String title, double amount, String category, LocalDate date, String note) {
        this.id = idCounter++;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    // Constructor used when loading from file (ID already known)
    public Expense(int id, String title, double amount, String category, LocalDate date, String note) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
        if (id >= idCounter) idCounter = id + 1;
    }

    // --- Getters ---
    public int getId()          { return id; }
    public String getTitle()    { return title; }
    public double getAmount()   { return amount; }
    public String getCategory() { return category; }
    public LocalDate getDate()  { return date; }
    public String getNote()     { return note; }

    // --- Setters ---
    public void setTitle(String title)       { this.title = title; }
    public void setAmount(double amount)     { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(LocalDate date)      { this.date = date; }
    public void setNote(String note)         { this.note = note; }

    /**
     * Serializes this expense to a CSV line.
     * Format: id,title,amount,category,date,note
     */
    public String toCsv() {
        String safeNote = note.replace(",", ";"); // avoid CSV corruption
        return id + "," + title + "," + amount + "," + category + "," + date.format(DATE_FORMAT) + "," + safeNote;
    }

    /**
     * Deserializes a CSV line back into an Expense object.
     */
    public static Expense fromCsv(String csvLine) {
        String[] parts = csvLine.split(",", 6);
        int id           = Integer.parseInt(parts[0].trim());
        String title     = parts[1].trim();
        double amount    = Double.parseDouble(parts[2].trim());
        String category  = parts[3].trim();
        LocalDate date   = LocalDate.parse(parts[4].trim(), DATE_FORMAT);
        String note      = parts.length > 5 ? parts[5].trim() : "";
        return new Expense(id, title, amount, category, date, note);
    }

    @Override
    public String toString() {
        return String.format("[%d] %s | %.2f | %s | %s", id, title, amount, category, date.format(DATE_FORMAT));
    }
}
