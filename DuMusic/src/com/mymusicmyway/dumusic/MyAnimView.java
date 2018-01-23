package com.mymusicmyway.dumusic;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

public class MyAnimView extends View {
	public static final float RADIUS = 50f;
	private Point currentPoint;
	private Paint mPaint;

	private boolean isEnd;

	private String drawText = "DuMusic";
	
	public boolean reStart=false;

	public MyAnimView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.WHITE);
		mPaint.setTextSize(textSize);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (currentPoint == null) {
			currentPoint = new Point(RADIUS, RADIUS);
			drawCircle(canvas);
			startAnimation();
		}else if(reStart){
			drawCircle(canvas);
			textSize=20;
			isEnd=false;
			reStart=false;
			startAnimation();
			
		} 
		else {
			drawCircle(canvas);
			

		}

	}

	private float endX;
	private float endY;

	@SuppressLint("NewApi")
	private void startAnimation() {
		Point startPoint = new Point(getWidth() / 2, RADIUS);
		endX = getWidth() / 2;
		endY = getHeight() - RADIUS;
		Point endPoint = new Point(endX, endY);
		ValueAnimator animator = ValueAnimator.ofObject(new PointEvaluator(),
				startPoint, endPoint);
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				currentPoint = (Point) animation.getAnimatedValue();
				invalidate();
			}
		});
		// 通过mycolor参数映射到对应的Set方法自动回调在set方法中改变画笔的颜色
		ObjectAnimator animator2 = ObjectAnimator.ofObject(this, "mycolor",
				new ColorEvaluator(), "#0000FF", "#FF0000");
		AnimatorSet animatorSet = new AnimatorSet();
		// 实现AnimatorListener的之类只需要重写自己需要的方法
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				isEnd = true;
				invalidate();
			}
		});
		// 反弹效果
		animator.setInterpolator(new BounceInterpolator());
		animatorSet.play(animator).with(animator2);
		animatorSet.setDuration(7000);
		animatorSet.start();

	}

	private float textSize = 20;

	private void drawCircle(Canvas canvas) {

		mPaint.setTextSize(textSize);
		// mPaint.setAlpha(50);
		if (isEnd) {
			drawText = "";
		} else {
			canvas.drawText(drawText, currentPoint.getX(), currentPoint.getY(),
					mPaint);
		}
		// mPaint.setColor(Color.WHITE);
		// canvas.drawCircle(currentPoint.getX(), currentPoint.getY(), RADIUS,
		// mPaint);

	}

	private String mycolor;

	public String getMycolor() {
		return mycolor;
	}

	public void setMycolor(String mycolor) {
		this.mycolor = mycolor;
		mPaint.setColor(Color.parseColor(mycolor));
		textSize = textSize + 0.2f;
		invalidate();
	}

}
