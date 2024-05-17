import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class BattleshipGUI extends JFrame {
    private static final int SIZE = 5;
    private static final char EMPTY = '-';
    private static final char SHIP = 'S';
    private static final char HIT = 'X';
    private static final char MISS = 'O';
    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private char[][] board = new char[SIZE][SIZE];
    private int shipsCount = 3;

    public BattleshipGUI() {
        setTitle("Battleship Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(SIZE, SIZE));

        initializeBoard();
        placeShips();
        initializeButtons();

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
        int placedShips = 0;
        while (placedShips < shipsCount) {
            int row = rand.nextInt(SIZE);
            int col = rand.nextInt(SIZE);
            if (board[row][col] == EMPTY) {
                board[row][col] = SHIP;
                placedShips++;
            }
        }
    }

    private void initializeButtons() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(60, 60));
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                add(buttons[i][j]);
            }
        }
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
}
