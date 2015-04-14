package edu.gatech.cs2340.trydent.internal;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

    private static Map<Integer, KeyCode> swingToJavaFXKeyMap;

    static {
        swingToJavaFXKeyMap = new HashMap<>();
        swingToJavaFXKeyMap.put(KeyEvent.VK_A, KeyCode.A);
        swingToJavaFXKeyMap.put(KeyEvent.VK_ACCEPT, KeyCode.ACCEPT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_ADD, KeyCode.ADD);
        swingToJavaFXKeyMap.put(KeyEvent.VK_AGAIN, KeyCode.AGAIN);
        swingToJavaFXKeyMap.put(KeyEvent.VK_ALL_CANDIDATES, KeyCode.ALL_CANDIDATES);
        swingToJavaFXKeyMap.put(KeyEvent.VK_ALPHANUMERIC, KeyCode.ALPHANUMERIC);
        swingToJavaFXKeyMap.put(KeyEvent.VK_ALT, KeyCode.ALT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_ALT_GRAPH, KeyCode.ALT_GRAPH);
        swingToJavaFXKeyMap.put(KeyEvent.VK_AMPERSAND, KeyCode.AMPERSAND);
        swingToJavaFXKeyMap.put(KeyEvent.VK_ASTERISK, KeyCode.ASTERISK);
        swingToJavaFXKeyMap.put(KeyEvent.VK_AT, KeyCode.AT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_B, KeyCode.B);
        swingToJavaFXKeyMap.put(KeyEvent.VK_BACK_QUOTE, KeyCode.BACK_QUOTE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_BACK_SLASH, KeyCode.BACK_SLASH);
        swingToJavaFXKeyMap.put(KeyEvent.VK_BACK_SPACE, KeyCode.BACK_SPACE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_BEGIN, KeyCode.BEGIN);
        swingToJavaFXKeyMap.put(KeyEvent.VK_BRACELEFT, KeyCode.BRACELEFT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_BRACERIGHT, KeyCode.BRACERIGHT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_C, KeyCode.C);
        swingToJavaFXKeyMap.put(KeyEvent.VK_CANCEL, KeyCode.CANCEL);
        swingToJavaFXKeyMap.put(KeyEvent.VK_CAPS_LOCK, KeyCode.CAPS);
        // swingToJavaFXKeyMap.put(, KeyCode.CHANNEL_DOWN);
        // swingToJavaFXKeyMap.put(, KeyCode.CHANNEL_UP);
        swingToJavaFXKeyMap.put(KeyEvent.VK_CIRCUMFLEX, KeyCode.CIRCUMFLEX);
        swingToJavaFXKeyMap.put(KeyEvent.VK_CLEAR, KeyCode.CLEAR);
        swingToJavaFXKeyMap.put(KeyEvent.VK_CLOSE_BRACKET, KeyCode.CLOSE_BRACKET);
        swingToJavaFXKeyMap.put(KeyEvent.VK_CODE_INPUT, KeyCode.CODE_INPUT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_COLON, KeyCode.COLON);
        // swingToJavaFXKeyMap.put(, KeyCode.COLORED_KEY_0);
        // swingToJavaFXKeyMap.put(, KeyCode.COLORED_KEY_1);
        // swingToJavaFXKeyMap.put(, KeyCode.COLORED_KEY_2);
        // swingToJavaFXKeyMap.put(, KeyCode.COLORED_KEY_3);
        swingToJavaFXKeyMap.put(KeyEvent.VK_COMMA, KeyCode.COMMA);
        // swingToJavaFXKeyMap.put(KeyEvent.VK_META, KeyCode.COMMAND);
        swingToJavaFXKeyMap.put(KeyEvent.VK_COMPOSE, KeyCode.COMPOSE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_CONTEXT_MENU, KeyCode.CONTEXT_MENU);
        swingToJavaFXKeyMap.put(KeyEvent.VK_CONTROL, KeyCode.CONTROL);
        swingToJavaFXKeyMap.put(KeyEvent.VK_CONVERT, KeyCode.CONVERT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_COPY, KeyCode.COPY);
        swingToJavaFXKeyMap.put(KeyEvent.VK_CUT, KeyCode.CUT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_D, KeyCode.D);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_ABOVEDOT, KeyCode.DEAD_ABOVEDOT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_ABOVERING, KeyCode.DEAD_ABOVERING);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_ACUTE, KeyCode.DEAD_ACUTE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_BREVE, KeyCode.DEAD_BREVE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_CARON, KeyCode.DEAD_CARON);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_CEDILLA, KeyCode.DEAD_CEDILLA);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_CIRCUMFLEX, KeyCode.DEAD_CIRCUMFLEX);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_DIAERESIS, KeyCode.DEAD_DIAERESIS);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_DOUBLEACUTE, KeyCode.DEAD_DOUBLEACUTE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_GRAVE, KeyCode.DEAD_GRAVE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_IOTA, KeyCode.DEAD_IOTA);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_MACRON, KeyCode.DEAD_MACRON);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_OGONEK, KeyCode.DEAD_OGONEK);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_SEMIVOICED_SOUND, KeyCode.DEAD_SEMIVOICED_SOUND);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_TILDE, KeyCode.DEAD_TILDE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DEAD_VOICED_SOUND, KeyCode.DEAD_VOICED_SOUND);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DECIMAL, KeyCode.DECIMAL);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DELETE, KeyCode.DELETE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_0, KeyCode.DIGIT0);
        swingToJavaFXKeyMap.put(KeyEvent.VK_1, KeyCode.DIGIT1);
        swingToJavaFXKeyMap.put(KeyEvent.VK_2, KeyCode.DIGIT2);
        swingToJavaFXKeyMap.put(KeyEvent.VK_3, KeyCode.DIGIT3);
        swingToJavaFXKeyMap.put(KeyEvent.VK_4, KeyCode.DIGIT4);
        swingToJavaFXKeyMap.put(KeyEvent.VK_5, KeyCode.DIGIT5);
        swingToJavaFXKeyMap.put(KeyEvent.VK_6, KeyCode.DIGIT6);
        swingToJavaFXKeyMap.put(KeyEvent.VK_7, KeyCode.DIGIT7);
        swingToJavaFXKeyMap.put(KeyEvent.VK_8, KeyCode.DIGIT8);
        swingToJavaFXKeyMap.put(KeyEvent.VK_9, KeyCode.DIGIT9);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DIVIDE, KeyCode.DIVIDE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DOLLAR, KeyCode.DOLLAR);
        swingToJavaFXKeyMap.put(KeyEvent.VK_DOWN, KeyCode.DOWN);
        swingToJavaFXKeyMap.put(KeyEvent.VK_E, KeyCode.E);
        // swingToJavaFXKeyMap.put(, KeyCode.EJECT_TOGGLE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_END, KeyCode.END);
        swingToJavaFXKeyMap.put(KeyEvent.VK_ENTER, KeyCode.ENTER);
        swingToJavaFXKeyMap.put(KeyEvent.VK_EQUALS, KeyCode.EQUALS);
        swingToJavaFXKeyMap.put(KeyEvent.VK_ESCAPE, KeyCode.ESCAPE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_EURO_SIGN, KeyCode.EURO_SIGN);
        swingToJavaFXKeyMap.put(KeyEvent.VK_EXCLAMATION_MARK, KeyCode.EXCLAMATION_MARK);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F, KeyCode.F);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F1, KeyCode.F1);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F2, KeyCode.F2);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F3, KeyCode.F3);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F4, KeyCode.F4);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F5, KeyCode.F5);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F6, KeyCode.F6);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F7, KeyCode.F7);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F8, KeyCode.F8);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F9, KeyCode.F9);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F10, KeyCode.F10);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F11, KeyCode.F11);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F12, KeyCode.F12);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F13, KeyCode.F13);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F14, KeyCode.F14);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F15, KeyCode.F15);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F16, KeyCode.F16);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F17, KeyCode.F17);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F18, KeyCode.F18);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F19, KeyCode.F19);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F20, KeyCode.F20);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F21, KeyCode.F21);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F22, KeyCode.F22);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F23, KeyCode.F23);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F24, KeyCode.F24);
        // swingToJavaFXKeyMap.put(, KeyCode.FAST_FWD);
        swingToJavaFXKeyMap.put(KeyEvent.VK_FINAL, KeyCode.FINAL);
        swingToJavaFXKeyMap.put(KeyEvent.VK_FIND, KeyCode.FIND);
        swingToJavaFXKeyMap.put(KeyEvent.VK_FULL_WIDTH, KeyCode.FULL_WIDTH);
        swingToJavaFXKeyMap.put(KeyEvent.VK_G, KeyCode.G);
        // swingToJavaFXKeyMap.put(, KeyCode.GAME_A);
        // swingToJavaFXKeyMap.put(, KeyCode.GAME_B);
        // swingToJavaFXKeyMap.put(, KeyCode.GAME_C);
        // swingToJavaFXKeyMap.put(, KeyCode.GAME_D);
        swingToJavaFXKeyMap.put(KeyEvent.VK_GREATER, KeyCode.GREATER);
        swingToJavaFXKeyMap.put(KeyEvent.VK_H, KeyCode.H);
        swingToJavaFXKeyMap.put(KeyEvent.VK_HALF_WIDTH, KeyCode.HALF_WIDTH);
        swingToJavaFXKeyMap.put(KeyEvent.VK_HELP, KeyCode.HELP);
        swingToJavaFXKeyMap.put(KeyEvent.VK_HIRAGANA, KeyCode.HIRAGANA);
        swingToJavaFXKeyMap.put(KeyEvent.VK_HOME, KeyCode.HOME);
        swingToJavaFXKeyMap.put(KeyEvent.VK_I, KeyCode.I);
        // swingToJavaFXKeyMap.put(, KeyCode.INFO);
        swingToJavaFXKeyMap.put(KeyEvent.VK_INPUT_METHOD_ON_OFF, KeyCode.INPUT_METHOD_ON_OFF);
        swingToJavaFXKeyMap.put(KeyEvent.VK_INSERT, KeyCode.INSERT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_INVERTED_EXCLAMATION_MARK, KeyCode.INVERTED_EXCLAMATION_MARK);
        swingToJavaFXKeyMap.put(KeyEvent.VK_F, KeyCode.J);
        swingToJavaFXKeyMap.put(KeyEvent.VK_JAPANESE_HIRAGANA, KeyCode.JAPANESE_HIRAGANA);
        swingToJavaFXKeyMap.put(KeyEvent.VK_JAPANESE_KATAKANA, KeyCode.JAPANESE_KATAKANA);
        swingToJavaFXKeyMap.put(KeyEvent.VK_JAPANESE_ROMAN, KeyCode.JAPANESE_ROMAN);
        swingToJavaFXKeyMap.put(KeyEvent.VK_K, KeyCode.K);
        swingToJavaFXKeyMap.put(KeyEvent.VK_KANA, KeyCode.KANA);
        swingToJavaFXKeyMap.put(KeyEvent.VK_KANA_LOCK, KeyCode.KANA_LOCK);
        swingToJavaFXKeyMap.put(KeyEvent.VK_KANJI, KeyCode.KANJI);
        swingToJavaFXKeyMap.put(KeyEvent.VK_KATAKANA, KeyCode.KATAKANA);
        swingToJavaFXKeyMap.put(KeyEvent.VK_KP_DOWN, KeyCode.KP_DOWN);
        swingToJavaFXKeyMap.put(KeyEvent.VK_KP_LEFT, KeyCode.KP_LEFT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_KP_RIGHT, KeyCode.KP_RIGHT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_KP_UP, KeyCode.KP_UP);
        swingToJavaFXKeyMap.put(KeyEvent.VK_L, KeyCode.L);
        swingToJavaFXKeyMap.put(KeyEvent.VK_LEFT, KeyCode.LEFT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_LEFT_PARENTHESIS, KeyCode.LEFT_PARENTHESIS);
        swingToJavaFXKeyMap.put(KeyEvent.VK_LESS, KeyCode.LESS);
        swingToJavaFXKeyMap.put(KeyEvent.VK_M, KeyCode.M);
        swingToJavaFXKeyMap.put(KeyEvent.VK_META, KeyCode.META);
        swingToJavaFXKeyMap.put(KeyEvent.VK_MINUS, KeyCode.MINUS);
        swingToJavaFXKeyMap.put(KeyEvent.VK_MODECHANGE, KeyCode.MODECHANGE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_MULTIPLY, KeyCode.MULTIPLY);
        // swingToJavaFXKeyMap.put(, KeyCode.MUTE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_N, KeyCode.N);
        swingToJavaFXKeyMap.put(KeyEvent.VK_NONCONVERT, KeyCode.NONCONVERT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_NUM_LOCK, KeyCode.NUM_LOCK);
        swingToJavaFXKeyMap.put(KeyEvent.VK_NUMBER_SIGN, KeyCode.NUMBER_SIGN);
        swingToJavaFXKeyMap.put(KeyEvent.VK_NUMPAD0, KeyCode.NUMPAD0);
        swingToJavaFXKeyMap.put(KeyEvent.VK_NUMPAD1, KeyCode.NUMPAD1);
        swingToJavaFXKeyMap.put(KeyEvent.VK_NUMPAD2, KeyCode.NUMPAD2);
        swingToJavaFXKeyMap.put(KeyEvent.VK_NUMPAD3, KeyCode.NUMPAD3);
        swingToJavaFXKeyMap.put(KeyEvent.VK_NUMPAD4, KeyCode.NUMPAD4);
        swingToJavaFXKeyMap.put(KeyEvent.VK_NUMPAD5, KeyCode.NUMPAD5);
        swingToJavaFXKeyMap.put(KeyEvent.VK_NUMPAD6, KeyCode.NUMPAD6);
        swingToJavaFXKeyMap.put(KeyEvent.VK_NUMPAD7, KeyCode.NUMPAD7);
        swingToJavaFXKeyMap.put(KeyEvent.VK_NUMPAD8, KeyCode.NUMPAD8);
        swingToJavaFXKeyMap.put(KeyEvent.VK_NUMPAD9, KeyCode.NUMPAD9);
        swingToJavaFXKeyMap.put(KeyEvent.VK_O, KeyCode.O);
        swingToJavaFXKeyMap.put(KeyEvent.VK_OPEN_BRACKET, KeyCode.OPEN_BRACKET);
        swingToJavaFXKeyMap.put(KeyEvent.VK_P, KeyCode.P);
        swingToJavaFXKeyMap.put(KeyEvent.VK_PAGE_DOWN, KeyCode.PAGE_DOWN);
        swingToJavaFXKeyMap.put(KeyEvent.VK_PAGE_UP, KeyCode.PAGE_UP);
        swingToJavaFXKeyMap.put(KeyEvent.VK_PASTE, KeyCode.PASTE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_PAUSE, KeyCode.PAUSE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_PERIOD, KeyCode.PERIOD);
        // swingToJavaFXKeyMap.put(, KeyCode.PLAY);
        swingToJavaFXKeyMap.put(KeyEvent.VK_PLUS, KeyCode.PLUS);
        // swingToJavaFXKeyMap.put(, KeyCode.POUND);
        // swingToJavaFXKeyMap.put(, KeyCode.POWER);
        swingToJavaFXKeyMap.put(KeyEvent.VK_PREVIOUS_CANDIDATE, KeyCode.PREVIOUS_CANDIDATE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_PRINTSCREEN, KeyCode.PRINTSCREEN);
        swingToJavaFXKeyMap.put(KeyEvent.VK_PROPS, KeyCode.PROPS);
        swingToJavaFXKeyMap.put(KeyEvent.VK_Q, KeyCode.Q);
        swingToJavaFXKeyMap.put(KeyEvent.VK_QUOTE, KeyCode.QUOTE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_QUOTEDBL, KeyCode.QUOTEDBL);
        swingToJavaFXKeyMap.put(KeyEvent.VK_R, KeyCode.R);
        // swingToJavaFXKeyMap.put(, KeyCode.RECORD);
        // swingToJavaFXKeyMap.put(, KeyCode.REWIND);
        swingToJavaFXKeyMap.put(KeyEvent.VK_RIGHT, KeyCode.RIGHT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_RIGHT_PARENTHESIS, KeyCode.RIGHT_PARENTHESIS);
        swingToJavaFXKeyMap.put(KeyEvent.VK_ROMAN_CHARACTERS, KeyCode.ROMAN_CHARACTERS);
        swingToJavaFXKeyMap.put(KeyEvent.VK_S, KeyCode.S);
        swingToJavaFXKeyMap.put(KeyEvent.VK_SCROLL_LOCK, KeyCode.SCROLL_LOCK);
        swingToJavaFXKeyMap.put(KeyEvent.VK_SEMICOLON, KeyCode.SEMICOLON);
        swingToJavaFXKeyMap.put(KeyEvent.VK_SEPARATOR, KeyCode.SEPARATOR);
        swingToJavaFXKeyMap.put(KeyEvent.VK_SHIFT, KeyCode.SHIFT);
        // swingToJavaFXKeyMap.put(, KeyCode.SHORTCUT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_SLASH, KeyCode.SLASH);
        // swingToJavaFXKeyMap.put(, KeyCode.SOFTKEY_0);
        // swingToJavaFXKeyMap.put(, KeyCode.SOFTKEY_1);
        // swingToJavaFXKeyMap.put(, KeyCode.SOFTKEY_3);
        // swingToJavaFXKeyMap.put(, KeyCode.SOFTKEY_4);
        // swingToJavaFXKeyMap.put(, KeyCode.SOFTKEY_5);
        // swingToJavaFXKeyMap.put(, KeyCode.SOFTKEY_6);
        // swingToJavaFXKeyMap.put(, KeyCode.SOFTKEY_7);
        // swingToJavaFXKeyMap.put(, KeyCode.SOFTKEY_8);
        // swingToJavaFXKeyMap.put(, KeyCode.SOFTKEY_9);
        swingToJavaFXKeyMap.put(KeyEvent.VK_SPACE, KeyCode.SPACE);
        // swingToJavaFXKeyMap.put(, KeyCode.STAR);
        swingToJavaFXKeyMap.put(KeyEvent.VK_STOP, KeyCode.STOP);
        swingToJavaFXKeyMap.put(KeyEvent.VK_SUBTRACT, KeyCode.SUBTRACT);
        swingToJavaFXKeyMap.put(KeyEvent.VK_T, KeyCode.T);
        swingToJavaFXKeyMap.put(KeyEvent.VK_TAB, KeyCode.TAB);
        // swingToJavaFXKeyMap.put(, KeyCode.TRACK_NEXT);
        // swingToJavaFXKeyMap.put(, KeyCode.TRACK_PREV);
        swingToJavaFXKeyMap.put(KeyEvent.VK_U, KeyCode.U);
        swingToJavaFXKeyMap.put(KeyEvent.VK_UNDEFINED, KeyCode.UNDEFINED);
        swingToJavaFXKeyMap.put(KeyEvent.VK_UNDERSCORE, KeyCode.UNDERSCORE);
        swingToJavaFXKeyMap.put(KeyEvent.VK_UNDO, KeyCode.UNDO);
        swingToJavaFXKeyMap.put(KeyEvent.VK_UP, KeyCode.UP);
        swingToJavaFXKeyMap.put(KeyEvent.VK_V, KeyCode.V);
        // swingToJavaFXKeyMap.put(, KeyCode.VOLUME_DOWN);
        // swingToJavaFXKeyMap.put(, KeyCode.VOLUME_UP);
        swingToJavaFXKeyMap.put(KeyEvent.VK_W, KeyCode.W);
        swingToJavaFXKeyMap.put(KeyEvent.VK_WINDOWS, KeyCode.WINDOWS);
        swingToJavaFXKeyMap.put(KeyEvent.VK_X, KeyCode.X);
        swingToJavaFXKeyMap.put(KeyEvent.VK_Y, KeyCode.Y);
        swingToJavaFXKeyMap.put(KeyEvent.VK_Z, KeyCode.Z);
    }

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

    static void newFrame() {
        synchronized(UPDATE_LOCK) {
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
        synchronized(UPDATE_LOCK) {
            if(!lastDownBuffer.contains(key)) {
                pressedBuffer.add(key);
            }
            downBuffer.add(key);
        }
    }

    static void released(KeyCode key) {
        synchronized(UPDATE_LOCK) {
            downBuffer.remove(key);
            releasedBuffer.add(key);
        }
    }

    static KeyCode convertSwingToJavaFXKeyEvent(int key) {
        return swingToJavaFXKeyMap.get(key);
    }
}
