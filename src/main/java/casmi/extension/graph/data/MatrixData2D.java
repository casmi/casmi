/*   casmi examples
 *   http://casmi.github.com/
 *   Copyright (C) 2011, Xcoo, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package casmi.extension.graph.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import casmi.extension.graph.data.PairData;

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
	
	public MatrixData2D(){
		this.data = new ArrayList<PairData>();
	}
	
	public MatrixData2D(String firstAxis, String secondAxis){
		this.max = 0;
		this.min = 0;
		this.length = 0;
		this.firstAxis = firstAxis;
		this.secondAxis = secondAxis;
		this.data = new ArrayList<PairData>();
	}
	
	public void setAxis(String firstAxis, String secondAxis){
		this.firstAxis = firstAxis;
		this.secondAxis = secondAxis;		
	}
	
	public void setFirstAxis(String firstAxis){
		this.firstAxis = firstAxis;
	}
	
	public void setSecondAxis(String secondAxis){
		this.secondAxis = secondAxis;
	}
	
	public void setData(String x, double y){
		PairData p = new PairData(x,y);
		this.data.add(p);
	}
	
	public String getFirstAxis(){
		return this.firstAxis;
	}
	
	public String getSecondAxis(){
		return this.secondAxis;
	}
	
	public PairData getData(int i){
		return this.data.get(i);
	}

	public String getDataX(int i){
		return this.data.get(i).getX();
	}

	public double getDataY(int i){
		return this.data.get(i).getY();
	}


	public ArrayList<PairData> getData(){
		return this.data;
	}
	
	public int getSize(){
		return this.data.size();
	}

	public void calculate(){
		ArrayList<Double> l = new ArrayList<Double>();
		Collection<Double> collection = l;
		for(PairData p: this.data){
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
