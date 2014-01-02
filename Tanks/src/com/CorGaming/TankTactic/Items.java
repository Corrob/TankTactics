package com.CorGaming.TankTactic;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.CorGaming.framework.FileIO;

public class Items 
{
	public static List<Item> items;
	
	public Items()
	{
		items = new ArrayList<Item>();
	}
	
	public void loadItems(FileIO files)
    {
    	try 
    	{
			InputStream file = files.readAsset("items.dat");
			ObjectInputStream buffer = new ObjectInputStream(file);
			
			int size = buffer.readInt();
			
			for (int item = 0; item < size; item++)
			{
				String name = (String) buffer.readObject();
				String description = (String) buffer.readObject();
				boolean equip = buffer.readBoolean();
				int type = buffer.readInt();
				int hp = buffer.readInt();
				int speed = buffer.readInt();
				int damage = buffer.readInt();
				int attackSpeed = buffer.readInt();
				int cost = buffer.readInt();
				items.add(new Item(name, description, equip, type, hp, speed, damage, attackSpeed, cost));
			}
			
			buffer.close();
		} catch (Exception e) { }
    }
	
	public Item getItem(String name)
	{
		for (int i = 0; i < items.size(); i++)
		{
			if (items.get(i).name.equals(name))
			{
				return items.get(i);
			}
		}
		
		return new Item("None", "Empty Item Slot (Not found)", true, Item.NONE, 0, 0, 0, 0, 0);
	}
	
	public Item getItem(int index)
	{
		return items.get(index);
	}
}
