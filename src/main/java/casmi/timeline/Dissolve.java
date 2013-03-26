/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2012, Xcoo, Inc.
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

package casmi.timeline;

import casmi.graphics.Graphics;
import casmi.graphics.element.Rect;
import casmi.tween.TweenEquation;
import casmi.tween.equations.Linear;

/**
 * Dissolve class.
 *
 * @author Y. Ban
 */
public class Dissolve {

    private double time;
    protected boolean nowDissolve = false;
    private Scene nowScene = null, nextScene = null;
    private Rect maskRect;

    private DissolveMode mode = DissolveMode.CROSS;

    private TweenEquation equation = Linear.INOUT;
    protected double width, height;

    public Dissolve(DissolveMode mode, double time) {
        this.setTime(time);
        this.setMode(mode);
    }

    public Dissolve(DissolveMode mode, double time, TweenEquation equation) {
        this.setTime(time);
        this.setMode(mode);
        this.setEquation(equation);
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public DissolveMode getMode() {
        return mode;
    }

    public void setMode(DissolveMode mode) {
        this.mode = mode;
    }

    public TweenEquation getEquation() {
        return equation;
    }

    public void setEquation(TweenEquation equation) {
        this.equation = equation;
    }

    protected void start(Scene nowScene, Scene nextScene) {
        this.nowScene = nowScene;
        this.nextScene = nextScene;
        switch (getMode()) {
        case CURTAIN_LEFT:
            startRectDissolve(0.1, height, 0, height / 2.0);
            break;
        case CURTAIN_RIGHT:
            startRectDissolve(0.1, height, width, height / 2.0);
            break;
        case CURTAIN_TOP:
            startRectDissolve(width, 0.1, width / 2.0, height);
            break;
        case CURTAIN_BOTTOM:
            startRectDissolve(width, 0.1, width / 2.0, 0);
            break;
        case ORIGINAL:
            startMyDissolve(nowScene, nextScene);
        }
    }

    protected void startMyDissolve(Scene nowScene, Scene nextScene) {

    }

    private void startRectDissolve(double width, double height, double x, double y) {
        maskRect = new Rect(width, height);
        maskRect.setStroke(false);
        maskRect.setPosition(x, y);
        this.nowScene.getMask().clear();
        this.nowScene.enableMask();
        this.nowScene.getMask().add(maskRect);
        this.nowScene.getMask().setInverseMask();
        this.nextScene.getMask().clear();
        this.nextScene.enableMask();
        this.nextScene.getMask().add(maskRect);
        this.nextScene.getMask().setNormalMask();
    }

    private void setRectDissolve(double width, double height, double x, double y, Graphics g) {
        maskRect.set(width, height);
        maskRect.setPosition(x, y);
        nowScene.setDepthTest(false);
        nowScene.setSceneA(1.0, g);
        if (nowDissolve)
            nowScene.drawscene(g);
        nextScene.setSceneA(1.0, g);
        nextScene.drawscene(g);
    }

    protected void end() {
        if (getMode() == DissolveMode.ORIGINAL)
            endMyDissolve(this.nowScene, this.nextScene);
        this.nowScene.disableMask();
        this.nextScene.disableMask();
    }

    protected void endMyDissolve(Scene nowScene, Scene nextScene) {

    }

    protected void render(Scene nowScene, Scene nextScene, double nowDissolveRate, Graphics g) {

        switch (getMode()) {
        default:
        case CROSS:
            nowScene.setDepthTest(false);
            nowScene.setSceneA((1.0 - nowDissolveRate), g);
            if (nowDissolve)
                nowScene.drawscene(g);
            nextScene.setSceneA(nowDissolveRate, g);
            nextScene.drawscene(g);
            break;
        case BLACK:
            if (nowDissolveRate <= 0.5) {
                nowScene.setSceneA((1.0 - nowDissolveRate * 2), g);
                nowScene.drawscene(g);
            }
            if (nowDissolveRate >= 0.5) {
                nextScene.setSceneA(((nowDissolveRate - 0.5) * 2), g);
                nextScene.drawscene(g);
            }
            break;
        case SLIDE_LEFT:
            setSlideDissolve(-width * nowDissolveRate, 0, width * (1 - nowDissolveRate), 0, g);
            break;
        case SLIDE_RIGHT:
            setSlideDissolve(width * nowDissolveRate, 0, width * (nowDissolveRate - 1), 0, g);
            break;
        case SLIDE_TOP:
            setSlideDissolve(0, height * nowDissolveRate, 0, height * (nowDissolveRate - 1), g);
            break;
        case SLIDE_BOTTOM:
            setSlideDissolve(0, -height * nowDissolveRate, 0, height * (1 - nowDissolveRate), g);
            break;
        case CURTAIN_LEFT:
            setRectDissolve(width * nowDissolveRate, height, width * nowDissolveRate / 2.0, height / 2.0, g);
            break;
        case CURTAIN_RIGHT:
            setRectDissolve(width * nowDissolveRate, height, width * (1 - nowDissolveRate / 2.0), height / 2.0, g);
            break;
        case CURTAIN_TOP:
            setRectDissolve(width, height * nowDissolveRate, width / 2.0, height * (1 - nowDissolveRate / 2.0), g);
            break;
        case CURTAIN_BOTTOM:
            setRectDissolve(width, height * nowDissolveRate, width / 2.0, height * nowDissolveRate / 2.0, g);
        case ORIGINAL:
            setMyDissolve(nowScene, nextScene, nowDissolveRate, g);
        }
    }

    protected void setMyDissolve(Scene nowScene, Scene nextScene, double nowDissolveRate, Graphics g) {

    }

    private void setSlideDissolve(double x1, double y1, double x2, double y2, Graphics g) {
        nowScene.setPosition(x1, y1);
        nowScene.setDepthTest(false);
        nowScene.setSceneA(1.0, g);
        if (nowDissolve)
            nowScene.drawscene(g);
        nextScene.setPosition(x2, y2);
        nextScene.setSceneA(1.0, g);
        nextScene.drawscene(g);
    }

    protected void setNowDissolve(boolean dissolve) {
        this.nowDissolve = dissolve;
    }

    protected void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }
}
