package com.CorGaming.TankTactic;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

import android.graphics.Color;
import android.util.Log;

import com.CorGaming.framework.Game;
import com.CorGaming.framework.Graphics;
import com.CorGaming.framework.Music;
import com.CorGaming.framework.Screen;
import com.CorGaming.framework.Input.TouchEvent;

public class InventoryScreen extends Screen 
{
	private GamePlay gamePlay;
	private Button back;
	private Button drop;
	
	private Button[] items;
	
	private int selected;
	
	private boolean dragging;
	private int dragX;
	private int dragY;
	
	private Music song;
	
	public InventoryScreen(Game game) 
	{
		super(game);
		
		song = Assets.songs.get("Inventory");
		
		resume();
		
		back = new Button(new Rect(416, 256, 480, 320), new Rect(0, 0, 0, 0));
		drop = new Button(new Rect(416, 192, 480, 256), new Rect(0, 0, 0, 0));
		
		items = new Button[gamePlay.tank.INV_SIZE];
		for (int i = 0; i < gamePlay.tank.INV_SIZE; i++)
		{
			int x = 0;
			int y = 0;
			if (i < 2)
			{
				x = 32;
				y = 64 * (i + 1);
			} else if (i < 8)
			{
				int newI = i - 2;
				x = (newI % 3) * 64 + 144;
				y = (newI / 3) * 64  + 64;
			} else
			{
				int newI = i - 7;
				x = 384;
				y = 64 * newI;
			}
			
			items[i] = new Button(new Rect(x, y, x + Item.WIDTH, y + Item.HEIGHT), new Rect(0, 0, 0, 0));
		}
		
		selected = 0;
		dragging = false;
		dragX = 0;
		dragY = 0;
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
    				game.setScreen(new GameScreen(game));
    				return;
    			}
    			
