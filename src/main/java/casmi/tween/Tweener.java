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

    public Tweener(Element e) {
        this.element = e;
        init();
    }

    public Element getElement() {
        return element;
    }

    public void animatePosition(Vector2D v, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.POSITION_X, element.getX(), v.getX(), duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.POSITION_Y, element.getY(), v.getY(), duration, equationClazz));
    }

    public void animatePosition(Vector3D v, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.POSITION_X, element.getX(), v.getX(), duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.POSITION_Y, element.getY(), v.getY(), duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.POSITION_Z, element.getZ(), v.getZ(), duration, equationClazz));
    }

    public void animateRotation(double x, double y, double z, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ROTATION_X, element.getRotationX(), x, duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.ROTATION_Y, element.getRotationY(), y, duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.ROTATION_Z, element.getRotationZ(), z, duration, equationClazz));
    }

    public void animateRotation(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ROTATION_Z, element.getRotationZ(), val, duration, equationClazz));
    }

    public void animateRotationX(double x, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ROTATION_X, element.getRotationX(), x, duration, equationClazz));
    }

    public void animateRotationY(double y, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ROTATION_Y, element.getRotationY(), y, duration, equationClazz));
    }

    public void animateRotationZ(double z, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ROTATION_Z, element.getRotationZ(), z, duration, equationClazz));
    }

    public void animateScale(double sx, double sy, double sz, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.SCALE_X, element.getScaleX(), sx, duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.SCALE_Y, element.getScaleY(), sy, duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.SCALE_Z, element.getScaleZ(), sz, duration, equationClazz));
    }

    public void animateScale(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.SCALE_X, element.getScaleX(), val, duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.SCALE_Y, element.getScaleY(), val, duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.SCALE_Z, element.getScaleZ(), val, duration, equationClazz));
    }

    public void animateScaleX(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.SCALE_X, element.getScaleX(), val, duration, equationClazz));
    }

    public void animateScaleY(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.SCALE_Y, element.getScaleY(), val, duration, equationClazz));
    }

    public void animateScaleZ(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.SCALE_Z, element.getScaleZ(), val, duration, equationClazz));
    }

    public void animateAlpha(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ALPHA_STROKE, element.getStrokeAlpha(), val, duration, equationClazz));
        animations.add(new TweenAnimation(AnimationTarget.ALPHA_FILL, element.getFillAlpha(), val, duration, equationClazz));
    }

    public void animateStrokeAlpha(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ALPHA_STROKE, element.getStrokeAlpha(), val, duration, equationClazz));
    }

    public void animateFillAlpha(double val, double duration, Class<? extends TweenEquation> equationClazz) {
        animations.add(new TweenAnimation(AnimationTarget.ALPHA_FILL, element.getFillAlpha(), val, duration, equationClazz));
    }

    private final void init() {
        initialPositionX = this.element.getX();
        initialPositionY = this.element.getY();
        initialPositionZ = this.element.getZ();
        initialRotationX = this.element.getRotationX();
        initialRotationY = this.element.getRotationY();
        initialRotationZ = this.element.getRotationZ();
        initialScaleX = this.element.getScaleX();
        initialScaleY = this.element.getScaleY();
        initialScaleZ = this.element.getScaleZ();
        initialStrokeAlpha = this.element.getStrokeColor().getAlpha();
        initialFillAlpha = this.element.getFillColor().getAlpha();

        this.element.setTween(true);
        this.animations.clear();
    }

    public final void reset() {
        element.setPosition(initialPositionX, initialPositionY, initialPositionZ);
        element.setRotation(initialRotationX, initialRotationY, initialRotationZ);
        element.setScale(initialScaleX, initialScaleY, initialScaleZ);
        element.setStrokeColorAlpha(initialStrokeAlpha);
        element.setFillColorAlpha(initialFillAlpha);

        this.animations.clear();
    }

    public final void start() {
        long currentTime = System.currentTimeMillis();

        for (TweenAnimation a : this.animations) {
            a.start(currentTime);
        }
    }

    public final void render() {
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
                    element.setFillAlpha(a.getValue());
                    break;

                case ALPHA_STROKE:
                    element.setStrokeAlpha(a.getValue());
                    break;

                default:
                    break;
            }
        }
    }

    public boolean isReady() {
        return this.animations.size() > 0;
    }
}
