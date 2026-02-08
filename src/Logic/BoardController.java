package Logic;

import Data.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class BoardController {
    private Board board;
    private int scale = 50;
    private final int size = 12;

    public PieceList whiteTeam;

    public PieceList blackTeam;

    public BoardController(Color lightColor, Color darkColor) throws IOException {
        board = new Board(lightColor, darkColor, 12);
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

    public Tile getTile(int[] location) {
        return board.tiles[location[1] * size + location[0]];
    }

    public void setTile(int[] location, Piece piece) {
        board.tiles[location[1] * size + location[0]].setPiece(piece);
    }

    public TileList getPiecesOnBoard(Team team) {
        TileList pieces = new TileList();
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

    public void setUpBoard(ArrayList<int[]> pieceData, Team team) {
        PieceList pieces = new PieceList();
        for (int[] data : pieceData) {
            Piece piece = pieceFactory(PieceType.values()[data[0]], team, data[1], data[2]);
            board.setTile(data[1], data[2], piece);
            pieces.add(piece);
        }
        if (team == Team.BLACK) {
            blackTeam = pieces;
        }
        else {
            whiteTeam = pieces;
        }
    }

    public void removePiece(Piece piece) {
        if (piece.getTeam() == Team.BLACK) {
            blackTeam.remove(piece);
        }
        else {
            whiteTeam.remove(piece);
        }
        board.setTile(piece.getLocation()[0], piece.getLocation()[1], null);
    }

    public PieceList getTeam(Team team) {
        if (team == Team.BLACK) {
            return blackTeam;
        }
        return whiteTeam;
    }

    public PieceList getWhiteTeam() {
        return whiteTeam;
    }

    public PieceList getBlackTeam() {
        return blackTeam;
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
