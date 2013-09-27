package UwyGraph;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThemeChooser extends JFrame {
	private static final long serialVersionUID = 1L;
	private JLabel label;
	private Core core;
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox<String> themesSelector;
	private ThemeDatabase themeDatabase;

	public ThemeChooser(Core core) {
		super("Theme chooser");
		this.setCore(core);
		this.themeDatabase = core.getThemeDatabase();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		this.setContent();
		this.setWrapper();
		this.setCallbacks();
	}

	public void setCore(Core core) {
		if (core == null)
			throw new IllegalArgumentException(
					"Core : Null component not allowed.");
		this.core = core;
	}

	private void setContent() {
		this.setLayout(new BorderLayout());
		this.label = new JLabel(
				"Select the theme you want to set as your current theme : ");
		this.label.setMaximumSize(this.label.getPreferredSize());
		this.themesSelector = new JComboBox<String>(
				this.themeDatabase.getThemesNames());
		this.themesSelector.setMaximumSize(this.themesSelector
				.getPreferredSize());

		this.okButton = new JButton("OK");
		this.cancelButton = new JButton("Cancel");
	}

	private void setWrapper() {
		Box wrapper = new Box(BoxLayout.Y_AXIS);
		Box themeSelectorWrapper = new Box(BoxLayout.X_AXIS);
		themeSelectorWrapper.add(Box.createHorizontalGlue());
		themeSelectorWrapper.add(this.label);
		themeSelectorWrapper.add(this.themesSelector);
		themeSelectorWrapper.add(Box.createHorizontalGlue());

		Box buttonsReturnWrapper = new Box(BoxLayout.X_AXIS);
		buttonsReturnWrapper.add(Box.createHorizontalGlue());
		buttonsReturnWrapper.add(this.okButton);
		buttonsReturnWrapper.add(this.cancelButton);
		buttonsReturnWrapper.add(Box.createHorizontalGlue());

		Box subWrapper[] = new Box[] { themeSelectorWrapper,
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
		this.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCurrentTheme((String) themesSelector.getSelectedItem());
				setVisible(false);
			}
		});
		this.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

	}

	public void display() {
		this.setVisible(true);
		this.pack();
		this.setMinimumSize(this.getPreferredSize());
		this.setLocationRelativeTo(null);
	}

	private void setCurrentTheme(String themeName) {
		this.themeDatabase.setAsCurrent(themeName);
		this.core.reloadTheme();
	}

}
