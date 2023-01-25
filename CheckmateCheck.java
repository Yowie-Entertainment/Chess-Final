/*
 * Aidan Chien, Aiden Donavan, Max Novak
 * 
 * 
 * This class is what controls the checkmate detector and also the check detector to see whether each king is in check or in checkmate.
 * 
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CheckmateCheck {
    private final LinkedList<Square> squares;
    private King bk;
    private King wk;
    private HashMap<Square, List<Piece>> mWhite;
    private HashMap<Square, List<Piece>> mBlack;
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
        
        // use the captureAttacker method to see if the threat can be captured by something
        List<Piece> attackers = mWhite.get(bk.getPosition());

         if (captureAttacker(mBlack, attackers, bk)) {
            checkmate = false;
         }
        // use the block method to see if the threat can be blocked somehow
        if (block(attackers, mBlack, bk)) {
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
        List<Piece> attackers = mBlack.get(wk.getPosition());
         if (captureAttacker(mWhite, attackers, wk)) {
             checkmate = false;
         }
        
        // check if king can block
        if (block(attackers, mWhite, wk)) {
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
    
    //see if the check can be blocked
    private boolean block(List<Piece> attackers, Map <Square,List<Piece>> blockMoves, King k) {
        boolean blockable = false;
        //if there is only 1 attacker
        if (attackers.size() == 1) {
            //variables for the attacker's and king's square and get the array of the board
            Square ts = attackers.get(0).getPosition();
            Square ks = k.getPosition();
            Square[][] brdArray = b.getBoardArray();
            //if the king and attacker are in the same rank, see if any pieces can be put between them
            if (ks.getXNum() == ts.getXNum()) {
                //get the higher and lower value y value to find all the squares between them
                int highValue = Math.max(ks.getYNum(), ts.getYNum());
                int lowValue = Math.min(ks.getYNum(), ts.getYNum());
                //loop through those middle squares to see if any pieces can move to them
                for (int j = lowValue + 1; j < highValue; j++) {
                    //blocks is the list of the pieces that can move to the certain square (j)
                    List<Piece> blocks = blockMoves.get(brdArray[j][ks.getXNum()]);
                    ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                    blockers.addAll(blocks);
                    //while blockers still has pieces
                    if (!blockers.isEmpty()) {
                        //adds j to moveableSquares
                        movableSquares.add(brdArray[j][ks.getXNum()]);
                        
                        //for every piece that can move to j
                        for (Piece p : blockers) {

                            //if it stops check, then blockable is true
                            if (checkMove(p,brdArray[j][ks.getXNum()])) {
                                blockable = true;
                            }
                        }
                        
                    }
                }
            }
            
            //same thing as above but if the king and attacker are on the same file
            if (ks.getYNum() == ts.getYNum()) {

                //get higher and lower value of the x values
                int highValue = Math.max(ks.getXNum(), ts.getXNum());
                int lowValue = Math.min(ks.getXNum(), ts.getXNum());
                
                //see above comments, same process applies
                for (int j = lowValue + 1; j < highValue; j++) {
                    List<Piece> blocks = blockMoves.get(brdArray[ks.getYNum()][j]);
                    ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                    blockers.addAll(blocks);
                    
                    if (!blockers.isEmpty()) {
                        
                        movableSquares.add(brdArray[ks.getYNum()][j]);
                        
                        for (Piece p : blockers) {
                            if (checkMove(p, brdArray[ks.getYNum()][j])) {
                                blockable = true;
                            }
                        }
                        
                    }
                }
            }
            
            //check the class of the attacking piece to see if it is checking the king diagonally
            Class<? extends Piece> attackerClass = attackers.get(0).getClass();
            if (attackerClass.equals(Queen.class) || attackerClass.equals(Bishop.class)) {
                int attackX = ts.getXNum();
                int attackY = ts.getYNum();
                int kingX = ks.getXNum();
                int kingY = ks.getYNum();
                
                // if attacker is up and left from king
                if (kingX > attackX && kingY > attackY) {
                    for (int i = attackX + 1; i < kingX; i++) {
                        attackY++;
                        List<Piece> blocks = blockMoves.get(brdArray[attackY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blocks);
                        
                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[attackY][i]);
                            
                            for (Piece p : blockers) {
                                if (checkMove(p, brdArray[attackY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
                
                //if attacker is down and left
                if (kingX > attackX && attackY > kingY) {
                    for (int i = attackX + 1; i < kingX; i++) {
                        attackY--;
                        List<Piece> blocks = blockMoves.get(brdArray[attackY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blocks);
                        
                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[attackY][i]);
                            
                            for (Piece p : blockers) {
                                if (checkMove(p, brdArray[attackY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
                
                //if attacker is up and right
                if (attackX > kingX && kingY > attackY) {
                    for (int i = attackX - 1; i > kingX; i--) {
                        attackY++;
                        List<Piece> blocks = blockMoves.get(brdArray[attackY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blocks);
                        
                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[attackY][i]);
                            
                            for (Piece p : blockers) {
                                if (checkMove(p, brdArray[attackY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
                
                //if attacker is down and right
                if (attackX > kingX && attackY > kingY) {
                    for (int i = attackX - 1; i > kingX; i--) {
                        attackY--;
                        List<Piece> blocks = blockMoves.get(brdArray[attackY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blocks);
                        
                        if (!blockers.isEmpty()) {
                            movableSquares.add(brdArray[attackY][i]);
                            
                            for (Piece p : blockers) {
                                if (checkMove(p, brdArray[attackY][i])) {
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
    private boolean captureAttacker(Map<Square,List<Piece>> poss, List<Piece> attackers, King k) {
        boolean capture = false;
        if (attackers.size() == 1) {
            Square sq = attackers.get(0).getPosition();
            
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
        } 
        
        else if (blackInCheck()) {
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
        if (c != null) {
            sq.put(c);
        }

        update();
        movableSquares.addAll(squares);
        return movetest;
    }

}