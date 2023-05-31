package edu.cis;
import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

public class Breakout extends GraphicsProgram {

	private RandomGenerator rgen = RandomGenerator.getInstance();

	double vx;
	double vy;
	public static final int BRICK_SEP = 4;
	public static final int BRICK_HEIGHT = 12;
	public static final int CANVAS_WIDTH = 420;
	public static final int CANVAS_HEIGHT = 600;

	public static final double PADDLE_WIDTH = 60;
	public static final double PADDLE_HEIGHT = 10;
	public static final double START_X = (CANVAS_WIDTH - 10) / 2.0;
	public static final double START_Y = 400 - PADDLE_HEIGHT - 10;

	public static final double DELAY = 1000.0 / 200.0;

	private long startTime;

	// Number of turns
	GRect MyPaddle;
	GRect MyBrick;
	GOval myBall;

	public void init() {
		// Set the title of the game
		setTitle("CIS Y11 CS Breakout");
		// Initialize array of colors for bricks
		Color clr[] = {Color.RED, Color.RED, Color.ORANGE, Color.ORANGE, Color.YELLOW, Color.YELLOW, Color.GREEN, Color.GREEN, Color.CYAN, Color.CYAN};

		// Set canvas size
		setCanvasSize(420, 600);
		// Create bricks
		for (int i = 1; i <= 10; i++) {
			for (int j = 0; j < 10; j++) {
				makeBrick(clr[i - 1], j * 42 + BRICK_SEP, i * BRICK_HEIGHT);
			}
		}

		// Create paddle and ball
		makePaddle();
		makeBall();
		// Add mouse listeners
		addMouseListeners();
	}

	public void run() {
		play();
	}
	private void makeBall() {
		// Create the ball
		myBall = new GOval(START_X, START_Y, 10, 10);
		myBall.setFilled(true);
		myBall.setColor(Color.BLACK);
		add(myBall);
	}
	public GObject getCollidingObject()
	{
		GObject elem = getElementAt(myBall.getX()+10,myBall.getY()+10);
		return(elem);
	}
	public void makeBrick(Color cl, double xpos, double ypos)
	{
		// Create a brick
		GRect gr = new GRect( 40, BRICK_HEIGHT);
		gr.setFillColor(cl);
		gr.setFilled(true);
		gr.setLocation(xpos,ypos+40);
		add(gr);
	}
	public void play() {
		waitForClick();
		startTime = System.currentTimeMillis();
		// Initialize the ball velocity
		RandomGenerator rgen = RandomGenerator.getInstance();
		vx = rgen.nextDouble(1.0, 3.0) / 2;
		if (rgen.nextBoolean(0.5)) vx = -vx;
		vy = -2;

		// Game loop
		while (true) {
			myBall.move(vx, vy);

			// Check for collisions with walls
			if (myBall.getX() <= 0 || myBall.getX() + 10 >= CANVAS_WIDTH) {
				vx = -vx;
			}
			if (myBall.getY() <= 0) {
				vy = -vy;
			}

			// Check for game over conditions
			if ((myBall.getY() > 610) || (myBall.getY() > MyPaddle.getY() + MyPaddle.getHeight())) {
				setTitle("GAME OVER!");
				// Calculate time elapsed
				long endTime = System.currentTimeMillis();
				long timeElapsed = (endTime - startTime) / 1000;

				// Prompt user to enter their name and store the high score
				String name = JOptionPane.showInputDialog("Enter your name: ");
				HighScore score = new HighScore();
				score.addScore(new Score(name, (int) timeElapsed));
				System.exit(0);
			}

			// Check for collisions with other objects
			GObject Collider = getCollidingObject();

			if (Collider == null) {

			} else if (Collider == MyPaddle) {
				vy = -vy;
			} else {
				remove(Collider);
				vy = -vy;
			}

			// Pause the game loop for the specified delay
			pause(DELAY);
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT);
	}

	public void makePaddle()
	{
		// Create the paddle
		MyPaddle = new GRect(PADDLE_WIDTH,PADDLE_HEIGHT);
		MyPaddle.setFillColor(Color.BLACK);
		MyPaddle.setFilled(true);
		MyPaddle.setLocation(160,400);
		add(MyPaddle);
	}

	public void mouseMoved(MouseEvent e)
	{
		// Move the paddle based on the mouse position
		if(e.getX()<1)
		{
			MyPaddle.setLocation(1,400);
		}
		else if(e.getX()>360)
		{
			MyPaddle.setLocation(360,400);
		}
		else
		{
			MyPaddle.setLocation(e.getX(),400);
		}
	}

	public static void main(String[] args)
	{
		new Breakout().start();
	}

}