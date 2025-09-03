
public class DijkstrasTile extends Tile {
	
	private double distanceFromStart;
	private boolean visited;



	public boolean isVisited() {
		return visited;
	}


	public void setVisited(boolean visited) {
		this.visited = visited;
	}


	public double getDistanceFromStart() {
		return distanceFromStart;
	}


	public void setDistanceFromStart(double distanceFromStart) {
		this.distanceFromStart = distanceFromStart;
	}


	/** Sets all variables for the tile
	 * @param x the xID
	 * @param y the yID
	 * @param distanceFromStart distance from start tile
	 * @param target whether it is target or not
	 * @param mazeSize the size of the maze
	 */
	public DijkstrasTile(int x, int y, double distanceFromStart, boolean target, int mazeSize) {
		super(x, y, 100/mazeSize*7);
		
		this.distanceFromStart = distanceFromStart;
		this.target = target;
	}

}
