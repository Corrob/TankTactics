package com.CorGaming.TankTactic;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

import com.CorGaming.framework.Game;
import com.CorGaming.framework.Graphics;
import com.CorGaming.framework.Music;
import com.CorGaming.framework.Screen;
import com.CorGaming.framework.Input.TouchEvent;

public class ShopScreen  extends Screen 
{	
	private final int NUM_ITEMS = 6;
	private GamePlay gamePlay;
	
	private int selected;
	private int selectedCost;
	
	private List<Item> items;
	
	private Button[] shopItems;
	private Button[] tankItems;
	private Button back;
	private Button buy;
	private Button sell;
	
	String message;
	
	private Music song;
	
	public ShopScreen(Game game, String type)
	{
		super(game);
		
		song = Assets.songs.get("Menu");
		
		resume();
		
		selected = 0;
		items = new ArrayList<Item>();
		
		if (type == "USE")
		{
			items.add(Assets.items.getItem("Bridge"));
			items.add(Assets.items.getItem("Repair Kit"));
			items.add(Assets.items.getItem("Super Repair Kit"));
			items.add(Assets.items.getItem("Tree Bullet"));
			items.add(Assets.items.getItem("Speed Boost"));
			items.add(Assets.items.getItem("None"));
		} else if (type == "EQUIP")
		{
			items.add(Assets.items.getItem("Bronze Armor"));
			items.add(Assets.items.getItem("Silver Armor"));
			items.add(Assets.items.getItem("Bronze Turret"));
			items.add(Assets.items.getItem("Silver Turret"));
			items.add(Assets.items.getItem("None"));
			items.add(Assets.items.getItem("None"));
		}
		
		selectedCost = items.get(0).cost;
		
		shopItems = new Button[NUM_ITEMS];
		for (int i = 0; i < NUM_ITEMS; i++)
		{
			int x = i % 3 * 64;
			int y = i / 3 * 64 + 64;
			shopItems[i] = new Button(new Rect(x, y, x + 64, y + 64), new Rect(0, 0, 0, 0));
		}
		
		tankItems = new Button[NUM_ITEMS];
		for (int i = 0; i < NUM_ITEMS; i++)
		{
			int x = i % 3 * 64 + 288;
			int y = i / 3 * 64 + 64;
			tankItems[i] = new Button(new Rect(x, y, x + 64, y + 64), new Rect(0, 0, 0, 0));
		}
		
		back = new Button(new Rect(416, 256, 480, 320), new Rect(0, 0, 0, 0));
		buy = new Button(new Rect(208, 32, 272, 96), new Rect(0, 0, 0, 0));
		sell = new Button(new Rect(208, 128, 272, 192), new Rect(0, 0, 0, 0));
		
		message = "";
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
    			} else if (buy.contains(touchEvents.get(i).x, touchEvents.get(i).y))
    			{
    				//if an item to buy is selected and the player has enough money
    				if (selected < NUM_ITEMS && gamePlay.tank.money >= items.get(selected).cost) 
    				{
    					if (items.get(selected).name.equals("None"))
    					{
    						message = "Nothing to buy";
    					}
    					else
    					{
	    					int slot = gamePlay.tank.getSlot(items.get(selected));
		    				if (slot != -1) //if player has the item already
		    				{
	    						gamePlay.tank.money -= items.get(selected).cost; //make the transaction
	    						gamePlay.tank.numItems[slot]++;
	    						message = "";
		    				} else
		    				{
		    					slot = gamePlay.tank.emptySlot();
		    					if (slot != -1) //if an empty slot exists
		    					{
		    						gamePlay.tank.money -= items.get(selected).cost; //make the transaction
		    						gamePlay.tank.inventory[slot] = items.get(selected);
		    						gamePlay.tank.numItems[slot] = 1;
		    						message = "";
		    					} else
		    					{
		    						message = "All slots full";
		    					}
		    				}
    					}
    				} else
    				{
    					if (selected >= NUM_ITEMS)
    						message = "Can't buy item you already own";
    					else
    						message = "Not enough     money to buy!";
    				}
    			} else if (sell.contains(touchEvents.get(i).x, touchEvents.get(i).y))
    			{
    				if (selected >= NUM_ITEMS)
    				{
    					int slot = selected - NUM_ITEMS + 2;
    					if (gamePlay.tank.numItems[slot] > 0)
    					{
    						gamePlay.tank.money += gamePlay.tank.inventory[slot].cost / 2;
	    					gamePlay.tank.numItems[slot]--;
			    			if (gamePlay.tank.numItems[slot] == 0)
			    				gamePlay.tank.inventory[slot] = Assets.items.getItem("None");
			    			message = "";
    					}
    					else
    					{
    						message = "Nothing to sell";
    					}
    				}
    				else
     				{
     					message = "Can't sell itemyou don't own";
     				}
    			}
    			
