/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2011-2012, Xcoo, Inc.
 *
 *  casmi is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package casmi.tween;

import java.util.ArrayList;
import java.util.List;

import casmi.graphics.Graphics;

/**
 * Tween class.
 *
 * @author Y. Ban
 */
public class Tween {

    /** If you need to repeat your tween for infinity, use this. */
    public static final int INFINITY = -1;

    // -------------------------------------------------------------------------
    // Factories
    // -------------------------------------------------------------------------

//    /**
//     * Convenience method to create a new interpolation.
//     *
//     * <br/>
//     * <br/>
//     * You need to set the target values of the interpolation by using one of
//     * the ".target()" methods. The interpolation will run from the current
//     * values (retrieved after the delay, if any) to these target values.
//     *
//     * <br/>
//     * <br/>
//     * The following lines are equivalent (if pooling has been disabled):
//     *
//     * <br/>
//     * <br/>
//     *
//     * <pre>
//     * Tween.to(myObject, Types.POSITION, 1000, Quad.INOUT).target(50, 70);
//     * new Tween(myObject, Types.POSITION, 1000, Quad.INOUT).target(50, 70);
//     * </pre>
//     *
//     * Several options such as delays and callbacks can be added to the tween.
//     * This method hides some of the internal optimizations such as object reuse
//     * for convenience. However, you can control the creation of the tween by
//     * using the classic constructor.
//     *
//     * @param target
//     *            The target of the interpolation.
//     * @param tweenType
//     *            The desired type of interpolation.
//     * @param durationMillis
//     *            The duration of the interpolation, in milliseconds.
//     * @param equation
//     *            The easing equation used during the interpolation.
//     * @return The generated Tween.
//     * @see Tweenable
//     * @see TweenEquation
//     */
//    public static Tween to(Tweenable target, TweenType tweenType, int durationMillis,
//            TweenEquation equation) {
//        Tween tween = new Tween(target, tweenType, durationMillis, equation);
//        return tween;
//    }
//
////    public static Tween to(SimpleTweenable target, int durationMillis,
////            TweenEquation equation) {
////        Tween tween = new Tween(target, TweenType.POSITION, durationMillis, equation);
////        return tween;
////    }
//
//    /**
//     * Convenience method to create a new reversed interpolation.
//     *
//     * <br/>
//     * <br/>
//     * You need to set the target values of the interpolation by using one of
//     * the ".target()" methods. The interpolation will run from these target
//     * values to the current values (retrieved after the delay, if any).
//     *
//     * <br/>
//     * <br/>
//     * The following lines are equivalent (if pooling has been disabled):
//     *
//     * <br/>
//     * <br/>
//     *
//     * <pre>
//     * Tween.from(myObject, Types.POSITION, 1000, Quad.INOUT).target(50, 70);
//     * new Tween(myObject, Types.POSITION, 1000, Quad.INOUT).target(50, 70).reverse();
//     * </pre>
//     *
//     * Several options such as delays and callbacks can be added to the tween.
//     * This method hides some of the internal optimizations such as object reuse
//     * for convenience. However, you can control the creation of the tween by
//     * using the classic constructor.
//     *
//     * @param target
//     *            The target of the interpolation.
//     * @param tweenType
//     *            The desired type of interpolation.
//     * @param durationMillis
//     *            The duration of the interpolation, in milliseconds.
//     * @param equation
//     *            The easing equation used during the interpolation.
//     * @return The generated Tween.
//     * @see Tweenable
//     * @see TweenEquation
//     */
//    public static Tween from(Tweenable target, TweenType tweenType,
//            int durationMillis, TweenEquation equation) {
//        Tween tween = new Tween(target, tweenType, durationMillis, equation);
//        tween.reverse();
//        return tween;
//    }

//    public static Tween from(SimpleTweenable target, int durationMillis,
//            TweenEquation equation) {
//        Tween tween = new Tween(target, TweenType.POSITION, durationMillis, equation);
//        tween.reverse();
//        return tween;
//    }

    // -------------------------------------------------------------------------
    // Attributes
    // -------------------------------------------------------------------------

    // Main
    private Tweenable target;
    private TweenType tweenType;
    private TweenEquation equation;

