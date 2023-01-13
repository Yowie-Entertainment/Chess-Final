
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;

public class TitleScreen implements Runnable {
    public void run() {
        final JFrame startWindow = new JFrame("Yowie Chess");
       
    
        startWindow.setLocationRelativeTo(null);
        startWindow.setResizable(false);
        startWindow.setSize(1000, 1000);
        
        Box components = Box.createVerticalBox();
        startWindow.add(components);
        
        



      final JPanel titlePanel = new JPanel();
      components.add(titlePanel);
      titlePanel.setBounds(0,0,700,600);
      titlePanel.setOpaque(false);
      components.add(titlePanel);
   
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

      
        // Buttons
        Box buttons = Box.createHorizontalBox();
        final JButton quit = new JButton("Quit");
        
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              startWindow.dispose();
            }
          });
        
        final JButton start = new JButton("Start");
        
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ChessWindow();
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