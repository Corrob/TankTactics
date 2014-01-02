package com.CorGaming.TankTactic;

import java.io.Serializable;

public class MapItem implements Serializable
{
	private static final long serialVersionUID = -8656970453672523402L;
	
	public String itemName;
	public int x, y;
	
	public MapItem(int x, int y, String itemName)
	{
		this.itemName = itemName;
		this.x = x;
		this.y = y;
	}
}
