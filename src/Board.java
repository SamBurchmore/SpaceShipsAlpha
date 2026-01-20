import jdk.jfr.FlightRecorder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Random;



public class Board {
    private Color lightColor;// = new Color(0, 122, 0);
    private Color darkColor;// = new Color(0, 92, 0);
    private Tile[] tiles = new Tile[]{};
    
    private final int size = 12;


    public Board(Color lightColor, Color darkColor) throws IOException {
        setLightColor(lightColor);
        setDarkColor(darkColor);
        Color currentColor = lightColor;
        this.tiles = new Tile[size * size];
        int x = 0;
        int y = 0;
        for (int i = 0; i < size * size; i++) {
            this.tiles[i] = new Tile(x, y);
            this.tiles[i].setColor(currentColor);
            if (currentColor == lightColor) {
                currentColor = darkColor;
            }
            else {
                currentColor = lightColor;
            }
            x++;
            if (x == size) {
                x = 0;
                y++;
                if (currentColor == lightColor) {
                    currentColor = darkColor;
                }
                else {
                    currentColor = lightColor;
                }
            }
        }
    }

    public Color getLightColor() {
        return lightColor;
    }

    public void setLightColor(Color lightColor) {
        this.lightColor = lightColor;
    }

    public Color getDarkColor() {
        return darkColor;
    }

    public void setDarkColor(Color darkColor) {
        this.darkColor = darkColor;
    }

    public Tile getTile(int x, int y) {
        return tiles[y * size + x];
    }

    public void setTile(int x, int y, Piece piece) {
        tiles[y * size + x].setPiece(piece);
    }

    public int getSize() {
        return size;
    }
}
