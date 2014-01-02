package com.CorGaming.TankTactic;

public class BossTank extends EnemyTank 
{
	private static final long serialVersionUID = -2378679588093073887L;

	public BossTank(int theDirection, int xPos, int yPos, int lvl) 
	{
		super(theDirection, xPos, yPos, lvl);
		
		width = 48;
		
		maxHP = 500 + level * 10;
		hp = maxHP;
	}
	
	@Override
	public String update(float deltaTime, boolean collision, int playerX, int playerY, AStar ai)
	{
		return super.update(deltaTime, collision, playerX, playerY, ai);
	}
	
	@Override
	public int getAttackDamage()
	{
		return BASE_DAMAGE + level * 5;
	}
}
