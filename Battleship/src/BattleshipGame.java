import java.util.Random;

public class BattleshipGame {
    private static final int SIZE = 5;
    private static final char EMPTY = '-';
    private static final char SHIP = 'S';
    private static final char HIT = 'X';
    private static final char MISS = 'O';
    private int shipsCount;
    private final int[] shipSizes = {3, 2, 2};
    private char[][] board = new char[SIZE][SIZE];

    public BattleshipGame() {
        shipsCount = shipSizes.length;
        initializeBoard();
        placeShips();
    }

    public char[][] getBoard() {
        return board;
    }

    public int getShipsCount() {
        return shipsCount;
    }

    public boolean isGameOver() {
        return shipsCount == 0;
    }

    public boolean isHit(int row, int col) {
        if (board[row][col] == SHIP) {
            board[row][col] = HIT;
            shipsCount--;
            return true;
        } else if (board[row][col] == EMPTY) {
            board[row][col] = MISS;
        }
        return false;
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    private void placeShips() {
        Random rand = new Random();
        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                int row = rand.nextInt(SIZE);
                int col = rand.nextInt(SIZE);
                boolean horizontal = rand.nextBoolean();
                if (canPlaceShip(row, col, size, horizontal)) {
                    placeShip(row, col, size, horizontal);
                    placed = true;
                }
            }
        }
    }

    private boolean canPlaceShip(int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            if (col + size > SIZE) return false;
            for (int i = 0; i < size; i++) {
                if (board[row][col + i] != EMPTY) return false;
            }
        } else {
            if (row + size > SIZE) return false;
            for (int i = 0; i < size; i++) {
                if (board[row + i][col] != EMPTY) return false;
            }
        }
        return true;
    }

    private void placeShip(int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            for (int i = 0; i < size; i++) {
                board[row][col + i] = SHIP;
            }
        } else {
            for (int i = 0; i < size; i++) {
                board[row + i][col] = SHIP;
            }
        }
    }

    public void resetGame() {
        shipsCount = shipSizes.length;
        initializeBoard();
        placeShips();
    }
}
