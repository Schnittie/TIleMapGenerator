package de.schnittie.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

class ImagePanel extends JPanel {
    private BufferedImage image;
    private double scaleFactor = 1.0;

    public ImagePanel(BufferedImage image) {
        this.image = image;
        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        setMinimumSize(new Dimension(image.getWidth(), image.getHeight()));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    scaleFactor *= 1.1;
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    scaleFactor *= 0.9;
                }
                revalidate();
                repaint();
            }
        });

    }

    public ImagePanel() {
        //TODO
        // unused?
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        setMinimumSize(new Dimension(image.getWidth(), image.getHeight()));
        scaleFactor = 1.0;
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int scaledWidth = (int) (image.getWidth() * scaleFactor);
        int scaledHeight = (int) (image.getHeight() * scaleFactor);

        int x = (getWidth() - scaledWidth) / 2;
        int y = (getHeight() - scaledHeight) / 2;

        g.drawImage(image, x, y, scaledWidth, scaledHeight, null);
    }
}
