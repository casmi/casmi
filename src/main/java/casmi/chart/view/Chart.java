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
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.font.Font;
import casmi.graphics.group.Group;
import casmi.tween.TweenElement;
import casmi.tween.TweenManager;

/**
 * Chart Class.
 * 
 * @author Y.Ban
 */
public class Chart extends Group {

    protected MatrixData2D m;
    protected Axis axisHolizontal;
    protected Axis axisVertical;
    protected double width, height;
    protected double minY, maxY;
    protected boolean tweenstart = false;
    protected boolean tweenreset = false;
    protected TweenManager manager;
    protected int tweenMillSec = 1000;
    protected TweenElement tw;
    protected List<TweenElement> twlist = new ArrayList<TweenElement>();

    public Chart(double width, double height, MatrixData2D m) {
        setSize(width, height);
        setMatrixData2D(m);
        minY = m.getMin();
        maxY = m.getMax();
        axisHolizontal = new Axis(ChartAxis.HORIZONTAL, this.width, this.m);
        axisVertical = new Axis(ChartAxis.VERTICAL, this.height, this.m);
        add(axisHolizontal);
        add(axisVertical);
    }

    public Chart(double width, double height, MatrixData2D m, double max, double min) {
        setSize(width, height);
        setMatrixData2D(m);
        this.minY = min;
        this.maxY = max;
        axisHolizontal = new Axis(ChartAxis.HORIZONTAL, this.width, this.m);
        axisVertical = new Axis(ChartAxis.VERTICAL, this.height, this.m, max, min);
        add(axisHolizontal);
        add(axisVertical);
    }

    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setAxisNameFont(Font f) {
        this.axisHolizontal.setNameFont(f);
        this.axisVertical.setNameFont(f);
    }

    public void setAxisDivisionFont(Font f) {
        this.axisHolizontal.setDivisionFont(f);
        this.axisVertical.setDivisionFont(f);
    }

    public void setAxisColor(Color color) {
        axisHolizontal.setStrokeColor(color);
        axisVertical.setStrokeColor(color);
    }

    public void setAxisColor(ColorSet color) {
        axisHolizontal.setStrokeColor(color);
        axisVertical.setStrokeColor(color);
    }

    public void setAxisName(ChartAxis axis, String name) {
        switch (axis) {
        case HORIZONTAL:
            this.axisHolizontal.setAxisName(name);
            break;
        case VERTICAL:
            this.axisVertical.setAxisName(name);
            break;
        }
    }

    public Axis getAxisX() {
        return axisHolizontal;
    }

    public Axis getAxisY() {
        return axisVertical;
    }

    public void drawAxis() {
        this.axisHolizontal.setData(m);
        this.axisVertical.setData(m);
    }

    public void setMatrixData2D(MatrixData2D m) {
        this.m = m;
    }

    public MatrixData2D getMatrixData2D() {
        return m;
    }

    public void setDivisionSpace(ChartAxis axis, double space) {
        switch (axis) {
        case HORIZONTAL:
            this.axisHolizontal.setDivisionDiff(space);
            break;
        case VERTICAL:
            this.axisVertical.setDivisionDiff(space);
            break;
        }
    }

    public void startTween() {
        this.tweenstart = true;
    }

    public int getTweenMilliSec() {
        return tweenMillSec;
    }

    public void setTweenMilliSec(int tweenMilliSec) {
        this.tweenMillSec = tweenMilliSec;
    }

    @Override
    public void update() {}
}
