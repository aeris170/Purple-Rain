package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gui.Window;

/**
 * @author Doða Oruç <doga.oruc.tr@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public class Handler {

	/** The Constant INSTANCE, handler is singleton. */
	private static final Handler INSTANCE = new Handler();

	private static int MAX = 20;

	private boolean halted;
	private Random r;
	private List<Rain> list;

	/**
	 * Instantiates a new handler.
	 */
	private Handler() {
		halted = false;
		list = new ArrayList<>();
		r = new Random();
	}

	/**
	 * Gets the single instance of Handler.
	 *
	 * @return single instance of Handler
	 */
	public static Handler getInstance() {
		return INSTANCE;
	}

	/**
	 * Gets the max raindrop per tick.
	 *
	 * @return the max raindrop per tick
	 */
	public static int getMAX() {
		return MAX;
	}

	/**
	 * Sets the max raindrop per tick.
	 *
	 * @param max the new max raindrop per tick
	 */
	public static void setMAX(int max) {
		MAX = max;
	}

	/**
	 * Halt the handling process.
	 */
	public void halt() {
		halted = true;
	}

	/**
	 * Resume the handling process.
	 */
	public void unhalt() {
		halted = false;
	}

	/**
	 * Add new raindrop to the handler's list of processing.
	 */
	public void add() {
		if (!halted) {
			for (int i = 0; i < MAX; i++) {
				Rain rain = new Rain(r.nextInt(Rain.getDefaultRainWidth()), r.nextInt(Rain.getDefaultRainHeight()), r.nextInt(Window.getWindowWidth()), 0);
				list.add(rain);
			}
		}
	}

	/**
	 * Gets raindrops.
	 *
	 * @return the raindrops that are being handled
	 */
	public List<Rain> getRains() {
		return list;
	}

	/**
	 * Handle the raindrops. Move them down, remove the ones that went
	 * off-screen. Add new ones depending on how much raindrop is removed on the
	 * previous step.
	 */
	public void handle() {
		if (!halted) {
			for (int i = 0; i < list.size(); i++) {
				Rain rain = list.get(i);
				rain.moveDown();
				if (rain.getYPos() > Window.getWindowHeight()) {
					list.remove(i);
				}
			}
			for (int i = list.size(); i < MAX; i++) {
				Rain rain = new Rain(r.nextInt(Rain.getDefaultRainWidth()), r.nextInt(Rain.getDefaultRainHeight()), r.nextInt(Window.getWindowWidth()), 0);
				list.add(rain);
			}
		}
	}
}