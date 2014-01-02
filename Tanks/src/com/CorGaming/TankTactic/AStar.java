package com.CorGaming.TankTactic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AStar implements Serializable
{
	private static final long serialVersionUID = 7993572648728434580L;
	private Map map;
	private List<Tile> open;
	private List<Tile> closed;
	private int start;
	private int end;
	
	public String debug;
	
	public AStar(Map theMap)
	{
		map = theMap;
		
		open = new ArrayList<Tile>();
		closed = new ArrayList<Tile>();
		
		debug = "";
	}
	
	public int getNextDir(int x1, int y1, int x2, int y2)
	{
		if (x1 == x2 && y1 == y2)
			return Tank.STOP;
		
		Tile curTile;
		open.clear();
		closed.clear();
		
		start = CoordToInt(x1, y1);
		end = CoordToInt(x2, y2);
		
		open.add(new Tile(start, -1, 0, getH(start)));
		
		int attempts = 0;
		
		while (!closedContains(end) && open.size() != 0 && attempts < 100)
		{
			attempts++;
			
			curTile = openMin();
			
			closed.add(curTile);
			removeOpen(curTile.pos);
			
			curTile = closed.get(closed.size() - 1);
			
			for (int dir = 0; dir < 4; dir++)
			{
				int newPos = 0;
				
				switch(dir)
				{
				case Tank.RIGHT:
					newPos = CoordToInt(IntToX(curTile.pos) + 1, IntToY(curTile.pos));
					break;
				case Tank.UP:
					newPos = CoordToInt(IntToX(curTile.pos), IntToY(curTile.pos) - 1);
					break;
				case Tank.LEFT:
					newPos = CoordToInt(IntToX(curTile.pos) - 1, IntToY(curTile.pos));
					break;
				case Tank.DOWN:
					newPos = CoordToInt(IntToX(curTile.pos), IntToY(curTile.pos) + 1);
					break;
				}
				
				if (!map.collidableTile(IntToX(newPos), IntToY(newPos)) && !closedContains(newPos) && !openContains(newPos))
					open.add(new Tile(newPos, curTile.pos, curTile.g + 1, getH(newPos)));
			}
		}
		
		if (attempts >= 100)
			return Tank.STOP;
		
		if (open.size() > 0)
		{
			curTile = findClosed(end);
			open.clear();
			open.add(curTile);
			while (curTile.parent != -1)
			{
				open.add(findClosed(curTile.parent));
				curTile = findClosed(curTile.parent);
			}
			
			Tile tileBefore = open.get(open.size() - 2);
			
			debug = Integer.toString(IntToX(tileBefore.pos)) + ", " +Integer.toString(IntToY(tileBefore.pos));
			
			if (IntToX(tileBefore.pos) < x1)
				return Tank.LEFT;
			else if (IntToX(tileBefore.pos) > x1)
				return Tank.RIGHT;
			else if (IntToY(tileBefore.pos) < y1)
				return Tank.UP;
			else if (IntToY(tileBefore.pos) > y1)
				return Tank.DOWN;
		}
		
		return Tank.STOP;
	}
	
	private boolean openContains(int pos)
	{
		for (int x = 0; x < open.size(); x++)
		{
			if (open.get(x).pos == pos)
				return true;
		}
		
		return false;
	}
	
	private boolean closedContains(int pos)
	{
		for (int x = 0; x < closed.size(); x++)
		{
			if (closed.get(x).pos == pos)
				return true;
		}
		
		return false;
	}
	
	private Tile findClosed(int pos)
	{
		Tile tile = new Tile(0, 0, 0, 0);
		for (int x = 0; x < closed.size(); x++)
		{
			if (closed.get(x).pos == pos)
				tile = closed.get(x);
		}
		
		return tile;
	}
	
	private Tile openMin()
	{
		int curMin = open.get(0).f;
		Tile curTile = open.get(0);
		
		for(int x = 1; x < open.size(); x++)
		{
			if (curMin > open.get(x).f)
			{
				curMin = open.get(x).f;
				curTile = open.get(x);
			}
		}
		
		return curTile;
	}
	
	private void removeOpen(int pos)
	{
		for (int x = 0; x < open.size(); x++)
		{
			if (open.get(x).pos == pos)
				open.remove(x);
		}
	}
	
	private int getH(int pos)
	{
		int dx = Math.abs(IntToX(end) - IntToX(pos));
		int dy = Math.abs(IntToY(end) - IntToY(pos));
		
		return dx + dy;
	}
	
	private int CoordToInt(int x, int y)
	{
		return y * map.width + x;
	}
	
	private int IntToX(int pos)
	{
		return pos % map.width;
	}
	
	private int IntToY(int pos)
	{
		return pos / map.width;
	}
}
