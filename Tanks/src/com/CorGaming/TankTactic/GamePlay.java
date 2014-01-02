package com.CorGaming.TankTactic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

import com.CorGaming.framework.Game;
import com.CorGaming.framework.Graphics;
import com.CorGaming.framework.Input.TouchEvent;

public class GamePlay implements Serializable
{
	private static final long serialVersionUID = 1820004733188973269L;
	
	PlayerTank tank;
	Button dPad;
	Button shootButton;
	Button itemsButton;
	Button menuButton;
	Button item1Button;
	Button item2Button;
	
	Camera cam;
	Map map;
	List<Bullet> bullets;
	List<Bullet> treeBullets;
	List<Explosion> explosions;
	
	String message;
	
	private double timer;
	private boolean shopped;
	private boolean messageToShow;
	
	String debug;
	
	Settings settings;
	
	float time;
	
	int touchX;
	int touchY;
	
	public GamePlay(Game game)
	{
		dPad = new Button(new Rect(0, 256, 64, 320), new Rect(64, 0, 128, 64));
        shootButton = new Button(new Rect(416, 256, 480, 320), new Rect(0, 0, 64, 64));
        itemsButton = new Button(new Rect(240, 256, 288, 320), new Rect(128, 0, 176, 64));
        menuButton = new Button(new Rect(192, 256, 240, 320), new Rect(176, 0, 224, 64));
        item1Button = new Button(new Rect(288, 256, 352, 320), new Rect(224, 0, 288, 64));
        item2Button = new Button(new Rect(352, 256, 416, 320), new Rect(224, 0, 288, 64));
        
        map = new Map(30, 30, "");
        map.load("2-2", game.getFileIO(), tank);
        cam = new Camera(map.width * map.TILE_WIDTH, map.height * map.TILE_HEIGHT);
        tank = new PlayerTank(Tank.UP, 12*map.TILE_WIDTH,//map.width * map.TILE_WIDTH / 2 - map.TILE_WIDTH / 2, 
        		12*map.TILE_HEIGHT, 1);//map.height * map.TILE_HEIGHT / 2 - map.TILE_HEIGHT / 2, 1);
        
        bullets = new ArrayList<Bullet>();
        treeBullets = new ArrayList<Bullet>();
        explosions = new ArrayList<Explosion>();
        
        timer = 0;
        shopped = false;
        
        messageToShow = false;
        message = "";
        debug = "";
        
        settings = new Settings();
        
        GameScreen.curSong = Assets.songs.get(map.name);
        
        time = 0;
        
        touchX = -1;
        touchY = -1;
	}
	
	public GamePlay(Game game, String mapName, int startX, int startY)
	{
		this(game);
		map.load(mapName, game.getFileIO(), tank);
		cam = new Camera(map.width * map.TILE_WIDTH, map.height * map.TILE_HEIGHT);
        tank = new PlayerTank(Tank.UP, startX, startY, 1);
        GameScreen.curSong = Assets.songs.get(map.name);
	}
	
	public GamePlay(Game game, Settings sets)
	{
		this(game);
		settings = sets;
	}
	
	public void update(float deltaTime, Game game) 
    {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		
		if (messageToShow)
			checkMessage(touchEvents);
		updateGame(deltaTime, game, touchEvents);
		
		time += deltaTime;
    }
	
	private void checkMessage(List<TouchEvent> touchEvents)
	{
		for (int i = 0; i < touchEvents.size(); i++)
    	{
			if (settings.inputMethod.equals("DPad"))
			{
				if (touchEvents.get(i).type == TouchEvent.TOUCH_DOWN || touchEvents.get(i).type == TouchEvent.TOUCH_DRAGGED)
					messageToShow = false;
			}
			else
				messageToShow = false;
    	}
	}
	
