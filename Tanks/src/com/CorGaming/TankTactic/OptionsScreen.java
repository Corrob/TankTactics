package com.CorGaming.TankTactic;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

import android.graphics.Color;

import com.CorGaming.framework.Game;
import com.CorGaming.framework.Graphics;
import com.CorGaming.framework.Music;
import com.CorGaming.framework.Screen;
import com.CorGaming.framework.Input.TouchEvent;

public class OptionsScreen extends Screen
{
	Button dpad;
	Button touch;
	
	Button soundOn;
	Button soundOff;
	
	Button back;
	
	GamePlay gamePlay;
	
	private Music song;
	
	public OptionsScreen(Game game) 
	{
		super(game);
		
		dpad = new Button(new Rect(102, 89, 312, 128), new Rect(0, 0, 0, 0));
		touch = new Button(new Rect(102, 132, 312, 171), new Rect(0, 0, 0, 0));
		
		soundOn = new Button(new Rect(103, 204, 314, 243), new Rect(0, 0, 0, 0));
		soundOff = new Button(new Rect(104, 250, 314, 289), new Rect(0, 0, 0, 0));
		
		back = new Button(new Rect(376, 256, 480, 320), new Rect(0, 0, 0, 0));
		
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
    			if (dpad.contains(touchEvents.get(i).x, touchEvents.get(i).y))
    			{
    				gamePlay.settings.inputMethod = "DPad";
    			}
    			else if (touch.contains(touchEvents.get(i).x, touchEvents.get(i).y))
    			{
    				gamePlay.settings.inputMethod = "TouchScreen";
    			}
    			else if (soundOn.contains(touchEvents.get(i).x, touchEvents.get(i).y))
    			{
    				gamePlay.settings.sound = true;
    				Assets.settings.sound = true;
    				song.play();
    			}
    			else if (soundOff.contains(touchEvents.get(i).x, touchEvents.get(i).y))
    			{
    				gamePlay.settings.sound = false;
    				Assets.settings.sound = false;
    				song.pause();
    			}
    			else if (back.contains(touchEvents.get(i).x, touchEvents.get(i).y))
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
		
		g.drawPixmap(Assets.options, 0, 0);
		
		if (gamePlay.settings.inputMethod.equals("DPad"))
			g.drawPixmap(Assets.check, 135, 97);
		else
			g.drawPixmap(Assets.check, 135, 140);
		
		if (gamePlay.settings.sound)
			g.drawPixmap(Assets.check, 135, 215);
		else
			g.drawPixmap(Assets.check, 135, 262);
	}

	@Override
	public void pause() 
	{
		song.pause();
		try {
			OutputStream file = game.getFileIO().writeFile(Settings.saveLoc);
			ObjectOutputStream buffer = new ObjectOutputStream(file);
			
			buffer.writeObject(gamePlay);
			
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
			InputStream file = game.getFileIO().readFile(Settings.saveLoc);
			ObjectInputStream buffer = new ObjectInputStream(file);
			
			gamePlay = (GamePlay) buffer.readObject();
			
			buffer.close();
			
		} catch (Exception e) {
			gamePlay = new GamePlay(game);
		}
	}

	@Override
	public void dispose() 
	{
		song.pause();
		try {
			OutputStream file = game.getFileIO().writeFile(Settings.saveLoc);
			ObjectOutputStream buffer = new ObjectOutputStream(file);
			
			buffer.writeObject(gamePlay);
			
			buffer.close();
		} catch (Exception e) { 
			
		}
	}
}
