package edu.gatech.cs2340.trydent.internal;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.input.KeyCode;

/**
 * A static keyboard class for ease of using key events.
 *
 */
public class KeyboardImpl {
    private static Set<KeyCode> down = new HashSet<>();
    private static Set<KeyCode> released = new HashSet<>();
    private static Set<KeyCode> pressed = new HashSet<>();
    private static Set<KeyCode> lastDownBuffer = new HashSet<>();
    private static Set<KeyCode> downBuffer = new HashSet<>();
    private static Set<KeyCode> releasedBuffer = new HashSet<>();
    private static Set<KeyCode> pressedBuffer = new HashSet<>();

    private static final Object UPDATE_LOCK = new Object();

    /**
     * Determines if a key is down on the keyboard.
     * @param key the key to check
     * @return whether the key is currently down
     */
    public static boolean isKeyDown(KeyCode key) {
        return down.contains(key);
    }

    /**
     * Determines if a key is pressed just now.
     * @param key the key to check
     * @return whether the key is down starting with this frame
     */
    public static boolean isKeyDownOnce(KeyCode key) {
        return pressed.contains(key);
    }

    /**
     * Determines if a key is released just now.
     * @param key the key to check
     * @return whether the key is is up starting with this frame
     */
    public static boolean isKeyUpOnce(KeyCode key) {
        return released.contains(key);
    }

    static void newFrame(){
        synchronized(UPDATE_LOCK){
            down.clear();
            down.addAll(downBuffer);
            pressed.clear();
            pressed.addAll(pressedBuffer);
            released.clear();
            released.addAll(releasedBuffer);

            lastDownBuffer.clear();
            lastDownBuffer.addAll(downBuffer);
            pressedBuffer.clear();
            releasedBuffer.clear();
        }
    }

    static void pressed(KeyCode key) {
        synchronized(UPDATE_LOCK){
            if(!lastDownBuffer.contains(key)) {
                pressedBuffer.add(key);
            }
            downBuffer.add(key);
        }
    }

    static void released(KeyCode key) {
        synchronized(UPDATE_LOCK){
            downBuffer.remove(key);
            releasedBuffer.add(key);
        }
    }
}
