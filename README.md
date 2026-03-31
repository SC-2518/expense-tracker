# expense-tracker

so basically i built this because i checked my bank balance one day and had absolutely no idea where ₹3,000 had disappeared. canteen probably. autos definitely. i'd tried the notes app, a spreadsheet i made once and never opened again, even a whatsapp group where i sent myself messages. none of it worked.

every actual expense app i looked at wanted a login or a subscription or asked me to link my bank account. i just wanted to type what i spent and see a chart at the end of the month. so i built it instead.

it's a java desktop app. you add an expense, you can filter by date or category, and there's a summary tab with a bar chart. everything saves to a csv file sitting in the same folder. no internet needed, no account, nothing to install except java.

---

## how to run it

you need java 11 or above. that's literally it, no maven no gradle no extra jars.

```bash
# compile
mkdir -p out
javac -d out $(find src -name "*.java")

# run
java -cp out com.expensetracker.Main
```

`expenses.csv` gets created automatically on the first run. after that it just updates itself.

---

## what's inside

```
expense-tracker/
├── src/
│   └── com/expensetracker/
│       ├── Main.java
│       ├── Expense.java            ← one expense record, handles csv serialization
│       ├── ExpenseManager.java     ← all the logic + file i/o
│       ├── MainWindow.java         ← the main window, tabbed ui
│       ├── ExpenseFormDialog.java  ← add/edit form
│       └── SummaryPanel.java       ← bar chart + breakdown
├── expenses.csv                    ← auto-created, don't delete
└── README.md
```

---

## how to use it

**adding something** — hit `+ Add`, fill in the title, amount, pick a category, date is pre-filled to today. note is optional. save.

**editing/deleting** — click a row to select it, then hit edit or delete. works even if you've sorted the table by a column.

**filtering** — use the dropdowns at the top, pick category and/or a date range, click apply. the bar at the bottom shows filtered count and subtotal. clear button brings everything back.

**summary tab** — click it and it shows total spent, a bar chart split by category, and percentage breakdown on the right. refreshes every time you switch to it.

---

## the csv is just plain text

```
id,title,amount,category,date,note
1,Lunch at Canteen,120.0,Food,2026-03-20,Veg thali
2,Bus pass,500.0,Transport,2026-03-21,Monthly pass
```

you can open it in excel, copy it as a backup, read it in notepad if something breaks. dates are `YYYY-MM-DD`. if you type a comma inside a field value it gets swapped to a semicolon internally so the parser doesn't break — it's not proper csv spec compliance but it handles the actual real-world cases fine.

---

## stuff i know is missing

- date input is a plain text field, typing yyyy-mm-dd every time is annoying, a calendar picker would've been better but every jdatepicker i found was an external library
- no budget warnings — it tells you what you spent, doesn't warn you while you still have time to stop
- recurring expenses (phone bill, netflix etc.) still need to be entered manually every month
- if this ever needed to scale to thousands of entries or sync across devices, csv would stop working and sqlite would be the next step — all the file i/o is inside `ExpenseManager` so the rest of the code wouldn't need to change

---

built for my java semester project — sneha chauhan | 24BAI10530
