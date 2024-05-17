import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class BattleshipGUI extends JFrame {
    private static final int SIZE = 10;
    private static final char EMPTY = '-';
    private static final char SHIP = 'S';
    private static final char HIT = 'X';
    private static final char MISS = 'O';
    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private char[][] board = new char[SIZE][SIZE];
    private int shipsCount = 3;
    private final int[] shipSizes = {3, 2, 2}; // Different ship sizes

    public BattleshipGUI() {
        setTitle("Battleship Game");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        initializeBoard();
        placeShips();
        initializeButtons(boardPanel);

        JPanel controlPanel = new JPanel();
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> resetGame(boardPanel));
        controlPanel.add(restartButton);

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
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
        shipsCount = shipSizes.length; // Update shipsCount based on the number of ships
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

    private void initializeButtons(JPanel boardPanel) {
        boardPanel.removeAll();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(60, 60));
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                boardPanel.add(buttons[i][j]);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (board[row][col] == SHIP) {
                buttons[row][col].setText("X");
                buttons[row][col].setBackground(Color.RED);
                board[row][col] = HIT;
                shipsCount--;
                if (shipsCount == 0) {
                    JOptionPane.showMessageDialog(null, "Congratulations! You've sunk all the ships!");
                }
            } else if (board[row][col] == EMPTY) {
                buttons[row][col].setText("O");
                buttons[row][col].setBackground(Color.BLUE);
                board[row][col] = MISS;
            }
            buttons[row][col].setEnabled(false);
        }
    }

    private void resetGame(JPanel boardPanel) {
        initializeBoard();
        placeShips();
        initializeButtons(boardPanel);
    }

}
