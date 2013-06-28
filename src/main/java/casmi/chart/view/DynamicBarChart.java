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

package casmi.chart.view;

import java.util.ArrayList;
import java.util.List;

import casmi.chart.data.MatrixData2D;
import casmi.chart.data.PairData;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Rect;
import casmi.tween.Tween;
import casmi.tween.Tweener;
import casmi.tween.TweenManager;
import casmi.tween.TweenParallelGroup;
import casmi.tween.TweenSerialGroup;
import casmi.tween.TweenType;
import casmi.tween.equations.Linear;

/**
 * DynamicBarChart Class.
 *
 * @author Y.Ban
 */
public class DynamicBarChart extends Chart {

    private List<Rect> rlist = new ArrayList<Rect>();
    private Color rectColor;
    private double barRatio = 0.8;
    private int delayMillSec = 300;
    private boolean animation = true;
    DynamicBarChartTweenType tweenType = DynamicBarChartTweenType.AT_ONCE;

    public DynamicBarChart(double width, double height, MatrixData2D m) {
        super(width, height, m);
        rectColor = new RGBColor(ColorSet.WHITE);
        setGraphBar();
    }

    public DynamicBarChart(double width, double height, MatrixData2D m, double maxY, double minY) {
        super(width, height, m, maxY, minY);
        rectColor = new RGBColor(ColorSet.WHITE);
        setGraphBar();
    }

    public DynamicBarChart(double width, double height, MatrixData2D m, boolean animation) {
        super(width, height, m);
        rectColor = new RGBColor(ColorSet.WHITE);
        this.animation = animation;
        setGraphBar();
    }

    public DynamicBarChart(double width, double height,
                           MatrixData2D m,
                           double maxY, double minY,
                           boolean animation) {
        super(width, height, m, maxY, minY);
        rectColor = new RGBColor(ColorSet.WHITE);
        this.animation = animation;
        setGraphBar();
    }

    public void setBarColor(Color color) {
        rectColor = color;
        for (Rect r : rlist)
            r.setFillColor(rectColor);
    }

    public void setBarColor(ColorSet colorset) {
        rectColor = new RGBColor(colorset);
        for (Rect r : rlist)
            r.setFillColor(rectColor);
    }

    private void setGraphBar() {
        int count = 0;
        for (Rect r : rlist)
            r.remove();
        rlist.clear();
        double barSize = width / m.getSize();
        this.axisHolizontal.setOffset(0.5 * barSize);
        for (PairData p : m.getData()) {
            // setting graph bars
            Rect r = new Rect(barSize * barRatio, 1);
            r.setFillColor(rectColor);
            r.setStroke(false);
            r.setPosition((count + 0.5) * barSize, 0);
            if (!animation) {
                r.setHeight(p.getY());
                r.setY(p.getY() / 2.0);
            }
            rlist.add(r);
            count++;
        }

        for (Rect r : rlist)
            add(r);
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
        setGraphBar();
    }

    public double getBarRatio() {
        return barRatio;
    }

    public void setBarRatio(double barRatio) {
        this.barRatio = barRatio;
    }

    public void setData(MatrixData2D m) {
        this.m = m;
        for (Rect r : rlist)
            r.remove();
        rlist.clear();
        setGraphBar();
        for (Rect r : rlist)
            add(r);
    }

    private void animationUpdate() {
        if (tweenreset) {
            tweenreset = false;
            for (Tweener t : twlist) {
                t.reset();
            }
        }

        if (tweenstart) {
            tweenstart = false;
            double barSize = width / m.getSize();
            tweenstart = false;
            manager = new TweenManager();
            addTweenManager(manager);

            tw = null;

            int count = 0;
            for (Rect r : rlist) {
                tw = new Tweener(r);
                twlist.add(tw);
                TweenSerialGroup tsg = null;
                if (tweenType == DynamicBarChartTweenType.AT_ONCE) {
                    tsg = TweenSerialGroup.create(
                        TweenParallelGroup.create(
                            Tween.to(tw, TweenType.SCALE_Y, tweenMillSec, Linear.INOUT).target((float)m.getDataY(count)),
                            Tween.to(tw, TweenType.POSITION_3D, tweenMillSec, Linear.INOUT).target((count + 0.5) * barSize, (float)(m.getDataY(count) / 2.0), 0)
                            )
                        );
                } else if (tweenType == DynamicBarChartTweenType.ORDER) {
                    tsg = TweenSerialGroup.create(
                        TweenParallelGroup.create(
                            Tween.to(tw, TweenType.SCALE_Y, tweenMillSec, Linear.INOUT).target((float)m.getDataY(count)).addDelay(delayMillSec * count),
                            Tween.to(tw, TweenType.POSITION_3D, tweenMillSec, Linear.INOUT).target((count + 0.5) * barSize, (float)(m.getDataY(count) / 2.0), 0).addDelay(delayMillSec * count)
                            )
                        );
                }
                manager.add(tsg);
                count++;
            }
        }
    }

    @Override
    public void update() {
        animationUpdate();
    }

    public void resetTween() {
        this.tweenstart = false;
        this.tweenreset = true;
    }

    public boolean isTweenstart() {
        return tweenstart;
    }

    public int getDelayMilliSec() {
        return delayMillSec;
    }

    public void setDelayMilliSec(int delayMilliSec) {
        this.delayMillSec = delayMilliSec;
    }

    public void setTweenType(DynamicBarChartTweenType tweentype) {
        this.tweenType = tweentype;
    }

    public DynamicBarChartTweenType getTweenType() {
        return this.tweenType;
    }
}
