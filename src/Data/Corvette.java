package Data;

public class Corvette extends BasePiece {

    public Corvette(Team team, int[] location) {
        super(team, location, 4, 6, 1, 3, null, 1, AttackType.CANNON);
    }

    @Override
    public PieceType getType() {
        return PieceType.CORVETTE;
    }

}
