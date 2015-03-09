/**
 * Classes pertaining to animating the motion of game objects.
 * <p>
 * The most common use-case of this package is to create a new KeyframeAnimation object like so:
 * <pre>
 * Animation animation = KeyframeAnimation.create() // create animation builder
 *         // code to create keyframes
 *         .setPosition(...)
 *         .addKeyframe(duration1)
 *         .setPosition(...)
 *         .setRotation(...)
 *         .addKeyframe(duration2)
 *         .rotateBy(...)
 *         .addKeyframe() // last keyframe has no duration
 *         // ... et cetera
 *         .build(); // build the animation
 * </pre>
 */
package edu.gatech.cs2340.trydent.animation;