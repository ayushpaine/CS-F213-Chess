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

    // mutable Builder objects
    // used to build immutable BoardController objects
    private static class Builder {
        private Square[][] board;
        // private Player whitePlayer, blackPlayer;
        private String currentPlayer;

        private static final String[] HOME_RANK = { "rook", "knight", "bishop", "queen", "king", "bishop", "knight",
                "rook" };

        // to create a fresh instance of Builder
        private Builder() {
            this.board = new Square[8][8];

            for (int file = 0; file < 8; file++) {
                Piece whitePeice = Piece.createPiece(0, file, "WHITE", HOME_RANK[file]);
                this.board[0][file] = Square.createSquare(0, file, whitePeice);

                Piece whitePawn = Piece.createPiece(1, file, "WHITE", "pawn");
                this.board[1][file] = Square.createSquare(1, file, whitePawn);

                Piece blackPeice = Piece.createPiece(7, file, "BLACK", HOME_RANK[7 - file]);
                this.board[7][file] = Square.createSquare(7, file, blackPeice);

                Piece blackPawn = Piece.createPiece(6, file, "BLACK", "pawn");
                this.board[6][file] = Square.createSquare(6, file, blackPawn);
            }

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
