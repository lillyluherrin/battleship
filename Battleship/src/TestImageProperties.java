import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TestImageProperties extends JFrame {
    private JLabel hitLabel;
    private JLabel missLabel;

    public TestImageProperties() {
        setTitle("Test Image Properties");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        hitLabel = new JLabel();
        missLabel = new JLabel();

        BufferedImage hitImage = loadImage("/hit.png");
        BufferedImage missImage = loadImage("/miss.png");

        if (hitImage != null) {
            hitLabel.setIcon(new ImageIcon(hitImage));
            printImageProperties(hitImage, "Hit Image");
        } else {
            hitLabel.setText("Hit image not loaded");
        }

        if (missImage != null) {
            missLabel.setIcon(new ImageIcon(missImage));
            printImageProperties(missImage, "Miss Image");
        } else {
            missLabel.setText("Miss image not loaded");
        }

        add(hitLabel);
        add(missLabel);

        setVisible(true);
    }

    private BufferedImage loadImage(String path) {
        try {
            BufferedImage img = ImageIO.read(getClass().getResource(path));
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void printImageProperties(BufferedImage img, String label) {
        System.out.println(label + ":");
        System.out.println("Width: " + img.getWidth());
        System.out.println("Height: " + img.getHeight());
        System.out.println("Color Model: " + img.getColorModel());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TestImageProperties());
    }
}
