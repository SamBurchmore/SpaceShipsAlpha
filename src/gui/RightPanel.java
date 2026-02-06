package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import Data.*;

public class RightPanel {

    private JPanel rightPanel;
    private JPanel piecePanel;
    private JLabel pieceImage;
    private JLabel pieceRangeImage;
    private JPanel pieceAttributes;
    private final InfoDisplay infoDisplay;
    private JLabel remainingActionPointsLabel;

    private JLabel pieceNameLabel;

    private MiniImage miniImage;


    private BufferedImage actionPointImage;
    private BufferedImage actionPointImage1;
    private BufferedImage actionPointImage2;
    private BufferedImage actionPointImage3;
    private BufferedImage actionPointImage4;
    private BufferedImage actionPointImage5;
    private BufferedImage actionPointImage6;
    private BufferedImage actionPointImage7;
    private BufferedImage actionPointImage8;
    private BufferedImage actionPointImage9;
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
    private BufferedImage corvetteNameDisplayImage;
    private BufferedImage frigateNameDisplayImage;
    private BufferedImage destroyerNameDisplayImage;
    private BufferedImage cruiserNameDisplayImage;
    private BufferedImage battleshipNameDisplayImage;
    private BufferedImage carrierNameDisplayImage;
    private BufferedImage blankNameDisplayImage;

    private final Color backgroundColour = new Color(200, 200, 200);
    public RightPanel(BufferedImage initialMiniImage) throws IOException {
        loadImages();
        rightPanel = new JPanel();
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

        pieceRangeImage.setIcon(new ImageIcon(blankRangeDisplay));
        pieceImage.setIcon(new ImageIcon(blankPieceImage));

        miniImage = new MiniImage(initialMiniImage);

        pieceNameLabel.setIcon(new ImageIcon(blankNameDisplayImage));
        JPanel pieceNamePanel = new JPanel();
        pieceNamePanel.add(pieceNameLabel);
        piecePanel.add(pieceNamePanel);

        JPanel pieceImagePanel = new JPanel();
        pieceImagePanel.add(pieceImage);
        piecePanel.add(pieceImagePanel);
        //piecePanel.setBorder(new EmptyBorder(10, 10, 10, 10));


        JPanel pieceRangeImagePanel = new JPanel();
        pieceRangeImagePanel.add(miniImage.getDisplayComponent());
        miniImage.getDisplayComponent().setPreferredSize(new Dimension(220, 220));
        piecePanel.add(pieceRangeImagePanel);


        rightPanel.add(piecePanel);

        JPanel infoDisplayPanel = new JPanel();
        infoDisplayPanel.add(infoDisplay.getDisplayComponent());
        rightPanel.add(infoDisplayPanel);



        JPanel remainingActionPointsPanel = new JPanel();
        remainingActionPointsPanel.add(remainingActionPointsLabel);
        remainingActionPointsPanel.setLayout(new GridBagLayout());
        rightPanel.add(remainingActionPointsPanel);

//        JPanel blankSpace0 = new JPanel();
//        blankSpace0.setPreferredSize(new Dimension(350, 50));
//        blankSpace0.setBackground(Color.green);
//        rightPanel.add(blankSpace0);

        pieceNamePanel.setPreferredSize(new Dimension(350, 35));
        remainingActionPointsPanel.setPreferredSize(new Dimension(260, 100));
        pieceImagePanel.setPreferredSize(new Dimension(220, 240));
        piecePanel.setPreferredSize(new Dimension(260, 600));

        remainingActionPointsPanel.setPreferredSize(new Dimension(350, 110));

        //infoDisplay.getDisplayComponent().setPreferredSize(new Dimension(350, 100));

        rightPanel.setPreferredSize(new Dimension(350, 760));


        pieceImagePanel.setBackground(backgroundColour);
        pieceRangeImagePanel.setBackground(backgroundColour);
        piecePanel.setBackground(backgroundColour);
        pieceImage.setBackground(backgroundColour);
        rightPanel.setBackground(backgroundColour);
        remainingActionPointsPanel.setBackground(backgroundColour);
        //panel.setBackground(backgroundColour);
        infoDisplayPanel.setBackground(backgroundColour);
        infoDisplay.setColour(backgroundColour);

    }

