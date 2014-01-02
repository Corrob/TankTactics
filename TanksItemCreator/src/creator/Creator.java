package creator;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class Creator {
	
	final String[] types = {"None", "Bridge", "Health", "Super Health", "Tree Bullet", "Speed Boost", "Item6", "Item7", "Item8", 
			"Item9", "Bronze Armor", "Silver Armor", "Gold Armor", "Bronze Turret", "Silver Turret", "Gold Turret"};
	
	private JFrame frmTanktacticsItemCreator;
	private JTextField nameText;
	private JTextField HPText;
	private JTextField speedText;
	private JTextField damageText;
	private JTextField attackSpeedText;
	private JComboBox equipBox;
	private JComboBox typeBox;
	private JList list;
	
	private DefaultListModel model;
	private DefaultComboBoxModel typeModel;
	
	private List<Item> items;
	private JTextField DesText1;
	private JTextField DesText2;
	private JTextField DesText3;
	private JTextField costText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Creator window = new Creator();
					window.frmTanktacticsItemCreator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Creator() {
		initialize();
		loadItems();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		items = new ArrayList<Item>();
		
		frmTanktacticsItemCreator = new JFrame();
		frmTanktacticsItemCreator.setTitle("Tanktactics Item Creator");
		frmTanktacticsItemCreator.setBounds(100, 100, 400, 400);
		frmTanktacticsItemCreator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTanktacticsItemCreator.getContentPane().setLayout(null);
		
		JLabel lblItems = new JLabel("Items");
		lblItems.setBounds(10, 40, 46, 14);
		frmTanktacticsItemCreator.getContentPane().add(lblItems);
		
		JScrollPane scrollPaneList = new JScrollPane();
		scrollPaneList.setBounds(50, 21, 200, 80);
		frmTanktacticsItemCreator.getContentPane().add(scrollPaneList);
		
		model = new DefaultListModel();
		
		list = new JList();
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				changeItem();
			}
		});
		list.setModel(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(4);
		scrollPaneList.setViewportView(list);
		
		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newItem();
			}
		});
		btnNew.setBounds(270, 19, 89, 23);
		frmTanktacticsItemCreator.getContentPane().add(btnNew);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteItem();
			}
		});
		btnDelete.setBounds(270, 57, 89, 23);
		frmTanktacticsItemCreator.getContentPane().add(btnDelete);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 122, 46, 14);
		frmTanktacticsItemCreator.getContentPane().add(lblName);
		
		nameText = new JTextField();
		nameText.setBounds(83, 119, 167, 20);
		frmTanktacticsItemCreator.getContentPane().add(nameText);
		nameText.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(10, 222, 75, 14);
		frmTanktacticsItemCreator.getContentPane().add(lblDescription);
		
		JLabel lblHp = new JLabel("HP:");
		lblHp.setBounds(298, 91, 46, 14);
		frmTanktacticsItemCreator.getContentPane().add(lblHp);
		
		HPText = new JTextField();
		HPText.setBounds(270, 104, 86, 20);
		frmTanktacticsItemCreator.getContentPane().add(HPText);
		HPText.setColumns(10);
		
		JLabel lblSpeed = new JLabel("Speed:");
		lblSpeed.setBounds(293, 127, 57, 14);
		frmTanktacticsItemCreator.getContentPane().add(lblSpeed);
		
		JLabel lblDamage = new JLabel("Damage:");
		lblDamage.setBounds(287, 176, 65, 14);
		frmTanktacticsItemCreator.getContentPane().add(lblDamage);
		
		JLabel lblAttackSpeed = new JLabel("Attack Speed:");
		lblAttackSpeed.setBounds(270, 217, 89, 14);
		frmTanktacticsItemCreator.getContentPane().add(lblAttackSpeed);
		
		speedText = new JTextField();
		speedText.setBounds(273, 145, 86, 20);
		frmTanktacticsItemCreator.getContentPane().add(speedText);
		speedText.setColumns(10);
		
		damageText = new JTextField();
		damageText.setBounds(273, 192, 86, 20);
		frmTanktacticsItemCreator.getContentPane().add(damageText);
		damageText.setColumns(10);
		
		attackSpeedText = new JTextField();
		attackSpeedText.setBounds(270, 232, 86, 20);
		frmTanktacticsItemCreator.getContentPane().add(attackSpeedText);
		attackSpeedText.setColumns(10);
		
		JLabel lblEquipable = new JLabel("Equipable:");
		lblEquipable.setBounds(140, 295, 75, 14);
		frmTanktacticsItemCreator.getContentPane().add(lblEquipable);
		
		equipBox = new JComboBox();
		equipBox.setModel(new DefaultComboBoxModel(new String[] {"True", "False"}));
		equipBox.setBounds(140, 317, 63, 20);
		frmTanktacticsItemCreator.getContentPane().add(equipBox);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveItem();
			}
		});
		btnSave.setBounds(10, 316, 89, 23);
		frmTanktacticsItemCreator.getContentPane().add(btnSave);
		
		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(284, 300, 46, 14);
		frmTanktacticsItemCreator.getContentPane().add(lblType);
		
		typeModel = new DefaultComboBoxModel();
		for (int i = 0; i < types.length; i++)
			typeModel.addElement(types[i]);
		
		typeBox = new JComboBox();
		typeBox.setModel(typeModel);
		typeBox.setBounds(237, 316, 122, 23);
		frmTanktacticsItemCreator.getContentPane().add(typeBox);
		
		DesText1 = new JTextField();
		DesText1.setBounds(83, 173, 167, 20);
		frmTanktacticsItemCreator.getContentPane().add(DesText1);
		DesText1.setColumns(27);
		
		DesText2 = new JTextField();
		DesText2.setColumns(27);
		DesText2.setBounds(83, 219, 167, 20);
		frmTanktacticsItemCreator.getContentPane().add(DesText2);
		
		DesText3 = new JTextField();
		DesText3.setColumns(27);
		DesText3.setBounds(83, 264, 167, 20);
		frmTanktacticsItemCreator.getContentPane().add(DesText3);
		
		JLabel lblCost = new JLabel("Cost:");
		lblCost.setBounds(298, 260, 46, 14);
		frmTanktacticsItemCreator.getContentPane().add(lblCost);
		
		costText = new JTextField();
		costText.setBounds(273, 277, 86, 20);
		frmTanktacticsItemCreator.getContentPane().add(costText);
		costText.setColumns(10);
	}
	
	private void newItem()
	{
		model.addElement("New Item");
		items.add(new Item("New Item", "None", false, Item.NONE, 0, 0, 0, 0, 0));
	}
	
	private void deleteItem()
	{
		if (list.getSelectedIndex() != -1)
		{
			int index = list.getSelectedIndex();
			model.remove(index);
			items.remove(index);
		}
	}
	
	private void changeItem()
	{
		if (list.getSelectedIndex() != -1)
		{
			Item sel = items.get(list.getSelectedIndex());
			nameText.setText(sel.name);
			
			//set Description
			if (sel.description.length() < 27)
			{
				DesText1.setText(sel.description);
				DesText2.setText("");
				DesText3.setText("");
			}
			else if (sel.description.length() < 54)
			{
				DesText1.setText(sel.description.substring(0, 27));
				DesText2.setText(sel.description.substring(27));
				DesText3.setText("");
			} else
			{
				DesText1.setText(sel.description.substring(0, 27));
				DesText2.setText(sel.description.substring(27, 54));
				DesText3.setText(sel.description.substring(54));
			}
			
			HPText.setText(Integer.toString(sel.hp));
			speedText.setText(Integer.toString(sel.speed));
			damageText.setText(Integer.toString(sel.damage));
			attackSpeedText.setText(Integer.toString(sel.attackSpeed));
			costText.setText(Integer.toString(sel.cost));
			if (sel.equipable)
				equipBox.setSelectedIndex(0);
			else
				equipBox.setSelectedIndex(1);
			typeBox.setSelectedIndex(sel.type);
		}
	}
	
	private void saveItem()
	{
		if (list.getSelectedIndex() != -1)
		{
			int index = list.getSelectedIndex();
			
			boolean equip;
			if (equipBox.getSelectedIndex() == 0)
				equip = true;
			else
				equip = false;
			
			int hp = Integer.parseInt(HPText.getText());
			int speed = Integer.parseInt(speedText.getText());
			int damage = Integer.parseInt(damageText.getText());
			int attackSpeed = Integer.parseInt(attackSpeedText.getText());
			int cost = Integer.parseInt(costText.getText());
			
			String description = DesText1.getText() + DesText2.getText() + DesText3.getText();
			
			Item item = new Item(nameText.getText(), description, equip, typeBox.getSelectedIndex(),
					hp, speed, damage, attackSpeed, cost);
			
			items.remove(index);
			items.add(index, item);
			
			model.remove(index);
			model.add(index, item.name);
			
			saveItems();
		}
	}
	
	private void saveItems()
	{
		try {
			FileOutputStream file = new FileOutputStream("C:\\Users\\Cory\\workspace\\Tanks\\assets\\items.dat");
			ObjectOutputStream buffer = new ObjectOutputStream(file);
			
			buffer.writeInt(items.size());
			
			for (int item = 0; item < items.size(); item++)
			{
				buffer.writeObject(items.get(item).name);
				buffer.writeObject(items.get(item).description);
				buffer.writeBoolean(items.get(item).equipable);
				buffer.writeInt(items.get(item).type);
				buffer.writeInt(items.get(item).hp);
				buffer.writeInt(items.get(item).speed);
				buffer.writeInt(items.get(item).damage);
				buffer.writeInt(items.get(item).attackSpeed);
				buffer.writeInt(items.get(item).cost);
			}
			
			buffer.close();
		} catch (Exception e) { System.out.println(e.getMessage()); }
	}
	
	private void loadItems()
	{
		try {
			FileInputStream file = new FileInputStream("C:\\Users\\Cory\\workspace\\Tanks\\assets\\items.dat");
			ObjectInputStream buffer = new ObjectInputStream(file);
			
			int size = buffer.readInt();
			
			for (int item = 0; item < size; item++)
			{
				String name = (String) buffer.readObject();
				String description = (String) buffer.readObject();
				boolean equip = buffer.readBoolean();
				int type = buffer.readInt();
				int hp = buffer.readInt();
				int speed = buffer.readInt();
				int damage = buffer.readInt();
				int attackSpeed = buffer.readInt();
				int cost = buffer.readInt();
				items.add(new Item(name, description, equip, type, hp, speed, damage, attackSpeed, cost));
				model.addElement(name);
			}
			
			buffer.close();
		} catch (Exception e) { System.out.println(e.getMessage()); }
	}
}
