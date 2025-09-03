import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class ReinforcementLearning extends JPanel implements ActionListener {

	private boolean goalReached = false;
	private int episodes;
	private int steps;
	private double reward;
	private Graphics2D g2d;
	private Timer mainTimer = new Timer(1, this);
	private ReinforcementTile previousState;
	private double previousReward;
	private int targetxPos;
	private int targetyPos;
	private int STEPCOUNT;
	private double tileDistance;
	private int lastEpisode;
	private int lastSteps;
	private double epsilon;
	private double epislonDecay;
	private double distanceRewardMultiplier;
	private double repeatTileReward;
	private int winReward;
	private int wins;
	private int MAZESIZE;
	private boolean solutionFound;
	private double strongQValueOffset;
	private int firstWinAttempt;
	private String qValueFile;

	private ArrayList<Experience> experiences = new ArrayList<Experience>();
	private ArrayList<ReinforcementTile> tiles = new ArrayList<ReinforcementTile>();

	public double getReward() {
		return reward;
	}

	public void setReward(double reward) {
		this.reward = reward;
	}

	public double getPreviousReward() {
		return previousReward;
	}

	public void setPreviousReward(double previousReward) {
		this.previousReward = previousReward;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	public double getTileDistance() {
		return tileDistance;
	}

	public void setTileDistance(double tileDistance) {
		this.tileDistance = tileDistance;
	}

	public int getEpisodes() {
		return episodes;
	}

	public void setEpisodes(int episodes) {
		this.episodes = episodes;
	}

	/**Sets all reinforcement learning constants and assigns tiles
	 * Every constant used in the code is set here
	 * q-values are reset
	 * Assign tiles is called, which makes a new maze and adds all tiles in the maze to a list
	 * @param mazeSize
	 */
	public ReinforcementLearning(int mazeSize) { // Allows for multithreading
		MAZESIZE = mazeSize;
		firstWinAttempt = -1;
		solutionFound = false;	
		resetQValues();
		assignTiles(); // assigns tiles their special attributes
		steps = 0;
		reward = 4000; // Default rewards
		lastEpisode = 0;
		episodes = 0;
		STEPCOUNT = 20 * MAZESIZE / 7; // Maximum steps before reset
		strongQValueOffset = 0.3; // How much less likely a strong q value is of being random
		tileDistance = 8;
		epsilon = 0.8; // The frequency of random actions that will happen - exploitation vs
						// exploration function
		epislonDecay = 0.00001; // Dictates rate of epislon decay, higher the value the faster it decays
		distanceRewardMultiplier = 230; // Controls the amount the distance away from goal rewards the agent
		repeatTileReward = 70; // Controls how much repeating tiles will punish the agent
		winReward = 5000;
		wins = 0;

		mainTimer.start(); // Starts the episode loop
	}

	/**
	 * Generates a maze and creates an object for each tile which is added to a list
	 * Calls generatePath() from GenerateMap class that generates a maze using
	 * iterative randomised prim's algorithm Prints out the generated maze Iterates
	 * through the maze and creates a ReinforcementTile object for each tile in the
	 * class, and assigns attributes depending on the character stored in the map
	 * array Adds tile to tiles list
	 */
	public void assignTiles() {
		// Fill tiles array with tiles
		GenerateMap newMap = new GenerateMap(MAZESIZE);
		newMap.generatePath();
		char[][] map = newMap.getMap();
		newMap.writeToFile();

		for (int i = 0; i < MAZESIZE; i++) {
			for (int j = 0; j < MAZESIZE; j++) {
				ReinforcementTile tile = new ReinforcementTile(j, i, MAZESIZE);
				if (map[i][j] == 'f') {
					tile.setFilled(true);
				}

				if (map[i][j] == 's') {
					tile.setStart(true);
					tile.setVisits(1);
					tile.setOccupied(true);
				}

				if (map[i][j] == 't') {
					tile.setTarget(true);
					targetxPos = j;
					targetyPos = i;
				}

				tiles.add(tile);
			}
		}
	}
	

	/**
	 * Makes the agent move based on the action given as a parameter Depending on
	 * the action being done, decided by switch statement that is decided by the
	 * parameter action Iterates through maze to find the tile that the agent will
	 * move to Sets new tile to be occupied, and increments visits Sets the old tile
	 * to be not occupied Sets old tile to be previous state
	 * 
	 * @param action is the action that is being done by the agent
	 */
	public void doAction(Action action) {

		ReinforcementTile currentState = getCurrentState();

		// For each action, it will remove the current state as occupied, and sets a new
		// state as occupied in the correct position
		switch (action.getName()) {

		case "left":
			for (int i = 0; i < MAZESIZE * MAZESIZE; i++) {
				if (tiles.get(i).getxID() == currentState.getxID() - 1
						&& tiles.get(i).getyPos() == currentState.getyPos()) {
					tiles.get(i).setOccupied(true);
					tiles.get(i).setVisits(tiles.get(i).getVisits() + 1);
				}
			}
			break;

		case "right":
			for (int i = 0; i < MAZESIZE * MAZESIZE; i++) {
				if (tiles.get(i).getxID() == currentState.getxID() + 1
						&& tiles.get(i).getyPos() == currentState.getyPos()) {
					tiles.get(i).setOccupied(true);
					tiles.get(i).setVisits(tiles.get(i).getVisits() + 1);
				}
			}
			break;

		case "up":
			for (int i = 0; i < MAZESIZE * MAZESIZE; i++) {
				if (tiles.get(i).getyID() == currentState.getyID() - 1
						&& tiles.get(i).getxPos() == currentState.getxPos()) {
					tiles.get(i).setOccupied(true);
					tiles.get(i).setVisits(tiles.get(i).getVisits() + 1);
				}
			}
			break;

		case "down":
			for (int i = 0; i < MAZESIZE * MAZESIZE; i++) {
				if (tiles.get(i).getyID() == currentState.getyID() + 1
						&& tiles.get(i).getxPos() == currentState.getxPos()) {
					tiles.get(i).setOccupied(true);
					tiles.get(i).setVisits(tiles.get(i).getVisits() + 1);
				}
			}
			break;

		}
		currentState.setOccupied(false);
		previousState = currentState;
	}

	/**
	 * Returns a random action from the current state Gets a random number between 0
	 * and the size of the states' actions list size Indexes from actions list with
	 * random int
	 * 
	 * @param state is the state the agent is currently in
	 * @return returns a random action
	 */
	public Action getRandomAction(ReinforcementTile state) {
		// Gets a random action

		Random rnd = new Random();
		int arrayListLength = state.getActions().size();
		int randomInt = rnd.nextInt(arrayListLength);
		return state.getActions().get(randomInt);
	}
	
	/**Stops main timer
	 * 
	 */
	public void stopMainTimer() {
		mainTimer.stop();
	}

	
	/**Draws the maze graphics
	 * Draws text at the point that contains information about the current episode and when tthe first win was
	 * If no solution has been found, it draws each tile normally, walls are black
	 * Each tile has a shade of purple correlating to the average q-value in the tile
	 * If a solution has been found, the path is shown in green
	 * Start and end tiles have different shades of green
	 * 
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Instantiate grpahics 2d object
		g2d = (Graphics2D) g;

		// Attempt counter and first win attempt

		g2d.drawString("Attempt: " + episodes, 370, 750);
		if (firstWinAttempt != -1) {
			g2d.drawString("First Win on Attempt: " + firstWinAttempt, 450, 750);
		}

		if (solutionFound == false) { // If no solution found, it draws the tiles without the solution route

			// Draw tiles as squares
			for (int i = 0; i < MAZESIZE * MAZESIZE; i++) {
				g2d.setColor(Color.BLACK);
				ReinforcementTile tile = tiles.get(i);
				g2d.drawRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());

				if (tile.isFilled()) { // if filled it is black
					g2d.setColor(Color.BLACK);
					g2d.fillRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
				}

				else if (tile.isOccupied()) { // if occupied it is green
					g2d.setColor(new Color(40, 180, 40));
					g2d.fillRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
					g2d.setColor(Color.BLACK);
					g2d.drawRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());

				}

				else if (tile.isTarget()) { // if target, it is green
					g2d.setColor(Color.GREEN);
					g2d.fillRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
					g2d.setColor(Color.BLACK);
					g2d.drawRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
				}

				else {
					try {
						g2d.setColor(new Color((int) (Math
								.abs((((MAZESIZE * 4 * 1000) + 10000 - (tile.getAverageActionQValue() + 10000)) // Colour is based on average q value
										/ ((MAZESIZE * 4 * 1000) + 10000)))
								* 255), 0, 100));
					} catch (IllegalArgumentException e) {
						g2d.setColor(new Color(255, 0, 100));
					}
					g2d.fillRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
					g2d.setColor(Color.BLACK);
					g2d.drawRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
				}

			}
		} else { // If a solution found the solution route is shown
			for (int i = 0; i < MAZESIZE * MAZESIZE; i++) {
				g2d.setColor(Color.BLACK);
				ReinforcementTile tile = tiles.get(i);
				g2d.drawRect(tile.getxPos(), tile.getyPos(), 100, 100);

				if (tile.isFilled()) {
					g2d.setColor(Color.BLACK);
					g2d.fillRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
				}

				else if (tile.isTarget()) {
					g2d.setColor(Color.GREEN);
					g2d.fillRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
					g2d.setColor(Color.BLACK);
					g2d.drawRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
				}

				else if (tile.isPath()) {
					g2d.setColor(new Color(40, 180, 40));
					g2d.fillRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
					g2d.setColor(Color.BLACK);
					g2d.drawRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());

				} else {
					try {
						g2d.setColor(new Color((int) (Math
								.abs((((MAZESIZE * 4 * 1000) + 10000 - (tile.getAverageActionQValue() + 10000))
										/ ((MAZESIZE * 4 * 1000) + 10000)))
								* 255), 0, 100));
					} catch (IllegalArgumentException e) {
						g2d.setColor(new Color(255, 0, 100));
					}
					g2d.fillRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
					g2d.setColor(Color.BLACK);
					g2d.drawRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
				}

			}
		}
	}

	/**Returns distance from target
	 * Calculates x distance and y distance
	 * Uses pythagoras to calculate total distance
	 * @param tile the current tile the agent is in
	 * @return the distance from target as a double
	 */
	public double findTileDistance(ReinforcementTile tile) {
		tileDistance = Math.sqrt(Math.pow((tile.getxID() - targetxPos), 2) + Math.pow((tile.getyID() - targetyPos), 2));
		return tileDistance;
	}

	/**Returns distance from target
	 * Calculates x distance and y distance
	 * Uses pythagoras to calculate total distance
	 * @param tile the current tile the agent is in
	 * @return the distance from target as a double
	 */
	public double getReward(double reward, ReinforcementTile tile) { // Adds to the original reward with the distance to
																		// target
		return reward - (findTileDistance(tile) * distanceRewardMultiplier);
	}

	
	/**After an episode has been completed, all variables are reset
	 * The number of steps taken becomes last steps, and steps is reset
	 * Episodes incremented
	 * Reward is calculated and set to previous reward
	 * Reward is reset
	 * Epsilon is updated
	 * If too many episodes have passed, all q values are reset
	 * resets the maze
	 */
	public void resetScene() { // Resets all temporary variables for each episode

		lastSteps = steps;
		steps = 0;

		episodes += 1;
		previousReward = getReward(reward, getCurrentState());
		reward = 4000;
		if (epsilon - (epislonDecay * previousReward) > 0) { // Modifies epsilon
			epsilon -= epislonDecay * (previousReward); // Adding 1000 ensures reward is always positive to ensure
														// epsilon will always decrease
			if (getCurrentState().isTarget() && epsilon - 0.05 > 0) {
				epsilon -= 0.05;
			}
		}

		if (episodes == 40 * MAZESIZE / 7 || episodes == 60 * MAZESIZE / 7 || episodes == 80 * MAZESIZE / 7) { // if it has been 40 episodes of no success, it resets
			epsilon = 0.5;
			for (int i =0; i < tiles.size(); i++) {
				tiles.get(i).invertQValues();
			}
		}

		resetState(); // Resets the agent back to square 1

	}
	/**If the agent can go forwards or backwards this calculates the action requried to go forwards
	 * Gets the bearing of the direction to go backwards, and gets the first action that doesn't equal this bearing
	 * Allows it to go any direction except backwards
	 * @param state is current tile
	 * @param previousAction is the action the agent just executed
	 * @return the action requried to go forwards
	 */
	public Action findNextTile(ReinforcementTile state, Action previousAction) {
		int oppositePreviousActionBearing = 0;
		if (previousAction.getBearing() + 180 >= 360) {
			oppositePreviousActionBearing = previousAction.getBearing() + 180 - 360;
		} else {
			oppositePreviousActionBearing = previousAction.getBearing() + 180;
		}
		for (int i = 0; i < state.getActionsSize(); i++) {
			if (state.getActions().get(i).getBearing() != oppositePreviousActionBearing) {

				return state.getActions().get(i);
			}
		}
		return null;
	}

	
	/** On each loop, the last episode is evaluted
	 * Iterates through each experience, which is the steps the agent took on its last attempt
	 * Gives each action it executed the reward of the entire episode
	 * Clears the experiences list
	 * Increments last episode
	 */
	public void analyseEpisode() {
		for (int i = 0; i < lastSteps; i++) {
			Experience experience = experiences.get(i);

			experience.getPreviousState().updateQValue(experience.getAction(), previousReward);

		}
		experiences.clear();

		lastEpisode += 1;
	}

	/** After a solution is found, it iterates through the last attempt and marks each step as a part of the path
	 * Writes q values to the file so they are saved
	 * Iterates through the last experiences and marks each tile as part of the path
	 * Stops main loop
	 * Paints screen to show path
	 */
	public void updateAfterSolutionFound() {
		// Saves Q values
		writeQValues();
		solutionFound = true;
		for (int i = 0; i < lastSteps; i++) {
			Experience experience = experiences.get(i);

			experience.getPreviousState().setPath(true);
			experience.getCurrentState().setPath(true);

		}
		stopMainTimer();
		repaint();
	}

	/** The main loop for the reinforcement learning algorithm
	 * If goal has been reached, wins is incremented and reward added
	 * If wins exceeds 3, it is assumed that the most efficient solution has been found
	 * Once one episode has been completed, the previous episode is analysed
	 * If goal hasn't been reached and there are still steps left in episode, next action si found
	 * If max steps is reached and goal reached is false, new episode is started
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// If it reaches the goal it has a large reward and then resets the episode
		if (goalReached()) {
			if (wins == 0) {
				firstWinAttempt = episodes;
			}
			wins += 1;
			reward += winReward;
			if (episodes < 200) {
				resetScene();
			}

		}

		// If solution found, it shows the route on the screen
		if (wins > 3) {
			updateAfterSolutionFound();
		}
		
		if (episodes > lastEpisode) { // if there has been an episode before, it will analyse the reward and update q
										// values based on this
			analyseEpisode();
		}

		if (!(goalReached()) && steps < STEPCOUNT) { // If goal not found and steps left in the episode, the next action
														// is decided
			getNextAction();

		}
		if (steps >= STEPCOUNT && episodes < 200 && goalReached() == false) { // If there are still episodes to do and
																				// steps is exceeded, a new episode is
																				// started
			resetScene();
		}

		// Repaints visuals
		repaint();
	}

	/** Gets the next action to be used
	 * Gets a random decimal
	 * If the current state has a strong q-value, random double is updated so unlikely to cause a random action to be used
	 * If it only has two options, forwards and backwards, it will continue forwards
	 * Else, if random decimal is less than epsilon, action is random
	 * If random decimal is larger than epsilon, the action with largest q-value is taken
	 * If a tile has been visited, some reward is taken away
	 * Action is completed and added to experiences	 * 
	 */
	public void getNextAction() {
		Action action = null; // Inititialises action

		double randomDouble = Math.random();

		if (getCurrentState().getAverageActionQValue() > 3000) {
			randomDouble += strongQValueOffset; // makes a strong q value less likely to be random
		}

		if (experiences.size() > 0 && getCurrentState().getActionsSize() == 2) { // If in a corridor, it will continue
																					// down corridor without going back
																					// on itself
			action = findNextTile(getCurrentState(), experiences.get(experiences.size() - 1).getAction());

		} else {
			if (randomDouble < epsilon) { // do random guesses
				action = getRandomAction(getCurrentState()); // Chooses a random action

			} else {
				action = getCurrentState().getMaxAction(); // Chooses qValue action

			}

		}

		if (getCurrentState().getVisits() > 0) {
			reward -= repeatTileReward * getCurrentState().getVisits(); // The more a tile has been visited the worse
																		// the reward
		}

		doAction(action); // Carries out the chosen action
		steps += 1; // Adds a setp

		experiences.add(new Experience(previousState, action, reward, getCurrentState(), goalReached())); // adds a new
																											// experience
	}

	/**
	 * Resets all q values in q values text file to nothing Writes to file using
	 * file writer an empty string Catch statement necessary for writing to a file
	 * Also resets q values in each states, action list to a default value of 100
	 */
	public void resetQValues() {
		String outputString = "";
		// Writes to File
		try {
			FileWriter myWriter = new FileWriter("qValues.txt");
			myWriter.write(outputString);
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		for (int i = 0; i < tiles.size(); i++) {
			tiles.get(i).resetQValues(); // sets all q values to default of 100
		}
	}

	/** Writes the qvalues to a file so they can be saved
	 * Iterates through tiles, and adds to a string the q values for each tile
	 * Writes to file using FileWriter class inside a try-catch
	 */
	public void writeQValues() {

		String outputString = "";

		for (int i = 0; i < tiles.size(); i++) {
			outputString += tiles.get(i).storeQValues() + "\n"; // Adds to the list a tiles qvalues

			// Writes to File
			try {
				FileWriter myWriter = new FileWriter("qValues.txt");
				myWriter.write(outputString);
				myWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
	}

	/** Reads qvalues from a given file
	 * Uses the Buffered Reader class to read the file
	 * Reads each line in the file and splits it into two parts: state ID and action q-values
	 * If there are actions in a state, they will be assigned to the state
	 * THen it searches for the state using its ID, and assigns the q-values to the state using inputQValues method
	 * @param fileName the file that is being read from
	 */
	public void readQValues(String fileName) {
		try {

			String actions = "";
			// Reads from the file and creates a 2D array of positions for a map
			BufferedReader reader = new BufferedReader(new FileReader(fileName));

			String line = reader.readLine();

			while (line != null) {
				String tileIDString = line.split(":")[0]; // Gets the state ID

				if (line.split(":").length > 1) { // If the state has actions, they will be assigned to the state
					actions = line.split(":")[1]; // Splits actions and states
					int tileID = Integer.valueOf(tileIDString);
					for (int i = 0; i < tiles.size(); i++) { // Searches for the state using its ID
						if (tiles.get(i).getxID() + tiles.get(i).yID * 7 == tileID) { // Once state has been found, it
																						// is assigned its actions
							ReinforcementTile tile = tiles.get(i);
							tile.inputQValues(actions);
						}
					}
				}

				line = reader.readLine(); // New line is read
			}

		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void resetState() {
		for (int i = 0; i < MAZESIZE * MAZESIZE; i++) {

			if (tiles.get(i).getxID() == 0 && tiles.get(i).getyID() == 0) {
				tiles.get(i).setOccupied(true);
				tiles.get(i).setVisits(1);
			} else {
				tiles.get(i).setOccupied(false);
				tiles.get(i).setVisits(0);
			}
		}
	}

	/**
	 * <p>
	 * Iterates through the tiles arraylist and returns the tile that is occupied
	 * </p>
	 * 
	 * @return the ReinforcementTile that the client is currently on
	 */
	public ReinforcementTile getCurrentState() {
		for (int i = 0; i < MAZESIZE * MAZESIZE; i++) {
			if (tiles.get(i).isOccupied()) {
				return tiles.get(i);
			}
		}
		return null;
	}

	/**
	 * Finds whether the goal has been found If current state is target, returns
	 * true
	 * @return true when the current state is the target
	 */
	public boolean goalReached() {
		if (getCurrentState().isTarget()) {
			return true;
		} else {
			return false;
		}
	}

}