    // General
    // isReversed: play reverse
    // isRelative: play object in relative time
    private boolean isReversed;
    private boolean isRelative;

    // Values
    private List<Float> startValues;
    private List<Float> targetValues;
    private List<Float> targetMinusStartValues;

    // Timings
    private long readyMillis;
    private long durationMillis;
    private long delayMillis;
    private long startMillis;
    private long endMillis;
    private boolean isReady;
    private boolean isStarted;
    private boolean isEnded;
    private boolean isCompleted;

    // Callback functions
    private final List<TweenCallback> readyCallbacks;
    private final List<TweenCallback> startCallbacks;
    private final List<TweenCallback> endCallbacks;
    private final List<TweenCallback> completeCallbacks;
    private final List<TweenCallback> killCallbacks;

    // Repeat
    private int repeatCnt;
    private int iteration;
    private long intervalMillis;
    private long nextStartMillis;

    // UserData
    private Object userData;

    private List<Float> currentValues;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Instantiates a new Tween from scratch.
     *
     * @param target
     *            The target of the interpolation.
     * @param tweenType
     *            The desired type of interpolation.
     * @param durationMillis
     *            The duration of the interpolation, in milliseconds.
     * @param equation
     *            The easing equation used during the interpolation.
     */


    private Tween(Tweenable target, TweenType tweenType, int durationMillis,
            TweenEquation equation) {
        startValues = new ArrayList<Float>();
        targetValues = new ArrayList<Float>();
        targetMinusStartValues = new ArrayList<Float>();
        currentValues = new ArrayList<Float>();

        readyCallbacks = new ArrayList<TweenCallback>();
        startCallbacks = new ArrayList<TweenCallback>();
        endCallbacks = new ArrayList<TweenCallback>();
        completeCallbacks = new ArrayList<TweenCallback>();
        killCallbacks = new ArrayList<TweenCallback>();

        reset();

        this.target = target;
        this.tweenType = tweenType;
        this.durationMillis = durationMillis;
        this.equation = equation;

        if (target != null) {
            this.currentValues = target
                    .getTweenValues(tweenType);
        }
    }

    /**
     * Clone a new Tween from scratch.
     */
    @Override
    public Tween clone(){
        Tween t = Tween.to(this.target,this.tweenType,(int)this.durationMillis,this.equation);
        t.startValues = this.startValues;
        t.targetValues = this.targetValues;
        t.currentValues = this.currentValues;
        return t;
    }

    /**
     * Sets the target value of the interpolation. - start value: value at start
     * time, after delay<br/>
     * - end value: param
     *
     * @param targetValue
     *            The target value of the interpolation.
     * @return The current tween for chaining instructions.
     */
    public Tween target(float targetValue) {
        targetValues.add(targetValue);
        return this;
    }

    public Tween target(double targetValue) {
        targetValues.add((float)targetValue);
        return this;
    }

    /**
     * Sets the target values of the interpolation.
     *
     * @param targetValue1
     *            The 1st target value of the interpolation.
     * @param targetValue2
     *            The 2nd target value of the interpolation.
     * @return The current tween for chaining instructions.
     */
    public Tween target(float targetValue1, float targetValue2) {
        targetValues.add(targetValue1);
        targetValues.add(targetValue2);
        return this;
    }

    public Tween target(double targetValue1, double targetValue2) {
        targetValues.add((float)targetValue1);
        targetValues.add((float)targetValue2);
        return this;
    }

    /**
     * Sets the target values of the interpolation.
     *
     * @param targetValue1
     *            The 1st target value of the interpolation.
     * @param targetValue2
     *            The 2nd target value of the interpolation.
     * @param targetValue3
     *            The 3rd target value of the interpolation.
     * @return The current tween for chaining instructions.
     */
    public Tween target(float targetValue1, float targetValue2,
            float targetValue3) {
        targetValues.add(targetValue1);
        targetValues.add(targetValue2);
        targetValues.add(targetValue3);
        return this;
    }

    public Tween target(double targetValue1, double targetValue2,
            double targetValue3) {
        targetValues.add((float)targetValue1);
        targetValues.add((float)targetValue2);
        targetValues.add((float)targetValue3);
        return this;
    }

