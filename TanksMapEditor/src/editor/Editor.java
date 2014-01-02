package editor;

import javax.swing.JFrame;

public class Editor 
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("TankTactics Map Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add(new MapPanel());
		
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
}
