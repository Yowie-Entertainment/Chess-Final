/*
 * Aidan Chien, Aiden Donavan, Max Novak
 * 
 * 
 * This class inherits from Piece, and has a method called getMoves, which returns the specific moves a bishop can make.
 * 
 */


import java.util.List;

public class Bishop extends Piece {

    public Bishop(int color, Square initSq, String img) {
        super(color, initSq, img);
    }
    
    //gets legal moves for the bishop
    public List<Square> getMoves(Board b) {
        Square[][] board = b.getBoardArray();
        int x = this.getPosition().getXNum();
        int y = this.getPosition().getYNum();
        
        //see "Piece" class for this method
        return getDiagonalOccupations(board, x, y); 
    }
}