import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;

public class Game extends JFrame {
    private JRadioButton easy, medium, hard;
    private JButton start, restart, chooseImageButton;
    private JPanel controlPanel;
    private JFileChooser fileChooser;
    private BufferedImage selectedImage = null;
    private int level = 3;
    private Board board;
    private Timer timer;
    private JLabel timerLabel, moveCountLabel;
    private int timeElapsed = 0, moveCount = 0;

    public Game() {
        setTitle("Brain Teaser");
        setSize(900, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.RED);

        timerLabel = new JLabel("Time: 0s", JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.BLUE);
        add(timerLabel, BorderLayout.NORTH);

        moveCountLabel = new JLabel("Moves: 0", JLabel.CENTER);
        moveCountLabel.setFont(new Font("Arial", Font.BOLD, 20));
        moveCountLabel.setForeground(Color.RED);
        add(moveCountLabel, BorderLayout.SOUTH);

        controlPanel = new JPanel(new FlowLayout());

        easy = new JRadioButton("Easy (3x3)");
        medium = new JRadioButton("Medium (6x6)");
        hard = new JRadioButton("Hard (9x9)");

        ButtonGroup difficultyGroup = new ButtonGroup();
        difficultyGroup.add(easy);
        difficultyGroup.add(medium);
        difficultyGroup.add(hard);
        easy.setSelected(true);

        start = new JButton("START");
        restart = new JButton("RESTART");
        chooseImageButton = new JButton("PICK AN IMAGE");

        start.setBackground(Color.GREEN);
        start.setForeground(Color.BLACK);
        start.setFont(new Font("Arial", Font.BOLD, 14));
        start.setPreferredSize(new Dimension(120, 40));

        restart.setBackground(Color.RED);
        restart.setForeground(Color.BLACK);
        restart.setFont(new Font("Arial", Font.BOLD, 14));
        restart.setPreferredSize(new Dimension(120, 40));

        chooseImageButton.setBackground(Color.YELLOW);
        chooseImageButton.setForeground(Color.BLACK);
        chooseImageButton.setFont(new Font("Arial", Font.BOLD, 14));
        chooseImageButton.setPreferredSize(new Dimension(180, 40));

        start.setToolTipText("Click to start the game with selected difficulty.");
        restart.setToolTipText("Click to restart the game.");
        easy.setToolTipText("Select Easy difficulty level.");
        medium.setToolTipText("Select Medium difficulty level.");
        hard.setToolTipText("Select Hard difficulty level.");
        chooseImageButton.setToolTipText("Choose Image Puzzle.");

        start.setEnabled(false);

        chooseImageButton.addActionListener(e -> {
            openFileChooser();
            if (selectedImage != null) start.setEnabled(true);
        });

        start.addActionListener(e -> {
            if (easy.isSelected()) level = 2;
            if (medium.isSelected()) level = 4;
            if (hard.isSelected()) level = 6;
            startGame();
        });

        restart.addActionListener(e -> restartGame());

        controlPanel.add(easy);
        controlPanel.add(medium);
        controlPanel.add(hard);
        controlPanel.add(start);
        controlPanel.add(restart);
        controlPanel.add(chooseImageButton);
        add(controlPanel, BorderLayout.SOUTH);

        timer = new Timer(1000, e -> {
            timeElapsed++;
            timerLabel.setText("Time: " + timeElapsed + "s");
            moveCountLabel.setText("Moves: " + moveCount);
        });
    }

    private void startGame() {
        if (selectedImage == null) {
            JOptionPane.showMessageDialog(this, "Select the puzzle image first!");
            return;
        }
        timeElapsed = 0;
        moveCount = 0;
        timerLabel.setText("Time: 0s");
        moveCountLabel.setText("Moves: 0");
        timer.restart();

        restart.setEnabled(true);
        start.setEnabled(false);

        if (board != null) remove(board);

        board = new Board(level, selectedImage, this);
        add(board, BorderLayout.NORTH);
        revalidate();
        repaint();
        timer.start();
    }

    public void updateMoveCount() {
        moveCount++;
        moveCountLabel.setText("Moves: " + moveCount);
    }

    public void stopTimer() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Congratulations! You solved the puzzle in " + moveCount + " moves and " + timeElapsed + " seconds.");
    }

    private void openFileChooser() {
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Puzzle Image");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "gif"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                selectedImage = ImageIO.read(fileChooser.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Image selection successful.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void restartGame() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to restart the game?", "Restart Game", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            if (board != null) remove(board);
            selectedImage = null;
            start.setEnabled(false);
            restart.setEnabled(false);
            easy.setSelected(true);
            timeElapsed = 0;
            moveCount = 0;
            timer.stop();
            timerLabel.setText("Time: 0s");
            moveCountLabel.setText("Moves: 0");
            revalidate();
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game().setVisible(true));
    }
}
