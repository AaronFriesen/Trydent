package edu.gatech.cs2340.trydent;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * A basic class for using audio.
 */
public class Audio {
    public static final String NO_MUSIC = "";
    public static final double MIN_VOLUME = 0.0;
    public static final double MAX_VOLUME = 1.0;

    private static double masterVolume = MAX_VOLUME;
    private static double musicMolume = MAX_VOLUME;
    private static Set<MediaContainer> soundEffects = new HashSet<>();
    private static MediaPlayer music;
    private static boolean playing;

    /**
     * Sets the background music for the game.
     * By default, does not play immediately.
     * @param musicFilename the path to the music file
     */
    public static void setMusic(String musicFilename) {
        if(music != null) {
            music.stop();
            music.dispose();
        }
        switch(musicFilename) {
        case NO_MUSIC:
            break;
        default:
            music = new MediaPlayer(new Media(musicFilename));
            music.setCycleCount(MediaPlayer.INDEFINITE);
            music.setVolume(masterVolume * musicMolume);
            if(playing) music.play();
            break;
        }
    }

    /**
     * Allows the creation of sound effects
     * @param soundEffectFilename the path to the sound effect
     * @return a builder object for creating sound effects
     */
    public static SoundEffectBuilder createSoundEffect(String soundEffectFilename) {
        return new SoundEffectBuilder(soundEffectFilename);
    }

    /**
     * Sets the master volume, which affects both music and sound effects
     * Volume is calculated by MasterVolumn * IndividualVolumn
     * @param newMasterVolume the new master volume (should be between 0.0 and 1.0)
     */
    public static void setMasterVolume(double newMasterVolume){
        masterVolume = newMasterVolume;
        if(music != null) {
            music.setVolume(masterVolume * musicMolume);
        }
        for(MediaContainer soundEffect : soundEffects) {
            soundEffect.player.setVolume(masterVolume * soundEffect.playerVolume);
        }
    }

    /**
     * Sets the volume for music.
     * This value is always multiplied by the master volume,
     * so the resulting volumne is MasterVolume * new_music
     * @param newMusicVolume the new music volume
     */
    public static void setMusicVolume(double newMusicVolume){
        musicMolume = newMusicVolume;
        if(music != null) {
            music.setVolume(masterVolume * musicMolume);
        }
    }

    /**
     * Pauses all of the audio.
     * This method is useful for when the game is paused or is minimized.
     */
    public static void pauseAudio() {
        playing = false;
        if(music != null) {
            music.pause();
        }
        for(MediaContainer soundEffect : soundEffects) {
            soundEffect.player.pause();
        }
    }

    /**
     * Resumes audio or starts playing.
     */
    public static void resumeAudio() {
        playing = true;
        if(music != null) {
            music.play();
        }
        for(MediaContainer soundEffect : soundEffects){
            soundEffect.player.play();
        }
    }

    /**
     * Determines if the audio is currently playing.
     * @return true if currently playing
     */
    public static boolean isPlaying(){
        return playing;
    }

    /**
     * A builder class for creating sound effects.
     */
    public static class SoundEffectBuilder {
        private MediaPlayer soundEffect;
        private double volume;
        private Runnable callback;

        private SoundEffectBuilder(String soundEffectFilename) {
            soundEffect = new MediaPlayer(new Media(soundEffectFilename));
            soundEffect.setCycleCount(1);
            soundEffect.setVolume(masterVolume);
            callback = () -> {};
        }

        /**
         * This method sets the balance for a sound effect.
         * This can be used to add a sense of direction to sound.
         * @param balance a value between -1.0 (left) to 1.0 (right)
         * @return current sound effect being built
         */
        public SoundEffectBuilder setBalance(double balance){
            soundEffect.setBalance(balance);
            return this;
        }

        /**
         * Sets the volume of the sound effect.
         * There is an implicit multiplication by the master volume
         * @param volume the volume value between 0.0 and 1.0
         * @return current sound effect being built
         */
        public SoundEffectBuilder setVolume(double volume){
            this.volume = volume;
            soundEffect.setVolume(masterVolume * volume);
            return this;
        }

        /**
         * Sets a callback for when the sound effect finishes.
         * @param callback some action to occur when the sound effect finishes playing
         * @return current sound effect being built
         */
        public SoundEffectBuilder setCallback(Runnable callback){
            this.callback = callback;
            return this;
        }

        /**
         * Plays the built sound effect.
         */
        public void play(){
            soundEffects.add(new MediaContainer(soundEffect, volume));
            soundEffect.play();
            soundEffect.setOnEndOfMedia(() -> {
                    TrydentEngine.runOnce(() -> {
                            soundEffects.remove(soundEffect);
                            soundEffect.dispose();
                            callback.run();
                        });
                });
        }
    }

    private static class MediaContainer {
        MediaPlayer player;
        double playerVolume;

        MediaContainer(MediaPlayer player, double playerVolume){
            this.player = player;
            this.playerVolume = playerVolume;
        }
    }
}
