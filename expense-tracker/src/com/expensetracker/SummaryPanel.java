package com.expensetracker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Map;

/**
 * A panel that displays a bar chart of spending by category
 * and a text-based summary of totals.
 */
public class SummaryPanel extends JPanel {

    private ExpenseManager manager;

    public SummaryPanel(ExpenseManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        refresh();
    }

    public void refresh() {
        removeAll();

        Map<String, Double> summary = manager.getCategorySummary();
        double grandTotal = manager.getTotalAmount(manager.getAllExpenses());

        // --- Top: Grand Total Label ---
        JLabel totalLabel = new JLabel(
            String.format("  Total Spent: ₹%.2f   |   Entries: %d",
                grandTotal, manager.getAllExpenses().size()),
            SwingConstants.LEFT
        );
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setForeground(new Color(44, 62, 80));
        totalLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(totalLabel, BorderLayout.NORTH);

        if (summary.isEmpty()) {
            JLabel emptyLabel = new JLabel("No data yet. Add expenses to see the summary.", SwingConstants.CENTER);
            emptyLabel.setForeground(Color.GRAY);
            add(emptyLabel, BorderLayout.CENTER);
            revalidate();
            repaint();
            return;
        }

        // --- Center: Bar Chart ---
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBarChart(g, summary, grandTotal);
            }
        };
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(new TitledBorder("Spending by Category"));
        chartPanel.setPreferredSize(new Dimension(600, 300));
        add(chartPanel, BorderLayout.CENTER);

        // --- South: Text Table ---
        JPanel tablePanel = new JPanel(new GridLayout(0, 2, 10, 4));
        tablePanel.setBorder(new TitledBorder("Breakdown"));
        tablePanel.setBackground(new Color(245, 247, 250));

        for (Map.Entry<String, Double> entry : summary.entrySet()) {
            double pct = grandTotal > 0 ? (entry.getValue() / grandTotal) * 100 : 0;
            JLabel cat = new JLabel("  " + entry.getKey());
            cat.setFont(new Font("Arial", Font.PLAIN, 12));
            JLabel amt = new JLabel(String.format("₹%.2f  (%.1f%%)", entry.getValue(), pct));
            amt.setFont(new Font("Arial", Font.BOLD, 12));
            amt.setForeground(new Color(41, 128, 185));
            tablePanel.add(cat);
            tablePanel.add(amt);
        }

        add(tablePanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void drawBarChart(Graphics g, Map<String, Double> summary, double grandTotal) {
        if (summary.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelW = getWidth() - 120;
        int panelH = getHeight() - 80;
        int startX = 80;
        int startY = 20;

        int n = summary.size();
        int barH = Math.max(18, (panelH / n) - 8);
        int gap = 8;

        Color[] palette = {
            new Color(52, 152, 219), new Color(46, 204, 113), new Color(231, 76, 60),
            new Color(241, 196, 15), new Color(155, 89, 182), new Color(26, 188, 156),
            new Color(230, 126, 34), new Color(52, 73, 94), new Color(149, 165, 166)
        };

        int i = 0;
        for (Map.Entry<String, Double> entry : summary.entrySet()) {
            double ratio = grandTotal > 0 ? entry.getValue() / grandTotal : 0;
            int barWidth = (int) (ratio * (panelW - startX - 10));
            int y = startY + i * (barH + gap);

            // Category label
            g2.setColor(new Color(44, 62, 80));
            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            g2.drawString(entry.getKey(), 5, y + barH - 4);

            // Bar
            g2.setColor(palette[i % palette.length]);
            g2.fillRoundRect(startX, y, Math.max(barWidth, 4), barH, 6, 6);

            // Amount label
            g2.setColor(new Color(44, 62, 80));
            g2.setFont(new Font("Arial", Font.BOLD, 10));
            g2.drawString(String.format("₹%.0f", entry.getValue()), startX + barWidth + 6, y + barH - 4);

            i++;
        }
    }
}
