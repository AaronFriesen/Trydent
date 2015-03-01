package edu.gatech.cs2340.trydent;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import edu.gatech.cs2340.trydent.animation.Animation;
import edu.gatech.cs2340.trydent.internal.TrydentInternalException;
import edu.gatech.cs2340.trydent.math.BaseVector;
import edu.gatech.cs2340.trydent.math.MathTools;
import edu.gatech.cs2340.trydent.math.Orientation;
import edu.gatech.cs2340.trydent.math.Position;
import edu.gatech.cs2340.trydent.math.Scale;
import edu.gatech.cs2340.trydent.math.curve.TimeWrapMode;

/**
 * Basic GameObject all visual elements of a game should either use directly or
 * extend.
 *
 * @author Garrett Malmquist
 *
 */
public class GameObject {

    private String name = "Untitled GameObject";

    protected Group fxNode;
    private GameObject parent;

    private Transform localRotate;
    private Transform localScale;
    private Transform localTranslate;

    private Animation animation;
    private double animationStartTime;
    private int animationLoopCounter = 0;

    public GameObject(String name) {
        this();
        this.name = name;
    }

    public GameObject() {
        if (!TrydentEngine.isRunning())
            throw new TrydentException("Cannot instantiate GameObject before TrydentEngine has been started.");

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
    }

    public GameObject(Node javaFXObject) {
        this();
        this.fxNode.getChildren().add(javaFXObject);
    }

    public GameObject(String name, Node javaFXObject) {
        this(javaFXObject);
        this.name = name;
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
        this.animation = animation;
        this.animationStartTime = Time.getTime();
        this.animationLoopCounter = 1;
        animation.setTimeWrap(TimeWrapMode.CLAMP);
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
        this.animation = animation;
        this.animationStartTime = Time.getTime();
        this.animationLoopCounter = count;
        animation.setTimeWrap(TimeWrapMode.WRAP);
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
     * Stops the currently playing animation.
     */
    public void stopAnimation() {
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
                double time = Time.getTime() - g.animationStartTime;
                if (g.animationLoopCounter >= 0 && time > g.animationLoopCounter * g.animation.getDuration()) {
                    g.animation = null;
                    // TODO: add callbacks saying that the animation ended.
                    return;
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
        Position oldPos = getPosition();
        double oldRot = getRotation();
        Scale oldScale = getScale();

        getParentFxNode().getChildren().remove(fxNode);

        this.parent = object;
        getParentFxNode().getChildren().add(fxNode);

        setScale(oldScale);
        setPosition(oldPos);
        setRotation(oldRot);
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
