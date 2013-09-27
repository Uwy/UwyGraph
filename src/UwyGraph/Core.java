package UwyGraph;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;

public class Core {
	private MouseTracker tracker;
	private GUI gui;
	private Theme theme;
	private int stillCounter;
	private BufferedImage output;
	private ThemeDatabase themeDB;

	public Core(GUI gui) {
		if (gui == null)
			throw new IllegalArgumentException(
					"Core : Null component not allowed.");
		this.gui = gui;
		this.themeDB = new ThemeDatabase();
		this.theme = this.themeDB.getTheme("Current");
		this.stillCounter = 0;
		this.tracker = new MouseTracker() {
			protected void onMouseStillEvent() {
				pixelInvokedStill();
			}

			protected void onMouseMotionEvent(Point oldPoint, Point point) {
				pixelInvoked(oldPoint, point);
			}
		};
		this.gui.setCore(this);
		this.resetTheme();
	}
	
	public ThemeDatabase getThemeDatabase() {
		return this.themeDB;
	}

	public synchronized void pixelInvokedStill() {
		this.stillCounter++;
	}

	public synchronized void pixelInvoked(Point oldPoint,Point point) {
		this.theme.render(oldPoint, point, this.stillCounter);
		this.gui.refresh();
		this.stillCounter = 0;
	}

	public synchronized void resetTheme() {
		this.theme.reset();
		this.output = this.theme.getOutput();
		this.stillCounter = 0;
		this.gui.setRenderedImage(this.theme.getOutput());
	}

	public void setUseColors(boolean useColors) {
		this.theme.setUseColors(useColors);
	}

	public void setMouseStops(boolean mouseStops) {
		this.theme.setMouseStops(mouseStops);
	}

	public synchronized void startTracking() {
		this.stillCounter = 0;
		this.tracker.start();
	}

	public void stopTracking() {
		this.tracker.stop();
	}

	public void save(File filePath, String ext) {
		this.theme.save(filePath, ext);
	}

	public synchronized void reloadTheme() {
		this.theme = this.themeDB.getTheme("Current");
		this.theme.setOutput(this.output);
	}
}