    public void loadImages() throws IOException {
        actionPointImage = ImageIO.read(new File("images\\actionPointPanels\\actionPointPanel.png"));
        actionPointImage1 = ImageIO.read(new File("images\\actionPointPanels\\actionPointPanel1.png"));
        actionPointImage2 = ImageIO.read(new File("images\\actionPointPanels\\actionPointPanel2.png"));
        actionPointImage3 = ImageIO.read(new File("images\\actionPointPanels\\actionPointPanel3.png"));
        actionPointImage4 = ImageIO.read(new File("images\\actionPointPanels\\actionPointPanel4.png"));
        actionPointImage5 = ImageIO.read(new File("images\\actionPointPanels\\actionPointPanel5.png"));
        actionPointImage6 = ImageIO.read(new File("images\\actionPointPanels\\actionPointPanel6.png"));
        actionPointImage7 = ImageIO.read(new File("images\\actionPointPanels\\actionPointPanel7.png"));
        actionPointImage8 = ImageIO.read(new File("images\\actionPointPanels\\actionPointPanel8.png"));
        actionPointImage9 = ImageIO.read(new File("images\\actionPointPanels\\actionPointPanel9.png"));
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
        corvetteNameDisplayImage = ImageIO.read(new File("images\\corvette_name_display.png"));
        frigateNameDisplayImage = ImageIO.read(new File("images\\frigate_name_display.png"));
        destroyerNameDisplayImage = ImageIO.read(new File("images\\destroyer_name_display.png"));
        cruiserNameDisplayImage = ImageIO.read(new File("images\\cruiser_name_display.png"));
        battleshipNameDisplayImage = ImageIO.read(new File("images\\battleship_name_display.png"));
        carrierNameDisplayImage = ImageIO.read(new File("images\\carrier_name_display.png"));
        blankNameDisplayImage = ImageIO.read(new File("images\\blank_name_display.png"));
    }

    public JPanel getDisplayComponent() {
        return rightPanel;
    }

    public MiniImage getMiniImage() {
        return miniImage;
    }

    public void updateSelectedPiece(Piece piece, SpriteTags spriteTag) {
        pieceImage.setIcon(new ImageIcon(spritePathMap(spriteTag)));
        pieceNameLabel.setIcon(new ImageIcon(pieceNameImageMap(piece)));

    }

    public void updateSelectedPieceRange(Piece piece) {
        pieceRangeImage.setIcon(new ImageIcon(pieceRangeImageMap(piece)));
    }
    public void clearPieceImages() {
        pieceImage.setIcon(new ImageIcon(blankPieceImage));
        pieceRangeImage.setIcon(new ImageIcon(blankRangeDisplay));
        pieceNameLabel.setIcon(new ImageIcon(blankNameDisplayImage));
        miniImage.clearImage();
    }

    public void resetActionPointAnimation(int ap) throws InterruptedException {
        updateActionPointsRemaining(0);
        TimeUnit.SECONDS.sleep(1);
        updateActionPointsRemaining(ap);
    }

    public void updateActionPointsRemaining(int ap) {
        switch (ap) {
            case 0 -> remainingActionPointsLabel.setIcon(new ImageIcon(actionPointImage));
            case 1 -> remainingActionPointsLabel.setIcon(new ImageIcon(actionPointImage1));
            case 2 -> remainingActionPointsLabel.setIcon(new ImageIcon(actionPointImage2));
            case 3 -> remainingActionPointsLabel.setIcon(new ImageIcon(actionPointImage3));
            case 4 -> remainingActionPointsLabel.setIcon(new ImageIcon(actionPointImage4));
            case 5 -> remainingActionPointsLabel.setIcon(new ImageIcon(actionPointImage5));
            case 6 -> remainingActionPointsLabel.setIcon(new ImageIcon(actionPointImage6));
            case 7 -> remainingActionPointsLabel.setIcon(new ImageIcon(actionPointImage7));
            case 8 -> remainingActionPointsLabel.setIcon(new ImageIcon(actionPointImage8));
            case 9 -> remainingActionPointsLabel.setIcon(new ImageIcon(actionPointImage9));
        }
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

    public BufferedImage pieceNameImageMap(Piece piece) {
        switch (piece.getType()) {
            case CORVETTE -> {
                return corvetteNameDisplayImage;
            }
            case FRIGATE -> {
                return frigateNameDisplayImage;
            }
            case DESTROYER -> {
                return destroyerNameDisplayImage;
            }
            case CRUISER -> {
                return cruiserNameDisplayImage;
            }
            case BATTLESHIP -> {
                return battleshipNameDisplayImage;
            }
            case CARRIER -> {
                return carrierNameDisplayImage;
            }
        }
        return null;
    }



    public BufferedImage spritePathMap(SpriteTags spriteTag) {
        switch (spriteTag) {
            case WHITECORVETTE -> {
                return whiteCorvetteDisplayImage;
            }
            case WHITEFRIGATENORTH, WHITEFRIGATEEAST, WHITEFRIGATEWEST, WHITEFRIGATESOUTH -> {
                return whiteFrigateDisplayImage;
            }
            case WHITEDESTROYER -> {
                return whiteDestroyerDisplayImage;
            }
            case WHITECRUISERNORTH, WHITECRUISEREAST, WHITECRUISERSOUTH, WHITECRUISERWEST -> {
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
            case BLACKFRIGATESOUTH, BLACKFRIGATEEAST, BLACKFRIGATENORTH, BLACKCRUISERWEST -> {
                return blackFrigateDisplayImage;
            }
            case BLACKDESTROYER -> {
                return blackDestroyerDisplayImage;
            }
            case BLACKCRUISERSOUTH, BLACKCRUISEREAST, BLACKCRUISERNORTH, BLACKFRIGATEWEST -> {
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


}
