package edu.gatech.cs2340.trydent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import edu.gatech.cs2340.trydent.animation.Animation;
import edu.gatech.cs2340.trydent.animation.AnimationEvent;
import edu.gatech.cs2340.trydent.animation.AnimationListener;
import edu.gatech.cs2340.trydent.animation.DispatchAnimationListener;
import edu.gatech.cs2340.trydent.internal.TrydentInternalException;
import edu.gatech.cs2340.trydent.math.BaseVector;
import edu.gatech.cs2340.trydent.math.MathTools;
import edu.gatech.cs2340.trydent.math.Orientation;
import edu.gatech.cs2340.trydent.math.Position;
import edu.gatech.cs2340.trydent.math.Scale;
import edu.gatech.cs2340.trydent.math.curve.IndexWrapMode;
import edu.gatech.cs2340.trydent.math.curve.TimeWrapMode;

/**
 * Basic GameObject all visual elements of a game should either use directly or
 * extend.
 *
 * <p>
 * Core functionality of GameObjects includes arbitrary positioning, scaling,
 * and rotating in the scene graph, display of JavaFXNodes, and animation.
 *
 * <p>
 * <strong>GameObject generic `Features'</strong>
 * <p>
 * Features are similar to `Components' in similar game engines; essentially,
 * they are objects that can be looked up by the classes they are instances of.
 * This can be used to extend GameObjects with generic functionality.
 * <p>
 * For example, say you want to add to GameObject the ability to serialize
 * themselves -- that is, the ability to store a String representation of
 * themselves, for use in a save-game feature. You might do the following:
 * <p>
 *
 * <pre>
 * public interface Serializer {
 *   public String serialize();
 * }
 * ... (in some other file) ...
 * GameObject bulletGameObject = ...
 * Serializer s = new BulletSerializer();
 * bulletGameObject.addFeature(s);
 * </pre>
 * <p>
 * Then later on, you might use the serializer as such:
 *
 * <pre>
 * public void saveGameObjects(Collection&lt;GameObject&gt; objects, PrintStream file) {
 *     for (GameObject g : objects) {
 *         if (g.hasFeature(Serializer.class)) {
 *             file.println(g.getFeature(Serializer.class).serialize());
 *         }
 *     }
 * }
 * </pre>
 *
 * @author Garrett Malmquist
 *
 */
public class GameObject {

    private String name = "Untitled GameObject";

    private Group fxNode;
    private GameObject parent;

    // Only used for deletion.
    private List<GameObject> children;

    private Transform localRotate;
    private Transform localScale;
    private Transform localTranslate;

    private Animation animation;
    private double animationStartTime;
    private int animationLoopCounter = 0;

    private DispatchAnimationListener animationListener;
    private volatile boolean animationPaused = false;

    private boolean isDestroyed = false;

    private Map<Class<?>, Set<Object>> features;

    /**
     * Creates a new GameObject with the given name.
     *
     * @param name
     *            the name of the GameObject.
     */
    public GameObject(String name) {
        this();
        this.name = name;
    }

    /**
     * Creates a new unnamed GameObject.
     */
    public GameObject() {
        if (!TrydentEngine.isRunning())
            throw new TrydentException("Cannot instantiate GameObject before TrydentEngine has been started.");

        this.children = new LinkedList<>();
        this.features = new HashMap<>();

        // We initialize all GameObjects to be groups, so that they can easily
        // have children. GameObjects which are more than just empty will simply
        // have children that include the "actual" thing they seem to represent
        // (eg, an image view), but the user need not know that.
        this.fxNode = new Group();
        // By default, our javafx Node parent is the scene root, which
        // corresponds
        // to a 'null' GameObject parent.
        TrydentEngine.getRootNode().getChildren().add(fxNode);

        // Identity
        localRotate = Transform.affine(1, 0, 0, 1, 0, 0);
        localScale = Transform.affine(1, 0, 0, 1, 0, 0);
        localTranslate = Transform.affine(1, 0, 0, 1, 0, 0);

        setMatrices(localTranslate, localRotate, localScale);

        createAnimationBehavior();

        animationListener = new DispatchAnimationListener();
    }

    /**
     * Creates a new GameObject displaying the given JavaFX node.
     *
     * @param javaFXObject
     *            the JavaFX node to display
     */
    public GameObject(Node javaFXObject) {
        this();
        this.fxNode.getChildren().add(javaFXObject);
    }

