package com.CorGaming.TankTactic;

import java.util.HashMap;

import com.CorGaming.framework.Game;
import com.CorGaming.framework.Graphics;
import com.CorGaming.framework.Music;
import com.CorGaming.framework.Screen;
import com.CorGaming.framework.Graphics.PixmapFormat;

public class LoadingScreen extends Screen 
{
    public LoadingScreen(Game game) 
    {
        super(game);
    }

    @Override
    public void update(float deltaTime) 
    {
        Graphics g = game.getGraphics();
        
        //Load images
        Assets.greenTank = g.newPixmap("GreenTank.png", PixmapFormat.ARGB8888);
        Assets.blueTank = g.newPixmap("BlueTank.png", PixmapFormat.ARGB8888);
        Assets.npcTank = g.newPixmap("NPCTank.png", PixmapFormat.ARGB8888);
        Assets.tiles = g.newPixmap("Tiles.png", PixmapFormat.ARGB4444);
        Assets.bulletsAndExplosions = g.newPixmap("BulletsAndExplosions.png", PixmapFormat.ARGB8888);
        Assets.buttons = g.newPixmap("Buttons.png", PixmapFormat.ARGB4444);
        Assets.display = g.newPixmap("Display.png", PixmapFormat.ARGB4444);
        Assets.inventoryPic = g.newPixmap("InventoryPic.png", PixmapFormat.ARGB8888);
        Assets.itemsPic = g.newPixmap("Items.png", PixmapFormat.ARGB8888);
        Assets.shopPic = g.newPixmap("ShopPic.png", PixmapFormat.ARGB8888);
        Assets.textBox = g.newPixmap("TextBox.png", PixmapFormat.RGB565);
        Assets.menu = g.newPixmap("MainMenu.png", PixmapFormat.RGB565);
        Assets.options = g.newPixmap("Options.png", PixmapFormat.RGB565);
        Assets.newgame = g.newPixmap("NewGame.png", PixmapFormat.RGB565);
        Assets.check = g.newPixmap("checkMark.png", PixmapFormat.ARGB4444);
        Assets.gameOver = g.newPixmap("GameOver.png", PixmapFormat.ARGB4444);
        Assets.victory1 = g.newPixmap("Victory1.png", PixmapFormat.ARGB4444);
        Assets.victory2 = g.newPixmap("Victory2.png", PixmapFormat.ARGB4444);
        Assets.victory3 = g.newPixmap("Victory3.png", PixmapFormat.ARGB4444);
        Assets.bestTimes = g.newPixmap("BestTimes.png", PixmapFormat.ARGB4444);
        
        //Load music
        Assets.ballad = game.getAudio().newMusic("DST-3rdBallad.ogg");
        Assets.ballad.setLooping(true);
        Assets.pariah = game.getAudio().newMusic("DST-Pariah.ogg");
        Assets.pariah.setLooping(true);
        Assets.aurora = game.getAudio().newMusic("DST-Aurora.ogg");
        Assets.aurora.setLooping(true);
        Assets.aronara = game.getAudio().newMusic("DST-Aronara.ogg");
        Assets.aronara.setLooping(true);
        
        Assets.songs = new HashMap<String, Music>();
        Assets.songs.put("0-0", Assets.aurora);
        Assets.songs.put("0-1", Assets.aurora);
        Assets.songs.put("0-2", Assets.aurora);
        Assets.songs.put("1-0", Assets.aronara);
        Assets.songs.put("1-1", Assets.aronara);
        Assets.songs.put("1-2", Assets.ballad);
        Assets.songs.put("2-0", Assets.pariah);
        Assets.songs.put("2-1", Assets.ballad);
        Assets.songs.put("2-2", Assets.ballad);
        Assets.songs.put("Inventory", Assets.aronara);
        Assets.songs.put("Menu", Assets.aronara);
        Assets.songs.put("Victory", Assets.aurora);
        Assets.songs.put("GameOver", Assets.pariah);
        
        Assets.items = new Items();
        Assets.items.loadItems(game.getFileIO());
        
        game.setScreen(new GameScreen(game));
    }

    @Override
    public void present(float deltaTime) 
    {

    }

    @Override
    public void pause() 
    {

    }

    @Override
    public void resume() 
    {

    }

    @Override
    public void dispose() 
    {

    }
}