package Logic;

import Data.Tile;

import java.util.ArrayList;

public class TileList extends ArrayList<Tile> {

    public TileList deepCopy() {
        TileList tileListCopy = new TileList();
        for (Tile tile : this) {
            tileListCopy.add(tile.deepCopy());
        }
        return tileListCopy;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Tile tile : this) {
            stringBuilder.append(tile.x);
            stringBuilder.append(" ");
            stringBuilder.append(tile.y);
            stringBuilder.append(" ");
            stringBuilder.append(tile.getPiece());
            stringBuilder.append(" | ");
        }
        return stringBuilder.toString();
    }
}
