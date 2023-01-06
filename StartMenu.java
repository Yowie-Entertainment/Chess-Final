import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;

public class StartMenu implements Runnable {
    public void run() {
        final JFrame startWindow = new JFrame("Chess");
        // Set window properties
        startWindow.setLocationRelativeTo(null);
        startWindow.setResizable(false);
        startWindow.setSize(1920, 1080);
        
        Box components = Box.createVerticalBox();
        startWindow.add(components);
        
        // Game title
        // final JPanel titlePanel = new JPanel();
        // components.add(titlePanel);
        // final JLabel titleLabel = new JLabel("Chess");
        // titlePanel.add(titleLabel);
        
        // // Black player selections
        // final JPanel blackPanel = new JPanel();
        // components.add(blackPanel, BorderLayout.EAST);
        // final JLabel blackPiece = new JLabel();
        // try {
        //     Image blackImg = ImageIO.read(getClass().getResource("bp.png"));
        //     blackPiece.setIcon(new ImageIcon(blackImg));
        //     blackPanel.add(blackPiece);
        // } catch (Exception e) {
        //     System.out.println("Required game file bp.png missing");
        // }
        



      final JPanel titlePanel = new JPanel();
      components.add(titlePanel);
      titlePanel.setBounds(0,0,1920,1080);
      titlePanel.setOpaque(false);
      components.add(titlePanel);
   
      JButton chessB = new JButton("CHESS");
      chessB.setFont(new Font("Book Antiqua", Font.PLAIN,82));      
      chessB.setBounds((1920 / 2) - 200, (1080 / 2) - 50, 400, 100);
      chessB.setBorder(null);
      chessB.setBackground(null);
      chessB.setFocusPainted(false);
      chessB.setForeground(Color.white);
      chessB.setVisible(true); 
      titlePanel.add(chessB);
      chessB.setContentAreaFilled(true);





        
        
        // final JTextField blackInput = new JTextField("Black", 10);
        // blackPanel.add(blackInput);
        
        // // White player selections
        // final JPanel whitePanel = new JPanel();
        // components.add(whitePanel);
        // final JLabel whitePiece = new JLabel();
        
        // try {
        //     Image whiteImg = ImageIO.read(getClass().getResource("wp.png"));
        //     whitePiece.setIcon(new ImageIcon(whiteImg));
        //     whitePanel.add(whitePiece);
        //     startWindow.setIconImage(whiteImg);
        // }  catch (Exception e) {
        //     System.out.println("Required game file wp.png missing");
        // }
        
        
        // final JTextField whiteInput = new JTextField("White", 10);
        // whitePanel.add(whiteInput);
        
        // Buttons
        Box buttons = Box.createHorizontalBox();
        final JButton quit = new JButton("Quit");
        
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              startWindow.dispose();
            }
          });
        
        final JButton start = new JButton("Start");
        
        chessB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // String bn = blackInput.getText();
                // String wn = whiteInput.getText();
                
                new GameWindow("black", "white");
                startWindow.dispose();
            }
          });
        
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