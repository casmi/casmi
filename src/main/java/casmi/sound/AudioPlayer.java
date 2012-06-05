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

import ddf.minim.AudioMetaData;
import ddf.minim.spi.AudioRecordingStream;

/**
 * Audio player class.
 * 
 * @author Y. Ban
 */
public class AudioPlayer {

    ddf.minim.AudioPlayer audioPlayer;

    /**
     * Constructs an <code>AudioPlayer</code> that plays <code>recording</code>.
     * It is expected that <code>recording</code> will have a <code>DataLine</code>
     * to control.
     * If it doesn't, any calls to <code>Controller</code>'s methods will result
     * in a <code>NullPointerException</code>.
     * 
     * @param recording
     *     the <code>AudioRecording</code> to play
     */
    public AudioPlayer(AudioRecordingStream recording) {
        audioPlayer = new ddf.minim.AudioPlayer(recording);
    }

    public AudioPlayer(ddf.minim.AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public void play() {
        audioPlayer.play();
    }

    public void play(int millis) {
        audioPlayer.play(millis);
    }

    public void pause() {
        audioPlayer.pause();
    }

    public void rewind() {
        audioPlayer.rewind();
    }

    public void loop() {
        audioPlayer.loop();
    }

    public void loop(int n) {
        audioPlayer.loop(n);
    }

    public int loopCount() {
        return audioPlayer.loopCount();
    }

    public int length() {
        return audioPlayer.length();
    }

    public int position() {
        return audioPlayer.position();
    }

    public void cue(int millis) {
        audioPlayer.cue(millis);
    }

    public void skip(int millis) {
        audioPlayer.skip(millis);
    }

    public boolean isLooping() {
        return audioPlayer.isLooping();
    }

    public boolean isPlaying() {
        return audioPlayer.isPlaying();
    }

    public boolean isFinished() {
        return audioPlayer.isFinished();
    }

    /**
     * Returns the meta data for the recording being played by this player.
     * 
     * @return the meta data for this player's recording
     */
    public AudioMetaData getMetaData() {
        return audioPlayer.getMetaData();
    }

    public void setLoopPoints(int start, int stop) {
        audioPlayer.setLoopPoints(start, stop);
    }

    public int bufferSize() {
        return audioPlayer.bufferSize();
    }

    public ddf.minim.AudioBuffer left() {
        return audioPlayer.left;
    }

    public ddf.minim.AudioBuffer right() {
        return audioPlayer.right;
    }

    public void close() {
        audioPlayer.close();
    }

}
