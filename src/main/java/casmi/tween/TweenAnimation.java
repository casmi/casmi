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
        RUNNING,
    }

    private TweenAnimationStatus status = TweenAnimationStatus.WAIT;

    private AnimationTarget target;
    private double duration;

    private double startValue;
    private double endValue;
    private double value;

    private TweenEquation equation;

    public TweenAnimation(AnimationTarget target, double startValue, double targetValue, double duration, Class<? extends TweenEquation> equationClazz) {
        this.target = target;
        this.setStartValue(startValue);
        this.setEndValue(targetValue);
        this.setDuration(duration);

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

    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }

    public void setEndValue(double endValue) {
        this.endValue = endValue;
    }

    public void setDuration(double duration) {
        this.duration = duration;
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
        this.startTime = this.readyTime + this.delayTime;
        this.endTime = this.startTime + getDuration();
        this.status = TweenAnimationStatus.RUNNING;
    }

    /**
     * Updates the tween state. Using this method can be unsafe if tween pooling
     * was first enabled.
     */
    final void render(long currentTime) {
        if (this.getStatus() != TweenAnimationStatus.RUNNING) {
            return;
        }

        if (currentTime >= endTime && this.getStatus() != TweenAnimationStatus.WAIT) {
            this.status = TweenAnimationStatus.WAIT;
            return;
        }

        value = equation.compute(currentTime - startTime, getStartValue(), getEndValue() - getStartValue(), getDuration());
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
