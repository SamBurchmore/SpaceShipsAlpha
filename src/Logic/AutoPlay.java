package Logic;
import Data.*;

import java.awt.*;


public class AutoPlay {

    private Logic.MainController mainController;

    public AutoPlay(MainController mainController) {
        this.mainController = mainController;
    }


    public BoardState getBoardState(Board board) {
        int size = board.getSize();
        Piece[][] pieces = new Piece[size+1][size+1];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Piece piece = board.getTile(i, j).getPiece();
                if (piece == null) {
                    pieces[i][j] = new BlankPiece();
                }
                else {
                    pieces[i][j] = piece;
                }
            }
        }
        return new BoardState(pieces, size);
    }

    public Board getBoard(BoardState boardState, Color lightColor, Color darkColor) {
        int size = boardState.size();
        Piece[][] pieces = boardState.pieces();
        Board board = new Board(lightColor, darkColor, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ( pieces[i][j] != null) {
                    if (pieces[i][j].getType() != null) {
                        board.getTile(i, j).setPiece(pieces[i][j]);
                    }
                }
            }
        }
        return board;
    }


}
