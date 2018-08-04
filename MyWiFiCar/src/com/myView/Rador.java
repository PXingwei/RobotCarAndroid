package com.myView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

public class Rador extends View{
	
	private Paint paint;
	private RectF oval=new RectF();
	private PointF mCenterPoint;
	
	private int mode;
	
	public Rador(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint=new Paint();
		paint.setStrokeWidth(10);
		
		getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			
			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub
				getViewTreeObserver().removeOnPreDrawListener(this); 
				
				mCenterPoint = new PointF(getWidth() / 2, getHeight() / 2);
				
				return true;
			}
		});
		
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		switch(mode){
		// 000
		case 0:
			paint.setColor(Color.GRAY);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//001	
		case 1:
			paint.setColor(Color.GRAY);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//002	
		case 2:
			paint.setColor(Color.GRAY);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.GRAY);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//003	
		case 3:
			drawLow();
			paint.setColor(Color.GRAY);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.GRAY);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			break;
		//010	
		case 4:
			paint.setColor(Color.GRAY);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//011	
		case 5:
			paint.setColor(Color.GRAY);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//012
		case 6:
			paint.setColor(Color.GRAY);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//013	
		case 7:
			paint.setColor(Color.GRAY);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			break;
		//020	
		case 8:
			paint.setColor(Color.GRAY);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//021	
		case 9:
			paint.setColor(Color.GRAY);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//022	
		case 10:
			paint.setColor(Color.GRAY);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//023	
		case 11:
			paint.setColor(Color.GRAY);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			break;
		//030
		case 12:
			paint.setColor(Color.GRAY);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			break;
		//031	
		case 13:
			paint.setColor(Color.GRAY);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			break;
		//032	
		case 14:
			paint.setColor(Color.GRAY);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			break;
		//033	
		case 15:
			paint.setColor(Color.GRAY);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//100	
		case 16:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//101	
		case 17:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//102	
		case 18:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//103	
		case 19:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			break;
		//110
		case 20:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//111	
		case 21:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//112	
		case 22:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//113	
		case 23:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			break;
		//120	
		case 24:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawMid();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//121	
		case 25:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.GRAY);
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//122	
		case 26:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.GRAY);
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//123	
		case 27:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.GRAY);
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			break;
		//130	
		case 28:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawMid();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			break;
		//131	
		case 29:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawMid();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			break;
		//132	
		case 30:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.GRAY);
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			break;
		//133	
		case 31:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.GRAY);
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//200	
		case 32:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//201	
		case 33:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//202	
		case 34:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//203	
		case 35:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			break;
		//210	
		case 36:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//211	
		case 37:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//212	
		case 38:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//213	
		case 39:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			break;
		//220	
		case 40:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//221	
		case 41:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//222	
		case 42:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//223	
		case 43:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			break;
		//230	
		case 44:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			break;
		//231	
		case 45:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawHigh();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//232	
		case 46:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			break;
		//233	
		case 47:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			paint.setColor(Color.GRAY);
			drawHigh();
			drawRadorLeft(canvas);
			paint.setColor(Color.RED);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//300	
		case 48:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//301	
		case 49:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//302	
		case 50:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//303	
		case 51:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			break;
		//310	
		case 52:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//311	
		case 53:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//312	
		case 54:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//313	
		case 55:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			break;
		//320	
		case 56:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//321	
		case 57:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//322	
		case 58:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		//323	
		case 59:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			paint.setColor(Color.GRAY);
			drawRadorMid(canvas);
			paint.setColor(Color.RED);
			drawRadorRight(canvas);
			break;
		//330	
		case 60:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			break;
		//331	
		case 61:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			break;
		//332	
		case 62:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			paint.setColor(Color.GRAY);
			drawRadorRight(canvas);
			break;
		//333	
		case 63:
			paint.setColor(Color.RED);
			drawLow();
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawMid();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			
			drawHigh();
			paint.setColor(Color.RED);
			drawRadorLeft(canvas);
			drawRadorMid(canvas);
			drawRadorRight(canvas);
			break;
		}

	}
	
	public void getMode(int left, int mid, int right){
		mode = dealData(left, mid, right);
	}
	
	private int dealData(int left, int mid, int right){
		
		if(left==0&&mid==0&&right==1){
			return 1;
		}else if(left==0&&mid==0&&right==2){
			return 2;
		}else if(left==0&&mid==0&&right==3){
			return 3;
		}else if(left==0&&mid==1&&right==0){
			return 4;
		}else if(left==0&&mid==1&&right==1){
			return 5;
		}else if(left==0&&mid==1&&right==2){
			return 6;
		}else if(left==0&&mid==1&&right==3){
			return 7;
		}else if(left==0&&mid==2&&right==0){
			return 8;
		}else if(left==0&&mid==2&&right==1){
			return 9;
		}else if(left==0&&mid==2&&right==2){
			return 10;
		}else if(left==0&&mid==2&&right==3){
			return 11;
		}else if(left==0&&mid==3&&right==0){
			return 12;
		}else if(left==0&&mid==3&&right==1){
			return 13;
		}else if(left==0&&mid==3&&right==2){
			return 14;
		}else if(left==0&&mid==3&&right==3){
			return 15;
		}
		
		if(left==1&&mid==0&&right==0){
			return 16;
		}else if(left==1&&mid==0&&right==1){
			return 17;
		}else if(left==1&&mid==0&&right==2){
			return 18;
		}else if(left==1&&mid==1&&right==3){
			return 19;
		}else if(left==1&&mid==1&&right==0){
			return 20;
		}else if(left==1&&mid==1&&right==1){
			return 21;
		}else if(left==1&&mid==1&&right==2){
			return 22;
		}else if(left==1&&mid==1&&right==3){
			return 23;
		}else if(left==1&&mid==2&&right==0){
			return 24;
		}else if(left==1&&mid==2&&right==1){
			return 25;
		}else if(left==1&&mid==2&&right==2){
			return 26;
		}else if(left==1&&mid==2&&right==3){
			return 27;
		}else if(left==1&&mid==3&&right==0){
			return 28;
		}else if(left==1&&mid==3&&right==1){
			return 29;
		}else if(left==1&&mid==3&&right==2){
			return 30;
		}else if(left==1&&mid==3&&right==3){
			return 31;
		}
		
		if(left==2&&mid==0&&right==0){
			return 32;
		}else if(left==2&&mid==0&&right==1){
			return 33;
		}else if(left==2&&mid==0&&right==2){
			return 34;
		}else if(left==2&&mid==0&&right==3){
			return 35;
		}else if(left==2&&mid==1&&right==0){
			return 36;
		}else if(left==2&&mid==1&&right==1){
			return 37;
		}else if(left==2&&mid==1&&right==2){
			return 38;
		}else if(left==2&&mid==1&&right==3){
			return 39;
		}else if(left==2&&mid==2&&right==0){
			return 40;
		}else if(left==2&&mid==2&&right==1){
			return 41;
		}else if(left==2&&mid==2&&right==2){
			return 42;
		}else if(left==2&&mid==2&&right==3){
			return 43;
		}else if(left==2&&mid==3&&right==0){
			return 44;
		}else if(left==2&&mid==3&&right==1){
			return 45;
		}else if(left==2&&mid==3&&right==2){
			return 46;
		}else if(left==2&&mid==3&&right==3){
			return 47;
		}
		
		if(left==3&&mid==0&&right==0){
			return 48;
		}else if(left==3&&mid==0&&right==1){
			return 49;
		}else if(left==3&&mid==0&&right==2){
			return 50;
		}else if(left==3&&mid==0&&right==3){
			return 51;
		}else if(left==3&&mid==1&&right==0){
			return 52;
		}else if(left==3&&mid==1&&right==1){
			return 53;
		}else if(left==3&&mid==1&&right==2){
			return 54;
		}else if(left==3&&mid==1&&right==3){
			return 55;
		}else if(left==3&&mid==2&&right==0){
			return 56;
		}else if(left==3&&mid==2&&right==1){
			return 57;
		}else if(left==3&&mid==2&&right==2){
			return 58;
		}else if(left==3&&mid==2&&right==3){
			return 59;
		}else if(left==3&&mid==3&&right==0){
			return 60;
		}else if(left==3&&mid==3&&right==1){
			return 61;
		}else if(left==3&&mid==3&&right==2){
			return 62;
		}else if(left==3&&mid==3&&right==3){
			return 63;
		}
		
		return 0;
	}
	
	private void drawRadorLeft(Canvas canvas){
		canvas.drawArc(oval, -135, 18, false, paint);
	}

	private void drawRadorMid(Canvas canvas){
		canvas.drawArc(oval, -99, 18, false, paint);
	}
	
	private void drawRadorRight(Canvas canvas){
		canvas.drawArc(oval, -63, 18, false, paint);
	}
	
	private void drawLow(){
		oval.left=(float)(getWidth()*1/3); 
	    oval.top=(float)(getHeight()*3/4);
	    oval.right=(float)(getWidth()*2/3); 
	    oval.bottom=(float)(getHeight()*5/4);
	}
	
	private void drawMid(){
		oval.left=(float)(getWidth()/4); 
	    oval.top=(float)(getHeight()*5/8);
	    oval.right=(float)(getWidth()*3/4); 
	    oval.bottom=(float)(getHeight()*11/8);
	}
	
	private void drawHigh(){
		oval.left=(float)(getWidth()/6); 
	    oval.top=(float)(getHeight()/3);
	    oval.right=(float)(getWidth()*5/6); 
	    oval.bottom=(float)(getHeight()*5/3);
	}
}
