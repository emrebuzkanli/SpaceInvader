/**
 * The GameManager class manages the state and score of the game.
 * It centralizes all operations related to the game's progress,
 * including start, stop, pause, and score updates.
 */
public class GameManager {
    private int score;
    private boolean gameOver;
    private boolean gameStarted;
    private boolean gamePaused;
    /**
     * Initializes a new GameManager instance with default values.
     * The game is not started, not paused, and the score is set to 0.
     */
    public GameManager() {
        this.score = 0;
        this.gameOver = false;
        this.gameStarted = false;
        this.gamePaused = false;
    }


    /**
     * Increases the score by a specified number of points.
     *
     * @param points the number of points to add to the score.
     */
    public void increaseScore(int points) {
        score = score + points;
    }

    /**
     * Decreases the score by 50(punishment).
     */

    public void decreaseScore() {
        score = score - 50;
    }
    /**
     * Retrieves the current score.
     *
     * @return the current score of the game.
     */
    public int getScore() {
        return score;
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over; false otherwise.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Sets the game's state to "game over."
     */
    public void setGameOver() {
        gameOver = true;
    }

    /**
     * Checks if the game has started.
     *
     * @return true if the game has started; false otherwise.
     */

    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Starts the game. Resets the paused and game-over states.
     */
    public void startGame() {
        gameStarted = true;
        gamePaused = false; // Ensure game is unpaused when starting
        gameOver = false;   // Reset gameOver when starting
    }

    /**
     * Stops the game, setting the started state to false.
     */
    public void stopGame() {
        gameStarted = false;
    }

    /**
     * Checks if the game is currently paused.
     *
     * @return true if the game is paused; false otherwise.
     */

    public boolean isGamePaused() {
        return gamePaused;
    }

    /**
     * Toggles the game's paused state.
     * If the game is paused, it unpauses; otherwise, it pauses the game.
     */
    public void togglePause() {
        if (gamePaused) {
            gamePaused = false;
        } else {
            gamePaused = true;
        }
    }

    /**
     * Resets the game state to its initial values:
     * the score is set to 0, and the game is not started, paused, or over.
     */

    public void reset() {
        score = 0;
        gameOver = false;
        gameStarted = false;
        gamePaused = false;
    }
}
