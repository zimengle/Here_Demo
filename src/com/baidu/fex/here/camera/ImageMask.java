package com.baidu.fex.here.camera;

import com.baidu.fex.here.R;
import com.baidu.fex.here.R.dimen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.graphics.PorterDuff;

public class ImageMask extends ImageView {

	public ImageMask(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ImageMask(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ImageMask(Context context) {
		super(context);
		init();
	}

	private Paint clearPaint = new Paint();

	private int rectWidth;
	
	private int rectHeight;
	
	private void drawCircle(Canvas canvas) {
		
		int x1 = (getWidth() - rectWidth) / 2;
		int y1 = Math.max((getHeight() - rectHeight) / 2, 0);
		int x2 = x1 + rectWidth;
		int y2 = y1 + rectHeight;
		canvas.drawOval(new RectF(x1, y1, x2, y2), clearPaint);

	}
	
	private void init() {
		rectWidth = rectHeight = (int)getResources().getDimension(R.dimen.mask_dimen);
		clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		clearPaint.setColor(Color.TRANSPARENT);
		clearPaint.setFilterBitmap(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		drawCircle(canvas);

	}

}
