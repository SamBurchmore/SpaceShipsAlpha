import java.awt.*;

public class Cruiser extends BasePiece {


    public Cruiser(Team team, int[] location) {
        super(team, location, 6, 10, 6, 8, Direction.NORTH, 2, "Cruiser");
    }

    @Override
    public PieceType getType() {
        return PieceType.CRUISER;
    }

}
