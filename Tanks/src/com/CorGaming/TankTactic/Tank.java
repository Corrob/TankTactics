package com.CorGaming.TankTactic;

import java.io.Serializable;

public class Tank implements Serializable
{
	private static final long serialVersionUID = -528984094654695376L;
	public static final int STOP = -1;
	public static final int RIGHT = 0;
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int DOWN = 3;
    
    public int width = 32;
    
    private final int MOVING_FRAMES = 7;
    private final int SHOOTING_FRAMES = 3;
    private final float MOVING_FRAMETIME = 0.1f;
    private final float SHOOTING_FRAMETIME = 0.2f;
    
    protected final int BASE_DAMAGE = 20;
    protected final int BASE_SPEED = 70;
    
    public int direction;
    public float x;
	public float y;
	
	public Rect source;
	private int numFrames;
	private int curFrame;
	private float frameTime;
	private float tickTime;
	
	public String state;
	
	protected int level;
	
    public int speed; //pixels per second
    public int attackSpeed;
    public float hp;
    public int maxHP;
    
    public int equipSpeed;
    public int equipHP;
    public int equipDamage;
    public int equipAttackSpeed;
	
	public Tank(int theDirection, int xPos, int yPos, int lvl) 
	{
		direction = theDirection;
		x = xPos;
		y = yPos;
		
		source = new Rect(0, 0, width, width);
		numFrames = MOVING_FRAMES;
		curFrame = 0;
		
		state = "still";
		
		frameTime = MOVING_FRAMETIME;
		tickTime = 0;
		
		level = lvl;
		
		speed = BASE_SPEED + level;
		attackSpeed = level;
		
		maxHP = 100 + level * 10;
		hp = maxHP;
		
		equipSpeed = 0;
	    equipHP = 0;
	    equipDamage = 0;
	    equipAttackSpeed = 0;
	}
	
	public void shoot()
	{
		state = "shooting";
		curFrame = 0;
		numFrames = SHOOTING_FRAMES;
		frameTime = SHOOTING_FRAMETIME - (attackSpeed * .004f);
	}
	
	public void move(int theDirection)
	{
		if (theDirection == STOP)
		{
			if (moving())
				state = "still";
		} else if (notShooting())
		{
			direction = theDirection;
			state = "moving";
		}
	}
	
	public void update(float deltaTime, boolean collision)
	{		
		if (state.equals("moving") && !collision)
		{
			switch(direction)
			{
			case RIGHT:
				x += speed * deltaTime;
				break;
			case UP:
				y -= speed * deltaTime;
				break;
			case LEFT:
				x -= speed * deltaTime;
				break;
			case DOWN:
				y += speed * deltaTime;
				break;
			}
		}
		
		tickTime += deltaTime;
		
		while (tickTime > frameTime)
		{
			tickTime -= frameTime;
			
			if (state.equals("moving"))
			{
				curFrame++;
				if (curFrame >= numFrames)
					curFrame = 0;
			} else if (state.equals("shooting"))
			{
				curFrame++;
				if (curFrame >= numFrames)
				{
					state = "still";
					curFrame = 0;
					numFrames = MOVING_FRAMES;
					frameTime = MOVING_FRAMETIME;
				}
			}
		}
		
		if (state.equals("shooting"))
			source.offsetTo((curFrame + (direction % 2 * 3)) * 32, (direction / 2 + 4) * 32);
		else 
			source.offsetTo(curFrame * 32, direction * 32);
	}
	
	public void unMove(float deltaTime)
	{
		if (state.equals("moving"))
		{
			switch(direction)
			{
			case RIGHT:
				x -= (speed + level) * deltaTime;
				break;
			case UP:
				y += (speed + level) * deltaTime;
				break;
			case LEFT:
				x += (speed + level) * deltaTime;
				break;
			case DOWN:
				y -= (speed + level) * deltaTime;
				break;
			}
		}
	}
	
	public int getTileAheadX(int tileWidth)
	{
		int tileX = 0;
		
		switch(direction)
		{
		case RIGHT:
			tileX = ((int) x) / tileWidth;
			return tileX + 1;
		case DOWN:
		case UP:
			tileX = ((int) x + width / 2) / tileWidth;
			return tileX;
		case LEFT:
			tileX = ((int) x + width) / tileWidth;
			return tileX - 1;
		}
		return tileX;
	}
	
	public int getTileAheadY(int tileHeight)
	{
		int tileY = 0;
		
		switch(direction)
		{
		case DOWN:
			tileY = ((int) y) / tileHeight;
			return tileY + 1;
		case LEFT:
		case RIGHT:
			tileY = ((int) y + width / 2) / tileHeight;
			return tileY;
		case UP:
			tileY = ((int) y + width) / tileHeight;
			return tileY - 1;
		}
		
		return tileY;
	}
	
	public int getTileX(int tileWidth)
	{
		return ((int) x +  width / 2) / tileWidth;
	}
	
	public int getTileY(int tileHeight)
	{
		return ((int) y + width / 2) / tileHeight;
	}
	
	public int centerX()
	{
		return (int) x + width / 2;
	}
	
	public int centerY()
	{
		return (int) y + width / 2;
	}
	
	public boolean notShooting()
	{
		return !state.equals("shooting");
	}
	
	public boolean moving()
	{
		return state.equals("moving");
	}
	
	public String getLevel()
	{
		return Integer.toString(level);
	}
	
	public int getAttackDamage()
	{
		return 0;
	}
}