    /**
     * Creates a new GameObject with the given name and JavaFX node.
     *
     * @param name
     *            the name of the GameObject
     * @param javaFXObject
     *            the JavaFX node to display
     */
    public GameObject(String name, Node javaFXObject) {
        this(javaFXObject);
        this.name = name;
    }

    /**
     * Creates a new GameObject displaying the JavaFX node generated from the
     * parameter.
     *
     * @param node
     *            the object which can generate javafx nodes.
     */
    public GameObject(JavaFxConvertable node) {
        this(node.toJavaFxNode());
    }

    /**
     * Creates a new GameObject displaying the JavaFX node generated from the
     * parameter.
     *
     * @param name
     *            the name of this GameObject.
     * @param node
     *            the object which can generate javafx nodes.
     */
    public GameObject(String name, JavaFxConvertable node) {
        this(name, node.toJavaFxNode());
    }

    /**
     * Associates a new `feature' with this GameObject.
     * <p>
     * {@see edu.gatech.cs2340.trydent.GameObject} for a discussion of what
     * features are and how to use them.
     *
     * @param feature
     *            the Object to add as a feature.
     */
    public <T> void addFeature(T feature) {
        Class<?> key = feature.getClass();
        if (!features.containsKey(key)) {
            features.put(key, new HashSet<Object>());
        }
        features.get(key).add(feature);
    }

    /**
     * Retrieves a `feature' of the given type associated with this GameObject.
     * <p>
     * {@see edu.gatech.cs2340.trydent.GameObject} for a discussion of what
     * features are and how to use them.
     *
     * @param type
     *            the type (or supertype) of the feature to retrieve.
     * @return the feature if it exists, null otherwise.
     */
    @SuppressWarnings("unchecked")
    public <T> T getFeature(Class<T> type) {
        if (features.containsKey(type)) {
            for (Object o : features.get(type)) {
                return (T) o;
            }
        }

        for (Class<?> key : features.keySet()) {
            if (type.isAssignableFrom(key)) {
                for (Object o : features.get(key)) {
                    return (T) o;
                }
            }
        }

        return null;
    }

    /**
     * Retrieves all `features' of the given type associated with this
     * GameObject.
     * <p>
     * {@see edu.gatech.cs2340.trydent.GameObject} for a discussion of what
     * features are and how to use them.
     *
     * @param type
     *            the type (or supertype) of the features to retrieve. E.g.,
     *            passing in `Object.class' will retrieve <i>all</i> features on
     *            this game objects, because all features are subclasses of the
     *            java Object superclass.
     * @return an Iterable over all features of the given type that this object
     *         contains.
     */
    @SuppressWarnings("unchecked")
    public <T> Iterable<T> getFeatures(Class<T> type) {
        Set<T> iterable = new HashSet<>();
        for (Class<?> key : features.keySet()) {
            if (type.isAssignableFrom(key)) {
                for (Object o : features.get(key)) {
                    iterable.add((T) o);
                }
            }
        }
        return iterable;
    }

    /**
     * Checks whether this object contains a `feature' of the given type
     * associated with this GameObject.
     * <p>
     * {@see edu.gatech.cs2340.trydent.GameObject} for a discussion of what
     * features are and how to use them.
     *
     * @param type
     *            the type (or supertype) of the feature to check for.
     * @return true if this object has a feature of the given type, false
     *         otherwise.
     */
    public <T> boolean hasFeature(Class<T> type) {
        return getFeature(type) != null;
    }

    /**
     * Removes all `features' of (or subclasses of) the given type associated
     * with this GameObject.
     * <p>
     * {@see edu.gatech.cs2340.trydent.GameObject} for a discussion of what
     * features are and how to use them.
     *
     * @param type
     *            the type (or supertype) of the features to remove.
     */
    public <T> void removeAllFeatures(Class<T> type) {
        Set<Class<?>> toRemove = new HashSet<>();
        for (Class<?> key : features.keySet()) {
            if (type.isAssignableFrom(key)) {
                toRemove.add(key);
            }
        }
        for (Class<?> key : toRemove) {
            features.remove(key);
        }
    }

