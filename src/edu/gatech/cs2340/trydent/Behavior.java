package edu.gatech.cs2340.trydent;

public abstract class Behavior extends ContinuousEvent {

    private GameObject object;
    
    public Behavior(GameObject object) {
        this.object = object;
    }
    
    public GameObject getGameObject() {
        return object;
    }

}
