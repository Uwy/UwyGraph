package UwyGraph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class ColorWidget extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int sizeDisplay = 50;
	private Color colorChoosen;
	private JPanel colorDisplay;
	private JButton edit;
	private JButton delete;
	private JButton up;
	private JButton down;
	protected int index;

	public ColorWidget(Color color, int position) {
		if (color == null)
			throw new IllegalArgumentException(
					"ColorWidget : Null component not allowed.");
		this.colorChoosen = color;
		this.index = position;
		this.setLayout(new BorderLayout());
		this.up = new JButton("Up");
		this.down = new JButton("Down");
		this.edit = new JButton("Edit");
		this.delete = new JButton("X");
		this.up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onUp(index);
			}
		});
		this.down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onDown(index);
			}
		});
		this.edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onEdit(index);
			}
		});
		this.delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onDelete(index);
			}
		});

		this.colorDisplay = new JPanel() {
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(colorChoosen);
				g.fillRect(0, 0, ColorWidget.sizeDisplay,
						ColorWidget.sizeDisplay);
			}
		};
		this.colorDisplay.setPreferredSize(new Dimension(
				ColorWidget.sizeDisplay, ColorWidget.sizeDisplay));
		this.colorDisplay.setMinimumSize(this.colorDisplay.getPreferredSize());
		this.colorDisplay.setMaximumSize(this.colorDisplay.getPreferredSize());
		this.colorDisplay.setBackground(Color.black);
		Box wrapper = new Box(BoxLayout.X_AXIS);
		Box upDownWrapper = new Box(BoxLayout.Y_AXIS);
		upDownWrapper.add(Box.createVerticalGlue());
		upDownWrapper.add(this.up);
		upDownWrapper.add(this.down);
		upDownWrapper.add(Box.createVerticalGlue());
		wrapper.add(Box.createHorizontalGlue());
		wrapper.add(this.colorDisplay);
		wrapper.add(upDownWrapper);
		wrapper.add(this.edit);
		wrapper.add(this.delete);
		wrapper.add(Box.createHorizontalGlue());
		this.add(wrapper, BorderLayout.CENTER);
		this.setMaximumSize(this.getPreferredSize());
	}

	public void setColor(Color color) {
		if (color != null)
			this.colorChoosen = color;
	}

	public Color getColor() {
		return this.colorChoosen;
	}

	public void refresh() {
		this.colorDisplay.repaint();
	}

	public void setIndex(int i, int total) {
		this.up.setEnabled(i != 0);
		this.down.setEnabled(i != total);
		this.delete.setEnabled(total >= 2);
		this.index = i;
	}

	protected abstract void onDelete(int index);

	protected abstract void onUp(int index);

	protected abstract void onEdit(int index);

	protected abstract void onDown(int index);
}
