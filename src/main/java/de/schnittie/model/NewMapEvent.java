package de.schnittie.model;

import java.awt.image.BufferedImage;

public class NewMapEvent extends MapGeneratorEvent {
    private final BufferedImage image;

    public NewMapEvent(BufferedImage image){
        this.image = image;
    }
    public BufferedImage getImage() {
        return image;
    }
}
