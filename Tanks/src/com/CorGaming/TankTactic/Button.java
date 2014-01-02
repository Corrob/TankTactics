package com.CorGaming.TankTactic;

import java.io.Serializable;

public class Button implements Serializable
{
	private static final long serialVersionUID = -6499124276290268637L;
	private Rect dRect;
	private Rect sRect;
	
	public Button(Rect destination, Rect source)
	{
		dRect = destination;
		sRect = source;
	}
	
	public boolean contains(int x, int y)
	{
		return dRect.contains(x, y);
	}
	
	public int X()
	{
		return dRect.left;
	}
	
	public int Y()
	{
		return dRect.top;
	}
	
	public int centerX()
	{
		return dRect.centerX();
	}
	
	public int centerY()
	{
		return dRect.centerY();
	}
	
	public int sourceX()
	{
		return sRect.left;
	}
	
	public int sourceY()
	{
		return sRect.top;
	}
	
	public int sourceW()
	{
		return sRect.width();
	}
	
	public int sourceH()
	{
		return sRect.height();
	}
}
