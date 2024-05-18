import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class BattleshipGUI extends JFrame {
    private static final int SIZE = 10;  // Updated size
    private static final int BUTTON_SIZE = 60;  // Button size
    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private BattleshipGame game;
    private JLabel shipsLabel;
    private JLabel shotsLabel;
    private JLabel triesLabel;  // Label for remaining tries
    private JLabel difficultyLabel;  // Label for difficulty level
    private BufferedImage hitImage;
    private BufferedImage missImage;
    private String difficulty;  // Difficulty level

    public BattleshipGUI() {
        setTitle("Battleship Game");
        setSize(800, 800);  // Updated size for the larger board
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Load images
        hitImage = loadImage("/hit.png");
        missImage = loadImage("/miss.png");

        // Verify images are loaded correctly
        if (hitImage == null || missImage == null) {
            JOptionPane.showMessageDialog(this, "Error loading images.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        selectDifficulty();  // Select difficulty level

        game = new BattleshipGame(difficulty);
        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        initializeButtons(boardPanel);

        JPanel infoPanel = new JPanel(new GridLayout(1, 4));  // Adjusted layout to 1x4 grid
        shipsLabel = new JLabel("Remaining Ships: " + game.getShipsCount());
        shotsLabel = new JLabel("Shots Taken: " + game.getShotsTaken());
        triesLabel = new JLabel("Tries Left: " + game.getTriesLeft());  // Initialize triesLabel
        difficultyLabel = new JLabel("Difficulty: " + difficulty);  // Initialize difficultyLabel

        infoPanel.add(shipsLabel);
        infoPanel.add(shotsLabel);
        infoPanel.add(triesLabel);  // Add triesLabel to infoPanel
        infoPanel.add(difficultyLabel);  // Add difficultyLabel to infoPanel

        JPanel controlPanel = new JPanel();
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> resetGame(boardPanel));
        JButton changeDifficultyButton = new JButton("Change Difficulty");
        changeDifficultyButton.addActionListener(e -> changeDifficulty(boardPanel));
        controlPanel.add(restartButton);
        controlPanel.add(changeDifficultyButton);

        add(boardPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private BufferedImage loadImage(String path) {
        try {
            BufferedImage img = ImageIO.read(getClass().getResource(path));
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initializeButtons(JPanel boardPanel) {
        boardPanel.removeAll();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        if (getIcon() != null) {
                            int x = (getWidth() - getIcon().getIconWidth()) / 2;
                            int y = (getHeight() - getIcon().getIconHeight()) / 2;
                            getIcon().paintIcon(this, g, x, y);
                        }
                    }
                };
                buttons[i][j].setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
                buttons[i][j].setContentAreaFilled(false);
                buttons[i][j].setOpaque(true);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j, boardPanel));
                boardPanel.add(buttons[i][j]);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private void selectDifficulty() {
        String[] options = {
                "Easy - " + BattleshipGame.EASY_TRIES + " tries",
                "Medium - " + BattleshipGame.MEDIUM_TRIES + " tries",
                "Hard - " + BattleshipGame.HARD_TRIES + " tries"
        };
        String selection = (String) JOptionPane.showInputDialog(
                this,
                "Select Difficulty Level",
                "Difficulty",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
        );
        if (selection == null) {
            difficulty = "Medium";  // Default to Medium if no selection
        } else if (selection.startsWith("Easy")) {
            difficulty = "Easy";
        } else if (selection.startsWith("Medium")) {
            difficulty = "Medium";
        } else if (selection.startsWith("Hard")) {
            difficulty = "Hard";
        }
    }

    private void changeDifficulty(JPanel boardPanel) {
        selectDifficulty();  // Select new difficulty level
        game = new BattleshipGame(difficulty);  // Initialize new game with selected difficulty
        resetGame(boardPanel);  // Reset the game with the new difficulty level
        updateLabels();  // Update labels to reflect new difficulty and tries left
    }

    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;
        private JPanel boardPanel;  // Added reference to boardPanel

        public ButtonClickListener(int row, int col, JPanel boardPanel) {
            this.row = row;
            this.col = col;
            this.boardPanel = boardPanel;  // Initialize boardPanel
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.isGameOver()) {
                return;
            }

            if (game.isHit(row, col)) {
                buttons[row][col].setIcon(new ImageIcon(hitImage));
                buttons[row][col].setBackground(Color.RED);
                playSound("/hit.wav");
                if (game.isSunk(row, col)) {
                    int[][] locations = game.getShipLocations(row, col);
                    if (locations != null) {
                        for (int[] location : locations) {
                            buttons[location[0]][location[1]].setBackground(Color.ORANGE);
                        }
                    }
                }
            } else {
                buttons[row][col].setIcon(new ImageIcon(missImage));
                buttons[row][col].setBackground(Color.BLUE);
                playSound("/miss.wav");
                game.decrementTries();  // Decrement tries only on miss
            }
            buttons[row][col].setEnabled(false);
            updateLabels();

            if (game.isGameOver()) {
                if (game.getShipsCount() == 0) {
                    playSound("/win.wav");
                    JOptionPane.showMessageDialog(null, "Congratulations! You've sunk all the ships!");
                } else {
                    playSound("/lose.wav");
                    JOptionPane.showMessageDialog(null, "Game Over! You've run out of tries.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    resetGame(boardPanel);  // Reset the game after the dialog is dismissed
                }
            }
        }
    }

    private void playSound(String path) {
        try {
            URL url = getClass().getResource(path);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void updateLabels() {
        shipsLabel.setText("Remaining Ships: " + game.getShipsCount());
        shotsLabel.setText("Shots Taken: " + game.getShotsTaken());
        triesLabel.setText("Tries Left: " + game.getTriesLeft());  // Update triesLabel
        difficultyLabel.setText("Difficulty: " + difficulty);  // Update difficultyLabel
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