	private void updateGame(float deltaTime, Game game, List<TouchEvent> touchEvents)
	{
		boolean canMove = false;
		int dx = 999;
		int dy = 0;
    	for (int i = 0; i < touchEvents.size(); i++)
    	{
    		
    		if (touchEvents.get(i).type == TouchEvent.TOUCH_DOWN ||
    			touchEvents.get(i).type == TouchEvent.TOUCH_DRAGGED)
    		{
    			if (settings.inputMethod.equals("DPad"))
    			{
    				if (dPad.contains(touchEvents.get(i).x, touchEvents.get(i).y))
    				{
	    				dx = touchEvents.get(i).x - dPad.centerX();
	    				dy = touchEvents.get(i).y - dPad.centerY();
	    				touchX = 0;
	    				touchY = 0;
	    				canMove = true;
    				}
    			}
	    			
    			else if (settings.inputMethod.equals("TouchScreen"))
    			{
	    			if (touchEvents.get(i).y < cam.rect.height())
	    			{
	    				if (touchEvents.get(i).type == TouchEvent.TOUCH_DOWN)
	    				{
	    					touchX = touchEvents.get(i).x;
	    					touchY = touchEvents.get(i).y;
	    				}
	    				/*	
	    				dx = (int) touchEvents.get(i).x - (tank.centerX() - cam.rect.left);
	    				dy = (int) touchEvents.get(i).y - (tank.centerY() - cam.rect.top);
	    				
	    				//weight the y direction more because of wide screen
	    				dx *= 2;
	    				dy *= 3;*/
	    			}
	    			else
	    			{
	    				touchX = -1;
	    			}
    			}
    			
    			//check button presses
	    		if (shootButton.contains(touchEvents.get(i).x, touchEvents.get(i).y))
	    		{
	    			if (tank.notShooting())
	    			{
		    			tank.shoot();
		    			bullets.add(new Bullet((int) tank.x + 12, (int) tank.y + 12, tank.direction, tank));
	    			}
	    		}
	    		
	    		if (touchEvents.get(i).type == TouchEvent.TOUCH_DOWN)
	    		{	    			
		    		if (itemsButton.contains(touchEvents.get(i).x, touchEvents.get(i).y))
		    		{
		    			game.setScreen(new InventoryScreen(game));
		    		}
		    		
		    		else if (item1Button.contains(touchEvents.get(i).x, touchEvents.get(i).y))
		    		{
		    			if (tank.inventory[0].type != Item.NONE)
		    			{
		    				boolean used = useItem(tank.inventory[0]);
		    				if (used)
		    				{
				    			tank.numItems[0]--;
				    			if (tank.numItems[0] == 0)
				    				tank.inventory[0] = Assets.items.getItem("None");
		    				}
		    			}
		    		}
		    		
		    		else if (item2Button.contains(touchEvents.get(i).x, touchEvents.get(i).y))
		    		{
		    			if (tank.inventory[1].type != Item.NONE)
		    			{
		    				boolean used = useItem(tank.inventory[1]);
		    				if (used)
		    				{
				    			tank.numItems[1]--;
				    			if (tank.numItems[1] == 0)
				    				tank.inventory[1] = Assets.items.getItem("None");
		    				}
		    			}
		    		}
		    		
		    		else if (menuButton.contains(touchEvents.get(i).x, touchEvents.get(i).y))
		    		{
		    			game.setScreen(new MenuScreen(game));
		    			return;
		    		}
	    		}
    		}
    		
    		else if (touchEvents.get(i).type == TouchEvent.TOUCH_UP && touchX != -1)
    		{
    			if (settings.inputMethod.equals("TouchScreen"))// && touchEvents.get(i).y < cam.rect.height())
    			{
					dx = touchEvents.get(i).x - touchX;
					dy = touchEvents.get(i).y - touchY;
					canMove = true;
					if (Math.abs(dx) < 20 && Math.abs(dy) < 20)
					{
						canMove = false;
						if (tank.notShooting())
		    			{
			    			tank.shoot();
			    			bullets.add(new Bullet((int) tank.x + 12, (int) tank.y + 12, tank.direction, tank));
		    			}
						else
							tank.move(Tank.STOP);
					}
    			}
    			else
    				tank.move(Tank.STOP);
    		}
    		
    		if (canMove)
			{
    			//determine the direction to go
				if (dx > 0)
				{
					if (dy > 0)
					{
						if (dy > dx)
							tank.move(Tank.DOWN);
						else
							tank.move(Tank.RIGHT);
					}
					else
					{
						if (-dy > dx)
							tank.move(Tank.UP);
						else
							tank.move(Tank.RIGHT);
					}
				}
				else
				{
					if (dy > 0)
					{
						if (dy > -dx)
							tank.move(Tank.DOWN);
						else
							tank.move(Tank.LEFT);
					}
					else
					{
						if (-dy > -dx)
							tank.move(Tank.UP);
						else
							tank.move(Tank.LEFT);
					}
				}
			}
    	}
    	
    	tank.update(deltaTime, checkMapCollision(tank));
    	updateMap(game);
        
        //update enemies
        for (int enemy = 0; enemy < map.enemies.size(); enemy++)
        {
        	String shot = map.enemies.get(enemy).update(deltaTime, checkMapCollision(map.enemies.get(enemy)), 
        			tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT), map.ai);
        	if (shot.equals("SHOT"))
        	{
        		bullets.add(new Bullet((int) map.enemies.get(enemy).x + 12, (int) map.enemies.get(enemy).y + 12, 
        				map.enemies.get(enemy).direction, map.enemies.get(enemy)));
        	}
        }
        
