
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
package casmi.sound.javasound;

import casmi.sound.AudioMetaData;
import casmi.sound.AudioSample;
import casmi.sound.spi.AudioSynthesizer;

final class JSAudioSample extends AudioSample
{
  private SampleSignal sample;
  private AudioMetaData meta;
  
  JSAudioSample(AudioMetaData mdata, SampleSignal ssig, AudioSynthesizer out)
  {
    super(out);
    sample = ssig;
    meta = mdata;
  }
  
  public void trigger()
  {
    sample.trigger();
  }
  
  public void stop()
  {
    sample.stop();
  }
  
  public float[] getChannel(int channelNumber)
  {
    return sample.getChannel(channelNumber);
  }

  public int length()
  {
    return meta.length();
  }
  
  public AudioMetaData getMetaData()
  {
	  return meta;
  }
}
