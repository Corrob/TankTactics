package com.CorGaming.TankTactic;

import java.io.Serializable;

public class Item  implements Serializable
{
	private static final long serialVersionUID = -7270252445613417795L;
	public static final int NONE = 0;
	public static final int BRIDGE = 1;
	public static final int HEALTH = 2;
	public static final int SUPER_HEALTH = 3;
	public static final int TREE_BULLET = 4;
	public static final int SPEED_BOOST = 5;
	public static final int B_ARMOR = 10;
	public static final int S_ARMOR = 11;
	public static final int G_ARMOR = 12;
	public static final int B_TURRET = 13;
	public static final int S_TURRET = 14;
	public static final int G_TURRET = 15;
	public static final int WIDTH = 64;
	public static final int HEIGHT = 64;
	
	public String name;
	public String description;
	public boolean equipable;
	public int type;
	public int hp;
	public int speed;
	public int damage;
	public int attackSpeed;
	public int cost;
	
	public Item(String name, String description, boolean equipable, int type, int hp, int speed, int damage, int attackSpeed,
			int cost)
	{
		this.name = name;
		this.description = description;
		this.equipable = equipable;
		this.type = type;
		this.hp = hp;
		this.speed = speed;
		this.damage = damage;
		this.attackSpeed = attackSpeed;
		this.cost = cost;
	}
}
