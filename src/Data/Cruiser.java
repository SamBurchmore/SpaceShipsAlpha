package Data;

public class Cruiser extends BasePiece {


    public Cruiser(Team team, int[] location) {
        super(team, location, 8, 12, 6, 11, Direction.NORTH, 2, AttackType.ROCKET);
    }

    @Override
    public PieceType getType() {
        return PieceType.CRUISER;
    }

}
