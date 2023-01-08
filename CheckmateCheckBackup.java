import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

// TODO: It does not say checkmate during checkmate. The king can move into a square guarded by the checkmating piece after the mate. 
//This happens during an actual mate, and when the checkmating piece can be captured to stop mate.

// TODO: It does say checkmate when not. If the checkmating piece can be captured, and the king cannot move anywhere, it calls checkmate.

//TODO: IF JUST NORMAL CHeck, and the checking piece is capturable, king can move into a square already covered


public class CheckmateCheckBackup {
    private final LinkedList<Square> squares;
    private King bk;
    private King wk;
    private HashMap<Square,List<Piece>> mWhite;
    private HashMap<Square,List<Piece>> mBlack;
    private LinkedList<Piece> pBlack;
    private LinkedList<Square> movableSquares;
    private Board b;
    private LinkedList<Piece> pWhite;
    
    public CheckmateCheckBackup(Board board, LinkedList<Piece> pWhite1, LinkedList<Piece> pBlack1, King wk1, King bk1) {
        //initializes instance variables
        b = board;
        pWhite = pWhite1;
        pBlack = pBlack1;
        bk = bk1;
        wk = wk1;
        squares = new LinkedList<Square>();
        movableSquares = new LinkedList<Square>();
        mWhite = new HashMap<Square,List<Piece>>();
        mBlack = new HashMap<Square,List<Piece>>();
        Square[][] brd = b.getBoardArray();
        
        // add all squares to squares list, and create new mWhite and mBlack, in which the key is
        // the square, and the value is a list of pieces that can move there
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                squares.add(brd[y][x]);
                mWhite.put(brd[y][x], new LinkedList<Piece>());
                mBlack.put(brd[y][x], new LinkedList<Piece>());
            }
        }
        update();
    }
    
    
   
    //this method will update the board with new mWhite and mBlack  
    public void update() {

        //create iterators for all the pWhite and pBlack
        Iterator<Piece> wIter = pWhite.iterator();
        Iterator<Piece> bIter = pBlack.iterator();
        
        //remove all the mWhite and mBlack moves each time it is updated
        for (List<Piece> pieces : mWhite.values()) {
            pieces.removeAll(pieces);
        }
        
        for (List<Piece> pieces : mBlack.values()) {
            pieces.removeAll(pieces);
        }
        
        movableSquares.removeAll(movableSquares);
        
        // adds all possible white moves to mWhite
        while (wIter.hasNext()) {
            Piece p = wIter.next();
            //if the piece is not king (because the king has a seperate method)
            if (!p.getClass().equals(King.class)) {
                //if the piece is dead then remove it from mWhite
                if (p.getPosition() == null) {
                    wIter.remove();
                    continue;
                }
                //get all the possible moves the piece can make and add them to mWhite
                List<Square> mvs = p.getMoves(b);
                Iterator<Square> iter = mvs.iterator();
                while (iter.hasNext()) {
                    List<Piece> pieces = mWhite.get(iter.next());
                    pieces.add(p);
                }
            }
        }

        //do the exact same thing as above, but with all the black pieces
        while (bIter.hasNext()) {
            Piece p = bIter.next();
            
            if (!p.getClass().equals(King.class)) {
                if (p.getPosition() == null) {
                    wIter.remove();
                    continue;
                }
                
                List<Square> mvs = p.getMoves(b);
                Iterator<Square> iter = mvs.iterator();
                while (iter.hasNext()) {
                    List<Piece> pieces = mBlack.get(iter.next());
                    pieces.add(p);
                }
            }
        }
    }
    
    //check if black is in check
    public boolean blackInCheck() {
        update();
        Square sq = bk.getPosition();
        if (mWhite.get(sq).isEmpty()) {
            movableSquares.addAll(squares);
            return false;
        } 
        else {
            return true;
        }
    }
    
    //check if white is in check
    public boolean whiteInCheck() {
        update();
        Square sq = wk.getPosition();
        if (mBlack.get(sq).isEmpty()) {
            movableSquares.addAll(squares);
            return false;
        } 
        else {
            return true;
        }
    }
    
    //check if black is check mated
    public boolean blackCheckMated() {
        boolean checkmate = true;
        // if black is not in check, then it can't be checkmate
        if (!this.blackInCheck()) {
            return false;
        }
        
        
        // use the evade method to see if the king can run away
        if (evade(mWhite, bk)) {
            checkmate = false;
        }
        
        // use the captureThreat method to see if the threat can be captured by something DOESNTWORK
        List<Piece> threats = mWhite.get(bk.getPosition());
        if (captureThreat(mBlack, threats, bk)) {
            checkmate = false;
        }
        
        // use the block method to see if the threat can be blocked somehow
        if (block(threats, mBlack, bk)) {
            checkmate = false;
        }
        
        // else return that it is checkmate
        return checkmate;
    }
    
    //same as above but with white
    public boolean whiteCheckMated() {
        boolean checkmate = true;
        // check if in check
        if (!this.whiteInCheck()) {
            return false;
        }
        
        // check if king can run
        if (evade(mBlack, wk)) {
            checkmate = false;
        }
        
        // check if king can capture
        List<Piece> threats = mBlack.get(wk.getPosition());
        if (captureThreat(mWhite, threats, wk)) {
            checkmate = false;
        }
        
        // check if king can block
        if (block(threats, mWhite, wk)) {
            checkmate = false;
        }
        // if all else is false, then it is checkmate
        return checkmate;
    }
    
    //check if the king can move away from check mate
    private boolean evade(Map<Square, List<Piece>> coveredSquares, King tKing) {
        
        //create an iterator for all the king's possible moves
        List<Square> kingsMoves = tKing.getMoves(b);
        Iterator<Square> iter = kingsMoves.iterator();
        
        boolean evadable = false;
        // checking all the squares the king can go to, and if he can go there and not be in check
        while (iter.hasNext()) {
            Square sq = iter.next();
            //check if king can move to square and not be in check, if false, continue to next iteration
            if (checkMove(tKing, sq) == false) {
                continue;
            }

            //if the given square is not covered, add it to movablesquares of the king and set that it is evadable
            if (coveredSquares.get(sq).isEmpty()) {
                movableSquares.add(sq);
                evadable = true;
            }
        }
        
        return evadable;
    }
    

    
    
    
    // test if the check can be blocked by a friendly piece
    private boolean block(List<Piece> attackers, Map <Square,List<Piece>> blockMoves, King k) {
        
        boolean blockable = false;
        
        //get the class of the attacker
        Class<? extends Piece> attackerClass = attackers.get(0).getClass();

        //if there is only 1 attacker
        if (attackers.size() == 1) {
            
            //variables for the attacker's and king's square and get the array of the board
            Square attackerSquare = attackers.get(0).getPosition();
            
            Square kingSquare = k.getPosition();
            Square[][] boardArray = b.getBoardArray();
            
            //if the king and attacker are in the same rank, see if any pieces can be put between them
            if (kingSquare.getXNum() == attackerSquare.getXNum()) {
                //get the higher and lower value y value to find all the squares between them
                int lowerValue = Math.min(kingSquare.getYNum(), attackerSquare.getYNum());
                
                int higherValue = Math.max(kingSquare.getYNum(), attackerSquare.getYNum());
                
                
                //loop through those middle squares to see if any pieces can move to them
                for (int j = lowerValue + 1; j < higherValue; j++) {
                    //blocks is the list of the pieces that can move to the certain square (j)
                    List<Piece> blocks = blockMoves.get(boardArray[j][kingSquare.getXNum()]);
                    //add blocks to a concurrentlinkeddeque so it can be shared among different threads
                    ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                    blockers.addAll(blocks);
                    
                    //while blockers still has pieces
                    if (blockers.isEmpty() == false) {
                        //adds j to moveableSquares
                        movableSquares.add(boardArray[j][kingSquare.getXNum()]);
                        
                        //for every piece that can move to j
                        for (Piece p : blockers) {
                            //if it stops check, then blockable is true
                            if (checkMove(p,boardArray[j][kingSquare.getXNum()])) {
                                blockable = true;
                            }
                        }
                        
                    }
                }
            }
            
            //same thing as above but if the king and attacker are on the same file
            if (kingSquare.getYNum() == attackerSquare.getYNum()) {
                //get higher and lower value of the x values
                int higherValue = Math.max(kingSquare.getXNum(), attackerSquare.getXNum());
                int lowerValue = Math.min(kingSquare.getXNum(), attackerSquare.getXNum());
                
                //see above comments, same process applies
                for (int j = lowerValue + 1; j < higherValue; j++) {
                    List<Piece> blocks = blockMoves.get(boardArray[kingSquare.getYNum()][j]);
                    ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                    blockers.addAll(blocks);
                    
                    if (!blockers.isEmpty()) {
                        
                        movableSquares.add(boardArray[kingSquare.getYNum()][j]);
                        
                        for (Piece p : blockers) {
                            if (checkMove(p, boardArray[kingSquare.getYNum()][j])) {
                                blockable = true;
                            }
                        }
                        
                    }
                }
            }
            
            //now see if the king is being checked from a queen or bishop (by a diagonal)
            if (attackerClass.equals(Queen.class) || attackerClass.equals(Bishop.class)) {
                int attackX = attackerSquare.getXNum();
                int attackY = attackerSquare.getYNum();
                
                int kingX = kingSquare.getXNum();
                int kingY = kingSquare.getYNum();
                
                // if the king is diagonally up from attacker
                if (kingX > attackX && kingY > attackY) {
                    for (int i = attackX + 1; i < kingX; i++) {
                        attackY++;
                        List<Piece> blocks = blockMoves.get(boardArray[attackY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blocks);
                        
                        if (!blockers.isEmpty()) {
                            movableSquares.add(boardArray[attackY][i]);
                            
                            for (Piece p : blockers) {
                                if (checkMove(p, boardArray[attackY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
                
                if (kingX > attackX && attackY > kingY) {
                    for (int i = attackX + 1; i < kingX; i++) {
                        attackY--;
                        List<Piece> blocks = blockMoves.get(boardArray[attackY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blocks);
                        
                        if (!blockers.isEmpty()) {
                            movableSquares.add(boardArray[attackY][i]);
                            
                            for (Piece p : blockers) {
                                if (checkMove(p, boardArray[attackY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
                
                if (attackX > kingX && kingY > attackY) {
                    for (int i = attackX - 1; i > kingX; i--) {
                        attackY++;
                        List<Piece> blocks = blockMoves.get(boardArray[attackY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blocks);
                        
                        if (!blockers.isEmpty()) {
                            movableSquares.add(boardArray[attackY][i]);
                            
                            for (Piece p : blockers) {
                                if (checkMove(p, boardArray[attackY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
                
                if (attackX > kingX && attackY > kingY) {
                    for (int i = attackX - 1; i > kingX; i--) {
                        attackY--;
                        List<Piece> blocks = blockMoves.get(boardArray[attackY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blocks);
                        
                        if (!blockers.isEmpty()) {
                            movableSquares.add(boardArray[attackY][i]);
                            
                            for (Piece p : blockers) {
                                if (checkMove(p, boardArray[attackY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return blockable;
    }
    
    //Helper method to determine if the threatening piece can be captured.
    private boolean captureThreat(Map<Square,List<Piece>> poss, 
            List<Piece> threats, King k) {
        
        boolean capture = false;
        if (threats.size() == 1) {
            Square sq = threats.get(0).getPosition();
            
            if (k.getMoves(b).contains(sq)) {
                movableSquares.add(sq);
                if (checkMove(k, sq)) {
                    capture = true;
                }
            }
            
            List<Piece> caps = poss.get(sq);
            ConcurrentLinkedDeque<Piece> capturers = new ConcurrentLinkedDeque<Piece>();
            capturers.addAll(caps);
            
            if (!capturers.isEmpty()) {
                movableSquares.add(sq);
                for (Piece p : capturers) {
                    if (checkMove(p, sq)) {
                        capture = true;
                    }
                }
            }
        }
        
        return capture;
    }
    
    //all squares the player can move. Especially when player is in check.
    public List<Square> getAllowableSquares(boolean b) {
        movableSquares.removeAll(movableSquares);
        if (whiteInCheck()) {
            whiteCheckMated();
        } else if (blackInCheck()) {
            blackCheckMated();
        }
        return movableSquares;
    }
    
    //make sure king can't move into check
    public boolean checkMove(Piece p, Square sq) {
        Piece c = sq.getOccupyingPiece();
        
        boolean movetest = true;
        Square init = p.getPosition();
        
        p.move(sq);
        update();
        
        if (p.getColor() == 0 && blackInCheck()) {
            movetest = false;
        }
        else if (p.getColor() == 1 && whiteInCheck()){ 
             movetest = false; 
        }
        
        p.move(init);
        if (c != null) sq.put(c);
        
        update();
        
        movableSquares.addAll(squares);
        return movetest;
    }

}
