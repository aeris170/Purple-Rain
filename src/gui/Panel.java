package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import logic.Handler;
import logic.Rain;

/**
 * @author Doða Oruç <doga.oruc.tr@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public class Panel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2674100066714668254L;

	private static Random r;
	private static Color globalRainColor;
	private static boolean userSelection, LSDSelection, rainbowSelection;

	private Timer timer;
	private Handler handler;

	/**
	 * Instantiates a new panel.
	 */
	public Panel() {
		super();
		r = new Random();
		globalRainColor = new Color(127, 0, 255, 100);
		userSelection = true;
		LSDSelection = false;
		rainbowSelection = false;
		handler = Handler.getInstance();
		timer = new Timer(10, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handler.handle();
				handler.add();
				repaint();
			}
		});
		timer.start();
		setOpaque(false);
	}

	/**
	 * Gets the global rain color.
	 *
	 * @return the global rain color
	 */
	public static Color getGlobalRainColor() {
		return globalRainColor;
	}

	/**
	 * Sets the global rain color.
	 *
	 * @param r the r
	 * @param g the g
	 * @param b the b
	 * @param a the a
	 */
	public static void setGlobalRainColor(int r, int g, int b, int a) {
		globalRainColor = new Color(r, g, b, a);
	}

	/**
	 * Sets the global rain color.
	 *
	 * @param c the new global rain color
	 */
	public static void setGlobalRainColor(Color c) {
		globalRainColor = c;
	}

	/**
	 * User is selecting the rain color.
	 */
	public static void userSelect() {
		userSelection = true;
		LSDSelection = false;
		rainbowSelection = false;
	}

	/**
	 * LSD rain preset is selected.
	 */
	public static void LSDSelect() {
		userSelection = false;
		LSDSelection = true;
		rainbowSelection = false;
	}

	/**
	 * Rainbow rain preset is selected.
	 */
	public static void rainbowSelect() {
		userSelection = false;
		LSDSelection = false;
		rainbowSelection = true;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (userSelection) {
			g.setColor(globalRainColor); //use global rain color
		} else if (LSDSelection) {
			g.setColor(new Color(r.nextInt(255), r.nextInt(255), (r.nextInt(255)))); //use a random color on each tick
		}
		for (int i = 0; i < handler.getRains().size(); i++) {
			final Rain rain = handler.getRains().get(i);
			if (!userSelection && !LSDSelection && rainbowSelection) {
				g.setColor(rain.getColor()); //use the built-in random random for each rain
			}
			g.fillRect(rain.getXPos(), rain.getYPos(), rain.getWidth(), rain.getHeight()); //clear unnecessary trails formed by graphics context
		}
	}
}
