package ui;

import chess.ChessBoard;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class ChessBoardUI {
    ChessPiece[][] board;
    Boolean isFacingBlack;
    PrintStream out;

    public static void main(String[] args){
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        draw(chessBoard, true);
        System.out.println();
        draw(chessBoard, false);
    }

    public static void draw(ChessBoard chessBoard, Boolean isFacingBlack){
        ChessBoardUI chessBoardUI = new ChessBoardUI(chessBoard.getBoard(), isFacingBlack);
        chessBoardUI.drawBoard();
    }

    public ChessBoardUI(ChessPiece[][] board, Boolean isFacingBlack) {
        this.board = board;
        this.isFacingBlack = isFacingBlack;
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    public void drawBoard() {
        drawHeaderFooter();
        if (isFacingBlack) {
            for (int i = board.length - 1; i >= 0; i--){
                drawRow(board[7-i], i);
            }
        }
        else {
            for (int i = 0; i < board.length; i++) {
                drawRow(board[7-i], i);
            }
        }
        drawHeaderFooter();
    }

    private void drawHeaderFooter() {
        String[] letters = !isFacingBlack ? new String[]{"a", "b", "c", "d", "e", "f", "g", "h"} :
                new String[]{"h", "g", "f", "e", "d", "c", "b", "a"};
        out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        out.print("   ");
        for (String letter : letters){
            out.print(" " + letter + " ");
        }
        out.print("   ");
        out.println("\u001B[0m"); //reset background color
    }

    private void drawRow(ChessPiece[] row, int rowNum) {
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
        out.print(" " + (8 - rowNum) + " ");
        if (isFacingBlack) {
            for (int i = row.length - 1; i >= 0; i--){
                drawCell(row[i], rowNum, i);
            }
        }
        else {
            for (int i = 0; i < row.length; i++) {
                drawCell(row[i], rowNum, i);
            }
        }
        out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        out.print(" " + (8 - rowNum) + " ");
        out.println("\u001B[0m"); //reset background color
    }

    private void drawCell(ChessPiece piece, int rowNum, int colNum) {
        if (rowNum % 2 == colNum % 2) {
            out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        }
        else {
            out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        }
        if (piece != null) {
            out.print(switch (piece.getTeamColor()) {
                case WHITE -> EscapeSequences.SET_TEXT_COLOR_WHITE;
                case BLACK -> EscapeSequences.SET_TEXT_COLOR_BLACK;
            });
        }
        out.print(returnStringRepresentation(piece));
    }

    private String returnStringRepresentation(ChessPiece piece) {
        if (piece == null) {
            return "   ";
        }
        return switch (piece.getPieceType()){
            case KING -> " K ";
            case QUEEN -> " Q ";
            case BISHOP -> " B ";
            case KNIGHT -> " N ";
            case ROOK -> " R ";
            case PAWN -> " P ";
        };
    }

}
