/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2012, Xcoo, Inc.
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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;

import ddf.minim.AudioEffect;
import ddf.minim.AudioInput;
import ddf.minim.AudioOutput;
import ddf.minim.AudioRecorder;
import ddf.minim.AudioSample;
import ddf.minim.AudioSignal;
import ddf.minim.AudioSnippet;
import ddf.minim.Recordable;
import ddf.minim.spi.MinimServiceProvider;

/**
 * Sound class.
 * 
 * @author Y. Ban
 */
public class Sound {

    ddf.minim.Sound sound;

    public Sound() {
        sound = new ddf.minim.Sound();
    }

    public Sound(MinimServiceProvider msp) {
        sound = new ddf.minim.Sound(msp);
    }

    /**
     * Used internally to report error messages. These error messages will
     * appear in the console area of the PDE if you are running a sketch from the
     * PDE, otherwise they will appear in the Java Console.
     * 
     * @param s
     *     the error message to report
     */
    public static void error(String s) {
        ddf.minim.Sound.error(s);
    }

    /**
     * Displays a debug message, but only if {@link #debugOn()} has been called.
     * The message will be displayed in the console area of the PDE,
     * if you are running your sketch from the PDE.
     * Otherwise, it will be displayed in the Java Console.
     * 
     * @param s
     *            the message to display
     * @see #debugOn()
     */
    public static void debug(String s) {
        ddf.minim.Sound.debug(s);
    }

    /**
     * Turns on debug messages.
     */
    public void debugOn() {
        sound.debugOn();
    }

    /**
     * Turns off debug messages.
     */
    public void debugOff() {
        sound.debugOff();
    }

    /**
     * Stops Minim.
     * 
     * A call to this method should be placed inside of the stop() function of
     * your sketch. We expect that implemenations of the Minim
     * interface made need to do some cleanup, so this is how we
     * tell them it's time.
     */
    public void stop() {
        sound.stop();
    }

    /**
     * Sets the Javasound Mixer that will be used for obtaining input sources
     * such as AudioInputs. This will do nothing if you have provided your
     * own MinimServiceProvider.
     * 
     * @param mixer
     *     The Mixer we should try to acquire inputs from.
     */
    public void setInputMixer(Mixer mixer) {
        sound.setInputMixer(mixer);
    }

    /**
     * Sets the Javasound Mixer that will be used for obtain output destinations
     * such as those required by AudioOuput, AudioPlayer, AudioSample, and so
     * forth.
     * This will do nothing if you have provided your own MinimServiceProvider.
     * 
     * @param mixer
     *     The Mixer we should try to acquire outputs from.
     */
    public void setOutputMixer(Mixer mixer) {
        sound.setOutputMixer(mixer);
    }

    /**
     * Creates an {@link AudioSample} using the provided samples and
     * AudioFormat,
     * with an output buffer size of 1024 samples.
     * 
     * @param samples
     *     the samples to use
     * @param format
     *     the format to play the samples back at
     */
    public AudioSample createSample(float[] samples, AudioFormat format) {
        return sound.createSample(samples, format, 1024);
    }

    /**
     * Creates an {@link AudioSample} using the provided samples and
     * AudioFormat, with the desired output buffer size.
     * 
     * @param samples
     *     the samples to use
     * @param format
     *     the format to play them back at
     * @param bufferSize
     *     the output buffer size to use
     */
    public AudioSample createSample(float[] samples, AudioFormat format, int bufferSize) {
        return sound.createSample(samples, format, bufferSize);
    }

    /**
     * Creates an {@link AudioSample} using the provided left and right channel
     * samples with an output buffer size of 1024.
     * 
     * @param left
     *     the left channel of the sample
     * @param right
     *     the right channel of the sample
     * @param format
     *     the format the sample should be played back with
     */
    public AudioSample createSample(float[] left, float[] right, AudioFormat format) {
        return sound.createSample(left, right, format, 1024);
    }

    /**
     * Creates an {@link AudioSample} using the provided left and right channel
     * samples.
     * 
     * @param left
     *     the left channel of the sample
     * @param right
     *     the right channel of the sample
     * @param format
     *     the format the sample should be played back with
     * @param bufferSize
     *     the output buffer size desired
     */
    public AudioSample createSample(float[] left, float[] right, AudioFormat format, int bufferSize) {
        return sound.createSample(left, right, format, bufferSize);
    }

