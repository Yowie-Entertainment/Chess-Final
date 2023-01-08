import javax.swing.*;

public class ChessRunner implements Runnable {
    public void run() {
        SwingUtilities.invokeLater(new TitleScreen());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ChessRunner());
    }
    
}
//TODO: Comment classes
//TODO: Fix checkmate and check logic
//TODO: Fix title screen