import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * SpaceInvader is a JavaFX-based game where the player controls the ship
 * and destroy enemy ships and avoid penalties(-score) while collecting rewards(+score,enchanted firing).
 */

public class SpaceInvader extends Application {

    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int PLAYER_SPEED = 4;
    private final int ENEMY_SPEED = 2;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;


    private double playerX = WIDTH / 2 - 25;
    private double playerY = HEIGHT -50;
    private List<double[]> bullets = new ArrayList<>();
    private List<double[]> enemies = new ArrayList<>();
    private List<double[]> drops = new ArrayList<>();


    private boolean enhancedFiring = false;
    private long enhancedFiringEndTime = 0;
    private GameManager gameManager = new GameManager();

    private String message = "";
    private Color messageColor;
    private long lastMessageTime = 0;


    private GraphicsContext gc;

    private Image mainImage ;
    private Image playerImage;
    private Image enemyImage;
    private Image rewardImage;
    private Image punishmentImage;

    /**
     * Main method to launch the game.
     * @param args Command-line arguments, including paths to image files.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes and starts the JavaFX application.
     * Prepares the canvas, loads images, and sets up key event listeners for player input.
     * Creates the primary animation loop for handling game logic and drawing.
     * @param primaryStage The primary stage for the game.
     */

    @Override
    public void start(Stage primaryStage) {
        Parameters params = getParameters();
        List<String> args = params.getRaw();
        mainImage = new Image("file:" + args.get(0));
        playerImage = new Image("file:" + args.get(1));
        enemyImage = new Image("file:" + args.get(2));
        rewardImage = new Image("file:" + args.get(3));
        punishmentImage = new Image("file:" + args.get(4));
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        Scene scene = new Scene(new javafx.scene.Group(canvas));
        scene.setFill(Color.BLACK);

        /*
          Handles key press events.
         */
        scene.setOnKeyPressed(pressedKey -> {
            KeyCode code = pressedKey.getCode();
            if(!gameManager.isGameStarted()){
                if (code == KeyCode.ESCAPE) {
                    System.exit(0);
                } else if (code == KeyCode.ENTER) {
                    gameManager.startGame();
                }
            }else {
                if (code == KeyCode.LEFT) {
                    leftPressed = true;
                } else if (code == KeyCode.RIGHT) {
                    rightPressed = true;
                } else if (code == KeyCode.SPACE) {
                    spacePressed = true;
                } else if (code == KeyCode.P) {
                    gameManager.togglePause();
                } else if (code == KeyCode.R && gameManager.isGameOver()) {
                    resetGame();
                    gameManager.startGame();
                } else if (code == KeyCode.ESCAPE) {
                    gameManager.stopGame();
                    showTitleScreen();
                    resetGame();
                }
            }
        });
        /*
         * Handles key release events.
         */
        scene.setOnKeyReleased(releasedKey -> {
            KeyCode code = releasedKey.getCode();
            if (code == KeyCode.LEFT) {
                leftPressed = false;
            } else if (code == KeyCode.RIGHT) {
                rightPressed = false;
            } else if (code == KeyCode.SPACE) {
                spacePressed = false;}
        });
        primaryStage.getIcons().add(mainImage);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Space Invader");
        primaryStage.show();

        AnimationTimer gameLoop = new AnimationTimer() {
            private long lastEnemySpawn = 0;
            /**
             * The main  loop , executed continually during the game runtime.
             * It handles all the gaming logic , including player movement, shooting,
             * enemy spawning and movement, collision detection, and drawing all game
             * elements such as the player, enemies, bullets, and drops.
             *
             * @param now The timestamp of the current frame in nanoseconds.
             */
            @Override
            public void handle(long now) {

                if (!gameManager.isGameStarted()) {
                    showTitleScreen();
                    return;
                }

                if (gameManager.isGamePaused()) {
                    return;
                }

                if (gameManager.isGameOver()) {
                    drawGameOverScreen();
                    return;
                }
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, WIDTH, HEIGHT);
                gc.drawImage(playerImage, playerX, playerY, 50, 50);

                if (leftPressed && playerX > 0)
                    playerX = playerX- PLAYER_SPEED;
                if (rightPressed && playerX < WIDTH - 50)
                    playerX = playerX + PLAYER_SPEED;

                if (spacePressed) {
                    if (enhancedFiring) {
                        bullets.add(new double[]{playerX + 20, playerY, 1, 0});    // Straight up
                        bullets.add(new double[]{playerX + 20, playerY, 1, 2.5});  // Right diagonal
                        bullets.add(new double[]{playerX + 20, playerY, 1, -2.5}); // Left diagonal
                        spacePressed = false;
                    } else {
                        bullets.add(new double[]{playerX + 20, playerY, 0}); // Regular bullet
                        spacePressed = false;
                    }

                }

                if (enhancedFiring && System.currentTimeMillis() > enhancedFiringEndTime) {
                    enhancedFiring = false;
                }


                for (int i = 0; i < bullets.size(); i++) {
                    double[] bullet = bullets.get(i);

                    if (bullet[2] == 0) {
                        bullet[1] = bullet[1]- 5;
                        if (bullet[1] < playerY - 400) {
                            bullets.remove(i);
                            continue;
                        }
                    } else if (bullet[2] == 1) {
                        bullet[1] =bullet[1]- 5;
                        bullet[0] =bullet[0]+ bullet[3];
                    }


                    if (bullet[1] < 0 || bullet[0] < 0 || bullet[0] > WIDTH) {
                        bullets.remove(i);
                    }
                }


                if (now - lastEnemySpawn> 2_000_000_000) {
                    enemies.add(new double[]{new Random().nextInt(WIDTH - 50), 0});
                    lastEnemySpawn = now;
                }


                for (int i = 0; i < enemies.size(); i++) {
                    double[] enemy = enemies.get(i);
                    enemy[1] =enemy[1]+ ENEMY_SPEED;
                    if (enemy[1] >= HEIGHT) {
                        enemies.remove(i);
                        i=i-1;
                    }
                }


                for (int i = 0; i < drops.size(); i++) {
                    double[] drop = drops.get(i);
                    drop[1] =drop[1]+ ENEMY_SPEED;
                    if (drop[1] >= HEIGHT) {
                        drops.remove(i);
                    }
                }


                handleCollisions();



                for (int i = 0; i < bullets.size(); i++) {
                    double[] bullet = bullets.get(i);
                    gc.setFill(Color.YELLOW);
                    gc.fillRect(bullet[0], bullet[1], 5, 10);
                }

                for (int i = 0; i < enemies.size(); i++) {
                    double[] enemy = enemies.get(i);
                    gc.drawImage(enemyImage, enemy[0], enemy[1], 50, 50);
                }



                for (int i = 0; i < drops.size(); i++) {
                    double[] drop = drops.get(i);
                    Image dropImage;

                    if (drop[2] == 0) {
                        dropImage = punishmentImage;
                    } else {
                        dropImage = rewardImage;
                    }
                    gc.drawImage(dropImage, drop[0], drop[1], 30, 30);
                }


                gc.setFill(Color.WHITE);
                gc.fillText("Score: " + gameManager.getScore(), 10, 40);

                if (message.equals("Enhanced Firing Activated!")) {
                    if (System.currentTimeMillis() - lastMessageTime < 5000) {
                        gc.setFill(messageColor);
                        gc.fillText(message, 10, 70);
                    }
                } else {
                    if(enhancedFiring){
                        gc.setFill(Color.GREEN);
                        gc.fillText("Enhanced Firing Activated!", 10, 70);
                        gc.setFill(messageColor);
                        gc.fillText(message, 10, 100);
                    }
                    else if (System.currentTimeMillis() - lastMessageTime < 2000) {
                        gc.setFill(messageColor);
                        gc.fillText(message, 10, 70);
                    }
                }
            }
        };

