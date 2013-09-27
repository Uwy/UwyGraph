package UwyGraph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorSrcEditor extends JPanel {
	private static final long serialVersionUID = 1L;
	JSpinner colorSteps;
	JLabel colorStepsLabel;
	JButton addButton;
	Theme editableTheme;
	List<ColorWidget> colorWidgets;
	ThemeEditor editor;

	public ColorSrcEditor(ThemeEditor edit, Theme editTheme) {
		if ((editTheme == null) || (edit == null))
			throw new IllegalArgumentException(
					"ColorSrcEditor : Null component not allowed.");
		this.editor = edit;
		this.setLayout(new BorderLayout());
		this.colorWidgets = new ArrayList<ColorWidget>();
		this.addButton = new JButton("Add");
		this.colorStepsLabel = new JLabel(
				"Numbers of colors in the gradient : ");
		this.addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addColorWidget();
			}
		});
		this.colorSteps = new JSpinner();
		this.colorSteps.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				editableTheme.setColorTab(getColorSrc(), getColorSteps());
				editor.refreshSampleDisplay();
			}
		});
		this.reset(editTheme);
		this.renderColorWidgets();
	}

	private void renderColorWidgets() {
		this.resetColorWidgetIndex();

		Box wrapper = new Box(BoxLayout.Y_AXIS);
		wrapper.add(Box.createVerticalGlue());
		int i = 0;
		while (i < colorWidgets.size()) {
			wrapper.add(colorWidgets.get(i));
			i++;
		}
		wrapper.add(this.addButton);
		Box colorStepWrapper = new Box(BoxLayout.X_AXIS);
		colorStepWrapper.add(Box.createHorizontalGlue());
		colorStepWrapper.add(this.colorStepsLabel);
		colorStepWrapper.add(this.colorSteps);
		colorStepWrapper.add(Box.createHorizontalGlue());
		wrapper.add(colorStepWrapper);
		wrapper.add(Box.createVerticalGlue());
		if(((BorderLayout) this.getLayout()).getLayoutComponent(BorderLayout.CENTER) != null) {
			this.remove(((BorderLayout) this.getLayout()).getLayoutComponent(BorderLayout.CENTER));
		}
		this.add(wrapper);
		this.revalidate();
		this.repaint();
		this.setMaximumSize(this.getPreferredSize());
		this.editor.revalidate();
		this.editor.setMinimumSize(this.editor.getPreferredSize());
		this.editor.pack();
		this.editableTheme
				.setColorTab(this.getColorSrc(), this.getColorSteps());
		this.editor.refreshSampleDisplay();
	}

	private void setColorWidgets() {
		Color colorSrc[] = this.editableTheme.getColorSrc();
		this.colorWidgets.clear();
		int i = 0;
		while (i < colorSrc.length) {
			this.colorWidgets.add(i, new ColorWidget(colorSrc[i], i) {
				private static final long serialVersionUID = 1L;

				protected void onUp(int index) {
					if (index - 1 >= 0) {
						Collections.swap(colorWidgets, index, index - 1);
						renderColorWidgets();
					}
				}

				protected void onDown(int index) {
					if (index + 1 < colorWidgets.size()) {
						Collections.swap(colorWidgets, index, index + 1);
						renderColorWidgets();
					}
				}

				protected void onEdit(int index) {
					colorWidgets.get(index).setColor(
							JColorChooser.showDialog(this, "Color Picker",
									colorWidgets.get(index).getColor()));
					editableTheme.setColorTab(getColorSrc(), getColorSteps());
					editor.refreshSampleDisplay();
					this.refresh();
				}

				protected void onDelete(int index) {
					colorWidgets.remove(index);
					renderColorWidgets();

				}
			});
			i++;
		}
	}

	private void addColorWidget() {
		this.colorWidgets.add(this.colorWidgets.size(), new ColorWidget(
				new Color(255, 255, 255, 255), this.colorWidgets.size()) {
			private static final long serialVersionUID = 1L;

			protected void onUp(int index) {
				if (index - 1 >= 0) {
					Collections.swap(colorWidgets, index, index - 1);
					renderColorWidgets();
				}
			}

			protected void onDown(int index) {
				if (index + 1 < colorWidgets.size()) {
					Collections.swap(colorWidgets, index, index + 1);
					renderColorWidgets();
				}
			}

			protected void onEdit(int index) {
				colorWidgets.get(index).setColor(
						JColorChooser.showDialog(this, "Color Picker",
								colorWidgets.get(index).getColor()));
				this.refresh();
			}

			protected void onDelete(int index) {
				colorWidgets.remove(index);
				resetColorWidgetIndex();
				renderColorWidgets();
			}
		});
		this.renderColorWidgets();

	}

	private void resetColorWidgetIndex() {
		int i = 0;
		while (i < this.colorWidgets.size()) {
			this.colorWidgets.get(i).setIndex(i, this.colorWidgets.size() - 1);
			i++;
		}
	}

	public void reset(Theme editableTheme) {
		this.editableTheme = editableTheme;
		this.setColorWidgets();
		this.colorSteps.setModel(new SpinnerNumberModel(this.editableTheme
				.getColorSteps(), this.editableTheme.getColorSrc().length, 128, 1));
		this.renderColorWidgets();
	}

	public Color[] getColorSrc() {
		List<Color> colorSrc = new ArrayList<Color>();
		int i = 0;
		while (i < this.colorWidgets.size()) {
			colorSrc.add(i, this.colorWidgets.get(i).getColor());
			i++;
		}
		return colorSrc.toArray(new Color[0]);
	}

	public int getColorSteps() {
		return ((Integer) this.colorSteps.getValue()).intValue();
	}
}
