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
 * @author Aurelien Ribon (aurelien.ribon@gmail.com)
 */
public class Quad {
	public static final TweenEquation IN = new TweenEquation() {
		@Override
		public final float compute(float t, float b, float c, float d) {
			return c*(t/=d)*t + b;
		}

		@Override
		public String toString() {
			return "Quad.IN";
		}
	};

	public static final TweenEquation OUT = new TweenEquation() {
		@Override
		public final float compute(float t, float b, float c, float d) {
			return -c*(t/=d)*(t-2) + b;
		}

		@Override
		public String toString() {
			return "Quad.OUT";
		}
	};

	public static final TweenEquation INOUT = new TweenEquation() {
		@Override
		public final float compute(float t, float b, float c, float d) {
			if ((t/=d/2) < 1) return c/2*t*t + b;
			return -c/2 * ((--t)*(t-2) - 1) + b;
		}

		@Override
		public String toString() {
			return "Quad.INOUT";
		}
	};
}