package mypack;

import javax.swing.JFrame;

public class Main{
	public static void main(String[] args) {
		JFrame window = new JFrame("Simple Chess");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);

		//Add GamePanel to window
		GamePanel gp=new GamePanel();
		window.add(gp);
		window.pack();

		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gp.launchGame();
	}
}
