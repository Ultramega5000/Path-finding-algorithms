import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		// Main Menu
		MainMenu mainMenu = new MainMenu();
		JFrame MainMenuFrame = new JFrame();
		MainMenuFrame.setSize(500, 200);
		MainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainMenuFrame.setContentPane(mainMenu);
		MainMenuFrame.setName("Main Menu");
		MainMenuFrame.setVisible(true);
	
		

	}

}