    			for (int item = 0; item < gamePlay.tank.INV_SIZE; item++)
    			{
    				if (items[item].contains(touchEvents.get(i).x, touchEvents.get(i).y))
    				{
    					selected = item;
    					dragging = true;
    					dragX = items[item].X();
        				dragY = items[item].Y();
    				}
    			}
    		}
    		else if (touchEvents.get(i).type == TouchEvent.TOUCH_DRAGGED)
    		{
    			if (dragging)
    			{
    				if (!items[selected].contains(touchEvents.get(i).x, touchEvents.get(i).y))
    				{
	    				dragX = touchEvents.get(i).x - Item.WIDTH / 2;
	    				dragY = touchEvents.get(i).y - Item.HEIGHT / 2;
    				}
    				else
    				{
    					dragX = items[selected].X();
        				dragY = items[selected].Y();
    				}
    			}
    		}
    		else if (touchEvents.get(i).type == TouchEvent.TOUCH_UP)
    		{
    			for (int item = 0; item < gamePlay.tank.INV_SIZE; item++)
    			{
    				if (items[item].contains(touchEvents.get(i).x, touchEvents.get(i).y))
    				{
    					if (item == 8 || item == 9 || selected == 8 || selected == 9)
    					{
    						if (gamePlay.tank.inventory[item].equipable && gamePlay.tank.inventory[selected].equipable)
    						{
    							//swap items
		    					Item temp = gamePlay.tank.inventory[item];
		    					gamePlay.tank.inventory[item] = gamePlay.tank.inventory[selected];
		    					gamePlay.tank.inventory[selected] = temp;
		    					
		    					//swap item amounts
		    					int tempNum = gamePlay.tank.numItems[item];
		    					gamePlay.tank.numItems[item] = gamePlay.tank.numItems[selected];
		    					gamePlay.tank.numItems[selected] = tempNum;
		    					
		    					selected = item;
		    					
		    					//update player stats
		    					gamePlay.tank.equipAttackSpeed = gamePlay.tank.inventory[8].attackSpeed + 
		    							gamePlay.tank.inventory[9].attackSpeed;
		    					gamePlay.tank.equipDamage = gamePlay.tank.inventory[8].damage + gamePlay.tank.inventory[9].damage;
		    					gamePlay.tank.equipHP = gamePlay.tank.inventory[8].hp + gamePlay.tank.inventory[9].hp;
		    					gamePlay.tank.equipSpeed = gamePlay.tank.inventory[8].speed + gamePlay.tank.inventory[9].speed;
		    					gamePlay.tank.calcStats();
    						}
    					} else
    					{
    						//swap items
    						Item temp = gamePlay.tank.inventory[item];
	    					gamePlay.tank.inventory[item] = gamePlay.tank.inventory[selected];
	    					gamePlay.tank.inventory[selected] = temp;
	    					
	    					//swap item amounts
	    					int tempNum = gamePlay.tank.numItems[item];
	    					gamePlay.tank.numItems[item] = gamePlay.tank.numItems[selected];
	    					gamePlay.tank.numItems[selected] = tempNum;
	    					
	    					selected = item;
    					}
    				}
    			}
    			
    			if (drop.contains(touchEvents.get(i).x, touchEvents.get(i).y))
    			{
    				if (dragging)
    				{
    					gamePlay.tank.inventory[selected] = Assets.items.getItem("None");
    					gamePlay.tank.numItems[selected] = 0;
    				}
    			}
    			
    			dragging = false;
    		}
    	}
	}

	@Override
	public void present(float deltaTime) 
	{
		Graphics g = game.getGraphics();
		
		g.drawRect(0,  0, 480, 320, Color.BLACK);
		
		for (int i = 0; i < gamePlay.tank.INV_SIZE; i++)
		{
			int x = 0;
			int y = 0;
			if (i < 2)
			{
				x = 32;
				y = 64 * (i + 1);
			} else if (i < 8)
			{
				int newI = i - 2;
				x = (newI % 3) * 64 + 144;
				y = (newI / 3) * 64  + 64;
			} else
			{
				int newI = i - 7;
				x = 384;
				y = 64 * newI;
			}
			
			if (i == selected)
			{
				g.drawRect(x, y, Item.WIDTH, Item.HEIGHT, Color.YELLOW);
				if (dragging)
					g.drawPixmap(Assets.itemsPic, dragX, dragY, (gamePlay.tank.inventory[i].type % 10) * Item.WIDTH, 
							(gamePlay.tank.inventory[i].type / 10) * Item.HEIGHT, Item.WIDTH, Item.WIDTH);
				else
					g.drawPixmap(Assets.itemsPic, x, y, (gamePlay.tank.inventory[i].type % 10) * Item.WIDTH, 
							(gamePlay.tank.inventory[i].type / 10) * Item.HEIGHT, Item.WIDTH, Item.WIDTH);
			} else
			{
				g.drawPixmap(Assets.itemsPic, x, y, (gamePlay.tank.inventory[i].type % 10) * Item.WIDTH, 
							(gamePlay.tank.inventory[i].type / 10) * Item.HEIGHT, Item.WIDTH, Item.WIDTH);
			}
			
			if (gamePlay.tank.numItems[i] != 0 && gamePlay.tank.numItems[i] != 1)
				drawNumber(gamePlay.tank.numItems[i], Integer.toString(gamePlay.tank.numItems[i]), 
						x + 18, y + 59, g);
		}
		
		g.drawPixmap(Assets.inventoryPic, 0, 0);
		
		//draw player stats
		g.drawText("Money: $" + gamePlay.tank.money, 10, 230, Color.RED, 16);
		g.drawText("MaxHP: " + gamePlay.tank.maxHP, 10, 250, Color.RED, 16);
		g.drawText("Speed: " + gamePlay.tank.speed, 10, 270, Color.RED, 16);
		g.drawText("Damage: " + gamePlay.tank.getAttackDamage(), 10, 290, Color.RED, 16);
		g.drawText("Att Speed: " + gamePlay.tank.attackSpeed, 10, 310, Color.RED, 16);
		
		//draw item name and description
		g.drawText(gamePlay.tank.inventory[selected].name + ":", 130, 230, Color.RED, 18);
		
		String description = gamePlay.tank.inventory[selected].description;
		if (description.length() < 27)
			g.drawText(description, 130, 255, Color.RED, 18);
		else if (description.length() < 54)
		{
			g.drawText(description.substring(0, 27), 130, 255, Color.RED, 18);
			g.drawText(description.substring(27), 130, 280, Color.RED, 18);
		} else
		{
			g.drawText(description.substring(0, 27), 130, 255, Color.RED, 18);
			g.drawText(description.substring(27, 54), 130, 280, Color.RED, 18);
			g.drawText(description.substring(54), 130, 305, Color.RED, 18);
		}
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
		try {
			InputStream file = game.getFileIO().readFile(Settings.saveLoc);
			ObjectInputStream buffer = new ObjectInputStream(file);
			
			gamePlay = (GamePlay) buffer.readObject();
			
			buffer.close();
			
		} catch (Exception e) {
			gamePlay = new GamePlay(game);
			Log.d("error", "not loading");
		}
		if (gamePlay.settings.sound)
			song.play();
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
	
	private void drawNumber(int num, String numText, int x, int y, Graphics g)
    {
    	if (num < 10)
    		g.drawText(numText, x + 12, 
    			y - 2, Color.RED, 12);
    	else
    		g.drawText(numText, x + 8, 
        		y - 2, Color.RED, 12);
    }
}
