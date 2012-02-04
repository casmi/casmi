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

/**
 * A TweenGroup can be used to create complex animations made of sequences and
 * parallel sets of Tweens.
 * 
 * @see Tween
 * @see TweenManager
 */
public class TweenSerialGroup extends TweenGroup {

	/**
	 * Convenience method to create a group with its elements running as a
	 * sequence. TweenGroup.sequence(...);
	 * TweenGroup.getNew().addInSequence(...); </pre>
	 * 
	 * @param objs
	 *            A list of objects made of Tweens and/or TweenGroups.
	 * @return The TweenGroup created.
	 */
	public static TweenSerialGroup create(Groupable... objs) {
		TweenSerialGroup group = new TweenSerialGroup();
		
		for (Groupable o : objs){
			group.append(o);
		}
		return group;
	}

	/**
	 * Adds a list of Tweens and/or TweenGroups and make them run after the
	 * current ones, one after the other.
	 * 
	 * @param objs
	 *            A list of objects made of Tweens and/or TweenGroups.
	 * @return The group, for instruction chaining.
	 */
	public TweenSerialGroup append(Groupable... objs) {
		for (Groupable o : objs) {
			if (o != null) {
				if (getGroupables().isEmpty()) {
					getGroupables().add(o);
				} else {
					Groupable last = getGroupables().get(getGroupables().size() - 1);
					o.addDelay(last.getDelay() + last.getDuration());
					getGroupables().add(o);
				}
				setDuration( getDuration() + o.getDelay() + o.getDuration() );
			}
		}
		return this;
	}

}
