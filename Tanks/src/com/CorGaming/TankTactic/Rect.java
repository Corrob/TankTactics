package com.CorGaming.TankTactic;

import java.io.Serializable;

public class Rect implements Serializable
{
	private static final long serialVersionUID = 1846331304832146707L;
	public int left, right, top, bottom;
	
	public Rect(int Left, int Top, int Right, int Bottom)
	{
		left = Left;
		top = Top;
		right = Right;
		bottom = Bottom;
	}
	
	public int width()
	{
		return right - left;
	}
	
	public int height()
	{
		return bottom - top;
	}
	
	public int centerX()
	{
		return (left + right) / 2;
	}
	
	public int centerY()
	{
		return (top + bottom) / 2;
	}
	
	public void offsetTo(int x, int y)
	{
		int w = width();
		int h = height();
		left = x;
		top = y;
		right = x + w;
		bottom = y + h;
	}
	
	public boolean contains(int x, int y)
	{
		boolean withinX = left < x && x < right;
		boolean withinY = top < y && y < bottom;
		return withinX && withinY;
	}
	
	public static boolean intersects(Rect r1, Rect r2)
	{
		return !(r2.left > r1.right || r2.right < r1.left || r2.top > r1.bottom || r2.bottom < r1.top);
	}
}
