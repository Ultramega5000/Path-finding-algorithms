import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Dijkstras extends JPanel implements ActionListener {

	private Graphics2D g2d;
	private Timer mainTimer = new Timer(1, this);
	private Timer optimalPathTimer = new Timer(100, this);
	private char[][] map;
	private double newDistance;
	private ArrayList<DijkstrasTile> unvisitedSet = new ArrayList<DijkstrasTile>();
	private ArrayList<DijkstrasTile> tiles = new ArrayList<DijkstrasTile>();
	private ArrayList<DijkstrasTile> visitedSet = new ArrayList<DijkstrasTile>();
	private ArrayList<DijkstrasTile> solutionSet = new ArrayList<DijkstrasTile>();
	private DijkstrasTile currentState;
	private DijkstrasTile leftNeighbour;
	private DijkstrasTile rightNeighbour;
	private DijkstrasTile upNeighbour;
	private DijkstrasTile downNeighbour;
	private DijkstrasTile pathState;
	private int MAZESIZE;

	/** Calls assign tiles which is necessary before the algorithm starts
	 * Starts the algorithm
	 * @param mazeSize
	 */
	public Dijkstras(int mazeSize) { 

		MAZESIZE = mazeSize;
		assignTiles();
		mainTimer.start(); // Starts the episode loop
	}
	
	/** If main timer is running, gets path distances
	 * If main timer not running, goes through past distances to find optimal path
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		repaint();
		
		if (e.getSource() == mainTimer) {
			findPathDistances();
		} else {
			findOptimalPath();
		}

		
	}
	
	/** Stops main timer
	 * Calls main timer's stop method
	 */
	public void stopMainTimer() {
		mainTimer.stop();
	}
	
	
	/** Finds distance from original state, to each other state
	 * If all states have been visited, the program is stopped
	 * Finds the state with smallest distance from start, and sets as the current node
	 * If all nodes have infinite distance, algorithm terminates
	 * If current node is the target node, algorithm terminates
	 * All neighbours of current node are given the current nodes distance, plus 1
	 * Current Node is removed from unvisited set and added to visited set
	 * 
	 */
	public void findPathDistances() {
		if (unvisitedSet.size() == 0) { // if univisted is empty, algorithm terminates
			stopMainTimer();
		}

		double smallestDistanceFromStart = Math.pow(10, 99);
		for (int i = 0; i < unvisitedSet.size(); i++) {

			if (unvisitedSet.get(i).getDistanceFromStart() < smallestDistanceFromStart) {
				currentState = unvisitedSet.get(i);
				smallestDistanceFromStart = unvisitedSet.get(i).getDistanceFromStart();
			}

		}
		currentState.setOccupied(true);

		if (currentState.getDistanceFromStart() == Math.pow(10, 99)) { // if all nodes have infinite distance, algorithm
																		// terminates
			stopMainTimer();
			System.out.println("No solution found");
		}

		if (currentState.isTarget()) { // if target node is current node, we terminate
			currentState.setVisited(true);
			visitedSet.add(currentState);
			updateGraphics(currentState);
			stopMainTimer();
			
			
			for (int i = 0; i < visitedSet.size(); i++) {
				if (visitedSet.get(i).isTarget()) {
					pathState = visitedSet.get(i);
					solutionSet.add(pathState);
				}
			}
			optimalPathTimer.start();
		}

		// Updates distances of neighbours

		newDistance = (currentState.getDistanceFromStart() + 1); // gets distance of old route + 1 to show distance to
		// new tile
		// left neighbour
		for (int i = 0; i < unvisitedSet.size(); i++) {
			if (unvisitedSet.get(i).getxID() == currentState.getxID() - 1
					&& unvisitedSet.get(i).getyID() == currentState.getyID()) {
				leftNeighbour = unvisitedSet.get(i);

				if (newDistance < leftNeighbour.getDistanceFromStart()) {
					leftNeighbour.setDistanceFromStart(newDistance);
				}

			}
			// Right
			if (unvisitedSet.get(i).getxID() == currentState.getxID() + 1
					&& unvisitedSet.get(i).getyID() == currentState.getyID()) {
				rightNeighbour = unvisitedSet.get(i);
				if (newDistance < rightNeighbour.getDistanceFromStart()) {
					rightNeighbour.setDistanceFromStart(newDistance);
				}

			}

			// Down
			if (unvisitedSet.get(i).getyID() == currentState.getyID() + 1
					&& unvisitedSet.get(i).getxID() == currentState.getxID()) {
				downNeighbour = unvisitedSet.get(i);

				if (newDistance < downNeighbour.getDistanceFromStart()) {
					downNeighbour.setDistanceFromStart(newDistance);
				}
			}

			// up
			if (unvisitedSet.get(i).getyID() == currentState.getyID() - 1
					&& unvisitedSet.get(i).getxID() == currentState.getxID()) {
				upNeighbour = unvisitedSet.get(i);
				if (newDistance < upNeighbour.getDistanceFromStart()) {
					upNeighbour.setDistanceFromStart(newDistance);
				}

			}
		}

		currentState.setVisited(true);
		visitedSet.add(currentState);
		unvisitedSet.remove(currentState);
		currentState.setOccupied(false);

		// Removes the old tile from the graphics tiles list and adds the current Node
		// with the updated attributes
		updateGraphics(currentState);
	}

	/**Using the visited set, finds the best route back to the start
	 * <p>
	 * Uses the generated visited set of path distances, it starts from the target node and chooses out of its adjacent nodes the node with the smallest distance to start
	 * It does this for each tile in the path, moving back to the start along the shortest route
	 * </p>
	 */
	public void findOptimalPath() {


		if (pathState.isStart() == false) {
			double minimumDistance = 10000000;
			for (int i = 0; i < visitedSet.size(); i++) {
				if (visitedSet.get(i).getxID() == pathState.getxID() - 1
						&& visitedSet.get(i).getyID() == pathState.getyID()) {
					leftNeighbour = visitedSet.get(i);
					if (leftNeighbour.getDistanceFromStart() < minimumDistance) {
						minimumDistance = leftNeighbour.getDistanceFromStart();
						pathState = leftNeighbour;
					}
				}
				// Right
				if (visitedSet.get(i).getxID() == pathState.getxID() + 1
						&& visitedSet.get(i).getyID() == pathState.getyID()) {
					rightNeighbour = visitedSet.get(i);
					if (rightNeighbour.getDistanceFromStart() < minimumDistance) {
						minimumDistance = rightNeighbour.getDistanceFromStart();
						pathState = rightNeighbour;
					}

				}

				// Down
				if (visitedSet.get(i).getyID() == pathState.getyID() + 1
						&& visitedSet.get(i).getxID() == pathState.getxID()) {
					downNeighbour = visitedSet.get(i);

					if (downNeighbour.getDistanceFromStart() < minimumDistance) {
						minimumDistance = downNeighbour.getDistanceFromStart();
						pathState = downNeighbour;
					}
				}

				// up
				if (visitedSet.get(i).getyID() == pathState.getyID() - 1
						&& visitedSet.get(i).getxID() == pathState.getxID()) {
					upNeighbour = visitedSet.get(i);
					if (upNeighbour.getDistanceFromStart() < minimumDistance) {
						minimumDistance = upNeighbour.getDistanceFromStart();
						pathState = upNeighbour;
					}
				}
			}
			
			solutionSet.add(pathState);
			pathState.setPath(true);
			pathState.setVisited(false);
			
		} else {
			optimalPathTimer.stop();
			getFinalPath();
		}

		
	}
	
	
	/** Prints the final path to the console
	 * Reverses the solution set found by findOptimalPath()
	 * Prints the result as a string
	 */
	public void getFinalPath() {
		// Reverses the list
		Collections.reverse(solutionSet);
		// Prints the list
		String returnString = "";

		for (int i = 0; i < solutionSet.size(); i++) {
			if (i == solutionSet.size() - 1) {
				returnString += "("+solutionSet.get(i).getxID()+", "+solutionSet.get(i).getyID()+")";
			} else {
				returnString += "("+solutionSet.get(i).getxID()+", "+solutionSet.get(i).getyID()+")" + ", ";
			}
		}
		
	}

	/** Replaces a tile at the position of current node with the current node tile
	 * New node has updated attributes to change its colour to show it has been visited
	 * @param currentNode the tile that's neighbours' distances are being updated
	 */
	public void updateGraphics(DijkstrasTile currentNode) {
		for (int i = 0; i < tiles.size(); i++) {
			if (tiles.get(i).getxID() == currentNode.getxID() && tiles.get(i).getyID() == currentNode.getyID()) {
				tiles.remove(i);
				tiles.add(currentNode);
			}
		}
		repaint();
	}

	
	/** Gets distance from tile to start
	 * @param x the x value of the tile
	 * @param y the y value of the tile
	 * @return the distance from start to tile as a double
	 */
	public double getDistance(int x, int y) {
		double tileDistance = Math.sqrt(Math.pow((x), 2) + Math.pow((y), 2)); // start is always at 0, 0
		return tileDistance;
	}

	/** Draws each tile
	 *  If a wall, tile is black
	 *  If visited, it is given a colour proportional to distance from the start
	 *  If target or start, it is green
	 *  If it is optimal path, it is green
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Instantiate grpahics 2d object
		g2d = (Graphics2D) g;

		// Attempt counter

		// Draw tiles as squares
		for (int i = 0; i < tiles.size(); i++) {
			g2d.setColor(Color.BLACK);
			DijkstrasTile tile = tiles.get(i);
			g2d.drawRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());

			if (tile.isFilled()) {
				g2d.setColor(Color.BLACK);
				g2d.fillRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
			}
			
			if (tile.isVisited()) {
				g2d.setColor(new Color((int) ((((MAZESIZE*3)-tile.getDistanceFromStart())/ (MAZESIZE*3)) * 255), 0, 100));
				g2d.fillRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
				g2d.setColor(Color.BLACK);
				g2d.drawRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
			}
			
			if (tile.isPath()) {
				g2d.setColor(new Color(40,180,40));
				g2d.fillRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
				g2d.setColor(Color.BLACK);
				g2d.drawRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
			}

			if (tile.isTarget()) {
				g2d.setColor(Color.GREEN);
				g2d.fillRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
				g2d.setColor(Color.BLACK);
				g2d.drawRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
			}

			if (tile.isStart()) {
				g2d.setColor(new Color(40,180,40));
				g2d.fillRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
				g2d.setColor(Color.BLACK);
				g2d.drawRect(tile.getxPos(), tile.getyPos(), tile.getLength(), tile.getLength());
			}


		}
	}

	
	/** Generates a new map and creates a set of unvisited tiles with their distance
	 * Start is assigned a distance of 0
	 * Every other node has distance infinity because they are unvisited
	 * If a target is villed, it cannot be visited so not added to unvisited set
	 * All tiles are added to graphic tiles list so that they can all be rendered
	 */
	public void assignTiles() {
		// Fill tiles array with tiles
		GenerateMap newMap = new GenerateMap(MAZESIZE);
		newMap.generatePath();
		map = newMap.getMap();
		for (int i = 0; i < MAZESIZE; i++) {
			for (int j = 0; j < MAZESIZE; j++) {

				if (j == 0 && i == 0) {
					DijkstrasTile startTile = new DijkstrasTile(j, i, 0, false, MAZESIZE);
					startTile.setStart(true);
					unvisitedSet.add(startTile);
				} else {
					if (map[i][j] != 'f') { // checks if it isn't a wall
						if (map[i][j] == 't') { // if target, it is set to target
							unvisitedSet.add(new DijkstrasTile(j, i, Math.pow(10, 99), true, MAZESIZE));
						} else {
							unvisitedSet.add(new DijkstrasTile(j, i, Math.pow(10, 99), false, MAZESIZE));
						}
					}
				}

				DijkstrasTile graphicTile = new DijkstrasTile(j, i, 0, false, MAZESIZE); // used in apint component to draw the
																				// map

				if (map[i][j] == 'f') {
					graphicTile.setFilled(true);
				}

				if (map[i][j] == 's') {
					graphicTile.setOccupied(true);
				}

				if (map[i][j] == 't') {
					graphicTile.setTarget(true);

				}
				tiles.add(graphicTile);

			}
		}

	}
}
