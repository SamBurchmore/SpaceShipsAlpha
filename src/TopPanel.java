import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TopPanel {

    private JLabel turnLabel;


    public TopPanel() throws IOException {
        turnLabel = new JLabel();
        turnLabel.setPreferredSize(new Dimension(600, 50));
    }



    public JLabel getDisplayComponent() {
        return turnLabel;
    }
}


