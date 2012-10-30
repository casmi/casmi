/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2011-2012, Xcoo, Inc.
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

import casmi.graphics.Graphics;

/**
 * A TweenManager let you pack many tweens together, and update them at once.
 * 
 * @author Y. Ban
 * 
 * @see Tween
 * @see TweenSerialGroup
 */
public class TweenManager {

	private final ArrayList<Tween> tweens;

	/**
	 * Instantiates a new manager.
	 */
	public TweenManager() {
		this.tweens = new ArrayList<Tween>();
	}
	
	/**
	 * Adds a new tween to the manager and starts it.
	 * 
	 * @param green
	 *            A tween. Does nothing if it is already present.
	 * @return The manager, for instruction chaining.
	 */
	public final TweenManager add(Tween tween) {
		tweens.add(tween);
		tween.start(System.currentTimeMillis(), false);
		return this;
	}

	/**
	 * Adds every tween from a tween group to the manager, and starts them. Note
	 * that the group will be cleared, as says its specification. Therefore,
	 * only call this method as the last one!
	 * 
	 * @param group
	 *            A tween group.
	 * @return The manager, for instruction chaining.
	 */
	public final TweenManager add(TweenGroup group) {
		long currentMillis = System.currentTimeMillis();

		for (Groupable g : group.getGroupables()) {
			if (g instanceof Tween) {
				Tween tween = (Tween) g;
				tweens.add(tween);
				tween.start(currentMillis, false);
			} else if (g instanceof TweenSerialGroup) {
				this.add((TweenSerialGroup) g);
			} else if (g instanceof TweenParallelGroup) {
				this.add((TweenParallelGroup) g);
			}
		}

		group.reset();

		return this;
	}

//	/**
//	 * Adds every tween from a tween group to the manager, and starts them. Note
//	 * that the group will be cleared, as says its specification. Therefore,
//	 * only call this method as the last one!
//	 * 
//	 * @param group
//	 *            A tween group.
//	 * @return The manager, for instruction chaining.
//	 */
//	public final TweenManager add(TweenSerialGroup group) {
//		long currentMillis = System.currentTimeMillis();
//
//		for (Groupable g : group.getGroupables()) {
//			if (g instanceof Tween) {
//				Tween tween = (Tween) g;
//				tweens.add(tween);
//				tween.start(currentMillis, false);
//			} else if (g instanceof TweenSerialGroup) {
//				this.add((TweenSerialGroup) g);
//			} else if (g instanceof TweenParallelGroup) {
//				this.add((TweenParallelGroup) g);
//			}
//		}
//
//		group.reset();
//
//		return this;
//	}

	/**
	 * Clears the manager from every tween.
	 */
	public void clear() {
		tweens.clear();
	}

	/**
	 * Returns true if the manager contains any valid tween associated to the
	 * given target.
	 */
	public final boolean contains(Tweenable target) {
		for (Tween t : tweens) {
			if (t.getTarget() == target &&
					!t.isFinished())
				return true;
		}
		return false;
	}

	/**
	 * Returns true if the manager contains any valid tween associated to the
	 * given target and tween type.
	 */
	public final boolean contains(Tweenable target, TweenType tweenType) {
		for (Tween t : tweens) {
			if (t.getTarget() == target &&
					t.getTweenType() == tweenType &&
					!t.isFinished())
				return true;
		}
		return false;
	}

	/**
	 * Kills every valid tween associated to the given target.
	 */
	public final void remove(Tweenable target) {
		for (Tween t : tweens ) {
			if (t.getTarget() == target &&
					!t.isFinished())
				t.kill();
		}
	}
	
	public final void remove(Tween target) {
		for (Tween t : tweens ) {
			if (t == target ){
				if(!t.isFinished())
					t.kill();
				tweens.remove(t);
			}
		}
	}

	/**
	 * Kills every valid tween associated to the given target and tween type.
	 */
	public final void remove(Tweenable target, TweenType tweenType) {
		for (Tween t : tweens ) {
			if (t.getTarget() == target &&
					t.getTweenType() == tweenType && 
					!t.isFinished())
				t.kill();
		}
	}

	/**
	 * Gets the number of tweens managed by this manager.
	 * 
	 * @return The number of tweens in the manager.
	 */
	public int getTweenCount() {
		return tweens.size();
	}

	/**
	 * Gets an array containing every tween in the manager.
	 */
	public Tween[] getTweens() {
		return tweens.toArray(new Tween[tweens.size()]);
	}

	/**
	 * Gets an array containing every tween in the manager dedicated to the
	 * given target.
	 */
	public Tween[] getTweens(Tweenable target) {
		ArrayList<Tween> selectedTweens = new ArrayList<Tween>();
		for (Tween t : tweens) {
			if (t.getTarget() == target &&
					!t.isFinished())
				selectedTweens.add(t);
		}
		return selectedTweens.toArray(new Tween[selectedTweens.size()]);
	}

	/**
	 * Gets an array containing every tween in the manager dedicated to the
	 * given target and tween type.
	 */
	public Tween[] getTweens(Tweenable target, TweenType tweenType) {
		ArrayList<Tween> selectedTweens = new ArrayList<Tween>();
		for (Tween t : tweens) {
			if (t.getTarget() == target &&
					t.getTweenType() == tweenType && 
					!t.isFinished())
				selectedTweens.add(t);
		}
		return selectedTweens.toArray(new Tween[selectedTweens.size()]);
	}

	public final void render(Graphics g) {
		long currentMillis = System.currentTimeMillis();

		for (Tween t : tweens) {
			t.render(g, currentMillis);
		}
		//TODO delete completed tweens
		for (Tween t : tweens) {
			if (t.isFinished()){
				tweens.remove(t);
				break;}
		}

	}
}
