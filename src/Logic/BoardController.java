package Logic;

import Data.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class BoardController {
    private Board board;
    private int scale = 50;
    private final int size = 12;

    public BoardController(Color lightColor, Color darkColor) throws IOException {
        board = new Board(lightColor, darkColor, 12);
        setUpBoard();
    }

    public Board getBoard() {
        return board;
    }

    public static Piece pieceFactory(PieceType pieceType, Team team, int x, int y) {
        switch (pieceType) {
            case CORVETTE -> {
                return new Corvette(team, new int[]{x, y});
            }
            case FRIGATE -> {
                return new Frigate(team, new int[]{x, y});
            }
            case DESTROYER -> {
                return new Destroyer(team, new int[]{x, y});
            }
            case CRUISER -> {
                return new Cruiser(team, new int[]{x, y});
            }
            case BATTLESHIP -> {
                return new Battleship(team, new int[]{x, y});
            }
            case CARRIER -> {
                return new Carrier(team, new int[]{x, y});
            }
        }
        return null;
    }

    public ArrayList<Tile> getPiecesOnBoard(Team team) {
        ArrayList<Tile> pieces = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Piece piece = board.getTile(i, j).getPiece();
                if (piece != null) {
                    if (piece.getTeam() == team) {
                        pieces.add(board.getTile(i, j));
                        //System.out.println(piece.getType());
                    }
                }
            }
        }
        return pieces;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setUpBoard() {
        board.setTile(2, 1, pieceFactory(PieceType.FRIGATE, Team.BLACK, 2, 1));
        board.setTile(3, 1, pieceFactory(PieceType.CORVETTE, Team.BLACK, 3, 1));
        board.setTile(4, 1, pieceFactory(PieceType.CORVETTE, Team.BLACK, 4, 1));
        board.setTile(5, 1, pieceFactory(PieceType.CORVETTE, Team.BLACK, 5, 1));
        board.setTile(6, 1, pieceFactory(PieceType.CORVETTE, Team.BLACK, 6, 1));
        board.setTile(7, 1, pieceFactory(PieceType.CORVETTE, Team.BLACK, 7, 1));
        board.setTile(8, 1, pieceFactory(PieceType.CORVETTE, Team.BLACK, 8, 1));
        board.setTile(9, 1, pieceFactory(PieceType.FRIGATE, Team.BLACK, 9, 1));
        board.setTile(2, 0, pieceFactory(PieceType.FRIGATE, Team.BLACK, 2, 0));
        board.setTile(3, 0, pieceFactory(PieceType.CRUISER, Team.BLACK, 3, 0));
        board.setTile(4, 0, pieceFactory(PieceType.DESTROYER, Team.BLACK, 4, 0));
        board.setTile(5, 0, pieceFactory(PieceType.BATTLESHIP, Team.BLACK, 5, 0));
        board.setTile(6, 0, pieceFactory(PieceType.CARRIER, Team.BLACK, 6, 0));
        board.setTile(7, 0, pieceFactory(PieceType.DESTROYER, Team.BLACK, 7, 0));
        board.setTile(8, 0, pieceFactory(PieceType.CRUISER, Team.BLACK, 8, 0));
        board.setTile(9, 0, pieceFactory(PieceType.FRIGATE, Team.BLACK, 9, 0));


        board.setTile(2, 10, pieceFactory(PieceType.FRIGATE, Team.WHITE, 2, 10));
        board.setTile(3, 10, pieceFactory(PieceType.CORVETTE, Team.WHITE, 3, 10));
        board.setTile(4, 10, pieceFactory(PieceType.CORVETTE, Team.WHITE, 4, 10));
        board.setTile(5, 10, pieceFactory(PieceType.CORVETTE, Team.WHITE, 5, 10));
        board.setTile(6, 10, pieceFactory(PieceType.CORVETTE, Team.WHITE, 6, 10));
        board.setTile(7, 10, pieceFactory(PieceType.CORVETTE, Team.WHITE, 7, 10));
        board.setTile(8, 10, pieceFactory(PieceType.CORVETTE, Team.WHITE, 8, 10));
        board.setTile(9, 10, pieceFactory(PieceType.FRIGATE, Team.WHITE, 9, 10));
        board.setTile(2, 11, pieceFactory(PieceType.FRIGATE, Team.WHITE, 2, 11));
        board.setTile(3, 11, pieceFactory(PieceType.CRUISER, Team.WHITE, 3, 11));
        board.setTile(4, 11, pieceFactory(PieceType.DESTROYER, Team.WHITE, 4, 11));
        board.setTile(5, 11, pieceFactory(PieceType.BATTLESHIP, Team.WHITE, 5, 11));
        board.setTile(6, 11, pieceFactory(PieceType.CARRIER, Team.WHITE, 6, 11));
        board.setTile(7, 11, pieceFactory(PieceType.DESTROYER, Team.WHITE, 7, 11));
        board.setTile(8, 11, pieceFactory(PieceType.CRUISER, Team.WHITE, 8, 11));
        board.setTile(9, 11, pieceFactory(PieceType.FRIGATE, Team.WHITE, 9, 11));

    }



    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getSize() {
        return size;
    }

}
