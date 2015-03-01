package edu.gatech.cs2340.trydent;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A class that displays 2D sprite images and can support simple 2D animations.
 */
public class Sprite extends GameObject {

    private Image[] images;
    private ImageView currentView;

    public Sprite(String filename) {
        this(new String[]{filename}, 0);
    }
    public Sprite(String filename, String name) {
        this(new String[]{filename}, 0, name);
    }
    public Sprite(String[] filenames, double duration) {
        super();
        if (filenames.length == 0)
            throw new TrydentException("Sprites must be constructed with at least 1 file to load.");
        loadImages(filenames);
        initImageView(duration);
    }
    public Sprite(String[] filenames, double duration, String name) {
        super(name);
        if (filenames.length == 0)
            throw new TrydentException("Sprites must be constructed with at least 1 file to load.");
        loadImages(filenames);
        initImageView(duration);
    }

    private void loadImages(String[] filenames) {
        Image[] result = new Image[filenames.length];
        for (int i=0; i < filenames.length; i++) {
            try {
                result[i] = new Image(filenames[i]);
            } catch (IllegalArgumentException ex) {
                throw new TrydentException("Could not find the image filename. " + filenames[i], ex);
            }
        }
        images = result;
    }

    private void initImageView(double duration) {
        currentView = new ImageView(images[0]);
        if (duration > 0) {
            new ContinuousEvent() {
                @Override
                public void onUpdate() {
                    int index = (int) (Time.getTime() / duration) % images.length;
                    currentView.setImage(images[index]);
                }
            };
        }
        fxNode.getChildren().add(currentView);
    }

    public double getHeight() {
        return currentView.getImage().getHeight();
    }

    public double getWidth() {
        return currentView.getImage().getWidth();
    }
}

