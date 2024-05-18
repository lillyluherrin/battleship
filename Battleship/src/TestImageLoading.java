import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TestImageLoading extends JFrame {
    private JLabel hitLabel;
    private JLabel missLabel;

    public TestImageLoading() {
        setTitle("Test Image Loading");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        hitLabel = new JLabel();
        missLabel = new JLabel();

        loadImages();

        add(hitLabel);
        add(missLabel);

        setVisible(true);
    }

    private void loadImages() {
        hitLabel.setIcon(loadIcon("/hit.png"));
        missLabel.setIcon(loadIcon("/miss.png"));
    }

    private ImageIcon loadIcon(String path) {
        try {
            BufferedImage img = ImageIO.read(getClass().getResource(path));
            return new ImageIcon(img);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TestImageLoading());
    }
}
