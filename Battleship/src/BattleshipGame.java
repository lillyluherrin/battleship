import java.util.Random;

public class BattleshipGame {
    public static final int EASY_TRIES = 50;
    public static final int MEDIUM_TRIES = 30;
    public static final int HARD_TRIES = 20;
    private static final int SIZE = 10;
    private Board board;
    private int shipsCount;
    private final int[] shipSizes = {5, 4, 3, 3, 2};
    private int shotsTaken = 0;
    private int triesLeft;
    private String difficulty;

    public BattleshipGame(String difficulty) {
        this.difficulty = difficulty;
        setTriesBasedOnDifficulty(difficulty);
        shipsCount = shipSizes.length;
        board = new Board();
        placeShips();
    }

    private void setTriesBasedOnDifficulty(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy":
                triesLeft = EASY_TRIES;
                break;
            case "medium":
                triesLeft = MEDIUM_TRIES;
                break;
            case "hard":
                triesLeft = HARD_TRIES;
                break;
            default:
                triesLeft = MEDIUM_TRIES;  // Default to medium
        }
    }

    public char[][] getBoardGrid() {
        return board.getGrid();
    }

    public int getShipsCount() {
        return shipsCount;
    }

    public int getShotsTaken() {
        return shotsTaken;
    }

    public int getTriesLeft() {
        return triesLeft;
    }

    public boolean isGameOver() {
        return shipsCount == 0 || triesLeft == 0;
    }

    public boolean isHit(int row, int col) {
        shotsTaken++;
        return board.isHit(row, col);
    }

    public void decrementTries() {
        triesLeft--;
    }

    public boolean isSunk(int row, int col) {
        boolean sunk = board.isSunk(row, col);
        if (sunk) {
            shipsCount--;
        }
        return sunk;
    }

    public int[][] getShipLocations(int row, int col) {
        return board.getShipLocations(row, col);
    }

    private void placeShips() {
        Random rand = new Random();
        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                int row = rand.nextInt(SIZE);
                int col = rand.nextInt(SIZE);
                boolean horizontal = rand.nextBoolean();
                if (board.canPlaceShip(row, col, size, horizontal)) {
                    board.placeShip(row, col, size, horizontal);
                    placed = true;
                }
            }
        }
    }

    public void resetGame() {
        shotsTaken = 0;
        shipsCount = shipSizes.length;
        setTriesBasedOnDifficulty(difficulty);
        board.resetBoard();
        placeShips();
    }
}
