package com.myView;

import com.mywificar.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

public class BallButton extends View{
	
	private float mRockerBg_X;
	private float mRockerBg_Y;
	private Bitmap mBmpRockerBtn;
	
	private Paint paint;
	BallChangeListener mBallChangeListener = null;
	
	private PointF mCenterPoint;

	public BallButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	
	public BallButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint=new Paint(Paint.DITHER_FLAG);
		mBmpRockerBtn = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocker_btn);
		
		getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			
			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub
				getViewTreeObserver().removeOnPreDrawListener(this); 
				
				Log.e("RockerView", getWidth() + "/" +  getHeight());
				mCenterPoint = new PointF(getWidth() / 2, getHeight() / 2);
				mRockerBg_X = mCenterPoint.x;
				mRockerBg_Y = mCenterPoint.y;
				
				return true;
			}
		});
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					
					//系统调用onDraw方法刷新画面
					BallButton.this.postInvalidate();
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(5);
		canvas.drawBitmap(mBmpRockerBtn, null, 
				new Rect((int)(mRockerBg_X - getWidth() / 2), 
						(int)(mRockerBg_Y - getWidth() / 2), 
						(int)(mRockerBg_X + getWidth() / 2), 
						(int)(mRockerBg_Y + getWidth() / 2)), 
				null);
		canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			
			if(event.getY() > mCenterPoint.y+getHeight()/2){
				mRockerBg_Y = mCenterPoint.y+getHeight()/2-getWidth()/2;
			}else if(event.getY() < mCenterPoint.y-getHeight()/2){
				mRockerBg_Y = mCenterPoint.y-getHeight()/2+getWidth()/2;
			}else{
				mRockerBg_Y = event.getY();
			}
			if(mBallChangeListener != null) {
				mBallChangeListener.moveToPoint(mRockerBg_Y-getHeight()/2);
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			//当释放按键时摇杆要恢复摇杆的位置为初始位置
			mRockerBg_X = mCenterPoint.x;
			mRockerBg_Y = mCenterPoint.y;
			if(mBallChangeListener != null) {
				mBallChangeListener.moveToPoint(0);
			}
		}
		return true;
	}
	public void setBallChangeListener(BallChangeListener ballChangeListener) {
		mBallChangeListener = ballChangeListener;
	}
	
	public interface BallChangeListener {
		public void moveToPoint( float y);
	}
}
