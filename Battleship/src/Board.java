public class Board {
    private final char[][] grid;
    private final int size;

    public Board(int size) {
        this.size = size;
        grid = new char[size][size];
        initializeBoard();
    }

    public char[][] getGrid() {
        return grid;
    }

    public void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = '-';
            }
        }
    }
}
