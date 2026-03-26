# 💸 Personal Expense Tracker

A lightweight desktop application built in Java to help you track daily expenses, categorize spending, and visualize where your money goes — all stored locally on your machine.

---

## 📸 Features

- ➕ **Add / Edit / Delete** expense entries
- 🗂️ **Filter** expenses by category or date range
- 📊 **Visual summary** with a bar chart of spending per category
- 💾 **Persistent storage** using a local CSV file (no internet required)
- 🖥️ **Native desktop GUI** built with Java Swing

---

## 🛠️ Prerequisites

- **Java JDK 11 or higher** — [Download here](https://www.oracle.com/java/technologies/downloads/)

Verify your installation:
```bash
java -version
javac -version
```

---

## 🚀 Setup & Run

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/expense-tracker.git
cd expense-tracker
```

### 2. Compile the source code

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```

### 3. Run the application

```bash
java -cp out com.expensetracker.Main
```

---

## 📁 Project Structure

```
expense-tracker/
├── src/
│   └── com/expensetracker/
│       ├── Main.java               # Entry point
│       ├── Expense.java            # Expense model (OOP)
│       ├── ExpenseManager.java     # Business logic & File I/O
│       ├── MainWindow.java         # Main GUI window (Swing)
│       ├── ExpenseFormDialog.java  # Add/Edit dialog
│       └── SummaryPanel.java       # Bar chart & summary view
├── out/                            # Compiled .class files (generated)
├── expenses.csv                    # Auto-created data file
└── README.md
```

---

## 🧭 How to Use

### Adding an Expense
1. Click the **➕ Add** button
2. Fill in the title, amount, category, date, and an optional note
3. Click **Save**

### Editing an Expense
1. Click on any row in the table to select it
2. Click **✏ Edit**
3. Update the fields and click **Save**

### Deleting an Expense
1. Select a row in the table
2. Click **🗑 Delete** and confirm

### Filtering
- Use the **Category** dropdown to filter by type (Food, Transport, etc.)
- Enter **From** and **To** dates in `YYYY-MM-DD` format to filter by date range
- Click **Apply** to filter, or **Clear** to reset

### Viewing the Summary
- Click the **📊 Summary** tab to see:
  - Total amount spent
  - A color-coded bar chart by category
  - A percentage breakdown table

---

## 💾 Data Storage

All data is saved automatically to `expenses.csv` in the same directory as where you run the application. The file is human-readable and can be opened in any spreadsheet editor.

**CSV format:**
```
id,title,amount,category,date,note
1,Lunch at Canteen,120.0,Food,2026-03-20,Veg thali
2,Bus pass,500.0,Transport,2026-03-21,Monthly pass
```

---

## 🧑‍💻 Java Concepts Used

| Concept | Where Used |
|---|---|
| OOP (Classes & Encapsulation) | `Expense`, `ExpenseManager`, `MainWindow` |
| Inheritance & Polymorphism | `JPanel`, `JDialog` subclasses |
| Collections (`ArrayList`, `HashMap`) | `ExpenseManager` |
| File I/O (`BufferedReader/Writer`) | `ExpenseManager.saveToFile()` / `loadFromFile()` |
| Java Swing GUI | `MainWindow`, `ExpenseFormDialog`, `SummaryPanel` |
| Java Streams | Filtering, mapping, grouping in `ExpenseManager` |
| `LocalDate` (Java Time API) | Date handling in `Expense` |

---

## 📄 License

MIT License — free to use, modify, and distribute.
