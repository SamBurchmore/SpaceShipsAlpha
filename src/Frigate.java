import java.awt.*;

public class Frigate extends BasePiece {


    public Frigate(Team team, int[] location) {
        super(team, location, 2, 4, 4, 3, Direction.NORTH, 1, "Frigate");
    }

    @Override
    public PieceType getType() {
        return PieceType.FRIGATE;
    }

}
