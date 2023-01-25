/*
 * Aidan Chien, Aiden Donavan, Max Novak
 * 
 * 
 * This class inherits from Piece, and has a method called getMoves, which returns the specific moves a rook can make.
 * 
 */


import java.util.LinkedList;
import java.util.List;

public class Rook extends Piece {

    public Rook(int color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }

    //just gets the legal moves for the rook
    public List<Square> getMoves(Board b) {
        LinkedList<Square> legalMoves = new LinkedList<Square>();
        Square[][] board = b.getBoardArray();
        int x = this.getPosition().getXNum();
        int y = this.getPosition().getYNum();
        
         //See "Piece" class for method
        int[] occups = getLinearOccupations(board, x, y);
        
        for (int index = occups[0]; index <= occups[1]; index++) {
            if (index != y) legalMoves.add(board[index][x]);
        }
        
        for (int index2 = occups[2]; index2 <= occups[3]; index2++) {
            if (index2 != x) legalMoves.add(board[y][index2]);
        }
        
        return legalMoves;
    }

}