    			for (int item = 0; item < NUM_ITEMS; item++)
    			{
    				if (shopItems[item].contains(touchEvents.get(i).x, touchEvents.get(i).y))
    				{
    					selected = item;
    					selectedCost = items.get(selected).cost;
    				}
    				if (tankItems[item].contains(touchEvents.get(i).x, touchEvents.get(i).y))
    				{
    					selected = item + NUM_ITEMS;
    					selectedCost = gamePlay.tank.inventory[item+2].cost / 2;
    				}
    			}
    		}
    		else if (touchEvents.get(i).type == TouchEvent.TOUCH_UP)
    		{
    			
    		}
    	}
	}
	
	@Override
	public void present(float deltaTime) 
	{
		Graphics g = game.getGraphics();
		
		g.drawRect(0,  0, 480, 320, Color.BLACK);
		
		for (int i = 0; i < NUM_ITEMS; i++)
		{
			int x = i % 3 * 64;
			int y = i / 3 * 64 + 64;
			
			if (i == selected)
				g.drawRect(x, y, Item.WIDTH, Item.HEIGHT, Color.YELLOW);
			
			g.drawPixmap(Assets.itemsPic, x, y, (items.get(i).type % 10) * Item.WIDTH, 
					(items.get(i).type / 10) * Item.HEIGHT, Item.WIDTH, Item.WIDTH);
		}
		
		for (int i = 0; i < NUM_ITEMS; i++)
		{
			int x = i % 3 * 64 + 288;
			int y = i / 3 * 64 + 64;
			
			if (i + NUM_ITEMS == selected)
				g.drawRect(x, y, Item.WIDTH, Item.HEIGHT, Color.YELLOW);
			
			g.drawPixmap(Assets.itemsPic, x, y, (gamePlay.tank.inventory[i + 2].type % 10) * Item.WIDTH, 
					(gamePlay.tank.inventory[i + 2].type / 10) * Item.HEIGHT, Item.WIDTH, Item.WIDTH);
			
			if (gamePlay.tank.numItems[i + 2] != 0 && gamePlay.tank.numItems[i + 2] != 1)
				drawNumber(gamePlay.tank.numItems[i + 2], Integer.toString(gamePlay.tank.numItems[i + 2]), 
						x + 18, y + 59, g);
		}
		
		g.drawPixmap(Assets.shopPic, 0, 0);
		
		//draw cost and player's money
		g.drawText("Cost: $" + selectedCost, 10, 230, Color.RED, 16);
		g.drawText("Money: $" + gamePlay.tank.money, 10, 250, Color.RED, 16);
		
		//show message
		if (message.length() < 15)
			g.drawText(message, 10, 270, Color.YELLOW, 15);
		else if (message.length() < 30)
		{
			g.drawText(message.substring(0, 15), 10, 270, Color.YELLOW, 15);
			g.drawText(message.substring(15), 10, 290, Color.YELLOW, 15);
		} else
		{
			g.drawText(message.substring(0, 15), 10, 270, Color.YELLOW, 15);
			g.drawText(message.substring(15, 30), 10, 290, Color.YELLOW, 15);
			g.drawText(message.substring(30), 10, 3100, Color.YELLOW, 15);
		}
		
		//draw item name and description
		String description = "";
		if (selected < NUM_ITEMS)
		{
			g.drawText(items.get(selected).name + ":", 130, 230, Color.RED, 18);
			
			description = items.get(selected).description;
		} else
		{
			g.drawText(gamePlay.tank.inventory[selected - NUM_ITEMS + 2].name + ":", 130, 230, Color.RED, 18);
			
			description = gamePlay.tank.inventory[selected - NUM_ITEMS + 2].description;
		}
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
