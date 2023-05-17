package frontend;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class MapPanel extends JPanel {
    private BufferedImage image;

    public MapPanel() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void fillWithImage(String filepath) {
        try {
            image = ImageIO.read(new File(filepath));
            repaint();
        } catch (IOException ex) {
            System.out.println("Error loading image from file: " + filepath);
            ex.printStackTrace();
        }
    }

    public void reset() {
        image = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }


    public Dimension getPreferredSize() {
        return new Dimension(100, 100);
    }
}