    /**
     * Removes the `feature' of (or a subclass of) the given type associated
     * with this GameObject.
     * <p>
     * {@see edu.gatech.cs2340.trydent.GameObject} for a discussion of what
     * features are and how to use them.
     *
     * @param type
     *            the type (or supertype) of the feature to remove.
     */
    public <T> boolean removeFeature(T feature) {
        for (Class<?> key : features.keySet()) {
            if (feature.getClass().isAssignableFrom(key)) {
                if (features.get(key).remove(feature)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sets the fill of the underlying javafx node, if applicable.
     *
     * @param paint
     */
    public void setFill(Paint paint) {
        if (fxNode != null && fxNode.getChildren().size() > 0) {
            Node child = fxNode.getChildren().get(0);
            if (child != null && child instanceof Shape) {
                ((Shape) child).setFill(paint);
            }
        }
    }

    /**
     * Adds the animation listener.
     *
     * @param listener
     *            AnimationListener object to receive AnimationEvents.
     */
    public void addAnimationListener(AnimationListener listener) {
        animationListener.addAnimationListener(listener);
    }

    /**
     * Removes the animation listener.
     *
     * @param listener
     *            AnimationListener to remove.
     */
    public void removeAnimationListener(AnimationListener listener) {
        animationListener.removeAnimationListener(listener);
    }

    /**
     * Removes all animation listeners.
     */
    public void clearAnimationListeners() {
        animationListener.clearAnimationListeners();
    }

    /**
     * Plays the given animation, stopping any currently playing animations.
     * Animations modify the position, rotate, and scale of an object over time.
     * <p>
     *
     * @param animation
     *            the animation to play
     */
    public void playAnimation(Animation animation) {
        if (this.animation != null) {
            animationListener.animationInterrupted(new AnimationEvent(this, this.animation));
        }
        this.animation = animation;
        this.animationStartTime = Time.getTime();
        this.animationLoopCounter = 1;
        animation.setTimeWrap(TimeWrapMode.CLAMP);
        animationPaused = false;
        animationListener.animationStarted(new AnimationEvent(this, animation));
    }

    /**
     * Plays the given animation, stopping any currently playing animations, and
     * loops it count number of times.
     * <p>
     * Animations modify the position, rotate, and scale of an object over time.
     *
     * @param animation
     *            the animation to loop
     * @param count
     *            The number of times to play the animation. count = 0 will not
     *            play anything, count = 1 will play the animation exactly once.
     *            A negative value for count will cause the animation to loop
     *            infinitely.
     */
    public void loopAnimation(Animation animation, int count) {
        if (this.animation != null) {
            animationListener.animationInterrupted(new AnimationEvent(this, this.animation));
        }
        this.animation = animation;
        this.animationStartTime = Time.getTime();
        this.animationLoopCounter = count;
        animation.setTimeWrap(TimeWrapMode.WRAP);
        animation.setIndexWrap(IndexWrapMode.WRAP);
        animationPaused = false;
        animationListener.animationStarted(new AnimationEvent(this, animation));
    }

    /**
     * Plays the given animation in a loop forever.
     * <p>
     * Animations modify the position, rotation, and scale of an object over
     * time.
     *
     * @param animation
     *            the animation to loop
     */
    public void loopAnimation(Animation animation) {
        loopAnimation(animation, -1);
    }

    /**
     * Pauses or unpauses the currently playing animation (if any).
     *
     * @param paused
     *            whether to pause or play the animation.
     */
    public void setAnimationPaused(boolean paused) {
        if (this.animationPaused == paused)
            return;

        this.animationPaused = paused;
        if (this.animation != null) {
            if (paused) {
                animationListener.animationPaused(new AnimationEvent(this, animation));
            } else {
                animationListener.animationUnpaused(new AnimationEvent(this, animation));
            }
        }
    }

    /**
     * Stops the currently playing animation.
     */
    public void stopAnimation() {
        if (this.animation != null) {
            animationListener.animationStopped(new AnimationEvent(this, animation));
        }
        this.animation = null;
    }

    private void createAnimationBehavior() {
        new Behavior(this) {
            @Override
            public void onUpdate() {
                GameObject g = getGameObject();

                if (g.animation == null) {
                    return;
                }

                if (g.animationPaused) {
                    // Simulate pausing by moving the start-time forward.
                    g.animationStartTime += Time.getTimePassed();
                    return; // No updates should occur, so don't bother.
                }

                double time = Time.getTime() - g.animationStartTime;
                if (g.animationLoopCounter >= 0 && time > g.animationLoopCounter * g.animation.getDuration()) {
                    animationListener.animationEnded(new AnimationEvent(g, animation));
                    g.animation = null;
                    return;
                } else {
                    int lastLoopIndex = (int) (Math.max(0, time - Time.getTimePassed()) / g.animation.getDuration());
                    int currLoopIndex = (int) (Math.max(0, time) / g.animation.getDuration());
                    if (currLoopIndex > lastLoopIndex) {
                        animationListener.animationLooped(new AnimationEvent(g, animation));
                    }
                }
                g.setLocalOrientation(g.animation.sample(time));
            }
        };
    }

    private void setMatrices(Transform translate, Transform rotate, Transform scale) {
        List<Transform> transforms = fxNode.getTransforms();
        if (transforms.size() >= 3) {
            // This should happen every time except for initialization.
            for (int i = 0; i < 3; i++) {
                transforms.remove(transforms.size() - 1);
            }
        }
        transforms.add(localTranslate = translate);
        transforms.add(localRotate = rotate);
        transforms.add(localScale = scale);
    }

    private void setTranslateMatrix(Transform translate) {
        setMatrices(translate, localRotate, localScale);
    }

    private void setRotateMatrix(Transform rotate) {
        setMatrices(localTranslate, rotate, localScale);
    }

    private void setScaleMatrix(Transform scale) {
        setMatrices(localTranslate, localRotate, scale);
    }

    /**
     * Sets the global orientation of this object.
     *
     * @param orientation
     *            orientation object with position, rotation, and scale.
     */
    public void setOrientation(Orientation orientation) {
        setPosition(orientation.getPosition());
        setRotation(orientation.getRotation());
        setScale(orientation.getScale());
    }

    /**
     * Sets the local orientation of this object.
     *
     * @param orientation
     *            orientation object with position, rotation, and scale.
     */
    public void setLocalOrientation(Orientation orientation) {
        setLocalPosition(orientation.getPosition());
        setLocalRotation(orientation.getRotation());
        setLocalScale(orientation.getScale());
    }

    /**
     * Returns the global orientation of this object.
     *
     * @return the orientation object containing position, rotation, and scale
     */
    public Orientation getOrientation() {
        return new Orientation(getPosition(), getRotation(), getScale());
    }

    /**
     * Returns the local orientation of this object.
     *
     * @return the orientation object containing position, rotation, and scale
     */
    public Orientation getLocalOrientation() {
        return new Orientation(getLocalPosition(), getLocalRotation(), getLocalScale());
    }

    /**
     * Sets the global position.
     *
     * @param position
     *            the 2D position
     */
    public void setPosition(Position position) {
        try {
            Point2D p = getParentFxNode().getLocalToSceneTransform().inverseTransform(position.getX(), position.getY());
            setTranslateMatrix(Transform.translate(p.getX(), p.getY()));
        } catch (NonInvertibleTransformException e) {
            throw new TrydentInternalException("Local -> Scene not invertable! " + e);
        }
    }

    /**
     * Sets the global rotation in degrees.
     *
     * @param rotation
     *            the rotation about the Z axis
     */
    public void setRotation(double rotation) {
        double delta = rotation - getRotation();
        setLocalRotation(delta + getLocalRotation());
    }

    /**
     * Sets the global scale.
     *
     * @param scale
     *            the 2D scale
     */
    public void setScale(Scale scale) {
        if (scale.getX() == 0 || scale.getY() == 0) {
            throw new TrydentException("Setting the x or y scale to 0 is not a good idea (tried to set scale to "
                    + scale + ").");
        }

        Scale old = getScale();
        Scale change = scale.copy().scale(1f / old.getX(), 1f / old.getY());
        setLocalScale(change.scale(getLocalScale()));
    }

    /**
     * Sets the position of this object relative to its parent's rotation,
     * position, and scale.
     *
     * @param position
     *            the 2D position
     */
    public void setLocalPosition(Position position) {
        setTranslateMatrix(Transform.translate(position.getX(), position.getY()));
    }

    /**
     * Sets the rotation of this object relative to its parent's rotation in
     * degrees.
     *
     * @param rotation
     *            the rotation about the z axis
     */
    public void setLocalRotation(double rotation) {
        setRotateMatrix(new Rotate(rotation));
    }

    /**
     * Sets the scale of this object relative to its parent's scale.
     *
     * @param scale
     *            the 2D scale
     */
    public void setLocalScale(Scale scale) {
        if (scale.getX() == 0 || scale.getY() == 0) {
            throw new TrydentException("Setting the x or y scale to 0 is not a good idea (tried to set scale to "
                    + scale + ").");
        }
        setScaleMatrix(Transform.scale(scale.getX(), scale.getY()));
    }

    /**
     * Rotates this object in global space by the given amount.
     *
     * @param rotation
     *            - rotation in degrees.
     */
    public void rotate(double rotation) {
        setRotation(rotation + getRotation());
    }

    /**
     * Translates (aka moves) this object in global space by the given amount.
     *
     * @param x
     *            the x displacement
     * @param y
     *            the y displacement
     */
    public void translate(double x, double y) {
        setPosition(getPosition().add(x, y));
    }

    /**
     * Translates (aka moves) this object in global space by the given amount.
     *
     * @param by
     *            the 2D displacement
     */
    public void translate(BaseVector<?> by) {
        translate(by.getX(), by.getY());
    }

    /**
     * Scales this object by the given amount. Non-uniform scalings (ie not
     * scaling sx and sy by the same amount) are not recommended as they can
     * have undesirable effects (shearing) if this object has rotated children.
     *
     * @param sx
     *            the x scale
     * @param sy
     *            the y scale
     */
    public void scale(double sx, double sy) {
        fxNode.getTransforms().add(Transform.scale(sx, sy));
    }

    /**
     * Scales this object by the given amount. Non-uniform scalings (ie not
     * scaling sx and sy by the same amount) are not recommended as they can
     * have undesirable effects (shearing) if this object has rotated children.
     *
     * @param scale
     *            the 2D scale
     */
    public void scale(Scale scale) {
        scale(scale.getX(), scale.getY());
    }

    /**
     * Returns this objects global (x, y) position.
     *
     * @return the current position (changing the returned value will not affect
     *         the position of this object)
     */
    public Position getPosition() {
        return MathTools.getTranslation(fxNode.getLocalToSceneTransform());
    }

    /**
     * Returns this object's global rotation in degrees.
     *
     * @return the 2D rotation
     */
    public double getRotation() {
        return MathTools.getRotation(fxNode.getLocalToSceneTransform());
    }

    /**
     * Returns this object's x,y scale in global space.
     *
     * @return the 2D scale (changing the returned value will not affect the
     *         scale of this object)
     */
    public Scale getScale() {
        return MathTools.getScale(fxNode.getLocalToSceneTransform());
    }

    /**
     * Return's this object's position relative to its parent's position,
     * rotation, and scale.
     *
     * @return the 2D position (changing the returned value will not affect the
     *         position of this object)
     */
    public Position getLocalPosition() {
        return MathTools.getTranslation(fxNode.getLocalToParentTransform());
    }

    /**
     * Returns this object's rotation relative to its parent's rotation in
     * degrees.
     *
     * @return the 2D rotation
     */
    public double getLocalRotation() {
        return MathTools.getRotation(fxNode.getLocalToParentTransform());
    }

    /**
     * Returns this object's local (sx, sy) scale.
     *
     * @return the 2D scale (changing the returned value will not affect the
     *         scale of this object)
     */
    public Scale getLocalScale() {
        return MathTools.getScale(fxNode.getLocalToParentTransform());
    }

    /**
     * Returns this object's underlying JavaFX Group object. This allows child
     * classes to modify some properties directly, such as the children list.
     * This should be used with caution, to avoid breaking Trydent's scene
     * graph.
     *
     * @return
     */
    protected Group getFxNode() {
        return fxNode;
    }

    public boolean isChildOf(GameObject object) {
        for (GameObject obj = parent; obj != null; obj = obj.parent) {
            if (obj == object) {
                return true;
            }
        }
        return false;
    }

    public void setParent(GameObject object) {
        if (object != null && (object == this || object.isChildOf(this)))
            throw new TrydentException(
                    "Time-travel paradox detected: GameObjects cannot have their descendents as their parents.");

        if (fxNode == null)
            return;
        Position oldPos = getPosition();
        double oldRot = getRotation();
        Scale oldScale = getScale();

        getParentFxNode().getChildren().remove(fxNode);

        if (this.parent != null) {
            this.parent.children.remove(this);
        }

        this.parent = object;
        getParentFxNode().getChildren().add(fxNode);

        setScale(oldScale);
        setPosition(oldPos);
        setRotation(oldRot);

        if (this.parent != null) {
            this.parent.children.add(this);
        }
    }

    /**
     * Marks this object and its children for deletion.
     */
    public void destroy() {
        for (GameObject child : this.children) {
            child.destroy();
        }
        setParent(null);
        getParentFxNode().getChildren().remove(fxNode);
        isDestroyed = true;
        this.fxNode = null;
    }

    /**
     * Returns whether this object has been destroyed.
     *
     * @return
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    public GameObject getParent() {
        return parent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "GameObject[" + name + "]";
    }

    private Group getParentFxNode() {
        if (this.parent == null)
            return TrydentEngine.getRootNode();
        return this.parent.fxNode;
    }

}
