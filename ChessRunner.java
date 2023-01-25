/*
 * Aidan Chien, Aiden Donavan, Max Novak
 * 
 * 
 * This is our main class that the user runs when they want to run the game. It creates a title screen for the user.
 * Is contains the main method for our project.
 */
import javax.swing.*;

public class ChessRunner implements Runnable {
    public void run() {
        SwingUtilities.invokeLater(new TitleScreen());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ChessRunner());
    }
    
}