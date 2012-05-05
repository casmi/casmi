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
package casmi.sound;

class StereoBuffer implements AudioListener
{
  public MAudioBuffer left;
  public MAudioBuffer right;
  public MAudioBuffer mix;
  
  private Controller parent;
  
  StereoBuffer(int type, int bufferSize, Controller c)
  {
    left = new MAudioBuffer(bufferSize);
    if ( type == Sound.MONO )
    {
      right = left;
      mix = left;
    }
    else
    {
      right = new MAudioBuffer(bufferSize);
      mix = new MAudioBuffer(bufferSize);
    }
    parent = c;
  }
  
  public void samples(float[] samp)
  {
    // Minim.debug("Got samples!");
    left.set(samp);
    parent.update();
  }

  public void samples(float[] sampL, float[] sampR)
  {
    left.set(sampL);
    right.set(sampR);
    mix.mix(sampL, sampR);
    parent.update();
  }
}
