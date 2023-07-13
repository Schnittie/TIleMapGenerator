package de.schnittie.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class ImagePanel extends JPanel {
    private BufferedImage image;
    private double scaleFactor = 1.0;
    private int translationX = 0;
    private int translationY = 0;
    private Point dragStart;

    public ImagePanel(BufferedImage image) {
        this.image = image;
        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        setMinimumSize(new Dimension(image.getWidth(), image.getHeight()));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                dragStart = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e){
                dragStart = null;
            }

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

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    scaleFactor *= 1.1;
                } else {
                    scaleFactor *= 0.9;
                }
                revalidate();
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){
                if (dragStart != null) {
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;
                    translationX += dx;
                    translationY += dy;
                    dragStart = e.getPoint();
                    repaint();
                }
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
        scaleFactor = calculateInitialScale();
        translationX = 0;
        translationY = 0;
        revalidate();
        repaint();
    }

    private double calculateInitialScale(){
        double scaleWidth = (double) getWidth() / image.getWidth();
        double scaleHeight = (double) getHeight() / image.getHeight();
        return Math.min(scaleWidth, scaleHeight);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.translate(translationX, translationY);

            g2d.scale(scaleFactor, scaleFactor);

            int scaledWidth = (int) (image.getWidth() * scaleFactor);
            int scaledHeight = (int) (image.getHeight() * scaleFactor);

            int x = (getWidth() - scaledWidth) / 2;
            int y = (getHeight() - scaledHeight) / 2;

            g2d.drawImage(image, x, y, scaledWidth, scaledHeight, null);

            g2d.dispose();
        }
    }
}
