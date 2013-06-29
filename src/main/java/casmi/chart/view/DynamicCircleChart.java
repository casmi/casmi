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
import casmi.graphics.element.Arc;
import casmi.graphics.element.Text;
import casmi.graphics.element.TextAlign;
import casmi.graphics.font.Font;
import casmi.graphics.group.Group;
import casmi.tween.TweenCallback;
import casmi.tween.TweenCallbackTypes;
import casmi.tween.Tweener;
import casmi.tween.TweenerManager;
import casmi.util.Random;

/**
 * DynamicCircleChart Class.
 *
 * @author Y.Ban
 */
public class DynamicCircleChart extends Group {

//    private TweenDouble tw;  // TODO fix
    private List<Arc> arclist = new ArrayList<Arc>();
    private List<Color> colorList = new ArrayList<Color>();
    private List<Text> textList = new ArrayList<Text>();
    private Font indexNameFont;
    private boolean tweenstart = false;
    private TweenerManager manager;
    private MatrixData2D data;
    private double radius;
    private ChartTurnType turn = ChartTurnType.CLOCKWISE;
    private int tweenMilliSecond = 1500;
    TweenCallback tweencallback;
    private boolean animation = true;

    public DynamicCircleChart(MatrixData2D m, double radius) {
        init(m, radius, true);
    }

    public DynamicCircleChart(MatrixData2D m, double radius, ChartTurnType turn) {
        this.setTweenType(turn);
        init(m, radius, true);
    }

    public DynamicCircleChart(MatrixData2D m, double radius, boolean animation) {
        init(m, radius, animation);
    }

    public DynamicCircleChart(MatrixData2D m, double radius, ChartTurnType turn, boolean animation) {
        this.setTweenType(turn);
        init(m, radius, animation);
    }

    private void init(MatrixData2D m, double radius, boolean animation) {
        data = m;
        this.radius = radius;
// TODO fix
//        tw = new TweenDouble();
//        tw.setValue(0);
        indexNameFont = new Font();
        this.animation = animation;
        setGraphArc();

        tweencallback = new TweenCallback() {

            @Override
            public void run(TweenCallbackTypes eventType, Tweener tween) {
                arclist.get(arclist.size() - 1).setEnd(450.0);
            }
        };
    }

    public void animationUpdate() {
// TODO fix
//        if (tweenstart && tw.getValue() <= 360) {
//            setTweenstart(false);
//            manager = new TweenManager();
//            addTweenManager(manager);
//            TweenSerialGroup tsg = TweenSerialGroup.create(
//                Tween.to(tw, tweenMilliSecond, Linear.INOUT).target(360.01).addCompleteCallback(tweencallback)
//                );
//
//            manager.add(tsg);
//        }
//
//        if (tw.getValue() != 0 && tw.getValue() <= 360) {
//            for (int i = 0; i < arclist.size(); i++) {
//                if (arclist.get(i).getStart() - 90 < tw.getValue()) {
//                    arclist.get(i).setVisible(true);
//                    double maxRad = 0;
//                    if (i == arclist.size() - 1) maxRad = 360;
//                    else maxRad = arclist.get(i + 1).getStart() - 90;
//                    if (maxRad > tw.getValue()) {
//                        if (i > 0) {
//                            arclist.get(i - 1).setEnd(arclist.get(i).getStart());
//                        }
//                        arclist.get(i).setEnd(tw.getValue() + 90);
//                    }
//                }
//            }
//        }
    }

    @Override
    public void update() {
        if (animation)
            animationUpdate();

    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
        setGraphArc();
    }

    public void resetTween() {
        for (Arc arc : arclist) {
            arc.setEnd(arc.getStart() + 0.1);
            arc.setVisible(false);
        }
// TODO fix
//        tw.setValue(0);
    }

    private void setGraphArc() {
        textList.clear();
        colorList.clear();
        arclist.clear();
        int count = 0;
        double total = 0;
        for (PairData p : data.getData())
            total += p.getY();
        for (int j = 0; j < data.getData().size(); ++j) {
            double startRad = 0;
            for (int i = 0; i < count; i++)
                startRad += (data.getDataY(i) * 360.0 / total);

            Arc arc = new Arc(this.radius, startRad + 90, startRad + 90.1);
            colorList.add(new RGBColor(ColorSet.values()[Random.random(ColorSet.values().length)]));
            arc.setFillColor(colorList.get(count));
            arc.setStroke(false);
            arc.setDetail(180);
            if (turn == ChartTurnType.CLOCKWISE)
                arc.flip(0);
            if (animation)
                arc.setVisible(false);
            arclist.add(arc);
            add(arclist.get(count));
            count++;
        }
        setIndexName();

    }

    private void setIndexName() {
        int count = 0;
        for (PairData p : data.getData()) {
            Text indexName = new Text(p.getX(), indexNameFont);
            indexName.setAlign(TextAlign.CENTER);
            double textRadian;
            if (count < data.getSize() - 1) {
                if (!animation)
                    arclist.get(count).setEnd(arclist.get(count + 1).getStart());
                textRadian = ((arclist.get(count).getStart() + arclist.get(count + 1).getStart()) / 2.0 - 90) * Math.PI / 180.0;
            }
            else {
                if (!animation)
                    arclist.get(count).setEnd(450.1);
                textRadian = ((arclist.get(count).getStart() + 450.0) / 2.0 - 90) * Math.PI / 180.0;
            }
            indexName.setPosition(this.radius * 0.6 * Math.sin(textRadian), this.radius * 0.6 * Math.cos(textRadian));
            textList.add(indexName);
            add(textList.get(count));
            count++;
        }
    }

    public void setIndexNameFont(Font font) {
        this.indexNameFont = font;
    }

    public void setIndexNameTextColor(Color color) {
        for (Text t : textList)
            t.setStrokeColor(color);
    }

    public void setIndexNameTextColor(ColorSet colorSet) {
        for (Text t : textList)
            t.setStrokeColor(colorSet);
    }

    public Color getIndexNameTextColor() {
        return textList.get(0).getStrokeColor();
    }

    public void setIndexNameTextWidth(double width) {
        for (Text t : textList)
            t.setStrokeWidth(width);
    }

    public double getIndexNameTextWidth() {
        return textList.get(0).getStrokeWidth();
    }

    public List<Text> getIndexNameTextList() {
        return textList;
    }

    public Color getArcColor(int index) {
        if (index >= colorList.size())
            index = colorList.size() - 1;
        if (index < 0)
            index = 0;
        return colorList.get(index);
    }

    public void setArcColor(int index, Color color) {
        if (index >= colorList.size())
            index = colorList.size() - 1;
        if (index < 0)
            index = 0;
        colorList.set(index, color);
    }

    public MatrixData2D getData() {
        return data;
    }

    public void setData(MatrixData2D data) {
        this.data = data;
    }

    public boolean isTweenstart() {
        return tweenstart;
    }

    private void setTweenstart(boolean tweenstart) {
        this.tweenstart = tweenstart;
    }

    public void startTween() {
        this.setTweenstart(true);
    }

    public int getTweenMilliSecond() {
        return tweenMilliSecond;
    }

    public void setTweenMilliSecond(int tweenMilliSecond) {
        this.tweenMilliSecond = tweenMilliSecond;
    }

    public ChartTurnType getTweenType() {
        return turn;
    }

    public void setTweenType(ChartTurnType turn) {
        this.turn = turn;
    }
}
