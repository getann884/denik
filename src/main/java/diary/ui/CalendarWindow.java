package diary.ui;

import diary.Navigation;
import diary.storage.FileStorage;
import diary.model.Entry;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.List;

public class CalendarWindow extends JFrame {

    private LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);

    public CalendarWindow() {

        setTitle("Kalendář");
        setSize(600, 400);
        setLocationRelativeTo(null);

        render();
    }

    private void render() {

        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel top = new JPanel();

        JButton prev = new JButton("<");
        JButton next = new JButton(">");
        JButton back = new JButton("Menu");
        JButton exit = new JButton("Exit");

        JLabel label = new JLabel(currentMonth.getMonth() + " " + currentMonth.getYear());

        prev.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            render();
        });

        next.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            render();
        });

        back.addActionListener(e ->
                Navigation.go(this, new MainWindow(FileStorage.loadUserName()))
        );

        exit.addActionListener(e -> System.exit(0));

        top.add(prev);
        top.add(label);
        top.add(next);
        top.add(back);
        top.add(exit);

        add(top, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 7));

        LocalDate first = currentMonth.withDayOfMonth(1);

        int offset = first.getDayOfWeek().getValue();

        for (int i = 1; i < offset; i++) {
            grid.add(new JLabel());
        }

        int days = currentMonth.lengthOfMonth();

        for (int i = 1; i <= days; i++) {

            LocalDate date = currentMonth.withDayOfMonth(i);

            JButton btn = new JButton(String.valueOf(i));

            btn.addActionListener(e -> showEntries(date));

            grid.add(btn);
        }

        add(grid, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void showEntries(LocalDate date) {

        List<Entry> list = FileStorage.loadEntries().stream()
                .filter(e -> e.getDateTime().toLocalDate().equals(date))
                .toList();

        JTextArea area = new JTextArea();

        if (list.isEmpty()) {
            area.setText("Žádné zápisy");
        } else {
            StringBuilder sb = new StringBuilder();

            for (Entry e : list) {
                sb.append(e.getTitle()).append("\n");
                sb.append(e.getContent()).append("\n\n");
            }

            area.setText(sb.toString());
        }

        JOptionPane.showMessageDialog(this, new JScrollPane(area));
    }
}