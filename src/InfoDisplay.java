import javax.swing.*;
import java.awt.*;

public class InfoDisplay {

    private JLabel armourLabel;
    private JLabel attackScoreLabel;
    private JLabel attackRangeLabel;
    private JLabel attackLabel;
    private JLabel pieceLabel;
    private JLabel actionPointCostLabel;
    private JPanel infoPanel;

    private JPanel attackInfoPanel;

    private JPanel containerPanel;

    public InfoDisplay() {
        containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.X_AXIS));
        armourLabel = new JLabel(" ");
        attackScoreLabel = new JLabel(" ");
        attackRangeLabel = new JLabel(" ");
        attackLabel = new JLabel(" ");
        pieceLabel = new JLabel(" ");
        actionPointCostLabel = new JLabel(" ");
        attackScoreLabel.setForeground(Color.black);
        attackRangeLabel.setForeground(Color.black);
        attackLabel.setForeground(Color.black);
        pieceLabel.setForeground(Color.black);
        actionPointCostLabel.setForeground(Color.black);
        armourLabel.setFont(new Font("Sans Serif", Font.BOLD, 13));
        attackScoreLabel.setFont(new Font("Sans Serif", Font.BOLD, 13));
        attackRangeLabel.setFont(new Font("Sans Serif", Font.BOLD, 13));
        attackLabel.setFont(new Font("Sans Serif", Font.BOLD, 13));
        pieceLabel.setFont(new Font("Sans Serif", Font.BOLD, 13));
        actionPointCostLabel.setFont(new Font("Sans Serif", Font.BOLD, 13));
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        //infoPanel.add(pieceLabel);
        infoPanel.add(attackRangeLabel);
        infoPanel.add(attackScoreLabel);
        infoPanel.add(armourLabel);
        infoPanel.add(actionPointCostLabel);

        attackInfoPanel = new JPanel();
        attackInfoPanel.add(attackLabel);

        infoPanel.add(attackInfoPanel);
        JPanel space = new JPanel();
        //containerPanel.add(space);
        //containerPanel.add(infoPanel);

    }


    public void setInfoPanelText(Piece piece) {
        armourLabel.setText("Armour: " +piece.getArmour());
        armourLabel.setForeground(Color.BLACK);
        if (piece.getActionCost() > 1) {
            actionPointCostLabel.setText("Costs " + piece.getActionCost() + " Action Points");
        }
        else {
            actionPointCostLabel.setText("Costs " + piece.getActionCost() + " Action Point");
        }
        if (piece.getType() == PieceType.FRIGATE) {
            attackScoreLabel.setText("Attack Dice: Equal to targets maximum armour");
        }
        else {
            attackScoreLabel.setText("Attack Dice: " + piece.getAttack());
        }
        attackRangeLabel.setText("Attack Range: " + piece.getAttackRange());
        pieceLabel.setText(String.valueOf(piece.getType()));
        if (piece.getDamaged()) {
            armourLabel.setText("Armour: " + piece.getArmour()/2 + "/" + piece.getArmour());
            armourLabel.setForeground(Color.RED);
        }
        attackLabel.setText(" ");
    }

    public void clearInfoPanelText() {
        armourLabel.setText(" ");
        attackScoreLabel.setText(" ");
        attackRangeLabel.setText(" ");
        pieceLabel.setText(" ");
        actionPointCostLabel.setText(" ");
    }

    public void setAttackResult(Piece attacker, Piece defender, int attackScore, boolean result, boolean damaged) {
        if (defender.getDamaged() && !damaged) {
            if (result) {
                if (defender.getType() == PieceType.BATTLESHIP) {
                    attackLabel.setText(attacker.getTeam() + " wins.");
                }
                else {
                    attackLabel.setText(attackScore + " vs " + defender.getArmour()/2 + ". Success.\n " + defender.getType() + " destroyed.");
                }
            }
            else {
                attackLabel.setText(attackScore + " vs " + defender.getArmour()/2 + ". Fail.\n" + defender.getType() + " survived attack.");
            }
        }
        else if (damaged) {
            attackLabel.setText(attackScore + " vs " + defender.getArmour() + ". Success.\n " + defender.getType() + " damaged.");
        }
        else if (result) {
            if (defender.getType() == PieceType.BATTLESHIP) {
                attackLabel.setText(attacker.getTeam() + " wins.");
            }
            else {
                attackLabel.setText(attackScore + " vs " + defender.getArmour() + ". Success.\n " + defender.getType() + " destroyed.");
            }
        }
        else {
            attackLabel.setText(attackScore + " vs " + defender.getArmour() + ". Fail.\n " + defender.getType() + " survived attack.");
        }
    }

    public JPanel getDisplayComponent() {
        return infoPanel;
    }

    public void setColour(Color color) {
        infoPanel.setBackground(color);
        attackInfoPanel.setBackground(color);
    }


}
