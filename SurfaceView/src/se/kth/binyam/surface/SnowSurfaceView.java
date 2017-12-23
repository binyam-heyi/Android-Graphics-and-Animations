package se.kth.binyam.surface;

import java.util.LinkedList;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SnowSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder holder;
	private boolean hasSurface;

	public final int X_RESOLUTION, Y_RESOLUTION;

	private final Drawable slSprite, snowSprite; // representations of the actual images
	private MovableIcon slMovable; // SL = the commuter traffic company
	private LinkedList<MovableIcon> snowList, removeList;
	
	// Flags indicating motion of slMovable 
	private boolean movingRight, movingLeft;
	private float slVelocity, flakeVelocity;
	
	private GraphicsThread graphicsThread;
	private Handler handler = new Handler();
	
	private Random rand = new Random();

	public SnowSurfaceView(Context context, int xRes, int yRes) {
		super(context);
		
		// SurfaceView specific initialization
		holder = getHolder();
		holder.addCallback(this);
		hasSurface = false;

		X_RESOLUTION = xRes;
		Y_RESOLUTION = yRes;

		// Create movable icons using the specified images
		slSprite = context.getResources().getDrawable(R.drawable.sl_logo);
		snowSprite = context.getResources().getDrawable(R.drawable.snowflake_with_shadow);
		slMovable = new MovableIcon(10, 10, slSprite);
		snowList = new LinkedList<MovableIcon>();
		removeList = new LinkedList<MovableIcon>();
		
		slVelocity = 4.0F;
		flakeVelocity = 4.0F;
		
		initGame();
	}
	
	private void initGame() {
		snowList.clear();
		slMovable.setPosition(X_RESOLUTION/2, Y_RESOLUTION-150);
		movingRight = movingLeft = false;

		// NB!! Need this for capturing key events
		setFocusable(true);
		requestFocus();
	}

	public void resume() {
		if (graphicsThread == null) {
			Log.i("BounceSurfaceView", "resume");
			graphicsThread = new GraphicsThread(this, 20); // 20 ms between updates
			if (hasSurface) {
				graphicsThread.start();
			}
		}
	}

	public void pause() {
		if (graphicsThread != null) {
			Log.i("BounceSurfaceView", "pause");
			graphicsThread.requestExitAndWait();
			graphicsThread = null;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i("KeyHandler", "movingRight = " + movingRight);
		
		if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			movingRight = true;
			Log.i("KeyHandler", "movingRight = " + movingRight);
			return true;
		}
		
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			movingLeft = true;
			Log.i("KeyHandler", "movingLeft = " + movingLeft);
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.i("KeyHandler", "keyDownCalled");
		
		if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			movingRight = false;
			Log.i("KeyHandler", "movingRight = " + movingRight);
			return true;
		}
		
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			movingLeft = false;
			Log.i("KeyHandler", "movingLeft = " + movingLeft);
			return true;
		}
		
		return false;		
	}
	
	protected void move() {
		if(movingLeft) {
			slMovable.setVelocity(-slVelocity, 0.0F);
		}
		else if(movingRight) {
			slMovable.setVelocity(slVelocity, 0.0F);
		}
		else {
			slMovable.setVelocity(0.0F, 0.0F);
		}
		slMovable.move();
		
		for(MovableIcon m: snowList) {
			m.move();
			// Outside view?
			if(m.getPosY() > Y_RESOLUTION) {
				removeList.add(m);
			}
		}
		snowList.removeAll(removeList);
		removeList.clear();
	}

	protected void draw() {
		// TO DO: Draw on an off screen Bitmap before
		// calling holder.lockCanvas()
		
		Canvas canvas = holder.lockCanvas();
		{	
			// Paint the background
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			canvas.drawPaint(paint);

			// Draw the movables
			slMovable.draw(canvas);
			for(MovableIcon m: snowList) {
				m.draw(canvas);
			}
			
			if(graphicsThread.isDone()) {
				paint.setColor(Color.RED);
				paint.setTextAlign(Align.CENTER);
				paint.setTextSize(20);
				paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
				canvas.drawText("All traffic canceled", (float)X_RESOLUTION/2, (float)Y_RESOLUTION/2, paint);
			}
		}
		holder.unlockCanvasAndPost(canvas);
	}
	
	protected void generateSnow() {
		int spawn = rand.nextInt(12);
		if(spawn==0) {
			MovableIcon snowflake = new MovableIcon(
					getWidth()*rand.nextInt(10)/(float)10, 0.0F, this.snowSprite);
			snowflake.setVelocity(0.0F, flakeVelocity);
			snowList.add(snowflake);
		}
	}
	
	protected void checkForHit() {
		Rect slBounds = slMovable.getIconBounds();
		for(MovableIcon m: snowList) {
			if(slBounds.intersect(m.getIconBounds())) {
				graphicsThread.setDone();
				handler.post(new Runnable() {
					public void run() {
						
						//setBackgroundColor(Color.BLUE);
					}
				});
				return;
			}
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		hasSurface = true;
		if (graphicsThread != null) {
			graphicsThread.start();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
		pause();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (graphicsThread != null) {
			graphicsThread.onWindowResize(w, h);
		}
	}
}
