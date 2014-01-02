package com.CorGaming.TankTactic;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

import android.graphics.Color;

import com.CorGaming.framework.Game;
import com.CorGaming.framework.Graphics;
import com.CorGaming.framework.Music;
import com.CorGaming.framework.Screen;
import com.CorGaming.framework.Input.TouchEvent;

public class BestTimesScreen extends Screen
{
	private float[] bestScores;
	
	private Music song;
	
	private Button back;
	
	public BestTimesScreen(Game game)
	{
		super(game);
		
		song = Assets.songs.get("Menu");
		
		resume();
		
		back = new Button(new Rect(376, 256, 480, 320), new Rect(0, 0, 0, 0));
		
		if (Assets.settings.sound)
			song.play();
	}

	@Override
	public void update(float deltaTime)
	{
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
    	
    	for (int i = 0; i < touchEvents.size(); i++)
    	{
    		if (touchEvents.get(i).type == TouchEvent.TOUCH_DOWN)
    		{
    			if (back.contains(touchEvents.get(i).x, touchEvents.get(i).y))
    			{
    				game.setScreen(new MenuScreen(game));
    				return;
    			}
    		}    		
    	}	
	}

	@Override
	public void present(float deltaTime)
	{
		Graphics g = game.getGraphics();
		
		g.drawRect(0,  0, 480, 320, Color.BLACK);
		
		g.drawPixmap(Assets.bestTimes, 0, 0);
		
		if (bestScores[0] % 60 < 10)
    		g.drawText("" + (int)bestScores[0] / 60 + ":0" + (int)bestScores[0] % 60, 180, 113, Color.RED, 28);
    	else
    		g.drawText("" + (int)bestScores[0] / 60 + ":" + (int)bestScores[0] % 60, 180, 113, Color.RED, 28);
		
		if (bestScores[1] % 60 < 10)
    		g.drawText("" + (int)bestScores[1] / 60 + ":0" + (int)bestScores[1] % 60, 190, 173, Color.RED, 28);
    	else
    		g.drawText("" + (int)bestScores[1] / 60 + ":" + (int)bestScores[1] % 60, 190, 173, Color.RED, 28);
		
		if (bestScores[2] % 60 < 10)
    		g.drawText("" + (int)bestScores[2] / 60 + ":0" + (int)bestScores[2] % 60, 180, 234, Color.RED, 28);
    	else
    		g.drawText("" + (int)bestScores[2] / 60 + ":" + (int)bestScores[2] % 60, 180, 234, Color.RED, 28);
	}

	@Override
	public void pause()
	{
		song.pause();	
	}

	@Override
	public void resume()
	{
		if (Assets.settings.sound)
			song.play();
		
		try {
			InputStream file = game.getFileIO().readFile(Settings.bestSaveLoc);
			ObjectInputStream buffer = new ObjectInputStream(file);
			
			bestScores = (float []) buffer.readObject();
			
			buffer.close();
		} catch (Exception e) {
			bestScores = new float[3];
			bestScores[0] = 900;
			bestScores[1] = 1800;
			bestScores[2] = 3600;
		}
	}

	@Override
	public void dispose()
	{
		song.pause();
	}

}
