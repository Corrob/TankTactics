package com.CorGaming.TankTactic;

import java.io.Serializable;

public class Explosion implements Serializable
{
	private static final long serialVersionUID = -3441645365747532539L;
	public static int SMALL = 0;
	public static int BIG = 1;
	
	private final int SMALL_FRAMES = 2;
	private final int BIG_FRAMES = 3;
    private final float FRAMETIME = 0.2f;
    
    public int x;
    public int y;
    public int type;
    
    public Rect source;
	private int curFrame;
	private int numFrames;
	private float tickTime;
	
	public Explosion(int X, int Y, int Type)
	{
		x = X;
		y = Y;
		type = Type;
		
		curFrame = 0;
		
		if(type == SMALL)
		{
			source = new Rect(96, 64, 128, 96);
			numFrames = SMALL_FRAMES;
		}
		else if (type == BIG)
		{
			source = new Rect(0, 64, 32, 96);
			numFrames = BIG_FRAMES;
		}
	}
	
	public boolean update(float deltaTime)
	{
		tickTime += deltaTime;
		
		while (tickTime > FRAMETIME)
		{
			tickTime -= FRAMETIME;
			curFrame++;
			if (curFrame >= numFrames)
				return false; //don't continue to update
		}
		
		if (type == SMALL)
			source.offsetTo(curFrame * 32 + 96, source.top);
		else if (type == BIG)
			source.offsetTo(curFrame * 32, source.top);
		
		return true; //continue to update
	}
}

