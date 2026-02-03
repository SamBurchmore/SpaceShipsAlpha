package Data;

import java.awt.*;

public interface Piece {

    Direction getValidDirectionChanges();

    PieceType getType();

    Color getColor();

    Team getTeam();

    int[] getLocation();

    void setLocation(int[] location);

    int getArmour();

    void setDamaged(boolean damaged);

    boolean getDamaged();

    int getAttack();

    int getMovementRange();

    int getAttackRange();

    Direction getDirection();

    void setDirection(Direction direction);

    boolean hasMoved();

    void setHasMoved(boolean hasMoved);

    int getActionCost();

    void setActionCost(int actionCost);

    AttackType getAttackType();

    long getID();
}
