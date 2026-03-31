# Personal Expense Tracker 💸

Maintaining a clear view of your daily spending doesn't have to be complicated. I created this desktop application to provide a simple, local-first way to manage financial activity without needing an internet connection or complex cloud services.

Everything stays on your machine in a plain `expenses.csv` file, so you're always in control of your data.

---

### What this app does
The core idea here was to build a tool that makes it effortless to see where your money is going. You can:
- **Track daily spending:** Add new expenses with titles, amounts, and categories.
- **Stay organized:** Use custom categories like "Food", "Transport", or "Bills" to keep things tidy.
- **Look back at history:** Filter your logs by specific date ranges or categories to find exactly what you're looking for.
- **Visualize the data:** The app generates a color-coded bar chart so you can see your biggest spending areas at a glance.

---

### Getting it up and running
Since this is a Java application using Swing, you just need a standard Java Development Kit (JDK 11 or newer) installed.

1. **Grab the code:**
   Clone this repository to your local machine:
   ```bash
   git clone https://github.com/YOUR_USERNAME/expense-tracker.git
   cd expense-tracker
   ```

2. **Compiling the source:**
   I've organized the code into a `src` folder. You can compile it all into an `out` directory with this command:
   ```bash
   mkdir -p out
   javac -d out src/com/expensetracker/*.java
   ```

3. **Launching the tracker:**
   Once it's compiled, fire it up by running:
   ```bash
   java -cp out com.expensetracker.Main
   ```

---

### A few notes on the technical side
I kept the architecture pretty straightforward. The project uses:
* **Java Swing** for the UI — old school but very reliable for desktop tools.
* **Java Streams** to handle all the filtering and data aggregation for the summary reports.
* **Basic File I/O** to save and load your data directly from a CSV file.

The `expenses.csv` file is auto-created in your project folder the first time you save an entry. You can even open it in Excel or Google Sheets if you want to do some deeper analysis later.

### Reflection
Building this was a great way to practice applying OOP principles in a real-world scenario. Managing states between the main window and the dialogs, and ensuring data persistence stayed consistent, were the most interesting parts of the process.

Feel free to poke around the code or use it as a starting point for your own tracking tool!

---
*Distributed under the MIT License — do whatever you like with it.*
