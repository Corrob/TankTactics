package com.CorGaming.TankTactic;

import java.io.Serializable;

public class Tile implements Serializable
{
	private static final long serialVersionUID = -5341277360897766875L;
	public int pos;
	public int parent;
	public int f, g, h;
	
	public Tile(int position, int Parent, int G, int H)
	{
		pos = position;
		parent = Parent;
		f = G + H;
		g = G;
		h = H;
	}
}
