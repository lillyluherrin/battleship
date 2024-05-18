import java.util.HashMap;
import java.util.Map;

public class Board {
    private final char[][] grid;
    private final int size;
    private Map<String, Integer> shipHits;

    public static final char EMPTY = '-';
    public static final char SHIP = 'S';
    public static final char HIT = 'X';
    public static final char MISS = 'O';

    public Board(int size) {
        this.size = size;
        grid = new char[size][size];
        shipHits = new HashMap<>();
        initializeBoard();
    }

    public char[][] getGrid() {
        return grid;
    }

    public void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = EMPTY;
            }
        }
        shipHits.clear();
    }

    public boolean canPlaceShip(int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            if (col + size > this.size) return false;
            for (int i = 0; i < size; i++) {
                if (grid[row][col + i] != EMPTY) return false;
            }
        } else {
            if (row + size > this.size) return false;
            for (int i = 0; i < size; i++) {
                if (grid[row + i][col] != EMPTY) return false;
            }
        }
        return true;
    }

    public void placeShip(int row, int col, int size, boolean horizontal) {
        String shipKey = row + "," + col + "," + size + "," + horizontal;
        shipHits.put(shipKey, size);
        if (horizontal) {
            for (int i = 0; i < size; i++) {
                grid[row][col + i] = SHIP;
            }
        } else {
            for (int i = 0; i < size; i++) {
                grid[row + i][col] = SHIP;
            }
        }
    }

    public boolean isHit(int row, int col) {
        if (grid[row][col] == SHIP) {
            grid[row][col] = HIT;
            return true;
        } else if (grid[row][col] == EMPTY) {
            grid[row][col] = MISS;
        }
        return false;
    }

    public boolean isSunk(int row, int col) {
        String hitKey = null;
        for (String key : shipHits.keySet()) {
            String[] parts = key.split(",");
            int shipRow = Integer.parseInt(parts[0]);
            int shipCol = Integer.parseInt(parts[1]);
            int shipSize = Integer.parseInt(parts[2]);
            boolean horizontal = Boolean.parseBoolean(parts[3]);

            if (horizontal) {
                if (row == shipRow && col >= shipCol && col < shipCol + shipSize) {
                    hitKey = key;
                    break;
                }
            } else {
                if (col == shipCol && row >= shipRow && row < shipRow + shipSize) {
                    hitKey = key;
                    break;
                }
            }
        }

        if (hitKey != null) {
            shipHits.put(hitKey, shipHits.get(hitKey) - 1);
            if (shipHits.get(hitKey) == 0) {
                shipHits.remove(hitKey);
                return true;
            }
        }
        return false;
    }

    public void resetBoard() {
        initializeBoard();
    }
}