        //update the boss if it is there
        if (map.boss != null)
        {
        	String shot = map.boss.update(deltaTime, checkMapCollision(map.boss), tank.getTileX(map.TILE_WIDTH), 
        			tank.getTileY(map.TILE_HEIGHT), map.ai);
        	if (shot.equals("SHOT"))
        	{
        		bullets.add(new Bullet((int) map.boss.x + 20, (int) map.boss.y + 20, map.boss.direction, map.boss));
        	}
        	
        	if (checkObjectCollision((int) map.boss.x, (int) map.boss.y, map.boss.width, map.boss.width,
        			(int) tank.x, (int) tank.y, 32, 32))
        	{
        		map.boss.unMove(deltaTime);
        		tank.unMove(deltaTime);
        	}
        }
        
        //update npcs
        for (int npc = 0; npc < map.npcs.size(); npc++)
		{
        	map.npcs.get(npc).update(deltaTime, checkMapCollision(map.npcs.get(npc)));
        	
			if (checkObjectCollision((int) map.npcs.get(npc).x, (int) map.npcs.get(npc).y, 32, 32, 
					(int) tank.x, (int) tank.y, 32, 32))
    		{
				map.npcs.get(npc).unMove(deltaTime);
				tank.unMove(deltaTime);
				messageToShow = true;
				message = map.npcs.get(npc).statement;
    		}
			
			if (map.boss != null)
			{
				if (checkObjectCollision((int) map.npcs.get(npc).x, (int) map.npcs.get(npc).y, 32, 32,
						(int) map.boss.x, (int) map.boss.y, map.boss.width, map.boss.width))
				{
					map.npcs.get(npc).unMove(deltaTime);
					map.boss.unMove(deltaTime);
				}
			}
			
			for (int bullet = 0; bullet < bullets.size(); bullet++)
            {
    			if (checkObjectCollision((int) map.npcs.get(npc).x, (int) map.npcs.get(npc).y, 32, 32, 
    					(int) bullets.get(bullet).x, (int) bullets.get(bullet).y, bullets.get(bullet).WIDTH, 
    					bullets.get(bullet).HEIGHT))
        		{
        			bullets.remove(bullet);
        		}
            }
		}
        
        //update explosions
        for (int explosion = 0; explosion < explosions.size(); explosion++)
        {
        	if (!explosions.get(explosion).update(deltaTime)) //if explosion done
        		explosions.remove(explosion);
        }
             
        //update bullets
        for (int bullet = 0; bullet < bullets.size(); bullet++)
        {
        	bullets.get(bullet).update(deltaTime);
        	
        	if (bullets.get(bullet).outOfBounds(map.width * map.TILE_WIDTH, map.height * map.TILE_HEIGHT))
        		bullets.remove(bullet);
        	
        	//Check bullet collision with player tank
        	else if (checkObjectCollision((int) tank.x, (int) tank.y, 32, 32, bullets.get(bullet).rect.left, 
        			bullets.get(bullet).rect.top, 8, 8))
    		{
    			if (!bullets.get(bullet).sentFrom.equals(tank)) //if bullet came from other source
    			{
    				tank.hp -= bullets.get(bullet).sentFrom.getAttackDamage();
    				explosions.add(new Explosion((int) tank.x, (int) tank.y, Explosion.SMALL));
    				bullets.remove(bullet);
    			}
    		}
        	
        	//Check bullet collision with boss
        	else if (map.boss != null)
        	{
        		if (checkObjectCollision((int) map.boss.x, (int) map.boss.y, map.boss.width, map.boss.width, 
        				bullets.get(bullet).rect.left, bullets.get(bullet).rect.top, 8, 8))
        		{
        			if (!bullets.get(bullet).sentFrom.equals(map.boss))
        			{
        				map.boss.hp -= bullets.get(bullet).sentFrom.getAttackDamage();
        				explosions.add(new Explosion((int) bullets.get(bullet).x, (int) bullets.get(bullet).y, Explosion.SMALL));
        				bullets.remove(bullet);
        			}
        		}
        	}
        	
        	//Check bullet collision with tree
        	else
        	{
        		int x = bullets.get(bullet).getTileX(map.TILE_WIDTH);
            	int y = bullets.get(bullet).getTileY(map.TILE_HEIGHT);
            	
            	/*if (x >= map.width)
            		x = map.width - 1;
            	if (y >= map.height)
            		y = map.height - 1;*/
            	
            	if (map.blockBullet(x, y))
            	{
	        		bullets.remove(bullet);
            	}
        	}
        }
        
