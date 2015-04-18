package edu.gatech.cs2340.trydent;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

/**
 * A basic class that provides cached loading and access to JavaFX audio resources.
 */
public class Images {

    private static Map<String, Image> cache = new HashMap<String, Image>();
    // TODO: clear out old images when necessary

    /**
     * Attempts to load an image, fetching from the cache if the image is already in memory.
     * @see #reloadImage for arguments
     */
    public static Image getImage(String filename) {
        Image result;
        if(!cache.containsKey(filename)){
            result = reloadImage(filename);
        } else {
            result = cache.get(filename);
        }
        return result;
    }

    /**
     * Forces a new disk read of the specified resource. Users should generally invoke getImage(). The filename
     * should meet the specifications of the JavaFX Image class; in particular, it supports local filepaths, web
     * URLs, and resources on the classpath.
     *
     * @param filename A correctly specified filename or resource specifier
     * @return the specified image object
     * @throws TrydentException if the resource could not be found
     */
    public static Image reloadImage(String filename){
        Image result = null;
        try {
            result = new Image(filename);
        } catch (IllegalArgumentException ex) {
            throw new TrydentException("Could not find the image filename. " + filename, ex);
        }
        cache.put(filename, result);
        return result;
    }
}

