import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.image.BufferedImage; // Needed for the image type


public class Board extends JPanel {
    private PuzzlePiece[][] board;
    private ArrayList<PuzzlePiece> pieces;
    private int gridSize, pieceWidth, pieceHeight;

    private PuzzlePiece movedPiece = null;
    private int draggedPieceX, draggedPieceY;
    private int previousRow, previousCol;
    private int emptyRow, emptyCol;

    private Game game;

    public Board(int gridSize, BufferedImage image, Game game) {
        this.gridSize = gridSize;
        this.game = game;
        this.pieceWidth = image.getWidth() / gridSize;
        this.pieceHeight = image.getHeight() / gridSize;

        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        setBackground(Color.WHITE);

        board = new PuzzlePiece[gridSize][gridSize];
        pieces = new ArrayList<>();

        // Create puzzle pieces
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                int id = row * gridSize + col;
                BufferedImage pieceImage = image.getSubimage(col * pieceWidth, row * pieceHeight, pieceWidth, pieceHeight);
                PuzzlePiece piece = new PuzzlePiece(id, pieceImage, row, col);
                pieces.add(piece);
            }
        }

        // Remove last piece to create empty space
        pieces.remove(pieces.size() - 1);

        shuffleBoard();

        int index = 0;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (i == gridSize - 1 && j == gridSize - 1) {
                    board[i][j] = null;
                    emptyRow = i;
                    emptyCol = j;
                } else {
                    board[i][j] = pieces.get(index++);
                }
            }
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point pos = getBoardPosition(e);
                int row = pos.x, col = pos.y;

                if (SwingUtilities.isRightMouseButton(e)) {
                    if (board[row][col] != null) {
                        board[row][col].rotate();
                        repaint();
                    }
                    return;
                }

                if (board[row][col] != null && isAdjacent(row, col, emptyRow, emptyCol)) {
                    movedPiece = board[row][col];
                    previousRow = row;
                    previousCol = col;
                    draggedPieceX = e.getX();
                    draggedPieceY = e.getY();
                    board[row][col] = null;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (movedPiece != null) {
                    board[emptyRow][emptyCol] = movedPiece;
                    emptyRow = previousRow;
                    emptyCol = previousCol;
                    movedPiece = null;
                    repaint();

                    if (game != null) game.updateMoveCount();
                    if (isPuzzleSolved() && game != null) game.stopTimer();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (movedPiece != null) {
                    draggedPieceX = e.getX();
                    draggedPieceY = e.getY();
                    repaint();
                }
            }
        });
    }

    private void shuffleBoard() {
        do {
            Collections.shuffle(pieces);
        } while (!isShuffleValid(pieces));
    }

    private boolean isShuffleValid(ArrayList<PuzzlePiece> pieces) {
        int inversions = 0;
        for (int i = 0; i < pieces.size(); i++)
            for (int j = i + 1; j < pieces.size(); j++)
                if (pieces.get(i).getId() > pieces.get(j).getId()) inversions++;

        if (gridSize % 2 == 1) return inversions % 2 == 0;

        int emptyRowFromBottom = gridSize - emptyRow;
        return (inversions + emptyRowFromBottom) % 2 == 1;
    }

    private Point getBoardPosition(MouseEvent e) {
        return new Point(e.getY() / pieceHeight, e.getX() / pieceWidth);
    }

    private boolean isAdjacent(int row1, int col1, int row2, int col2) {
        return (Math.abs(row1 - row2) == 1 && col1 == col2) ||
               (Math.abs(col1 - col2) == 1 && row1 == row2);
    }

    private boolean isPuzzleSolved() {
        for (int i = 0; i < gridSize; i++)
            for (int j = 0; j < gridSize; j++)
                if (board[i][j] != null &&
                    (board[i][j].getpreviousRow() != i || board[i][j].getpreviousCol() != j || board[i][j].getDirection() != 0))
                    return false;
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (int row = 0; row < gridSize; row++)
            for (int col = 0; col < gridSize; col++) {
                PuzzlePiece piece = board[row][col];
                if (piece != null)
                    g2d.drawImage(piece.getImage(), col * pieceWidth, row * pieceHeight, pieceWidth, pieceHeight, null);
                else {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(col * pieceWidth, row * pieceHeight, pieceWidth, pieceHeight);
                }
            }

        if (movedPiece != null)
            g2d.drawImage(movedPiece.getImage(), draggedPieceX - pieceWidth / 2, draggedPieceY - pieceHeight / 2, pieceWidth, pieceHeight, null);
    }
}
