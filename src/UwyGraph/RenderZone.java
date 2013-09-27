package UwyGraph;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Dimension;

public class RenderZone extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage image;

	public RenderZone() {
		super(true);
		this.image = null;
		this.setBackground(Color.black);
	}

	public void setRenderedImage(BufferedImage image) {
		if (image == null)
			throw new IllegalArgumentException(
					"RenderZone : Null component not allowed.");
		this.image = image;
		this.setMinimumSize(new Dimension(this.image.getWidth() / 2, this.image
				.getHeight() / 2));
		this.setPreferredSize(new Dimension(this.image.getWidth() / 2,
				this.image.getHeight() / 2));
		this.setMaximumSize(new Dimension(this.image.getWidth() / 2, this.image
				.getHeight() / 2));
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.image != null) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
					RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.drawRenderedImage(this.image,
					AffineTransform.getScaleInstance(0.5, 0.5));
		}
	}
}
