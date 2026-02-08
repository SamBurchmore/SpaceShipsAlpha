package Data;

public class Destroyer extends BasePiece {


    public Destroyer(Team team, int[] location) {
        super(team, location, 6, 10, 2, 2, null, 1, AttackType.CANNON, 2);
    }

    @Override
    public PieceType getType() {
        return PieceType.DESTROYER;
    }

    public Destroyer deepCopy() {
        Destroyer destroyer = new Destroyer(this.getTeam(), new int[]{this.getLocation()[0], this.getLocation()[1]});
        destroyer.setHasMoved(this.hasMoved());
        destroyer.setDamaged(this.getDamaged());
        return destroyer;
    }

}
