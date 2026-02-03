package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import Data.*;

public class GameView {

    private JFrame gameFrame;

    private JPanel boardPanel;

    private BufferedImage boardImage;

    private final int size = 600;

    private final BottomPanel bottomPanel;
    private final TopPanel topPanel;
    private final RightPanel rightPanel;

    private Color backgroundColour = new Color(200, 200, 200);


    public GameView() throws IOException {
        gameFrame = new JFrame();
        gameFrame.setForeground(backgroundColour);
        bottomPanel = new BottomPanel();
        topPanel = new TopPanel();
        rightPanel = new RightPanel();
        gameFrame.setName("Land Battle");
        //gameFrame.getContentPane().setLayout(new FlowLayout(FlowLayout.LEADING));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setIconImage(ImageIO.read((new File("images\\battleship.png").toURI()).toURL()));
        gameFrame.setLayout(new GridBagLayout());
        gameFrame.setResizable(false);
        gameFrame.setLocationRelativeTo(null);

        boardPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x = (size - boardImage.getWidth(null)) / 2;
                int y = (size - boardImage.getHeight(null)) / 2;
                g.drawImage(boardImage, x, y, this);
            }
        };

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 4;
        c.gridy = 0;
        c.gridheight = 5;
        c.gridwidth = 2;
        gameFrame.add(rightPanel.getDisplayComponent(), c);

        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 3;
        gameFrame.add(topPanel.getDisplayComponent(), c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 3;
        c.gridwidth = 3;
        gameFrame.add(boardPanel, c);

        c.gridx = 0;
        c.gridy = 4;
        c.gridheight = 1;
        c.gridwidth = 3;

        gameFrame.add(bottomPanel.getDisplayComponent(), c);
        boardPanel.setPreferredSize(new Dimension(size, size));
        topPanel.getDisplayComponent().setPreferredSize(new Dimension(600, 80));
        bottomPanel.getDisplayComponent().setPreferredSize(new Dimension(600, 80));

        // Define the menu bar and its menus
        JMenuBar menuBar = new JMenuBar();
        JMenu infoMenu = new JMenu("Info");
        JMenu controlMenu = new JMenu("Game");

        JPanel infoMenuPanel = new JPanel(new GridLayout(3, 3));

        JButton ruleInfoButton = new JButton("Game Rules");
        JButton corvetteStatsButton = new JButton("Data.Corvette Stats");
        JButton frigateStatsButton = new JButton("Data.Frigate Stats");
        JButton destroyerStatsButton = new JButton("Data.Destroyer Stats");
        JButton cruiserStatsButton = new JButton("Data.Cruiser Stats");
        JButton carrierStatsButton = new JButton("Data.Carrier Stats");
        JButton battleshipStatsButton = new JButton("Data.Battleship Stats");

        infoMenuPanel.add(ruleInfoButton);
        infoMenuPanel.add(corvetteStatsButton);
        infoMenuPanel.add(frigateStatsButton);
        infoMenuPanel.add(destroyerStatsButton);
        infoMenuPanel.add(cruiserStatsButton);
        infoMenuPanel.add(carrierStatsButton);
        infoMenuPanel.add(battleshipStatsButton);

        infoMenu.add(infoMenuPanel);

        menuBar.add(infoMenu);

        gameFrame.setJMenuBar(menuBar);

        gameFrame.pack();

    }

    public JFrame getGameFrame() {
        return gameFrame;
    }

    public void setGameFrame(JFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void updateBoardImage(BufferedImage boardImage) {
        this.boardImage = boardImage;
    }

    public void updateBoard(BufferedImage worldImage) {
        updateBoardImage(worldImage);
        boardPanel.repaint();
    }

    public void setVisible(boolean visible) {
        gameFrame.setVisible(visible);
    }


    public RightPanel getRightPanel() {
        return rightPanel;
    }

    public BufferedImage getBoardImage() {
        return boardImage;
    }

    public JPanel getBoardPanel() {
        return boardPanel;
    }

    public void updateTurn(Team team) throws IOException {
        BufferedImage turnIcon;
        Color color;
        if (team == Team.WHITE) {
            turnIcon = ImageIO.read(new File("images\\white_turn_bar.png"));
            color = Color.white;
        }
        else {
            turnIcon = ImageIO.read(new File("images\\black_turn_bar.png"));
            color = Color.black;
        }
        bottomPanel.getImage().setIcon(new ImageIcon(turnIcon));
        bottomPanel.getDisplayComponent().setBackground(color);
        topPanel.getImage().setIcon(new ImageIcon(turnIcon));
        topPanel.getDisplayComponent().setBackground(color);
    }
}
