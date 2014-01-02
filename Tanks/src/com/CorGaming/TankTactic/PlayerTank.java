package com.CorGaming.TankTactic;

import java.util.ArrayList;
import java.util.List;

public class PlayerTank extends Tank
{
	private static final long serialVersionUID = 1705972099256108530L;
	private final int EXP_PER_LEVEL = 100;
	public final int INV_SIZE = 10;
	public final int SPEED_BUFF_TIME = 5;
	
	public int experience;
	public int money;
	
	public Item[] inventory;
	public int[] numItems;
	public List<String> itemsFound;
	
	String buffed;
	
	public PlayerTank(int theDirection, int xPos, int yPos, int lvl)
	{
		super(theDirection, xPos, yPos, lvl);
		
		experience = 0;
		money = 0;
		
		inventory = new Item[INV_SIZE];
		numItems = new int[INV_SIZE];
		
		for (int i = 0; i < INV_SIZE; i++)
		{
			/*if (i < INV_SIZE)
			{
				inventory[i] = Assets.items.getItem(i);
				numItems[i] = 99;
				
			}
			else
			{*/
				inventory[i] = Assets.items.getItem("None");
				numItems[i] = 0;
			//}
		}
		
		buffed = "None";
		itemsFound = new ArrayList<String>();
	}
	
	public void update(float deltaTime, boolean collision)
	{
		super.update(deltaTime, collision);
		
		if (state.equals("moving") && !collision)
		{
			addHP(deltaTime * (maxHP / 100.0f * 7));
		}
	}
	
	public void addExp(int amount)
	{
		experience += amount;
		if (experience >= expToNextLevel())
		{
			experience -= expToNextLevel();
			level++;
			
			calcStats();
			hp = maxHP; //restore full health on level up
		}
	}
	
	public int expToNextLevel()
	{
		return level * EXP_PER_LEVEL;
	}
	
	@Override
	public int getAttackDamage()
	{
		return BASE_DAMAGE + level * 2 + equipDamage;
	}
	
	public void buffSpeed()
	{
		speed += 50;
		buffed = "Speed";
	}
	
	public void unBuffSpeed()
	{
		speed -= 50;
		buffed = "None";
	}
	
	public boolean speedBuffed()
	{
		return buffed.equals("Speed");
	}
	
	public boolean buffed()
	{
		return !buffed.equals("None");
	}
	
	public void calcStats()
	{
		calcMaxHP();		
		calcSpeed();
		calcAttackSpeed();
	}
	
	public void addHP(float amount)
	{
		hp += amount;
		if (hp > maxHP)
			hp = maxHP;
	}
	
	public int getSlot(Item item)
	{
		for (int i = 0; i < INV_SIZE; i++)
		{
			if (item.name.equals(inventory[i].name))
				return i;
		}
		return -1;
	}
	
	public int emptySlot()
	{
		for (int i = 0; i < INV_SIZE - 2; i++)
		{
			if (inventory[i].name.equals("None"))
				return i;
		}
		return -1;
	}
	
	public boolean itemFound(String item)
	{
		for (String itemFound : itemsFound)
		{
			if (itemFound.equals(item))
				return true;
		}
		return false;
	}
	
	private void calcSpeed()
	{
		speed = BASE_SPEED + level + equipSpeed;
	}
	
	private void calcMaxHP()
	{
		int newMaxHP = 150 + level * 10 + equipHP;
		if (newMaxHP < maxHP)
			hp = newMaxHP;
		maxHP = newMaxHP;
	}
	
	private void calcAttackSpeed()
	{
		attackSpeed = level + equipAttackSpeed;
	}
}
