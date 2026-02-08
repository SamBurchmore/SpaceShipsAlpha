package Logic;

import Data.*;

import java.util.ArrayList;
import java.util.HashMap;

public record BoardState(Board board, Tile selectedTile, Piece activePiece, GameState gameState, PieceList blackTeam, PieceList whiteTeam, TeamMemberTiles attackingTeamMembers, TeamMemberTiles teamMembers, TileList attackTiles, Team activeTeam, int actionPoints, int turnCost, int attackCost, Team winner) {

    public BoardState deepCopy() {
        if (this.selectedTile == null) {
            if (this.activePiece == null) {
                return new BoardState(this.board.deepCopy(), null, null, this.gameState, this.blackTeam.deepCopy(), this.whiteTeam.deepCopy(), this.attackingTeamMembers.deepCopy(), this.teamMembers.deepCopy(), this.attackTiles.deepCopy(), this.activeTeam, this.actionPoints, this.turnCost, this.attackCost, this.winner);
            }
            return new BoardState(this.board.deepCopy(), null, this.activePiece.deepCopy(), this.gameState, this.blackTeam.deepCopy(), this.whiteTeam.deepCopy(), this.attackingTeamMembers.deepCopy(), this.teamMembers.deepCopy(), this.attackTiles.deepCopy(), this.activeTeam, this.actionPoints, this.turnCost, this.attackCost, this.winner);

        }
        return new BoardState(this.board.deepCopy(), this.selectedTile.deepCopy(), this.activePiece.deepCopy(), this.gameState, this.blackTeam.deepCopy(), this.whiteTeam.deepCopy(), this.attackingTeamMembers.deepCopy(), this.teamMembers.deepCopy(), this.attackTiles.deepCopy(), this.activeTeam, this.actionPoints, this.turnCost, this.attackCost, this.winner);
    }
}
