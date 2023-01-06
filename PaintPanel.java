import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PaintPanel extends JPanel {
   BufferedImage titleImage;
	
   public PaintPanel() {

      try {
         titleImage = ImageIO.read(getClass().getClassLoader().getResource("Finalimage.png"));
      	
      }catch(IOException e) {			
      }		
   }

   public void paintComponent(Graphics g) {
      //
      
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      int w = getWidth(), h = getHeight();
      Color color1 = new Color(255,100,0);
      Color color2 = Color.YELLOW;
      GradientPaint gp = new GradientPaint(w / 2, 0, color2, w / 2, h, color1);
      g2d.setPaint(gp);
      g2d.fillRect(0, 0, w, h);
           
      String text = "SUPER CHESS";
      g.setFont(new Font("Book Antiqua", Font.PLAIN, 164));
      g.setColor(Color.BLACK);
   	
      int stringLength = (int)g.getFontMetrics().getStringBounds(text, g).getWidth();
      int start = w / 2 - stringLength / 2;
           		
      g.drawString(text, start, h / 4);		
      g.drawImage(titleImage, 0, 0, w, (int)(1.5 * h), null);
      
   }
}