package Logic;
import Data.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class AutoPlay {

    int waitTime = 20;

    private Logic.MainController mainController;

    public AutoPlay(MainController mainController) {
        this.mainController = mainController;
    }

    public ArrayList<BoardState> nextStates(BoardState baseState, BoardState initialState, int depth) throws IOException, InterruptedException {
        System.out.println("next states");
        TimeUnit.MILLISECONDS.sleep(waitTime);
        //baseState = initialState;
        ArrayList<BoardState> newStates = new ArrayList<>();
        if (initialState.winner() != null || depth < 1) {
            System.out.println(initialState.winner() + " wins");
            initialState = baseState;
            TimeUnit.MINUTES.sleep(waitTime* 4L);
            depth = 5000;
            //return newStates;
        }
        mainController.setGame(initialState); // load initial state
        TimeUnit.MILLISECONDS.sleep(waitTime);
        depth -= 1;
        //System.out.println(depth);
        // if no piece is selected, iterate over every piece on active team
        if (initialState.gameState() == GameState.NO_PIECE_SELECTED) {
            // load the active team
            ArrayList<Piece> activeTeam;
            if (initialState.activeTeam() == Team.BLACK) {
                activeTeam = initialState.blackTeam();
            } else {
                activeTeam = initialState.whiteTeam();
            }
            ArrayList<Piece> at = new ArrayList<>();
            for (Piece p : activeTeam) { // iterate over team
                if (!p.hasMoved()) { // TODO prune pieces which are to expensive
                    at.add(p);
                }
            }
//            if (!mainController.getPlayLogic().canPlayerTakeAction()) {
//                return newStates;
//            }
            Collections.shuffle(at);
                for (Piece p : at) { // iterate over team
                    if (p.hasMoved()) {

                    }
                    else {
                        TimeUnit.MILLISECONDS.sleep(waitTime);
                        mainController.setGame(initialState); // reset game to initial state
                        mainController.getPlayLogic().runGame(p.getLocation()); // select piece location
                    if (mainController.getGame().gameState() == GameState.NO_PIECE_SELECTED) {
                        System.out.println(activeTeam.toString());
                        System.out.println(mainController.getActiveTeam());
                        System.out.println(initialState.activeTeam());
                        System.out.println(p.getTeam() + " " + p.getType() + " " + p.getLocation()[0] + " " + p.getLocation()[1]);
                        System.out.println(p.hasMoved());
                        //TimeUnit.MINUTES.sleep(10);
                    }
                        TileList moveTiles = mainController.attackTiles.deepCopy(); // get move tiles
                        moveTiles.addAll(mainController.moveTiles.deepCopy()); // get attack tiles
                        moveTiles.addAll(mainController.teamMemberTiles.deepCopy().keySet()); // get team member tiles
                        if (!moveTiles.isEmpty()) {
                            System.out.println(moveTiles.size());
                            Collections.shuffle(moveTiles);
                            newStates.addAll(playMoves(baseState, initialState, moveTiles, depth));
                        }
                    }
                }
        }
        else if (initialState.activePiece() != null && !initialState.activePiece().hasMoved()){ // if a piece(s) is selected, iterate over possible moves
            TileList moveTiles = mainController.attackTiles.deepCopy(); // get move tiles
            moveTiles.addAll(mainController.moveTiles.deepCopy()); // get attack tiles
            moveTiles.addAll(mainController.teamMemberTiles.deepCopy().keySet()); // get team member tiles
            if (!moveTiles.isEmpty()) {
                System.out.println(moveTiles.size());
                Collections.shuffle(moveTiles);
                newStates.addAll(playMoves(baseState, initialState, moveTiles, depth));
            }
        }
        return newStates;
    }



    public ArrayList<BoardState> playMoves(BoardState baseState, BoardState initialState, Collection<Tile> tiles, int depth) throws IOException, InterruptedException {
        System.out.println("play moves");
        ArrayList<BoardState> newStates = new ArrayList<>();
        //mainController.setGame(initialState); // reset game to initial state
//        if (initialState.activePiece().hasMoved()) {
//            return newStates;
//        }
        for (Tile tile : tiles) { // iterate over moves
            TimeUnit.MILLISECONDS.sleep(waitTime);
            mainController.getPlayLogic().runGame(tile.getLocation()); // play move, creating new state
            if (mainController.gameState == GameState.SELECTING_MORE_PIECES || mainController.gameState == GameState.MULTIPLE_PIECES_SELECTED) {
                TimeUnit.MILLISECONDS.sleep(waitTime* 4L);
            }
            // display move
//            mainController.getBoardImage().resetBoardImage();
//            mainController.getGameView().updateBoard(mainController.getBoardImage().getBoardImage());
            newStates.addAll(nextStates(baseState, mainController.getGame(), depth)); // (RECURSIVE) get next states from new state
            mainController.setGame(initialState); // reset game to initial state
        }
        return newStates;
    }

    public Team swapTeam(Team team) {
        if (team == Team.BLACK) {
            return Team.WHITE;
        }
        return Team.BLACK;
    }
}
