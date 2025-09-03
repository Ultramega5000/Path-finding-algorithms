import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class MainMenu extends JPanel implements ActionListener {

	private String pathFindingType = "Dijkstras";
	private int mazeSize = 7;

	private JFrame graphWindow;
	private JFrame mazeWindow;
	private JFrame dijkstrasWindow;
	private JButton createMazeBtn;
	private JComboBox pathFindingTypeBox;
	private JTextField mazeSizeField;
	private JTextField fileNameField;
	private ReinforcementLearning reinforcementLearningRunner;
	private Dijkstras dijkstrasRunner;

	/** Adds all the JComponents to the JPanel
	 * Title is added to the top using border layout
	 * Menu JPanel is made
	 * Smaller JPanels that contain the label and the input box are added to the menu Jpanel using border layout
	 * Each smaller Jpanel uses grid layout
	 * Button added at button
	 */
	public MainMenu() {

		
		// Fonts
		Font titleFont = new Font("Arial", Font.PLAIN, 24);
		Font mainFont = new Font("Arial", Font.PLAIN, 15);
		Font buttonFont = new Font("Arial", Font.BOLD, 15);

		setLayout(new BorderLayout());

		// Title
		JLabel title = new JLabel("Main Menu");
		title.setFont(titleFont);
		title.setBorder(new EmptyBorder(5, 185, 0, 0));
		add(title, BorderLayout.NORTH);

		// Menu Panel
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new BorderLayout());
		menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(menuPanel, BorderLayout.CENTER);
		
		// Smaller panels inside menu panel
		JPanel typeJPanel = new JPanel();
		typeJPanel.setLayout(new GridLayout(1, 2));
		JPanel sizeJPanel = new JPanel();
		sizeJPanel.setLayout(new GridLayout(1, 2));

		// Path finding type
		JLabel pathFindingTypeLabel = new JLabel("Path finding Type: ");
		pathFindingTypeLabel.setFont(mainFont);
		typeJPanel.add(pathFindingTypeLabel);
		
		// Options for path finding types
		String[] pathFindingTypes = { "Dijkstra", "Reinforcement Learning" };
		
		pathFindingTypeBox = new JComboBox(pathFindingTypes);
		pathFindingTypeBox.setSelectedIndex(0);
		pathFindingTypeBox.addActionListener(this);
		typeJPanel.add(pathFindingTypeBox);

		// Maze Size
		JLabel mazeSizeLabel = new JLabel("Maze Size:");
		mazeSizeLabel.setFont(mainFont);
		sizeJPanel.add(mazeSizeLabel);

		mazeSizeField = new JTextField("7");
		sizeJPanel.add(mazeSizeField);
		mazeSizeField.addActionListener(this);


		// Both smaller panels added to menu panel
		menuPanel.add(typeJPanel, BorderLayout.NORTH);
		menuPanel.add(sizeJPanel, BorderLayout.CENTER);
		
		// Button to create maze

		JPanel buttonPanel = new JPanel();
		createMazeBtn = new JButton("Create Maze");
		createMazeBtn.setFont(buttonFont);

		buttonPanel.add(createMazeBtn);

		add(buttonPanel, BorderLayout.SOUTH);
		createMazeBtn.addActionListener(this);

	}

	/** Gets the mazesize number from the text field
	 * If the size is less than 5, size is set to 5
	 * If the size is more than 100, size is set to 100
	 * If it is within the accepted bounds, it is just set as the variable
	 */
	public void getNumberFromTextField() {
		if (Integer.valueOf(mazeSizeField.getText()) < 5) {
			mazeSizeField.setText("5");
			mazeSize = 5;
		} else if (Integer.valueOf(mazeSizeField.getText()) > 100) {
			mazeSizeField.setText("100");
			mazeSize = 100;
		} else {
			mazeSize = Integer.valueOf(mazeSizeField.getText());
		}
	}

	/** Creates maze from the options stated in the form
	 * First disposes of all jframes that already exist
	 * Gets all of the data from the form
	 * Then it runs the class for the selected path-finding type, passing the necessary parameters
	 * For ReinforcementLearning, loads the graphs required aswell
	 * @param mazeFileName
	 */
	public void createMaze() {
		if (graphWindow != null) {
			graphWindow.dispose();
			
		}

		if (mazeWindow != null) {
			mazeWindow.dispose();
			reinforcementLearningRunner.stopMainTimer();
		}

		if (dijkstrasWindow != null) {
			dijkstrasWindow.dispose();
			dijkstrasRunner.stopMainTimer();
		}

		getNumberFromTextField();
		pathFindingType = (String) pathFindingTypeBox.getSelectedItem();

		if (pathFindingType == "Reinforcement Learning") {
			reinforcementLearningRunner = new ReinforcementLearning(mazeSize);

			// Maze Window
			mazeWindow = new JFrame();
			mazeWindow.setSize(800, 800);
			mazeWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			mazeWindow.setContentPane(reinforcementLearningRunner);
			mazeWindow.setName("NEA POC");
			mazeWindow.setVisible(true);
			mazeWindow.setLocation(200, 200);

			// Graph window

			timeDistanceGraph timeDistance = new timeDistanceGraph(reinforcementLearningRunner); // Makes a time-distance
																							// graph
			timeEpsilonGraph timeEpsilon = new timeEpsilonGraph(reinforcementLearningRunner); // Makes a time-distance graph
			timeRewardGraph timeReward = new timeRewardGraph(reinforcementLearningRunner); // Makes a time-reward graph

			JPanel graphPanel = new JPanel(); // Makes a jpanel of graphs
			graphPanel.setLayout((LayoutManager) new BoxLayout(graphPanel, BoxLayout.X_AXIS));
			graphPanel.add(timeDistance);
			graphPanel.add(timeEpsilon);
			graphPanel.add(timeReward);

			graphWindow = new JFrame();
			graphWindow.setSize(800, 800);
			mazeWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			graphWindow.setContentPane(graphPanel);
			graphWindow.setName("Graph");
			graphWindow.setVisible(true);
			graphWindow.setLocation(1100, 200);

		}

		if (pathFindingType == "Dijkstra") {
			dijkstrasRunner = new Dijkstras(mazeSize);

			// Maze Window
			dijkstrasWindow = new JFrame();
			dijkstrasWindow.setSize(800, 800);
			dijkstrasWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dijkstrasWindow.setContentPane(dijkstrasRunner);
			dijkstrasWindow.setName("NEA POC");
			dijkstrasWindow.setVisible(true);
			dijkstrasWindow.setLocation(500, 200);
		}
	}
	
	/** Handles inputs from text input boxes and button
	 *  Gets the item selected in the path finding type box
	 *  Gets text input from maze size field
	 *  Creates maze when button clicked
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// TODO Auto-generated method stub
		if (e.getSource() == pathFindingTypeBox) {
			pathFindingType = (String) pathFindingTypeBox.getSelectedItem();

		}

		if (e.getSource() == mazeSizeField) {
			getNumberFromTextField();
		}

		if (e.getSource() == createMazeBtn) {
			createMaze();
		}

	}

}
