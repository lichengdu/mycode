package com.mymusicmyway.dumusic;

import android.animation.TypeEvaluator;
import android.annotation.SuppressLint;

@SuppressLint("NewApi")
public class PointEvaluator implements TypeEvaluator<Object> {

	@Override
	public Object evaluate(float fraction, Object stratValue, Object endValue) {
		Point startPoint = (Point) stratValue;
		Point endPoint = (Point) endValue;
		float x = startPoint.getX() + fraction
				* (endPoint.getX() - startPoint.getX());
		float y = startPoint.getY() + fraction
		* (endPoint.getY() - startPoint.getY());
		return new Point(x-(30*4),y+15);
	}

}
