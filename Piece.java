import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

public abstract class Piece {
    private int color;
    private Square currentSquare;
    private BufferedImage img;
    
    public Piece(int color, Square initSq, String imageFile) {
        this.color = color;
        this.currentSquare = initSq;
        
        try {
            if (this.img == null) {
              this.img = ImageIO.read(getClass().getResource(imageFile));
            }
          } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
          }
    }
    
    public boolean move(Square destination) {
        Piece occup = destination.getOccupyingPiece();
        
        if (occup != null) {
            if (occup.getColor() == this.color) {
                return false;
            }
            else {
                destination.capture(this);
            }
        }
        
        currentSquare.removePiece();
        this.currentSquare = destination;
        currentSquare.put(this);
        return true;
    }
    
    public Square getPosition() {
        return currentSquare;
    }
    
    public void setPosition(Square sq) {
        this.currentSquare = sq;
    }
    
    public int getColor() {
        return color;
    }
    
    public Image getImage() {
        return img;
    }
    
    public void draw(Graphics g) {
        int x = currentSquare.getX();
        int y = currentSquare.getY();
        
        g.drawImage(this.img, x + 10, y + 10, 100, 100,  null);
    }
    
    public int[] getLinearOccupations(Square[][] board, int x, int y) {
        int yAbove = 0;
        int xRight = 7;
        int yBelow = 7;
        int xLeft = 0;
        
        //find the last moveable square linearly to the square (above)
        for (int i = 0; i < y; i++) {
            if (board[i][x].isOccupied()) {
                if (board[i][x].getOccupyingPiece().getColor() != this.color) {
                    yAbove = i;
                } 
                else    {
                    yAbove = i + 1;
                }
            }
        }

        //find last square below
        for (int i = 7; i > y; i--) {
            if (board[i][x].isOccupied()) {
                if (board[i][x].getOccupyingPiece().getColor() != this.color) {
                    yBelow = i;
                } 
                
                else {
                    yBelow = i - 1;
                }
            }
        }

        //find last square left
        for (int i = 0; i < x; i++) {
            if (board[y][i].isOccupied()) {
                if (board[y][i].getOccupyingPiece().getColor() != this.color) {
                    xLeft = i;
                } 
                
                else {
                    xLeft = i + 1;
                }
            }
        }

        //find last square right
        for (int i = 7; i > x; i--) {
            if (board[y][i].isOccupied()) {
                if (board[y][i].getOccupyingPiece().getColor() != this.color) {
                    xRight = i;
                } else xRight = i - 1;
            }
        }
        
        int[] occups = {yAbove, yBelow, xLeft, xRight};
        
        return occups;
    }
    
    //find last square in the diagonal directions
    public List<Square> getDiagonalOccupations(Square[][] board, int x, int y) {
        
        LinkedList<Square> diagOccup = new LinkedList<Square>();
        
        int xNW = x - 1;
        int xSW = x - 1;
        int xNE = x + 1;
        int xSE = x + 1;
        int yNW = y - 1;
        int ySW = y + 1;
        int yNE = y - 1;
        int ySE = y + 1;
        
        while (xNW >= 0 && yNW >= 0) {
            if (board[yNW][xNW].isOccupied()) {
                if (board[yNW][xNW].getOccupyingPiece().getColor() == this.color) {
                    break;
                } 
                else {
                    diagOccup.add(board[yNW][xNW]);
                    break;
                }
            } 
            
            else {
                diagOccup.add(board[yNW][xNW]);
                yNW--;
                xNW--;
            }
        }
        
        while (xSW >= 0 && ySW < 8) {
            if (board[ySW][xSW].isOccupied()) {
                if (board[ySW][xSW].getOccupyingPiece().getColor() == this.color) {
                    break;
                } 
                else {
                    diagOccup.add(board[ySW][xSW]);
                    break;
                }
            } 
            
            else {
                diagOccup.add(board[ySW][xSW]);
                ySW++;
                xSW--;
            }
        }
        
        while (xSE < 8 && ySE < 8) {
            if (board[ySE][xSE].isOccupied()) {
                if (board[ySE][xSE].getOccupyingPiece().getColor() == this.color) {
                    break;
                } else {
                    diagOccup.add(board[ySE][xSE]);
                    break;
                }
            } else {
                diagOccup.add(board[ySE][xSE]);
                ySE++;
                xSE++;
            }
        }
        
        while (xNE < 8 && yNE >= 0) {
            if (board[yNE][xNE].isOccupied()) {
                if (board[yNE][xNE].getOccupyingPiece().getColor() == this.color) {
                    break;
                } else {
                    diagOccup.add(board[yNE][xNE]);
                    break;
                }
            } else {
                diagOccup.add(board[yNE][xNE]);
                yNE--;
                xNE++;
            }
        }
        
        return diagOccup;
    }
    
    //each inherited piece will have their own method
    public abstract List<Square> getMoves(Board b);
}