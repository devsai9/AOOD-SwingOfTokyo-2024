import javax.swing.*;
import java.awt.*;

public class KOT_Swing {

    JFrame frame;
    JPanel panel;
    JLabel background;

    public KOT_Swing() {
        frame = new JFrame("King of Tokyo GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        panel = new JPanel();
        panel.setBounds(0, 0, 800, 600);
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridLayout(1, 3));

        background = new JLabel();
        background.setBackground(Color.BLACK);

        frame.add(panel);

        frame.setVisible(true);

    }
}