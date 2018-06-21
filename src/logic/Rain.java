package logic;

import java.awt.Color;
import java.util.Random;

/**
 * @author Doða Oruç <doga.oruc.tr@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public class Rain {

	/** The Constant CHANGE_IN_Y_POSITION_PER_TICK. */
	private static final int CHANGE_IN_Y_POSITION_PER_TICK = 3;

	/** The Constant DEFAULT_RAIN_WIDTH. */
	private static final int DEFAULT_RAIN_WIDTH = 3;

	/** The Constant DEFAULT_RAIN_HEIGHT. */
	private static final int DEFAULT_RAIN_HEIGHT = 25;

	/** Random object to randomly position and paint raindrops. */
	private static Random r = new Random();

	/** Width of a raindrop. */
	private int width;

	/** Height of a raindrop. */
	private int height;

	/** The y pos of a raindrop. */
	private int yPos;

	/** The x pos of a raindrop. */
	private int xPos;

	/** The color of a raindrop. */
	private Color color;

	/**
	 * Instantiates a new rain.
	 *
	 * @param width the width of a raindrop
	 * @param height the height of a raindrop
	 * @param xPos the x pos of a raindrop
	 * @param yPos the y pos of a raindrop
	 */
	public Rain(final int width, final int height, final int xPos, final int yPos) {
		this.width = width;
		this.height = height;
		this.xPos = xPos;
		this.yPos = yPos;
		this.color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
	}

	/**
	 * Gets the width of the raindrop.
	 *
	 * @return the width of the raindrop
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height of the raindrop.
	 *
	 * @return the height of the raindrop
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the y pos of the raindrop.
	 *
	 * @return the y pos of the raindrop
	 */
	public int getYPos() {
		return yPos;
	}

	/**
	 * Gets the x pos of the raindrop.
	 *
	 * @return the x pos of the raindrop
	 */
	public int getXPos() {
		return xPos;
	}

	/**
	 * Move down the raindrop by CHANGE_IN_Y_POSITION_PER_TICK pixels.
	 */
	public void moveDown() {
		yPos = yPos + CHANGE_IN_Y_POSITION_PER_TICK;
	}

	/**
	 * Gets the default rain width.
	 *
	 * @return the default rain width
	 */
	public static int getDefaultRainWidth() {
		return DEFAULT_RAIN_WIDTH;
	}

	/**
	 * Gets the default rain height.
	 *
	 * @return the default rain height
	 */
	public static int getDefaultRainHeight() {
		return DEFAULT_RAIN_HEIGHT;
	}

	/**
	 * Gets the color of the raindrop.
	 *
	 * @return the color of the raindrop
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color of the raindrop.
	 *
	 * @param color the new color of the raindrop
	 */
	public void setColor(Color color) {
		this.color = color;
	}
}