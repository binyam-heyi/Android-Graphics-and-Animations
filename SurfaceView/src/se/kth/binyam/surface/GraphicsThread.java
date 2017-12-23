package se.kth.binyam.surface;

class GraphicsThread extends Thread {

	private SnowSurfaceView view;
	private long sleepTime;
	private boolean done;

	GraphicsThread(SnowSurfaceView view, long sleepTime) {
		this.view = view;
		this.sleepTime = sleepTime;
		this.done = false;
	}
	
	protected void setDone() {
		done = true;
	}
	
	protected boolean isDone() {
		return done;
	}

	public void run() {
		
		while (done == false) {
			
			view.generateSnow();
			view.move();
			view.checkForHit();
			view.draw();

			// Wait for some time
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException ie) {
			}
		}
	}

	void requestExitAndWait() {
		done = true;
		try {
			this.join();
		} catch (InterruptedException ie) {
		}
	}

	void onWindowResize(int w, int h) {
		// Deal with change in surface size
	}
}