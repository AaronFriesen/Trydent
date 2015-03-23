package edu.gatech.cs2340.trydent;

import javafx.scene.input.KeyCode;
import edu.gatech.cs2340.trydent.internal.KeyboardImpl;

/**
 * A static keyboard class for ease of using key events.
 *
 */
public class Keyboard {
    /**
     * Determines if a key is down on the keyboard.
     * @param key the key to check
     * @return whether the key is currently down
     */
    public static boolean isKeyDown(KeyCode key) {
        return KeyboardImpl.isKeyDown(key);
    }

    /**
     * Determines if a key is pressed just now.
     * @param key the key to check
     * @return whether the key is down starting with this frame
     */
    public static boolean isKeyDownOnce(KeyCode key) {
        return KeyboardImpl.isKeyDownOnce(key);
    }

    /**
     * Determines if a key is released just now.
     * @param key the key to check
     * @return whether the key is is up starting with this frame
     */
    public static boolean isKeyUpOnce(KeyCode key) {
        return KeyboardImpl.isKeyUpOnce(key);
    }
}
