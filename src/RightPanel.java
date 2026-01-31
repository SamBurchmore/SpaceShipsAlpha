import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RightPanel {

    private JPanel rightPanel;
    private JPanel piecePanel;
    private JLabel pieceImage;
    private JLabel pieceRangeImage;
    private JPanel pieceAttributes;
    private final InfoDisplay infoDisplay;
    private JLabel remainingActionPointsLabel;

    private JLabel pieceNameLabel;


    private BufferedImage actionPointImage;
    private BufferedImage actionPointImage1;
    private BufferedImage actionPointImage2;
    private BufferedImage actionPointImage3;
    private BufferedImage blankRangeDisplay;
    private BufferedImage blankPieceImage;
    private BufferedImage corvetteRangeImage;
    private BufferedImage frigateRangeImage;
    private BufferedImage destroyerRangeImage;
    private BufferedImage cruiserRangeImage;
    private BufferedImage battleshipRangeImage;
    private BufferedImage carrierRangeImage;
    private BufferedImage blackCorvetteDisplayImage;
    private BufferedImage blackFrigateDisplayImage;
    private BufferedImage blackDestroyerDisplayImage;
    private BufferedImage blackCruiserDisplayImage;
    private BufferedImage blackBattleshipDisplayImage;
    private BufferedImage blackCarrierDisplayImage;
    private BufferedImage whiteCorvetteDisplayImage;
    private BufferedImage whiteFrigateDisplayImage;
    private BufferedImage whiteDestroyerDisplayImage;
    private BufferedImage whiteCruiserDisplayImage;
    private BufferedImage whiteBattleshipDisplayImage;
    private BufferedImage whiteCarrierDisplayImage;

    private final Color backgroundColour = new Color(200, 200, 200);
    public RightPanel() throws IOException {
        loadImages();
        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(350, 750));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        infoDisplay = new InfoDisplay();
        //infoPanel.getDisplayComponent().setPreferredSize(new Dimension(200, 1));
        piecePanel = new JPanel();
        pieceImage = new JLabel();
        pieceRangeImage = new JLabel();
        remainingActionPointsLabel = new JLabel();
        remainingActionPointsLabel.setIcon(new ImageIcon(actionPointImage));
        remainingActionPointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pieceNameLabel = new JLabel();
        
        pieceAttributes = new JPanel();
        pieceRangeImage.setPreferredSize(new Dimension(260, 260));
        pieceRangeImage.setIcon(new ImageIcon(blankRangeDisplay));
        pieceRangeImage.setIcon(new ImageIcon(blankPieceImage));
        piecePanel.setPreferredSize(new Dimension(260, 480));
        piecePanel.add(pieceImage);
        piecePanel.add(pieceRangeImage);

        JPanel pieceNamePanel = new JPanel();
        pieceNamePanel.add(pieceNameLabel);
        rightPanel.add(pieceNamePanel);

        rightPanel.add(piecePanel);

        JPanel infoDisplayPanel = new JPanel();
        infoDisplayPanel.add(infoDisplay.getDisplayComponent());
        rightPanel.add(infoDisplayPanel);

        JPanel remainingActionPointsPanel = new JPanel();
        remainingActionPointsPanel.add(remainingActionPointsLabel);
        rightPanel.add(remainingActionPointsPanel);

        piecePanel.setBackground(backgroundColour);
        pieceImage.setBackground(backgroundColour);
        rightPanel.setBackground(backgroundColour);
        //panel.setBackground(backgroundColour);
        infoDisplay.getDisplayComponent().setBackground(backgroundColour);
    }

    public void loadImages() throws IOException {
        actionPointImage = ImageIO.read(new File("images\\actionPointPanels\\actionPointPanel.png"));
        actionPointImage1 = ImageIO.read(new File("images\\actionPointPanels\\actionPointPanel1.png"));
        actionPointImage2 = ImageIO.read(new File("images\\actionPointPanels\\actionPointPanel2.png"));
        actionPointImage3 = ImageIO.read(new File("images\\actionPointPanels\\actionPointPanel3.png"));
        blankRangeDisplay = ImageIO.read(new File("images\\blank_range_display.png"));
        blankPieceImage = ImageIO.read(new File("images\\blank_piece.png"));
        corvetteRangeImage = ImageIO.read(new File("images\\corvette_range_display.png"));
        frigateRangeImage = ImageIO.read(new File("images\\frigate_range_display.png"));
        destroyerRangeImage = ImageIO.read(new File("images\\destroyer_range_display.png"));
        cruiserRangeImage = ImageIO.read(new File("images\\cruiser_range_display.png"));
        battleshipRangeImage = ImageIO.read(new File("images\\battleship_range_display.png"));
        carrierRangeImage = ImageIO.read(new File("images\\carrier_range_display.png"));
        whiteCorvetteDisplayImage = ImageIO.read(new File("images\\big_corvette_white.png"));
        whiteFrigateDisplayImage = ImageIO.read(new File("images\\big_frigate_white.png"));
        whiteDestroyerDisplayImage = ImageIO.read(new File("images\\big_destroyer_white.png"));
        whiteCruiserDisplayImage = ImageIO.read(new File("images\\big_cruiser_white.png"));
        whiteBattleshipDisplayImage = ImageIO.read(new File("images\\big_battleship_white.png"));
        whiteCarrierDisplayImage = ImageIO.read(new File("images\\big_carrier_white.png"));
        blackCorvetteDisplayImage = ImageIO.read(new File("images\\big_corvette_black.png"));
        blackFrigateDisplayImage = ImageIO.read(new File("images\\big_frigate_black.png"));
        blackDestroyerDisplayImage = ImageIO.read(new File("images\\big_destroyer_black.png"));
        blackCruiserDisplayImage = ImageIO.read(new File("images\\big_cruiser_black.png"));
        blackBattleshipDisplayImage = ImageIO.read(new File("images\\big_battleship_black.png"));
        blackCarrierDisplayImage = ImageIO.read(new File("images\\big_carrier_black.png"));
    }

    public JPanel getDisplayComponent() {
        return rightPanel;
    }

    public void updateSelectedPiece(Piece piece, SpriteTags spriteTag) {
        pieceImage.setIcon(new ImageIcon(spritePathMap(spriteTag)));
        pieceNameLabel.setText(piece.getName());

    }

    public void updateSelectedPieceRange(Piece piece) {
        pieceRangeImage.setIcon(new ImageIcon(pieceRangeImageMap(piece)));
    }
    public void clearPieceImages() {
        pieceImage.setIcon(new ImageIcon(blankPieceImage));
        pieceRangeImage.setIcon(new ImageIcon(blankRangeDisplay));
    }

    public BufferedImage pieceRangeImageMap(Piece piece) {
        switch (piece.getType()) {
            case CORVETTE -> {
                return corvetteRangeImage;
            }
            case FRIGATE -> {
                return frigateRangeImage;
            }
            case DESTROYER -> {
                return destroyerRangeImage;
            }
            case CRUISER -> {
                return cruiserRangeImage;
            }
            case BATTLESHIP -> {
                return battleshipRangeImage;
            }
            case CARRIER -> {
                return carrierRangeImage;
            }
        }
        return null;
    }

    public BufferedImage spritePathMap(SpriteTags spriteTag) {
        switch (spriteTag) {
            case WHITECORVETTE -> {
                return whiteCorvetteDisplayImage;
            }
            case WHITEFRIGATENORTH,WHITEFRIGATEEAST,WHITEFRIGATEWEST,WHITEFRIGATESOUTH -> {
                return whiteFrigateDisplayImage;
            }
            case WHITEDESTROYER -> {
                return whiteDestroyerDisplayImage;
            }
            case WHITECRUISERNORTH,WHITECRUISEREAST,WHITECRUISERSOUTH,WHITECRUISERWEST -> {
                return whiteCruiserDisplayImage;
            }
            case WHITEBATTLESHIP -> {
                return whiteBattleshipDisplayImage;
            }
            case WHITECARRIER -> {
                return whiteCarrierDisplayImage;
            }
            case BLACKCORVETTE -> {
                return blackCorvetteDisplayImage;
            }
            case BLACKFRIGATESOUTH,BLACKFRIGATEEAST,BLACKFRIGATENORTH,BLACKCRUISERWEST -> {
                return blackFrigateDisplayImage;
            }
            case BLACKDESTROYER -> {
                return blackDestroyerDisplayImage;
            }
            case BLACKCRUISERSOUTH,BLACKCRUISEREAST,BLACKCRUISERNORTH,BLACKFRIGATEWEST -> {
                return blackCruiserDisplayImage;
            }
            case BLACKBATTLESHIP -> {
                return blackBattleshipDisplayImage;
            }
            case BLACKCARRIER -> {
                return blackCarrierDisplayImage;
            }
        }
        return null;
    }

    public InfoDisplay getInfoPanel() {
        return infoDisplay;
    }

    public void updateActionPointsRemaining(int ap) {
        switch (ap) {
            case 1 -> remainingActionPointsLabel.setIcon(new ImageIcon(actionPointImage1));
            case 2 -> remainingActionPointsLabel.setIcon(new ImageIcon(actionPointImage2));
            case 3 -> remainingActionPointsLabel.setIcon(new ImageIcon(actionPointImage3));
        }
    }
}
