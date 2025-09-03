import java.util.ArrayList;

public class ReinforcementTile extends Tile {

	
	private LoadMap loadMap;
	
	private double learningRate;
	private double discountFactor;
	
	private int visits;
	
	private String actionQValues;
	private int MAZESIZE;
	private ArrayList<Action> actions = new ArrayList<Action>();
	
	
	
	public int getVisits() {
		return visits;
	}

	public void setVisits(int visits) {
		this.visits = visits;
	}

	public int getActionsSize() {
		return actions.size();
	}

	
	/** Gets maze from file, sets variables passed in the constructor and sets q-learning constants
	 * Creates a loadmap class to get the maze from the file
	 * Sets the q-learning constants
	 * Calls assign actions as this must be done before the main loop
	 * @param x the xID
	 * @param y the yID
	 * @param mazeSize the size of the maze
	 */
	public ReinforcementTile(int x, int y, int mazeSize) {
		super(x, y, 100/mazeSize*7);

		loadMap = new LoadMap("maze.txt", mazeSize);
		MAZESIZE = mazeSize;
		
		// Get all the available actions
		assignActions();
		
		// Q Learning constants
		learningRate = 0.4;
		discountFactor = 0.99;
		
		// Save q values to file
		storeQValues();
		
	}
	
	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}
	
	/**Makes all q-values in the state negative
	 * Iterates through each action and multiplies by minus 1 and updates it
	 */
	public void invertQValues() {
		for (int i =0; i < actions.size(); i++) {
			actions.get(i).setqValue(actions.get(i).getqValue()*-1);
		}
	}

	/** Assigns each state the possible actions
	 * If there is a free state in a direction, the action is added to the action list for the state
	 */
	public void assignActions() {
		// If the tile is against a wall, it won't have the option to go in to walls
		
		
		// Left
		if (xID != 0 && !(loadMap.isStateFilled(xID-1, yID))) {
			Action left = new Action("left");
			if (left != null) {
				actions.add(left);
			}
		}
		
		// Right
		if (xID < MAZESIZE-1 && !(loadMap.isStateFilled(xID+1, yID))) {
			Action right = new Action("right");
			if (right != null) {
				actions.add(right);
			}
		}
		
		// Up
		if (yID != 0 && !(loadMap.isStateFilled(xID, yID-1))) {
			Action up = new Action("up");
			if (up != null) {
				actions.add(up);
			}
		}
		
		// Down
		if (yID < MAZESIZE-1 && !(loadMap.isStateFilled(xID, yID+1))) {
			Action down = new Action("down");
			if (down != null) {
				actions.add(down);
			}
		}
		
		
	}
	
	/**Gets average q-value of a tile
	 * <p>
	 * Iterates through each action in the tile and adds to a total
	 * Divides total by total actions to find mean average q-value
	 * </p>
	 * @return The mean average q-value of the actions in a tile
	 */
	public double getAverageActionQValue() {
		double totalQValue = 0;
		for (int i = 0; i < actions.size(); i++) {
			totalQValue += actions.get(i).getqValue();
		}
		
		return totalQValue/actions.size();
		
	}
	
	/** Prints all of the actions in the state to the console
	 * 
	 */
	public void showActions() {
		for (int i = 0; i < actions.size(); i++) {
			System.out.println(" ");
			System.out.println(actions.get(i).getName());
			System.out.println(" ");
		}
	}

	public ArrayList<Action> getActions() {
		return actions;
	}
	
	/** Gets the maximum expected reward out of all actions in the state
	 * @return the maximum reward as a double
	 */
	public double getMaxReward() { // Gets the maximum expected reward of all actions in the state
		double maxValue = -1000000;
		for (int i = 0; i < actions.size(); i++) {
			if (actions.get(i).getqValue() > maxValue) {
				maxValue = actions.get(i).getqValue();
			}
		}
		return maxValue;
	}

	/** Updates the q-value using the reward from the iteration
	 * @param action
	 * @param actualReward
	 */
	public void updateQValue(Action action, double actualReward) { // Takes an action and reward and updates the q values
		double expectedReward = action.getqValue();
		double newExpectedReward = (1-learningRate) * expectedReward + learningRate*(actualReward+discountFactor*getMaxReward()); // Q-value equation to calculate q value
		
		action.setqValue(newExpectedReward);
	}


	/** Updates the action with new q-values
	 * Iterates through the list of q-values and replaces each original q-value with new one
	 * @param values list of q-values
	 */
	public void inputQValues(String values) { // Assigns inputted qvalues to qvalue lists
		String[] valuesList = values.split(",");
		for (int i =0; i < valuesList.length; i++) {
			double qValue = Double.valueOf(valuesList[i]);
			actions.get(i).setqValue(qValue);
		}
		
	}

	/** Resets all q-values to 100
	 * Iterates through all q-values and sets them all to 0
	 */
	public void resetQValues() { // Resets all qvalues to 100
		for (int i =0; i < actions.size(); i++) {
			actions.get(i).setqValue(100);
		}
	}
	

	
	/** Gets the action with the largest q-value in the state
	 * Iterates through the list and compares the q-values with the current largest value
	 * @return the action with the largest q-value
	 */
	public Action getMaxAction() { // Gets the max qvalue to get the optimal action
		double maxQValue = -100000; // Compares each value to max value and replaces it if its bigger
		Action returnAction = null;
		for (int i = 0; i < actions.size(); i++) {
			if (actions.get(i).getqValue() > maxQValue) {
				maxQValue = actions.get(i).getqValue(); // Updates new max q value
				returnAction = actions.get(i); // Returns optimal action
			}
		}
		return returnAction;
	}
	
	/** Gets a string containing a list of q-values for each state
	 * Iterates through actions list and appends the q-value to a list
	 * @return A string that contains all q-values for the state
	 */
	public String storeQValues() {
		
		actionQValues = "";
		
		for (int i =0; i< actions.size(); i++) {
			actionQValues += (actions.get(i).getqValue() + ","); // Exports qvalues in a string
		}
		
		String finalString = String.valueOf(xID+yID*7) + ":"+actionQValues; // Gets the name of tile and places on front of the values
		finalString.replaceAll("null", "");
		return finalString;
	}
}
