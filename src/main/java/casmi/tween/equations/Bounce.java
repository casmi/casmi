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

package casmi.tween.equations;

import casmi.tween.TweenEquation;

/**
 * Easing equations based on Robert Penner's work:
 * http://robertpenner.com/easing/
 */
public class Bounce {
	public static final TweenEquation IN = new TweenEquation() {
		@Override
		public final float compute(float t, float b, float c, float d) {
			return c - OUT.compute(d-t, 0, c, d) + b;
		}

		@Override
		public String toString() {
			return "Bounce.IN";
		}
	};

	public static final TweenEquation OUT = new TweenEquation() {
		@Override
		public final float compute(float t, float b, float c, float d) {
			if ((t/=d) < (1/2.75)) {
				return c*(7.5625f*t*t) + b;
			} else if (t < (2/2.75)) {
				return c*(7.5625f*(t-=(1.5f/2.75f))*t + .75f) + b;
			} else if (t < (2.5/2.75)) {
				return c*(7.5625f*(t-=(2.25f/2.75f))*t + .9375f) + b;
			} else {
				return c*(7.5625f*(t-=(2.625f/2.75f))*t + .984375f) + b;
			}
		}

		@Override
		public String toString() {
			return "Bounce.OUT";
		}
	};

	public static final TweenEquation INOUT = new TweenEquation() {
		@Override
		public final float compute(float t, float b, float c, float d) {
			if (t < d/2) return IN.compute(t*2, 0, c, d) * .5f + b;
			else return OUT.compute(t*2-d, 0, c, d) * .5f + c*.5f + b;
		}

		@Override
		public String toString() {
			return "Bounce.INOUT";
		}
	};
}