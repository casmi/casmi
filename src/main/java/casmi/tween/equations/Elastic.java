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
public class Elastic {
	private static final float PI = 3.14159265f;

	public static final TweenEquation IN = new TweenEquation() {
		@Override
		public final float compute(float t, float b, float c, float d) {
			if (t==0) return b;  if ((t/=d)==1) return b+c;
			float p = d*.3f;
			float a = c;
			float s = p/4;
			return -(a*(float)Math.pow(2,10*(t-=1)) * (float)Math.sin( (t*d-s)*(2*PI)/p )) + b;
		}

		@Override
		public String toString() {
			return "Elastic.IN";
		}
	};

	public static final TweenEquation OUT = new TweenEquation() {
		@Override
		public final float compute(float t, float b, float c, float d) {
			if (t==0) return b;  if ((t/=d)==1) return b+c;
			float p = d*.3f;
			float a = c;
			float s = p/4;
			return (a*(float)Math.pow(2,-10*t) * (float)Math.sin( (t*d-s)*(2*PI)/p ) + c + b);
		}

		@Override
		public String toString() {
			return "Elastic.OUT";
		}
	};

	public static final TweenEquation INOUT = new TweenEquation() {
		@Override
		public final float compute(float t, float b, float c, float d) {
			if (t==0) return b;  if ((t/=d/2)==2) return b+c;
			float p = d*(.3f*1.5f);
			float a = c;
			float s = p/4;
			if (t < 1) return -.5f*(a*(float)Math.pow(2,10*(t-=1)) * (float)Math.sin( (t*d-s)*(2*PI)/p )) + b;
			return a*(float)Math.pow(2,-10*(t-=1)) * (float)Math.sin( (t*d-s)*(2*PI)/p )*.5f + c + b;
		}

		@Override
		public String toString() {
			return "Elastic.INOUT";
		}
	};
}
