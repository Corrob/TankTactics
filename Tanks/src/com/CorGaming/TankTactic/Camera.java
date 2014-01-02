package com.CorGaming.TankTactic;

import java.io.Serializable;

public class Camera  implements Serializable
{
	private static final long serialVersionUID = -1639920257719300957L;
	public Rect rect;
	int mapWidth, mapHeight;
	
	public Camera(int mapW, int mapH) 
	{
		rect = new Rect(0, 0, 480, 256);
		mapWidth = mapW;
		mapHeight = mapH;
	}
	
	public void setPos(int x, int y)
	{
		int newX = x;
		int newY = y;
		
		if (x < 0)
			newX = 0;
		if (x + rect.width() > mapWidth)
			newX = mapWidth - rect.width();
		if (y < 0)
			newY = 0;
		if (y + rect.height() > mapHeight)
			newY = mapHeight - rect.height();
		
		rect.offsetTo(newX, newY);
	}
}
