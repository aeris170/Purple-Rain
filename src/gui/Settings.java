package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import logic.Handler;

/**
 * @author Doða Oruç <doga.oruc.tr@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public final class Settings extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8165468108436061431L;

	// Preference key names
	/** The Key Constant RED. */
	private static final String RED = "red";

	/** The Key Constant GREEN. */
	private static final String GREEN = "green";

	/** The Key Constant BLUE. */
	private static final String BLUE = "blue";

	/** The Key Constant ALPHA. */
	private static final String ALPHA = "alpha";

	/** The Key Constant MAX. */
	private static final String MAX = "max";

	/** The Key Constant SPECIAL. */
	private static final String SPECIAL = "special";

	/** The Key Constant LSD. */
	private static final String LSD = "lsd";

	/** The Key Constant RAINBOW. */
	private static final String RAINBOW = "rainbow";

	private JPanel settingsPanel;
	private JSlider rSlider, gSlider, bSlider, aSlider;
	private JSpinner maxSpinner;
	private JCheckBox special;
	private JRadioButton lsd, rainbow;
	private JButton applyButton, cancelButton;

	/**
	 * Instantiates a new settings.
	 */
	public Settings() {
		settingsPanel = new JPanel();
		settingsPanel.setLayout(new BorderLayout());

		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		JPanel south = new JPanel();
		south.setLayout(new BoxLayout(south, BoxLayout.X_AXIS));
		JPanel boxesNbuttonsPanel = new JPanel();
		boxesNbuttonsPanel.setLayout(new BoxLayout(boxesNbuttonsPanel, BoxLayout.X_AXIS));

		final Color rainColor = Panel.getGlobalRainColor();

		rSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 255, rainColor.getRed());
		gSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 255, rainColor.getGreen());
		bSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 255, rainColor.getBlue());
		aSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 255, rainColor.getAlpha());

		maxSpinner = new JSpinner(new SpinnerNumberModel(Handler.getMAX(), 0, 1000, 1));

		special = new JCheckBox("Special Effects: ");

		lsd = new JRadioButton("LSD Rain");
		rainbow = new JRadioButton("Rainbow Rain");

		lsd.setEnabled(false);
		rainbow.setEnabled(false);

		ButtonGroup bg = new ButtonGroup();
		bg.add(lsd);
		bg.add(rainbow);

		rSlider.setBorder(BorderFactory.createTitledBorder("Red"));
		gSlider.setBorder(BorderFactory.createTitledBorder("Green"));
		bSlider.setBorder(BorderFactory.createTitledBorder("Blue"));
		aSlider.setBorder(BorderFactory.createTitledBorder("Alpha"));

		maxSpinner.setBorder(BorderFactory.createTitledBorder("MAX new drops per tick"));

		rSlider.addChangeListener(e -> {
			Panel.setGlobalRainColor(rSlider.getValue(), gSlider.getValue(), bSlider.getValue(), aSlider.getValue());
		});
		gSlider.addChangeListener(e -> {
			Panel.setGlobalRainColor(rSlider.getValue(), gSlider.getValue(), bSlider.getValue(), aSlider.getValue());
		});
		bSlider.addChangeListener(e -> {
			Panel.setGlobalRainColor(rSlider.getValue(), gSlider.getValue(), bSlider.getValue(), aSlider.getValue());
		});
		aSlider.addChangeListener(e -> {
			Panel.setGlobalRainColor(rSlider.getValue(), gSlider.getValue(), bSlider.getValue(), aSlider.getValue());
		});

		maxSpinner.addChangeListener(e -> {
			Handler.setMAX((int) maxSpinner.getValue());
		});

		special.addChangeListener(e -> {
			if (special.isSelected()) {
				lsd.setEnabled(true);
				rainbow.setEnabled(true);
				rSlider.setEnabled(false);
				gSlider.setEnabled(false);
				bSlider.setEnabled(false);
				aSlider.setEnabled(false);
			} else {
				lsd.setEnabled(false);
				rainbow.setEnabled(false);
				rSlider.setEnabled(true);
				gSlider.setEnabled(true);
				bSlider.setEnabled(true);
				aSlider.setEnabled(true);
				bg.clearSelection();
				Panel.userSelect();
			}
		});

		lsd.addActionListener(e -> {
			if (lsd.isEnabled() && lsd.isSelected()) {
				Panel.LSDSelect();
			}
		});

		rainbow.addActionListener(e -> {
			if (rainbow.isEnabled() && rainbow.isSelected()) {
				Panel.rainbowSelect();
			}
		});

		applyButton = new JButton("  OK  ");
		applyButton.addActionListener(e -> {
			saveNewSettingsToHierarchialNode();
			dispose();
		});

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> {
			retrieveSettingsFromHierarchialNode();
			dispose();
		});

		boxesNbuttonsPanel.add(special);
		boxesNbuttonsPanel.add(lsd);
		boxesNbuttonsPanel.add(rainbow);

		center.add(rSlider);
		center.add(gSlider);
		center.add(bSlider);
		center.add(aSlider);
		center.add(maxSpinner);
		center.add(boxesNbuttonsPanel);

		south.add(Box.createHorizontalStrut(25));
		south.add(applyButton);
		south.add(Box.createHorizontalStrut(25));
		south.add(cancelButton);

		settingsPanel.add(center, BorderLayout.CENTER);
		settingsPanel.add(south, BorderLayout.SOUTH);

		super.add(settingsPanel);
		super.setResizable(false);
		super.pack();
		super.setLocationByPlatform(true);
		retrieveSettingsFromHierarchialNode();
		super.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				retrieveSettingsFromHierarchialNode();
				dispose();
			}
		});
		super.setVisible(true);
	}

	/**
	 * Initialize settings to accurately represent saved data.
	 */
	public static void initializeSettings() {
		// Retrieve the user preference node for the this class
		Preferences prefs = Preferences.userNodeForPackage(Settings.class);

		Panel.setGlobalRainColor(new Color(prefs.getInt(RED, 127), prefs.getInt(GREEN, 0), prefs.getInt(BLUE, 255), prefs.getInt(ALPHA, 100)));
		Handler.setMAX(prefs.getInt(MAX, 20));
		if (prefs.getBoolean(SPECIAL, false)) {
			if (prefs.getBoolean(LSD, false)) {
				Panel.LSDSelect();
			} else if (prefs.getBoolean(RAINBOW, false)) {
				Panel.rainbowSelect();
			}
		} else {
			Panel.userSelect();
		}
	}

	/**
	 * Retrieves the user specified settings from the hierarchial node for this
	 * class.
	 */
	private void retrieveSettingsFromHierarchialNode() {
		// Retrieve the user preference node for the this class
		Preferences prefs = Preferences.userNodeForPackage(getClass());

		Panel.setGlobalRainColor(prefs.getInt(RED, 127), prefs.getInt(GREEN, 0), prefs.getInt(BLUE, 255), prefs.getInt(ALPHA, 100));
		Handler.setMAX(prefs.getInt(MAX, 20));

		final Color rainColor = Panel.getGlobalRainColor();
		final int max = Handler.getMAX();

		rSlider.setValue(rainColor.getRed());
		gSlider.setValue(rainColor.getGreen());
		bSlider.setValue(rainColor.getBlue());
		aSlider.setValue(rainColor.getAlpha());

		maxSpinner.setValue(max);

		if (prefs.getBoolean(SPECIAL, false)) {
			special.setSelected(true);
			lsd.setEnabled(true);
			rainbow.setEnabled(true);
			if (prefs.getBoolean(LSD, false)) {
				lsd.setSelected(true);
			} else if (prefs.getBoolean(RAINBOW, false)) {
				rainbow.setSelected(true);
			}
		}
	}

	/**
	 * Saves the user specified settings to the hierarchial node for this class.
	 */
	private void saveNewSettingsToHierarchialNode() {
		try {
			// Retrieve the user preference node for this class
			Preferences prefs = Preferences.userNodeForPackage(getClass());

			// Set the value of the preferences
			prefs.putInt(RED, rSlider.getValue());
			prefs.putInt(GREEN, gSlider.getValue());
			prefs.putInt(BLUE, bSlider.getValue());
			prefs.putInt(ALPHA, aSlider.getValue());
			prefs.putInt(MAX, (int) maxSpinner.getValue());
			prefs.putBoolean(SPECIAL, special.isSelected());
			prefs.putBoolean(LSD, lsd.isSelected());
			prefs.putBoolean(RAINBOW, rainbow.isSelected());

			// Save the changes to registry(if windows, else idk where)
			prefs.flush();
		} catch (BackingStoreException ex) {
			ex.printStackTrace();
		}
	}
}
