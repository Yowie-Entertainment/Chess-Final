/*
 * Aidan Chien, Aiden Donavan, Max Novak
 * 
 * 
 * This class inherits from Piece, and has a method called getMoves, which returns the specific moves a knight can make.
 * 
 */

import java.util.LinkedList;
import java.util.List;

public class Knight extends Piece {
    
    //gets legal moves for the knight
    public List<Square> getMoves(Board b) {
        LinkedList<Square> legalMoves = new LinkedList<Square>();
        Square[][] board = b.getBoardArray();
        int x = this.getPosition().getXNum();
        int y = this.getPosition().getYNum();
        
        //adds all of the legal moves in an "L" shape from where the knight is
        for (int index = 2; index > -3; index--) {
            for (int index2 = 2; index2 > -3; index2--) {
                if(Math.abs(index) == 2 ^ Math.abs(index2) == 2) {
                    if (index2 != 0 && index != 0) {
                        try {
                            legalMoves.add(board[y + index2][x + index]);
                        } 
                        catch (ArrayIndexOutOfBoundsException e) {
                            continue;
                        }
                    }
                }
            }
        }
        
        return legalMoves;
    }
    
    public Knight(int color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }

}