package Data;

import java.awt.*;

public class BasePiece implements Piece {

    private int[] location;
    private final Team team;
    private final int armour;

    private final int attack;

    private boolean damaged;

    private final int movementRange;

    private int attackRange;

    private Direction direction;

    private boolean hasMoved;

    private int actionCost;

    private int attackCost;
    private AttackType attackType;

    private boolean selected;

    long id;

    public BasePiece(Team team, int[] location, int armour, int attack, int attackRange, int movementRange, Direction direction, int actionCost, AttackType attackType, int attackCost) {
        this.team = team;
        this.location = location;
        this.armour = armour;
        this.attack = attack;
        this.damaged = false;
        this.attackRange = attackRange;
        this.movementRange = movementRange;
        this.direction = null;
        this.hasMoved = false;
        this.actionCost = actionCost;
        this.attackType = attackType;
        this.attackCost = attackCost;
        if (direction != null) {
            if (team == Team.BLACK) {
                this.direction = Direction.SOUTH;
            }
            else {
                this.direction = Direction.NORTH;
            }
        }
        this.id = System.nanoTime();
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    @Override
    public int getActionCost() {
        return actionCost;
    }

    @Override
    public int getAttackCost() {
        return attackCost;
    }

    @Override
    public void setActionCost(int actionCost) {
        this.actionCost = actionCost;
    }


    @Override
    public AttackType getAttackType() {
        return attackType;
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public Direction getValidDirectionChanges() {
        return null;
    }

    @Override
    public PieceType getType() {
        return null;
    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public Team getTeam() {
        return team;
    }

    @Override
    public int[] getLocation() {
        return location;
    }

    @Override
    public void setLocation(int[] location) {
        this.location = location;
    }

    @Override
    public int getArmour() {
        return armour;
    }

    @Override
    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    @Override
    public boolean getDamaged() {
        return damaged;
    }

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public int getMovementRange() {
        return movementRange;
    }

    @Override
    public int getAttackRange() {
        return attackRange;
    }

    @Override
    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
