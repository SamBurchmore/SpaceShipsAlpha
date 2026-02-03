package Data;

public class Frigate extends BasePiece {


    public Frigate(Team team, int[] location) {
        super(team, location, 4, 10, 2, 3, Direction.NORTH, 1, AttackType.ROCKET);
    }

    @Override
    public PieceType getType() {
        return PieceType.FRIGATE;
    }

}
