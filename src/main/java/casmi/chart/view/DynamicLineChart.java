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
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Line;
import casmi.matrix.Vector3D;

/**
 * DynamicLineChart Class.
 *
 * @author Y.Ban
 */
public class DynamicLineChart extends Chart {

    private List<Line> lines;
    private List<Vector3D> vertexs;
    private RGBColor topColor;
    private RGBColor bottomColor;
    private boolean gradation;
    private boolean useColor;
    private double xDivision;
    private double yDivision;
    private boolean animation = true;
//    private TweenDouble td; // TODO fix

    public DynamicLineChart(double width, double height, MatrixData2D m, boolean animation) {
        super(width, height, m);
        setupGraph(animation);
    }

    public DynamicLineChart(double width, double height, MatrixData2D m, double maxY, double minY, boolean animation) {
        super(width, height, m, maxY, minY);
        setupGraph(animation);
    }

    public DynamicLineChart(double width, double height, MatrixData2D m) {
        super(width, height, m);
        setupGraph(true);
    }

    public DynamicLineChart(double width, double height, MatrixData2D m, double maxY, double minY) {
        super(width, height, m, maxY, minY);
        setupGraph(true);
    }

    private void setupGraph(boolean anime) {
        this.bottomColor = new RGBColor(ColorSet.DARK_BLUE);
        this.topColor = new RGBColor(ColorSet.DARK_ORANGE);
        useColor = true;
        gradation = true;
        lines = new ArrayList<Line>();
        vertexs = new ArrayList<Vector3D>();
        tweenMillSec = 3000;
// TODO fix
//        td = new TweenDouble();
//        td.setValue(0);
        setGraphVertex();
        animation = anime;
        if (animation) setGraphLine4Tween();
        else setGraphLine();
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
        if (animation) setGraphLine4Tween();
        else setGraphLine();
    }

    private void setGraphVertex() {
        xDivision = width / m.getSize();
        yDivision = height / (maxY - minY);
        for (int i = 0; i < this.m.getSize(); i++) {
            Vector3D v = new Vector3D(xDivision * i, yDivision * (this.m.getDataY(i) - minY));
            vertexs.add(v);
        }
    }

    private void setGraphLine4Tween() {
        xDivision = width / m.getSize();
        yDivision = height / (maxY - minY);
        lines.clear();
        int index = 0;
        for (int i = 0; i < this.m.getSize() - 1; i++) {
            Line l = new Line(vertexs.get(i), vertexs.get(i));
            l.setCornerColor(0, RGBColor.lerpColor(this.bottomColor, this.topColor, vertexs.get(i).getY() / height));
            l.setCornerColor(1, RGBColor.lerpColor(this.bottomColor, this.topColor, vertexs.get(i).getY() / height));
            lines.add(l);
            add(lines.get(index));
            index++;
        }
    }

    private void setGraphLine() {
        xDivision = width / m.getSize();
        yDivision = height / (maxY - minY);
        lines.clear();
        int index = 0;
        for (int i = 0; i < this.m.getSize() - 1; i++) {
            Line l = new Line(vertexs.get(i), vertexs.get(i + 1));
            l.setCornerColor(0, RGBColor.lerpColor(this.bottomColor, this.topColor, vertexs.get(i).getY() / height));
            l.setCornerColor(1, RGBColor.lerpColor(this.bottomColor, this.topColor, vertexs.get(i + 1).getY() / height));
            lines.add(l);
            add(lines.get(index));
            index++;
        }
    }

    public void resetGraph() {
        this.clearAllObjects();
        this.drawAxis();
        this.add(axisHolizontal);
        this.add(axisVertical);
        setGraphLine();
    }

    public void setColor(RGBColor topColor, RGBColor bottomColor) {
        this.topColor = topColor;
        this.bottomColor = bottomColor;
    }

    @Override
    public boolean isGradation() {
        return gradation;
    }

    @Override
    public void setGradation(boolean gradation) {
        this.gradation = gradation;
    }

    public boolean isUseColor() {
        return useColor;
    }

    public void setUseColor(boolean useColor) {
        this.useColor = useColor;
    }

    public void draw() {

    }

    public List<Line> getLines() {
        return lines;
    }

    @Override
    public void update() {
// TODO fix
//        if (animation) {
//            if (tweenstart && td.getValue() <= vertexs.get(vertexs.size() - 1).getX()) {
//                tweenstart = false;
//                manager = new TweenManager();
//                td.setValue(vertexs.get(0).getX());
//                addTweenManager(manager);
//                TweenSerialGroup tsg = TweenSerialGroup.create(
//                    Tween.to(td, tweenMillSec, Linear.INOUT).target(vertexs.get(vertexs.size() - 1).getX() + 0.1)
//                    );
//
//                manager.add(tsg);
//            }
//
//            if (td.getValue() != 0 && td.getValue() <= vertexs.get(vertexs.size() - 1).getX()) {
//                for (int i = 0; i < vertexs.size() - 1; i++) {
//                    if (vertexs.get(i).getX() <= td.getValue() && vertexs.get(i + 1).getX() > td.getValue()) {
//                        if (i > 0) {
//                            lines.get(i - 1).set(vertexs.get(i - 1), vertexs.get(i));
//                            lines.get(i).setCornerColor(1, RGBColor.lerpColor(this.bottomColor, this.topColor, vertexs.get(i).getY() / height));
//                        }
//                        double nowY = (vertexs.get(i + 1).getY() - vertexs.get(i).getY()) * (td.getValue() - vertexs.get(i).getX()) / xDivision + vertexs.get(i).getY();
//                        lines.get(i).set(vertexs.get(i).getX(), vertexs.get(i).getY(), td.getValue(), nowY);
//                        lines.get(i).setCornerColor(1, RGBColor.lerpColor(this.bottomColor, this.topColor, nowY / height));
//                    }
//                }
//
//            }
//        }
    }

    public void resetTween() {
        int i = 0;
        for (Line line : lines) {
            line.set(vertexs.get(i), vertexs.get(i));
            line.setCornerColor(0, RGBColor.lerpColor(this.bottomColor, this.topColor, vertexs.get(i).getY() / height));
            line.setCornerColor(1, RGBColor.lerpColor(this.bottomColor, this.topColor, vertexs.get(i).getY() / height));
            i++;
        }
//        td.setValue(vertexs.get(0).getX()); // TODO fix
    }

    public double getxDivision() {
        return xDivision;
    }

    public double getyDivision() {
        return yDivision;
    }
}
