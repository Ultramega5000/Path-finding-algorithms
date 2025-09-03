import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoadMap {
	
	private char[][] map;


	/** Reads the map from the maze file and adds it to a 2D array
	 * Uses BufferedReader to read each line, and gets the width and height
	 * Then resets reader and adds each character in the file to each element in the arrays
	 * @param filename the file that is being read from
	 * @param mazeSize the size of the maze that is being created
	 */
	public LoadMap(String filename, int mazeSize) {
		try {
			// Reads from the file and creates a 2D array of positions for a map
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			double width = line.length();
			double height = 1;
			while (line != null) {
				height ++;
				line = reader.readLine();
			}
			
			map = new char[(int) height][(int) width];
			
			// Reset the reader
			reader = new BufferedReader(new FileReader(filename));
			for (int i=0; i < mazeSize; i++) {
				String row = reader.readLine();	
				for (int j=0; j < mazeSize; j++) {
					map[j][i] = row.charAt(j);
				}
			}
					

		}
		
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}


	public char[][] getMap() {
		return map;
	}


	public void setMap(char[][] map) {
		this.map = map;
	}
	
	/** Checks if a state is a wall
	 * @param xPos x coordinate of the state
	 * @param yPos y coordinate of the state
	 * @return true if it is a wall, false if coordinates are out of bounds or the state is not filled
	 */
	public boolean isStateFilled(int xPos, int yPos) { 
		try {
		if (map[xPos][yPos] == 'f') {
			return true;
		} else {
			return false;
		}
		} catch(IndexOutOfBoundsException e) {
			return true;
		}
	}
	
	
}
