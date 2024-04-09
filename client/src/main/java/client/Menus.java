package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.*;
import ui.ChessBoardUI;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

public class Menus implements ServerMessageObserver {
    private String authToken;
    private boolean hasNotQuit;
    private boolean isFacingBlack;
    private boolean inGame;
    private int gameID;
    private ChessGame chessGame;
    Scanner scan;
    ServerFacade serverFacade;

    public static void main(String[] args) {
        Menus menus = new Menus();
        menus.showMenus();
    }

    public Menus() {
        authToken = "";
        hasNotQuit = true;
        inGame = false;
        scan = new Scanner(System.in);
        serverFacade = new ServerFacade(8080);
    }

    public void showMenus() {
        while (hasNotQuit) {
            if (Objects.equals(authToken, "")) {
                loggedOutMenu();
            }
            else {
                if (inGame) {
                    inGameMenu();
                }
                else {
                    loggedInMenu();
                }
            }
        }
    }

    private void loggedOutMenu() {
        System.out.println("""
                Welcome to 240 Chess. Type 'help' if you have any questions.
                
                You are currently not logged in. Here are the following actions you can take:
                 - Register: type 'register'
                 - Login: type 'login'
                 - Quit: type 'quit'
                 - Help: type 'help'
                """);
        String in = scan.nextLine().toLowerCase().trim();
        switch (in){
            case "register":
                register();
                break;
            case "login":
                login();
                break;
            case "quit":
                quit();
                break;
            case "help":
                helpPreLogin();
                break;
            default: System.out.println(in + " is not recognized. Please try again\n");
        }
    }

    private void loggedInMenu() {
        System.out.println("""
                Welcome to 240 Chess. Type 'help' if you have any questions.
                
                You are currently logged in. Here are the following actions you can take:
                 - Create Game: type 'create'
                 - List Games: type 'list'
                 - Join Game: type 'join'
                 - Observe: type 'observe'
                 - Logout: type 'logout'
                 - Quit: type 'quit'
                 - Help: type 'help'
                """);
        String in = scan.nextLine().toLowerCase().trim();
        switch (in){
            case "create":
                createGame();
                break;
            case "list":
                listGames();
                break;
            case "join":
                joinGame();
                break;
            case "observe":
                observe();
                break;
            case "logout":
                logout();
                break;
            case "quit":
                quit();
                break;
            case "help":
                helpPostLogin();
                break;
            default: System.out.println(in + " is not recognized. Please try again\n");
        }
    }

    private void inGameMenu() {
        System.out.println("""
                 You are currently in a game. Type 'help' for a list of commands:
                """);
        String in = scan.nextLine().toLowerCase().trim();
        switch (in){
            case "draw":
                draw();
                break;
            case "highlight":
                highlight();
                break;
            case "move":
                move();
                break;
            case "resign":
                resign();
                break;
            case "leave":
                leaveGame();
                break;
            case "help":
                helpInGame();
                break;
            default: System.out.println(in + " is not recognized. Please try again\n");
        }
    }

