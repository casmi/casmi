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

import java.util.List;

import casmi.chart.data.MatrixData2D;
import casmi.graphics.color.ColorSet;
import casmi.graphics.element.Line;
import casmi.graphics.element.Text;
import casmi.graphics.element.TextAlign;
import casmi.graphics.font.Font;
import casmi.graphics.group.Group;

/**
 * Axis Class for Chart.
 * 
 * @author Y.Ban
 */
public class Axis extends Group {

    private ChartAxis axis; // true:x false:y
    private double minValue, maxValue;
    private double length;
    private Line ax;
    private List<Line> div;
    private String axisName;
    private Text name;
    private Font nameFont;
    private Font divFont;
    private List<Text> division;
    private int divNum = 15;
    private double divisionDiff = 10.0;
    private MatrixData2D datas;
    private double offset = 0.0;

    public Axis(ChartAxis axis, double length, MatrixData2D datas) {
        this.axis = axis;
        this.length = length;
        nameFont = new Font("San-Serif");
        nameFont.setSize(15);
        divFont = new Font("San-Serif");
        divFont.setSize(10);
        if (this.axis == ChartAxis.HORIZONTAL) {
            ax = new Line(0, 0, this.length, 0);
            name = new Text(datas.getFirstAxis(), nameFont);
            this.divisionDiff = 1.0;
        } else {
            ax = new Line(0, 0, 0, this.length);
            this.minValue = datas.getMin();
            this.maxValue = datas.getMax();
            name = new Text(datas.getSecondAxis(), nameFont);
        }
        ax.setStrokeColor(ColorSet.WHITE);
        ax.setStrokeWidth(2);
        setData(datas);
    }

    public Axis(ChartAxis axis, double length, MatrixData2D datas, double max, double min) {
        this.axis = axis;
        this.length = length;
        nameFont = new Font("San-Serif");
        nameFont.setSize(15);
        divFont = new Font("San-Serif");
        divFont.setSize(10);
        if (this.axis == ChartAxis.HORIZONTAL) {
            ax = new Line(0, 0, this.length, 0);
            name = new Text(datas.getFirstAxis(), nameFont);
            this.divisionDiff = 1.0;
        } else {
            ax = new Line(0, 0, 0, this.length);
            this.minValue = min;
            this.maxValue = max;
            name = new Text(datas.getSecondAxis(), nameFont);
        }
        ax.setStrokeColor(ColorSet.WHITE);
        ax.setStrokeWidth(2);
        setData(datas);
    }

    public void setData(MatrixData2D ex) {
        this.datas = ex;
        this.clearAllObjects();
        this.draw();
    }

    public void setNameFont(Font font) {
        this.nameFont = font;
    }

    public void setDivisionNum(int num) {
        this.divNum = num;
    }

    public void setDivisionFont(Font font) {
        this.divFont = font;
    }

    public void setAxisName(String axisName) {
        this.axisName = axisName;
        setName(new Text(this.axisName, nameFont));
    }

    public List<Line> getDiv() {
        return div;
    }

    public void setDiv(List<Line> div) {
        this.div = div;
    }

    public void setDivisionNumber(int divNum) {
        this.divNum = divNum;
    }

    public int getDivisionNumber() {
        return this.divNum;
    }

    public Line getAx() {
        return ax;
    }

    public void setAx(Line ax) {
        this.ax = ax;
    }

    public Text getName() {
        return name;
    }

    public void setName(Text name) {
        this.name = name;
    }

    public List<Text> getDivision() {
        return division;
    }

    public void setDivision(List<Text> division) {
        this.division = division;
    }

    public void draw() {
        if (axis == ChartAxis.HORIZONTAL) {
            add(ax);
            name.setPosition(length / 2, -50);
            name.setAlign(TextAlign.CENTER);
            name.setStrokeColor(ColorSet.WHITE);
            add(name);
            double diff = length / (double)(datas.getSize() / divisionDiff);
            for (int i = 0; i * divisionDiff < datas.getSize(); i++) {
                Line l = new Line(i * diff + offset, 0, i * diff + offset, -10);
                l.setStrokeColor(ColorSet.WHITE);
                add(l);
                Text t;
                t = new Text(this.datas.getDataX((int)(i * divisionDiff)), divFont);
                t.setPosition(i * diff + offset, -20);
                t.setAlign(TextAlign.CENTER);
                t.setStrokeColor(ColorSet.WHITE);
                add(t);
            }
        } else {
            add(ax);
            name.setAlign(TextAlign.CENTER);
            name.setPosition(-80, length / 2);
            name.setRotation(90);
            name.setStrokeColor(ColorSet.WHITE);
            add(name);
            double diff = length / (double)((maxValue - minValue) / divisionDiff);
            for (int i = 0; this.minValue + i * divisionDiff <= this.maxValue; i++) {
                Line l = new Line(0, i * diff + offset, -10, i * diff + offset);
                l.setStrokeColor(ColorSet.WHITE);
                add(l);
                Text t;
                if (i != divNum) t = new Text((minValue + i * divisionDiff) + "", divFont);
                else t = new Text(maxValue + "", divFont);
                t.setPosition(-20, i * diff + offset);
                t.setAlign(TextAlign.RIGHT);
                t.setStrokeColor(ColorSet.WHITE);
                add(t);
            }
        }
    }

    @Override
    public void update() {}

    public double getDivisionDiff() {
        return divisionDiff;
    }

    public void setDivisionDiff(double divisionDiff) {
        this.divisionDiff = divisionDiff;
        this.clearAllObjects();
        this.draw();
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
        this.clearAllObjects();
        this.draw();
    }
}
