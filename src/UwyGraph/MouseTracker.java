package UwyGraph;

import java.awt.Point;
import java.awt.MouseInfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

abstract public class MouseTracker {
	private static final int DELAY = 10;
	private Timer timer;

	public MouseTracker() {
		this.timer = new Timer(DELAY, new ActionListener() {
			private Point oldPoint = null;
			private Point point = null;

			public synchronized void actionPerformed(ActionEvent e) {
				if(point == null)
					point = new Point();
				point.setLocation(MouseInfo.getPointerInfo().getLocation());
				if (oldPoint != null) {
					if (!point.equals(oldPoint))
						onMouseMotionEvent(oldPoint, point);
					else
						onMouseStillEvent();
				}
				else
					oldPoint = new Point();
				oldPoint.setLocation(point);
			}
		});

	}

	abstract protected void onMouseStillEvent();

	abstract protected void onMouseMotionEvent(Point oldPoint, Point point);

	public void start() {
		this.timer.start();
	}

	public void stop() {
		this.timer.stop();
	}
}