        gameLoop.start();
    }

    /**
     * Determines whether two rectangular areas are colliding based on their coordinates and dimensions.
     * @param x1 The x-coordinate of the first rectangle's top-left corner.
     * @param y1 The y-coordinate of the first rectangle's top-left corner.
     * @param w1 The width of the first rectangle.
     * @param h1 The height of the first rectangle.
     * @param x2 The x-coordinate of the second rectangle's top-left corner.
     * @param y2 The y-coordinate of the second rectangle's top-left corner.
     * @param w2 The width of the second rectangle.
     * @param h2 The height of the second rectangle.
     * @return  true if the two rectangles are colliding
     */
    private boolean isColliding(double x1, double y1, double w1, double h1, double x2, double y2, double w2, double h2) {
        boolean Collided = false;
        if (x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2){
            Collided = true;
        }
        return Collided;
    }
    /**
     * Handles all collisions in the game with the help of isColliding method.
     *  Checks if the player collides with enemies, ending the game if collides.
     *  Checks if bullets hit enemies. Removes both, adds 100 points, and may create a drop by the %60 percent chance.
     *  Checks if the player collects drops. Applies penalties or rewards based on drop type.
     */

    private void handleCollisions() {

        for (int i = 0; i < enemies.size(); i++) {
            double[] enemy = enemies.get(i);
            if (isColliding(playerX, playerY, 50, 50, enemy[0], enemy[1], 50, 50)) {
                gameManager.setGameOver();
                return;
            }
        }


        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < enemies.size(); j++) {
                double[] bullet = bullets.get(i);
                double[] enemy = enemies.get(j);


                if (isColliding(bullet[0], bullet[1], 5, 10, enemy[0], enemy[1], 50, 50)) {
                    bullets.remove(i);
                    enemies.remove(j);


                    if (new Random().nextInt(10) < 6) {
                        int dropType = new Random().nextInt(3);
                        drops.add(new double[]{enemy[0], enemy[1], dropType});
                    }

                    gameManager.increaseScore(100);
                    return;
                }
            }
        }


        for (int i = 0; i < drops.size(); i++) {
            double[] drop = drops.get(i);
            if (isColliding(playerX, playerY, 50, 50, drop[0], drop[1], 30, 30)) {
                if (drop[2] == 0) {
                    gameManager.decreaseScore();
                    message = "Penalty! -50 points";
                    messageColor = Color.RED;
                } else if (drop[2] == 1) { // Reward 1
                    gameManager.increaseScore(50);
                    message = "Reward! +50 points";
                    messageColor = Color.GREEN;
                } else if (drop[2] == 2) { // Reward 2
                    enhancedFiring = true;
                    enhancedFiringEndTime = System.currentTimeMillis() + 5000;
                    message = "Enhanced Firing Activated!";
                    messageColor = Color.GREEN;
                }
                lastMessageTime = System.currentTimeMillis();
                drops.remove(i);
            }
        }
    }

    /**
     * Resets the game state to its initial values:
     *  Clears all bullets, enemies, and drops.
     * The game manager is reset, including the score, game state, and pause state.
     *  Resets the score and player position.
     *  Disables enhanced firing and sets the game state to not over.
     */
    private void resetGame() {
        bullets.clear();
        enemies.clear();
        drops.clear();
        gameManager.reset();
        enhancedFiring = false;

        playerX = WIDTH / 2 -25;
        playerY = HEIGHT - 100;
    }
    /**
     * Displays the title screen with instructions:
     *  Clears the canvas.
     *  Draws the background image.
     *  Shows text to inform the player how to start or exit the game.
     */

    private void showTitleScreen() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.drawImage(mainImage, 0, 0, WIDTH, HEIGHT);
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 30));
        gc.setFill(Color.RED);
        gc.fillText("PRESS ENTER TO PLAY", WIDTH / 2 - 150, HEIGHT / 2-70);
        gc.fillText("PRESS ESC TO EXIT", WIDTH / 2 - 120, HEIGHT / 2 -30);
    }

    /**
     *  Displays the game-over screen:
     *  Fills the canvas with a black background.
     *  Shows the final score.
     *  Provides instructions for restarting the game (PRESS R TO RESTART).
     */

    private void drawGameOverScreen() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.RED);
        gc.fillText("GAME OVER", WIDTH / 2 - 125, HEIGHT / 2 - 20);
        gc.fillText("Score: " + gameManager.getScore(), WIDTH / 2 - 125, HEIGHT / 2 +20);
        gc.fillText("PRESS R TO RESTART", WIDTH / 2 - 125, HEIGHT / 2 + 60);
    }
}
