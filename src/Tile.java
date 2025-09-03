
// A tile is what will represent a 'state' in the algorithm
// Each state has a set of actions it can call from
// Each action will be a class that has information about it

public abstract class  Tile {
	
	protected int length;

	protected int xPos;
	protected int yPos;
	protected int xID;
	protected int yID;
	
	protected boolean filled;
	protected boolean target;
	protected boolean start;
	protected boolean occupied;
	protected boolean path;


	public boolean isPath() {
		return path;
	}

	public void setPath(boolean path) {
		this.path = path;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}



	
	public boolean isFilled() {
		return filled;
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
	}

	public boolean isTarget() {
		return target;
	}

	public void setTarget(boolean target) {
		this.target = target;
	}

	
	/** Sets variables passed in the constructor
	 * @param x the xID
	 * @param y the yID
	 * @param length the length of the tile
	 */
	public Tile(int x, int y, int length) {
		this.length = length;
		xPos = (length * x);
		yPos = (length* y);

		xID = x;
		yID = y;
		
	}
	
	
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getxID() {
		return xID;
	}

	public void setxID(int xID) {
		this.xID = xID;
	}

	public int getyID() {
		return yID;
	}

	public void setyID(int yID) {
		this.yID = yID;
	}
		
}
