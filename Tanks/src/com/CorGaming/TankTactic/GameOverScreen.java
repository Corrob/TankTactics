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

public class GameOverScreen extends Screen
{
	private Button newGame;
	
	private Music song;
	
	public GameOverScreen(Game game)
	{
		super(game);
		
		song = Assets.songs.get("GameOver");
		
		newGame = new Button(new Rect(157, 218, 333, 266), new Rect(0, 0, 0, 0));
		
		saveNewGameplay();
		
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
				if (newGame.contains(touchEvents.get(i).x, touchEvents.get(i).y))
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

		g.drawPixmap(Assets.gameOver, 0, 0);
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
