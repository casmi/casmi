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

package casmi.chart.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * MatrixData2D Class.
 *
 * @author K. Nishimura
 */
public class MatrixData2D {

    private String firstAxis;
    private String secondAxis;
    private ArrayList<PairData> data;

    private double max;
    private double min;
    private double length;

    public MatrixData2D() {
        this.data = new ArrayList<PairData>();
    }

    public MatrixData2D(String firstAxis, String secondAxis) {
        this.max = 0;
        this.min = 0;
        this.length = 0;
        this.firstAxis = firstAxis;
        this.secondAxis = secondAxis;
        this.data = new ArrayList<PairData>();
    }

    public void setAxis(String firstAxis, String secondAxis) {
        this.firstAxis = firstAxis;
        this.secondAxis = secondAxis;
    }

    public void setFirstAxis(String firstAxis) {
        this.firstAxis = firstAxis;
    }

    public void setSecondAxis(String secondAxis) {
        this.secondAxis = secondAxis;
    }

    public void setData(String x, double y) {
        PairData p = new PairData(x, y);
        this.data.add(p);
    }

    public String getFirstAxis() {
        return this.firstAxis;
    }

    public String getSecondAxis() {
        return this.secondAxis;
    }

    public PairData getData(int i) {
        return this.data.get(i);
    }

    public String getDataX(int i) {
        return this.data.get(i).getX();
    }

    public double getDataY(int i) {
        return this.data.get(i).getY();
    }


    public ArrayList<PairData> getData() {
        return this.data;
    }

    public int getSize() {
        return this.data.size();
    }

    public void calculate() {
        ArrayList<Double> l = new ArrayList<Double>();
        Collection<Double> collection = l;
        for (PairData p : this.data) {
            l.add(p.getY());
        }
        this.max = Collections.max(collection);
        this.min = Collections.min(collection);
        this.length = Math.abs(this.max - this.min);
    }

    public double getMax() {
        return this.max;
    }


    public double getMin() {
        return this.min;
    }

    public double getLength() {
        return this.length;
    }
}
