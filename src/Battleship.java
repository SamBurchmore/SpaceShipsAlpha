import java.awt.*;

public class Battleship extends BasePiece {


    public Battleship(Team team, int[] location) {
        super(team, location, 10, 12, 3, 1, null);
    }

    @Override
    public PieceType getType() {
        return PieceType.BATTLESHIP;
    }

}
