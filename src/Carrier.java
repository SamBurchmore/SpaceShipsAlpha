import java.awt.*;

public class Carrier extends BasePiece {


    public Carrier(Team team, int[] location) {
        super(team, location, 6, 10, 5, 1, null);
    }

    @Override
    public PieceType getType() {
        return PieceType.CARRIER;
    }

}
