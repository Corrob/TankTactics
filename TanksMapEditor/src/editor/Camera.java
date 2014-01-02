package editor;

import java.awt.Rectangle;
import java.awt.geom.*;

public class Camera 
{
	Rectangle2D rect;
	
	public Camera(int startX, int startY) 
	{
		rect = new Rectangle(startX, startY, 480, 480);
	}
	
	public void setPos(int x, int y)
	{
		int rX = x;
		int rY = y;
		int rW = (int) rect.getWidth();
		int rH = (int) rect.getHeight();
		
		if (rX < 0)
			rX = 0;
		if (rY < 0)
			rY = 0;
		if (rX + rW > 480)
			rX = 480 - rW;
		if (rY + rH > 480)
			rY = 256 - rH;
		rect.setRect(rX, rY, rW, rH);
	}
}
