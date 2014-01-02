package com.CorGaming.TankTactic;

import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import android.os.Environment;
import android.util.Log;

import com.CorGaming.framework.Game;
import com.CorGaming.framework.Music;
import com.CorGaming.framework.Screen;

public class GameScreen extends Screen 
{
	public static Music curSong;
	
	private GamePlay gamePlay;
	
    public GameScreen(Game game) 
    {
        super(game);
        
        resume();
    }

	@Override
    public void update(float deltaTime) 
    {
    	gamePlay.update(deltaTime, game);
    }

    @Override
    public void present(float deltaTime) 
    {    	
    	gamePlay.present(deltaTime, game);
    }

    @Override
    public void pause() 
    {
    	curSong.pause();
    	try {
    		File dir = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + "Tanktactics");
    		dir.mkdir();
    		
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
    	try {
			InputStream file = game.getFileIO().readFile(Settings.saveLoc);
			ObjectInputStream buffer = new ObjectInputStream(file);
			
			gamePlay = (GamePlay) buffer.readObject();
			
			buffer.close();
			
			gamePlay.touchX = -1;
			gamePlay.touchY = -1;
			
			curSong = Assets.songs.get(gamePlay.map.name);
			
		} catch (Exception e) {
			gamePlay = new GamePlay(game, "2-2", 384, 384);
			Log.d("error", "not loading");
		}
    	if (gamePlay.settings.sound)
		{
    		curSong.play();
		}
    	Assets.settings = gamePlay.settings;
    }

    @Override
    public void dispose() 
    {
    	curSong.pause();
    	try {
			OutputStream file = game.getFileIO().writeFile(Settings.saveLoc);
			ObjectOutputStream buffer = new ObjectOutputStream(file);
			
			buffer.writeObject(gamePlay);
			
			buffer.close();
		} catch (Exception e) { 
			
		}
    }
}
