import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.*;


public class Board extends JPanel implements MouseListener, MouseMotionListener {

    private String whiteBishop = "wbishop.png";
	private String blackBishop = "bbishop.png";
	private String whiteKnight = "wknight.png";
	private String blackKnight = "bknight.png";
	private String whiteRook = "wrook.png";
	private String blackRook = "brook.png";
	private String whiteKing = "wking.png";
	private String blackKing = "bking.png";
	private String blackQueen = "bqueen.png";
	private String whiteQueen = "wqueen.png";
	private String whitePawn = "wpawn.png";
	private String blackPawn = "bpawn.png";
	
	private Square[][] board;
    private ChessWindow g;
    
    public LinkedList<Piece> Bpieces;
    public LinkedList<Piece> Wpieces;
    public List<Square> movable;
    
    private boolean whiteTurn;

    private Piece currPiece;
    private int currX;
    private int currY;
    
    private CheckmateCheck cmd;
    
    public Board(ChessWindow game) {
        g = game;
        board = new Square[8][8];
        Bpieces = new LinkedList<Piece>();
        Wpieces = new LinkedList<Piece>();
        setLayout(new GridLayout(8, 8, 0, 0));

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int xMod = x % 2;
                int yMod = y % 2;

                if ((xMod == 0 && yMod == 0) || (xMod == 1 && yMod == 1)) {
                    board[x][y] = new Square(this, 1, y, x);
                    this.add(board[x][y]);
                } 
                
                else {
                    board[x][y] = new Square(this, 0, y, x);
                    this.add(board[x][y]);
                }
            }
        }
        
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(1040, 1040));
        createPieces();

        this.setPreferredSize(new Dimension(1040, 1040));
        this.setMaximumSize(new Dimension(1040, 1040));
        

        whiteTurn = true;

    }

    private void createPieces() {
    	
        for (int x = 0; x < 8; x++) {
            board[1][x].put(new Pawn(0, board[1][x], blackPawn));
            board[6][x].put(new Pawn(1, board[6][x], whitePawn));
        }
        
        board[7][3].put(new Queen(1, board[7][3], whiteQueen));
        board[0][3].put(new Queen(0, board[0][3], blackQueen));
        
        King bk = new King(0, board[0][4], blackKing);
        King wk = new King(1, board[7][4], whiteKing);
        board[0][4].put(bk);
        board[7][4].put(wk);

        board[0][0].put(new Rook(0, board[0][0], blackRook));
        board[0][7].put(new Rook(0, board[0][7], blackRook));
        board[7][0].put(new Rook(1, board[7][0], whiteRook));
        board[7][7].put(new Rook(1, board[7][7], whiteRook));

        board[0][1].put(new Knight(0, board[0][1], blackKnight));
        board[0][6].put(new Knight(0, board[0][6], blackKnight));
        board[7][1].put(new Knight(1, board[7][1], whiteKnight));
        board[7][6].put(new Knight(1, board[7][6], whiteKnight));

        board[0][2].put(new Bishop(0, board[0][2], blackBishop));
        board[0][5].put(new Bishop(0, board[0][5], blackBishop));
        board[7][2].put(new Bishop(1, board[7][2], whiteBishop));
        board[7][5].put(new Bishop(1, board[7][5], whiteBishop));
        
        
        for(int y = 0; y < 2; y++) {
            for (int x = 0; x < 8; x++) {
                Bpieces.add(board[y][x].getOccupyingPiece());
                Wpieces.add(board[7-y][x].getOccupyingPiece());
            }
        }
        
        cmd = new CheckmateCheck(this, Wpieces, Bpieces, wk, bk);
    }

    public Square[][] getSquareArray() {
        return this.board;
    }

    public boolean getTurn() {
        return whiteTurn;
    }

    public void setCurrPiece(Piece p) {
        this.currPiece = p;
    }

    public Piece getCurrPiece() {
        return this.currPiece;
    }

    public void paintComponent(Graphics g) {

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square sq = board[y][x];
                sq.paintComponent(g);
            }
        }

        if (currPiece != null) {
            if ((currPiece.getColor() == 1 && whiteTurn)
                    || (currPiece.getColor() == 0 && !whiteTurn)) {
                final Image i = currPiece.getImage();
                g.drawImage(i, currX - 13, currY - 13, 100, 100, null);
            }
        }
    }


    public void mousePressed(MouseEvent e) {
        currX = e.getX();
        currY = e.getY();

        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if (sq.isOccupied()) {
            currPiece = sq.getOccupyingPiece();
            if (currPiece.getColor() == 0 && whiteTurn)
                return;
            if (currPiece.getColor() == 1 && !whiteTurn)
                return;
            sq.setDisplay(false);
        }
        repaint();
    }
    public void mouseReleased(MouseEvent e) {
        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if (currPiece != null) {
            if (currPiece.getColor() == 0 && whiteTurn) {
                return;
            }
            if (currPiece.getColor() == 1 && !whiteTurn) {
                return;
            }
                
            List<Square> legalMoves = currPiece.getMoves(this);
            movable = cmd.getAllowableSquares(whiteTurn);

            if (legalMoves.contains(sq) && movable.contains(sq)
                    && cmd.checkMove(currPiece, sq)) {
                sq.setDisplay(true);
                currPiece.move(sq);
                cmd.update();

                if (cmd.blackCheckMated()) {
                    currPiece = null;
                    repaint();
                    this.removeMouseListener(this);
                    this.removeMouseMotionListener(this);
                    g.checkmateOccurred(0);
                } else if (cmd.whiteCheckMated()) {
                    currPiece = null;
                    repaint();
                    this.removeMouseListener(this);
                    this.removeMouseMotionListener(this);
                    g.checkmateOccurred(1);
                } else {
                    currPiece = null;
                    whiteTurn = !whiteTurn;
                    movable = cmd.getAllowableSquares(whiteTurn);
                }

            } else {
                currPiece.getPosition().setDisplay(true);
                currPiece = null;
            }
        }

        repaint();
    }

    
    public void mouseDragged(MouseEvent e) {
        
        currX = e.getX() - 24;
        currY = e.getY() - 24;

        repaint();
        
    }
    

 

    public void mouseMoved(MouseEvent e) {
    }


    public void mouseClicked(MouseEvent e) {
    }


    public void mouseEntered(MouseEvent e) {
    }

 
    public void mouseExited(MouseEvent e) {
    }
    

}