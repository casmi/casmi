package casmi.sound;

import minim.sound.AudioMetaData;
import minim.sound.spi.AudioRecordingStream;

public class AudioPlayer {
	minim.sound.AudioPlayer audioPlayer;
	
	/**
	 * Constructs an <code>AudioPlayer</code> that plays <code>recording</code>.
	 * It is expected that <code>recording</code> will have a
	 * <code>DataLine</code> to control. If it doesn't, any calls to
	 * <code>Controller</code>'s methods will result in a
	 * <code>NullPointerException</code>.
	 * 
	 * @param recording
	 *           the <code>AudioRecording</code> to play
	 */
	public AudioPlayer(AudioRecordingStream recording)
	{
		audioPlayer = new minim.sound.AudioPlayer(recording);
	}
	
	public AudioPlayer(minim.sound.AudioPlayer audioPlayer){
		this.audioPlayer = audioPlayer;
	}

	public void play()
	{
		audioPlayer.play();
	}

	public void play(int millis)
	{
		audioPlayer.play(millis);
	}

	public void pause()
	{
		audioPlayer.pause();
	}

	public void rewind()
	{
		audioPlayer.rewind();
	}

	public void loop()
	{
		audioPlayer.loop();
	}

	public void loop(int n)
	{
		audioPlayer.loop(n);
	}

	public int loopCount()
	{
		return audioPlayer.loopCount();
	}

	public int length()
	{
		return audioPlayer.length();
	}

	public int position()
	{
		return audioPlayer.position();
	}

	public void cue(int millis)
	{
		audioPlayer.cue(millis);
	}

	public void skip(int millis)
	{
		audioPlayer.skip(millis);
	}

	public boolean isLooping()
	{
		return audioPlayer.isLooping();
	}

	public boolean isPlaying()
	{
		return audioPlayer.isPlaying();
	}
	
	public boolean isFinished()
	{
		return audioPlayer.isFinished();
	}

	/**
	 * Returns the meta data for the recording being played by this player.
	 * 
	 * @return the meta data for this player's recording
	 */
	public AudioMetaData getMetaData()
	{
		return audioPlayer.getMetaData();
	}

	public void setLoopPoints(int start, int stop)
	{
		audioPlayer.setLoopPoints(start, stop);
	}

	public int bufferSize() {
		return audioPlayer.bufferSize();
	}
	
	public minim.sound.AudioBuffer left(){
		return audioPlayer.left;
	}
	
	public minim.sound.AudioBuffer right(){
		return audioPlayer.right;
	}
	
	public void close(){
		audioPlayer.close();
	}
	
	
	
}
