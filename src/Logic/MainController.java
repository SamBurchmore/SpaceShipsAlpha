package Logic;

import Data.*;
import gui.GameView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
//import com.kong.unirest;


public class MainController {
    private final GameView gameView;
    private final BoardController boardController;
    private final BoardImage boardImage;
    private final BoardLogic boardLogic;
    private final PlayLogic playLogic;
    private Tile lastClickedTile;
    private Piece activePiece;
    private int turnCost;
    private Team activeTeam;
    private ArrayList<Tile> moveTiles;
    private ArrayList<Tile> attackTiles;
    private ArrayList<Tile> attackRangeTiles;
    private HashMap<Tile, ArrayList<Tile>> teamMemberTiles;
    private HashMap<Tile, ArrayList<Tile>> attackingTeamMembers;
    public GameState gameState;
    public int maxActionPoints = 3;
    public int actionPoints = maxActionPoints;
    public int attackCost;
    public ArrayList<Piece> movedPieces;
    public Board miniBoard;
    public JDialog loadingDialog;


    private Color lightMoveColor = new Color(122, 122, 222);
    private Color darkMoveColor = new Color(14, 14, 94);
    private Color darkAttackColor = new Color(150, 0, 0);
    private Color lightAttackColor = new Color(200, 0, 0);
//    private Color lightTeamMemberColor = new Color(230, 230, 0);
//    private Color darkTeamMemberColor = new Color(200, 200, 0);
private Color lightTeamMemberColor = new Color(0, 255, 0);
    private Color darkTeamMemberColor = new Color(0, 200, 0);
//    private Color lightAttackingTeamMemberColor = new Color(200, 0, 200);
//    private Color darkAttackingTeamMemberColor = new Color(150, 0, 150);
private Color lightAttackingTeamMemberColor = new Color(0, 255, 255);
    private Color darkAttackingTeamMemberColor = new Color(0, 200, 200);
//    private Color lightTileColor = new Color(50, 200, 20);
//    private Color darkTileColor = new Color(30, 150, 0);
private Color lightTileColor = new Color(200, 200, 200);
    private Color darkTileColor = new Color(70, 70, 70);

    public Color[] actionColors = new Color[]{lightAttackColor, darkAttackColor, lightMoveColor, darkMoveColor, lightTeamMemberColor, darkTeamMemberColor, lightAttackingTeamMemberColor, darkAttackingTeamMemberColor};
    Map<String, Color> tileColorMap = Map.of(
            "light_tile", lightTileColor,
            "dark_tile", darkTileColor,
            "light_attack", lightAttackColor,
            "dark_attack", darkAttackColor,
            "light_move", lightMoveColor,
            "dark_move", darkMoveColor,
            "light_team", lightTeamMemberColor,
            "dark_team", darkTeamMemberColor,
            "light_team_attack", lightAttackingTeamMemberColor,
            "dark_team_attack", darkAttackingTeamMemberColor
    );


    public MainController() throws IOException {
        showLoadingDialog();
        movedPieces = new ArrayList<>();
        miniBoard = new Board(lightTileColor, darkTileColor, 13);
        boardController  = new BoardController(lightTileColor, darkTileColor);
        boardController.getBoard().setDarkColor(darkTileColor);
        boardController.getBoard().setLightColor(lightTileColor);
        boardLogic = new BoardLogic();
        boardImage = new BoardImage();
        gameView = new GameView(boardImage.getMiniImage(13));
        playLogic = new PlayLogic();
        moveTiles = new ArrayList<>();
        attackTiles = new ArrayList<>();
        teamMemberTiles = new HashMap<>();
        attackingTeamMembers = new HashMap<>();
        turnCost = 0;
        attackCost = 0;
        boardImage.updateBoard();
        gameView.setVisible(true);
        gameView.getGameFrame().addMouseListener(playLogic);
        activeTeam = Team.BLACK;
        gameView.updateTurn(activeTeam);
        deleteLoadingDialog();

        debugMode();
    }

    public GameView getGameView() {
        return gameView;
    }

    public BoardController getBoardController() {
        return boardController;
    }

    public BoardImage getBoardImage() {
        return boardImage;
    }

    public BoardLogic getBoardLogic() {
        return boardLogic;
    }

    public PlayLogic getPlayLogic() {
        return playLogic;
    }

