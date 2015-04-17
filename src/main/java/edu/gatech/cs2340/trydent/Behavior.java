package edu.gatech.cs2340.trydent;

/**
 * Behavior class for adding scripts (behaviors) to GameObjects.
 *
 * @author Garrett Malmquist
 */
public abstract class Behavior extends ContinuousEvent {

    private GameObject object;

    public Behavior(GameObject object) {
        this.object = object;
    }

    public GameObject getGameObject() {
        return object;
    }

    @Override
    public void onPreUpdate() {
        super.onPreUpdate();
        if (getGameObject().isDestroyed()) {
            stop();
        }
    }

}
