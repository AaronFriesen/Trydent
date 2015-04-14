package edu.gatech.cs2340.trydent.sample;

import javafx.scene.input.MouseButton;
import edu.gatech.cs2340.trydent.Audio;
import edu.gatech.cs2340.trydent.Mouse;
import edu.gatech.cs2340.trydent.TrydentEngine;

/**
 * A basic audio example.
 * Use mouse middle to pause or play sound.
 * Click mouse primary or secondary for a sound effect.
 */
public class AudioExample implements Runnable {
    private static final String SONG = "edu/gatech/cs2340/trydent/sample/song.mp3";
    private static final String SFX = "edu/gatech/cs2340/trydent/sample/sfx.mp3";

    public static void main(String[] args) {
        TrydentEngine.start();
        TrydentEngine.setWindowTitle("TrydentEngine - Audio Example");
        TrydentEngine.setWindowSize(480, 480);
        TrydentEngine.runOnce(new AudioExample());
    }

    private void togglePlaying() {
        if(Audio.isPlaying()) {
            Audio.pauseAudio();
        } else {
            Audio.resumeAudio();
        }
    }

    @Override
    public void run() {
        String song = getClass().getClassLoader().getResource(SONG).toString();
        String sfx = getClass().getClassLoader().getResource(SFX).toString();
        Audio.setMusic(song);
        Audio.setMasterVolume(0.1);
        TrydentEngine.runContinuously(() -> {
                if(Mouse.isMouseDownOnce(MouseButton.PRIMARY)) {
                    Audio.createSoundEffect(sfx).setBalance(-1).setVolume(0.1).play();
                }
                if(Mouse.isMouseDownOnce(MouseButton.SECONDARY)) {
                    Audio.createSoundEffect(sfx).setBalance(1).setVolume(0.5).play();
                }
                if(Mouse.isMouseDownOnce(MouseButton.MIDDLE)) {
                    togglePlaying();
                }
            });
    }

}