    /**
     * Sets the target values of the interpolation. The interpolation will run
     * from the <b>values at start time (after the delay, if any)</b> to these
     * target values.
     *
     * @param targetValues
     *            The target values of the interpolation.
     * @return The current tween for chaining instructions.
     */
    public Tween target(float... targetValues) {
        this.targetValues.clear();
        for(Float t: targetValues){
            this.targetValues.add(t);
        }
        return this;
    }

    /**
     * Sets the target value of the interpolation, relatively to the <b>value at
     * start time (after the delay, if any).
     *
     * @param targetValue
     *            The relative target value of the interpolation.
     * @return The current tween for chaining instructions.
     */
    public Tween targetRelative(float targetValue) {
        isRelative = true;
        targetValues.add(targetValue);
        return this;
    }

    /**
     * Sets the target values of the interpolation, relatively to the <b>values
     * at start time (after the delay, if any)</b>.
     *
     * @param targetValue1
     *            The 1st relative target value of the interpolation.
     * @param targetValue2
     *            The 2nd relative target value of the interpolation.
     * @return The current tween for chaining instructions.
     */
    public Tween targetRelative(float targetValue1, float targetValue2) {
        isRelative = true;
        targetValues.add(targetValue1);
        targetValues.add(targetValue2);
        return this;
    }

    /**
     * Sets the target values of the interpolation, relatively to the <b>values
     * at start time (after the delay, if any)
     *
     * @param targetValue1
     *            The 1st relative target value of the interpolation.
     * @param targetValue2
     *            The 2nd relative target value of the interpolation.
     * @param targetValue3
     *            The 3rd relative target value of the interpolation.
     * @return The current tween for chaining instructions.
     */
    public Tween targetRelative(float targetValue1, float targetValue2,
            float targetValue3) {
        isRelative = true;
        targetValues.add(targetValue1);
        targetValues.add(targetValue2);
        targetValues.add(targetValue3);
        return this;
    }

    /**
     * Sets the target values of the interpolation, relatively to the <b>values
     * at start time (after the delay, if any)</b>.
     *
     * @param targetValues
     *            The relative target values of the interpolation.
     * @return The current tween for chaining instructions.
     */
    public Tween targetRelative(float... targetValues) {
        target(targetValues);
        isRelative = true;
        return this;
    }

    /**
     * Sets the target value(s) of the interpolation as <b>the current value(s),
     * the one(s) present when this call is made</b>. - start value: value at
     * start time, after delay<br/>
     * - end value: value at current time
     *
     * @return The current tween for chaining instructions.
     */
    public Tween targetCurrent() {
        this.targetValues = target.getTweenValues(tweenType);
        return this;
    }

    /**
     * Kills the interpolation.
     */
    public void kill() {
        isCompleted = true;
        callKillCallbacks();
    }

    /**
     * Adds a delay to the tween.
     *
     * @param millis
     *            The delay, in milliseconds.
     * @return The current tween for chaining instructions.
     */
    @Override
    public Tween addDelay(long millis) {
        this.delayMillis += millis;
        return this;
    }

    /**
     * Adds a callback to the tween. The callback is triggered when start() is
     * called on the tween.
     *
     * @param callback
     *            A tween callback.
     * @return The current tween for chaining instructions.
     */
    public Tween addStartCallback(TweenCallback callback) {
        readyCallbacks.add(callback);
        return this;
    }

    /**
     * Adds a callback to the tween. The callback is triggered at the end of the
     * delay.
     *
     * @param callback
     *            A tween callback.
     * @return The current tween for chaining instructions.
     */
    public Tween addEndOfDelayCallback(TweenCallback callback) {
        startCallbacks.add(callback);
        return this;
    }

    /**
     * Adds a callback to the tween. The callback is triggered on each iteration
     * ending. If no repeat behavior was specified, this callback is similar to
     * a Types.COMPLETE callback.
     *
     * @param callback
     *            A tween callback.
     * @return The current tween for chaining instructions.
     */
    public Tween addIterationCompleteCallback(TweenCallback callback) {
        endCallbacks.add(callback);
        return this;
    }

