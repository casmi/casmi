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

package casmi.timeline;

import javax.media.opengl.GL2;

import casmi.Keyboard;
import casmi.Mouse;
import casmi.MouseButton;
import casmi.graphics.Graphics;
import casmi.graphics.element.Reset;
import casmi.graphics.object.Camera;
import casmi.graphics.object.Frustum;
import casmi.graphics.object.GraphicsObject;
import casmi.graphics.object.Mask;
import casmi.graphics.object.Ortho;
import casmi.graphics.object.Perspective;
import casmi.graphics.object.RootObject;
import casmi.tween.Tween;
import casmi.tween.TweenEquation;
import casmi.tween.TweenManager;
import casmi.tween.TweenParallelGroup;
import casmi.tween.TweenSerialGroup;
import casmi.ui.PopupMenu;

/**
 * Scene class for time line animation.
 *
 * @author Y. Ban
 */
abstract public class Scene extends RootObject {

    private String idName;
    private double time;
    private double sceneA = 0.0;
    private boolean hasDissolve = false;
    private Timeline rootTimeline;
    private Dissolve dissolve;
    private TweenManager tweenManager;

    public Scene(String id) {
        super();
        setIdName(id);
        setTime(0);
    }

    public Scene(String id, double time) {
        super();
        setIdName(id);
        setTime(time);
    }

    public Scene(String id, double time, DissolveMode mode, double dTime){
        super();
        setIdName(id);
        setTime(time);
        dissolve = new Dissolve(mode, dTime);
        hasDissolve = true;
    }

    public void setRootTimeline(Timeline timeline) {
        this.rootTimeline = timeline;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String id) {
        this.idName = id;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }


    public double getSceneA() {
        return sceneA;
    }

    public void setSceneA(double sceneA, Graphics g) {
        this.sceneA = sceneA;
        g.setSceneA(this.sceneA);
    }

    public void drawscene(Graphics g) {
        this.clearSelections();
        this.renderAll(g);
        if(!this.rootTimeline.isNowDissolve())
            this.renderSelectionAll(g, getMouseX(), getMouseY(), 0);
        update(g);
    }

    @Override
    public void update(Graphics g) {
        update();
    }

    @Override
    abstract public void update();

    public int addObject(Object r) {
        this.add(r);
        return 0;
    }

    public void removeObject(int index) {
        this.remove(index);
    }

    public int addObject(int index, Object r) {
        this.add(index, r);
        return 0;
    }

    public Object getObject(int index) {
        return this.get(index);
    }

    public void clearObject() {
        this.clear();
    }

