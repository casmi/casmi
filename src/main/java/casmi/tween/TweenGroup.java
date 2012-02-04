package casmi.tween;

import java.util.ArrayList;

public abstract class TweenGroup implements Groupable {

	private final ArrayList<Groupable> groupables = new ArrayList<Groupable>(0);
	private long duration = 0;
	private long delay = 0;
	private int repeatCnt = 0;
	private int intervalMillis = 0;
	private int iteration = 0;
	private boolean reputation;
	

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
		return this;
	}

	public int getRepeatCnt() {
		return repeatCnt;
	}

	public void setRepeatCnt(int repeatCnt) {
		this.repeatCnt = repeatCnt;
	}

	public int getIntervalMillis() {
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
