package edu.gatech.cs2340.trydent.internal;

import javafx.scene.Group;
import javafx.scene.Scene;

/**
 * Class to manage starting/stopping JavaFX.
 * @author Garrett Malmquist
 */
public interface JavaFXManager {

    public void startJavaFX();
    
    public void stopJavaFX();
    
    public void setUpdateAction(Runnable updateAction);
    
    public boolean isRunning();
    
    public void setWindowTitle(String title);
    
    public void setWindowSize(int width, int height);
    
    public void setFullscreen(boolean fullscreen);
    
    public Scene getScene();
    
    public Group getRoot();
    
}
