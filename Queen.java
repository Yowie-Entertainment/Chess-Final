import java.util.LinkedList;
import java.util.List;

public class Queen extends Piece {

    public Queen(int color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }

    //gets the legal moves for the queen
    public List<Square> getMoves(Board b) {
        LinkedList<Square> legalMoves = new LinkedList<Square>();
        Square[][] board = b.getBoardArray();
        
        int x = this.getPosition().getXNum();
        int y = this.getPosition().getYNum();
        
        int[] occups = getLinearOccupations(board, x, y); //gets occupied squares left, right, up, and down
        
        for (int index = occups[0]; index <= occups[1]; index++) {
            if (index != y) legalMoves.add(board[index][x]);
        }
        
        for (int index2 = occups[2]; index2 <= occups[3]; index2++) {
            if (index2 != x) legalMoves.add(board[y][index2]);
        }
        
        List<Square> bMoves = getDiagonalOccupations(board, x, y); //gets occupied squares along diagonals. See "Piece" class for this method
        
        legalMoves.addAll(bMoves); //adds diagonal moves to all moves
        
        return legalMoves;
    }
    
}