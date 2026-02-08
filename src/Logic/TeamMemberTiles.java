package Logic;

import Data.Piece;
import Data.Team;
import Data.Tile;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamMemberTiles extends HashMap<Tile, TileList> {

    public TeamMemberTiles deepCopy() {
        TeamMemberTiles teamMemberTilesCopy = new TeamMemberTiles();
        for (Tile teamTile : this.keySet()){
            teamMemberTilesCopy.put(teamTile.deepCopy(), this.get(teamTile).deepCopy());
        }
        return teamMemberTilesCopy;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Tile teamTile : this.keySet()){
            stringBuilder.append(teamTile.getPiece().getType());
            stringBuilder.append(" --- ");
            stringBuilder.append(this.get(teamTile).toString());
        }
        return stringBuilder.toString();
    }

}
