package Data;

public class Carrier extends BasePiece {


    public Carrier(Team team, int[] location) {
        super(team, location, 4, 8, 5, 1, null, 3, AttackType.STRIKE, 1);
    }

    @Override
    public PieceType getType() {
        return PieceType.CARRIER;
    }

    public Carrier deepCopy() {
        Carrier carrier = new Carrier(this.getTeam(), new int[]{this.getLocation()[0], this.getLocation()[1]});
        carrier.setHasMoved(this.hasMoved());
        carrier.setDamaged(this.getDamaged());
        return carrier;
    }

}
