import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.imageio.ImageIO;
import javax.swing.*;


public class ChessWindow {
    private JFrame gameWindow;

    
    
    private Board board;
    
    
    
    public ChessWindow(String blackName, String whiteName) {

        
        gameWindow = new JFrame("Yowie Chess");
        

        try {
            Image whiteImg = ImageIO.read(getClass().getResource("wking.png"));
            gameWindow.setIconImage(whiteImg);
        } catch (Exception e) {
            System.out.println();
        }

        gameWindow.setLocation(400, 0);
        
        
        gameWindow.setLayout(new BorderLayout(20,20));
       
        // Game Data window
        JPanel gameData = gameDataPanel(blackName, whiteName);
        gameData.setSize(gameData.getPreferredSize());
        gameWindow.add(gameData, BorderLayout.NORTH);
        
        this.board = new Board(this);
        
        gameWindow.add(board, BorderLayout.CENTER);
        
        gameWindow.add(buttons(), BorderLayout.SOUTH);
        
        gameWindow.setMinimumSize(gameWindow.getPreferredSize());
        gameWindow.setSize(gameWindow.getPreferredSize());
        gameWindow.setResizable(true);
        
        gameWindow.pack();
        gameWindow.setVisible(true);
        gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    

    
    private JPanel gameDataPanel(final String bn, final String wn) {
        
        JPanel gameData = new JPanel();
        gameData.setLayout(new GridLayout(3,2,0,0));
        
        gameData.setPreferredSize(gameData.getMinimumSize());
        
        return gameData;
    }
    
    private JPanel buttons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 10, 0));
        
        final JButton quit = new JButton("Quit");
        
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        gameWindow,
                        "Do you really want to quit?",
                        "Confirm quit", JOptionPane.YES_NO_OPTION);
                
                if (n == JOptionPane.YES_OPTION) {
                    gameWindow.dispose();
                }
            }
          });
        
        final JButton nGame = new JButton("Back to title screen");
        
        nGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        gameWindow,
                        "Do you really want to go back to title screen?",
                        "Confirm", JOptionPane.YES_NO_OPTION);
                
                if (n == JOptionPane.YES_OPTION) {
                    SwingUtilities.invokeLater(new TitleScreen());
                    gameWindow.dispose();
                }
            }
          });
 
        buttons.add(nGame);
        buttons.add(quit);
        
        buttons.setPreferredSize(buttons.getMinimumSize());
        
        return buttons;
    }
    
    public JFrame getGameWindow() {
        return gameWindow;
    }
    public void checkmateOccurred (int c) {
        if (c == 0) {
            int n = JOptionPane.showConfirmDialog(gameWindow,"White wins!!!!! Do you want to create a new game? \n" +
                    "Clicking \"No\" allows you to look at the board some more.",
                    "White is the winner!",
                    JOptionPane.YES_NO_OPTION);
            
            if (n == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(new TitleScreen());
                gameWindow.dispose();
            }
        } 
        else {
            int n = JOptionPane.showConfirmDialog(
                    gameWindow,
                    "Black win!!!!! Do you want to create a new game? \n" +
                    "Clicking \"No\" allows you to look at the board some more.",
                    "Black is the winner!",
                    JOptionPane.YES_NO_OPTION);
            
            if (n == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(new TitleScreen());
                gameWindow.dispose();
            }
        }
    }
}