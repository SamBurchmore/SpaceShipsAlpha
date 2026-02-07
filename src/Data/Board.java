package Data;

import java.awt.*;
import java.io.IOException;


public class Board {
    private Color lightColor;// = new Color(0, 122, 0);
    private Color darkColor;// = new Color(0, 92, 0);
    private Tile[] tiles = new Tile[]{};
    
    private final int size;


    public Board(Color lightColor, Color darkColor, int size) {
        this.size = size;
        setLightColor(lightColor);
        setDarkColor(darkColor);
        Color currentColor = lightColor;
        this.tiles = new Tile[size * size];
        int x = 0;
        int y = 0;
        for (int i = 0; i < size * size; i++) {
            this.tiles[i] = new Tile(x, y);
            this.tiles[i].setColor(currentColor);
            currentColor = swapTileColor(currentColor);
            x++;
            if (x == size) {
                x = 0;
                y++;
                if (size % 2 == 0) {
                    currentColor = swapTileColor(currentColor);
                }
            }
        }
    }


    public Color swapTileColor(Color color) {
        //Random random = new Random();
        if (color == darkColor) {
            color = lightColor;
        }
        else {
            color = darkColor;

        }
        return color;
        //return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
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
