/*
 * Aidan Chien, Aiden Donavan, Max Novak
 * 
 * 
 * This class inherits from Piece, and has a method called getMoves, which returns the specific moves a king can make.
 * 
 */

import java.util.LinkedList;
import java.util.List;

public class King extends Piece {

    public King(int color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }

    //this method gets the legal moves for the king
    public List<Square> getMoves(Board b) {
        LinkedList<Square> legalMoves = new LinkedList<Square>();
        
        Square[][] board = b.getBoardArray();
        
        int x = this.getPosition().getXNum();
        int y = this.getPosition().getYNum();
        
        for (int index = 1; index > -2; index--) {
            for (int index2 = 1; index2 > -2; index2--) {
                if(!(index == 0 && index2 == 0)) {
                    try {
                        //basically if the square is not occupied or if is, but by an enemy piece
                        if(!board[y + index2][x + index].isOccupied() || board[y + index2][x + index].getOccupyingPiece().getColor() 
                        != this.getColor()) { 
                            //add it to the legal moves
                            legalMoves.add(board[y + index2][x + index]); 
                        }
                    } 
                    
                    catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                }
            }
        }
        //and return it
        return legalMoves; 
    }

}