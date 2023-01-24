/*
 * Aidan Chien, Aiden Donavan, Max Novak
 * 
 * 
 * This class stores all of the URLs for the sound effects and music in the game.
 * 
 */

import java.net.URL;

public class AudioAsset {
	URL msuc = getClass().getClassLoader().getResource("msuc.wav");
	URL moveSound = getClass().getClassLoader().getResource("moveSound.wav");
	URL checkSound = getClass().getClassLoader().getResource("checckkkksound.wav");
	URL winSound = getClass().getClassLoader().getResource("winSound.wav");
}
