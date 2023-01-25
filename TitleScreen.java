/*
 * Aidan Chien, Aiden Donavan, Max Novak
 * 
 * 
 * This class is what creates the title screen of our game, and has buttons that
 * create a chesswindow to actually run the game. This is what opens first when the chessrunner is run (our main class).
 */

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.Image;
import javax.imageio.ImageIO;

public class TitleScreen implements Runnable {
    public void run() {
        final JFrame startWindow = new JFrame("Yowie Chess");
        startWindow.setLocationRelativeTo(null);
        startWindow.setResizable(false);
        startWindow.setSize(700, 700);
        startWindow.setLocation(400, 200);
        Box components = Box.createVerticalBox();
        startWindow.add(components);
         
        //set the icon image to our company logo
        try {
          Image glass = ImageIO.read(getClass().getResource("yo.png"));
          startWindow.setIconImage(glass);
        } 
      
        catch (Exception e) {
        
        }
        
        //creates all of the window etc for the title screen
        final JPanel titlePanel = new JPanel();
        components.add(titlePanel);
        titlePanel.setBounds(0,0,400,400);
        titlePanel.setOpaque(false);
        components.add(titlePanel);
        startWindow.setBackground(Color.green);
        
        //create a text for the "CHESS" part of our title screen
        JButton chessB = new JButton("CHESS");
        chessB.setFont(new Font("Book Antiqua", Font.PLAIN,82));      
        chessB.setBounds((700 / 2) - 200, (600 / 2) - 40, 600, 200);
        chessB.setBorder(null);
        chessB.setBackground(null);
        chessB.setFocusPainted(false);
        chessB.setForeground(Color.BLACK);
        chessB.setVisible(true); 
        titlePanel.add(chessB);
        chessB.setContentAreaFilled(false);

        // initialze the buttons
        Box buttons = Box.createHorizontalBox();
        final JButton quit = new JButton("Quit");
        
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              startWindow.dispose();
            }
          });
        
        JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ChessWindow();
                startWindow.dispose();
            }
          });
        
        //create the image of the funny chess man in our title screen
        JPanel imgPanel = new JPanel();
        components.add(imgPanel, BorderLayout.EAST);
        JLabel imgLabel = new JLabel();

        try {
            Image blackImg = ImageIO.read(getClass().getResource("chessfunny.png"));
            blackImg.getScaledInstance(100, 100, 0);
            imgLabel.setIcon(new ImageIcon(blackImg));
            imgPanel.add(imgLabel);
        } 
        
        catch (Exception e) {
        
        }

        //add buttons the rest of the window settings
        buttons.add(start);
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(quit);
        components.add(buttons);
        Component space = Box.createGlue();
        components.add(space);

        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startWindow.setVisible(true);
    }
}