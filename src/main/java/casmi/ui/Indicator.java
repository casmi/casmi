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

package casmi.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import casmi.graphics.color.GrayColor;
import casmi.graphics.element.Element;
import casmi.graphics.element.Rect;

/**
 * Indicator UI.
 *
 * @author T. Takeuchi
 */
public class Indicator extends Element {

    private List<Rect> rectList = new ArrayList<Rect>();

    private int[]   highlight = {-1, -1, -1};

    private boolean isAnimating = false;
    private boolean hideWhenStopped = false;

    private Timer timer;

    public Indicator() {
        super();
    }

    public void setup() {
        for (int i = 0; i < 12; ++i) {
            Rect el = new Rect(13, 4);
            el.setStrokeColor(new GrayColor(0.25));
            el.setStroke(false);
// TODO fix
//            add(el);
            rectList.add(el);

            el.setX(19.0 * Math.cos(Math.toRadians((90.0 - i * 30.0))));
            el.setY(19.0 * Math.sin(Math.toRadians((90.0 - i * 30.0))));
            el.setRotation(90.0 - i * 30.0);
        }

        highlight[0] =  0;
        highlight[1] = -1;
        highlight[2] = -1;

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (isAnimating) {
                    if (11 < highlight[0]++) highlight[0] = 0;
                    if (11 < highlight[1]++) highlight[1] = 0;
                    if (11 < highlight[2]++) highlight[2] = 0;
                }
            }
        }, 0, 90);
    }

    public void update() {
        if (!isAnimating) return;

        for (int i = 0; i < rectList.size(); ++i) {
            Rect el = rectList.get(i);

            if (i == highlight[0]) {
                el.setFillColor(new GrayColor(1.0));
            } else if (i == highlight[1]) {
                el.setFillColor(new GrayColor(0.8));
            } else if (i == highlight[2]) {
                el.setFillColor(new GrayColor(0.6));
            } else {
                el.setFillColor(new GrayColor(0.4));
            }
        }
    }

    public void start() {
        if (hideWhenStopped) {
            setVisible(true);
        }

        isAnimating = true;
    }

    public void stop() {
        isAnimating = false;

        if (hideWhenStopped) {
            setVisible(false);
        }
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public boolean isHideWhenStopped() {
        return hideWhenStopped;
    }

    public void setHideWhenStopped(boolean hideWhenStopped) {
        this.hideWhenStopped = hideWhenStopped;
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height, boolean selection) {
        // TODO fix
    }

    @Override
    public void reset(GL2 gl) {
        // TODO fix
    }
}