    /**
     * Adds a callback to the tween. The callback is triggered at the end of the
     * tween.
     *
     * @param callback
     *            A tween callback.
     * @return The current tween for chaining instructions.
     */
    public Tween addCompleteCallback(TweenCallback callback) {
        completeCallbacks.add(callback);
        return this;
    }

    /**
     * Adds a callback to the tween. The callback is triggered if the tween is
     * manually killed.
     *
     * @param callback
     *            A tween callback.
     * @return The current tween for chaining instructions.
     */
    public Tween addKillCallback(TweenCallback callback) {
        killCallbacks.add(callback);
        return this;
    }

    /**
     * Repeats the tween for a given number of times.
     *
     * @param count
     *            The number of desired repetition. For infinite repetition, use
     *            Tween.INFINITY, or a negative number.
     * @param delayMillis
     *            A delay before each repetition.
     * @return The current tween for chaining instructions.
     */
    public Tween repeat(int count, int delayMillis) {
        repeatCnt = count;
        intervalMillis = delayMillis;
        return this;
    }

    /**
     * Reverse the tween. Will interpolate from target values to the current
     * values if not already reversed.
     *
     * @return The current tween for chaining instructions.
     */
    public Tween reverse() {
        isReversed = !isReversed;
        return this;
    }

    /**
     * Sets an object attached to this tween. It can be useful in order to
     * retrieve some data from a TweenCallback.
     *
     * @param data
     *            Any kind of object.
     * @return The current tween for chaining instructions.
     */
    public Tween setUserData(Object data) {
        userData = data;
        return this;
    }


    /**
     * Gets the tween target.
     *
     * @return The tween target.
     */
    public Tweenable getTarget() {
        return target;
    }

    /**
     * Gets the tween type.
     *
     * @return The tween type.
     */
    public TweenType getTweenType() {
        return tweenType;
    }

    /**
     * Gets the tween easing equation.
     *
     * @return The tween easing equation.
     */
    public TweenEquation getEquation() {
        return equation;
    }

    /**
     * Gets the tween target values.
     *
     * @return The tween target values.
     */
    public List<Float> getTargetValues() {
        return targetValues;
    }

    /**
     * Gets the tween duration.
     *
     * @return The tween duration.
     */
    @Override
    public long getDuration() {
        return durationMillis;
    }

    /**
     * Gets the tween delay.
     *
     * @return The tween delay.
     */
    @Override
    public long getDelay() {
        return delayMillis;
    }

    /**
     * Getsthe total number of repetitions.
     *
     * @return The total number of repetitions.
     */
    public int getRepeatCount() {
        return repeatCnt;
    }

    /**
     * Gets the delay before each repetition.
     *
     * @return The delay before each repetition.
     */
    public long getRepeatDelay() {
        return intervalMillis;
    }

    /**
     * Gets the number of remaining iterations.
     *
     * @return The number of remaining iterations.
     */
    public int getRemainingIterationCount() {
        return repeatCnt - iteration;
    }

    /**
     * Returns true if the tween is finished (i.e. if the tween has reached its
     * end or has been killed). If this is the case and tween pooling is
     * enabled, the tween should no longer been used, since it will be reset and
     * returned to the pool.
     *
     * @return True if the tween is finished.
     */
    public boolean isFinished() {
        return isCompleted;
    }

    /**
     * Gets the attached user data, or null if none.
     *
     * @return The attached user data.
     */
    public Object getUserData() {
        return userData;
    }

    /**
     * Starts and Restart the interpolation. Using this method can lead to some
     * side-effects if you call it multiple times.
     */
    Tween start(long currentMillis, boolean repeat) {
        readyMillis = currentMillis;
        if(repeat)
            startMillis = readyMillis;
        else
            startMillis = readyMillis + delayMillis;

        if (iteration > 0 && intervalMillis < 0)
            startMillis = Math.max(startMillis + intervalMillis,
                    readyMillis);

        endMillis = startMillis + durationMillis;
        nextStartMillis = Math.max(endMillis, endMillis
                + intervalMillis);

        isReady = true;
        isStarted = false;
        isEnded = false;
        isCompleted = false;

        callReadyCallbacks();

        return this;
    }

