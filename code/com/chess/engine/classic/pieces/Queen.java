package com.chess.engine.classic.pieces;

import java.util.List;

import com.chess.engine.classic.Alliance;
import com.chess.engine.classic.board.Board;
import com.chess.engine.classic.board.Move;
import com.chess.engine.classic.board.Move.AttackMove;
import com.chess.engine.classic.board.Tile;
import com.google.common.collect.ImmutableList.Builder;

public final class Queen extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = { -9, -8, -7, -1, 1,
        7, 8, 9 };

    public Queen(final Alliance alliance, final int piecePosition) {
        super(Type.QUEEN, alliance, piecePosition, true);
    }

    public Queen(final Alliance alliance,
                 final int piecePosition,
                 final boolean isFirstMove) {
        super(Type.QUEEN, alliance, piecePosition, isFirstMove);
    }

    private Queen(final Queen queen) {
        super(queen);
    }

    @Override
    public List<Move> calculateLegalMoves(final Board board) {
        final Builder<Move> legalMoves = new Builder<>();
        int candidateDestinationCoordinate;
        for (final int currentCandidate : CANDIDATE_MOVE_COORDINATES) {
            candidateDestinationCoordinate = this.piecePosition;
            while (true) {
                if (isFirstColumnExclusion(currentCandidate, candidateDestinationCoordinate) ||
                    isEightColumnExclusion(currentCandidate, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentCandidate;
                if (!Board.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    break;
                } else {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new Move(this.piecePosition, candidateDestinationCoordinate, this));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAtDestinationAllegiance = pieceAtDestination.getPieceAllegiance();
                        if (this.pieceAlliance != pieceAtDestinationAllegiance) {
                            legalMoves.add(new AttackMove(this.piecePosition, candidateDestinationCoordinate, this,
                                    pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return legalMoves.build();
    }

    @Override
    public int getPieceValue() {
        return Type.QUEEN.getPieceValue();
    }

    @Override
    public int locationBonus() {
        return this.pieceAlliance.queenBonus(this.piecePosition);
    }

    @Override
    public Queen createCopy() {
        return new Queen(this);
    }

    @Override
    public Queen movePiece(final Move move) {
        return new Queen(move.getMovedPiece().getPieceAllegiance(), move.getDestinationCoordinate(), false);
    }

    @Override
    public String toString() {
        return Type.QUEEN.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition,
                                                  final int candidatePosition) {
        return Board.FIRST_COLUMN[candidatePosition] && ((currentPosition == -9)
                || (currentPosition == -1) || (currentPosition == 7));
    }

    private static boolean isEightColumnExclusion(final int currentPosition,
                                                  final int candidatePosition) {
        return Board.EIGHTH_COLUMN[candidatePosition] && ((currentPosition == -7)
                || (currentPosition == 1) || (currentPosition == 9));
    }

}