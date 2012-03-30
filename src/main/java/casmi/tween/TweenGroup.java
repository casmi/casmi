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

package casmi.tween;

import java.util.ArrayList;

/**
 * @author Y. Ban
 */
public abstract class TweenGroup implements Groupable {

	private final ArrayList<Groupable> groupables = new ArrayList<Groupable>(0);
	private long duration = 0;
	private long delay = 0;
	private int repeatCnt = 0;
	private long intervalMillis = 0;
	private int iteration = 0;
	private boolean reputation;
	private int baseGroupNum;

	void reset() {
		getGroupables().clear();
		setDuration(0);
		delay = 0;
	}

	/**
	 * Adds a list of Tweens and/or TweenGroups and make them run in parallel
	 * to the current ones.
	 * @param objs A list of objects made of Tweens and/or TweenGroups.
	 * @return The group, for instruction chaining.
	 */
	public abstract TweenGroup append(Groupable... objs);

	/**
	 * Gets the current duration of the group, in milliseconds.
	 */
	@Override
	public long getDuration() {
		return duration;
	}

	/**
	 * Gets the current delay of the group, in milliseconds.
	 */
	@Override
	public long getDelay() {
		return delay;
	}

	/**
	 * Adds a delay to the group.
	 */
	@Override
	public TweenGroup addDelay(long millis) {
		this.delay += millis;
		for(Groupable g: getGroupables()){
			g.addDelay(millis);
		}
		return this;
	}

	ArrayList<Groupable> getGroupables() {
		return groupables;
	}

	protected void setDuration(long duration) {
		this.duration = duration;
	}
	
	
	public TweenGroup repeat(int count, int delayMillis) {
		setRepeatCnt(count);
		setIntervalMillis(delayMillis);
			baseGroupNum = this.getGroupables().size();
			if(this instanceof TweenSerialGroup){
				while(this.getRepeatCnt()>this.getIteration()){
					for(int i = 0; i < baseGroupNum; i++){
						if(this.getGroupables().get(i) instanceof Tween){
							Tween t = ((Tween) this.getGroupables().get(i)).clone();
							if(i==0)
								t.addDelay(getIntervalMillis());
							this.append(t);
						}
						else{
						}
					}
					iteration++;
				}
			}
		
		
		return this;
	}

	public int getRepeatCnt() {
		return repeatCnt;
	}

	public void setRepeatCnt(int repeatCnt) {
		this.repeatCnt = repeatCnt;
	}

	public long getIntervalMillis() {
		return intervalMillis;
	}

	public void setIntervalMillis(int intervalMillis) {
		this.intervalMillis = intervalMillis;
	}

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	public boolean isReputation() {
		return reputation;
	}

	public void setReputation(boolean reputation) {
		this.reputation = reputation;
	}
		
}
