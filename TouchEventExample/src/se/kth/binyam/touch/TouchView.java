package se.kth.binyam.touch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchView extends View {
	
	private Drawable cross;
	private Rect crossBounds = null;

	public TouchView(Context context) {
		super(context);
		
		// Get a representation of the image
		Resources resources = context.getResources();
		cross = (Drawable) resources.getDrawable(R.drawable.cross);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i("TouchView.onTouchEvent", "event = " + event);
		
		if(event.getAction() == MotionEvent.ACTION_DOWN || 
				event.getAction() == MotionEvent.ACTION_UP) {
			
			int w = cross.getIntrinsicWidth();
			int h = cross.getIntrinsicHeight();
			int x = (int) event.getX();
			int y = (int) event.getY();
			crossBounds = new Rect(x-w/2, y-w/2, x+w/2, y+h/2);
			
			// Request the system to redraw the view (call onDraw at 
			// some point in the future)
			// From a non-UI thread, call postInvalidate instead
			invalidate();
			
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		Log.i("TouchView.onDraw", "");
		
		// Background
		Paint bgPaint = new Paint();
		bgPaint.setColor(Color.WHITE);
		canvas.drawPaint(bgPaint);
		
		if(crossBounds != null) {
			cross.setBounds(crossBounds);
			cross.draw(canvas);
		}
	}
}
