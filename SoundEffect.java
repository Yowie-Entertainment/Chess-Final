/*
 * Aidan Chien, Aiden Donavan, Max Novak
 * 
 * 
 * This class controls the sound effects of the game, and an object is created at the beginning of the chesswindow etc, and the methods are called to play
 * sound effects when moves or checks are made.
 */
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundEffect {
	
	Clip clip;
	
	//sets file for sound effect
	public void setFile(URL name) {
		
		try {
			AudioInputStream sound = AudioSystem.getAudioInputStream(name);
			clip = AudioSystem.getClip();
			clip.open(sound);
		}
		catch(Exception e) {
			
		}
	}
	
	//plays sound effect
	public void play(URL name) {
		
		clip.setFramePosition(0);
		clip.start();
	}
	
	//stops the sound effect
	public void stop(URL name) {
		
		clip.stop();
	}
}