package Data;

public class Frigate extends BasePiece {


    public Frigate(Team team, int[] location) {
        super(team, location, 4, 10, 4, 2, Direction.NORTH, 1, AttackType.ROCKET, 1);
    }

    @Override
    public PieceType getType() {
        return PieceType.FRIGATE;
    }

}
