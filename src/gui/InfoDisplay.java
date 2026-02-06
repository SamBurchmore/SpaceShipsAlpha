package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import Data.*;
import Logic.BoardController;

public class InfoDisplay {

    private JLabel armourLabel;
    private JLabel attackScoreLabel;
    private JLabel attackRangeLabel;
    private JLabel attackLabel;
    private JLabel pieceLabel;
    private JLabel actionPointCostLabel;
    private JLabel attackCostLabel;
    private JLabel attackTypeLabel;
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
        attackCostLabel = new JLabel(" ");
        attackTypeLabel = new JLabel(" ");
        attackScoreLabel.setForeground(Color.black);
        attackRangeLabel.setForeground(Color.black);
        attackLabel.setForeground(Color.black);
        pieceLabel.setForeground(Color.black);
        actionPointCostLabel.setForeground(Color.black);
        attackTypeLabel.setForeground(Color.black);
        attackCostLabel.setForeground(Color.black);
        armourLabel.setFont(new Font("Sans Serif", Font.BOLD, 13));
        attackScoreLabel.setFont(new Font("Sans Serif", Font.BOLD, 13));
        attackRangeLabel.setFont(new Font("Sans Serif", Font.BOLD, 13));
        attackLabel.setFont(new Font("Sans Serif", Font.BOLD, 13));
        pieceLabel.setFont(new Font("Sans Serif", Font.BOLD, 13));
        actionPointCostLabel.setFont(new Font("Sans Serif", Font.BOLD, 13));
        attackCostLabel.setFont(new Font("Sans Serif", Font.BOLD, 13));
        attackTypeLabel.setFont(new Font("Sans Serif", Font.BOLD, 13));
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        //infoPanel.add(pieceLabel);
        infoPanel.add(attackRangeLabel);
        infoPanel.add(attackScoreLabel);
        infoPanel.add(armourLabel);
        infoPanel.add(actionPointCostLabel);
        infoPanel.add(attackTypeLabel);

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
        actionPointCostLabel.setText("Movement Cost: " + piece.getActionCost() + ". Attack Cost: " + piece.getAttackCost());
        attackTypeLabel.setText("Attack Type: " + piece.getAttackType().toString());

        attackLabel.setText(" ");
        displayAttackChance(piece);
    }

    public void clearInfoPanelText() {
        armourLabel.setText(" ");
        attackScoreLabel.setText(" ");
        attackRangeLabel.setText(" ");
        pieceLabel.setText(" ");
        actionPointCostLabel.setText(" ");
        attackTypeLabel.setText(" ");
    }

    public void setAttackResult(Piece attacker, Piece defender, int attackScore, boolean result, boolean damaged) {
        printAttackResult(attacker, defender, Integer.toString(attackScore), result, damaged);
    }

    public void setAttackResult(Piece attacker, Piece defender, ArrayList<Integer> attackScores, int totalAttackScore, boolean result, boolean damaged) {
        StringBuilder attackScoreStringBuilder = new StringBuilder();
        attackScoreStringBuilder.append(totalAttackScore);
        attackScoreStringBuilder.append(" (");
        for (int attackScore : attackScores) {
            attackScoreStringBuilder.append(attackScore);
            attackScoreStringBuilder.append(" + ");
        }
        String attackScoreString = attackScoreStringBuilder.substring(0, attackScoreStringBuilder.length() - 3);
        attackScoreString += ")";
        printAttackResult(attacker, defender, attackScoreString, result, damaged);
    }

    public void printAttackResult(Piece attacker, Piece defender, String attackScore, boolean result, boolean damaged) {
        if (defender.getDamaged() && !damaged) {
            if (result) {
                if (defender.getType() == PieceType.BATTLESHIP) {
                    attackLabel.setText(attackScore + " vs " + defender.getArmour() + "." + defender.getType() + " destroyed. " + attacker.getTeam() + " wins.");
                }
                else {
                    attackLabel.setText(attackScore + " vs " + defender.getArmour()/2 + "." + defender.getType() + " destroyed.");
                }
            }
            else {
                attackLabel.setText(attackScore + " vs " + defender.getArmour()/2 + "." + defender.getType() + " survived attack.");
            }
        }
        else if (damaged) {
            attackLabel.setText(attackScore + " vs " + defender.getArmour() + "." + defender.getType() + " damaged.");
        }
        else if (result) {
            if (defender.getType() == PieceType.BATTLESHIP) {
                attackLabel.setText(attackScore + " vs " + defender.getArmour() + "." + defender.getType() + " destroyed. " + attacker.getTeam() + " wins.");
            }
            else {
                attackLabel.setText(attackScore + " vs " + defender.getArmour() + "." + defender.getType() + " destroyed.");
            }
        }
        else {
            attackLabel.setText(attackScore + " vs " + defender.getArmour() + "." + defender.getType() + " survived attack.");
        }
    }

    public JPanel getDisplayComponent() {
        return infoPanel;
    }

    public void setColour(Color color) {
        infoPanel.setBackground(color);
        attackInfoPanel.setBackground(color);
    }

    public void displayAttackChance(Piece piece) {
        for (PieceType pieceType : PieceType.values()) {
            int targetArmour = BoardController.pieceFactory(pieceType, null, 0, 0).getArmour();
            int attackScore = piece.getAttack();
            if (piece.getType() == PieceType.FRIGATE) {
                attackScore = targetArmour;
            }
            double attackPercent = 100.0 / attackScore;
            double chanceToHit =  (Math.max(attackPercent * (attackScore + 1 - targetArmour), 0));
            System.out.println("                                        " + piece.getType().toString() + " has a " + (int) chanceToHit + "% chance to destroy " + pieceType);
            if (piece.getAttackType() == AttackType.ROCKET || piece.getAttackType() == AttackType.STRIKE) {
                targetArmour = targetArmour / 2;
                chanceToHit = Math.max((100 / attackScore) * (attackScore - targetArmour + 1), 0);
                System.out.println("                                                  --- and a " + (int) chanceToHit + "% chance to damage " + pieceType);
            }
        }
    }


}
