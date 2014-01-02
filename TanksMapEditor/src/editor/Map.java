package editor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Map
{	
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
	
	public List<EnemyTank> tanks;
	public List<mapItem> items;
	public List<NPC> npcs;
	
	public Map(int Width, int Height, String Name) 
	{
		width = Width;
		height = Height;
		tiles = new int[height][width];
		name = Name;
		tanks = new ArrayList<EnemyTank>();
		items = new ArrayList<mapItem>();
		npcs = new ArrayList<NPC>();
		
		create();
	}
	
	public Map(int Width, int Height, String Name, int tiles[][], List<EnemyTank> tanks, 
			List<mapItem> items, List<NPC> npcs) 
	{
		this(Width, Height, Name);
		
		for (int y = 0; y < tiles.length; y++)
		{
			for (int x = 0; x < tiles[0].length; x++)
			{
				this.tiles[y][x] = tiles[y][x];
			}
		}
		
		this.tanks = tanks;
		this.items = items;
		this.npcs = npcs;
	}
	
	public void create()
	{
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				tiles[y][x] = 0;
			}
		}
	}
	
	public void addTank(int x, int y, int level)
	{
		boolean exists = false;
		
		for (int enemy = 0; enemy < tanks.size(); enemy++)
		{
			if (tanks.get(enemy).x == x && tanks.get(enemy).y == y)
			{
				if (tanks.get(enemy).level == level)
					exists = true;
				else
					tanks.remove(enemy);
			}
		}
		
		if (!exists)
			tanks.add(new EnemyTank(x, y, level));
	}
	
	public void removeTank(int x, int y)
	{
		for (int enemy = 0; enemy < tanks.size(); enemy++)
		{
			if (tanks.get(enemy).x == x && tanks.get(enemy).y == y)
				tanks.remove(enemy);
		}
	}
	
	public void addItem(int x, int y, Item item)
	{
		boolean exists = false;
		
		for (int i = 0; i < items.size(); i++)
		{
			if (items.get(i).x == x && items.get(i).y == y)
			{
				if (items.get(i).itemName.equals(item.name))
					exists = true;
				else
					items.remove(i);
			}
		}
		
		if (!exists)
			items.add(new mapItem(x, y, item.name));
	}
	
	public void removeItem(int x, int y)
	{
		for (int i = 0; i < items.size(); i++)
		{
			if (items.get(i).x == x && items.get(i).y == y)
				items.remove(i);
		}
	}
	
	public void removeNPC(int x, int y)
	{
		for (int i = 0; i < npcs.size(); i++)
		{
			if (npcs.get(i).x == x && npcs.get(i).y == y)
				npcs.remove(i);
		}
	}
	
	public void addNPC(int x, int y, String statement)
	{
		boolean exists = false;
		
		for (int i = 0; i < npcs.size(); i++)
		{
			if (npcs.get(i).x == x && npcs.get(i).y == y)
			{
				if (npcs.get(i).statement.equals(statement))
					exists = true;
				else
					npcs.remove(i);
			}
		}
		
		if (!exists)
			npcs.add(new NPC(statement, x, y));
	}
	
	public void save()
	{
		try {
			FileOutputStream file = new FileOutputStream("C:\\Users\\Cory\\workspace\\Tanks\\assets\\" + name + ".map");
			ObjectOutputStream buffer = new ObjectOutputStream(file);
			
			buffer.writeObject(name);
			buffer.writeInt(width);
			buffer.writeInt(height);
			buffer.writeObject(tiles);
			
			buffer.writeObject(up);
			buffer.writeObject(left);
			buffer.writeObject(right);
			buffer.writeObject(down);
			
			buffer.writeInt(tanks.size());
			
			for (int enemy = 0; enemy < tanks.size(); enemy++)
			{
				buffer.writeInt(tanks.get(enemy).x);
				buffer.writeInt(tanks.get(enemy).y);
				buffer.writeInt(tanks.get(enemy).level);
			}
			
			buffer.writeInt(items.size());
			
			for (int i = 0; i < items.size(); i++)
			{
				buffer.writeInt(items.get(i).x);
				buffer.writeInt(items.get(i).y);
				buffer.writeObject(items.get(i).itemName);
			}
			
			buffer.writeInt(npcs.size());
			
			for (int i = 0; i < npcs.size(); i++)
			{
				buffer.writeInt(npcs.get(i).x);
				buffer.writeInt(npcs.get(i).y);
				buffer.writeObject(npcs.get(i).statement);
			}
			
			buffer.close();
		} catch (Exception e) { System.out.println(e.getMessage()); }
	}
	
	public void load(String fileName)
	{
		tanks.clear();
		items.clear();
		npcs.clear();
		try {
			FileInputStream file = new FileInputStream("C:\\Users\\Cory\\workspace\\Tanks\\assets\\" + fileName + ".map");
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
				addTank(x, y, level);
			}
			
			size = buffer.readInt();
			
			for (int i = 0; i < size; i++)
			{
				int x = buffer.readInt();
				int y = buffer.readInt();
				String name = (String) buffer.readObject();
				Item item = Items.getItem(name);
				addItem(x, y, item);
			}
			
			size = buffer.readInt();
			
			for (int i = 0; i < size; i++)
			{
				int x = buffer.readInt();
				int y = buffer.readInt();
				String statment = (String) buffer.readObject();
				addNPC(x, y, statment);
			}
			
			buffer.close();
		} catch (Exception e) { System.out.println(e.getMessage()); }
	}
}
