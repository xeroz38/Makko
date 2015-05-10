package com.project.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class TextProgressBar extends ProgressBar {  
	private String text;  
	private Paint textPaint;  

	public TextProgressBar(Context context, AttributeSet attrs) {  
		super(context, attrs);  
		text = "0%";  
		
		Typeface tf_cb = Typeface.createFromAsset(context.getAssets(), "fonts/DINNextLTPro-BoldCondensed.otf");
		
		textPaint = new Paint();  
		textPaint.setTextSize(20);
		textPaint.setShadowLayer((float)1.3, (float)1.3, (float)1.3, 0xAA333333);
		textPaint.setTypeface(tf_cb);
		textPaint.setColor(Color.WHITE);  
	}  

	@Override  
	protected synchronized void onDraw(Canvas canvas) {  
		// First draw the regular progress bar, then custom draw our text  
		super.onDraw(canvas);  
		Rect bounds = new Rect();  
		textPaint.getTextBounds(text, 0, text.length(), bounds);  
		int x = getWidth() / 2 - bounds.centerX();  
		int y = getHeight() / 2 - bounds.centerY();  
		canvas.drawText(text, x, y, textPaint);  
	}  

	public synchronized void setText(String text) {  
		this.text = text;  
		drawableStateChanged();  
	}  
}