import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
//import com.kong.unirest;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MainController {
    private final GameView gameView;
    private final BoardController boardController;
    private final BoardImage boardImage;
    private final BoardLogic boardLogic;
    private final PlayLogic playLogic;
    private Tile lastClickedTile;
    private Piece activePiece;
    private Team activeTeam;
    private ArrayList<Tile> moveTiles;
    private ArrayList<Tile> attackTiles;
    public GameState gameState;

    public int maxActionPoints = 3;
    public int actionPoints = maxActionPoints;

    public ArrayList<Piece> movedPieces;

    private Color lightMoveColor = new Color(122, 122, 222);
    private Color darkMoveColor = new Color(14, 14, 94);
    private Color darkAttackColor = new Color(150, 0, 0);
    private Color lightAttackColor = new Color(200, 0, 0);
    private Color tileLightColor = new Color(50, 200, 20);
    private Color tileDarkColor = new Color(30, 150, 0);

    public MainController() throws IOException {
        movedPieces = new ArrayList<>();
        gameView = new GameView();
        boardController  = new BoardController(tileLightColor, tileDarkColor);
        boardController.getBoard().setDarkColor(tileDarkColor);
        boardController.getBoard().setLightColor(tileLightColor);
        boardImage = new BoardImage();
        boardLogic = new BoardLogic();
        playLogic = new PlayLogic();
        moveTiles = new ArrayList<>();
        attackTiles = new ArrayList<>();
        boardImage.updateBoard();
        gameView.setVisible(true);
        gameView.getGameFrame().addMouseListener(playLogic);
        activeTeam = Team.BLACK;
        gameView.updateTurn(activeTeam);
    }

    // Groups methods which allow the game to be played .i.e control the game state, listen for input, send it to BoardLogic, update BoardImage and send the image to GameView
    public class PlayLogic implements java.awt.event.MouseListener {

        public boolean setActivePiece() throws IOException {
            if (lastClickedTile.getPiece() != null && !lastClickedTile.getPiece().hasMoved()) {
                if (lastClickedTile.getPiece().getActionCost() <= actionPoints) {
                    if (lastClickedTile.getPiece().getTeam() == activeTeam) {
                        activePiece = lastClickedTile.getPiece();
                        gameView.getRightPanel().getInfoPanel().setInfoPanelText(activePiece);
                        gameView.getRightPanel().updateSelectedPiece(activePiece, boardImage.getPieceSpriteTag(activePiece));
                        gameView.getRightPanel().updateSelectedPieceRange(activePiece);
                        return true;
                    }
                }
            }
            return false;
        }

        public void newTurn() throws IOException, InterruptedException {

            for (Piece piece: movedPieces) {
                piece.setHasMoved(false);
            }
            movedPieces.clear();
            actionPoints = maxActionPoints;
            if (activeTeam == Team.BLACK) {
                activeTeam = Team.WHITE;
            }
            else {
                activeTeam = Team.BLACK;
            }
            activePiece = null;
            gameState = GameState.WAITING_INPUT;
            gameView.getRightPanel().getInfoPanel().clearInfoPanelText();
            gameView.getRightPanel().clearPieceImages();
            boardImage.updateBoard();
            System.out.println("new turn");
        }

        public void resetTurn() throws IOException {
            activePiece = null;
            gameState = GameState.WAITING_INPUT;
            gameView.getRightPanel().getInfoPanel().clearInfoPanelText();
            gameView.getRightPanel().clearPieceImages();
            boardImage.updateBoard();
        }

        public void runGame() throws IOException, InterruptedException {
            gameView.getRightPanel().getInfoPanel().clearInfoPanelText();
            gameView.getRightPanel().clearPieceImages();
            System.out.println(gameState);
            playLogic.getPointer(); // Get the mouse location
            if (gameState == GameState.PIECE_TURNING) {
                boolean actionPerformed = boardLogic.performAction();
                if (actionPerformed) {
                    gameState = GameState.WAITING_INPUT;
                    newTurn();
                }
            }
            else {
                if (gameState == GameState.WAITING_INPUT) {
                    boolean pieceSelected = setActivePiece(); // Try to set the active piece
                    if (pieceSelected) {
                        gameState = GameState.PIECE_SELECTED;
                        boardLogic.getActivePieceActions(); // Retrieve the active pieces potential actions
                        boardImage.displayActions(); // Display the active pieces potential actions
                    }
                } else if (lastClickedTile.getPiece() != null) {
                    if (activePiece == lastClickedTile.getPiece()) {
                        resetTurn();
                    } else if (lastClickedTile.getPiece().getTeam() == activeTeam) {
                        resetTurn();
                    }
                }
                if (gameState == GameState.PIECE_SELECTED) {
                    boolean actionPerformed = boardLogic.performAction();
                    if (actionPerformed) {
                        System.out.println("action performed");
                        activePiece.setHasMoved(true);
                        movedPieces.add(activePiece);
                        actionPoints -= activePiece.getActionCost();
                        if (actionPoints <= 0) {
                            newTurn();
                            gameView.updateTurn(activeTeam);
                            gameView.getRightPanel().clearPieceImages();
                        }
                        else {
                            resetTurn();
                        }
                    }
                }
                System.out.println(gameState);
            }
        }
        public void setLastClickedTile(int x, int y) {
            lastClickedTile = boardController.getBoard().getTile((x) / boardController.getScale(), (y) / boardController.getScale());
            System.out.println(Arrays.toString(lastClickedTile.getLocation()));
        }

        public void getPointer() {
            Point point = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(point, gameView.getBoardPanel());
            setLastClickedTile(point.x, point.y);
        }

        public PlayLogic() {
            gameState = GameState.WAITING_INPUT;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e))
            {

            }
            else {
                try {
                    runGame();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    // Groups methods which make up board logic
    public class BoardLogic {

        public void move(Piece piece, Tile tile) {
            if (piece.getDirection() != null) {
                piece.setDirection(computeDirection(piece, tile));
            }
            int x = tile.getLocation()[0];
            int y = tile.getLocation()[1];
            int[] oldLocation = piece.getLocation();
            boardController.getBoard().setTile(oldLocation[0], oldLocation[1], null);
            boardController.getBoard().setTile(x, y, piece);
            piece.setLocation(new int[]{x, y});
        }
        public ArrayList<Object> attack(Piece attacker, Piece defender) {
            if (attacker.getDirection() != null) {
                attacker.setDirection(computeDirection(attacker, defender));
            }
            Random random = new Random();
            int attackScore = 1 + random.nextInt(attacker.getAttack());
            if (attacker.getType() == PieceType.FRIGATE) {
                attackScore = 1 + random.nextInt(Math.max(defender.getArmour(), 2));
            }
            ArrayList<Object> result = new ArrayList<>();
            int armour = defender.getArmour();
            if (defender.getDamaged()) {
                armour = armour/2;
            }
            System.out.println("attack score: " + attackScore + " armour: " + armour);
            if (attackScore >= armour) {
                boardController.getBoard().setTile(defender.getLocation()[0], defender.getLocation()[1], null);
                result.add(defender);
                result.add(attackScore);
                result.add(true);
                result.add(false);
                return result;
            }
            if (!defender.getDamaged()) {
                if (attacker.getType() == PieceType.FRIGATE || attacker.getType() == PieceType.CRUISER || attacker.getType() == PieceType.CARRIER) {
                    if (attackScore >= defender.getArmour() / 2) {
                        defender.setDamaged(true);
                        result.add(defender);
                        result.add(attackScore);
                        result.add(true);
                        result.add(true);
                        return result;
                    }
                }
            }
            result.add(defender);
            result.add(attackScore);
            result.add(false);
            result.add(false);
            return result;
        }
        public Direction computeDirection(Piece piece0, Tile tile) {
            int x = tile.getLocation()[0];
            int y = tile.getLocation()[1];
            int x1 = piece0.getLocation()[0];
            int y1 = piece0.getLocation()[1];
            return computeDirection(x, y, x1, y1);
        }
        public Direction computeDirection(Tile tile0, Tile tile1) {
            int x = tile0.getLocation()[0];
            int y = tile0.getLocation()[1];
            int x1 = tile1.getLocation()[0];
            int y1 = tile1.getLocation()[1];
            return computeDirection(x, y, x1, y1);
        }
        public Direction computeDirection(Piece piece0, Piece piece1) {
            int x = piece1.getLocation()[0];
            int y = piece1.getLocation()[1];
            int x1 = piece0.getLocation()[0];
            int y1 = piece0.getLocation()[1];
            return computeDirection(x, y, x1, y1);
        }
        public Direction computeDirection(int x, int y, int x1, int y1) {
            if(x > x1) {
                return Direction.EAST;
            }
            if (x < x1) {
                return Direction.WEST;
            }
            if(y > y1) {
                return Direction.SOUTH;
            }
            if (y < y1) {
                return Direction.NORTH;
            }
            return null;
        }
        public boolean performAction() {
            if (activePiece.hasMoved()) {
                return false;
            }
            for (Tile tile : moveTiles) {
                if (lastClickedTile.getLocation()[0] == tile.getLocation()[0] && lastClickedTile.getLocation()[1] == tile.getLocation()[1]) {
                    move(activePiece, lastClickedTile);
                    return true;
                }
            }
            for (Tile tile : attackTiles) {
                if (lastClickedTile.getLocation()[0] == tile.getLocation()[0] && lastClickedTile.getLocation()[1] == tile.getLocation()[1]) {
                    if (tile.getPiece().getTeam() != activePiece.getTeam()) {
                        ArrayList<Object> result = attack(activePiece, tile.getPiece());
                        gameView.getRightPanel().getInfoPanel().setAttackResult(activePiece, (Piece) result.get(0), (int) result.get(1), (boolean) result.get(2), (boolean) result.get(3));

                    }
                    else {
                        move(activePiece, lastClickedTile);
                    }
                    return true;
                }
            }
            return false;
        }
        public void getActivePieceActions() {
            moveTiles = boardLogic.getMoveTiles(activePiece.getLocation(), activePiece.getMovementRange(), activePiece.getDirection());
            attackTiles = boardLogic.getAttackTiles(activePiece.getLocation(), activePiece.getAttackRange(), activePiece.getDirection());
        }
        public ArrayList<Tile> getMoveTiles(int[] location, int range, Direction direction) {
                if (direction != null) {
//                if (boardController.getBoard().getTile(location[0], location[1]).getPiece().getType() == PieceType.FRIGATE) {
//                    ArrayList<Tile> moveTiles = getMoveTilesSquareRange(location, 1);
//                    moveTiles.addAll(getMoveTilesLineRange(location, range));
//                    return moveTiles;
//                }
                return getMoveTilesLineRange(location, range);
            }
            else {
                return getMoveTilesSquareRange(location, range);
            }
        }
        public ArrayList<Tile> getAttackTiles(int[] location, int range, Direction direction) {
            if (direction != null) {
                return getAttackTilesLineRange(location, range);
            }
            else {
                return getAttackTilesSquareRange(location, range);
            }
        }
        public ArrayList<Tile> getMoveTilesLineRange(int[] location, int range ) {
            int x = location[0];
            int y = location[1];
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int i = 0; i > -range; i--) {//north
                y = y - 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece() == null) {
                        tiles.add(boardController.getBoard().getTile(x, y));
                    }
                    else if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                        break;
                    }
                }
            }
            x = location[0];
            y = location[1];
            for (int i = 0; i < range; i++) {//east
                x = x + 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece() == null) {
                        tiles.add(boardController.getBoard().getTile(x, y));
                    }
                    else if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                        break;
                    }
                }
            }
            x = location[0];
            y = location[1];
            for (int i = 0; i < range; i++) {//south
                y = y + 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece() == null) {
                        tiles.add(boardController.getBoard().getTile(x, y));
                    }
                    else if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                        break;
                    }
                }
            }
            x = location[0];
            y = location[1];
            for (int i = 0; i > -range; i--) {//west
                x = x - 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece() == null) {
                        tiles.add(boardController.getBoard().getTile(x, y));
                    }
                    else if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                        break;
                    }
                }
            }
            return tiles;
        }

        public ArrayList<Tile> getMoveTilesSquareRange(int[] location, int range) {
            int X = location[0];
            int Y = location[1];
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int i = -range; i <= range; i++) {
                for (int j = -range; j <= range; j++) {
                    int x = X + i;
                    int y = Y + j;
                    // Checks the agent isn't looking outside the grid, at its current tile or at terrain.
                    if (((x < boardController.getSize())
                            && (y < boardController.getSize()))
                            && ((x >= 0) && (y >= 0))
                            && !(i == 0 && j == 0)) {
                        if (boardController.getBoard().getTile(x, y).getPiece() == null) {
                            tiles.add(boardController.getBoard().getTile(x, y));
                        }
                    }
                }
            }
            return tiles;
        }

        public ArrayList<Tile> getAttackTilesLineRange(int[] location, int range) {
            int x = location[0];
            int y = location[1];
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int i = 0; i > -range; i--) {//north
                y = y - 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece()!= null) {
                        if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                            tiles.add(boardController.getBoard().getTile(x, y));
                            break;
                        }
                    }
                }
            }
            x = location[0];
            y = location[1];
            for (int i = 0; i < range; i++) {//east
                x = x + 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece()!= null) {
                        if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                            tiles.add(boardController.getBoard().getTile(x, y));
                            break;
                        }
                    }
                }
            }
            x = location[0];
            y = location[1];
            for (int i = 0; i < range; i++) {//south
                y = y + 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece()!= null) {
                        if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                            tiles.add(boardController.getBoard().getTile(x, y));
                            break;
                        }
                    }
                }
            }
            x = location[0];
            y = location[1];
            for (int i = 0; i > -range; i--) {//west
                x = x - 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece()!= null) {
                        if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                            tiles.add(boardController.getBoard().getTile(x, y));
                            break;
                        }
                    }
                }
            }
            return tiles;
        }

        public ArrayList<Tile> getAttackTilesSquareRange(int[] location, int range) {
            int X = location[0];
            int Y = location[1];
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int i = -range; i <= range; i++) {
                for (int j = -range; j <= range; j++) {
                    int x = X + i;
                    int y = Y + j;
                    // Checks the agent isn't looking outside the grid, at its current tile or at terrain.
                    if (((x < boardController.getSize())
                            && (y < boardController.getSize()))
                            && ((x >= 0) && (y >= 0))
                            && !(i == 0 && j == 0)) {
                        if (boardController.getBoard().getTile(x, y).getPiece()!= null) {
                            if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                                tiles.add(boardController.getBoard().getTile(x, y));
                            }
                        }
                    }
                }
            }
            return tiles;
        }
        public ArrayList<Tile> getPotentialAttackTilesLineRange(int[] location, int range ) {
            int x = location[0];
            int y = location[1];
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int i = 0; i > -range; i--) {//north
                y = y - 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                        break;
                    }
                }
            }
            x = location[0];
            y = location[1];
            for (int i = 0; i < range; i++) {//east
                x = x + 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                        break;
                    }
                }
            }
            x = location[0];
            y = location[1];
            for (int i = 0; i < range; i++) {//south
                y = y + 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                        break;
                    }
                }
            }
            x = location[0];
            y = location[1];
            for (int i = 0; i > -range; i--) {//west
                x = x - 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                        break;
                    }
                }
            }
            return tiles;
        }


    }

    // Groups methods used to display the board as an image
    public class BoardImage {

        private HashMap<SpriteTags, BufferedImage> sprites = new HashMap<>();
        private final BufferedImage blankBoardImage;
        private BufferedImage lastBoardImage;

        public BoardImage() throws IOException {
            loadSprites();
            blankBoardImage = blankBoard();
            lastBoardImage = BoardImage.deepCopy(blankBoardImage);
        }

        public void displayActions() {
            lastBoardImage = BoardImage.deepCopy(blankBoardImage);
            gameView.updateBoard(boardImage.getBoardImage());
            boardImage.displayMovementRange(moveTiles);
            boardImage.displayAttackRange(attackTiles);
            gameView.updateBoard(boardImage.getBoardImage());
        }

        public void runActionPointResetAnimation(int ap) throws InterruptedException {
            gameView.getRightPanel().resetActionPointAnimation(ap);
        }

        public void updateBoard() {
            lastBoardImage = BoardImage.deepCopy(blankBoardImage);
            gameView.updateBoard(boardImage.getBoardImage());
            try {
                File outputfile = new File("turns\\" + System.currentTimeMillis() + ".png");
                ImageIO.write(boardImage.getBoardImage(), "png", outputfile);
            }
            catch (IOException ex) {
            }
            gameView.getRightPanel().updateActionPointsRemaining(actionPoints);
        }
        public BufferedImage getBoardImage() {
            int size = boardController.getSize();
            int scale = boardController.getScale();
            for (int x = 0; x <= scale * size; x += scale) {
                for (int y = 0; y <= scale * size; y += scale) {
                    if (((x < scale * size) && (y < scale * size)) && ((x >= 0) && (y >= 0))) {
                        if (boardController.getBoard().getTile(x / scale, y / scale).getPiece() != null) {
                            for (int i = 0; i < scale; i++) {
                                for (int j = 0; j < scale; j++) {
                                    if (((x + i < scale * size) && (y + j < scale * size)) && ((x + i >= 0) && (y + j >= 0))) {
                                        Tile tile = boardController.getBoard().getTile(x / scale, y / scale);
                                        Color rgba = new Color(sprites.get(getPieceSpriteTag(tile.getPiece())).getRGB(i, j), true);
                                        if (rgba.getAlpha() == 255) {
                                            if (tile.getPiece().getDamaged()) {
                                                if (i % 3 == 0 || j % 2 == 0) {
                                                    lastBoardImage.setRGB(x + i, y + j, rgba.getRGB());
                                                }
                                            }
                                            else {
                                                lastBoardImage.setRGB(x + i, y + j, rgba.getRGB());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return lastBoardImage;
        }

        public void displayMovementRange(ArrayList<Tile> tiles) {
            int size = boardController.getSize();
            int scale = boardController.getScale();
            for (Tile tile : tiles) {
                int x = tile.getLocation()[0];
                int y = tile.getLocation()[1];
                    for (int i = 0; i < scale; i++) {
                        for (int j = 0; j < scale; j++) {
                            if (((x + i < scale * size) && (y + j < scale * size)) && ((x + i >= 0) && (y + j >= 0))) {
                                if (tile.getColor() == boardController.getBoard().getDarkColor()) {
                                    lastBoardImage.setRGB((x * scale) + i, (y * scale) + j, darkMoveColor.getRGB());
                                }
                                else {
                                    lastBoardImage.setRGB((x * scale) + i, (y * scale) + j, lightMoveColor.getRGB());
                                }
                            }
                        }
                    }
                }
        }

        public void displayAttackRange(ArrayList<Tile> tiles) {
            int size = boardController.getSize();
            int scale = boardController.getScale();
            for (Tile tile : tiles) {
                int x = tile.getLocation()[0];
                int y = tile.getLocation()[1];
                if (true) {
                    for (int i = 0; i < scale; i++) {
                        for (int j = 0; j < scale; j++) {
                            if (((x + i < scale * size) && (y + j < scale * size)) && ((x + i >= 0) && (y + j >= 0))) {
                                Color rgba = new Color(sprites.get(getPieceSpriteTag(tile.getPiece())).getRGB(i, j), true);
                                if (rgba.getAlpha() != 255) {
                                    if (tile.getColor() == boardController.getBoard().getDarkColor()) {
                                        lastBoardImage.setRGB((x * scale) + i, (y * scale) + j, darkAttackColor.getRGB());
                                    }
                                    else {
                                        lastBoardImage.setRGB((x * scale) + i, (y * scale) + j, lightAttackColor.getRGB());
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        public void updateTiles(ArrayList<Tile> tiles) {
            int size = boardController.getSize();
            int scale = boardController.getScale();
            for (Tile tile : tiles) {
                int x = tile.getLocation()[0];
                int y = tile.getLocation()[1];
                if (tile.getPiece() != null) {
                    for (int i = 0; i < scale; i++) {
                        for (int j = 0; j < scale; j++) {
                            if (((x + i < scale * size) && (y + j < scale * size)) && ((x + i >= 0) && (y + j >= 0))) {
                                Color rgba = new Color(sprites.get(getPieceSpriteTag(tile.getPiece())).getRGB(i, j), true);
                                if (rgba.getAlpha() == 255) {
                                    lastBoardImage.setRGB(x + i, y + j, rgba.getRGB());
                                }

                            }
                        }
                    }
                }
            }
        }

        public BufferedImage blankBoard() {
            int size = boardController.getSize();
            int scale = boardController.getScale();
            BufferedImage boardImage = new BufferedImage(size * scale, size * scale, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x <= scale * size; x += scale) {
                for (int y = 0; y <= scale * size; y += scale) {
                    for (int i = 0; i < scale; i++) {
                        for (int j = 0; j < scale; j++) {
                            if (((x + i < scale * size) && (y + j < scale * size)) && ((x + i >= 0) && (y + j >= 0))) {
                                boardImage.setRGB(x + i, y + j, boardController.getBoard().getTile(x / scale, y / scale).getColor().getRGB());
                            }
                        }
                    }
                }
            }
            return boardImage;
        }

        public void loadSprites() throws IOException {
            for (SpriteTags spriteTag: SpriteTags.values()) {
                sprites.put(spriteTag, ImageIO.read((new File(spritePathMap(spriteTag)).toURI()).toURL()));
            }

        }

        public String spritePathMap(SpriteTags spriteTag) {
            switch (spriteTag) {
                case WHITECORVETTE -> {
                    return "images\\corvette_white.png";
                }
                case WHITEFRIGATENORTH -> {
                    return "images\\frigate_white_north.png";
                }
                case WHITEFRIGATEEAST -> {
                    return "images\\frigate_white_east.png";
                }
                case WHITEFRIGATESOUTH -> {
                    return "images\\frigate_white_south.png";
                }
                case WHITEFRIGATEWEST -> {
                    return "images\\frigate_white_west.png";
                }
                case WHITEDESTROYER -> {
                    return "images\\destroyer_white.png";
                }
                case WHITECRUISERNORTH -> {
                    return "images\\cruiser_white_north.png";
                }
                case WHITECRUISEREAST -> {
                    return "images\\cruiser_white_east.png";
                }
                case WHITECRUISERSOUTH -> {
                    return "images\\cruiser_white_south.png";
                }
                case WHITECRUISERWEST -> {
                    return "images\\cruiser_white_west.png";
                }
                case WHITEBATTLESHIP -> {
                    return "images\\battleship_white.png";
                }
                case WHITECARRIER -> {
                    return "images\\carrier_white.png";
                }
                case BLACKCORVETTE -> {
                    return "images\\corvette_black.png";
                }
                case BLACKFRIGATENORTH -> {
                    return "images\\frigate_black_north.png";
                }
                case BLACKFRIGATEEAST -> {
                    return "images\\frigate_black_east.png";
                }
                case BLACKFRIGATESOUTH -> {
                    return "images\\frigate_black_south.png";
                }
                case BLACKFRIGATEWEST -> {
                    return "images\\frigate_black_west.png";
                }
                case BLACKDESTROYER -> {
                    return "images\\destroyer_black.png";
                }
                case BLACKCRUISERNORTH -> {
                    return "images\\cruiser_black_north.png";
                }
                case BLACKCRUISEREAST -> {
                    return "images\\cruiser_black_east.png";
                }
                case BLACKCRUISERSOUTH -> {
                    return "images\\cruiser_black_south.png";
                }
                case BLACKCRUISERWEST -> {
                    return "images\\cruiser_black_west.png";
                }
                case BLACKBATTLESHIP -> {
                    return "images\\battleship_black.png";
                }
                case BLACKCARRIER -> {
                    return "images\\carrier_black.png";
                }
            }
            return null;
        }

        public SpriteTags getPieceSpriteTag(Piece piece) {
            if (piece.getTeam() == Team.BLACK) {
                switch (piece.getType()) {
                    case CORVETTE -> {
                        return SpriteTags.BLACKCORVETTE;
                    }
                    case FRIGATE -> {
                        switch (piece.getDirection()) {
                            case NORTH -> {
                                return SpriteTags.BLACKFRIGATENORTH;

                            }
                            case EAST -> {
                                return SpriteTags.BLACKFRIGATEEAST;

                            }
                            case SOUTH -> {
                                return SpriteTags.BLACKFRIGATESOUTH;

                            }
                            case WEST -> {
                                return SpriteTags.BLACKFRIGATEWEST;
                            }
                        }
                    }
                    case DESTROYER -> {
                        return SpriteTags.BLACKDESTROYER;
                    }
                    case CRUISER -> {
                        switch (piece.getDirection()) {
                            case NORTH -> {
                                return SpriteTags.BLACKCRUISERNORTH;

                            }
                            case EAST -> {
                                return SpriteTags.BLACKCRUISEREAST;

                            }
                            case SOUTH -> {
                                return SpriteTags.BLACKCRUISERSOUTH;

                            }
                            case WEST -> {
                                return SpriteTags.BLACKCRUISERWEST;
                            }
                        }
                    }
                    case BATTLESHIP -> {
                        return SpriteTags.BLACKBATTLESHIP;
                    }
                    case CARRIER -> {
                        return SpriteTags.BLACKCARRIER;
                    }
                }
            }
            else {
                switch (piece.getType()) {
                    case CORVETTE -> {
                        return SpriteTags.WHITECORVETTE;
                    }
                    case FRIGATE -> {
                        switch (piece.getDirection()) {
                            case NORTH -> {
                                return SpriteTags.WHITEFRIGATENORTH;

                            }
                            case EAST -> {
                                return SpriteTags.WHITEFRIGATEEAST;

                            }
                            case SOUTH -> {
                                return SpriteTags.WHITEFRIGATESOUTH;

                            }
                            case WEST -> {
                                return SpriteTags.WHITEFRIGATEWEST;
                            }
                        }
                    }
                    case DESTROYER -> {
                        return SpriteTags.WHITEDESTROYER;
                    }
                    case CRUISER -> {
                        switch (piece.getDirection()) {
                            case NORTH -> {
                                return SpriteTags.WHITECRUISERNORTH;

                            }
                            case EAST -> {
                                return SpriteTags.WHITECRUISEREAST;

                            }
                            case SOUTH -> {
                                return SpriteTags.WHITECRUISERSOUTH;

                            }
                            case WEST -> {
                                return SpriteTags.WHITECRUISERWEST;
                            }
                        }
                    }
                    case BATTLESHIP -> {
                        return SpriteTags.WHITEBATTLESHIP;
                    }
                    case CARRIER -> {
                        return SpriteTags.WHITECARRIER;
                    }
                }
            }
            return null;
        }

        static BufferedImage deepCopy(BufferedImage bufferedImage) {
            ColorModel cm = bufferedImage.getColorModel();
            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
            WritableRaster raster = bufferedImage.copyData(null);
            return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        }
    }
}