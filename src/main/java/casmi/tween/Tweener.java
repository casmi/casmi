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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import casmi.graphics.element.Element;
import casmi.matrix.Vector2D;
import casmi.matrix.Vector3D;
import casmi.tween.TweenAnimation.TweenAnimationStatus;

/**
 * TweenElement class.
 *
 * @author Y. Ban
 * @author Takashi AOKI <federkasten@me.com>
 *
 * @see Tweenable
 */
public class Tweener {

    private double initialPositionX, initialPositionY, initialPositionZ;
    private double initialRotationX, initialRotationY, initialRotationZ;
    private double initialScaleX, initialScaleY, initialScaleZ;
    private double initialStrokeAlpha, initialFillAlpha;

    private Element element;

    private List<TweenAnimation> animations = new CopyOnWriteArrayList<TweenAnimation>();

    private boolean repeat = false;
    private long numRepeats = 0;

    enum TweenerStatus {
        WAIT,
        RUNNING
    }

    private TweenerStatus status = TweenerStatus.WAIT;

    public Tweener() {
    }

    public Tweener(Element e) {
        this.element = e;
        init();
    }

    public void setElement(Element e) {
        reset();
        this.element = e;
        init();
    }

    public Element getElement() {
        return element;
    }

    public void animatePosition(Vector2D v, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.POSITION_X, initialPositionX, v.getX(), duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.POSITION_Y, initialPositionY, v.getY(), duration, equationClazz));
    }

    public void animatePosition(Vector3D v, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.POSITION_X, initialPositionX, v.getX(), duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.POSITION_Y, initialPositionY, v.getY(), duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.POSITION_Z, initialPositionZ, v.getZ(), duration, equationClazz));
    }

    public void animateRotation(double x, double y, double z, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ROTATION_X, initialRotationX, x, duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.ROTATION_Y, initialRotationY, y, duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.ROTATION_Z, initialRotationZ, z, duration, equationClazz));
    }

    public void animateRotation(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ROTATION_Z, initialRotationZ, val, duration, equationClazz));
    }

    public void animateRotationX(double x, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ROTATION_X, initialRotationX, x, duration, equationClazz));
    }

    public void animateRotationY(double y, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ROTATION_Y, initialRotationY, y, duration, equationClazz));
    }

    public void animateRotationZ(double z, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ROTATION_Z, initialRotationZ, z, duration, equationClazz));
    }

    public void animateScale(double sx, double sy, double sz, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.SCALE_X, initialScaleX, sx, duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.SCALE_Y, initialScaleY, sy, duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.SCALE_Z, initialScaleZ, sz, duration, equationClazz));
    }

    public void animateScale(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.SCALE_X, initialScaleX, val, duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.SCALE_Y, initialScaleY, val, duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.SCALE_Z, initialScaleZ, val, duration, equationClazz));
    }

    public void animateScaleX(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.SCALE_X, initialScaleX, val, duration, equationClazz));
    }

    public void animateScaleY(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.SCALE_Y, initialScaleY, val, duration, equationClazz));
    }

    public void animateScaleZ(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.SCALE_Z, initialScaleZ, val, duration, equationClazz));
    }

    public void animateAlpha(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ALPHA_STROKE, initialStrokeAlpha, val, duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.ALPHA_FILL, initialFillAlpha, val, duration, equationClazz));
    }

    public void animateStrokeAlpha(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ALPHA_STROKE, initialStrokeAlpha, val, duration, equationClazz));
    }

    public void animateFillAlpha(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ALPHA_FILL, initialFillAlpha, val, duration, equationClazz));
    }

    public final void clear() {
        this.animations.clear();
    }

    public final void reset() {
        resetElement();
    }

    private final void resetElement() {
        if (element != null) {
            element.setPosition(initialPositionX, initialPositionY, initialPositionZ);
            element.setRotation(initialRotationX, initialRotationY, initialRotationZ);
            element.setScale(initialScaleX, initialScaleY, initialScaleZ);
            element.setStrokeColorAlpha(initialStrokeAlpha);
            element.setFillColorAlpha(initialFillAlpha);
        }
    }

    private void init() {
        if (element != null) {
            initialPositionX = element.getX();
            initialPositionY = element.getY();
            initialPositionZ = element.getZ();
            initialRotationX = element.getRotationX();
            initialRotationY = element.getRotationY();
            initialRotationZ = element.getRotationZ();
            initialScaleX = element.getScaleX();
            initialScaleY = element.getScaleY();
            initialScaleZ = element.getScaleZ();
            initialStrokeAlpha = element.getStrokeColor().getAlpha();
            initialFillAlpha = element.getFillColor().getAlpha();
        }
    }

    public final void start() {
        if (element == null) {
            return;  // do nothing
        }

        init();

        long currentTime = System.currentTimeMillis();

        for (TweenAnimation a : this.animations) {
            a.start(currentTime);

            switch (a.getTarget()) {
            case POSITION_X:
                a.setStartValue(initialPositionX);
                break;

            case POSITION_Y:
                a.setStartValue(initialPositionY);
                break;

            case POSITION_Z:
                a.setStartValue(initialPositionZ);
                break;

            case ROTATION_X:
                a.setStartValue(initialRotationX);
                break;

            case ROTATION_Y:
                a.setStartValue(initialRotationY);
                break;

            case ROTATION_Z:
                a.setStartValue(initialRotationZ);
                break;

            case SCALE_X:
                a.setStartValue(initialScaleX);
                break;

            case SCALE_Y:
                a.setStartValue(initialScaleY);
                break;

            case SCALE_Z:
                a.setStartValue(initialScaleZ);
                break;

            case ALPHA_FILL:
                a.setStartValue(initialFillAlpha);
                break;

            case ALPHA_STROKE:
                a.setStartValue(initialStrokeAlpha);
                break;

            default:
                break;
            }
        }

        status = TweenerStatus.RUNNING;
    }

    public final void render() {
        if (element == null) {
            return; // do nothing
        }

        if (status != TweenerStatus.RUNNING) {
            return; // do nothing
        }

        long currentTime = System.currentTimeMillis();

        for (TweenAnimation a : this.animations) {
            a.render(currentTime);

            switch (a.getTarget()) {
                case POSITION_X:
                    element.setX(a.getValue());
                    break;

                case POSITION_Y:
                    element.setY(a.getValue());
                    break;

                case POSITION_Z:
                    element.setZ(a.getValue());
                    break;

                case ROTATION_X:
                    element.setRotationX(a.getValue());
                    break;

                case ROTATION_Y:
                    element.setRotationY(a.getValue());
                    break;

                case ROTATION_Z:
                    element.setRotationZ(a.getValue());
                    break;

                case SCALE_X:
                    element.setScaleX(a.getValue());
                    break;

                case SCALE_Y:
                    element.setScaleY(a.getValue());
                    break;

                case SCALE_Z:
                    element.setScaleZ(a.getValue());
                    break;

                case ALPHA_FILL:
                    element.setFillColorAlpha(a.getValue());
                    break;

                case ALPHA_STROKE:
                    element.setStrokeColorAlpha(a.getValue());
                    break;

                default:
                    break;
            }
        }

        boolean finished = true;

        for (TweenAnimation a : animations) {
            if (a.getStatus() == TweenAnimationStatus.RUNNING) {
                finished = false;
                break;
            }
        }

        if (finished) {
            if (repeat) {
                numRepeats ++;
                resetElement();
                start();
            } else {
                this.status = TweenerStatus.WAIT;
            }
        }
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
}
