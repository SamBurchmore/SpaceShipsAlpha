package Data;

public class Cruiser extends BasePiece {


    public Cruiser(Team team, int[] location) {
        super(team, location, 8, 12, 6, 11, Direction.NORTH, 2, AttackType.ROCKET, 2);
    }

    @Override
    public PieceType getType() {
        return PieceType.CRUISER;
    }

    public Cruiser deepCopy() {
        Cruiser cruiser = new Cruiser(this.getTeam(), new int[]{this.getLocation()[0], this.getLocation()[1]});
        cruiser.setHasMoved(this.hasMoved());
        cruiser.setDamaged(this.getDamaged());
        cruiser.setDirection(this.getDirection());
        return cruiser;
    }

}
