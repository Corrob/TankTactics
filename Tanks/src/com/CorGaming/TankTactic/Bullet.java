package com.CorGaming.TankTactic;

import java.io.Serializable;

public class Bullet implements Serializable
{
	private static final long serialVersionUID = 4374197815615462605L;
	public static final int RIGHT = 0;
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int DOWN = 3;
    
    public int WIDTH;
    public int HEIGHT;
    
    final int SPEED = 200;
    
    public float x;
    public float y;
    
    public int direction;
    
    public Rect rect;
    public Rect source;
    
    public Tank sentFrom;
    
    public float timeTraveled = 0;
    
	public Bullet(int X, int Y, int Direction, Tank from)
	{
		x = X;
		y = Y;
		WIDTH = 8;
		HEIGHT = 8;
		rect = new Rect(X, Y, WIDTH, HEIGHT);
		direction = Direction;
		source = new Rect(0, 0, WIDTH, HEIGHT);
		sentFrom = from;
	}
	
	public Bullet(int X, int Y, int Direction, int w, int h)
	{
		x = X;
		y = Y;
		WIDTH = w;
		HEIGHT = h;
		rect = new Rect(X, Y, WIDTH, HEIGHT);
		direction = Direction;
		source = new Rect(0, 32, WIDTH, 32 + HEIGHT);
		sentFrom = new Tank(0, 0, 0, 0);
	}
	
	public void update(float deltaTime)
	{
		timeTraveled += deltaTime;
		
		switch(direction)
		{
		case RIGHT:
			x += SPEED * deltaTime;
			break;
		case UP:
			y -= SPEED * deltaTime;
			break;
		case LEFT:
			x -= SPEED * deltaTime;
			break;
		case DOWN:
			y += SPEED * deltaTime;
			break;
		}
		
		rect.offsetTo((int) x, (int) y);
		
		source.offsetTo(direction * 32, source.top);
	}
	
	public boolean outOfBounds(int width, int height)
	{
		if (rect.left < 0 || rect.top < 0 || rect.right >= width || rect.bottom >= height ||
				timeTraveled > 1.0f)
			return true;
		else
			return false;
	}
	
	public int getTileX(int tileWidth)
	{
		return ((int) x + 5) / tileWidth;
	}
	
	public int getTileY(int tileHeight)
	{
		return ((int) y + 5) / tileHeight;
	}
}
