package diary;

import javax.swing.*;

public class Navigation {

    public static void go(JFrame from, JFrame to) {
        from.dispose();
        to.setVisible(true);
    }
}