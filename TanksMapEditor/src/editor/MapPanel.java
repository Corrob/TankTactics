package editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;

public class MapPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final int WIDTH = 788, HEIGHT = 700;
	
	private Map map;
	private Camera cam;
	private Items items;
	
	private BufferedImage tiles;
	private Image tank;
	private Image item;
	private Image npc;
	
	private JScrollBar vBar;
	private JScrollBar hBar;
	
	private Selector sel;
	
	private JButton newBut;
	private JButton save;
	private JButton load;
	private JButton addEnemy;
	private JButton removeEnemy;
	private JButton addItem;
	private JButton removeItem;
	private JButton addNPC;
	private JButton removeNPC;
	
	private JLabel mapNameLabel;
	private JLabel widthLabel;
	private JLabel heightLabel;
	
	private JLabel upLabel;
	private JLabel leftLabel;
	private JLabel rightLabel;
	private JLabel downLabel;
	
	private JLabel enemyLabel;
	private JLabel xLabel;
	private JLabel yLabel;
	private JLabel enemyLevelLabel;
	
	private JLabel itemsLabel;
	private JLabel itemLabel;
	
	private JLabel npcsLabel;
	
	private JTextField mapNameText;
	private JTextField widthText;
	private JTextField heightText;
	
	private JTextField upText;
	private JTextField leftText;
	private JTextField rightText;
	private JTextField downText;
	
	private JTextField xText;
	private JTextField yText;
	private JTextField enemyLevelText;
	
	private JTextField statementText1;
	private JTextField statementText2;
	private JTextField statementText3;
	
	private JComboBox itemsBox;
	private DefaultComboBoxModel itemsModel;
	
	public MapPanel()
	{		
		map = new Map(30, 30, "Default");
		cam = new Camera(0, 0);
		items = new Items();
		items.loadItems();
		
		try //load images
		{
			tiles = ImageIO.read(new File("C:\\Users\\Cory\\workspace\\Tanks\\assets\\Tiles.png"));
			tank = new ImageIcon("tank.png").getImage();
			item = new ImageIcon("C:\\Users\\Cory\\workspace\\Tanks\\ItemPic.png").getImage();
			npc = new ImageIcon("npc.png").getImage();
		} catch (IOException e) {
			System.out.print(e.getMessage());
		}
		
		BarListener barListener = new BarListener();
		ButtonListener buttonListener = new ButtonListener();
		
		Mouse mouseListener = new Mouse();
		MouseMotion mouseMotion = new MouseMotion();
		
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseMotion);
				
		sel = new Selector();
		
		//ScrollBars
		vBar = new JScrollBar(JScrollBar.VERTICAL, 0, (int) cam.rect.getHeight(), 0, map.height * map.TILE_HEIGHT);
		vBar.setUnitIncrement(map.TILE_HEIGHT);
		vBar.setBounds(480, 0, 20, 480);
		vBar.addAdjustmentListener(barListener);
		
		hBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, (int) cam.rect.getWidth(), 0, map.width * map.TILE_WIDTH);
		hBar.setUnitIncrement(map.TILE_WIDTH);
		hBar.setBounds(0, 480, 500, 20);
		hBar.addAdjustmentListener(barListener);
		
		//Buttons
		newBut = new JButton("New");
		newBut.setBounds(500, 256, 96, 25);
		newBut.addActionListener(buttonListener);
		
		save = new JButton("Save");
		save.setBounds(596, 256, 96, 25);
		save.addActionListener(buttonListener);
		
		load = new JButton("Load");
		load.setBounds(692, 256, 96, 25);
		load.addActionListener(buttonListener);
		
		addEnemy = new JButton("Add");
		addEnemy.setBounds(20, 660, 80, 25);
		addEnemy.addActionListener(buttonListener);
		
		removeEnemy = new JButton("Remove");
		removeEnemy.setBounds(100, 660, 80, 25);
		removeEnemy.addActionListener(buttonListener);
		
		addItem = new JButton("Add");
		addItem.setBounds(230, 660, 80, 25);
		addItem.addActionListener(buttonListener);
		
		removeItem = new JButton("Remove");
		removeItem.setBounds(310, 660, 80, 25);
		removeItem.addActionListener(buttonListener);
		
		addNPC = new JButton("Add");
		addNPC.setBounds(420, 660, 80, 25);
		addNPC.addActionListener(buttonListener);
		
		removeNPC = new JButton("Remove");
		removeNPC.setBounds(500, 660, 80, 25);
		removeNPC.addActionListener(buttonListener);
		
		//Labels
		mapNameLabel = new JLabel("Map Name: ");
		mapNameLabel.setBounds(500, 300, 100, 20);
		mapNameLabel.setForeground(Color.RED);
		
		widthLabel = new JLabel("Width: ");
		widthLabel.setBounds(500, 330, 100, 20);
		widthLabel.setForeground(Color.RED);
		
		heightLabel = new JLabel("Height: ");
		heightLabel.setBounds(500, 360, 100, 20);
		heightLabel.setForeground(Color.RED);
		
		upLabel = new JLabel("Up: ");
		upLabel.setBounds(500, 390, 100, 20);
		upLabel.setForeground(Color.RED);
		
		leftLabel = new JLabel("Left: ");
		leftLabel.setBounds(500, 420, 100, 20);
		leftLabel.setForeground(Color.RED);
		
		rightLabel = new JLabel("Right: ");
		rightLabel.setBounds(500, 450, 100, 20);
		rightLabel.setForeground(Color.RED);
		
		downLabel = new JLabel("Down: ");
		downLabel.setBounds(500, 480, 100, 20);
		downLabel.setForeground(Color.RED);
		
		enemyLabel = new JLabel("Enemies");
		enemyLabel.setBounds(70, 580, 100, 20);
		enemyLabel.setForeground(Color.RED);
		
		xLabel = new JLabel("X: ");
		xLabel.setBounds(20, 510, 100, 20);
		xLabel.setForeground(Color.RED);
		
		yLabel = new JLabel("Y: ");
		yLabel.setBounds(20, 540, 100, 20);
		yLabel.setForeground(Color.RED);
		
		enemyLevelLabel = new JLabel("Level: ");
		enemyLevelLabel.setBounds(20, 620, 100, 20);
		enemyLevelLabel.setForeground(Color.RED);
		
		itemsLabel = new JLabel("Items");
		itemsLabel.setBounds(293, 580, 100, 20);
		itemsLabel.setForeground(Color.RED);
		
		itemLabel = new JLabel("Item: ");
		itemLabel.setBounds(230, 620, 100, 20);
		itemLabel.setForeground(Color.RED);
		
		npcsLabel = new JLabel("NPCs");
		npcsLabel.setBounds(485, 510, 100, 20);
		npcsLabel.setForeground(Color.RED);
		
		//Text boxes
		mapNameText = new JTextField();
		mapNameText.setBounds(600, 300, 180, 20);
		
		widthText = new JTextField();
		widthText.setBounds(600, 330, 180, 20);
		
		heightText = new JTextField();
		heightText.setBounds(600, 360, 180, 20);
		
		upText = new JTextField();
		upText.setBounds(600, 390, 180, 20);
		
		leftText = new JTextField();
		leftText.setBounds(600, 420, 180, 20);
		
		rightText = new JTextField();
		rightText.setBounds(600, 450, 180, 20);
		
		downText = new JTextField();
		downText.setBounds(600, 480, 180, 20);
		
		xText = new JTextField();
		xText.setBounds(60, 510, 100, 20);
		
		yText = new JTextField();
		yText.setBounds(60, 540, 100, 20);
		
		enemyLevelText = new JTextField();
		enemyLevelText.setBounds(60, 620, 100, 20);
		
		statementText1 = new JTextField();
		statementText1.setBounds(425, 540, 150, 20);
		
		statementText2 = new JTextField();
		statementText2.setBounds(425, 580, 150, 20);
		
		statementText3 = new JTextField();
		statementText3.setBounds(425, 620, 150, 20);
		
		//Items model
		itemsModel = new DefaultComboBoxModel();
		for (int i = 0; i < Items.items.size(); i++)
		{
			itemsModel.addElement(Items.getItem(i).name);
		}
		itemsBox = new JComboBox();
		itemsBox.setModel(itemsModel);
		itemsBox.setBounds(270, 620, 120, 20);
		
		//add everything to the panel
		add(vBar);
		add(hBar);
		
		add(newBut);
		add(save);
		add(load);
		add(addEnemy);
		add(removeEnemy);
		add(addItem);
		add(removeItem);
		add(addNPC);
		add(removeNPC);
		
		add(mapNameLabel);
		add(widthLabel);
		add(heightLabel);
		
		add(upLabel);
		add(leftLabel);
		add(rightLabel);
		add(downLabel);
		
		add(enemyLabel);
		add(xLabel);
		add(yLabel);
		add(enemyLevelLabel);
		
		add(itemsLabel);
		add(itemLabel);
		
		add(npcsLabel);
		
		add(mapNameText);
		add(widthText);
		add(heightText);
		
		add(upText);
		add(leftText);
		add(rightText);
		add(downText);
		
		add(xText);
		add(yText);
		add(enemyLevelText);
		
		add(statementText1);
		add(statementText2);
		add(statementText3);
		
		add(itemsBox);
		
		//set preferences for the panel
		setLayout(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground (Color.BLACK);
		setDoubleBuffered(true);
	}
	
	public void paintComponent (Graphics g)
	{
		drawMap(g);
		
		//Draw coords
		int xCoord = (getMousePosition().x + (int) cam.rect.getX()) / map.TILE_WIDTH;
		int yCoord = (getMousePosition().y + (int) cam.rect.getY()) / map.TILE_HEIGHT;
		g.setColor(Color.RED);
		g.setFont(new Font("Dialog", Font.PLAIN, 24));
		g.drawString("(" + xCoord + ", " + yCoord + ")", 20, 20);
		
		//Clean the tile selection area and text option areas
		g.setColor(Color.BLACK);
		g.fillRect(500, 0, 288, 500);
		g.fillRect(0, 500, 788, 200);
		
		//Draw tiles to be selected
		g.drawImage(tiles, 500, 0, null);
		
		//Draw selector
		g.setColor(Color.RED);
		g.drawRect(sel.x, sel.y, sel.WIDTH, sel.HEIGHT);
	}
	
	private void drawMap(Graphics g)
    {
		//loop through the tiles within the camera rect and display each one
    	for (int y = (int) cam.rect.getY() / map.TILE_HEIGHT; y <= ((int) cam.rect.getY() + (int) cam.rect.getHeight()) / map.TILE_HEIGHT; y++)
		{
			for (int x = (int) cam.rect.getX() / map.TILE_WIDTH; x <= ((int)cam.rect.getX() + (int) cam.rect.getWidth()) / map.TILE_WIDTH; x++)
			{
				if (x >= 0 && x < map.width && y >= 0 && y < map.height)
				{
					int screenX = x * map.TILE_WIDTH - (int)cam.rect.getX();
					int screenY = y * map.TILE_HEIGHT - (int)cam.rect.getY();
					int sourceX = map.tiles[y][x] % 9 * map.TILE_WIDTH;
					int sourceY = map.tiles[y][x] / 9 * map.TILE_HEIGHT;
					g.drawImage(tiles, screenX, screenY, screenX + map.TILE_WIDTH, screenY + map.TILE_HEIGHT, 
							sourceX, sourceY, sourceX + map.TILE_WIDTH, sourceY + map.TILE_HEIGHT, null);
				}
			}
		}
    	
    	//draw enemies
    	for (int enemy = 0; enemy < map.tanks.size(); enemy++)
    	{
    		if (cam.rect.intersects(new Rectangle(map.tanks.get(enemy).x, map.tanks.get(enemy).y, 32, 32)))
    		{
    			g.drawImage(tank, map.tanks.get(enemy).x - (int) cam.rect.getX(), 
    					map.tanks.get(enemy).y - (int) cam.rect.getY(), null);
    			
    			//draw the enemies level
    			g.setColor(Color.RED);
    			g.setFont(new Font("Dialog", Font.BOLD, 12));
    			
    			if (map.tanks.get(enemy).level < 10)
    				g.drawString(Integer.toString(map.tanks.get(enemy).level), 
    					map.tanks.get(enemy).x - (int) cam.rect.getX() + 14, map.tanks.get(enemy).y - (int) cam.rect.getY() + 38);
    			else
    				g.drawString(Integer.toString(map.tanks.get(enemy).level), 
        					map.tanks.get(enemy).x - (int) cam.rect.getX() + 10, map.tanks.get(enemy).y - (int) cam.rect.getY() + 38);
    		}
    	}
    	
    	//draw items
    	for (int i = 0; i < map.items.size(); i++)
    	{
    		if (cam.rect.intersects(new Rectangle(map.items.get(i).x, map.items.get(i).y, 32, 32)))
    		{
    			g.drawImage(item, map.items.get(i).x - (int) cam.rect.getX(), 
    					map.items.get(i).y - (int) cam.rect.getY(), null);
    			
    			//draw the item name
    			g.setColor(Color.RED);
    			g.setFont(new Font("Dialog", Font.BOLD, 12));
    			g.drawString(map.items.get(i).itemName, map.items.get(i).x - (int) cam.rect.getX(), 
    					map.items.get(i).y - (int) cam.rect.getY() + 38);
    		}
    	}
    	
    	//draw NPCs
    	for (int i = 0; i < map.npcs.size(); i++)
    	{
    		if (cam.rect.intersects(new Rectangle(map.npcs.get(i).x, map.npcs.get(i).y, 32, 32)))
    		{
    			g.drawImage(npc, map.npcs.get(i).x - (int) cam.rect.getX(), 
    					map.npcs.get(i).y - (int) cam.rect.getY(), null);
    			
    			//draw the item name
    			g.setColor(Color.RED);
    			g.setFont(new Font("Dialog", Font.BOLD, 12));
    			String statement = map.npcs.get(i).statement;
    			
    			if (statement.length() < 27)
    				g.drawString(statement, map.npcs.get(i).x - (int) cam.rect.getX(), 
    						map.npcs.get(i).y - (int) cam.rect.getY() + 38);
    			else if (statement.length() < 54)
    			{
    				g.drawString(statement.substring(0, 27), map.npcs.get(i).x - (int) cam.rect.getX(), 
    						map.npcs.get(i).y - (int) cam.rect.getY() + 38);
    				g.drawString(statement.substring(27), map.npcs.get(i).x - (int) cam.rect.getX(), 
    						map.npcs.get(i).y - (int) cam.rect.getY() + 50);
    			}
    			else
    			{
    				g.drawString(statement.substring(0, 27), map.npcs.get(i).x - (int) cam.rect.getX(), 
    						map.npcs.get(i).y - (int) cam.rect.getY() + 38);
    				g.drawString(statement.substring(27, 54), map.npcs.get(i).x - (int) cam.rect.getX(), 
    						map.npcs.get(i).y - (int) cam.rect.getY() + 50);
    				g.drawString(statement.substring(54), map.npcs.get(i).x - (int) cam.rect.getX(), 
    						map.npcs.get(i).y - (int) cam.rect.getY() + 62);
    			}
    		}
    	}
    }
	
	private class ButtonListener implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			if (e.getSource() == newBut) //New Button pressed
			{
				if (mapNameText.getText().equals(map.name))
				{
					map = new Map(Integer.parseInt(widthText.getText()), Integer.parseInt(heightText.getText()),
							mapNameText.getText(), map.tiles, map.tanks, map.items, map.npcs);
				}
				else
				{
					map = new Map(Integer.parseInt(widthText.getText()), Integer.parseInt(heightText.getText()),
							mapNameText.getText());
				}
				cam.setPos(0, 0); //reset camera
				vBar.setValues(0, (int) cam.rect.getHeight(), 0, map.height * map.TILE_HEIGHT);
				hBar.setValues(0, (int) cam.rect.getWidth(), 0, map.width * map.TILE_WIDTH);
			} else if (e.getSource() == save)
			{
				//record all of the boxes and then save
				map.name = mapNameText.getText();
				map.up = upText.getText();
				map.left = leftText.getText();
				map.right = rightText.getText();
				map.down = downText.getText();
				map.save();
			} else if (e.getSource() == load)
			{
				//load and display the new info
				map.load(mapNameText.getText());
				cam.setPos(0, 0);
				vBar.setValues(0, (int) cam.rect.getHeight(), 0, map.height * map.TILE_HEIGHT);
				hBar.setValues(0, (int) cam.rect.getWidth(), 0, map.width * map.TILE_WIDTH);
				
				widthText.setText(Integer.toString(map.width));
				heightText.setText(Integer.toString(map.height));
				upText.setText(map.up);
				leftText.setText(map.left);
				rightText.setText(map.right);
				downText.setText(map.down);
			} else if (e.getSource() == addEnemy)
			{
				int x = Integer.parseInt(xText.getText()) * map.TILE_WIDTH;
				int y = Integer.parseInt(yText.getText()) * map.TILE_HEIGHT;
				int level = Integer.parseInt(enemyLevelText.getText());
				
				map.addTank(x, y, level);
			} else if (e.getSource() == removeEnemy)
			{
				int x = Integer.parseInt(xText.getText()) * map.TILE_WIDTH;
				int y = Integer.parseInt(yText.getText()) * map.TILE_HEIGHT;
				
				map.removeTank(x, y);
			} else if (e.getSource() == addItem)
			{
				int x = Integer.parseInt(xText.getText()) * map.TILE_WIDTH;
				int y = Integer.parseInt(yText.getText()) * map.TILE_HEIGHT;
				Item item = Items.getItem((String) itemsModel.getSelectedItem());
				
				map.addItem(x, y, item);
			} else if (e.getSource() == removeItem)
			{
				int x = Integer.parseInt(xText.getText()) * map.TILE_WIDTH;
				int y = Integer.parseInt(yText.getText()) * map.TILE_HEIGHT;
				
				map.removeItem(x, y);
			} else if (e.getSource() == addNPC)
			{
				int x = Integer.parseInt(xText.getText()) * map.TILE_WIDTH;
				int y = Integer.parseInt(yText.getText()) * map.TILE_HEIGHT;
				String statement = statementText1.getText() + statementText2.getText() + statementText3.getText();
				
				map.addNPC(x, y, statement);
			} else if (e.getSource() == removeNPC)
			{
				int x = Integer.parseInt(xText.getText()) * map.TILE_WIDTH;
				int y = Integer.parseInt(yText.getText()) * map.TILE_HEIGHT;
				
				map.removeNPC(x, y);
			}
			
			repaint();
		}
	}
	
	private class BarListener implements AdjustmentListener
	{
		public void adjustmentValueChanged(AdjustmentEvent e)
		{
			//set the camera to the values changed on the bars
			cam.rect.setRect(hBar.getValue(), vBar.getValue(), cam.rect.getWidth(), cam.rect.getHeight());
			repaint();
		}
	}
	
	private class Mouse implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) 
		{
			//if it is clicked in the tile selection area
			if (e.getX() > 500 && e.getY() < 256)
			{
				sel.moveTo(e.getX(), e.getY(), 500); //move the selector
			}
			
			//if it is clicked int the map area
			if (e.getX() < 480 && e.getY() < 480)
			{
				//change the tile to the selected tile
				map.tiles[((e.getY() + (int) cam.rect.getY())) / map.TILE_HEIGHT][(e.getX() + (int) cam.rect.getX()) / map.TILE_WIDTH]
						= sel.selected;
			}
			
			repaint();
		}
	}
	
	private class MouseMotion implements MouseMotionListener
	{

		@Override
		public void mouseDragged(MouseEvent e) {
			//if dragged in map area
			if (e.getX() < 480 && e.getY() < 480)
			{
				//change the tile to the selected tile
				map.tiles[((e.getY() + (int) cam.rect.getY())) / map.TILE_HEIGHT][(e.getX() + (int) cam.rect.getX()) / map.TILE_WIDTH]
						= sel.selected;
			}
			
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) 
		{
			repaint();
		}
	}
}
