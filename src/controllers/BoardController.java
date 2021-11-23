package controllers;

import models.Move;
import models.Piece;
import models.Square;

public final class BoardController {

    private final Square[][] board;
    // private final Player whitePlayer, blackPlayer;
    private final String currentPlayer;

    // receives Builder object
    // and shallow copies it into a BoardController object
    private BoardController(Builder builder) {
        this.board = builder.board;
        // this.whitePlayer = builder.whitePlayer;
        // this.blackPlayer = builder.blackPlayer;
        this.currentPlayer = builder.currentPlayer;
    }

    // getters
    public Square[][] getBoard() {
        return this.board;
    }

    // public Player getWhitePlayer() {
    // return this.whitePlayer;
    // }

    // public Player getBlackPlayer() {
    // return this.blackPlayer;
    // }

    public String getCurrentPlayer() {
        return this.currentPlayer;
    }

    // to create/initialize a fresh instance of BoardController
    public static BoardController initialize() {
        Builder builder = new Builder();
        return builder.build();
    }

    public BoardController executeMove(Move move) {
        Builder builder = new Builder(this);
        return builder.movePeice(move).togglePlayer().build();
    }

    // To create an instance of BoardController from FEN String
    // TODO check for errors in FEN String
    public static BoardController createBoardFromFEN(String FEN) {
        String[] fields = FEN.split(" ");
        String piece_placement = fields[0];
        String active_color = fields[1];
        // String castling = fields[2];
        // String enPassant = fields[3];
        // String half_moves = fields[4];
        // String full_moves = fields[5];

        Builder builder = new Builder();

        // For piece placement
        int rank = 7;
        int file = 0;
        for (String row : piece_placement.split("/")) {
            for (char c : row.toCharArray()) {
                if (Character.isAlphabetic(c)) {
                    String color = (Character.isUpperCase(c)) ? "WHITE" : "BLACK";
                    Piece piece = Piece.createPiece(rank, file++, color, String.valueOf(c));
                    builder.setPiece(piece);
                } else if (Character.isDigit(c))
                    file += Integer.parseInt(String.valueOf(c));
                else {
                    // TODO Error handling
                }
            }
            rank--;
        }

        // For active color
        String currentPlayerColor = (active_color.equals("w")) ? "WHITE" : "BLACK";
        builder.setCurrentPlayer(currentPlayerColor);

        return builder.build();
    }

    // mutable Builder objects
    // used to build immutable BoardController objects
    private static class Builder {
        private Square[][] board;
        // private Player whitePlayer, blackPlayer;
        private String currentPlayer;

        // to create a fresh instance of Builder
        private Builder() {
            this.board = new Square[8][8];
            // this.whitePlayer =
            // this.blackPlayer =
            this.currentPlayer = "WHITE";
        }

        // receives BoardController object
        // and shallow copies it into a Builder object
        private Builder(BoardController prevBoard) {
            this.board = prevBoard.board;
            // this.whitePlayer = prevBoard.whitePlayer;
            // this.blackPlayer = prevBoard.blackPlayer;
            this.currentPlayer = prevBoard.currentPlayer;
        }

        // Adds the given piece to the board
        private Builder setPiece(Piece piece) {
            if (piece == null)
                throw new Error("BoardController.Builder.setPiece(Piece piece) cannot be passed null as a value");

            int rank = piece.getRank();
            int file = piece.getFile();
            this.board[rank][file] = Square.createSquare(rank, file, piece);
            return this;
        }

        // Removes the given piece from the board
        // private Builder removePiece(Piece piece) {
        // if (piece == null)
        // throw new Error("BoardController.Builder.removePiece(Piece piece) cannot be
        // passed null as a value");

        // int rank = piece.getRank();
        // int file = piece.getFile();
        // this.board[rank][file] = Square.createSquare(rank, file, null);
        // return this;
        // }

        // Sets the current player of the board
        private Builder setCurrentPlayer(String color) {
            if (color.equalsIgnoreCase("WHITE"))
                this.currentPlayer = "WHITE";
            else if (color.equalsIgnoreCase("BLACK"))
                this.currentPlayer = "BLACK";
            else
                throw new Error("Invalid player color: " + color);
            return this;
        }

        // updates position of a peice on board, using a Move object
        private Builder movePeice(Move move) {
            Square startSquare = move.getStartSquare();
            int startRank = startSquare.getRank();
            int startFile = startSquare.getFile();

            Square endSquare = move.getEndSquare();
            int endRank = endSquare.getRank();
            int endFile = endSquare.getFile();

            Piece peiceToMove = startSquare.getPiece();

            this.board[startRank][startFile] = new Square.EmptySquare(startRank, startFile);
            this.board[endRank][endFile] = new Square.OccupiedSquare(endRank, endFile, peiceToMove);

            return this;
        }

        // to change the current playing player.
        // throws error if player is invalid.
        private Builder togglePlayer() throws Error {
            if (this.currentPlayer.equals("WHITE")) {
                this.currentPlayer = "BLACK";
            } else if (this.currentPlayer.equals("BLACK")) {
                this.currentPlayer = "WHITE";
            } else {
                throw new Error("Invalid currentPlayer: \"" + this.currentPlayer + "\"");
            }

            return this;
        }

        // return immutable BoardController object
        // by making a shallow copy of the Builder object
        private BoardController build() {
            return new BoardController(this);
        }
    }
}