        //update tree bullets
        for (int bullet = 0; bullet < treeBullets.size(); bullet++)
        {
        	treeBullets.get(bullet).update(deltaTime);
        	
        	if (treeBullets.get(bullet).outOfBounds(map.width * map.TILE_WIDTH, map.height * map.TILE_HEIGHT))
        		treeBullets.remove(bullet);
        	
        	//Check tree bullet collision with tree
        	else
        	{
        		int x = treeBullets.get(bullet).getTileX(map.TILE_WIDTH);
            	int y = treeBullets.get(bullet).getTileY(map.TILE_HEIGHT);
            	
            	if (x >= map.width)
            		x = map.width - 1;
            	if (y >= map.height)
            		y = map.height - 1;
            	
            	if (map.isTree(x, y))
            	{
	        		treeBullets.remove(bullet);
	        		map.setTile(0, x, y);
            	}
        	}
        }        
        
        //Check map items collision with player
        for (int item = 0; item < map.items.size(); item++)
        {
        	if (checkObjectCollision((int) tank.x, (int) tank.y, 32, 32, map.items.get(item).x, 
        			map.items.get(item).y, 32, 32))
        	{
        		int slot = tank.getSlot(Assets.items.getItem(map.items.get(item).itemName));
				if (slot != -1) //if player has the item already
				{
					tank.numItems[slot]++;
					
					tank.itemsFound.add(map.name + map.items.get(item).x + map.items.get(item).y);
					
					map.items.remove(item);
				} else
				{
					slot = tank.emptySlot();
					if (slot != -1) //if an empty slot exists
					{
						tank.inventory[slot] = Assets.items.getItem(map.items.get(item).itemName);
						tank.numItems[slot] = 1;
						
						tank.itemsFound.add(map.name + map.items.get(item).x + map.items.get(item).y);
						
						map.items.remove(item);
					}
					else
					{
						tank.unMove(deltaTime);
					}
				}
        	}
        }
        
        //Check collisions with enemies
        for (int enemy = 0; enemy < map.enemies.size(); enemy++)
    	{
    		if (checkObjectCollision((int) tank.x, (int) tank.y, 32, 32, (int) map.enemies.get(enemy).x, 
    				(int) map.enemies.get(enemy).y, 32, 32))
    		{
    			tank.unMove(deltaTime);
    			map.enemies.get(enemy).unMove(deltaTime);
    			tank.hp--;
    			map.enemies.get(enemy).hp--;
    		}
    		else if (map.boss != null)
        	{
	    		if (checkObjectCollision((int) map.boss.x, (int) map.boss.y, map.width, map.width, 
	    				(int) map.enemies.get(enemy).x, (int) map.enemies.get(enemy).y, 32, 32))
	    		{
	    			map.boss.unMove(deltaTime);
	    			map.enemies.get(enemy).unMove(deltaTime);
	    		}
        	}
    		
    		for (int npc = 0; npc < map.npcs.size(); npc++)
    		{
    			if (checkObjectCollision((int) map.npcs.get(npc).x, (int) map.npcs.get(npc).y, 32, 32, 
						(int) map.enemies.get(enemy).x, (int) map.enemies.get(enemy).y, 32, 32))
	    		{
					map.npcs.get(npc).unMove(deltaTime);
					map.enemies.get(enemy).unMove(deltaTime);
	    		}
    		}
    		
    		for (int otherEnem = 0; otherEnem < map.enemies.size(); otherEnem++)
        	{
    			if (otherEnem != enemy)
    			{
    				if (checkObjectCollision((int) map.enemies.get(otherEnem).x, (int) map.enemies.get(otherEnem).y, 32, 32, 
    						(int) map.enemies.get(enemy).x, (int) map.enemies.get(enemy).y, 32, 32))
    	    		{
    					map.enemies.get(otherEnem).unMove(deltaTime);
    					map.enemies.get(enemy).unMove(deltaTime);
    	    		}
    			}
        	}
    		
    		for (int bullet = 0; bullet < bullets.size(); bullet++)
            {
    			if (checkObjectCollision((int) map.enemies.get(enemy).x, (int) map.enemies.get(enemy).y, 32, 32, 
    					(int) bullets.get(bullet).x, (int) bullets.get(bullet).y, bullets.get(bullet).WIDTH, 
    					bullets.get(bullet).HEIGHT))
        		{
        			if (!bullets.get(bullet).sentFrom.equals(map.enemies.get(enemy))) //if bullet came from other source
        			{
        				map.enemies.get(enemy).hp -= bullets.get(bullet).sentFrom.getAttackDamage();
        				map.enemies.get(enemy).hit = true;
        				explosions.add(new Explosion((int) map.enemies.get(enemy).x, (int) map.enemies.get(enemy).y, Explosion.SMALL));
        				bullets.remove(bullet);
        			}
        		}
            }
    		
    		if (map.enemies.get(enemy).hp < 0) //check for dead
    		{
    			tank.addExp(map.enemies.get(enemy).expGiven());
    			tank.money += map.enemies.get(enemy).moneyGiven();
    			explosions.add(new Explosion((int) map.enemies.get(enemy).x, (int) map.enemies.get(enemy).y, Explosion.BIG));
    			map.enemies.remove(enemy);
    		}
    	}
        