    private void debugMode() {
        int X = 0;
        int Y = 0;
        int range = boardController.getSize();
        for (int i = 0; i <= range; i++) {
            for (int j = 0; j <= range; j++) {
                int x = X + i;
                int y = Y + j;
                // Checks the agent isn't looking outside the grid, at its current tile or at terrain.
                if (((x < range)
                        && (y < range))
                        && ((x >= 0) && (y >= 0))
                        && !(i == 0 && j == 0)) {
                    if (boardController.getBoard().getTile(x, y).getPiece() != null) {
                        boardController.getBoard().getTile(x, y).getPiece().setAttackRange(11);
                    }
                }
            }
        }
    }

    private void showLoadingDialog() throws MalformedURLException {
        loadingDialog = new JDialog();
        JLabel loadingIcon = new JLabel();
        loadingIcon.setIcon(new ImageIcon(new File("images\\load_image.png").toURI().toURL()));
        loadingDialog.add(loadingIcon);
        loadingDialog.setUndecorated(true);
        loadingDialog.pack();
        loadingDialog.setLocationRelativeTo(null);
        loadingDialog.setVisible(true);
    }

    private void deleteLoadingDialog() {
        loadingDialog.dispose();
    }


    // Groups methods which allow the game to be played .i.e control the game state, listen for input, send it to BoardLogic, update BoardImage and send the image to gui.GameView
    public class PlayLogic implements java.awt.event.MouseListener {

        public PlayLogic() {
            gameState = GameState.WAITING_INPUT;
        }
        public String setActivePiece() throws IOException {
            if (lastClickedTile.getPiece() != null && !lastClickedTile.getPiece().hasMoved()) {
                if (lastClickedTile.getPiece().getActionCost() <= actionPoints) {
                    if (lastClickedTile.getPiece().getTeam() == activeTeam) {
                        activePiece = lastClickedTile.getPiece();
                        gameView.getRightPanel().getInfoPanel().setInfoPanelText(activePiece);
                        gameView.getRightPanel().updateSelectedPiece(activePiece, boardImage.getPieceSpriteTag(activePiece));
                        gameView.getRightPanel().updateSelectedPieceRange(activePiece);
                        return "general_action";
                    }
                }
                else if (lastClickedTile.getPiece().getAttackCost() <= actionPoints){
                    if (lastClickedTile.getPiece().getTeam() == activeTeam) {
                        activePiece = lastClickedTile.getPiece();
                        gameView.getRightPanel().getInfoPanel().setInfoPanelText(activePiece);
                        gameView.getRightPanel().updateSelectedPiece(activePiece, boardImage.getPieceSpriteTag(activePiece));
                        gameView.getRightPanel().updateSelectedPieceRange(activePiece);
                        return "attack";
                    }
                }
            }
            return "no_action";
        }

        

        public void newTurn() throws IOException, InterruptedException {
            for (Piece piece: movedPieces) {
                piece.setHasMoved(false);
            }
            movedPieces.clear();
            attackingTeamMembers.clear();
            actionPoints = maxActionPoints;
            if (activeTeam == Team.BLACK) {
                activeTeam = Team.WHITE;
            }
            else {
                activeTeam = Team.BLACK;
            }
            gameView.updateTurn(activeTeam);
            gameView.getRightPanel().clearPieceImages();
            activePiece = null;
            gameState = GameState.WAITING_INPUT;
            gameView.getRightPanel().getInfoPanel().clearInfoPanelText();
            gameView.getRightPanel().clearPieceImages();
            boardImage.updateBoard();
            System.out.println("new turn");
            turnCost = 0;
            attackCost = 0;
        }

        public void resetTurn() throws IOException {
            activePiece = null;
            gameState = GameState.WAITING_INPUT;
            for (Tile tile : attackingTeamMembers.keySet()) {
                tile.getPiece().setSelected(false);
            }
            attackingTeamMembers.clear();
            turnCost = 0;
            attackCost = 0;
            gameView.getRightPanel().getInfoPanel().clearInfoPanelText();
            gameView.getRightPanel().clearPieceImages();
            boardImage.updateBoard();
        }

