package com.CorGaming.TankTactic;

import java.util.List;

import android.graphics.Color;

import com.CorGaming.framework.Game;
import com.CorGaming.framework.Graphics;
import com.CorGaming.framework.Music;
import com.CorGaming.framework.Screen;
import com.CorGaming.framework.Input.TouchEvent;

public class MenuScreen extends Screen
{
	Button contin;
	Button newGame;
	Button options;
	Button bestTimes;
	
	private Music song;

	public MenuScreen(Game game)
	{
		super(game);

		contin = new Button(new Rect(152, 64, 328, 100), new Rect(0, 0, 0, 0));
		newGame = new Button(new Rect(152, 105, 328, 141), new Rect(0, 0, 0, 0));
		options = new Button(new Rect(152, 146, 328, 182), new Rect(0, 0, 0, 0));
		bestTimes = new Button(new Rect(152, 187, 328, 223), new Rect(0, 0, 0, 0));
		
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
				if (contin.contains(touchEvents.get(i).x, touchEvents.get(i).y))
				{
					game.setScreen(new GameScreen(game));
					return;
				} else if (newGame.contains(touchEvents.get(i).x,
						touchEvents.get(i).y))
				{
					game.setScreen(new NewGameScreen(game));
					return;
				} else if (options.contains(touchEvents.get(i).x,
						touchEvents.get(i).y))
				{
					game.setScreen(new OptionsScreen(game));
					return;
				}  else if (bestTimes.contains(touchEvents.get(i).x,
						touchEvents.get(i).y))
				{
					game.setScreen(new BestTimesScreen(game));
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

		g.drawPixmap(Assets.menu, 0, 0);
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

}
