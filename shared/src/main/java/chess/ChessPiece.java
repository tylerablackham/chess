package chess;

import java.util.Collection;
import java.util.HashSet;

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
        HashSet<ChessMove> moves = new HashSet<>();

        return moves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        HashSet<ChessMove> moves = new HashSet<>();
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
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        //Forward 2
        if (row < 7) {
            //Right 1
            if (col < 8){
                if(board.getPiece(new ChessPosition(row + 2, col + 1)) == null ||
                        board.getPiece(new ChessPosition(row + 2, col + 1)).color != myColor){
                    moves.add(new ChessMove(myPosition,new ChessPosition(row + 2, col + 1), null));
                }
            }
            //Left 1
            if (col > 1){
                if(board.getPiece(new ChessPosition(row + 2, col - 1)) == null ||
                        board.getPiece(new ChessPosition(row + 2, col - 1)).color != myColor){
                    moves.add(new ChessMove(myPosition,new ChessPosition(row + 2, col - 1), null));
                }
            }
        }
        //Forward 1
        if (row < 8) {
            //Right 2
            if (col < 7){
                if(board.getPiece(new ChessPosition(row + 1, col + 2)) == null ||
                        board.getPiece(new ChessPosition(row + 1, col + 2)).color != myColor){
                    moves.add(new ChessMove(myPosition,new ChessPosition(row + 1, col + 2), null));
                }
            }
            //Left 2
            if (col > 2){
                if(board.getPiece(new ChessPosition(row + 1, col - 2)) == null ||
                        board.getPiece(new ChessPosition(row + 1, col - 2)).color != myColor){
                    moves.add(new ChessMove(myPosition,new ChessPosition(row + 1, col - 2), null));
                }
            }
        }
        //Back 2
        if (row > 2) {
            //Right 1
            if (col < 8){
                if(board.getPiece(new ChessPosition(row - 2, col + 1)) == null ||
                        board.getPiece(new ChessPosition(row - 2, col + 1)).color != myColor){
                    moves.add(new ChessMove(myPosition,new ChessPosition(row - 2, col + 1), null));
                }
            }
            //Left 1
            if (col > 1){
                if(board.getPiece(new ChessPosition(row - 2, col - 1)) == null ||
                        board.getPiece(new ChessPosition(row - 2, col - 1)).color != myColor){
                    moves.add(new ChessMove(myPosition,new ChessPosition(row - 2, col - 1), null));
                }
            }
        }
        //Back 1
        if (row > 1) {
            //Right 2
            if (col < 7){
                if(board.getPiece(new ChessPosition(row - 1, col + 2)) == null ||
                        board.getPiece(new ChessPosition(row - 1, col + 2)).color != myColor){
                    moves.add(new ChessMove(myPosition,new ChessPosition(row - 1, col + 2), null));
                }
            }
            //Left 2
            if (col > 2){
                if(board.getPiece(new ChessPosition(row - 1, col - 2)) == null ||
                        board.getPiece(new ChessPosition(row - 1, col - 2)).color != myColor){
                    moves.add(new ChessMove(myPosition,new ChessPosition(row - 1, col - 2), null));
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        PieceType [] promotions = {PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK, PieceType.QUEEN};
        boolean firstMove = (row == 7 && board.getPiece(myPosition).color == ChessGame.TeamColor.BLACK) ||
                (row == 2 && board.getPiece(myPosition).color == ChessGame.TeamColor.WHITE);
        boolean madeIt = (row - 1 == 1 && board.getPiece(myPosition).color == ChessGame.TeamColor.BLACK) ||
                (row + 1 == 8 && board.getPiece(myPosition).color == ChessGame.TeamColor.WHITE);
        int direction = board.getPiece(myPosition).color == ChessGame.TeamColor.BLACK ? -1 : 1;
        if (board.getPiece(new ChessPosition(row + direction, col)) == null){
            if (madeIt) {
                for (PieceType type : promotions) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(row + direction, col), type));
                }
            }
            else {
                moves.add(new ChessMove(myPosition,new ChessPosition(row + direction, col), null));
            }
            if(firstMove && (board.getPiece(new ChessPosition(row + (direction * 2), col)) == null)){
                moves.add(new ChessMove(myPosition,new ChessPosition(row + (direction * 2), col), null));
            }
        }
        if (board.getPiece(new ChessPosition(row + direction, col + 1)) != null &&
                board.getPiece(new ChessPosition(row + direction, col + 1)).color != myColor){
            if (madeIt) {
                for (PieceType type : promotions) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(row + direction, col + 1), type));
                }
            }
            else {
                moves.add(new ChessMove(myPosition,new ChessPosition(row + direction, col + 1), null));
            }
        }
        if (board.getPiece(new ChessPosition(row + direction, col - 1)) != null &&
                board.getPiece(new ChessPosition(row + direction, col - 1)).color != myColor){
            if (madeIt) {
                for (PieceType type : promotions) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(row + direction, col - 1), type));
                }
            }
            else {
                moves.add(new ChessMove(myPosition,new ChessPosition(row + direction, col - 1), null));
            }
        }

        return moves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        HashSet<ChessMove> moves = new HashSet<>();

        return moves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        HashSet<ChessMove> moves = new HashSet<>();

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
