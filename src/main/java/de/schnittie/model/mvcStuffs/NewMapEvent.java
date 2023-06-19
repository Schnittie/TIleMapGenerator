package de.schnittie.model.mvcStuffs;

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
