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

/**
 * A TweenGroup can be used to create complex animations made of sequences and
 * parallel sets of Tweens.
 *
 * @author Y. Ban
 *
 * @see Tween
 * @see TweenManager
 */
public class TweenParallelGroup extends TweenGroup {

    /**
     * Convenience method to create a group with its elements running all in
     * parallel. Both following calls lead to the same result:<br/>
     * TweenGroup.parallel(...);
     * TweenGroup.getNew().addInParallel(...);
     * </pre>
     * @param objs A list of objects made of Tweens and/or TweenGroups.
     * @return The TweenGroup created.
     */
    public static TweenParallelGroup create(Groupable... objs) {
        TweenParallelGroup group = new TweenParallelGroup();
        for (Groupable o : objs)
            group.append(o);
        return group;
    }

    /**
     * Adds a list of Tweens and/or TweenGroups and make them run in parallel
     * to the current ones.
     * @param objs A list of objects made of Tweens and/or TweenGroups.
     * @return The group, for instruction chaining.
     */
    public TweenParallelGroup append(Groupable... objs) {
        for (Groupable o : objs) {
            if (o != null) {
                getGroupables().add(o);
                setDuration( Math.max(getDuration(), o.getDelay() + o.getDuration()) );
            }
        }
        return this;
    }
}
