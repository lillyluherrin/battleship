import java.util.Random;

public class BattleshipGame {
    private static final int SIZE = 5;
    private Board board;
    private int shipsCount;
    private final int[] shipSizes = {3, 2, 2};
    private int shotsTaken = 0;

    public BattleshipGame() {
        shipsCount = shipSizes.length;
        board = new Board(SIZE);
        placeShips();
    }

    public char[][] getBoard() {
        return board.getGrid();
    }

    public int getShipsCount() {
        return shipsCount;
    }

    public int getShotsTaken() {
        return shotsTaken;
    }

    public boolean isGameOver() {
        return shipsCount == 0;
    }

    public boolean isHit(int row, int col) {
        shotsTaken++;
        boolean hit = board.isHit(row, col);
        if (hit) {
            if (board.isSunk(row, col)) {
                shipsCount--;
            }
        }
        return hit;
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
        board.resetBoard();
        placeShips();
    }
}
