package diary.ui;

import diary.Navigation;
import diary.model.Entry;
import diary.storage.FileStorage;
import diary.utils.DateUtils;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EntryWindow extends JFrame {

    public EntryWindow(LocalDateTime date) {

        setTitle("Zápis");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField title = new JTextField();
        JTextArea text = new JTextArea();

        JTextField imagePathField = new JTextField();
        imagePathField.setEditable(false);

        String[] img = {null};

        // 📅 дата
        LocalDateTime safeDateTime =
                (date != null)
                        ? DateUtils.fromDate(LocalDate.from(date))
                        : DateUtils.now();

        // 🔝 HEADER (с датой)
        JLabel header = new JLabel(
                "Nový zápis – " + safeDateTime.toLocalDate(),
                SwingConstants.CENTER
        );

        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setOpaque(true);
        header.setBackground(Color.GRAY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));

        add(header, BorderLayout.NORTH);

        // 🧠 CENTER
        JPanel center = new JPanel();
        center.setLayout(new GridBagLayout());
        center.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        center.setBackground(Color.WHITE);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,5,5,5);
        c.gridx = 0;
        c.gridy = 0;

        // Titulek
        center.add(new JLabel("Titulek:"), c);

        c.gridy++;
        title.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        center.add(title, c);

        // Text
        c.gridy++;
        center.add(new JLabel("Text:"), c);

        c.gridy++;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        text.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(text);
        scroll.setPreferredSize(new Dimension(400, 300)); // больше поле

        center.add(scroll, c);

        // Obrázek
        c.gridy++;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        center.add(new JLabel("Obrázek:"), c);

        c.gridy++;
        center.add(imagePathField, c);

        add(center, BorderLayout.CENTER);

        // 🔽 BUTTONS
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottom.setBackground(Color.GRAY);

        JButton save = new JButton("Uložit zápis");
        JButton delete = new JButton("Smazat zápis");
        JButton image = new JButton("Vybrat obrázek");
        JButton back = new JButton("Zpátky do kalendáře");

        for (JButton b : new JButton[]{save, delete, image, back}) {
            b.setFocusPainted(false);
            b.setBorderPainted(false);
            b.setOpaque(true);
            b.setBackground(Color.GRAY);
            b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            b.setMargin(new Insets(10, 20, 10, 20)); // 🔥 padding вместо фиксированной ширины
            bottom.add(b);
        }

        add(bottom, BorderLayout.SOUTH);

        // 🎯 ACTIONS

        image.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                img[0] = fc.getSelectedFile().getAbsolutePath();
                imagePathField.setText(img[0]);
            }
        });

        save.addActionListener(e -> {

            Entry entry = new Entry(
                    title.getText(),
                    text.getText(),
                    safeDateTime,
                    img[0]
            );

            FileStorage.saveEntry(entry);

            JOptionPane.showMessageDialog(this, "Uloženo!");
        });

        delete.addActionListener(e -> {

            int res = JOptionPane.showConfirmDialog(
                    this,
                    "Opravdu smazat?",
                    "Potvrzení",
                    JOptionPane.YES_NO_OPTION
            );

            if (res == JOptionPane.YES_OPTION) {
                FileStorage.deleteEntry(new Entry(title.getText(), "", safeDateTime, null));
                JOptionPane.showMessageDialog(this, "Smazáno");
            }
        });

        back.addActionListener(e ->
                Navigation.go(this, new CalendarWindow())
        );

        setVisible(true);
    }
}