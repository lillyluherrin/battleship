import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattleshipGUI extends JFrame {
    private static final int SIZE = 5;
    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private BattleshipGame game;
    private JLabel shipsLabel;
    private JLabel shotsLabel;

    public BattleshipGUI() {
        setTitle("Battleship Game");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        game = new BattleshipGame();
        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        initializeButtons(boardPanel);

        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        shipsLabel = new JLabel("Remaining Ships: " + game.getShipsCount());
        shotsLabel = new JLabel("Shots Taken: " + game.getShotsTaken());
        infoPanel.add(shipsLabel);
        infoPanel.add(shotsLabel);

        JPanel controlPanel = new JPanel();
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> resetGame(boardPanel));
        controlPanel.add(restartButton);

        add(boardPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
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
            if (game.isHit(row, col)) {
                buttons[row][col].setText("X");
                buttons[row][col].setBackground(Color.RED);
                if (game.isGameOver()) {
                    JOptionPane.showMessageDialog(null, "Congratulations! You've sunk all the ships!");
                }
            } else {
                buttons[row][col].setText("O");
                buttons[row][col].setBackground(Color.BLUE);
            }
            buttons[row][col].setEnabled(false);
            updateLabels();
        }
    }

    private void updateLabels() {
        shipsLabel.setText("Remaining Ships: " + game.getShipsCount());
        shotsLabel.setText("Shots Taken: " + game.getShotsTaken());
    }

    private void resetGame(JPanel boardPanel) {
        game.resetGame();
        initializeButtons(boardPanel);
        updateLabels();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BattleshipGUI());
    }
}
