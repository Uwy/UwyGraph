package UwyGraph;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

public class GUI {
	private Core core;

	private JMenuItem saveButton;
	private JMenuItem resetButton;
	private JMenuItem exitButton;
	private JMenuItem startStopButton;
	private JMenuItem themeChooser;
	private JMenuItem themeEditor;

	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenu menuSettings;
	private JMenu menuAbout;
	private JMenu menuTracking;

	private JFrame frame;
	private RenderZone renderZone;
	private Box wrapper;

	private boolean inTracking;

	public GUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println(e);
		}
		this.inTracking = false;
		this.setFrame();
		this.setWrapper();
		this.setMenus();
		this.setMenusCallbacks();
	}

	private void setFrame() {
		this.frame = new JFrame("UwyGraph");
		this.frame.setLayout(new BorderLayout());
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void setWrapper() {
		this.renderZone = new RenderZone();
		this.wrapper = new Box(BoxLayout.Y_AXIS);
		this.wrapper.add(Box.createVerticalGlue());
		this.wrapper.add(this.renderZone);
		this.wrapper.add(Box.createVerticalGlue());
		this.frame.add(this.wrapper, BorderLayout.CENTER);
	}

	private void setMenus() {
		this.menuBar = new JMenuBar();
		this.menuFile = new JMenu("File");
		this.menuSettings = new JMenu("Settings");
		this.menuTracking = new JMenu("Tracking");
		this.menuAbout = new JMenu("About");
		this.saveButton = new JMenuItem("Save");
		this.resetButton = new JMenuItem("Reset");
		this.exitButton = new JMenuItem("Exit");
		this.startStopButton = new JMenuItem("Start");
		this.themeChooser = new JMenuItem("Theme Chooser");
		this.themeEditor = new JMenuItem("Theme Editor");

		this.menuFile.add(this.saveButton);
		this.menuFile.addSeparator();
		this.menuFile.add(this.exitButton);

		this.menuTracking.add(this.startStopButton);
		this.menuTracking.add(this.resetButton);

		this.menuSettings.add(this.themeChooser);
		this.menuSettings.add(this.themeEditor);

		JMenu menus[] = new JMenu[] { this.menuFile, this.menuTracking,
				this.menuSettings, this.menuAbout };
		int i = 0;
		while (i < menus.length) {
			this.menuBar.add(menus[i]);
			i++;
		}
		this.frame.setJMenuBar(this.menuBar);
	}

	private void setMenusCallbacks() {
		this.saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(new File(GUI.class
						.getProtectionDomain().getCodeSource().getLocation()
						.getPath()));
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileFilter(new FileNameExtensionFilter(
						"PNG Image File", ".png"));
				int retVal = chooser.showSaveDialog(frame);
				if (retVal == JFileChooser.APPROVE_OPTION)
					core.save(chooser.getSelectedFile(),
							((FileNameExtensionFilter) chooser.getFileFilter())
									.getExtensions()[0]);
			}
		});
		this.resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (core != null) {
					core.resetTheme();
				}
			}
		});
		this.exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.startStopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (inTracking) {
					core.stopTracking();
					inTracking = false;
					startStopButton.setText("Start");
				} else {
					core.startTracking();
					inTracking = true;
					startStopButton.setText("Stop");
				}

			}
		});
		this.themeChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ThemeChooser themeChooserWindow = new ThemeChooser(core);
				themeChooserWindow.display();
			}
		});
		this.themeEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ThemeEditor themeEditorWindow= new ThemeEditor(core);
				themeEditorWindow.display();
			}
		});
	}

	public void setRenderedImage(BufferedImage image) {
		this.renderZone.setRenderedImage(image);
		this.frame.setVisible(true);
		this.refresh();
		this.frame.pack();
		this.frame.setMinimumSize(this.frame.getPreferredSize());
		this.frame.setLocationRelativeTo(null);
	}

	public void refresh() {
		this.renderZone.repaint();
	}

	public void setCore(Core core) {
		if (core == null)
			throw new IllegalArgumentException(
					"setCore : Null component not allowed.");
		;
		this.core = core;
	}
}
