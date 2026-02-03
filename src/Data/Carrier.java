package Data;

public class Carrier extends BasePiece {


    public Carrier(Team team, int[] location) {
        super(team, location, 4, 8, 5, 1, null, 1, AttackType.STRIKE);
    }

    @Override
    public PieceType getType() {
        return PieceType.CARRIER;
    }

}
