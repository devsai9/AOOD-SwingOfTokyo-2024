import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class JPanelWithBg extends JPanel {

    private Image bgImg;

    public JPanelWithBg(String fileName) {
        try {
            bgImg = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            System.out.println("Background image not found");
            System.exit(404);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);
    }
}