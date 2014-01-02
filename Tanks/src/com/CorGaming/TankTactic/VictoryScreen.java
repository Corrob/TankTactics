package com.CorGaming.TankTactic;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

import android.graphics.Color;

import com.CorGaming.framework.Game;
import com.CorGaming.framework.Graphics;
import com.CorGaming.framework.Input.TouchEvent;
import com.CorGaming.framework.Music;
import com.CorGaming.framework.Screen;

public class VictoryScreen extends Screen
{	
	private Button cont;
	private Button start;
	
	private Music song;
	
	private int stage;
	
	private float newTime;
	
	private float[] bestScores;
	
	private int message;
	
	public VictoryScreen(Game game, float time)
	{
		super(game);
		
		song = Assets.songs.get("Victory");
		
		resume();
		
		newTime = time;
		
		if (newTime < bestScores[0])
		{
			bestScores[2] = bestScores[1];
			bestScores[1] = bestScores[0];
			bestScores[0] = newTime;
			message = 1;
		}
		else if (newTime < bestScores[1])
		{
			bestScores[2] = bestScores[1];
			bestScores[1] = newTime;
			message = 2;
		}
		else if (newTime < bestScores[2])
		{
			bestScores[2] = newTime;
			message = 3;
		}
		else
		{
			message = 0;
		}
		
		saveNewGameplay();
		
		cont = new Button(new Rect(150, 245, 327, 292), new Rect(0, 0, 0, 0));
		start = new Button(new Rect(123, 231, 348, 271), new Rect(0, 0, 0, 0));
		
		if (Assets.settings.sound)
			song.play();
		
		stage = 1;
	}

	@Override
	public void update(float deltaTime)
	{
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		for (int i = 0; i < touchEvents.size(); i++)
		{
			if (touchEvents.get(i).type == TouchEvent.TOUCH_DOWN)
			{
				if (stage <= 2 && cont.contains(touchEvents.get(i).x, touchEvents.get(i).y))
    			{
					stage++;
    			}
				else if (stage == 3 && start.contains(touchEvents.get(i).x, touchEvents.get(i).y))
				{
					saveNewGameplay();
    				game.setScreen(new GameScreen(game));
					return;
    			}
			}
		}
	}

	@Override
	public void present(float deltaTime)
	{
		Graphics g = game.getGraphics();

		g.drawRect(0, 0, 480, 320, Color.BLACK);

		if (stage == 1)
			g.drawPixmap(Assets.victory1, 0, 0);
		else if (stage == 2)
			g.drawPixmap(Assets.victory2, 0, 0);
		else
		{
			g.drawPixmap(Assets.victory3, 0, 0);
			
	    	if (newTime % 60 < 10)
	    		g.drawText("" + (int)newTime / 60 + ":0" + (int)newTime % 60, 289, 46, Color.RED, 24);
	    	else
	    		g.drawText("" + (int)newTime / 60 + ":" + (int)newTime % 60, 289, 46, Color.RED, 24);
	    	
	    	switch (message)
	    	{
	    	case 0:
	    		g.drawText("Sorry, you did not", 75, 100, Color.RED, 24);
	    		g.drawText("obtain a best time.", 75, 150, Color.RED, 24);
	    		g.drawText("Better luck next time!", 75, 200, Color.RED, 24);
	    		break;
	    	case 1:
	    		g.drawText("You have achieved the", 75, 100, Color.RED, 24);
	    		g.drawText("best time!", 75, 150, Color.RED, 24);
	    		g.drawText("Congratulations!", 75, 200, Color.RED, 24);
	    		break;
	    	case 2:
	    		g.drawText("You have achieved the", 75, 100, Color.RED, 24);
	    		g.drawText("second best time!", 75, 150, Color.RED, 24);
	    		g.drawText("Congratulations!", 75, 200, Color.RED, 24);
	    		break;
	    	case 3:
	    		g.drawText("You have achieved the", 75, 100, Color.RED, 24);
	    		g.drawText("third best time!", 75, 150, Color.RED, 24);
	    		g.drawText("Congratulations!", 75, 200, Color.RED, 24);
	    		break;
	    	}
		}
	}

	@Override
	public void pause() 
	{
		song.pause();
		
		try {
			OutputStream file = game.getFileIO().writeFile(Settings.bestSaveLoc);
			ObjectOutputStream buffer = new ObjectOutputStream(file);
			
			buffer.writeObject(bestScores);
			
			buffer.close();
		} catch (Exception e) { 
			
		}
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
		
		try {
			OutputStream file = game.getFileIO().writeFile(Settings.bestSaveLoc);
			ObjectOutputStream buffer = new ObjectOutputStream(file);
			
			buffer.writeObject(bestScores);
			
			buffer.close();
		} catch (Exception e) { 
			
		}
	}
	
	private void saveNewGameplay()
	{
		song.stop();
		GamePlay gamePlay = new GamePlay(game, Assets.settings);
		try {
			OutputStream file = game.getFileIO().writeFile(Settings.saveLoc);
			ObjectOutputStream buffer = new ObjectOutputStream(file);
			
			buffer.writeObject(gamePlay);
			
			buffer.close();
		} catch (Exception e) { 
			
		}
		
		// Save best times as well
		try {
			OutputStream file = game.getFileIO().writeFile(Settings.bestSaveLoc);
			ObjectOutputStream buffer = new ObjectOutputStream(file);
			
			buffer.writeObject(bestScores);
			
			buffer.close();
		} catch (Exception e) { 
			
		}
	}
}