        public void runGame() throws IOException, InterruptedException {
            gameView.getRightPanel().getInfoPanel().clearInfoPanelText();
            gameView.getRightPanel().clearPieceImages();
            System.out.println(gameState);
            playLogic.getPointer(); // Get the mouse location
                if (gameState == GameState.WAITING_INPUT) { // If game is waiting for input, try to select a piece and display its actions
                    String moveAvailable = setActivePiece(); // Try to set the active piece
                    if (!Objects.equals(moveAvailable, "no_action")) {
                    turnCost = activePiece.getActionCost();
                    attackCost = activePiece.getAttackCost();
                    gameState = GameState.SINGLE_PIECE_SELECTED;
                        if (Objects.equals(moveAvailable, "general_action")) {
                            boardLogic.getActivePieceActions(); // Retrieve the active pieces potential actions
                            boardImage.displayActions(); // Display the active pieces potential actions
                        } else if (Objects.equals(moveAvailable, "attack")) {
                            boardLogic.getActivePieceAttackActions();
                            boardImage.displayAttackActions();
                        }
                        gameView.getRightPanel().getMiniImage().updateImage(boardImage.displayMiniAttackRange(attackRangeTiles, boardImage.getMiniImage(13)));
                    }
                }
                else if (activePiece == lastClickedTile.getPiece()) { // If player deselects active piece, reset game waiting state
                    resetTurn();
                }
                else if (lastClickedTile.getPiece() != null) { // Check if player is selecting another piece
                    if (lastClickedTile.getPiece().getTeam() == activeTeam) {
                        if (!teamMemberTiles.isEmpty() && gameState != GameState.MULTIPLE_PIECES_SELECTED) {
                            selectAdditionalPiece();
                            if (gameState == GameState.SELECTING_MORE_PIECES) {
                                boardImage.displayMultiSelect();
                            }
                            else {
                                boardImage.displayMultiAttack();
                            }
                        }
                        else {
                            resetTurn();
                        }
                    }
                }
                if (gameState == GameState.MULTIPLE_PIECES_SELECTED || gameState == GameState.SELECTING_MORE_PIECES) { // Check if player is making a multi attack
                    String  actionPerformed = boardLogic.performMultiAttack();
                    if (Objects.equals(actionPerformed, "multi_attack")) {
                        System.out.println("multi attack performed");
                        activePiece.setHasMoved(true);
                        for (Tile teamMember : attackingTeamMembers.keySet()) {
                            movedPieces.add(teamMember.getPiece());
                            teamMember.getPiece().setHasMoved(true);
                        }
                        actionPoints -= attackCost;
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
                if (gameState == GameState.SINGLE_PIECE_SELECTED) { // Check if player is moving a piece or performing a single attack
                    String actionPerformed = boardLogic.performAction();
                    if (Objects.equals(actionPerformed, "move")) {
                        System.out.println("piece moved");
                        activePiece.setHasMoved(true);
                        movedPieces.add(activePiece);
                        actionPoints -= turnCost;
                        if (actionPoints <= 0) { // If the player is out of action points, start a new turn
                            newTurn();
                        }
                        else {
                            resetTurn();
                        }
                    }
                    else if (actionPerformed.equals("attack")) {
                        actionPoints -= attackCost;
                        if (actionPoints <= 0) { // If the player is out of action points, start a new turn
                            newTurn();
                        }
                        else {
                            resetTurn();
                        }
                    }

                }
                System.out.println(gameState);
        }

        public void selectAdditionalPiece() {
            for (Tile teamMember : teamMemberTiles.keySet()) {
                if (lastClickedTile.getPiece() == teamMember.getPiece() && !lastClickedTile.getPiece().hasMoved() && !lastClickedTile.getPiece().isSelected()) {
                    if (gameState == GameState.SINGLE_PIECE_SELECTED) {
                        attackingTeamMembers.put(boardController.getBoard().getTile(activePiece.getLocation()[0], activePiece.getLocation()[1]), attackTiles);
                    }
                    moveTiles = new ArrayList<>();
                    attackingTeamMembers.put(teamMember, teamMemberTiles.get(teamMember));  // Add new piece to attacking list
                    teamMember.getPiece().setSelected(true);
                    turnCost += teamMember.getPiece().getAttackCost();
                    attackCost += teamMember.getPiece().getAttackCost();
                    updateTeamMembersInRange(teamMember);
                    if (attackCost < actionPoints) {
                        System.out.println("attack cost: " + attackCost);
                        gameState = GameState.SELECTING_MORE_PIECES;
                    }
                    else {
                        gameState = GameState.MULTIPLE_PIECES_SELECTED;
                        teamMemberTiles.clear();
                    }
                }
            }
        }

        public void updateTeamMembersInRange(Tile teamMember) {
            ArrayList<Tile> newAttackTiles = new ArrayList<>();
            HashMap<Tile, ArrayList<Tile>> newTeamMemberTiles = new HashMap<>();
            for (Tile enemyTile : attackingTeamMembers.get(teamMember)) { // Remove enemy tiles not in range of the new attacker
                for (Tile attackTile : attackTiles) {
                    if (attackTile.getPiece() == enemyTile.getPiece()) {
                        newAttackTiles.add(attackTile);
                    }
                }
            }
            for (Tile teamMemberTile : teamMemberTiles.keySet()) {
                for (Tile enemyTile : teamMemberTiles.get(teamMemberTile)) { // Add team members  in range of any enemies in the new attackers range and don't cost to many action points
                    for (Tile currentTeamMemberAttackTile : teamMemberTiles.get(teamMember)) {
                        if (currentTeamMemberAttackTile.getPiece() == enemyTile.getPiece() && teamMemberTile.getPiece().getAttackCost() < turnCost) {
                            newTeamMemberTiles.put(teamMemberTile, teamMemberTiles.get(teamMemberTile));
                            //System.out.println(teamMemberTile.getPiece().getType() + " " + turnCost + " " + teamMemberTile.getPiece().getAttackCost());
                        }
                    }
                }
            }
            System.out.println(turnCost);

            teamMemberTiles = newTeamMemberTiles;
            attackTiles = newAttackTiles;
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

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e))
            {
                try {
                    resetTurn();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
                if (attacker.getAttackType() == AttackType.ROCKET || attacker.getAttackType() == AttackType.STRIKE) {
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
        public ArrayList<Object> multiAttack(Set<Tile> attackers, Piece defender) {
            Random random = new Random();
            int attackScore = 0;
            ArrayList<Integer> attackScores = new ArrayList<>();
            for (Tile attacker : attackers) {
                int currentAttackScore;
                if (attacker.getPiece().getType() == PieceType.FRIGATE) {
                    currentAttackScore = 1 + random.nextInt(Math.max(defender.getArmour(), 2));;
                }
                else {
                    currentAttackScore = 1 + random.nextInt(attacker.getPiece().getAttack());
                }
                attackScore += currentAttackScore;
                attackScores.add(currentAttackScore);
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
                result.add(attackScores);
                result.add(attackScore);
                result.add(true);
                result.add(false);
                return result;
            }
            if (!defender.getDamaged()) {
                for (Tile attacker : attackers) {
                    if (attacker.getPiece().getAttackType() == AttackType.ROCKET || attacker.getPiece().getAttackType() == AttackType.STRIKE) {
                        if (attackScore >= defender.getArmour() / 2) {
                            defender.setDamaged(true);
                            result.add(defender);
                            result.add(attackScores);
                            result.add(attackScore);
                            result.add(true);
                            result.add(false);
                            return result;
                        }
                    }
                }
            }
            result.add(defender);
            result.add(attackScores);
            result.add(attackScore);
            result.add(false);
            result.add(false);
            return result;
        }
        public ArrayList<Object> multiAttack2(Set<Tile> attackers, Piece defender) {
            int attackScore = 0;
            for (Tile attacker : attackers) {
                attackScore += attacker.getPiece().getAttack();

            }
            ArrayList<Object> result = new ArrayList<>();
            int armour = defender.getArmour();
            System.out.println("attack score: " + attackScore + " armour: " + armour);
            if (attackScore >= armour) {
                boardController.getBoard().setTile(defender.getLocation()[0], defender.getLocation()[1], null);
                result.add(defender);
                result.add(attackScore);
                result.add(true);
                result.add(false);
                return result;
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
            if(y > y1) {
                return Direction.SOUTH;
            }
            if (y < y1) {
                return Direction.NORTH;
            }
            if(x > x1) {
                return Direction.EAST;
            }
            if (x < x1) {
                return Direction.WEST;
            }
            return null;
        }
        public String performAction() {
            if (activePiece.hasMoved()) { // If piece has already been moved this turn, do nothing and return false
                return "no_action";
            }
            for (Tile tile : moveTiles) { // Iterate over potential move tiles, if one matches the last clicked tile, run move() and return true
                if (lastClickedTile.getLocation()[0] == tile.getLocation()[0] && lastClickedTile.getLocation()[1] == tile.getLocation()[1]) {
                    move(activePiece, lastClickedTile);
                    return "move";
                }
            }
            for (Tile tile : attackTiles) { // Iterate over potential attack tiles, if one matches the last clicked tile, run attack() and return true
                if (lastClickedTile.getLocation()[0] == tile.getLocation()[0] && lastClickedTile.getLocation()[1] == tile.getLocation()[1]) {
                    if (tile.getPiece().getTeam() != activePiece.getTeam()) {
                        ArrayList<Object> result = attack(activePiece, tile.getPiece());
                        gameView.getRightPanel().getInfoPanel().setAttackResult(activePiece, (Piece) result.get(0), (int) result.get(1), (boolean) result.get(2), (boolean) result.get(3));
                        return "attack";
                    }
                    else {
                        move(activePiece, lastClickedTile);
                        return "move";
                    }
                }
            }
            return "no_action";
        }
        public boolean performAttack() {
            if (activePiece.hasMoved()) { // If piece has already been moved this turn, do nothing and return false
                return false;
            }
            for (Tile tile : attackTiles) { // Iterate over potential attack tiles, if one matches the last clicked tile, run attack() and return true
                if (lastClickedTile.getLocation()[0] == tile.getLocation()[0] && lastClickedTile.getLocation()[1] == tile.getLocation()[1]) {
                    if (tile.getPiece().getTeam() != activePiece.getTeam()) {
                        ArrayList<Object> result = attack(activePiece, tile.getPiece());
                        gameView.getRightPanel().getInfoPanel().setAttackResult(activePiece, (Piece) result.get(0), (int) result.get(1), (boolean) result.get(2), (boolean) result.get(3));
                    }
                    return true;
                }
            }
            return false;
        }
        public String performMultiAttack() {
            if (activePiece.hasMoved()) { // If piece has already been moved this turn, do nothing and return false
                return "no_action";
            }
            for (Tile tile : attackTiles) { // Iterate over potential attack tiles, if one matches the last clicked tile, run attack() and return true
                if (lastClickedTile.getLocation()[0] == tile.getLocation()[0] && lastClickedTile.getLocation()[1] == tile.getLocation()[1]) {
                    if (tile.getPiece().getTeam() != activePiece.getTeam()) {
                        ArrayList<Object> result = multiAttack(attackingTeamMembers.keySet(), tile.getPiece());
                        gameView.getRightPanel().getInfoPanel().setAttackResult(activePiece, (Piece) result.get(0), (ArrayList<Integer>) result.get(1), (int) result.get(2),(boolean) result.get(3), (boolean) result.get(4));
                    }
                    return "multi_attack";
                }
            }
            return "no_action";
        }
        public boolean performMove() {
            if (activePiece.hasMoved()) { // If piece has already been moved this turn, do nothing and return false
                return false;
            }
            for (Tile tile : moveTiles) { // Iterate over potential move tiles, if one matches the last clicked tile, run move() and return true
                if (lastClickedTile.getLocation()[0] == tile.getLocation()[0] && lastClickedTile.getLocation()[1] == tile.getLocation()[1]) {
                    move(activePiece, lastClickedTile);
                    return true;
                }
            }
            return false;
        }
        public void getActivePieceActions() {
            moveTiles = boardLogic.getMoveTiles(activePiece.getLocation(), activePiece.getMovementRange(), activePiece.getDirection());
            attackTiles = boardLogic.getAttackTiles(activePiece.getLocation(), activePiece.getAttackRange(), activePiece.getDirection());
            teamMemberTiles = boardLogic.getTeamMembersInAttackRangeOfTarget(activeTeam, attackTiles, activePiece);
            attackRangeTiles = boardLogic.getAttackRangeTiles(activePiece.getAttackRange(), activePiece.getDirection());
        }
        public void getActivePieceAttackActions() {
            moveTiles.clear();
            attackTiles = boardLogic.getAttackTiles(activePiece.getLocation(), activePiece.getAttackRange(), activePiece.getDirection());
            teamMemberTiles = boardLogic.getTeamMembersInAttackRangeOfTarget(activeTeam, attackTiles, activePiece);
            attackRangeTiles = boardLogic.getAttackRangeTiles(activePiece.getAttackRange(), activePiece.getDirection());
        }
        public ArrayList<Tile> getMoveTiles(int[] location, int range, Direction direction) {
                if (direction != null) {
                if (boardController.getBoard().getTile(location[0], location[1]).getPiece().getType() == PieceType.FRIGATE) {
                    ArrayList<Tile> moveTiles = getMoveTilesSquareRange(location, 1);
                    moveTiles.addAll(getMoveTilesLineRange(location, range));
                    return moveTiles;
                }
                return getMoveTilesLineRange(location, range);
            }
            else {
                return getMoveTilesSquareRange(location, range);
            }
        }
        public ArrayList<Tile> getAttackTiles(int[] location, int range, Direction direction) {
            if (direction != null) {
//                if (activePiece.getType() == PieceType.FRIGATE) {
//                    ArrayList<Tile> attackTiles = getAttackTilesSquareRange(location, 1);
//                    attackTiles.addAll(getAttackTilesLineRange(location, range));
//                    return attackTiles;
//                }
                return getAttackTilesLineRange(location, range);
            }
            else {
                return getAttackTilesSquareRange(location, range);
            }
        }
        public ArrayList<Tile> getAttackRangeTiles(int range, Direction direction) {
            int[] location = new int[]{6, 6};
            if (direction != null) {
//                if (activePiece.getType() == PieceType.FRIGATE) {
//                    ArrayList<Tile> attackTiles = getAttackRangeTilesSquareRange(location, 1);
//                    attackTiles.addAll(getAttackRangeTilesLineRange(location, range));
//                    return attackTiles;
//                }
                return getAttackRangeTilesLineRange(location, range);
            }
            else {
                return getAttackRangeTilesSquareRange(location, range);
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
            int size = boardController.getSize();
            int x = location[0];
            int y = location[1];
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int i = 0; i > -range; i--) {//north
                y = y - 1;
                if (((x < size)
                        && (y < size))
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
                if (((x < size)
                        && (y < size))
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
                if (((x < size)
                        && (y < size))
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
                if (((x < size)
                        && (y < size))
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
            int size =boardController.getSize();
            int X = location[0];
            int Y = location[1];
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int i = -range; i <= range; i++) {
                for (int j = -range; j <= range; j++) {
                    int x = X + i;
                    int y = Y + j;
                    // Checks the agent isn't looking outside the grid, at its current tile or at terrain.
                    if (((x < size)
                            && (y < size))
                            && ((x >= 0) && (y >= 0))
                            && !(i == 0 && j == 0)) {
                        if (boardController.getBoard().getTile(x, y).getPiece() != null) {
                            if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                                tiles.add(boardController.getBoard().getTile(x, y));
                            }
                        }
                    }
                }
            }
            return tiles;
        }
        public ArrayList<Tile> getAttackRangeTilesLineRange(int[] location, int range) {
            int size = miniBoard.getSize();
            int x = location[0];
            int y = location[1];
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int i = 0; i > -range; i--) {//north
                y = y - 1;
                if (((x < size)
                        && (y < size))
                        && ((x >= 0) && (y >= 0))) {
                            tiles.add(miniBoard.getTile(x, y));

                }
            }
            x = location[0];
            y = location[1];
            for (int i = 0; i < range; i++) {//east
                x = x + 1;
                if (((x < size)
                        && (y < size))
                        && ((x >= 0) && (y >= 0))) {
                            tiles.add(miniBoard.getTile(x, y));
                }
            }
            x = location[0];
            y = location[1];
            for (int i = 0; i < range; i++) {//south
                y = y + 1;
                if (((x < size)
                        && (y < size))
                        && ((x >= 0) && (y >= 0))) {
                            tiles.add(miniBoard.getTile(x, y));
                }
            }
            x = location[0];
            y = location[1];
            for (int i = 0; i > -range; i--) {//west
                x = x - 1;
                if (((x < size)
                        && (y < size))
                        && ((x >= 0) && (y >= 0))) {
                            tiles.add(miniBoard.getTile(x, y));
                }
            }
            return tiles;
        }
        public ArrayList<Tile> getAttackRangeTilesSquareRange(int[] location, int range) {
            int size = miniBoard.getSize();
            int X = location[0];
            int Y = location[1];
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int i = -range; i <= range; i++) {
                for (int j = -range; j <= range; j++) {
                    int x = X + i;
                    int y = Y + j;
                    // Checks the agent isn't looking outside the grid, at its current tile or at terrain.
                    if (((x < size)
                            && (y < size))
                            && ((x >= 0) && (y >= 0))
                            && !(i == 0 && j == 0)) {
                        tiles.add(miniBoard.getTile(x, y));

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
        public HashMap<Tile, ArrayList<Tile>> getTeamMembersInAttackRangeOfTarget(Team team, ArrayList<Tile> enemyTiles, Piece activePiece) {
            //System.out.println(activePiece.getID());
            HashMap<Tile, ArrayList<Tile>> teamMembersInRange = new HashMap<>();
            if (enemyTiles.isEmpty()) {
                return teamMembersInRange;
            }
            ArrayList<Tile> aliveTeamMembers = boardController.getPiecesOnBoard(team);
            for (Tile teamMember : aliveTeamMembers) {
                ArrayList<Tile> enemiesInRangeOfTeamMember = new ArrayList<>();
                ArrayList<Tile> enemiesTilesInRange = getAttackTiles(teamMember.getLocation(), teamMember.getPiece().getAttackRange(), teamMember.getPiece().getDirection());
                for (Tile enemyTileInRange : enemiesTilesInRange) {
                    for (Tile enemyTile : enemyTiles) {
                        if (enemyTileInRange.getPiece().getID() == enemyTile.getPiece().getID() && activePiece.getID() != teamMember.getPiece().getID() && activePiece.getAttackCost() + teamMember.getPiece().getAttackCost() <= actionPoints && !teamMember.getPiece().hasMoved()) {
                            enemiesInRangeOfTeamMember.add(enemyTileInRange);
                        }
                    }
                    if (!enemiesInRangeOfTeamMember.isEmpty()) {
                        teamMembersInRange.put(teamMember, enemiesInRangeOfTeamMember);
                    }
                }
            }
            return teamMembersInRange;
        }

    }

    // Groups methods used to display the board as an image
    public class BoardImage {

        public HashMap<String, Color[]> tileColors = new HashMap<>();
        private HashMap<SpriteTags, BufferedImage> sprites = new HashMap<>();
        private final BufferedImage blankBoardImage;
        private BufferedImage lastBoardImage;

        public BoardImage() throws IOException {
            loadSprites();
            blankBoardImage = blankBoard();
            lastBoardImage = BoardImage.deepCopy(blankBoardImage);
        }

        public void resetBoardImage() {
            blankBoard(boardController.getScale());
            lastBoardImage = BoardImage.deepCopy(blankBoardImage);
        }

        public void displayActions() {
            lastBoardImage = BoardImage.deepCopy(blankBoardImage);
            gameView.updateBoard(boardImage.getBoardImage());
            boardImage.displayMovementRange(moveTiles);
            boardImage.displayAttackRange(attackTiles);
            boardImage.displayTeamMembersInRange(teamMemberTiles.keySet());
            boardImage.displayAttackingTeamMembersInRange(attackingTeamMembers.keySet());
            gameView.updateBoard(boardImage.getBoardImage());
        }
        public void displayAttackActions() {
            lastBoardImage = BoardImage.deepCopy(blankBoardImage);
            gameView.updateBoard(boardImage.getBoardImage());
            boardImage.displayAttackRange(attackTiles);
            boardImage.displayTeamMembersInRange(teamMemberTiles.keySet());
            boardImage.displayAttackingTeamMembersInRange(attackingTeamMembers.keySet());
            gameView.updateBoard(boardImage.getBoardImage());
        }
        public void displayMultiSelect() {
            lastBoardImage = BoardImage.deepCopy(blankBoardImage);
            gameView.updateBoard(boardImage.getBoardImage());
            boardImage.displayAttackRange(attackTiles);
            boardImage.displayTeamMembersInRange(teamMemberTiles.keySet());
            boardImage.displayAttackingTeamMembersInRange(attackingTeamMembers.keySet());
            gameView.updateBoard(boardImage.getBoardImage());
        }
        public void displayMultiAttack() {
            lastBoardImage = BoardImage.deepCopy(blankBoardImage);
            gameView.updateBoard(boardImage.getBoardImage());
            boardImage.displayAttackRange(attackTiles);
            boardImage.displayAttackingTeamMembersInRange(attackingTeamMembers.keySet());
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
                                        if (tile.getPiece() != null) {
                                            Color rgba = new Color(sprites.get(getPieceSpriteTag(tile.getPiece())).getRGB(i, j), true);
                                            if (rgba.getAlpha() == 255) {
                                                if (tile.getPiece().getDamaged()) {
                                                    if (i % 3 == 0 || j % 2 == 0) {
                                                        lastBoardImage.setRGB(x + i, y + j, rgba.getRGB());
                                                    }
                                                } else {
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
            }
            return lastBoardImage;
        }
        public BufferedImage getMiniImage(int scale) {
            int size = miniBoard.getSize();
            BufferedImage boardImage = new BufferedImage(size * scale, size * scale, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x <= scale * size; x += scale) {
                for (int y = 0; y <= scale * size; y += scale) {
                    for (int i = 0; i < scale; i++) {
                        for (int j = 0; j < scale; j++) {
                            if (((x + i < scale * size) && (y + j < scale * size)) && ((x + i >= 0) && (y + j >= 0))) {
                                boardImage.setRGB(x + i, y + j, miniBoard.getTile(x / scale, y / scale).getColor().getRGB());
                            }
                        }
                    }
                }
            }
            return boardImage;
        }
        public BufferedImage displayMiniAttackRange(ArrayList<Tile> tiles, BufferedImage miniImage) {
            int size = miniBoard.getSize();
            int scale = miniImage.getHeight() / size;
            tiles.add(new Tile(Color.BLACK,6, 6));
            for (Tile tile : tiles) {
                int x = tile.getLocation()[0];
                int y = tile.getLocation()[1];
                if (true) {
                    for (int i = 0; i < scale; i++) {
                        for (int j = 0; j < scale; j++) {
                            if (((x + i < scale * size) && (y + j < scale * size)) && ((x + i >= 0) && (y + j >= 0))) {
                                if (tile.getColor() == Color.BLACK) {
                                    miniImage.setRGB((x * scale) + i, (y * scale) + j, Color.BLACK.getRGB());
                                }
                                else if (tile.getColor() == miniBoard.getDarkColor()) {
                                    miniImage.setRGB((x * scale) + i, (y * scale) + j, darkAttackColor.getRGB());
                                }
                                else if (tile.getColor() == miniBoard.getLightColor()) {
                                    miniImage.setRGB((x * scale) + i, (y * scale) + j, lightAttackColor.getRGB());
                                }

                            }
                        }
                    }
                }
            }
            return miniImage;
        }
        public Color swapTileColor(Color color) {
            //Random random = new Random();
            if (color == darkTileColor) {
                color = lightTileColor;
            }
            else {
                color = darkTileColor;

            }
            return color;
            //return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
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
        public void displayTeamMembersInRange(Set<Tile> tiles) {
            if (tiles == null) {
                return;
            }
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
                                        lastBoardImage.setRGB((x * scale) + i, (y * scale) + j, darkTeamMemberColor.getRGB());
                                    }
                                    else {
                                        lastBoardImage.setRGB((x * scale) + i, (y * scale) + j, lightTeamMemberColor.getRGB());
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        public void displayAttackingTeamMembersInRange(Set<Tile> tiles) {
            if (tiles == null) {
                return;
            }
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
                                        lastBoardImage.setRGB((x * scale) + i, (y * scale) + j, darkAttackingTeamMemberColor.getRGB());
                                    }
                                    else {
                                        lastBoardImage.setRGB((x * scale) + i, (y * scale) + j, lightAttackingTeamMemberColor.getRGB());
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
            Random random = new Random();
            int size = boardController.getSize();
            int scale = boardController.getScale();
            BufferedImage boardImage = new BufferedImage(size * scale, size * scale, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x <= scale * size; x += scale) {
                for (int y = 0; y <= scale * size; y += scale) {
                    for (int i = 0; i < scale; i++) {
                        for (int j = 0; j < scale; j++) {
                            if (((x + i < scale * size) && (y + j < scale * size)) && ((x + i >= 0) && (y + j >= 0))) {
                                boardImage.setRGB(x + i, y + j, boardController.getBoard().getTile(x / scale, y / scale).getColor().getRGB());
//                                if (random.nextInt(3) == 1) {
//                                    boardImage.setRGB(x + i, y + j, boardController.getBoard().getTile(x / scale, y / scale).getColor().darker().getRGB());
//
//                                }
//                                else {
//                                    boardImage.setRGB(x + i, y + j, boardController.getBoard().getTile(x / scale, y / scale).getColor().brighter().getRGB());
//                                }
                            }
                        }
                    }
                }
            }
            return boardImage;
        }
        public BufferedImage blankBoard(int scale) {
            int size = boardController.getSize();
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