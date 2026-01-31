import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BottomPanel {

    private JLabel turnLabel;
    private JPanel containerPanel;


    public BottomPanel() {
        containerPanel = new JPanel(new GridBagLayout());
        turnLabel = new JLabel();
        containerPanel.add(turnLabel);
        //turnLabel.setPreferredSize(new Dimension(600, 50));
    }



    public JPanel getDisplayComponent() {
        return containerPanel;
    }

    public JLabel getImage() {
        return turnLabel;
    }
}


