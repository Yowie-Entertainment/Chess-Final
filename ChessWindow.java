
/*
 * Aidan Chien, Aiden Donavan, Max Novak
 * 
 * 
 * This is the class that controls the actual window that the board is created on. This is the window that could be opened and closed, and has buttons to go back to the title screen, or quit.
 * 
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ChessWindow {

    //create the window and create a board
    private JFrame window;
    AudioAsset aa = new AudioAsset();
    private Board board;
    private Music music;
    private SoundEffect sound;
    
    public ChessWindow() {
        sound = new SoundEffect();
        music = new Music();
        music.setFile(aa.msuc);
        music.play(aa.msuc);
        music.loop(aa.msuc);
        window = new JFrame("Yowie Chess");
        
        //set the icon image to our company logo
        try {
            Image whiteImg = ImageIO.read(getClass().getResource("yo.png"));
            window.setIconImage(whiteImg);
        } 
        catch (Exception e) {
        
        }

        window.setLocation(400, 0);
        //create the window that chess will be played on
        window.setLayout(new BorderLayout(20,20));
        this.board = new Board(this);
        window.add(board, BorderLayout.CENTER);
        window.add(buttons(), BorderLayout.SOUTH);
        window.setMinimumSize(window.getPreferredSize());
        window.setSize(window.getPreferredSize()); 
        window.setResizable(true);
        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    //create the buttons on the bottom of the screen
    private JPanel buttons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 10, 0));
        
        //pressing quit will create another small window asking if they really want to quit
        JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int butt = JOptionPane.showConfirmDialog(window,"Do you really want to quit?","Confirm quit", JOptionPane.YES_NO_OPTION);
                if (butt == JOptionPane.YES_OPTION) {
                    window.dispose();
                }
            }
          });
        
        //create the button to back to title screen 
        final JButton nGame = new JButton("Back to title screen");
        
        nGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int butt = JOptionPane.showConfirmDialog(window,"Do you really want to go back to title screen?","Confirm", JOptionPane.YES_NO_OPTION);
                if (butt == JOptionPane.YES_OPTION) {
                    SwingUtilities.invokeLater(new TitleScreen());
                    window.dispose();
                }
            }
          });
        
        //add both of these buttons to buttons JPanel 
        buttons.add(nGame);
        buttons.add(quit);
        buttons.setPreferredSize(buttons.getMinimumSize());
        return buttons;
    }
    
    //returns the game window
    public JFrame getGameWindow() {
        return window;
    }

    //this is the method that is called when checkmate has occurred
    public void checkmateOccurred (int c) {
        music.stop(aa.msuc);

        if (c == 0) {
            int butt = JOptionPane.showConfirmDialog(window,"White wins!!!!! Do you want to create a new game? \n" + "Clicking \"No\" allows you to look at the board some more.",
                    "White is the winner!",
                    JOptionPane.YES_NO_OPTION);
            if (butt == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(new TitleScreen());
                window.dispose();
                
            }
        } 

        else {
            int butt = JOptionPane.showConfirmDialog(window,"Black win!!!!! Do you want to create a new game? \n" + "Clicking \"No\" allows you to look at the board some more.",
                    "Black is the winner!",
                    JOptionPane.YES_NO_OPTION);
            if (butt == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(new TitleScreen());
                window.dispose();
            }
        }
    }

}