    public void reset(GL2 gl) {
        for (Object obj : this.getObjectList()) {
            if (obj instanceof Reset) {
                Reset el = (Reset)obj;
                el.reset(gl);
            } else if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                go.resetObjects();
            }
        }
    }

    public int getMouseWheelRotation() {
        return rootTimeline.getMouse().getWheelRotation();
    }

    public Mouse getMouse() {
        return rootTimeline.getMouse();
    }

    public int getPrevMouseX() {
        return rootTimeline.getMouse().getPrevX();
    }

    public int getPrevMouseY() {
        return rootTimeline.getMouse().getPrevY();
    }

    @Deprecated
    public int getPreMouseX() {
        return rootTimeline.getMouse().getPrevX();
    }

    @Deprecated
    public int getPreMouseY() {
        return rootTimeline.getMouse().getPrevY();
    }

    public int getMouseX() {
        return rootTimeline.getMouse().getX();
    }

    public int getMouseY() {
        return rootTimeline.getMouse().getY();
    }

    public boolean isMousePressed() {
        return rootTimeline.getMouse().isPressed();
    }

    public boolean isMousePressed(MouseButton button) {
        return rootTimeline.getMouse().isButtonPressed(button);
    }

    public boolean isMouseClicked() {
        return rootTimeline.getMouse().isClicked();
    }

    public boolean isMouseEntered() {
        return rootTimeline.getMouse().isEntered();
    }

    public boolean isMouseExited() {
        return rootTimeline.getMouse().isExited();
    }

    public boolean isMouseReleased() {
        return rootTimeline.getMouse().isReleased();
    }

    public Keyboard getKeyboard() {
        return rootTimeline.getKeyboard();
    }

    public char getKey() {
        return rootTimeline.getKeyboard().getKey();
    }

    public int getKeyCode() {
        return rootTimeline.getKeyboard().getKeyCode();
    }

    public boolean isKeyPressed() {
        return rootTimeline.getKeyboard().isPressed();
    }

    public boolean isKeyReleased() {
        return rootTimeline.getKeyboard().isReleased();
    }

    public boolean isKeyTyped() {
        return rootTimeline.getKeyboard().isTyped();
    }

    public Scene getSceneWithId(String sceneIDName) {
        return this.rootTimeline.getSceneWithId(sceneIDName);
    }

    public void goNextScene() {
        this.rootTimeline.goNextSceneWithCallback();
    }

    public void goNextScene(String sceneIDName) {
        this.rootTimeline.goNextScene(sceneIDName);
    }

    public void goNextScene(String sceneIDName, Dissolve dissolve) {
        this.rootTimeline.goNextScene(sceneIDName, dissolve);
    }

    public void goNextScene(String sceneIDName, DissolveMode mode, double dissolveTime) {
        this.rootTimeline.goNextScene(sceneIDName, mode, dissolveTime);
    }

    public void goNextScene(String sceneIDName, DissolveMode mode, double dissolveTime, TweenEquation equation) {
        this.rootTimeline.goNextScene(sceneIDName, mode, dissolveTime, equation);
    }

    public PopupMenu getPopupMenu() {
        return rootTimeline.getPopup();
    }

    abstract public void keyEvent(casmi.KeyEvent e);

    abstract public void mouseEvent(casmi.MouseEvent e,  MouseButton b);

    public boolean isHasDissolve() {
        return hasDissolve;
    }

    public void setHasDissolve(boolean hasDissolve) {
        this.hasDissolve = hasDissolve;
    }

    public Dissolve getDissolve() {
        return dissolve;
    }

    public void setDissolve(Dissolve dissolve) {
        this.dissolve = dissolve;
    }

    private TweenManager getTweenManager() {
        if (tweenManager == null) {
            tweenManager = new TweenManager();
            this.addTweenManager(tweenManager);
        }

        return tweenManager;
    }

    public void addTween(Tween t) {
        getTweenManager().add(t);
    }

    public void addTween(TweenSerialGroup g) {
        getTweenManager().add(g);
    }

    public void addTween(TweenParallelGroup g) {
        getTweenManager().add(g);
    }

    public void clearTween(){
        tweenManager = null;
        this.clearTweenManager();
    }

    public void setPerspective() {
        this.addProjection(new Perspective());
    }

    public void setPerspective(double fov, double aspect, double zNear,    double zFar) {
        this.addProjection(new Perspective(fov, aspect, zNear, zFar));
    }

    public void setPerspective(Perspective perspective) {
        this.addProjection(perspective);
    }

    public void setOrtho() {
        this.addProjection(new Ortho());
    }

    public void setOrtho(double left, double right,
                         double bottom, double top,
                         double near, double far) {
        this.addProjection(new Ortho(left, right, bottom, top, near, far));
    }

    public void setOrtho(Ortho ortho) {
        this.addProjection(ortho);
    }

    public void setFrustum() {
        this.addProjection(new Frustum());
    }

    public void setFrustum(double left, double right, double bottom,
                           double top, double near, double far) {
        this.addProjection(new Frustum(left, right, bottom, top, near, far));
    }

    public void setFrustum(Frustum frustum) {
        this.addProjection(frustum);
    }

    public void setCamera() {
        this.addCamera(new Camera());
    }

    public void setCamera(double eyeX, double eyeY, double eyeZ,
                          double centerX, double centerY, double centerZ,
                          double upX, double upY, double upZ) {
        this.addCamera(
            new Camera(eyeX, eyeY, eyeZ,
                       centerX, centerY, centerZ,
                       upX, upY, upZ));
    }

    public void setCamera(Camera camera) {
        this.addCamera(camera);
    }

    @Override
    public Mask getMask() {
        if (mask == null) {
            mask = new Mask();
            this.setMask(mask);
        }
        return mask;
    }

    public void EnteredSceneCallback() {

    }

    public void ExitedSceneCallback() {

    }
}
