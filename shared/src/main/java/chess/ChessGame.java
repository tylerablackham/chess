package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor turn;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null){
            return null;
        }
        else {
            HashSet<ChessMove> vMoves = new HashSet<>();
            for (ChessMove move : piece.pieceMoves(board,startPosition)){
                ChessMove reversed = new ChessMove(move.getEndPosition(), move.getStartPosition(), null);
                ChessPiece captured = board.getPiece(move.getEndPosition());
                movePiece(move);
                if (!isInCheck(piece.getTeamColor())){
                    vMoves.add(move);
                }
                movePiece(reversed);
                board.addPiece(move.getEndPosition(), captured);
            }
            return vMoves;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(board.getPiece(move.getStartPosition()).getTeamColor() != turn){
            throw new InvalidMoveException("It is not this team's turn!");
        }
        if(validMoves(move.getStartPosition()).contains(move)){
            movePiece(move);
            if (move.getPromotionPiece() != null){
                board.getPiece(move.getEndPosition()).setPieceType(move.getPromotionPiece());
            }
            turn = turn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        } else {
            throw new InvalidMoveException("This is not a valid move!");
        }
    }

    private void movePiece(ChessMove move){
        ChessPiece piece = board.getPiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), piece);
        board.addPiece(move.getStartPosition(), null);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king = null;
        for (int r = 1; r < 9; r++){
            for (int c = 1; c < 9; c++){
                ChessPosition position = new ChessPosition(r,c);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING &&
                        piece.getTeamColor() == teamColor){
                    king = position;
                }
            }
        }
        for (int r = 1; r < 9; r++){
            for (int c = 1; c < 9; c++){
                ChessPosition position = new ChessPosition(r,c);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() != teamColor){
                    for (ChessMove move : piece.pieceMoves(board,position)){
                        if (move.getEndPosition().equals(king)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return (isInCheck(teamColor) && isInStalemate(teamColor));
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for (int r = 1; r < 9; r++){
            for (int c = 1; c < 9; c++){
                ChessPosition position = new ChessPosition(r,c);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor){
                    for (ChessMove move : piece.pieceMoves(board,position)){
                        ChessMove reverseLastMove = new ChessMove(move.getEndPosition(), move.getStartPosition(), null);
                        ChessPiece pieceTaken = board.getPiece(move.getEndPosition());
                        movePiece(move);
                        if (!isInCheck(teamColor)){
                            movePiece(reverseLastMove);
                            board.addPiece(reverseLastMove.getStartPosition(), pieceTaken);
                            return false;
                        }
                        movePiece(reverseLastMove);
                        board.addPiece(reverseLastMove.getStartPosition(), pieceTaken);
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    public boolean[][] getMoveMatrix(Collection<ChessMove> chessMoves) {
        boolean[][] moveMatrix = new boolean[8][8];
        for (ChessMove move: chessMoves){
            ChessPosition position = move.getEndPosition();
            moveMatrix[position.getRow()-1][position.getColumn()-1] = true;
        }
        return moveMatrix;
    }
}
