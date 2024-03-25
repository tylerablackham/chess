package client;

import chess.ChessBoard;
import chess.ChessGame;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import model.*;
import server.Server;
import ui.ChessBoardUI;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class Menus {
    private String authToken;
    private boolean hasNotQuit;
    Scanner scan;
    ServerFacade serverFacade;
    Server server;
    public static void main(String[] args){
        Menus menus = new Menus();
        menus.showMenus();
    }

    public Menus() {
        authToken = "";
        hasNotQuit = true;
        scan = new Scanner(System.in);
        server = new Server(new SQLUserDAO(), new SQLGameDAO(), new SQLAuthDAO());
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(port);
    }

    public void showMenus() {
        while (hasNotQuit) {
            if (Objects.equals(authToken, "")) {
                loggedOutMenu();
            }
            else {
                loggedInMenu();
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
                serverFacade.joinGame(joinGameRequest);
                ChessBoard chessBoard = new ChessBoard();
                chessBoard.resetBoard();
                System.out.println();
                ChessBoardUI.draw(chessBoard, (playerColor ==  ChessGame.TeamColor.BLACK), null, null);
                System.out.println();
            } catch(IOException e) {
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
            serverFacade.joinGame(joinGameRequest);
            ChessBoard chessBoard = new ChessBoard();
            chessBoard.resetBoard();
            ChessBoardUI.draw(chessBoard, true, null, null);
            System.out.println();
            ChessBoardUI.draw(chessBoard, false, null, null);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void logout() {
        try {
            serverFacade.logout(new AuthToken(authToken));
            authToken = "";
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void quit() {
        hasNotQuit = false;
        server.stop();
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
}