    /**
     * Loads the requested file into an {@link AudioSample}.
     * 
     * @param filename
     *      the file or URL that you want to load
     * 
     * @return an <code>AudioSample</code> with a 1024 sample buffer
     * 
     * @see #loadSample(String, int)
     * @see AudioSample
     */
    public AudioSample loadSample(String filename) {
        return sound.loadSample(filename, 1024);
    }

    /**
     * Loads the requested file into an {@link AudioSample}.
     * 
     * @param filename
     *     the file or URL that you want to load
     * @param bufferSize
     *     the sample buffer size you want
     *
     * @return an <code>AudioSample</code> with a sample buffer of the requested size
     */
    public AudioSample loadSample(String filename, int bufferSize) {
        return sound.loadSample(filename, bufferSize);
    }

    /**
     * Loads the requested file into an {@link AudioSnippet}.
     * 
     * @param filename
     *     the file or URL you want to load
     * 
     * @return an <code>AudioSnippet</code> of the requested file or URL
     */
    public AudioSnippet loadSnippet(String filename) {
        return sound.loadSnippet(filename);
    }

    /**
     * Loads the requested file into an {@link AudioPlayer} with a buffer size
     * of 1024 samples.
     * 
     * @param filename
     *     the file or URL you want to load
     * 
     * @return an <code>AudioPlayer</code> with a 1024 sample buffer
     * 
     * @see #loadFile(String, int)
     */
    public AudioPlayer loadFile(String filename) {
        return this.loadFile(filename, 1024);
    }

    /**
     * Loads the requested file into an {@link AudioPlayer} with
     * the request buffer size.
     * 
     * @param filename
     *     the file or URL you want to load
     * @param bufferSize
     *     the sample buffer size you want
     * 
     * @return an <code>AudioPlayer</code> with a sample buffer of the requested size
     */
    public AudioPlayer loadFile(String filename, int bufferSize) {
        ddf.minim.AudioPlayer minimAudioPlayer = sound.loadFile(filename, bufferSize);
        AudioPlayer audioplayer = new AudioPlayer(minimAudioPlayer);
        return audioplayer;
    }

    /**
     * Creates an {@link AudioRecorder} that will use <code>source</code> as its
     * record source and that will save to the file name specified.
     * The format of the file will be inferred from the extension in the file name.
     * If the extension is not a recognized file type, this will return null.
     * Be aware that if you choose buffered recording the call to {@link AudioRecorder#save()}
     * will block until the entire buffer is written to disk.
     * In the event that the buffer is very large, your sketch will noticably hang.
     * 
     * @param source
     *     the <code>Recordable</code> object you want to use as a record source
     * @param fileName
     *     the name of the file to record to
     * @param buffered
     *     whether or not to use buffered recording
     * 
     * @return an <code>AudioRecorder</code> for the record source
     */
    public AudioRecorder createRecorder(Recordable source, String fileName, boolean buffered) {
        return sound.createRecorder(source, fileName, buffered);
    }

    /**
     * Gets an {@link AudioInput}, to which you can attach {@link AudioEffect AudioEffects}.
     * 
     * @return an STEREO <code>AudioInput</code> with a 1024 sample buffer, a
     *     sample rate of 44100 and a bit depth of 16
     * 
     * @see #getLineIn(int, int, float, int)
     */
    public AudioInput getLineIn() {
        return sound.getLineIn();
    }

    /**
     * Gets an {@link AudioInput}, to which you can attach {@link AudioEffect AudioEffects}.
     * 
     * @param type
     *     Minim.MONO or Minim.STEREO
     * 
     * @return an <code>AudioInput</code> with the requested type, a 1024 sample
     *     buffer, a sample rate of 44100 and a bit depth of 16
     * 
     * @see #getLineIn(int, int, float, int)
     */
    public AudioInput getLineIn(int type) {
        return sound.getLineIn(type);
    }

    /**
     * Gets an {@link AudioInput}, to which you can attach {@link AudioEffect AudioEffects}.
     * 
     * @param type
     *     Minim.MONO or Minim.STEREO
     * @param bufferSize
     *     how long you want the <code>AudioInput</code>'s sample buffer to be
     * 
     * @return an <code>AudioInput</code> with the requested attributes, a
     *     sample rate of 44100 and a bit depth of 16
     * 
     * @see #getLineIn(int, int, float, int)
     */
    public AudioInput getLineIn(int type, int bufferSize) {
        return sound.getLineIn(type, bufferSize);
    }

