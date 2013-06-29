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
 * @author Takashi AOKI <federkasten@me.com>
 *
 */
public class TweenerManager {

    private final ArrayList<Tweener> tweens;

    /**
     * Instantiates a new manager.
     */
    public TweenerManager() {
        this.tweens = new ArrayList<Tweener>();
    }

    /**
     * Adds a new tween to the manager and starts it.
     *
     * @param tween
     *            A tween. Does nothing if it is already present.
     * @return The manager, for instruction chaining.
     */
    public final TweenerManager add(Tweener t) {
        tweens.add(t);
        t.start();
        return this;
    }

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
    public final boolean contains(Tweener target) {
// TODO fix
//        for (Tweener t : tweens) {
//            if (t.getElement() == target.getElement() && !t.isFinished())
//                return true;
//        }
        return false;
    }

    public final void remove(Tweener target) {
// TODO fix
//        for (Tweener t : tweens ) {
//            if (t == target ){
//                if(!t.isFinished())
//                    t.kill();
//                tweens.remove(t);
//            }
//        }
    }

    /**
     * Gets the number of tweens managed by this manager.
     *
     * @return The number of tweens in the manager.
     */
    public int getTweenCount() {
        return tweens.size();
    }

//    /**
//     * Gets an array containing every tween in the manager.
//     */
//    public Tween[] getTweens() {
//        return tweens.toArray(new Tween[tweens.size()]);
//    }

//    /**
//     * Gets an array containing every tween in the manager dedicated to the
//     * given target.
//     */
//    public Tween[] getTweens(Tweenable target) {
//        ArrayList<Tween> selectedTweens = new ArrayList<Tween>();
//        for (Tween t : tweens) {
//            if (t.getTarget() == target &&
//                    !t.isFinished())
//                selectedTweens.add(t);
//        }
//        return selectedTweens.toArray(new Tween[selectedTweens.size()]);
//    }

//    /**
//     * Gets an array containing every tween in the manager dedicated to the
//     * given target and tween type.
//     */
//    public Tween[] getTweens(Tweenable target, TweenType tweenType) {
//        ArrayList<Tween> selectedTweens = new ArrayList<Tween>();
//        for (Tween t : tweens) {
//            if (t.getTarget() == target &&
//                    t.getTweenType() == tweenType &&
//                    !t.isFinished())
//                selectedTweens.add(t);
//        }
//        return selectedTweens.toArray(new Tween[selectedTweens.size()]);
//    }

    public final void render(Graphics g) {
        for (Tweener t : tweens) {
            t.render();
        }

//        for (Tweener t : tweens) {
//            if (t.isFinished()){
//                tweens.remove(t);
//                break;}
//        }
    }
}
