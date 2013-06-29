/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2011, Xcoo, Inc.
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



/**
 * Tween Animation
 *
 * @author Takashi AOKI <federkasten@me.com>
 *
 */
public class TweenAnimation {

    enum TweenAnimationStatus {
        WAIT,
        STARTED,
    }

    private TweenAnimationStatus status = TweenAnimationStatus.WAIT;

    private AnimationTarget target;
    private double duration;

    private double startValue;
    private double endValue;
    private double value;

    private boolean repeat;
    private long numRepeats;

    private TweenEquation equation;

    public TweenAnimation(AnimationTarget target, double startValue, double targetValue, double duration, Class<? extends TweenEquation> equationClazz) {
        this.target = target;
        this.startValue = startValue;
        this.endValue = targetValue;
        this.duration = duration;

        try {
            this.equation = equationClazz.newInstance();
        } catch (Exception e) {
            this.equation = null;
        }
    }

    public AnimationTarget getTarget() {
        return target;
    }

    public double getDuration() {
        return duration;
    }

    public double getStartValue() {
        return startValue;
    }

    public double getEndValue() {
        return endValue;
    }

    private double readyTime;
    private double startTime;
    private double delayTime = 0.0;
    private double endTime;

    /**
     * Starts and Restart the interpolation. Using this method can lead to some
     * side-effects if you call it multiple times.
     */
    void start(long currentTime) {
        this.readyTime = currentTime;
        if(repeat)
            this.startTime = readyTime;
        else
            this.startTime = this.readyTime + this.delayTime;

        this.endTime = this.startTime + duration;

//        nextStartMillis = Math.max(endMillis, endMillis
//                + intervalMillis);

//        isReady = true;
//        isStarted = false;
//        isEnded = false;
//        isCompleted = false;

//        callReadyCallbacks();

        this.status = TweenAnimationStatus.STARTED;
    }

    /**
     * Updates the tween state. Using this method can be unsafe if tween pooling
     * was first enabled.
     */
    final void render(long currentTime) {
//        if (!isReady())
//            return;
//
//        if (checkRepetition(currentMillis)){
//            iteration += 1;
//            start(System.currentTimeMillis(), true);
//            return;
//        }
//
//        if (isEnded) {
//            target.end(g, tweenType);
//            return;
//        }

//        if (checkStart(currentMillis) && !isStarted) {
//
//
//            isStarted = true;
//            if (iteration > 0 && target != null) {
//                target.update(g, tweenType, startValues);
//            } else if (target != null) {
//                this.startValues = target.getTweenValues(tweenType);
//
//                int size = startValues.size();
//                targetMinusStartValues.clear();
//
//                for (int i = 0; i < size; i++) {
//
//                    if( isRelative ) {
//                        targetValues.set(i, startValues.get(i) + targetValues.get(i));
//                    }
//                    targetMinusStartValues.add(targetValues.get(i) - startValues.get(i));
//                }
//            }
//            callStartCallbacks();
//        }else if(!isStarted){
//            return;
//        }
//
//        if (checkEnd(currentMillis) && !isEnded) {
//            isEnded = true;
//
//            if (target != null) {
//
//                int size = startValues.size();
//
//                for (int i = 0; i < size; i++) {
//                    currentValues.set(i, isReversed
//                            ? targetValues.get(i) - targetMinusStartValues.get(i)
//                            : startValues.get(i) + targetMinusStartValues.get(i));
//                }
//                target.update(g, tweenType, currentValues);
//            }
//
//            if (isRepeat()) {
//                callIterationCompleteCallbacks();
//            } else {
//                isCompleted = true;
//                callIterationCompleteCallbacks();
//                callCompleteCallbacks();
//            }
//            return;
//        }

        value = equation.compute(currentTime - startTime, startValue, endValue - startValue, duration);
    }

    public void end() {

    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public long getNumRepeats() {
        return numRepeats;
    }

    public double getReadyTime() {
        return readyTime;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getDelayTime() {
        return delayTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public double getValue() {
        return value;
    }

    public TweenAnimationStatus getStatus() {
        return status;
    }
}
