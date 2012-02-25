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

import casmi.Applet;
import casmi.graphics.Graphics;
import casmi.parser.CSV;

/**
 * Timeline class.You can use time line with Scene class.
 * This class controls Scene and inserts disolve effects.
 * 
 * @author Y. Ban
 * 
 */
public class Timeline implements TimelineRender {

    private int nowSceneID = 0, nextSceneID = 1;
    private int nowDissolveID = 0, nextDissolveID = 1;
    private double preDhalf = 0.0, nextDhalf = 0.0;
    private boolean endScene = false;
    private boolean dissolve = false, nextDissolve = false, preDissolve = false;
    private long dissolveStart, dissolveNow;
    private Applet baseApplet;

    private List<Scene> sl;
    private List<Dissolve> dv;
    private Timer timer;
    private TimerTask task = new SceneTask();

    class SceneTask extends TimerTask {

        @Override
        public void run() {
            if (!nextDissolve) {
                goNextScene();
                preDissolve = nextDissolve;
                try {
                    if (dv.get(nowDissolveID).now == nowSceneID)
                        nextDissolve = true;
                } catch (java.lang.IndexOutOfBoundsException e) {
                    nextDissolve = false;
                }
            } else {
                if (!dissolve) {
                    goDissolve();
                } else {
                    endDgoS();
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
        sl = new ArrayList<Scene>();
        dv = new ArrayList<Dissolve>();
    }

    public Timeline(String csvFile) {
        sl = new ArrayList<Scene>();
        readTimelineCSV(csvFile);
    }

    private final void readTimelineCSV(String csvfile) {
        CSV csv;
        String name;
//		double time;
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        try {
            csv = new CSV(csvfile);
            String[] test;
            while ((test = csv.readLine()) != null) {
                name = test[0];
//        		time = Double.valueOf(test[3]).doubleValue();
                Class<?> clazz;
                Object obj;
                try {
                    clazz = loader.loadClass(name);
                    obj = clazz.newInstance();
                    if (obj instanceof Scene) {

                    }

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                // if(!sl.contains()){
                // sl.add(clazz);
                // }
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
            sl.get(nextSceneID);
        } catch (java.lang.IndexOutOfBoundsException e) {
            nextSceneID = 0;
        }
        setEndScene(true);
    }

    // TODO: Rename! Can't understand!
    private final void endDgoS() {
        preDhalf = dv.get(nowDissolveID).getTime() / 2;
        nextDhalf = dv.get(nextDissolveID).getTime() / 2;
        nowDissolveID = nextDissolveID;
        nextDissolveID++;
        dissolve = false;

        try {
            dv.get(nextDissolveID);
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
            sl.get(nextSceneID);
        } catch (java.lang.IndexOutOfBoundsException e) {
            nextSceneID = nowSceneID;
        }
    }

    public void appendScene(Scene s) {
        this.sl.add(s);
    }

    public void removeScene(int i) {
        this.sl.remove(i);
    }

    public void appendDissolve(double time) {
        int nowID = this.sl.size() - 1;
        Dissolve dissolve = new Dissolve(nowID, nowID + 1, time);
        this.dv.add(dissolve);
    }

    public void appendDisolve(double time, DissolveMode mode) {
        int nowID = this.sl.size() - 1;
        Dissolve dissolve = new Dissolve(nowID, nowID + 1, time);
        dissolve.mode = mode;
        this.dv.add(dissolve);
    }

    public void setDisolve(int now, int next, double time) {
        Dissolve dissolve;
        dissolve = new Dissolve(now, next, time);
        this.dv.add(dissolve);
    }

    public void startTimer() {
        timer = new Timer(true);
        long halfd;
        try {
            if (dv.get(nowSceneID).now == nowSceneID) {
                halfd = (long)dv.get(nowSceneID).getTime() / 2;
                timer.schedule(task, TimeUnit.SECONDS.toMillis((long)sl.get(nowSceneID).getTime() - halfd));
                nextDissolve = true;
            } else {
                timer.schedule(task, TimeUnit.SECONDS.toMillis((long)sl.get(nowSceneID).getTime()));
            }
        } catch (java.lang.IndexOutOfBoundsException e) {
            timer.schedule(task, TimeUnit.SECONDS.toMillis((long)sl.get(nowSceneID).getTime()));
        }

        if (dv.size() == 1) {
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
                timer.schedule(task, TimeUnit.SECONDS.toMillis((long)sl.get(nowSceneID).getTime()));
            } else {
                if (!dissolve) {
                    timer.schedule(task, TimeUnit.SECONDS.toMillis((long)dv.get(nowDissolveID).getTime()));
                    dissolveStart = System.currentTimeMillis();
                } else {
                    try {
                        if (dv.get(nowDissolveID).now == nowSceneID) {
                            nextDissolve = true;
                        } else {
                            nextDissolve = false;
                        }
                    } catch (java.lang.IndexOutOfBoundsException e) {
                        nextDissolve = false;
                    }

                    if (!nextDissolve) {
                        timer.schedule(task, TimeUnit.SECONDS.toMillis((long)(sl.get(nowSceneID).getTime() - preDhalf)));
                    } else if (!preDissolve) {
                        timer.schedule(task, TimeUnit.SECONDS.toMillis((long)(sl.get(nowSceneID).getTime() - nextDhalf)));
                    } else {
                        timer.schedule(task, TimeUnit.SECONDS.toMillis((long)(sl.get(nowSceneID).getTime() - preDhalf - nextDhalf)));
                    }
                }
            }
        }

        if (!dissolve) {
            sl.get(nowSceneID).drawscene(g);
        } else {
            dissolveNow = System.currentTimeMillis();
            double tmp = (dissolveNow - dissolveStart) / (dv.get(nowDissolveID).getTime() * 1000);

            switch (dv.get(nowDissolveID).mode) {
            default:
            case CROSS:
                sl.get(nowSceneID).setSceneA((1.0 - tmp), g);
                sl.get(nowSceneID).drawscene(g);
                sl.get(nextSceneID).setSceneA(tmp, g);
                sl.get(nextSceneID).drawscene(g);
                break;
            case NORMAL:
                if (tmp <= 0.5) {
                    sl.get(nowSceneID).setSceneA((1.0 - tmp * 2), g);
                    sl.get(nowSceneID).drawscene(g);
                }
                if (tmp >= 0.5) {
                    sl.get(nextSceneID).setSceneA(((tmp - 0.5) * 2), g);
                    sl.get(nextSceneID).drawscene(g);
                }
                break;
            }
        }
    }

    public boolean isEndScene() {
        return endScene;
    }

    public Scene getScene() {
        return sl.get(nowSceneID);
    }

    public Scene getScene(int index) {
        return sl.get(index);
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
}
