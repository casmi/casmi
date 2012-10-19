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
import java.util.concurrent.TimeUnit;

import javax.media.opengl.GL2;

import casmi.Applet;
import casmi.Keyboard;
import casmi.Mouse;
import casmi.PopupMenu;
import casmi.graphics.Graphics;
import casmi.graphics.element.Reset;
import casmi.parser.CSV;

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
    private int nowDissolveID = 0, nextDissolveID = 1;
    private double preDhalf = 0.0, nextDhalf = 0.0;
    private boolean endScene = false;
    private boolean dissolve = false, nextDissolve = false, preDissolve = false, nowDissolve = false;
    private long dissolveStart, dissolveNow;
    private Applet baseApplet;

    private List<Scene> sceneList;
    private List<Scene> tmpSceneList;
    private List<Dissolve> disolveList;
    private Timer timer;
    private TimerTask task = new SceneTask();
    
    private Mouse mouse;
    private Keyboard keyboard;
    private PopupMenu popup;

    class SceneTask extends TimerTask {

        @Override
        public void run() {
            if (!nextDissolve) {
                goNextScene();
                preDissolve = nextDissolve;
                try {
                    if (disolveList.get(nowDissolveID).now == nowSceneID)
                        nextDissolve = true;
                } catch (java.lang.IndexOutOfBoundsException e) {
                    nextDissolve = false;
                }
            } else {
                if (!dissolve) {
                    goDissolve();
                } else {
                    endDisolve();
                    preDissolve = nextDissolve;
                }
            }
        }
    }

    class Dissolve {

        private int now, next;
        private double time;

        private DissolveMode mode = DissolveMode.CROSS;

        public Dissolve(int now, int next, double time) {
            this.now = now;
            this.next = next;
            this.setTime(time);
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }

        int getNow() {
            return now;
        }

        int getNext() {
            return next;
        }
    }

    public Timeline() {
        sceneList = new ArrayList<Scene>();
        tmpSceneList = new ArrayList<Scene>();
        disolveList = new ArrayList<Dissolve>();
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
               if(name == "blackDissolve")
            	   appendDisolve(time, DissolveMode.BLACK);
               else if(name == "crossDissolve")
            	   appendDisolve(time, DissolveMode.CROSS);
               else{
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

    public void goNextScene() {
        nowSceneID = nextSceneID;
        nextSceneID++;
        try {
            sceneList.get(nextSceneID);
        } catch (java.lang.IndexOutOfBoundsException e) {
            nextSceneID = 0;
        }
        setEndScene(true);
    }

    private final void endDisolve() {
        preDhalf = disolveList.get(nowDissolveID).getTime() / 2;
        nextDhalf = disolveList.get(nextDissolveID).getTime() / 2;
        nowDissolveID = nextDissolveID;
        nextDissolveID++;
        dissolve = false;

        try {
            disolveList.get(nextDissolveID);
        } catch (java.lang.IndexOutOfBoundsException e) {
            nextDissolveID = 0;
        }

        goNextScene();

    }

    private final void goDissolve() {
        dissolve = true;
        setEndScene(true);
    }

    public void setNextScene(int i) {
        nextSceneID = i;
        try {
            sceneList.get(nextSceneID);
        } catch (java.lang.IndexOutOfBoundsException e) {
            nextSceneID = nowSceneID;
        }
    }

    public void appendScene(Scene s) {
    	s.setRootTimeline(this);
        this.sceneList.add(s);
    }

    public void removeScene(int i) {
        this.sceneList.remove(i);
    }

    public void appendDissolve(double time) {
        int nowID = this.sceneList.size() - 1;
        Dissolve dissolve = new Dissolve(nowID, nowID + 1, time);
        this.disolveList.add(dissolve);
    }

    public void appendDisolve(double time, DissolveMode mode) {
        int nowID = this.sceneList.size() - 1;
        Dissolve dissolve = new Dissolve(nowID, nowID + 1, time);
        dissolve.mode = mode;
        this.disolveList.add(dissolve);
    }

    public void setDisolve(int now, int next, double time) {
        Dissolve dissolve;
        dissolve = new Dissolve(now, next, time);
        this.disolveList.add(dissolve);
    }

    public void startTimer() {
        timer = new Timer(true);
        long halfd;
        try {
            if (disolveList.get(nowSceneID).now == nowSceneID) {
                halfd = (long)disolveList.get(nowSceneID).getTime() / 2;
                timer.schedule(task, TimeUnit.SECONDS.toMillis((long)sceneList.get(nowSceneID).getTime() - halfd));
                nextDissolve = true;
            } else {
                timer.schedule(task, TimeUnit.SECONDS.toMillis((long)sceneList.get(nowSceneID).getTime()));
            }
        } catch (java.lang.IndexOutOfBoundsException e) {
            timer.schedule(task, TimeUnit.SECONDS.toMillis((long)sceneList.get(nowSceneID).getTime()));
        }

        if (disolveList.size() == 1) {
            nextDissolveID = nowDissolveID;
        }
    }

    public void render(Graphics g) {

        if (endScene) {
            endScene = false;
            task.cancel();
            task = null;

            if (task == null)
                task = new SceneTask();

            if (!nextDissolve) {
                timer.schedule(task, TimeUnit.SECONDS.toMillis((long)sceneList.get(nowSceneID).getTime()));
            } else {
                if (!dissolve) {
                    timer.schedule(task, TimeUnit.SECONDS.toMillis((long)disolveList.get(nowDissolveID).getTime()));
                } else {
                    try {
                        if (disolveList.get(nowDissolveID).now == nowSceneID) {
                            nextDissolve = true;
                        } else {
                            nextDissolve = false;
                        }
                    } catch (java.lang.IndexOutOfBoundsException e) {
                        nextDissolve = false;
                    }

                    if (!nextDissolve) {
                        timer.schedule(task, TimeUnit.SECONDS.toMillis((long)(sceneList.get(nowSceneID).getTime() - preDhalf)));
                    } else if (!preDissolve) {
                        timer.schedule(task, TimeUnit.SECONDS.toMillis((long)(sceneList.get(nowSceneID).getTime() - nextDhalf)));
                    } else {
                        timer.schedule(task, TimeUnit.SECONDS.toMillis((long)(sceneList.get(nowSceneID).getTime() - preDhalf - nextDhalf)));
                    }
                }
            }
        }

        if (!dissolve) {
            sceneList.get(nowSceneID).drawscene(g);
            dissolveStart = System.currentTimeMillis();
            
        } else {
        	nowDissolve = true;
            dissolveNow = System.currentTimeMillis();
            double tmp = (dissolveNow - dissolveStart) / (disolveList.get(nowDissolveID).getTime() * 1000);
            if(tmp>=0.5)
            	nowId = nextSceneID;
            else 
            	nowId = nowSceneID;
            if(tmp>1.0){
            	nowDissolve = false;
            	tmp = 1.0;
            }
            switch (disolveList.get(nowDissolveID).mode) {
            default:
            case CROSS:
            	sceneList.get(nowSceneID).setDepthTest(false);
                sceneList.get(nowSceneID).setSceneA((1.0 - tmp), g);
                if(nowDissolve)
                	sceneList.get(nowSceneID).drawscene(g);
                sceneList.get(nextSceneID).setSceneA(tmp, g);
                sceneList.get(nextSceneID).drawscene(g);
                break;
            case BLACK:
                if (tmp <= 0.5) {
                    sceneList.get(nowSceneID).setSceneA((1.0 - tmp * 2), g);
                    sceneList.get(nowSceneID).drawscene(g);
                }
                if (tmp >= 0.5) {
                    sceneList.get(nextSceneID).setSceneA(((tmp - 0.5) * 2), g);
                    sceneList.get(nextSceneID).drawscene(g);
                }
                break;
            }
        }
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
}
