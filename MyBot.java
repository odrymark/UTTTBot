package dk.easv.bll.bot;

import dk.easv.bll.bot.IBot;
import dk.easv.bll.game.IGameState;
import dk.easv.bll.move.IMove;

import java.util.List;
import java.util.Random;

public class MyBot implements IBot {

    private static final String BOTNAME = "MyBot";
    private final Random rand = new Random();

    @Override
    public IMove doMove(IGameState state) {
        List<IMove> moves = state.getField().getAvailableMoves();
        if (moves.isEmpty()) {
            return null;
        }

        String currentPlayer = String.valueOf(state.getMoveNumber() % 2);
        String opponentPlayer = String.valueOf((state.getMoveNumber() + 1) % 2);

        IMove winningMove = findWinningMove(state, currentPlayer);
        if (winningMove != null) {
            return winningMove;
        }

        IMove blockingMove = findWinningMove(state, opponentPlayer);
        if (blockingMove != null) {
            return blockingMove;
        }

        IMove centerMove = findCenterMove(moves);
        if (centerMove != null) {
            return centerMove;
        }

        return moves.get(rand.nextInt(moves.size()));
    }

    private IMove findWinningMove(IGameState state, String player) {
        List<IMove> moves = state.getField().getAvailableMoves();
        String[][] board = formatBoard(state.getField().getBoard());

        for (IMove move : moves) {
            board[move.getX()][move.getY()] = player;
            if (isWinningMove(board, move, player)) {
                return move;
            }
            board[move.getX()][move.getY()] = ".";
        }
        return null;
    }

    private boolean isWinningMove(String[][] board, IMove move, String player) {
        int x = move.getX();
        int y = move.getY();
        int boxStartX = (x / 3) * 3;
        int boxStartY = (y / 3) * 3;

        for (int i = 0; i < 3; i++) {
            if (board[boxStartX + i][y].equals(player) && board[boxStartX + (i + 1) % 3][y].equals(player) && board[boxStartX + (i + 2) % 3][y].equals(player)) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (board[x][boxStartY + i].equals(player) && board[x][boxStartY + (i + 1) % 3].equals(player) && board[x][boxStartY + (i + 2) % 3].equals(player)) {
                return true;
            }
        }

        if (board[boxStartX][boxStartY].equals(player) && board[boxStartX + 1][boxStartY + 1].equals(player) && board[boxStartX + 2][boxStartY + 2].equals(player)) {
            return true;
        }

        if (board[boxStartX + 2][boxStartY].equals(player) && board[boxStartX + 1][boxStartY + 1].equals(player) && board[boxStartX][boxStartY + 2].equals(player)) {
            return true;
        }

        return false;
    }

    private IMove findCenterMove(List<IMove> moves) {
        for (IMove move : moves) {
            if (move.getX() % 3 == 1 && move.getY() % 3 == 1) {
                return move;
            }
        }
        return null;
    }

    private String[][] formatBoard(String[][] originalBoard) {
        String[][] formattedBoard = new String[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                formattedBoard[i][j] = originalBoard[i][j].equals("-") ? "." : originalBoard[i][j];
            }
        }
        return formattedBoard;
    }

    @Override
    public String getBotName() {
        return BOTNAME;
    }
}
