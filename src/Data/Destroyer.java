package Data;

public class Destroyer extends BasePiece {


    public Destroyer(Team team, int[] location) {
        super(team, location, 6, 10, 2, 2, null, 1, AttackType.CANNON, 2);
    }

    @Override
    public PieceType getType() {
        return PieceType.DESTROYER;
    }

}
