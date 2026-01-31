import java.awt.*;

public class Carrier extends BasePiece {


    public Carrier(Team team, int[] location) {
        super(team, location, 4, 8, 5, 1, null, 3, "Carrier");
    }

    @Override
    public PieceType getType() {
        return PieceType.CARRIER;
    }

}
