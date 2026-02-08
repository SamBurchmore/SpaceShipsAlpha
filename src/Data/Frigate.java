package Data;

public class Frigate extends BasePiece {


    public Frigate(Team team, int[] location) {
        super(team, location, 4, 10, 4, 2, Direction.NORTH, 1, AttackType.ROCKET, 1);
    }

    @Override
    public PieceType getType() {
        return PieceType.FRIGATE;
    }

    public Frigate deepCopy() {
        Frigate frigate = new Frigate(this.getTeam(), new int[]{this.getLocation()[0], this.getLocation()[1]});
        frigate.setHasMoved(this.hasMoved());
        frigate.setDamaged(this.getDamaged());
        frigate.setDirection(this.getDirection());
        return frigate;
    }

}
