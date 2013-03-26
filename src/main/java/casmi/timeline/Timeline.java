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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.media.opengl.GL2;

import casmi.Applet;
import casmi.Keyboard;
import casmi.Mouse;
import casmi.PopupMenu;
import casmi.graphics.Graphics;
import casmi.graphics.element.Reset;
import casmi.parser.CSV;
import casmi.tween.Tween;
import casmi.tween.TweenEquation;
import casmi.tween.equations.Linear;
import casmi.tween.simpletweenables.TweenDouble;

import java.util.HashMap;
import java.util.Map;

/**
 * Timeline class.You can use time line with Scene class.
 * This class controls Scene and inserts disolve effects.
 *
 * @author Y. Ban
 *
 * @see Scene
 */
public class Timeline implements TimelineRender, Reset {

    private int nowSceneID = 0, nextSceneID = 1, nowId = 0;
    private double preDhalf = 0.0, nextDhalf = 0.0;
    private boolean endScene = false;
    private boolean dissolve = false, nowDissolve = false;
    private long dissolveStart, dissolveNow;
    private Applet baseApplet;
    private double nowDissolveTime;

    TweenDouble td = new TweenDouble(0.0f);
    Tween tween;

    private List<Scene> sceneList;
    private List<Scene> tmpSceneList;
    private Timer timer;
    private TimerTask task = new SceneTask();

    private Mouse mouse;
    private Keyboard keyboard;
    private PopupMenu popup;
    private boolean firstCallback = true;

    private Map<String, Integer> map = new HashMap<String, Integer>();

    class SceneTask extends TimerTask {

        @Override
        public void run() {
            if (!sceneList.get(nowSceneID).isHasDissolve()) {
                goNextSceneWithCallback();

            } else {
                if (!dissolve) {
                    goDissolve();
                } else {
                    sceneList.get(nowSceneID).ExitedSceneCallback();
                    endDisolve();
                }
            }
        }
    }

    public Timeline() {
        sceneList = new ArrayList<Scene>();
        tmpSceneList = new ArrayList<Scene>();
        td = new TweenDouble(0.0);
    }