        if (tank.hp <= 0) //check tank for dead
        {
        	game.setScreen(new GameOverScreen(game));
        	return;
        }
        	
        
        if (map.boss != null)
        {
	        if (map.boss.hp < 0)
	        {
	        	tank.addExp(map.boss.expGiven());
				tank.money += map.boss.moneyGiven();
	        	explosions.add(new Explosion((int) map.boss.x, (int) map.boss.y, Explosion.BIG));
	        	map.boss = null;
	        	GameScreen.curSong.stop();
	        	game.setScreen(new VictoryScreen(game, time));
	        }
        }
        
        //update buffs
        timer += deltaTime;
        if (timer > tank.SPEED_BUFF_TIME && tank.speedBuffed())
        {
        	tank.unBuffSpeed();
        }
        
        //check for shop collision
        String shop = map.isShop(tank.getTileAheadX(map.TILE_WIDTH), tank.getTileAheadY(map.TILE_HEIGHT));
        if (shop != "NONE")
        {
        	if (!shopped)
        	{
	        	game.setScreen(new ShopScreen(game, shop));
	        	shopped = true;
	        	tank.move(Tank.STOP);
        	}
        } else
        {
        	shopped = false;
        }
        
        //Update the camera
        cam.setPos((int) tank.x - cam.rect.width() / 2, (int) tank.y - cam.rect.height() / 2);
	}
	
	private boolean checkMapCollision(Tank curTank)
    {
    	if (map.collidableTile(curTank.getTileAheadX(map.TILE_WIDTH), curTank.getTileAheadY(map.TILE_HEIGHT)))
    		return true;
    	return false;
    }
    
    private boolean checkObjectCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2)
    {
    	Rect object1 = new Rect(x1, y1, x1 + w1, y1 + h1);
    	Rect object2 = new Rect(x2, y2, x2 + w2, y2 + h2);
    	
    	if (Rect.intersects(object1, object2))
    			return true;
    	return false;
    }
    
    private void updateMap(Game game)
    {
    	String moveMap = map.outOfMap(tank.getTileAheadX(map.TILE_WIDTH), tank.getTileAheadY(map.TILE_HEIGHT));
    	
    	if (moveMap == "UP")
    	{
    		if (!map.up.equals("NULL"))
    		{
    			bullets.clear();
    			explosions.clear();
    			treeBullets.clear();
	    		map.load(map.up, game.getFileIO(), tank);
	    		tank.y = (map.height - 1) * map.TILE_HEIGHT - 1;
	    		cam = new Camera(map.width * map.TILE_WIDTH, map.height * map.TILE_HEIGHT);
	    		
	    		if (map.isTree(tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT)))
	    		{
	    			map.setTile(0, tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT));
	    		} else if (map.isWater(tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT)))
	    		{
	    			map.setTile(2, tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT));
	    		}
	    		
	    		if (settings.sound && GameScreen.curSong != Assets.songs.get(map.name))
	    		{
	    			GameScreen.curSong.stop();
		    		GameScreen.curSong = Assets.songs.get(map.name);
		    		GameScreen.curSong.play();
	    		}
    		}
    	} else if (moveMap == "LEFT")
    	{
    		if (!map.left.equals("NULL"))
    		{
    			bullets.clear();
    			explosions.clear();
    			treeBullets.clear();
	    		map.load(map.left, game.getFileIO(), tank);
	    		tank.x = (map.width - 1) * map.TILE_WIDTH - 1;
	    		cam = new Camera(map.width * map.TILE_WIDTH, map.height * map.TILE_HEIGHT);
	    		
	    		if (map.isTree(tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT)))
	    		{
	    			map.setTile(0, tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT));
	    		} else if (map.isWater(tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT)))
	    		{
	    			map.setTile(2, tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT));
	    		}
	    		
	    		if (settings.sound && GameScreen.curSong != Assets.songs.get(map.name))
	    		{
	    			GameScreen.curSong.stop();
		    		GameScreen.curSong = Assets.songs.get(map.name);
		    		GameScreen.curSong.play();
	    		}
    		}
    	} else if (moveMap == "RIGHT")
    	{
    		if (!map.right.equals("NULL"))
    		{
    			bullets.clear();
    			explosions.clear();
    			treeBullets.clear();
	    		map.load(map.right, game.getFileIO(), tank);
	    		tank.x = 1;
	    		cam = new Camera(map.width * map.TILE_WIDTH, map.height * map.TILE_HEIGHT);
	    		
	    		if (map.isTree(tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT)))
	    		{
	    			map.setTile(0, tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT));
	    		} else if (map.isWater(tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT)))
	    		{
	    			map.setTile(2, tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT));
	    		}
	    		
	    		if (settings.sound && GameScreen.curSong != Assets.songs.get(map.name))
	    		{
	    			GameScreen.curSong.stop();
		    		GameScreen.curSong = Assets.songs.get(map.name);
		    		GameScreen.curSong.play();
	    		}
    		}
    	} else if (moveMap == "DOWN")
    	{
    		if (!map.down.equals("NULL"))
    		{
    			bullets.clear();
    			explosions.clear();
    			treeBullets.clear();
	    		map.load(map.down, game.getFileIO(), tank);
	    		tank.y = 1;
	    		cam = new Camera(map.width * map.TILE_WIDTH, map.height * map.TILE_HEIGHT);
	    		
	    		if (map.isTree(tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT)))
	    		{
	    			map.setTile(0, tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT));
	    		} else if (map.isWater(tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT)))
	    		{
	    			map.setTile(2, tank.getTileX(map.TILE_WIDTH), tank.getTileY(map.TILE_HEIGHT));
	    		}
	    		
	    		if (settings.sound && GameScreen.curSong != Assets.songs.get(map.name))
	    		{
	    			GameScreen.curSong.stop();
		    		GameScreen.curSong = Assets.songs.get(map.name);
		    		GameScreen.curSong.play();
	    		}
    		}
    	}
    }
    
    public void present(float deltaTime, Game game) 
    {
    	Graphics g = game.getGraphics();
    	
    	drawMap(g);
    	
    	//Draw Enemies
    	for (int enemy = 0; enemy < map.enemies.size(); enemy++)
        {
        	g.drawPixmap(Assets.blueTank, (int) map.enemies.get(enemy).x - cam.rect.left, 
        			(int) map.enemies.get(enemy).y - cam.rect.top, map.enemies.get(enemy).source.left, 
        			map.enemies.get(enemy).source.top, map.enemies.get(enemy).source.width(), 
        			map.enemies.get(enemy).source.height());
        	
        	//Draw health
        	g.drawRect((int) map.enemies.get(enemy).x - cam.rect.left + 8, (int) map.enemies.get(enemy).y  - cam.rect.top - 10, 16, 8, 
        			Color.rgb((map.enemies.get(enemy).maxHP - (int) map.enemies.get(enemy).hp) * 255 / map.enemies.get(enemy).maxHP, 
        					(int) map.enemies.get(enemy).hp * 255 / map.enemies.get(enemy).maxHP, 0));
        	
        	//Draw level
        	drawNumber(map.enemies.get(enemy).level, map.enemies.get(enemy).getLevel(), (int) map.enemies.get(enemy).x - cam.rect.left, 
        			(int) map.enemies.get(enemy).y - cam.rect.top, g);
        }
    	
    	//Draw Boss
    	if (map.boss != null)
    	{
    		g.drawPixmap(Assets.blueTank, (int) map.boss.x - cam.rect.left, (int) map.boss.y - cam.rect.top, map.boss.width,
    				map.boss.width, map.boss.source.left, map.boss.source.top, map.boss.source.width(), map.boss.source.height());
    	}
    	
    	//Draw Player
    	g.drawPixmap(Assets.greenTank, (int) tank.x - cam.rect.left, (int) tank.y - cam.rect.top,
    				 tank.source.left, tank.source.top, tank.source.width(), tank.source.height());
    	
    	//Draw NPCs
    	for (int npc = 0; npc < map.npcs.size(); npc++)
    	{
    		g.drawPixmap(Assets.npcTank, (int) map.npcs.get(npc).x - cam.rect.left, 
    				(int) map.npcs.get(npc).y - cam.rect.top, map.npcs.get(npc).source.left, 
    				map.npcs.get(npc).source.top, map.npcs.get(npc).source.width(), 
    				map.npcs.get(npc).source.height());
    	}
    	
    	//Draw Explosions
    	 for (int explosion = 0; explosion < explosions.size(); explosion++)
         {
    		 g.drawPixmap(Assets.bulletsAndExplosions, explosions.get(explosion).x - cam.rect.left, 
    				 explosions.get(explosion).y - cam.rect.top, explosions.get(explosion).source.left, 
    				 explosions.get(explosion).source.top, explosions.get(explosion).source.width(), 
    				 explosions.get(explosion).source.height());
         }
    	
    	//Draw Bullets
    	for (int bullet = 0; bullet < bullets.size(); bullet++)
        {
    		g.drawPixmap(Assets.bulletsAndExplosions, bullets.get(bullet).rect.left - cam.rect.left, 
    			bullets.get(bullet).rect.top - cam.rect.top, bullets.get(bullet).source.left, bullets.get(bullet).source.top, 
    			bullets.get(bullet).source.width(), bullets.get(bullet).source.height());
        }
    	
    	//Draw Tree Bullets
    	for (int bullet = 0; bullet < treeBullets.size(); bullet++)
        {
    		g.drawPixmap(Assets.bulletsAndExplosions, treeBullets.get(bullet).rect.left - cam.rect.left, 
    			treeBullets.get(bullet).rect.top - cam.rect.top, treeBullets.get(bullet).source.left, 
    			treeBullets.get(bullet).source.top, treeBullets.get(bullet).source.width(), treeBullets.get(bullet).source.height());
        }
    	
    	int type; //Stores the items type
    	//Draw Map Items
        for (int item = 0; item < map.items.size(); item++)
        {
        	type = Assets.items.getItem(map.items.get(item).itemName).type;
        	
        	g.drawPixmap(Assets.itemsPic, map.items.get(item).x - cam.rect.left - 14, map.items.get(item).y - cam.rect.top - 14,
        			(type % 10) * Item.WIDTH, (type / 10) * Item.HEIGHT, Item.WIDTH, Item.HEIGHT);
        }
    	
    	//Clear bottom row for drawing
    	g.drawRect(0, 256, 480, 64, Color.BLACK);
    	
    	//Draw buttons
    	g.drawPixmap(Assets.buttons, dPad.X(), dPad.Y(), dPad.sourceX(), dPad.sourceY(), 
    			dPad.sourceW(), dPad.sourceH());
    	
    	g.drawPixmap(Assets.buttons, shootButton.X(), shootButton.Y(), shootButton.sourceX(), shootButton.sourceY(), 
    			shootButton.sourceW(), shootButton.sourceH());
    	
    	g.drawPixmap(Assets.buttons, itemsButton.X(), itemsButton.Y(), itemsButton.sourceX(), itemsButton.sourceY(),
    			itemsButton.sourceW(), itemsButton.sourceH());
    	
    	g.drawPixmap(Assets.buttons, menuButton.X(), menuButton.Y(), menuButton.sourceX(), menuButton.sourceY(),
    			menuButton.sourceW(), menuButton.sourceH());
    	
    	//Draw item1
    	g.drawPixmap(Assets.itemsPic, item1Button.X(), item1Button.Y(), (tank.inventory[0].type % 10) * Item.WIDTH, 
    			(tank.inventory[0].type / 10) * Item.HEIGHT, Item.WIDTH, Item.HEIGHT);
    	if (tank.numItems[0] != 0 && tank.numItems[0] != 1)
    		drawNumber(tank.numItems[0], Integer.toString(tank.numItems[0]), item1Button.X() + 18, item1Button.Y() + 59, g);
    	
    	g.drawPixmap(Assets.buttons, item1Button.X(), item1Button.Y(), item1Button.sourceX(), item1Button.sourceY(),
    			item1Button.sourceW(), item1Button.sourceH());
    	
    	//Draw item1
    	g.drawPixmap(Assets.itemsPic, item2Button.X(), item2Button.Y(), (tank.inventory[1].type % 10) * Item.WIDTH, 
    			(tank.inventory[1].type / 10) * Item.HEIGHT, Item.WIDTH, Item.HEIGHT);
    	if (tank.numItems[1] != 0 && tank.numItems[1] != 1)
    		drawNumber(tank.numItems[1], Integer.toString(tank.numItems[1]), item2Button.X() + 18, item2Button.Y() + 59, g);
    	
    	g.drawPixmap(Assets.buttons, item2Button.X(), item2Button.Y(), item2Button.sourceX(), item2Button.sourceY(),
    			item2Button.sourceW(), item2Button.sourceH());
    	
    	//Draw player info
    	g.drawPixmap(Assets.display, 64, 256);
    	
    	//level
    	g.drawText(tank.getLevel(), 118, 272, Color.rgb(2, 100, 255), 17);
    	
    	//hp
    	g.drawRect(120, 278, 64, 20, Color.rgb((tank.maxHP - (int) tank.hp) * 255 / tank.maxHP, (int) tank.hp * 255 / tank.maxHP, 0));
    	g.drawRect(123, 281, 58, 14, Color.BLACK);
    	g.drawRect(120, 278, (int) tank.hp * 64 / tank.maxHP, 20, 
    			Color.rgb((tank.maxHP - (int) tank.hp) * 255 / tank.maxHP, (int) tank.hp * 255 / tank.maxHP, 0));
    	
    	//exp
    	g.drawRect(120, 300, 64, 20, Color.YELLOW);
    	g.drawRect(123, 303, 58, 14, Color.BLACK);
    	g.drawRect(120, 300, tank.experience * 64 / tank.expToNextLevel(), 20, Color.YELLOW);
    	
    	// time
    	if (time % 60 < 10)
    		g.drawText("Time: " + (int)time / 60 + ":0" + (int)time % 60, 5, 20, Color.RED, 20);
    	else
    		g.drawText("Time: " + (int)time / 60 + ":" + (int)time % 60, 5, 20, Color.RED, 20);
    	
    	if (tank.speedBuffed())
    	{
    		g.drawPixmap(Assets.itemsPic, 0, 215, 333, 10, 39, 42); //draw speed buff icon
    		int timeLeft = (int) (tank.SPEED_BUFF_TIME - Math.round(timer));
    		g.drawText(Integer.toString(timeLeft), 0, 256, Color.RED, 16);
    	}
    	
    	if (messageToShow)
    	{
    		int x = 120;
    		int y = 175;
    		
    		g.drawPixmap(Assets.textBox, x - 15, y - 30);
    		
    		if (message.length() < 27)
    			g.drawText(message, x, y, Color.RED, 20);
			else if (message.length() < 54)
			{
				g.drawText(message.substring(0, 27), x, y, Color.RED, 20);
				g.drawText(message.substring(27), x, y + 30, Color.RED, 20);
			}
			else
			{
				g.drawText(message.substring(0, 27), x, y, Color.RED, 20);
				g.drawText(message.substring(27, 54), x, y + 30, Color.RED, 20);
				g.drawText(message.substring(54), x, y + 60, Color.RED, 20);
			}
    	}
    	
    	//g.drawText(debug, 10, 10, Color.YELLOW, 12);
    }
    
    private void drawMap(Graphics g)
    {
    	//draw each tile that is in the camera
    	for (int y = cam.rect.top / map.TILE_HEIGHT; y <= cam.rect.bottom / map.TILE_HEIGHT; y++)
		{
			for (int x = cam.rect.left / map.TILE_WIDTH; x <= cam.rect.right / map.TILE_WIDTH; x++)
			{
				if (x >= 0 && x < map.width && y >= 0 && y < map.height)
				{
					g.drawPixmap(Assets.tiles, x * map.TILE_WIDTH - cam.rect.left, y * map.TILE_HEIGHT - cam.rect.top, 
						map.tiles[y][x] % 9 * map.TILE_WIDTH, map.tiles[y][x] / 9 * map.TILE_HEIGHT, map.TILE_WIDTH, 
						map.TILE_HEIGHT);
				}
			}
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
    
    private boolean useItem(Item item)
    {
    	if (item.name.equals("Bridge"))
    	{
    		int x = tank.getTileAheadX(map.TILE_WIDTH);
    		int y = tank.getTileAheadY(map.TILE_HEIGHT);
    		int tile = map.tiles[y][x];
    		if (tile == 45)
    		{
    			switch(tank.direction)
    			{
    			case Tank.LEFT:
    			case Tank.RIGHT:
    				map.setTile(5, x, y);
    				break;
    			case Tank.UP:
    			case Tank.DOWN:
    				map.setTile(2, x, y);
    				break;
    			}
    		} else if (tile == 50)
    			map.setTile(7, x, y);
    		else if (tile == 51)
    			map.setTile(6, x, y);
    		else if (tile == 52)
    			map.setTile(3, x, y);
    		else if (tile == 53)
    			map.setTile(4, x, y);
    		else
    			return false;
    	} else if (item.name.equals("Repair Kit"))
    	{
    		if (tank.hp < tank.maxHP)
    			tank.addHP(item.hp);
    		else
    			return false;
    	} else if (item.name.equals("Super Repair Kit"))
    	{
    		if (tank.hp < tank.maxHP)
    			tank.addHP(item.hp);
    		else
    			return false;
    	} else if (item.name.equals("Tree Bullet"))
    	{
    		if (tank.notShooting())
    		{
	    		tank.shoot();
				treeBullets.add(new Bullet((int) tank.x + 12, (int) tank.y + 12, tank.direction, 32, 32));
    		}
    	} else if (item.name.equals("Speed Boost"))
    	{
    		if (!tank.buffed())
    		{
	    		timer = 0;
	    		tank.buffSpeed();
    		} else
    		{
    			return false;
    		}
    	} else {
    		return false;
    	}
    	return true;
    }
}
