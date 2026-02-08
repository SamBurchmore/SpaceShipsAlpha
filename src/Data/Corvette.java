package Data;

public class Corvette extends BasePiece {

    public Corvette(Team team, int[] location) {
        super(team, location, 4, 6, 1, 3, null, 1, AttackType.CANNON, 1);
    }

    @Override
    public PieceType getType() {
        return PieceType.CORVETTE;
    }

    public Corvette deepCopy() {
        Corvette corvette = new Corvette(this.getTeam(), new int[]{this.getLocation()[0], this.getLocation()[1]});
        corvette.setHasMoved(this.hasMoved());
        corvette.setDamaged(this.getDamaged());
        return corvette;
    }

}
