package model;

import chess.ChessGame;

public record JoinGameRequest(String authToken, int gameID, ChessGame.TeamColor clientColor) {
}
