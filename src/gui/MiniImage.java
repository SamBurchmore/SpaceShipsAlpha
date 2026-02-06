package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MiniImage {

    private JPanel miniBoardPanel;
    private BufferedImage miniBoardImage;

    private BufferedImage emptyMiniImage;

    private BufferedImage border;


    public MiniImage(BufferedImage initialImage) throws IOException {
        border = ImageIO.read(new File("images\\corvette_range_display.png"));
        miniBoardImage = initialImage;
        emptyMiniImage = initialImage;
        miniBoardPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                //int x = (miniBoardImage.getWidth(null) - miniBoardImage.getWidth(null)) / 2;
                //int y = (miniBoardImage.getWidth(null) - miniBoardImage.getHeight(null)) / 2;
                g.drawImage(miniBoardImage, 25, 25, this);
                g.drawImage(border, 0, 0, this);

            }
        };
    }

    public void updateImage(BufferedImage image) {
        miniBoardImage = image;
        miniBoardPanel.repaint();
    }
    public void clearImage() {
        updateImage(emptyMiniImage);
    }

    public JPanel getDisplayComponent() {
        return miniBoardPanel;
    }

}
