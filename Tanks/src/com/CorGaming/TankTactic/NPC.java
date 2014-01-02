package com.CorGaming.TankTactic;

import java.util.Random;

public class NPC extends Tank
{
	private static final long serialVersionUID = -126375026531012350L;
	
	public String statement;
	
	private float movementTime;
	private final float TIME_TO_MOVE = 0.5f;
	
	public NPC(int xPos, int yPos, String statement) 
	{
		super(Tank.UP, xPos, yPos, 0);
		speed = BASE_SPEED / 2;
		
		this.statement = statement;
		movementTime = 0f;
	}
	
	@Override
	public void update(float deltaTime, boolean collision)
	{
		movementTime += deltaTime;
		
		if(movementTime > TIME_TO_MOVE)
		{
			movementTime = 0f;
			
			Random rand = new Random();
			
			int keepMoving = rand.nextInt(2);
			
			if (keepMoving == 0)
			{
				switch(direction)
				{
				case RIGHT:
					move(UP);
					break;
				case UP:
					move(LEFT);
					break;
				case LEFT:
					move(DOWN);
					break;
				case DOWN:
					move(RIGHT);
					break;
				}
			} else
			{
				move(STOP);
			}
		}
		super.update(deltaTime, collision);
	}
}