    public final void readTimelineCSV(String csvfile) {
        CSV csv;
        String name;
        double time;
        try {
            csv = new CSV(csvfile);
            String[] test;
            while ((test = csv.readLine()) != null) {
                name = test[0];
                time = Double.valueOf(test[1]).doubleValue();
               {
                   for(Scene s : tmpSceneList){
                       if(s.getIdName() == name){
                           s.setTime(time);
                           this.appendScene(s);
                           break;
                       }
                   }
               }

            }
            csv.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Jump to the next Scene.
     */
    public void goNextScene() {
        firstCallback = true;
        nowSceneID = nextSceneID;
        nextSceneID++;
        try {
            sceneList.get(nextSceneID);
        } catch (java.lang.IndexOutOfBoundsException e) {
            nextSceneID = 0;
        }
        setEndScene(true);

        if(!sceneList.get(nowSceneID).isHasDissolve())
            nextDhalf = 0;
        else
            nextDhalf = sceneList.get(nowSceneID).getDissolve().getTime() / 2.0;

        task.cancel();
        task = null;
        task = new SceneTask();
        if(sceneList.get(nowSceneID).getTime()>0)
            timer.schedule(task, (long)(1000*((sceneList.get(nowSceneID).getTime() - preDhalf - nextDhalf))));
        preDhalf = 0;
        nextDhalf = 0;
    }

    private final void endDisolve() {
        sceneList.get(nowSceneID).getDissolve().end();
        if(!sceneList.get(nowSceneID).isHasDissolve())
            preDhalf = 0;
        else
            preDhalf = sceneList.get(nowSceneID).getDissolve().getTime() / 2.0;

        dissolve = false;

        sceneList.get(nowSceneID).setPosition(0, 0);
        goNextScene();

    }

    private final void goDissolve() {

        Dissolve diss = sceneList.get(nowSceneID).getDissolve();
        diss.setSize(getApplet().getWidth(), getApplet().getHeight());
        diss.start(sceneList.get(nowSceneID), sceneList.get(nextSceneID));
        dissolve = true;
        setEndScene(true);

        if(tween!=null)
            this.getApplet().removeTween(tween);
        tween = null;
        td.setValue(0);
        tween = Tween.to(td, (int)diss.getTime()*1000, diss.getEquation()).target(1.0);
        this.getApplet().addTween(tween);

        task.cancel();
        task = null;
        task = new SceneTask();
        timer.schedule(task, (long)(1000*(diss.getTime())));
        if(diss.getMode()==DissolveMode.CROSS)
            sceneList.get(nextSceneID).EnteredSceneCallback();
    }

    /**
     * Gets the Scene with name Id.
     * @param idName
     *                     The id Name of the Scene
     * @return
     *                     The Scene
     */
    public Scene getSceneWithId(String idName) {
        try {
            return sceneList.get(this.map.get(idName));
        } catch (java.lang.IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void goNextSceneWithCallback() {
        sceneList.get(nowSceneID).ExitedSceneCallback();
        sceneList.get(nextSceneID).EnteredSceneCallback();
        goNextScene();
    }

    /**
     * Jump to the next Scene with the name ID.
     * @param idName
     *                     The id Name of the Scene
     */
    public void goNextScene(String idName) {
        nextSceneID = this.map.get(idName);
        try {
            sceneList.get(nextSceneID);
        } catch (java.lang.IndexOutOfBoundsException e) {
            nextSceneID = nowSceneID;
        }
        if(!sceneList.get(nowSceneID).isHasDissolve())
            goNextSceneWithCallback();
        else
            goDissolve();
    }

    /**
     * Jump to the next Scene with name ID and set the dissolve with DissolveMode and time.
     * @param idName
     *                     The id Name of the Scene
     * @param mode
     *                     The dissolve mode
     * @param time
     *                     The dissolve time.
     */
    public void goNextScene(String idName, DissolveMode mode, double time) {
        goNextScene(idName, mode, time, Linear.INOUT);
    }

    /**
     * Jump to the next Scene with name ID and set the dissolve with DissolveMode, time and equation.
     * @param idName
     *                     The name of Scene
     * @param mode
     *                     The dissolve mode
     * @param time
     *                     The dissolve time
     * @param equation
     *                     The equation of the dissolve's tween
     */
    public void goNextScene(String idName, DissolveMode mode, double time, TweenEquation equation) {
        nextSceneID = this.map.get(idName);
        try {
            sceneList.get(nextSceneID);
        } catch (java.lang.IndexOutOfBoundsException e) {
            nextSceneID = nowSceneID;
        }
        sceneList.get(nowSceneID).setDissolve(new Dissolve(mode, time, equation));
        sceneList.get(nowSceneID).setHasDissolve(true);
        goDissolve();
    }

    /**
     * Jump to the next Scene with name ID and set the dissolve.
     * @param idName
     *                     The name of Scene
     * @param dissolve
     *                     The dissolve of Scene
     */
    public void goNextScene(String idName, Dissolve dissolve) {
        nextSceneID = this.map.get(idName);
        try {
            sceneList.get(nextSceneID);
        } catch (java.lang.IndexOutOfBoundsException e) {
            nextSceneID = nowSceneID;
        }
        sceneList.get(nowSceneID).setDissolve(dissolve);
        sceneList.get(nowSceneID).setHasDissolve(true);
        goDissolve();
    }

    /**
     * Appends Scene to Timeline.
     * @param scene
     */
    public void appendScene(Scene scene) {
        scene.setRootTimeline(this);
        this.sceneList.add(scene);
        this.map.put(scene.getIdName(), this.sceneList.size()-1);
    }

    /**
     * Appends Scene to Timeline with dissolve.
     * @param scene
     *
     * @param dissolve
     *
     */
    public void appendScene(Scene scene, Dissolve dissolve) {
        scene.setRootTimeline(this);
        this.sceneList.add(scene);
        this.map.put(scene.getIdName(), this.sceneList.size()-1);
        scene.setDissolve(dissolve);
        scene.setHasDissolve(true);
    }

    /**
     * Appends Scene to Timeline with dissolve.
     * @param scene
     *
     * @param mode
     *                     The DissolveMode
     * @param dissolveTime
     *                     The dissolve time
     *
     */
    public void appendScene(Scene scene, DissolveMode mode, double dissolveTime) {
        appendScene(scene, mode, dissolveTime, Linear.INOUT);
    }

    /**
     * Appends Scene to Timeline with dissolve.
     * @param scene
     *
     * @param mode
     *                     The DissolveMode
     * @param dissolveTime
     *                     The dissolve time
     * @param equation
     *                     The equation of the dissolve's tween
     *
     */
    public void appendScene(Scene scene, DissolveMode mode, double dissolveTime, TweenEquation equation) {
        scene.setRootTimeline(this);
        this.sceneList.add(scene);
        this.map.put(scene.getIdName(), this.sceneList.size()-1);
        scene.setDissolve(new Dissolve(mode, dissolveTime, equation));
        scene.setHasDissolve(true);
    }

    /**
     * Removes the Scene with ID from TimeLine
     * @param idName
     *                     The name of the scene
     */
    public void removeScene(String idName) {
        this.sceneList.remove(this.map.get(idName));
        this.map.remove(idName);
    }

    /**
     * Starts the timer of the Timeline.
     */
    public void startTimer() {
        timer = new Timer(true);
        double halfd = 0;
        try {
            if (sceneList.get(nowSceneID).isHasDissolve()) {
                halfd = (sceneList.get(nowSceneID).getDissolve().getTime() / 2.0);
               // sceneList.get(nowSceneID).getDissolve().start(nowScene, nextScene);
            }
            if(sceneList.get(nowSceneID).getTime()>0)
                timer.schedule(task, (long)(1000*(sceneList.get(nowSceneID).getTime() - halfd)));

        } catch (java.lang.IndexOutOfBoundsException e) {
            if(sceneList.get(nowSceneID).getTime()>0)
                timer.schedule(task, (long)(1000*(sceneList.get(nowSceneID).getTime())));
        }
    }

    public int render(Graphics g) {

        if (!dissolve) {
            sceneList.get(nowSceneID).drawscene(g);
            dissolveStart = System.currentTimeMillis();

        } else {
            nowDissolve = true;
            dissolveNow = System.currentTimeMillis();
            nowDissolveTime = 0;
            if(sceneList.get(nowSceneID).isHasDissolve())
                nowDissolveTime = (dissolveNow - dissolveStart) / ((sceneList.get(nowSceneID).getDissolve().getTime() * 1000));
            else
                return 0;
            if(nowDissolveTime>=0.5)
                nowId = nextSceneID;
            else
                nowId = nowSceneID;
            if(nowDissolveTime>1.0){
                nowDissolve = false;
                nowDissolveTime = 1.0;
            }
            if(nowId==nextSceneID && sceneList.get(nowSceneID).getDissolve().getMode()==DissolveMode.BLACK && firstCallback){
                sceneList.get(nextSceneID).EnteredSceneCallback();
                firstCallback = false;
            }

            Dissolve dissolve = sceneList.get(nowSceneID).getDissolve();
            dissolve.setNowDissolve(nowDissolve);
            dissolve.render(sceneList.get(nowSceneID),sceneList.get(nextSceneID),td.getValue(), g);

        }
        return 0;
    }


    public boolean isEndScene() {
        return endScene;
    }

    public Scene getScene() {
        return sceneList.get(nowId);
    }

    public Scene getScene(int index) {
        return sceneList.get(index);
    }

    public void setApplet(Applet a) {
        baseApplet = a;
    }

    public Applet getApplet() {
        return baseApplet;
    }

    public boolean setEndScene(boolean endScene) {
        this.endScene = endScene;
        return endScene;
    }

    public void setKeyboard(Keyboard keyboard){
        this.keyboard = keyboard;
    }

    public void setMouse(Mouse mouse){
        this.mouse = mouse;
    }

    public void setPopup(PopupMenu popup){
        this.popup = popup;
    }

    protected Keyboard getKeyboard(){
        return this.keyboard;
    }

    public Mouse getMouse(){
        return this.mouse;
    }

    public PopupMenu getPopup(){
        return this.popup;
    }

    public boolean isNowDissolve() {
        return nowDissolve;
    }


    @Override
    public void reset(GL2 gl) {
        this.getScene().reset(gl);

    }


    public void setSize(int width, int height) {
        this.getApplet().setSize(width, height);
    }


    public int getWidth() {
        return this.getApplet().getWidth();
    }

    public int getHeight() {
        return this.getApplet().getHeight();
    }




}
