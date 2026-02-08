package Logic;

import Data.Piece;
import Data.Tile;

import java.util.ArrayList;

public class PieceList extends ArrayList<Piece> {

    public PieceList deepCopy() {
        PieceList pieceListCopy = new PieceList();
        for (Piece piece : this) {
            pieceListCopy.add(piece.deepCopy());
        }
        return pieceListCopy;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Piece piece : this) {
            stringBuilder.append(piece.getLocation()[0]);
            stringBuilder.append(" ");
            stringBuilder.append(piece.getLocation()[1]);
            stringBuilder.append(" ");
            stringBuilder.append(piece.getType().toString());
            stringBuilder.append(" | ");
        }
        return stringBuilder.toString();
    }
}
