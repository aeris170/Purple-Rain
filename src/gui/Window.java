package gui;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

import logic.Handler;

/**
 * The Class Window.
 *
 * @author Do�a Oru� <doga.oruc.tr@gmail.com>
 * @version 1.1
 * @since 1.0
 */
public class Window extends JWindow {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8057422959109245215L;

	/** The Constant VERSION NUMBER. */
	public static final String VERSION = "1.2";

	/** The Constant WIDTH. */
	@SuppressWarnings("hiding")
	private static int WIDTH;

	/** The Constant HEIGHT. */
	@SuppressWarnings("hiding")
	private static int HEIGHT;

	/** The Constant SCREEN_SIZE. */
	private static Dimension SCREEN_SIZE;

	/**
	 * Gets the window width.
	 *
	 * @return the window width
	 */
	public static int getWindowWidth() {
		return WIDTH;
	}

	/**
	 * Gets the window height.
	 *
	 * @return the window height
	 */
	public static int getWindowHeight() {
		return HEIGHT;
	}

	/**
	 * The main method.
	 *
	 * @param args the command line arguments
	 */
	public static void main(final String[] args) {
		final Rectangle2D result = new Rectangle2D.Double();
		final GraphicsEnvironment localGE = GraphicsEnvironment.getLocalGraphicsEnvironment();
		for (final GraphicsDevice gd : localGE.getScreenDevices()) {
			for (final GraphicsConfiguration graphicsConfiguration : gd.getConfigurations()) {
				Rectangle2D.union(result, graphicsConfiguration.getBounds(), result);
			}
		}
		WIDTH = (int) result.getWidth();
		HEIGHT = (int) result.getHeight();
		SCREEN_SIZE = new Dimension(WIDTH, HEIGHT);
		System.out.println(SCREEN_SIZE);
		SwingUtilities.invokeLater(() -> createAndShowGUI());
	}

	/**
	 * Creates a GUI on EDT.
	 */
	private static void createAndShowGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}
		final Window purpleRain = new Window();
		createAndShowTray(purpleRain);
		purpleRain.add(new Panel());
		purpleRain.setLocation(0, 0);
		purpleRain.setMinimumSize(SCREEN_SIZE);
		purpleRain.setPreferredSize(SCREEN_SIZE);
		purpleRain.setMaximumSize(SCREEN_SIZE);
		purpleRain.setAlwaysOnTop(true);
		purpleRain.setBackground(new Color(0, 0, 0, 0));
		Settings.initializeSettings();
		purpleRain.setVisible(true);
		setTransparent(purpleRain);
	}

	/**
	 * Creates and shows tray icon.
	 *
	 * @param window the window
	 */
	@SuppressWarnings("unused")
	private static void createAndShowTray(final Window window) {
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			System.exit(-1);
		}
		final PopupMenu popup = new PopupMenu();
		TrayIcon trayIcon = null;
		try {
			final BufferedImage bf = ImageIO.read(Window.class.getResourceAsStream("/trayIcon.png"));
			trayIcon = new TrayIcon(bf, "Purple-Rain " + VERSION);
		} catch (IOException | IllegalArgumentException ex) {
			ex.printStackTrace();
			System.exit(-2);
		}
		final SystemTray tray = SystemTray.getSystemTray();

		final CheckboxMenuItem pauseItem = new CheckboxMenuItem("Pause");
		pauseItem.addItemListener(e -> {
			if (pauseItem.getState()) {
				Handler.getInstance().halt();
			} else {
				Handler.getInstance().unhalt();
			}
		});

		final CheckboxMenuItem hideItem = new CheckboxMenuItem("Hide");
		hideItem.addItemListener(e -> {
			if (hideItem.getState()) {
				pauseItem.setState(true);
				Handler.getInstance().halt();
				window.setVisible(false);
			} else {
				pauseItem.setState(false);
				Handler.getInstance().unhalt();
				window.setVisible(true);
			}
		});

		final MenuItem aboutItem = new MenuItem("About");
		aboutItem.addActionListener(e -> {
			try {
				JOptionPane.showMessageDialog(null, "Written in tribute to Prince", "About", JOptionPane.OK_OPTION,
				        new ImageIcon(ImageIO.read(Window.class.getResourceAsStream("/trayIcon.png"))));
			} catch (HeadlessException | IOException ex) {
				ex.printStackTrace();
			}
		});

		final MenuItem settingsItem = new MenuItem("Settings");
		settingsItem.addActionListener(e -> {
			new Settings();
		});

		final MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(e -> {
			for (final TrayIcon ti : tray.getTrayIcons()) {
				tray.remove(ti);
			}
			System.exit(0);
		});

		popup.add(pauseItem);
		popup.add(hideItem);
		popup.addSeparator();
		popup.add(aboutItem);
		popup.add(settingsItem);
		popup.addSeparator();
		popup.add(exitItem);

		trayIcon.setImageAutoSize(true);
		trayIcon.setPopupMenu(popup);
		trayIcon.setToolTip("Purple-Rain " + VERSION);

		try {
			tray.add(trayIcon);
		} catch (final AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
		trayIcon.displayMessage("Minimised", "Purple-Rain is running in System Tray.", TrayIcon.MessageType.INFO);
	}

	// https://stackoverflow.com/questions/11217660/java-making-a-window-click-through-including-text-images
	// By Joe C
	/**
	 * Sets a component transparent(click-through).
	 *
	 * @param w the window which to be made transparent
	 */
	private static void setTransparent(final Component w) {
		final WinDef.HWND hwnd = getHWnd(w);
		int wl = User32.INSTANCE.GetWindowLong(hwnd, WinUser.GWL_EXSTYLE);
		wl = wl | WinUser.WS_EX_LAYERED | WinUser.WS_EX_TRANSPARENT;
		User32.INSTANCE.SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, wl);
	}

	/**
	 * Get the window handle from the OS.
	 *
	 * @param w the window
	 * @return the window handle
	 */
	private static HWND getHWnd(final Component w) {
		final HWND hwnd = new HWND();
		hwnd.setPointer(Native.getComponentPointer(w));
		return hwnd;
	}
}