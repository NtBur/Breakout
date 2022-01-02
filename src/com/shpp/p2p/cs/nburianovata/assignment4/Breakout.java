package com.shpp.p2p.cs.nburianovata.assignment4;

import acm.graphics.*;
import acm.util.RandomGenerator;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Breakout extends WindowProgram {
    /**
     * Width and height of application window in pixels
     */
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 600;

    /**
     * Dimensions of game board (usually the same)
     */
    private static final int WIDTH = APPLICATION_WIDTH;
    private static final int HEIGHT = APPLICATION_HEIGHT;

    /**
     * Dimensions of the paddle
     */
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;

    /**
     * Offset of the paddle up from the bottom
     */
    private static final int PADDLE_Y_OFFSET = 30;

    /**
     * Offset of the paddle up from the left
     */
    private static final int PADDLE_X_OFFSET = 0;

    /**
     * Number of bricks per row
     */
    private static final int NBRICKS_PER_ROW = 10;

    /**
     * Number of rows of bricks
     */
    private static final int NBRICK_ROWS = 10;

    /**
     * Separation between bricks
     */
    private static final int BRICK_SEP = 4;

    /**
     * Width of a brick
     */
    private static final int BRICK_WIDTH =
            (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

    /**
     * Height of a brick
     */
    private static final int BRICK_HEIGHT = 8;

    /**
     * Radius of the ball in pixels
     */
    private static final int BALL_RADIUS = 10;

    /**
     * Offset of the top brick row from the top
     */
    private static final int BRICK_Y_OFFSET = 70;

    /**
     * Number of turns
     */
    private static final int NTURNS = 3;

    private static final double PAUSE_TIME = 10;
    /**
     * Lettering for labels if  don't winner
     */
    public static final String NOT_WINNER = "You are not a winner";
    /**
     * Lettering for labels if winner
     */
    public static final String WINNER = "You are the winner";
    /**
     * Font for labels
     */
    public static final String FONT = "SansSerif-20";

    /**
     * Creating a paddle for the game
     */
    private GRect paddle = createPaddle();

    /**
     * Creating a ball for the game
     */
    private GOval ball;

    /**
     * Coordinates to move the ball
     */
    private double vx, vy;

    /**
     * The amount of all the bricks
     */
    private int countBricks = NBRICKS_PER_ROW * NBRICK_ROWS;
    /**
     * Move the ball if isBallMoving - true
     */
    private boolean isBallMoving = true;

    /**
     * Create the top wall with bricks
     */
    private GRect brickWall = drawBrickWall();

    public void run() {
        addMouseListeners();
        createRound();
    }

    /**
     * Creating a black ball in the center
     */
    private void createBall() {
        ball = new GOval(WIDTH / 2 - BALL_RADIUS,
                HEIGHT / 2 - BALL_RADIUS,
                BALL_RADIUS * 2,
                BALL_RADIUS * 2);
        ball.setFilled(true);
        ball.setColor(Color.BLACK);
        add(ball);
    }

    /**
     * Creating three turns to destroy all the bricks
     */
    private void createRound() {
        for (int i = 0; i < NTURNS; i++) {
            isBallMoving = true;
            waitForClick();
            createBall();
            moveBall(ball);
        }
    }

    /**
     * Creating a  label in the center about the result of the game
     *
     * @param res - inscription: winner or don't winner
     */
    private void createWinner(String res) {
        GLabel winner = new GLabel(res);
        winner.setFont(FONT);
        double x = (getWidth() / 2 - winner.getWidth() / 2);
        double y = (getHeight() / 2 - winner.getDescent() / 2);
        add(winner, x, y);
    }

    /**
     * Reproducing the movement of the ball
     *
     * @param ball - the ball is created createBall()
     */
    private void moveBall(GOval ball) {
        //Random value of type double in the range from 1.0 to 3.0
        RandomGenerator rgen = RandomGenerator.getInstance();
        vx = rgen.nextDouble(1.0, 3.0);

        //Probability of 50%, which changes the sign
        // of the obtained number to minus
        if (rgen.nextBoolean(0.5)) vx = -vx;

        //Speed +3.0 for vy
        vy = +3.0;

        //if you can run the ball:  isBallMoving - true
        while (isBallMoving) {
            //Contact with the walls change the direction of movement
            if (ball.getY() < 0 || ball.getY() > (HEIGHT - BALL_RADIUS * 2)) vy = -vy;
            if (ball.getX() > (WIDTH - BALL_RADIUS * 2) || ball.getX() < 0) vx = -vx;

            //If you collide with a paddle - change the direction of movement
            GObject collider = getCollidingObject(ball.getX(), ball.getY());
            if (collider == paddle) vy = -vy;

            //If you collide with a brick -  destroy brick
            //reduce the number of bricks
            //and change the direction of movement
            if (collider != paddle) {
                if (collider != null) {
                    remove(collider);
                    countBricks--;
                    print(countBricks + " ");
                    vy = -vy;
                }
            }
            //If all the bricks are broken - the winner
            if (countBricks == 0) {
                createWinner(WINNER);
                isBallMoving = false;
            }
            //if  missed the ball -  lost
           /* if (ball.getY() > (HEIGHT - BALL_RADIUS * 2)) {
                createWinner(NOT_WINNER);
                remove(ball);
                isBallMoving = false;
            }*/
            ball.move(vx, vy);
            pause(0);
        }
    }

    /**
     * Checking for a collision with the ball
     *
     * @param x - position of the ball on the X
     * @param y - position of the ball on the Y
     * @return object  with which  the ball or null
     */
    private GObject getCollidingObject(double x, double y) {
        GObject object = getElementAt(x, y);
        if (object != null) {
            object = getElementAt(x, y);
        } else if (getElementAt(x + 2 * BALL_RADIUS, y) != null) {
            object = getElementAt(x + 2 * BALL_RADIUS, y);
        } else if (getElementAt(x, y + 2 * BALL_RADIUS) != null) {
            object = getElementAt(x, y + 2 * BALL_RADIUS);
        } else if (getElementAt(x + 2 * BALL_RADIUS, y + 2 * BALL_RADIUS) != null) {
            object = getElementAt(x + 2 * BALL_RADIUS, y + 2 * BALL_RADIUS);
        }
        return object;
    }

    /**
     * Creating a centered colored brick wall
     * at a height BRICK_Y_OFFSET
     * change colors in two rows
     *
     * @return brick wall
     */
    private GRect drawBrickWall() {
        brickWall = new GRect(WIDTH / 2 - (NBRICKS_PER_ROW * (BRICK_WIDTH + BRICK_SEP)) / 2,
                BRICK_Y_OFFSET,
                BRICK_WIDTH,
                BRICK_HEIGHT);
        Color[] colorsRow = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN};
        for (int i = 0; i < NBRICKS_PER_ROW; i++) {
            for (int j = 0; j < NBRICK_ROWS; j++) {
                GRect res = new GRect(
                        brickWall.getX() + (i * (BRICK_WIDTH + BRICK_SEP)),
                        brickWall.getY() + (j * (BRICK_HEIGHT + BRICK_SEP)),
                        BRICK_WIDTH,
                        BRICK_HEIGHT);
                //paint two rows
                fillRect(res, colorsRow[j / 2]);
                add(res);
            }
        }
        return brickWall;
    }

    /**
     * Fill the rectangles with color
     *
     * @param r     rectangle to fill
     * @param color to fill
     */
    private void fillRect(GRect r, Color color) {
        r.setFilled(true);
        r.setColor(color);
    }

    /**
     * Creating a black paddle
     * at a distance PADDLE_Y_OFFSET from the bottom
     *
     * @return paddle
     */
    private GRect createPaddle() {
        paddle = new GRect(PADDLE_X_OFFSET,
                (HEIGHT - PADDLE_HEIGHT - PADDLE_Y_OFFSET),
                PADDLE_WIDTH,
                PADDLE_HEIGHT);
        paddle.setFillColor(Color.BLACK);
        paddle.setFilled(true);
        add(paddle);
        return paddle;
    }

    /**
     * Changing the position of the paddle
     * depending on the movement of the mouse
     */
    public void mouseMoved(MouseEvent e) {
        //cursor in the center of the paddle
        double newX = e.getX() - PADDLE_WIDTH / 2;

        //paddle does not go beyond the game window
        if (newX <= 0) newX = 0;
        if ((newX + PADDLE_WIDTH) >= WIDTH) newX = (WIDTH - PADDLE_WIDTH);

        //paddle is stable on the Y
        double newY = paddle.getY();
        paddle.setLocation(newX, newY);
    }
}