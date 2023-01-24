


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
        
        return getDiagonalOccupations(board, x, y); //see "Piece" class for this method
    }
}