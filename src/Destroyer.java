import java.awt.*;

public class Destroyer extends BasePiece {


    public Destroyer(Team team, int[] location) {
        super(team, location, 4, 10, 2, 2, null, 2, "Destroyer");
    }

    @Override
    public PieceType getType() {
        return PieceType.DESTROYER;
    }

}
