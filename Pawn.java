/*
 * Aidan Chien, Aiden Donavan, Max Novak
 * 
 * 
 * This class inherits from Piece, and has a method called getMoves, which returns the specific moves a pawn can make.
 * 
 */
import java.util.List;
import java.util.LinkedList;

public class Pawn extends Piece {
    private boolean wasMoved;
    
    public Pawn(int color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }
    
    //determins if the piece was moved
    public boolean move(Square fin) {
        boolean b = super.move(fin);
        wasMoved = true;
        return b;
    }

    //method gets legal moves for the pawns
    public List<Square> getMoves(Board b) {
        LinkedList<Square> legalMoves = new LinkedList<Square>();
        Square[][] board = b.getBoardArray();
        int x = this.getPosition().getXNum();
        int y = this.getPosition().getYNum();
        int c = this.getColor();
        
        if (c == 0) {
            if (!wasMoved) {
                if (!board[y + 2][x].isOccupied()) {
                    legalMoves.add(board[y + 2][x]);
                }
            }
            
            if (y + 1 < 8) {
                if (!board[y + 1][x].isOccupied()) {
                    legalMoves.add(board[y + 1][x]);
                }
            }
            
            if (x + 1 < 8 && y + 1 < 8) {
                if (board[y + 1][x + 1].isOccupied()) {
                    legalMoves.add(board[y + 1][x + 1]);
                }
            }
                
            if (x - 1 >= 0 && y + 1 < 8) {
                if (board[y + 1][x - 1].isOccupied()) {
                    legalMoves.add(board[y + 1][x - 1]);
                }
            }
        }
        //marks occupied squares
        if (c == 1) {
            if (!wasMoved) {
                if (!board[y - 2][x].isOccupied()) {
                    legalMoves.add(board[y - 2][x]);
                }
            }
            
            if (y - 1 >= 0) {
                if (!board[y - 1][x].isOccupied()) {
                    legalMoves.add(board[y - 1][x]);
                }
            }
            
            if (x + 1 < 8 && y - 1 >= 0) {
                if (board[y - 1][x + 1].isOccupied()) {
                    legalMoves.add(board[y - 1][x + 1]);
                }
            }
                
            if (x - 1 >= 0 && y - 1 >= 0) {
                if (board[y - 1][x - 1].isOccupied()) {
                    legalMoves.add(board[y - 1][x - 1]);
                }
            }
        }
        //returns the legal moves (obviously)
        return legalMoves;
    }
}