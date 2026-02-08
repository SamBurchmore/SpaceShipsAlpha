package Data;

public class Battleship extends BasePiece {


    public Battleship(Team team, int[] location) {
        super(team, location, 10, 12, 3, 1, null, 3, AttackType.CANNON, 2);
    }

    @Override
    public PieceType getType() {
        return PieceType.BATTLESHIP;
    }

    public Battleship deepCopy() {
        Battleship battleship = new Battleship(this.getTeam(), new int[]{this.getLocation()[0], this.getLocation()[1]});
        battleship.setHasMoved(this.hasMoved());
        battleship.setDamaged(this.getDamaged());
        return battleship;
    }

}
