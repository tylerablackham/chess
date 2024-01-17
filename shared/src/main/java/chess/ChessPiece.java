package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private PieceType pieceType;
    private ChessGame.TeamColor color;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        pieceType = type;
        color = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessGame.TeamColor myColor = board.getPiece(myPosition).color;
        return switch (pieceType) {
            case KING -> kingMoves(board, myPosition, myColor);
            case QUEEN -> queenMoves(board, myPosition, myColor);
            case BISHOP -> bishopMoves(board, myPosition, myColor);
            case KNIGHT -> knightMoves(board, myPosition, myColor);
            case ROOK -> rookMoves(board, myPosition, myColor);
            case PAWN -> pawnMoves(board, myPosition, myColor);
        };
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        return moves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        boolean forward = (row < 8);
        boolean back = (row > 1);
        boolean left = (column > 1);
        boolean right = (column < 8);
        //Forward
        if (forward && (board.getPiece(new ChessPosition(row + 1, column)) == null ||
                board.getPiece(new ChessPosition(row + 1, column)).color != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column),null));
        }
        //Back
        if (back && (board.getPiece(new ChessPosition(row - 1, column)) == null ||
                board.getPiece(new ChessPosition(row - 1, column)).color != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column),null));
        }
        //Left
        if (left && (board.getPiece(new ChessPosition(row, column - 1)) == null ||
                board.getPiece(new ChessPosition(row, column - 1)).color != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row, column - 1),null));
        }
        //Right
        if (right && (board.getPiece(new ChessPosition(row, column + 1)) == null ||
                board.getPiece(new ChessPosition(row, column + 1)).color != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row, column + 1),null));
        }
        //Forward-Left
        if ((forward && left) && (board.getPiece(new ChessPosition(row + 1, column - 1)) == null ||
                board.getPiece(new ChessPosition(row + 1, column - 1)).color != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column - 1),null));
        }
        //Forward-Right
        if ((forward && right) && (board.getPiece(new ChessPosition(row + 1, column + 1)) == null ||
                board.getPiece(new ChessPosition(row + 1, column + 1)).color != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column + 1),null));
        }
        //Back-Left
        if ((back && left) && (board.getPiece(new ChessPosition(row - 1, column - 1)) == null ||
                board.getPiece(new ChessPosition(row - 1, column - 1)).color != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column - 1),null));
        }
        //Back-Right
        if ((back && right) && (board.getPiece(new ChessPosition(row - 1, column + 1)) == null ||
                board.getPiece(new ChessPosition(row - 1, column + 1)).color != myColor)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column + 1),null));
        }
        return moves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        return moves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        return moves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        return moves;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChessPiece that = (ChessPiece) o;

        if (pieceType != that.pieceType) return false;
        return color == that.color;
    }

    @Override
    public int hashCode() {
        int result = pieceType != null ? pieceType.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }
}
