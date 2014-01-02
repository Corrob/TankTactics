package com.CorGaming.TankTactic;

public class EnemyTank  extends Tank
{
	private static final long serialVersionUID = 7634974438597554750L;
	
	private float aiCheck = 0.2f;
	private float aiTick;
	public boolean hit;
	
	public EnemyTank(int theDirection, int xPos, int yPos, int lvl)
	{
		super(theDirection, xPos, yPos, lvl);
		
		aiTick = 0;
		maxHP = 50 + level * 10;
		hp = maxHP;
		hit = false;
	}
	
	public String update(float deltaTime, boolean collision, int playerX, int playerY, AStar ai)
	{	
		String out = "";
		
		aiTick += deltaTime;
		while (aiTick > aiCheck)
		{
			aiTick -= aiCheck;
			
			int tileX = getTileX(32);
			int tileY = getTileY(32);
			
			if (Math.abs(tileX - playerX) < getRange() && Math.abs(tileY - playerY) < getRange() || hit)
			{
				if (shotLinedUp(tileX, tileY, playerX, playerY))
				{
					if (notShooting())
					{
						shoot();
						out = "SHOT";
					}
				}
				else
					move(ai.getNextDir(tileX, tileY, playerX, playerY));
			}
			else
				move(Tank.STOP);
		}
		
		super.update(deltaTime, collision);
		
		return out;
	}
	
	private boolean shotLinedUp(int tileX, int tileY, int pX, int pY)
	{
		switch(direction)
		{
		case RIGHT:
			if (pX > tileX && pY == tileY)
				return true;
			break;
		case UP:
			if (pY < tileY && pX == tileX)
				return true;
			break;
		case LEFT:
			if (pX < tileX && pY == tileY)
				return true;
			break;
		case DOWN:
			if (pY > tileY && pX == tileX)
				return true;
			break;
		}
		return false;
	}
	
	private int getRange()
	{
		if (level > 4 && level < 8)
			return level;
		else if (level < 8)
			return 4;
		else
			return 8;
	}
	
	@Override
	public int getAttackDamage()
	{
		return BASE_DAMAGE + level * 3 - 10;
	}
	
	public int expGiven()
	{
		return level * 10;
	}
	
	public int moneyGiven()
	{
		return level;
	}
}
