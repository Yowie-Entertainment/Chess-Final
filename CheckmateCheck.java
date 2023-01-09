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


public class CheckmateCheck {
    private final LinkedList<Square> squares;
    private King bk;
    private King wk;
    private HashMap<Square,List<Piece>> mWhite;
    private HashMap<Square,List<Piece>> mBlack;
    private LinkedList<Piece> bPieces;
    private LinkedList<Square> movableSquares;
    private Board b;
    private LinkedList<Piece> wPieces;
    
    public CheckmateCheck(Board board, LinkedList<Piece> wPieces1, LinkedList<Piece> bPieces1, King wk1, King bk1) {
        //initializes instance variables
        b = board;
        wPieces = wPieces1;
        bPieces = bPieces1;
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

        
        //create iterators for all the wPieces and bPieces
        Iterator<Piece> w = wPieces.iterator();
        Iterator<Piece> bIterator = bPieces.iterator();
        
        int king = 0;
        while (w.hasNext()) {
            Piece p = w.next();
            if (p.getClass() == King.class) {
                king++;
            }
        }
        if (king == 0) {
            b.getChessWindow().checkmateOccurred(1);
            //b.getChessWindow().getGameWindow().dispose();
        }

        while (bIterator.hasNext()) {
            Piece p = bIterator.next();
            if (p.getClass() == King.class) {
                king++;
            }
        }
        if (king == 1) {
            b.getChessWindow().checkmateOccurred(0);
            //b.getChessWindow().getGameWindow().dispose();
        }
        
        //remove all the mWhite and mBlack moves each time it is updated
        
        Iterator<Piece> wIter = wPieces.iterator();
        Iterator<Piece> bIter = bPieces.iterator();
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
        
        // use the captureAttacker method to see if the threat can be captured by something DOESNTWORK
        List<Piece> threats = mWhite.get(bk.getPosition());
        if (captureAttacker(mBlack, threats, bk)) {
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
        if (captureAttacker(mWhite, threats, wk)) {
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
    private boolean evade(Map<Square, List<Piece>> tMoves, King tKing) {
        
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
            if (tMoves.get(sq).isEmpty()) {
                movableSquares.add(sq);
                evadable = true;
            }
        }
        
        return evadable;
    }
    

    
    
    
    //Helper method to determine if check can be blocked by a piece.
    private boolean block(List<Piece> threats, Map <Square,List<Piece>> blockMoves, King k) {
        boolean blockable = false;
        
        if (threats.size() == 1) {
            Square ts = threats.get(0).getPosition();
            Square ks = k.getPosition();
            Square[][] brdArray = b.getBoardArray();
            
            if (ks.getXNum() == ts.getXNum()) {
                int max = Math.max(ks.getYNum(), ts.getYNum());
                int min = Math.min(ks.getYNum(), ts.getYNum());
                
                for (int i = min + 1; i < max; i++) {
                    List<Piece> blks = blockMoves.get(brdArray[i][ks.getXNum()]);
                    ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                    blockers.addAll(blks);
                    
                    if (!blockers.isEmpty()) {
                        movableSquares.add(brdArray[i][ks.getXNum()]);
                        
                        for (Piece p : blockers) {
                            if (checkMove(p,brdArray[i][ks.getXNum()])) {
                                blockable = true;
                            }
                        }
                        
                    }
                }
            }
            
            if (ks.getYNum() == ts.getYNum()) {
                int max = Math.max(ks.getXNum(), ts.getXNum());
                int min = Math.min(ks.getXNum(), ts.getXNum());
                
                for (int i = min + 1; i < max; i++) {
                    List<Piece> blks = blockMoves.get(brdArray[ks.getYNum()][i]);
                    ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                    blockers.addAll(blks);
                    
                    if (!blockers.isEmpty()) {
                        
                        movableSquares.add(brdArray[ks.getYNum()][i]);
                        
                        for (Piece p : blockers) {
                            if (checkMove(p, brdArray[ks.getYNum()][i])) {
                                blockable = true;
                            }
                        }
                        
                    }
                }
            }
            
            Class<? extends Piece> tC = threats.get(0).getClass();
            
            if (tC.equals(Queen.class) || tC.equals(Bishop.class)) {
                int kX = ks.getXNum();
                int kY = ks.getYNum();
                int tX = ts.getXNum();
                int tY = ts.getYNum();
                
                if (kX > tX && kY > tY) {
                    for (int i = tX + 1; i < kX; i++) {
                        tY++;
                        List<Piece> blks = 
                                blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = 
                                new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blks);
                        
                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[tY][i]);
                            
                            for (Piece p : blockers) {
                                if (checkMove(p, brdArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
                
                if (kX > tX && tY > kY) {
                    for (int i = tX + 1; i < kX; i++) {
                        tY--;
                        List<Piece> blks = 
                                blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = 
                                new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blks);
                        
                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[tY][i]);
                            
                            for (Piece p : blockers) {
                                if (checkMove(p, brdArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
                
                if (tX > kX && kY > tY) {
                    for (int i = tX - 1; i > kX; i--) {
                        tY++;
                        List<Piece> blks = 
                                blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = 
                                new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blks);
                        
                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[tY][i]);
                            
                            for (Piece p : blockers) {
                                if (checkMove(p, brdArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
                
                if (tX > kX && tY > kY) {
                    for (int i = tX - 1; i > kX; i--) {
                        tY--;
                        List<Piece> blks = 
                                blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = 
                                new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blks);
                        
                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[tY][i]);
                            
                            for (Piece p : blockers) {
                                if (checkMove(p, brdArray[tY][i])) {
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
    
    //check if someothing or the king can capture the attacker
    private boolean captureAttacker(Map<Square,List<Piece>> poss, 
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