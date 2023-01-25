/*
 * Aidan Chien, Aiden Donavan, Max Novak
 * 
 * This class is each square on the board. It contains a occupyingPiece, which is the piece that is on the square.
 * This is painted either color (either green or beige), and repaints each piece when it moves onto each square
 */
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.*;

public class Square extends JComponent {
    private Board b;
    private int xNum;
    private int yNum;
    private boolean color;
    private Piece occupyingPiece;
    private boolean pieceDisplay;
    
    public Square(Board b, boolean c, int xNum, int yNum) {
        this.b = b;
        color = c;
        pieceDisplay = true;
        this.xNum = xNum;
        this.yNum = yNum;
        this.setBorder(BorderFactory.createEmptyBorder());
    }
    
    //setters and getters for the instance variables and such
    public Piece getOccupyingPiece() {
        return occupyingPiece;
    }
    
    public boolean isOccupied() {
        return (this.occupyingPiece != null);
    }

    public void setDisplay(boolean v) {
        this.pieceDisplay = v;
    }
    
    public boolean getColor() {
        return this.color;
    }
    
    public void put(Piece p) {
        this.occupyingPiece = p;
        p.setPosition(this);
    }
    
    //changes the display when some piece is captured
    public void capture(Piece p) {
        Piece k = getOccupyingPiece();
        if (k.getColor() == 0) {
            b.pBlack.remove(k);
        }
        if (k.getColor() == 1) {
            b.pWhite.remove(k);
        }
        this.occupyingPiece = p;
    }

    public int getXNum() {
        return this.xNum;
    }
    
    public int getYNum() {
        return this.yNum;
    }
    
    public String toString() {
        return ""+ xNum + "" + yNum;
    }

    //paints the square and the occupying piece on the square
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (color) {
            g.setColor(new Color(221,192,127));
        } 
        
        else {
            g.setColor(new Color(32,87,31));
        }
        
        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        
        if(occupyingPiece != null && pieceDisplay) {
            occupyingPiece.draw(g);
        }
    }
    
    //removes the piece
    public Piece removePiece() {
        Piece p = this.occupyingPiece;
        this.occupyingPiece = null;
        return p;
    }

    //custom hashcode for the squares
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + xNum;
        result = prime * result + yNum;
        return result;
    }
    
}