package UwyGraph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Theme {
	private Color colorSrc[];
	private Color colorTab[];
	private BufferedImage output;
	private Graphics2D outputEditor;
	private Dimension screenResolution;
	private boolean useColors;
	private boolean mouseStops;
	private boolean isModifiableByUser;
	private double ratioCircle;
	private double ratioDisk;
	private int lastNorm;
	private int minMouseStop;
	private int colorSteps;
	private typeMouseStop typeMS;

	public Theme(Color colorSrc[], int colorSteps, boolean useColors,
			boolean mouseStops, BufferedImage background, double ratioCircle,
			double ratioDisk, int minMouseStop, boolean isModifiableByUser,
			typeMouseStop typeMS) {
		this.colorSrc = colorSrc;
		this.colorSteps = colorSteps;
		this.colorTab = Gradient.createMultiGradient(this.colorSrc, colorSteps);
		this.useColors = useColors;
		this.mouseStops = mouseStops;
		this.ratioCircle = ratioCircle;
		this.ratioDisk = ratioDisk;
		this.lastNorm = this.colorTab.length / 2;

		this.minMouseStop = minMouseStop;
		this.screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
		this.isModifiableByUser = isModifiableByUser;
		this.output = background;
		this.setTypeMS(typeMS);
	}

	public Theme(Theme themeSrc) {
		if (themeSrc == null)
			throw new IllegalArgumentException(
					"Theme : Null component not allowed.");
		this.colorSrc = themeSrc.colorSrc;
		this.colorSteps = themeSrc.colorSteps;
		this.colorTab = Gradient.createMultiGradient(this.colorSrc,
				this.colorSteps);
		this.useColors = themeSrc.useColors;
		this.mouseStops = themeSrc.mouseStops;
		this.ratioCircle = themeSrc.ratioCircle;
		this.ratioDisk = themeSrc.ratioDisk;
		this.lastNorm = this.colorTab.length / 2;
		this.minMouseStop = themeSrc.minMouseStop;
		this.screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
		this.output = null;
		this.isModifiableByUser = themeSrc.isModifiableByUser;
		this.typeMS = themeSrc.typeMS;
	}

	public static Theme getDefaultTheme() {
		return new Theme(new Color[] { new Color(181, 32, 255), Color.blue,
				Color.green, Color.yellow, Color.orange, Color.red }, 64, true,
				true, null, 2, 1, 30, false, typeMouseStop.circles);
	}

	public static Theme getMikuTheme() {
		return new Theme(new Color[] { new Color(19, 160, 163),
				new Color(47, 52, 53), new Color(228, 0, 126) }, 64, true,
				true, null, 2, 1, 30, false, typeMouseStop.squares);
	}

	public static Theme getBlueToRedTheme() {
		return new Theme(new Color[] { Color.blue, Color.green }, 64, true,
				true, null, 2, 1, 30, false, typeMouseStop.circles);
	}

	public static Theme getRedToOrangeTheme() {
		return new Theme(new Color[] { Color.red, Color.orange }, 64, true,
				true, null, 2, 1, 30, false, typeMouseStop.circles);
	}

	private void drawMouseStop(Point oldPoint, int stillCounter) {
		Color temp = this.outputEditor.getColor();
		int formSize = (int) (Math.pow(Math.log(stillCounter), this.ratioCircle));
		this.outputEditor.setColor(new Color(0, 0, 0, stillCounter % 256));
		switch (this.getTypeMS()) {
		case circles:
			this.outputEditor.fillOval(oldPoint.x - (formSize / 2), oldPoint.y - (formSize / 2), formSize, formSize);
			this.outputEditor.setColor(temp);
			formSize = (int) (Math.pow(Math.log(stillCounter), this.ratioDisk));
			this.outputEditor.fillOval(oldPoint.x - (formSize / 2), oldPoint.y - (formSize / 2), formSize, formSize);
			break;
		case squares:
			this.outputEditor.fillRect(oldPoint.x - (formSize / 2), oldPoint.y - (formSize / 2), formSize, formSize);
			this.outputEditor.setColor(temp);
			formSize = (int) (Math.pow(Math.log(stillCounter), this.ratioDisk));
			this.outputEditor.fillRect(oldPoint.x - (formSize / 2), oldPoint.y - (formSize / 2), formSize, formSize);
			break;
		default:
			System.out.println("typeMouseStop : Shape not defined !");
			break;
		}
	}

	private void setColorFromSpeed(Point oldPoint, Point point) {
		int newNorm = (int) Math.sqrt(Math.pow(Math.abs(point.x - oldPoint.x), 2) + Math.pow(Math.abs(point.y - oldPoint.y), 2));
		if(this.lastNorm < newNorm)
			this.lastNorm++;
		else
			this.lastNorm--;
		this.outputEditor.setColor(this.colorTab[Math.min(this.lastNorm, this.colorTab.length - 1)]);
	}
	
	public synchronized void render(Point oldPoint, Point point,
			int stillCounter) {
		if (this.mouseStops && (stillCounter > this.minMouseStop)) {
			this.drawMouseStop(oldPoint, stillCounter);
		}
		// if(this.useColors) {
		this.setColorFromSpeed(oldPoint, point);
		// }
		this.outputEditor.drawLine(oldPoint.x, oldPoint.y, point.x, point.y);
	}

	public synchronized BufferedImage getOutput() {
		if (this.output == null) {
			this.output = new BufferedImage(this.screenResolution.width,
					this.screenResolution.height, BufferedImage.TYPE_INT_ARGB);
			this.outputEditor = this.output.createGraphics();
			this.outputEditor.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			this.outputEditor.setRenderingHint(
					RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			this.outputEditor.setRenderingHint(
					RenderingHints.KEY_COLOR_RENDERING,
					RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		}
		return this.output;
	}

	public synchronized void setOutput(BufferedImage output) {
		if (output == null)
			throw new IllegalArgumentException(
					"Theme : Null component not allowed.");
		this.output = output;
		this.outputEditor = this.output.createGraphics();
		this.outputEditor.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		this.outputEditor.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		this.outputEditor.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	}

	public synchronized void save(File filePath, String ext) {
		if (!filePath.toString().matches(".*" + ext)) {
			filePath = new File(filePath.toString() + ext);
		}
		try {
			ImageIO.write(this.output, ext.replace(".", ""), filePath);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public boolean getIsModifiableByUser() {
		return this.isModifiableByUser;
	}

	public void setIsModifiableByUser(boolean val) {
		this.isModifiableByUser = val;
	}

	public synchronized void reset() {
		this.output = new BufferedImage(this.screenResolution.width,
				this.screenResolution.height, BufferedImage.TYPE_INT_ARGB);
		this.outputEditor = this.output.createGraphics();
	}

	public boolean getUseColors() {
		return this.useColors;
	}

	public synchronized void setUseColors(boolean useColors) {
		this.useColors = useColors;
	}

	public boolean getMouseStops() {
		return this.mouseStops;
	}

	public synchronized void setMouseStops(boolean mouseStops) {
		this.mouseStops = mouseStops;
	}

	public double getRatioCircle() {
		return this.ratioCircle;
	}

	public synchronized void setRatioCircle(double ratioCircle) {
		this.ratioCircle = ratioCircle;
	}

	public double getRatioDisk() {
		return this.ratioDisk;
	}

	public synchronized void setRatioDisk(double ratioDisk) {
		this.ratioDisk = ratioDisk;
	}

	public int getMinMouseStop() {
		return this.minMouseStop;
	}

	public synchronized void setMinMouseStop(int minMouseStop) {
		this.minMouseStop = minMouseStop;
	}

	public int getColorSteps() {
		return this.colorSteps;
	}

	public Color[] getColorSrc() {
		return this.colorSrc;
	}

	public synchronized void setColorTab(Color colorSrc[], int colorSteps) {
		this.colorSrc = colorSrc;
		this.colorSteps = colorSteps;
		this.colorTab = Gradient.createMultiGradient(this.colorSrc,
				this.colorSteps);
	}

	public synchronized BufferedImage getSample(int width, int height) {
		BufferedImage sample = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D sampleEditor = sample.createGraphics();
		sampleEditor.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		sampleEditor.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		sampleEditor.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);

		Point oldPoint = new Point(width / 10, height / 2);
		Point point = new Point(width / 10, height / 2);
		int i = 0;
		int formSize = (int) (int) (Math.pow(Math.log(Math.max(width, height)), this.ratioCircle));
		sampleEditor.setColor(new Color(0, 0, 0, 128));
		if (this.mouseStops) {
			switch (this.typeMS) {
			case circles:
				sampleEditor.fillOval(
						((int) (point.x - ((double) formSize / 2))),
						((int) (point.y - ((double) formSize / 2))), formSize,
						formSize);
				sampleEditor.setColor(this.colorTab[0]);
				formSize = (int) (Math.pow(Math.log(Math.max(width, height)), this.ratioDisk));
				sampleEditor.fillOval(
						((int) (point.x - ((double) formSize / 2))),
						((int) (point.y - ((double) formSize / 2))), formSize,
						formSize);
				break;
			case squares:
				sampleEditor.fillRect(point.x - (formSize / 2), point.y
						- (formSize / 2), formSize, formSize);
				sampleEditor.setColor(this.colorTab[0]);
				formSize = (int) (Math.pow(Math.log(Math.max(width, height)), this.ratioDisk));
				sampleEditor.fillRect(point.x - (formSize / 2), point.y
						- (formSize / 2), formSize, formSize);
				break;
			default:
				System.out.println("typeMouseStop : Shape not defined !");
				break;
			}
		}
		double quantum = (width - (width / 5))
				/ ((double) this.colorTab.length - 1);
		while (i < this.colorTab.length) {
			point.x = (int) ((width / 10) + (quantum * i));
			sampleEditor.setColor(this.colorTab[i]);
			sampleEditor.drawLine(oldPoint.x, oldPoint.y, point.x, point.y);
			oldPoint.x = point.x;
			i++;
		}
		sampleEditor.setColor(new Color(0, 0, 0, 128));
		formSize = (int) (Math.pow(Math.log(Math.max(width, height)), this.ratioCircle));
		if (this.mouseStops) {
			switch (this.typeMS) {
			case circles:
				sampleEditor.fillOval(
						((int) (point.x - ((double) formSize / 2))),
						((int) (point.y - ((double) formSize / 2))), formSize,
						formSize);
				sampleEditor.setColor(this.colorTab[i - 1]);
				formSize = (int) (Math.pow(Math.log(Math.max(width, height)), this.ratioDisk));
				sampleEditor.fillOval(
						((int) (point.x - ((double) formSize / 2))),
						((int) (point.y - ((double) formSize / 2))), formSize,
						formSize);
				break;
			case squares:
				sampleEditor.fillRect(
						((int) (point.x - ((double) formSize / 2))),
						((int) (point.y - ((double) formSize / 2))), formSize,
						formSize);
				sampleEditor.setColor(this.colorTab[i - 1]);
				formSize = (int) (Math.pow(Math.log(Math.max(width, height)), this.ratioDisk));
				sampleEditor.fillRect(
						((int) (point.x - ((double) formSize / 2))),
						((int) (point.y - ((double) formSize / 2))), formSize,
						formSize);
				break;
			default:
				System.out.println("typeMouseStop : Shape not defined !");
				break;
			}
		}
		return sample;
	}

	public synchronized typeMouseStop getTypeMS() {
		return typeMS;
	}

	public synchronized void setTypeMS(typeMouseStop typeMS) {
		if (typeMS == null)
			throw new IllegalArgumentException(
					"setTypeMS : Null component not allowed.");

		this.typeMS = typeMS;
	}
}
