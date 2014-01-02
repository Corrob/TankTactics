package com.CorGaming.TankTactic;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

import android.graphics.Color;

import com.CorGaming.framework.Game;
import com.CorGaming.framework.Graphics;
import com.CorGaming.framework.Music;
import com.CorGaming.framework.Screen;
import com.CorGaming.framework.Input.TouchEvent;

public class NewGameScreen extends Screen
{
	Button yes;
	Button no;
	
	private Music song;
	
	public NewGameScreen(Game game) 
	{
		super(game);
		
		yes = new Button(new Rect(150, 136, 327, 175), new Rect(0, 0, 0, 0));
		no = new Button(new Rect(151, 193, 327, 241), new Rect(0, 0, 0, 0));
		
		song = Assets.songs.get("Menu");
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
    			if (yes.contains(touchEvents.get(i).x, touchEvents.get(i).y))
    			{
    				saveNewGameplay();
    				game.setScreen(new GameScreen(game));
					return;
    			}
    			else if (no.contains(touchEvents.get(i).x, touchEvents.get(i).y))
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
		
		g.drawPixmap(Assets.newgame, 0, 0);
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
	}

	@Override
	public void dispose() 
	{
		song.pause();
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
	}
}
