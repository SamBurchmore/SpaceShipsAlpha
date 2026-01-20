import java.awt.*;

public class Corvette extends BasePiece {


    public Corvette(Team team, int[] location) {
        super(team, location, 2, 6, 1, 3, null);
    }

    @Override
    public PieceType getType() {
        return PieceType.CORVETTE;
    }

}