    /**
     * Gets an {@link AudioInput}, to which you can attach {@link AudioEffect AudioEffects}.
     * 
     * @param type
     *     Minim.MONO or Minim.STEREO
     * @param bufferSize
     *     how long you want the <code>AudioInput</code>'s sample buffer to be
     * @param sampleRate
     *     the desired sample rate in Hertz (typically 44100)
     * 
     * @return an <code>AudioInput</code> with the requested attributes and a
     *     bit depth of 16
     * 
     * @see #getLineIn(int, int, float, int)
     */
    public AudioInput getLineIn(int type, int bufferSize, float sampleRate) {
        return sound.getLineIn(type, bufferSize, sampleRate);
    }

    /**
     * Gets an {@link AudioInput}, to which you can attach {@link AudioEffect AudioEffects}.
     * 
     * @param type
     *     Minim.MONO or Minim.STEREO
     * @param bufferSize
     *     how long you want the <code>AudioInput</code>'s sample buffer to be
     * @param sampleRate
     *     the desired sample rate in Hertz (typically 44100)
     * @param bitDepth
     *     the desired bit depth (typically 16)
     * 
     * @return an <code>AudioInput</code> with the requested attributes
     */
    public AudioInput getLineIn(int type, int bufferSize, float sampleRate, int bitDepth) {
        return sound.getLineIn(type, bufferSize, sampleRate, bitDepth);
    }

    /**
     * Gets an {@link AudioOutput}, to which you can attach {@link AudioSignal AudioSignals}
     * and {@link AudioEffect AudioEffects}.
     * 
     * @return a STEREO <code>AudioOutput</code> with a 1024 sample buffer, a sample
     *     rate of 44100 and a bit depth of 16
     * 
     * @see #getLineOut(int, int, float, int)
     */
    public AudioOutput getLineOut() {
        return sound.getLineOut();
    }

    /**
     * Gets an {@link AudioOutput}, to which you can attach {@link AudioSignal AudioSignals}
     * and {@link AudioEffect AudioEffects}.
     * 
     * @param type
     *     Minim.MONO or Minim.STEREO
     * 
     * @return an <code>AudioOutput</code> with the requested type, a 1024 sample
     *     buffer, a sample rate of 44100 and a bit depth of 16
     * 
     * @see #getLineOut(int, int, float, int)
     */
    public AudioOutput getLineOut(int type) {
        return sound.getLineOut(type);
    }

    /**
     * Gets an {@link AudioOutput}, to which you can attach {@link AudioSignal AudioSignals}
     * and {@link AudioEffect AudioEffects}.
     * 
     * @param type
     *     Minim.MONO or Minim.STEREO
     * @param bufferSize
     *     how long you want the <code>AudioOutput</code>'s sample buffer to be
     * 
     * @return an <code>AudioOutput</code> with the requested attributes, a sample
     *     rate of 44100 and a bit depth of 16
     * 
     * @see #getLineOut(int, int, float, int)
     */
    public AudioOutput getLineOut(int type, int bufferSize) {
        return sound.getLineOut(type, bufferSize);
    }

    /**
     * Gets an {@link AudioOutput}, to which you can attach {@link AudioSignal AudioSignals}
     * and {@link AudioEffect AudioEffects}.
     * 
     * @param type
     *     Minim.MONO or Minim.STEREO
     * @param bufferSize
     *     how long you want the <code>AudioOutput</code>'s sample buffer to be
     * @param sampleRate
     *     the desired sample rate in Hertz (typically 44100)
     * 
     * @return an <code>AudioOutput</code> with the requested attributes and a
     *     bit depth of 16
     * 
     * @see #getLineOut(int, int, float, int)
     */
    public AudioOutput getLineOut(int type, int bufferSize, float sampleRate) {
        return sound.getLineOut(type, bufferSize, sampleRate);
    }

    /**
     * Gets an {@link AudioOutput}, to which you can attach {@link AudioSignal AudioSignals}
     * and {@link AudioEffect AudioEffects}.
     * 
     * @param type
     *     Minim.MONO or Minim.STEREO
     * @param bufferSize
     *     how long you want the <code>AudioOutput</code>'s sample buffer to be
     * @param sampleRate
     *     the desired sample rate in Hertz (typically 44100)
     * @param bitDepth
     *     the desired bit depth (typically 16)
     * 
     * @return an <code>AudioOutput</code> with the requested attributes
     */
    public AudioOutput getLineOut(int type, int bufferSize, float sampleRate, int bitDepth) {
        return sound.getLineOut(type, bufferSize, sampleRate, bitDepth);
    }

}
