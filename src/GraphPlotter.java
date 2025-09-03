import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import javax.swing.JPanel;

public abstract class GraphPlotter extends JPanel implements ActionListener {
	
	private Graphics2D g2d;
	protected ArrayList<Integer> xValues = new ArrayList<Integer>();
	protected ArrayList<Double> yValues = new ArrayList<Double>();
	
	protected ReinforcementLearning reinforcementLearning;
	private Timer mainTimer = new Timer(100, this);
	protected int lastEpisode;
	protected int offset;
	protected int zeroError;
	protected double magnitudeMultiplier;
	protected int maxValue;
	
	protected String xAxis;
	protected String yAxis;
	
	/** Sets all constant variables necessary to draw axes
	 * @param m Passes a reinforcement learning object to the graph plotter in order to get data
	 */
	public GraphPlotter(ReinforcementLearning m) {
		reinforcementLearning = m;
		mainTimer.start(); // Starts main loop
		lastEpisode = 0; // No episodes graphed yet
		offset = 30; // Screen offset for when it moves off the screen
	}	
	
	/** Draws the axes and the labels and the data
	 * Draws x axis and label
	 * Draws y axis and label
	 * Goes through the list of xvalues and yvalues, and draws each oval, then draws a line between each oval
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		

		
		// Instantiate grpahics 2d object
		g2d = (Graphics2D) g;
		
		// Plots x axis
		g2d.drawLine(offset, getHeight()-30, getWidth()-offset, getHeight()-30); // Draws x axis and label
		g2d.drawString(xAxis, (getWidth()/2)-offset, getHeight()-15);
		
		// Plot y axis
		g2d.drawLine(offset, getHeight()-30, offset, 30); // Draws y axis and label
		g2d.drawString(yAxis, offset, 30);

		
		for (int i =0; i < xValues.size(); i++) {

			g2d.fillOval((int) ((xValues.get(i)*10)-2+offset), (int) ((maxValue*magnitudeMultiplier)-(yValues.get(i)*magnitudeMultiplier)+zeroError), 2, 2); // Plots circles where values are
			
			if (i > 0) {
				g2d.drawLine((int) ((xValues.get(i-1)*10)+offset), (int) ((maxValue*magnitudeMultiplier)-(yValues.get(i-1)*magnitudeMultiplier)+zeroError), (int) ((xValues.get(i)*10)+offset), (int) ((maxValue*magnitudeMultiplier)-(yValues.get(i)*magnitudeMultiplier)+zeroError)); // Connects circles with lines
			}
		}
		

		
	}
	
}