    private void register() {
        System.out.println("Please enter a username.");
        String username = scan.nextLine();
        System.out.println("Please enter a password.");
        String password = scan.nextLine();
        System.out.println("Please enter your email.");
        String email = scan.nextLine();
        UserData userData = new UserData(username, password, email);
        try {
            authToken = serverFacade.register(userData).authToken();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void login() {
        System.out.println("Please enter your username.");
        String username = scan.nextLine();
        System.out.println("Please enter your password.");
        String password = scan.nextLine();
        LoginRequest loginRequest = new LoginRequest(username, password);
        try {
            authToken = serverFacade.login(loginRequest).authToken();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createGame() {
        System.out.println("What would you like to name your game?");
        String gameName = scan.nextLine();
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);

        try {
            CreateGameResponse response = serverFacade.createGame(createGameRequest);
            System.out.println("Created game: " + gameName + "\n\twith game ID: " + response.gameID());

        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listGames() {
        AuthToken authToken1 = new AuthToken(authToken);

        try {
            GameList gameList = serverFacade.listGames(authToken1);
            System.out.println("The existing games are as follows (Game Name - Game ID):\n");
            for (GameData game : gameList.games()) {
                System.out.println(game.gameName() + " - " + game.gameID());
            }
            System.out.println("\n");
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void joinGame() {
        System.out.println("Enter the ID number for the game you would like to join.");
        int gameID = Integer.parseInt(scan.nextLine().trim());
        System.out.println("What team will you be on? (White or Black)");
        String color = scan.nextLine().trim().toLowerCase();
        if (color.equals("black") || color.equals("white")){
            ChessGame.TeamColor playerColor = color.equals("black") ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
            JoinGameRequest joinGameRequest =  new JoinGameRequest(authToken, gameID, playerColor);
            try {
                serverFacade.addObserver(this);
                serverFacade.joinGame(joinGameRequest);
                serverFacade.joinPlayer(joinGameRequest);
                this.isFacingBlack = playerColor == ChessGame.TeamColor.BLACK;
                inGame = true;
                chessGame = new ChessGame();
                this.gameID = gameID;
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            System.out.println("Unrecognized team color. Please try again.");
            joinGame();
        }
    }

    private void observe() {
        System.out.println("Enter the ID number for the game you would like to observe.");
        int gameID = Integer.parseInt(scan.nextLine().trim());
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, gameID, null);
        try {
            serverFacade.addObserver(this);
            serverFacade.joinGame(joinGameRequest);
            serverFacade.joinObserver(joinGameRequest);
            this.isFacingBlack = false;
            inGame = true;
            this.gameID = gameID;
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void logout() {
        try {
            serverFacade.logout(new AuthToken(authToken));
            authToken = "";
            inGame = false;
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void draw() {
        ChessBoardUI.draw(chessGame.getBoard(), isFacingBlack, null, null);
    }

    private void highlight() {
        System.out.println("Enter the column and row of the piece you would like to highlight, like so: A1.");
        String coordinate = scan.nextLine().trim().toUpperCase();
        int col = coordinate.charAt(0) - 'A' + 1;
        int row = Integer.parseInt(coordinate.substring(1,2));
        ChessPosition position = new ChessPosition(row, col);
        Collection<ChessMove> chessMoves = chessGame.validMoves(position);
        boolean [][] moveMatrix = chessGame.getMoveMatrix(chessMoves);
        ChessBoardUI.draw(chessGame.getBoard(), isFacingBlack, moveMatrix, position);
    }

    private void move() {
        System.out.println("Enter the column and row of the piece you would like to move, like so: A1.");
        String coordinate = scan.nextLine().trim().toUpperCase();
        int col = coordinate.charAt(0) - 'A' + 1;
        int row = Integer.parseInt(coordinate.substring(1,2));
        ChessPosition start = new ChessPosition(row, col);
        System.out.println("Enter the column and row where you would like to move to, like so: A1.");
        coordinate = scan.nextLine().trim().toUpperCase();
        col = coordinate.charAt(0) - 'A' + 1;
        row = Integer.parseInt(coordinate.substring(1,2));
        ChessPosition end = new ChessPosition(row, col);
        System.out.println("If promoting a pawn, enter the piece you would like to promote. You cannot choose pawn or king. " +
                "If not promoting, hit enter.");
        String piece = scan.nextLine().trim().toUpperCase();
        ChessPiece.PieceType pieceType = new Gson().fromJson(piece, ChessPiece.PieceType.class);
        try {
            serverFacade.makeMove(new AuthToken(authToken), gameID, new ChessMove(start, end, pieceType));
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void resign() {
        System.out.println("Are you sure you want to resign? Your opponent will win. (Y/N)");
        String answer = scan.nextLine().trim().toUpperCase();
        if (answer.equals("Y") || answer.equals("YES")) {
            try {
                serverFacade.resign(new AuthToken(authToken), gameID);
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void leaveGame() {
        try {
            serverFacade.leave(new AuthToken(authToken), gameID);
            inGame = false;
            gameID = 0;
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void quit() {
        hasNotQuit = false;
    }

    private void helpPreLogin() {
        System.out.println("""
                Register: Type 'register' to play, if you haven't registered yet.
                Login: Type 'login' to play, if you have already registered.
                Quit: Type 'quit' to stop running the program.
                
                Press enter to continue.
                """);
        scan.nextLine();
    }

    private void helpPostLogin() {
        System.out.println("""
                Create Game: Type 'create' to make a new game.
                List Games: Type 'list' to list all existing games.
                Join Game: Type 'join' to join and play in an existing game.
                Observe: Type 'observe' to watch a game being played.
                Logout: Type 'logout' to logout of your current session.
                Quit: Type 'quit' to stop running the program.
                
                Press enter to continue.
                """);
        scan.nextLine();
    }

    private void helpInGame() {
        System.out.println("""
                Here are the options of what you can do during the game
                
                Redraw Chess Board: Type 'draw' to redraw the chess board.
                Highlight Legal Moves: Type 'highlight' to highlight the legal moves of a piece
                Make Move: Type 'move' to make a move. (Players only)
                Resign: Type 'resign' to forfeit the game. (Players only)
                Leave: Type 'leave' to leave the game.
                
                Press enter to continue.
                """);
        scan.nextLine();
    }

    public void notify(ServerMessage message, String json) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> {
                Notification notification = new Gson().fromJson(json, Notification.class);
                System.out.println(notification.getMessage());
            }
            case ERROR -> {
                Error error = new Gson().fromJson(json, Error.class);
                System.out.println(error.getErrorMessage());
            }
            case LOAD_GAME -> {
                LoadGame loadGame = new Gson().fromJson(json, LoadGame.class);
                System.out.println();
                chessGame = loadGame.getGame();
                ChessBoardUI.draw(chessGame.getBoard(), isFacingBlack, null, null);
            }
        }
    }

}
