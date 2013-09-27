package UwyGraph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ThemeEditor extends JFrame {
	private static final long serialVersionUID = 1L;

	private JButton okButton;
	private JButton saveButton;
	private JButton cancelButton;

	private JLabel ratioCircleLabel;
	private JLabel ratioDiskLabel;
	private JLabel minMouseStopLabel;
	private JLabel mouseStopLabel;
	private JSpinner ratioCircle;
	private JSpinner ratioDisk;
	private JSpinner minMouseStop;

	private JCheckBox useColors;
	private JCheckBox mouseStops;

	private JComboBox<String> themesSelector;

	private JComboBox<String> mouseStopSelector;

	private ThemeDatabase themeDatabase;
	private Theme editableTheme;
	private Core core;

	private ColorSrcEditor colorSrcEditor;

	private JPanel sampleDisplay;
	private static final Dimension sampleSize = new Dimension(600, 100);

	public ThemeEditor(Core core) {
		super("Theme editor");
		this.setCore(core);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		this.themeDatabase = core.getThemeDatabase();
		this.setContent();
		this.setWrapper();
		this.setCallbacks();
		this.setEditorElements(false);
	}

	public void setCore(Core core) {
		if (core == null)
			throw new IllegalArgumentException(
					"setCore : Null component not allowed.");
		;
		this.core = core;
	}

	private void setContent() {
		this.setLayout(new BorderLayout());
		this.sampleDisplay = new JPanel() {
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
						RenderingHints.VALUE_COLOR_RENDER_QUALITY);
				g2d.drawRenderedImage(editableTheme.getSample(
						ThemeEditor.sampleSize.width,
						ThemeEditor.sampleSize.height), AffineTransform
						.getScaleInstance(1, 1));
			}
		};
		this.sampleDisplay.setBackground(Color.darkGray);
		this.sampleDisplay.setPreferredSize(ThemeEditor.sampleSize);
		this.sampleDisplay.setMinimumSize(ThemeEditor.sampleSize);
		this.sampleDisplay.setMaximumSize(ThemeEditor.sampleSize);

		this.themesSelector = new JComboBox<String>(
				this.themeDatabase.getModifiableThemesNames());
		this.mouseStopSelector = new JComboBox<String>();
		int i = 0;
		while (i < typeMouseStop.values().length) {
			this.mouseStopSelector
					.addItem(typeMouseStop.values()[i].toString());
			i++;
		}
		this.editableTheme = new Theme(
				this.themeDatabase.getTheme((String) this.themesSelector
						.getSelectedItem()));

		this.colorSrcEditor = new ColorSrcEditor(this, this.editableTheme);
		this.mouseStopLabel = new JLabel("Mouse stop shape : ");
		this.ratioCircleLabel = new JLabel(
				"Ratio for the outter circle/square : ");
		this.ratioDiskLabel = new JLabel("Ratio for the inner circle/square : ");
		this.minMouseStopLabel = new JLabel(
				"Minimum time for displaying a mouse stop (in 0.01s) : ");
		this.ratioCircle = new JSpinner();
		this.ratioDisk = new JSpinner();
		this.minMouseStop = new JSpinner();
		this.useColors = new JCheckBox(
				"Use color generated from speed (Doesn't change anything yet, sorry)");
		this.mouseStops = new JCheckBox("Display mouse stops");
		this.okButton = new JButton("OK");
		this.saveButton = new JButton("Save");
		this.cancelButton = new JButton("Cancel");

	}

	private void setEditorElements(boolean reloadTheme) {
		if (reloadTheme)
			this.editableTheme = new Theme(
					this.themeDatabase.getTheme((String) this.themesSelector
							.getSelectedItem()));
		this.ratioCircle.setModel(new SpinnerNumberModel(this.editableTheme
				.getRatioCircle(), 0.0, 128.0, 0.1));
		this.ratioDisk.setModel(new SpinnerNumberModel(this.editableTheme
				.getRatioDisk(), 0.0, 128.0, 0.1));
		this.minMouseStop.setModel(new SpinnerNumberModel(this.editableTheme
				.getMinMouseStop(), 0, 1024, 1));

		this.themesSelector.setMaximumSize(this.themesSelector
				.getPreferredSize());
		this.mouseStopSelector.setMaximumSize(this.themesSelector
				.getPreferredSize());
		this.ratioCircle.setMaximumSize(this.ratioCircle.getPreferredSize());
		this.ratioDisk.setMaximumSize(this.ratioDisk.getPreferredSize());
		this.minMouseStop.setMaximumSize(this.minMouseStop.getPreferredSize());

		this.useColors.setSelected(this.editableTheme.getUseColors());
		this.mouseStops.setSelected(this.editableTheme.getMouseStops());

		this.colorSrcEditor.reset(this.editableTheme);
	}

	private void setWrapper() {

		Box themeSelectorWrapper = new Box(BoxLayout.X_AXIS);
		themeSelectorWrapper.add(Box.createHorizontalGlue());
		themeSelectorWrapper.add(this.themesSelector);
		themeSelectorWrapper.add(Box.createHorizontalGlue());

		Box sampleDisplayWrapper = new Box(BoxLayout.X_AXIS);
		sampleDisplayWrapper.add(Box.createHorizontalGlue());
		sampleDisplayWrapper.add(this.sampleDisplay);
		sampleDisplayWrapper.add(Box.createHorizontalGlue());

		Box colorsWrapper = new Box(BoxLayout.X_AXIS);
		colorsWrapper.add(Box.createHorizontalGlue());
		colorsWrapper.add(this.colorSrcEditor);
		colorsWrapper.add(Box.createHorizontalGlue());

		Box ratioCircleWrapper = new Box(BoxLayout.X_AXIS);
		ratioCircleWrapper.add(Box.createHorizontalGlue());
		ratioCircleWrapper.add(this.ratioCircleLabel);
		ratioCircleWrapper.add(this.ratioCircle);
		ratioCircleWrapper.add(Box.createHorizontalGlue());

		Box ratioDiskWrapper = new Box(BoxLayout.X_AXIS);
		ratioDiskWrapper.add(Box.createHorizontalGlue());
		ratioDiskWrapper.add(this.ratioDiskLabel);
		ratioDiskWrapper.add(this.ratioDisk);
		ratioDiskWrapper.add(Box.createHorizontalGlue());

		Box minMouseStopWrapper = new Box(BoxLayout.X_AXIS);
		minMouseStopWrapper.add(Box.createHorizontalGlue());
		minMouseStopWrapper.add(this.minMouseStopLabel);
		minMouseStopWrapper.add(this.minMouseStop);
		minMouseStopWrapper.add(Box.createHorizontalGlue());

		Box digitalValuesWrapper = new Box(BoxLayout.Y_AXIS);
		digitalValuesWrapper.add(Box.createVerticalGlue());
		digitalValuesWrapper.add(ratioCircleWrapper);
		digitalValuesWrapper.add(ratioDiskWrapper);
		digitalValuesWrapper.add(minMouseStopWrapper);
		digitalValuesWrapper.add(Box.createVerticalGlue());

		Box booleanValuesWrapper = new Box(BoxLayout.X_AXIS);
		booleanValuesWrapper.add(Box.createHorizontalGlue());
		booleanValuesWrapper.add(this.useColors);
		booleanValuesWrapper.add(this.mouseStops);
		booleanValuesWrapper.add(Box.createHorizontalGlue());

		Box buttonsReturnWrapper = new Box(BoxLayout.X_AXIS);
		buttonsReturnWrapper.add(Box.createHorizontalGlue());
		buttonsReturnWrapper.add(this.okButton);
		buttonsReturnWrapper.add(this.saveButton);
		buttonsReturnWrapper.add(this.cancelButton);
		buttonsReturnWrapper.add(Box.createHorizontalGlue());

		Box mouseStopWrapper = new Box(BoxLayout.X_AXIS);
		mouseStopWrapper.add(Box.createHorizontalGlue());
		mouseStopWrapper.add(this.mouseStopLabel);
		mouseStopWrapper.add(this.mouseStopSelector);
		mouseStopWrapper.add(Box.createHorizontalGlue());

		Box wrapper = new Box(BoxLayout.Y_AXIS);
		Box subWrapper[] = new Box[] { sampleDisplayWrapper,
				themeSelectorWrapper, colorsWrapper, mouseStopWrapper,
				digitalValuesWrapper, booleanValuesWrapper,
				buttonsReturnWrapper, };
		wrapper.add(Box.createVerticalGlue());
		int i = 0;
		while (i < subWrapper.length) {
			wrapper.add(subWrapper[i]);
			i++;
		}
		wrapper.add(Box.createVerticalGlue());
		this.add(wrapper, BorderLayout.CENTER);
	}

	private void setCallbacks() {
		this.themesSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editableTheme = themeDatabase.getTheme((String) themesSelector
						.getSelectedItem());
				setEditorElements(true);
				refreshSampleDisplay();
			}
		});
		this.mouseStopSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editableTheme.setTypeMS(typeMouseStop
						.toTypeMouseStop((String) mouseStopSelector
								.getSelectedItem()));
				refreshSampleDisplay();
			}
		});

		this.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save((String) themesSelector.getSelectedItem());
				setVisible(false);
			}
		});
		this.saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save((String) themesSelector.getSelectedItem());
				setVisible(true);
			}
		});
		this.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		this.mouseStops.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editableTheme.setMouseStops(mouseStops.isSelected());
				refreshSampleDisplay();
			}
		});
		this.useColors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editableTheme.setUseColors(useColors.isSelected());
				refreshSampleDisplay();
			}
		});
		this.minMouseStop.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				editableTheme.setMinMouseStop(((Integer) minMouseStop
						.getValue()).intValue());
				refreshSampleDisplay();
			}
		});
		this.ratioCircle.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				editableTheme.setRatioCircle(((Double) ratioCircle.getValue())
						.doubleValue());
				refreshSampleDisplay();
			}
		});
		this.ratioDisk.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				editableTheme.setRatioDisk(((Double) ratioDisk.getValue())
						.doubleValue());
				refreshSampleDisplay();
			}
		});
	}

	public void refreshSampleDisplay() {
		this.sampleDisplay.repaint();
	}

	public void display() {
		this.setEditorElements(true);
		this.setVisible(true);
		this.pack();
		this.setMinimumSize(this.getPreferredSize());
		this.setLocationRelativeTo(null);
	}

	private void save(String name) {
		this.themeDatabase.setTheme(name, this.editableTheme);
		this.core.reloadTheme();

	}
}
