package diary.ui;

import diary.Navigation;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow(String name) {

        setTitle("Deník");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Vítej, " + name, SwingConstants.CENTER);

        JButton add = new JButton("Přidat");
        JButton cal = new JButton("Kalendář");

        add.addActionListener(e ->
                Navigation.go(this, new EntryWindow(null))
        );

        cal.addActionListener(e ->
                Navigation.go(this, new CalendarWindow())
        );

        setLayout(new GridLayout(3,1));
        add(label);
        add(add);
        add(cal);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}