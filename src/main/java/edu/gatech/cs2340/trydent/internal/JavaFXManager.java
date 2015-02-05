package edu.gatech.cs2340.trydent.internal;

import javafx.scene.Group;
import javafx.scene.Scene;

/**
 * Class to manage starting/stopping JavaFX.
 *
 * @author Garrett Malmquist
 */
public interface JavaFXManager {

    void startJavaFX();

    void stopJavaFX();

    void setUpdateAction(Runnable updateAction);

    boolean isRunning();

    void setWindowTitle(String title);

    void setWindowSize(int width, int height);

    void setFullscreen(boolean fullscreen);

    Scene getScene();

    Group getRoot();

}
