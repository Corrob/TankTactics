package com.CorGaming.TankTactic;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.CorGaming.framework.FileIO;

public class Map implements Serializable
{
	private static final long serialVersionUID = -434015850541340989L;
	
	public int tiles[][];
	public int width;
	public int height;
	
	public final int TILE_WIDTH = 32;
	public final int TILE_HEIGHT = 32;
	
	public String name;
	public String up;
	public String left;
	public String right;
	public String down;
	
	public List<EnemyTank> enemies;
	public List<MapItem> items;
	public List<NPC> npcs;
	public BossTank boss;
	
	public AStar ai;
	
	public Map(int Width, int Height, String Name) 
	{
		width = Width;
		height = Height;
		tiles = new int[height][width];
		name = Name;
		
		enemies = new ArrayList<EnemyTank>();
		items = new ArrayList<MapItem>();
		npcs = new ArrayList<NPC>();
		boss = null;
		
		ai = new AStar(this);
		
		create();
	}
	
	public void create()
	{
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				tiles[y][x] = 9;
			}
		}
	}
	
	public boolean collidableTile(int x, int y)
	{
		int tile = 0;
		if (!outOfMap(x, y).equals("IN_MAP"))
			return true;
		else
			tile = tiles[y][x];
		
		if (isWater(x, y))
			return true;
		
		else if (isTree(x, y))
			return true;
		
		else if (tile > 54)
			return true;
		
		else
			return false;
	}
	
	public boolean blockBullet(int x, int y)
	{
		int tile = 0;
		if (!outOfMap(x, y).equals("IN_MAP"))
			return true;
		else
			tile = tiles[y][x];
		
		if (isTree(x, y))
			return true;
		
		else if (tile > 54)
			return true;
		
		return false;
	}
	
	public boolean isTree(int x, int y)
	{
		if (outOfMap(x, y).equals("IN_MAP"))
		{
			int tile = tiles[y][x];
			
			if (tile == 8 || tile == 54 || tile == 55 || tile == 62 || tile == 63 || tile == 64)
				return true;
		}
		return false;
	}
	
	public boolean isWater(int x, int y)
	{
		if (outOfMap(x, y).equals("IN_MAP"))
		{
			int tile = tiles[y][x];
			
			if (tile > 35 && tile < 54)
				return true;
		}
		return false;
	}
	
	public boolean canBridge(int x, int y)
	{
		if (outOfMap(x, y).equals("IN_MAP"))
		{
			int tile = tiles[y][x];
			return tile == 45;
		}
		return false;
	}
	
	public boolean canGrassToBridge(int x, int y)
	{
		if (outOfMap(x, y).equals("IN_MAP"))
		{
			int tile = tiles[y][x];
			return tile >= 50 && tile <= 53;
		}
		return false;
	}
	
	public String isShop(int x, int y)
	{
		if (outOfMap(x, y).equals("IN_MAP"))
		{
			int tile = tiles[y][x];
			if (tile == 65)
				return "USE";
			else if (tile == 66)
				return "EQUIP";
		}
		return "NONE";
	}
	
	public String outOfMap(int x, int y)
	{
		if (x < 0)
		{
			return "LEFT";
		} else if (x >= width)
		{
			return "RIGHT";
		} else if (y < 0)
		{
			return "UP";
		} else if (y >= height)
		{
			return "DOWN";
		}
		
		return "IN_MAP";
	}
	
	public void setTile(int tile, int x, int y)
	{
		tiles[y][x] = tile;
	}
	
	public String load(String fileName, FileIO files, PlayerTank tank)
	{
		enemies.clear();
		items.clear();
		npcs.clear();
		boss = null;
		
		String error = "NO ERROR";
		
		try {
			InputStream file = files.readAsset(fileName + ".map");
			ObjectInputStream buffer = new ObjectInputStream(file);
			
			name = (String) buffer.readObject();
			width = buffer.readInt();
			height = buffer.readInt();
			tiles = (int[][]) buffer.readObject();
			
			up = (String) buffer.readObject();
			left = (String) buffer.readObject();
			right = (String) buffer.readObject();
			down = (String) buffer.readObject();

			int size = buffer.readInt();
			
			for (int enemy = 0; enemy < size; enemy++)
			{
				int x = buffer.readInt();
				int y = buffer.readInt();
				int level = buffer.readInt();
				if (level < 24)
					enemies.add(new EnemyTank(Tank.UP, x, y, level));
				else
					boss = new BossTank(Tank.UP, x, y, level);
			}
			
			size = buffer.readInt();
			
			for (int i = 0; i < size; i++)
			{
				int x = buffer.readInt();
				int y = buffer.readInt();
				String name = (String) buffer.readObject();
				String item = this.name + x + y;
				if (tank != null)
				{
					if (!tank.itemFound(item))
						items.add(new MapItem(x, y, name));
				}
				else
					items.add(new MapItem(x, y, name));
			}
			
			size = buffer.readInt();
			
			for (int i = 0; i < size; i++)
			{
				int x = buffer.readInt();
				int y = buffer.readInt();
				String statement = (String) buffer.readObject();
				npcs.add(new NPC(x, y, statement));
			}
						
			buffer.close();
			
			ai = new AStar(this);
		} catch (Exception e) { 
			error = e.getMessage();
		}
		
		return error;
	}
}
