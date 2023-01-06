import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.imageio.ImageIO;
import javax.swing.*;


public class GameWindow {
    private JFrame gameWindow;
    
    public Clock blackClock;
    public Clock whiteClock;
    
    
    private Board board;
    
    
    
    public GameWindow(String blackName, String whiteName) {

        
        gameWindow = new JFrame("Chess");
        

        try {
            Image whiteImg = ImageIO.read(getClass().getResource("wp.png"));
            gameWindow.setIconImage(whiteImg);
        } catch (Exception e) {
            System.out.println("Game file wp.png not found");
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
        gameWindow.setResizable(false);
        
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
                        "Are you sure you want to quit?",
                        "Confirm quit", JOptionPane.YES_NO_OPTION);
                
                if (n == JOptionPane.YES_OPTION) {
                    gameWindow.dispose();
                }
            }
          });
        
        final JButton nGame = new JButton("New game");
        
        nGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        gameWindow,
                        "Are you sure you want to begin a new game?",
                        "Confirm new game", JOptionPane.YES_NO_OPTION);
                
                if (n == JOptionPane.YES_OPTION) {
                    SwingUtilities.invokeLater(new StartMenu());
                    gameWindow.dispose();
                }
            }
          });
        
        final JButton instr = new JButton("How to play");
        
        instr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(gameWindow,
                        "Move the chess pieces on the board by clicking\n"
                        + "and dragging. The game will watch out for illegal\n"
                        + "moves. You can win either by your opponent running\n"
                        + "out of time or by checkmating your opponent.\n"
                        + "\nGood luck, hope you enjoy the game!",
                        "How to play",
                        JOptionPane.PLAIN_MESSAGE);
            }
          });
        
        buttons.add(instr);
        buttons.add(nGame);
        buttons.add(quit);
        
        buttons.setPreferredSize(buttons.getMinimumSize());
        
        return buttons;
    }
    
    public void checkmateOccurred (int c) {
        if (c == 0) {
            //if (timer != null) timer.stop();
            int n = JOptionPane.showConfirmDialog(
                    gameWindow,
                    "White wins by checkmate! Set up a new game? \n" +
                    "Choosing \"No\" lets you look at the final situation.",
                    "White wins!",
                    JOptionPane.YES_NO_OPTION);
            
            if (n == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(new StartMenu());
                gameWindow.dispose();
            }
        } else {
            //if (timer != null) timer.stop();
            int n = JOptionPane.showConfirmDialog(
                    gameWindow,
                    "Black wins by checkmate! Set up a new game? \n" +
                    "Choosing \"No\" lets you look at the final situation.",
                    "Black wins!",
                    JOptionPane.YES_NO_OPTION);
            
            if (n == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(new StartMenu());
                gameWindow.dispose();
            }
        }
    }
}