import java.util.LinkedList;
import java.util.List;

public class King extends Piece {

    public King(int color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }

    public List<Square> getMoves(Board b) {
        LinkedList<Square> legalMoves = new LinkedList<Square>();
        
        Square[][] board = b.getSquareArray();
        
        int x = this.getPosition().getXNum();
        int y = this.getPosition().getYNum();
        
        for (int index = 1; index > -2; index--) {
            for (int index2 = 1; index2 > -2; index2--) {
                if(!(index == 0 && index2 == 0)) {
                    try {
                        if(!board[y + index2][x + index].isOccupied() || 
                                board[y + index2][x + index].getOccupyingPiece().getColor() 
                                != this.getColor()) {
                            legalMoves.add(board[y + index2][x + index]);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                }
            }
        }
        
        return legalMoves;
    }

}