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

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

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

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor){
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for (int r = -1; r <= 1; r++){
            for (int c = -1; c <= 1; c++){
                if ((r != 0 || c != 0) && row + r <= 8 && row + r >= 1 && col + c <= 8 && col + c >= 1){
                    ChessPosition position = new ChessPosition(row + r, col + c);
                    if (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != myColor){
                        moves.add(new ChessMove(myPosition, position, null));
                    }
                }
            }
        }
        return moves;
    }

    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor){
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for (int r = -1; r <= 1; r++){
            for (int c = -1; c <= 1; c++){
                boolean canMove = true;
                if (r != 0 || c != 0){
                    for (int i = 1; i <= 8; i++){
                        if(canMove && row + r*i <= 8 && row + r*i >= 1 && col + c*i <= 8 && col + c*i >= 1){
                            ChessPosition position = new ChessPosition(row + r*i, col + c*i);
                            if(board.getPiece(position) == null || board.getPiece(position).getTeamColor() != myColor){
                                moves.add(new ChessMove(myPosition, position, null));
                                if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != myColor){
                                    canMove = false;
                                }
                            } else {
                                canMove = false;
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor){
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for (int r = -1; r <= 1; r++){
            for (int c = -1; c <= 1; c++){
                boolean canMove = true;
                if (r != 0 && c != 0){
                    for (int i = 1; i <= 8; i++){
                        if(canMove && row + r*i <= 8 && row + r*i >= 1 && col + c*i <= 8 && col + c*i >= 1){
                            ChessPosition position = new ChessPosition(row + r*i, col + c*i);
                            if(board.getPiece(position) == null || board.getPiece(position).getTeamColor() != myColor){
                                moves.add(new ChessMove(myPosition, position, null));
                                if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != myColor){
                                    canMove = false;
                                }
                            } else {
                                canMove = false;
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor){
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for (int r = -1; r <= 1; r++){
            for (int c = -1; c <= 1; c++){
                if ((r != 0 && c != 0) && row + r <= 8 && row + r >= 1 && col + c*2 <= 8 && col + c*2 >= 1){
                    ChessPosition position = new ChessPosition(row + r, col + c*2);
                    if (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != myColor){
                        moves.add(new ChessMove(myPosition, position, null));
                    }
                }
                if ((r != 0 && c != 0) && row + r*2 <= 8 && row + r*2 >= 1 && col + c <= 8 && col + c >= 1){
                    ChessPosition position = new ChessPosition(row + r*2, col + c);
                    if (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != myColor){
                        moves.add(new ChessMove(myPosition, position, null));
                    }
                }
            }
        }
        return moves;
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor){
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for (int r = -1; r <= 1; r++){
            for (int c = -1; c <= 1; c++){
                boolean canMove = true;
                if (r == 0 || c == 0){
                    for (int i = 1; i <= 8; i++){
                        if(canMove && row + r*i <= 8 && row + r*i >= 1 && col + c*i <= 8 && col + c*i >= 1){
                            ChessPosition position = new ChessPosition(row + r*i, col + c*i);
                            if(board.getPiece(position) == null || board.getPiece(position).getTeamColor() != myColor){
                                moves.add(new ChessMove(myPosition, position, null));
                                if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != myColor){
                                    canMove = false;
                                }
                            } else {
                                canMove = false;
                            }
                        }
                    }
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
        if (col != 8 && board.getPiece(new ChessPosition(row + direction, col + 1)) != null &&
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
        if (col != 1 && board.getPiece(new ChessPosition(row + direction, col - 1)) != null &&
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
