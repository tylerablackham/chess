package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board; //ChessPiece [row][col]

    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

//          1 2 3 4 5 6 7 8
//       8 |r|n|b|q|k|b|n|r| Black
//       7 |p|p|p|p|p|p|p|p| Black
//       6 | | | | | | | | |
//       5 | | | | | | | | |
//       4 | | | | | | | | |
//       3 | | | | | | | | |
//       2 |P|P|P|P|P|P|P|P| White
//       1 |R|N|B|Q|K|B|N|R| White

        for (int i = 1; i <= 8; i++){
            board[7-1][i-1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            board[2-1][i-1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            if (i == 1 || i == 8){
                board[8-1][i-1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
                board[1-1][i-1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
            }
            if (i == 2 || i == 7){
                board[8-1][i-1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
                board[1-1][i-1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
            }
            if (i == 3 || i == 6){
                board[8-1][i-1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
                board[1-1][i-1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
            }
            if (i == 4){
                board[8-1][i-1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
                board[1-1][i-1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
            }
            if (i == 5){
                board[8-1][i-1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
                board[1-1][i-1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChessBoard that = (ChessBoard) o;

        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
