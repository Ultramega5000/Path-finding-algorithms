import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;

public class timeDistanceGraph extends GraphPlotter {

	/**Sets all of the constant variables required for drawing
	 * @param m passes a Reinforcement Learning object to the super constructor
	 */
	public timeDistanceGraph(ReinforcementLearning m) {
		super(m);
		
		xAxis = "Episodes"; // Labels for the graph
		yAxis = "Distance"; 
		zeroError = 330; // how much y value is added at the end
		magnitudeMultiplier = 50; // Puts it in a sensible place on the graph
		maxValue = 8; // max value that it could be
	}

	/** Adds the new distance values to the list after each episode, and ensures graph doesn't go off the screen
	 * When a new episode has happened, adds a new episode to the list
	 * Adds new distance value to the list
	 * Increments last episode
	 * Once size of xValues exceeds 20, removes old values from the start
	 * Repaints the screen
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (reinforcementLearning.getEpisodes() > lastEpisode) { // If an episode has happenned
			xValues.add(reinforcementLearning.getEpisodes()); // Add a new episode to the list to be plotted
			yValues.add(reinforcementLearning.getTileDistance()); // Add tile distance to list to be plotted
			lastEpisode += 1; // Increment that the last episode has been read
		}
		
		while (xValues.size() > 20) {
			xValues.remove(xValues.get(xValues.size()-1));
			yValues.remove(yValues.get(0));
		}
		repaint(); // Repaints the screen
		
	}


}
