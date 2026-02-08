package Data;

import java.awt.*;

public class Tile {

    private Piece piece = null;

    public int x;

    public int y;

    private Color color;

    private boolean lightOrDark; // light = false, dark = true

    public Tile(int x, int y) {
        setLocation(x, y);
    }

    public Tile(Color color, Piece piece, int x, int y) {
        setLocation(x, y);
        setPiece(piece);
        setColor(color);
    }

    public Tile(Color color, int x, int y) {
        setLocation(x, y);
        setColor(color);
    }


    public Tile deepCopy() {
        if (this.piece == null) {
            return new Tile(this.getColor(), null, this.x, this.y);
        }
        return new Tile(this.getColor(), this.getPiece().deepCopy(), this.x, this.y);
    }



    public Piece getPiece() {
        return piece;
    }


    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int[] getLocation() {
        return new int[]{x,y};
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }


}
