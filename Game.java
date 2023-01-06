import javax.swing.*;

public class Game implements Runnable {
    public void run() {
        SwingUtilities.invokeLater(new StartMenu());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
//TODO: Comment / revariable / reformat
//TODO: Fix checkmate
//TODO: piece sticking to mouse when drag
//6