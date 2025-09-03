
public class Experience {
	
	private ReinforcementTile previousState;
	private Action action;
	private double reward;
	private ReinforcementTile currentState;
	private boolean gameOver;
	
	
	/** Sets all variables passed in constructor
	 * @param ppreviousState the state it was in in before the action was made
	 * @param paction the action it made
	 * @param preward the reward from the whole episode
	 * @param pcurrentState the state it transitioned into
	 * @param pgameOver Whether the game has ended or not
	 */
	public Experience(ReinforcementTile ppreviousState, Action paction, double preward, ReinforcementTile pcurrentState, boolean pgameOver) {
		previousState = ppreviousState;
		action = paction;
		reward = preward;
		currentState = pcurrentState;
		gameOver = pgameOver;
		
	}


	public ReinforcementTile getPreviousState() {
		return previousState;
	}


	public void setPreviousState(ReinforcementTile previousState) {
		this.previousState = previousState;
	}


	public Action getAction() {
		return action;
	}


	public void setAction(Action action) {
		this.action = action;
	}


	public double getReward() {
		return reward;
	}


	public void setReward(double reward) {
		this.reward = reward;
	}


	public ReinforcementTile getCurrentState() {
		return currentState;
	}


	public void setCurrentState(ReinforcementTile currentState) {
		this.currentState = currentState;
	}


	public boolean isGameOver() {
		return gameOver;
	}


	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	
}
