package editor;

public class Selector 
{	
	public final int WIDTH = 32,  HEIGHT = 32;
	
	public int x;
	public int y;
	public int selected;
	
	public Selector()
	{
		x = 500;
		y = 0;
		selected = 0;
	}
	
	public void moveTo(int X, int Y, int offSet)
	{
		x = (X - offSet) / WIDTH * WIDTH + offSet;
		y = Y / HEIGHT * HEIGHT;
		selected = (y / HEIGHT * 9) + (x - offSet) / WIDTH;
	}
}