    /**
     * Updates the tween state. Using this method can be unsafe if tween pooling
     * was first enabled.
     */
    final void render(Graphics g, long currentMillis) {

//        this.g = g;

        if (!isReady)
            return;

        if (checkRepetition(currentMillis)){
            iteration += 1;
            start(System.currentTimeMillis(), true);
            return;
        }

        if (isEnded) {
            target.end(g, tweenType);
            return;
        }

        if (checkStart(currentMillis) && !isStarted) {


            isStarted = true;
            if (iteration > 0 && target != null) {
                target.update(g, tweenType, startValues);
            } else if (target != null) {
                this.startValues = target.getTweenValues(tweenType);

                int size = startValues.size();
                targetMinusStartValues.clear();

                for (int i = 0; i < size; i++) {

                    if( isRelative ) {
                        targetValues.set(i, startValues.get(i) + targetValues.get(i));
                    }
                    targetMinusStartValues.add(targetValues.get(i) - startValues.get(i));
                }
            }
            callStartCallbacks();
        }else if(!isStarted){
            return;
        }

        if (checkEnd(currentMillis) && !isEnded) {
            isEnded = true;

            if (target != null) {

                int size = startValues.size();

                for (int i = 0; i < size; i++) {
                    currentValues.set(i, isReversed
                            ? targetValues.get(i) - targetMinusStartValues.get(i)
                            : startValues.get(i) + targetMinusStartValues.get(i));
                }
                target.update(g, tweenType, currentValues);
            }

            if (isRepeat()) {
                callIterationCompleteCallbacks();
            } else {
                isCompleted = true;
                callIterationCompleteCallbacks();
                callCompleteCallbacks();
            }
            return;
        }

        // update
        if (target != null) {

            int size = currentValues.size();

            for (int i = 0; i < size; i++) {
                currentValues.set(i,
                    equation.compute(currentMillis - startMillis,
                                     isReversed ? targetValues.get(i) : startValues.get(i),
                                     isReversed ? - targetMinusStartValues.get(i) : targetMinusStartValues.get(i),
                                    durationMillis)
                                 );
            }
            target.update(g, tweenType, currentValues);
        }
    }

    private final boolean checkRepetition(long currentMillis) {
        return isRepeat() && currentMillis >= nextStartMillis;
    }

    private final boolean checkStart(long currentMillis) {
        return currentMillis >= startMillis;
    }

    private final boolean checkEnd(long currentMillis) {
        return currentMillis >= endMillis;
    }

    public Tweenable getTweenable() {
        return this.target;
    }

    private void reset() {
        this.target = null;
        this.tweenType = TweenType.NONE;
        this.equation = null;

        this.isReversed = false;
        this.isRelative = false;

        this.delayMillis = 0;
        this.isReady = false;
        this.isStarted = false;
        this.isEnded = false;
        this.isCompleted = true;

        this.completeCallbacks.clear();
        this.endCallbacks.clear();
        this.killCallbacks.clear();
        this.readyCallbacks.clear();
        this.startCallbacks.clear();

        this.repeatCnt = 0;
        this.iteration = 0;
        this.intervalMillis = 0;

        this.userData = null;
    }

    private boolean isRepeat() {
        return (repeatCnt < 0) || (iteration < repeatCnt);
    }

    private void callReadyCallbacks() {
        for (TweenCallback c : readyCallbacks) {
            c.run(TweenCallbackTypes.START, this);
        }
    }

    private void callStartCallbacks() {
        for (TweenCallback c : startCallbacks) {
            c.run(TweenCallbackTypes.END_OF_DELAY, this);
        }
    }

    private void callIterationCompleteCallbacks() {
        for (TweenCallback c : endCallbacks) {
            c.run(TweenCallbackTypes.ITERATION_COMPLETE, this);
        }
    }

    private void callCompleteCallbacks() {
        for (TweenCallback c : completeCallbacks) {
            c.run(TweenCallbackTypes.COMPLETE, this);
        }
    }

    private void callKillCallbacks() {
        for (TweenCallback c : killCallbacks) {
            c.run(TweenCallbackTypes.KILL, this);
        }
    }
}
