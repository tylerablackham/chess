package ui;

import chess.ChessBoard;
import chess.ChessPiece;

public class ChessBoardUI {
    ChessPiece[][] board;
    Boolean isFacingWhite;

    public static void main(String[] args){
        ChessBoardUI chessBoardUI = new ChessBoardUI(new ChessBoard().getBoard(), true);
        chessBoardUI.drawBoard();
    }

    public ChessBoardUI(ChessPiece[][] board, Boolean isFacingWhite) {
        this.board = board;
        this.isFacingWhite = isFacingWhite;
    }

    public void drawBoard() {

    }

    private void drawHeaderFooter() {

    }

    private void drawRow() {

    }

    private void drawCell() {

    }

    private String returnStringRepresentation(ChessPiece piece) {

    }

}
