package gui;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

/**
 * The Class SpinSlider.
 *
 * @author trashgod
 * @version 1.11
 * @since 1.11
 * @see "https://stackoverflow.com/questions/6067898"
 */
public class SpinSlider extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4628802037851293886L;

	private static final int MAX = 255;
	private static final int MIN = 0;

	private static final int SLIDER_MAJOR_TICK_SPACE = 64;
	private static final int SLIDER_MINOR_TICK_SPACE = 8;

	boolean sliderFired = false;
	JSpinner spinner;
	JSlider slider;

	/**
	 * Instantiates a new spin slider.
	 *
	 * @param initialValue self-explanatory
	 */
	public SpinSlider(final int initialValue) {
		setLayout(new FlowLayout());

		spinner = new JSpinner();
		slider = new JSlider(MIN, MAX, initialValue);

		slider.setMinorTickSpacing(SLIDER_MINOR_TICK_SPACE);
		slider.setMajorTickSpacing(SLIDER_MAJOR_TICK_SPACE);
		slider.setPaintTicks(true);
		slider.addChangeListener(e -> {
			final JSlider s = (JSlider) e.getSource();
			if (!sliderFired) {
				spinner.setValue(s.getValue());
			}
		});
		this.add(slider);

		spinner.setModel(new SpinnerNumberModel(initialValue, MIN, MAX, 1));
		spinner.addChangeListener(e -> {
			final JSpinner s = (JSpinner) e.getSource();
			sliderFired = true;
			slider.setValue((int) s.getValue());
			sliderFired = false;
		});
		this.add(spinner);
	}

	public void addChangeListener(final ChangeListener ch) {
		slider.addChangeListener(ch);
		spinner.addChangeListener(ch);
	}

	/**
	 * Gets the value of the slider.
	 *
	 * @return the value of the slider
	 */
	public int getValue() {
		return slider.getValue();
	}

	/**
	 * Sets the value of the slider.
	 *
	 * @param value the new value
	 */
	public void setValue(final int value) {
		slider.setValue(value);
	}
}