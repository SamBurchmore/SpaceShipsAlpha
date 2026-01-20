import java.awt.*;

public class Frigate extends BasePiece {


    public Frigate(Team team, int[] location) {
        super(team, location, 2, 4, 4, 2, Direction.NORTH);
    }

    @Override
    public PieceType getType() {
        return PieceType.FRIGATE;
    }

}
