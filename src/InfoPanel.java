import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InfoPanel {

    private JLabel armourLabel;
    private JLabel attackScoreLabel;
    private JLabel attackRangeLabel;
    private JLabel attackLabel;
    private JLabel pieceLabel;
    private JLabel objectiveLabel;
    private JPanel infoPanel;

    public InfoPanel() throws IOException {
        armourLabel = new JLabel();
        attackScoreLabel = new JLabel();
        attackRangeLabel = new JLabel();
        attackLabel = new JLabel();
        pieceLabel = new JLabel();
        objectiveLabel = new JLabel();
        infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(6, 1));
        infoPanel.setBackground(new Color(220, 220, 220));
        objectiveLabel.setText("<html><br/>[OBJECTIVE]: Destroy Enemy Battleship</html>");
//        for (int i = 0; i < 6; i++) {
//            JLabel label = new JLabel();
//            label.setForeground(new Color(i*12, i*12, i));
//            label.setText("[]");
//            infoPanel.add(label);
//        }
        infoPanel.add(objectiveLabel);
        infoPanel.add(pieceLabel);
        infoPanel.add(armourLabel);
        infoPanel.add(attackScoreLabel);
        infoPanel.add(attackRangeLabel);
        infoPanel.add(attackLabel);
//        BufferedImage myPicture = ImageIO.read(new File("images\\big_destroyer.png"));
//        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
//        infoPanel.add(picLabel);
    }


    public void setInfoPanelText(Piece piece) {
        armourLabel.setText("Armour: " +piece.getArmour());
        armourLabel.setForeground(Color.BLACK);
        attackScoreLabel.setText("Attack Score: " + piece.getAttack());
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
    }

    public void setAttackResult(Piece attacker, Piece defender, int attackScore, boolean result, boolean damaged) {
        if (defender.getDamaged() && !damaged) {
            if (result) {
                if (defender.getType() == PieceType.BATTLESHIP) {
                    attackLabel.setText(attacker.getTeam() + " wins.");
                }
                else {
                    attackLabel.setText(attackScore + " vs " + defender.getArmour()/2 + ". Success. " + defender.getType() + " destroyed.");
                }
            }
            else {
                attackLabel.setText(attackScore + " vs " + defender.getArmour()/2 + ". Fail." + defender.getType() + " missed.");
            }
        }
        else if (damaged) {
            attackLabel.setText(attackScore + " vs " + defender.getArmour() + ". Success. " + defender.getType() + " damaged.");
        }
        else if (result) {
            if (defender.getType() == PieceType.BATTLESHIP) {
                attackLabel.setText(attacker.getTeam() + " wins.");
            }
            else {
                attackLabel.setText(attackScore + " vs " + defender.getArmour() + ". Success. " + defender.getType() + " destroyed.");
            }
        }
        else {
            attackLabel.setText(attackScore + " vs " + defender.getArmour() + ". Fail. " + defender.getType() + " missed.");
        }
    }

    public JPanel getInfoPanel() {
        return infoPanel;
    }
}
