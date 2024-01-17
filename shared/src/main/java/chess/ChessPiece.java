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
        return switch (pieceType) {
            case KING -> kingMoves(board);
            case QUEEN -> queenMoves(board);
            case BISHOP -> bishopMoves(board);
            case KNIGHT -> knightMoves(board);
            case ROOK -> rookMoves(board);
            case PAWN -> pawnMoves(board);
        };

    }

    private Collection<ChessMove> bishopMoves(ChessBoard board) {
        return new ArrayList<>();
    }

    private Collection<ChessMove> kingMoves(ChessBoard board) {
        return new ArrayList<>();
    }

    private Collection<ChessMove> knightMoves(ChessBoard board) {
        return new ArrayList<>();
    }
    private Collection<ChessMove> pawnMoves(ChessBoard board) {
        return new ArrayList<>();
    }
    private Collection<ChessMove> queenMoves(ChessBoard board) {
        return new ArrayList<>();
    }
    private Collection<ChessMove> rookMoves(ChessBoard board) {
        return new ArrayList<>();
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
