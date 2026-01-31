import java.awt.*;
import java.security.DigestInputStream;

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

    public boolean hasMoved();

    public void setHasMoved(boolean hasMoved);

    public int getActionCost();

    public void setActionCost(int actionCost);

    public String getName();

}
