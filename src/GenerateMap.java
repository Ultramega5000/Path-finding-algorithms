import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GenerateMap {

	private int MAZESIZE;
	private char[][] map;
	private Random rd = new Random();
	private ArrayList<int[]> wallList = new ArrayList<int[]>();
	public char[][] getMap() {
		return map;
	}




	public void setMap(char[][] map) {
		this.map = map;
	}




	private int[] lastPassage;
	
	/** Generates a maze full of walls
	 * Sets the start top left corner as s
	 * @param mazeSize the size of the maze
	 */
	public GenerateMap(int mazeSize) {
		MAZESIZE = mazeSize;
		map = new char[MAZESIZE][MAZESIZE];
		
		for (int i = 0; i < MAZESIZE; i++) {
			for (int j = 0; j < MAZESIZE; j++) {
				map[j][i] = 'f';
			}
		}
		map[0][0] = 's';
		
	}
	

	/** Iteratively generates a path with prim's algorithm
	 * Starts with a grid full of walls
	 * The top left cell is assigned as the start, the walls of the start cell added to wall list
	 * While there are walls in the list, pick a random wall from teh lsit, if only one of the cells that the wall divides is visited then:
	 * 1. Make the wall empty and mark the unvisited cell as part of the maze
	 * 2. Add the neighbouring walls of the cell to the wall list
	 * Remove the wall from the wall list
	 */
	public void generatePath() {

		map[0][0] = 's';
		
		
		// Adds neighbouring walls to walls list
		
		wallList.add(new int[]{1, 0});

		wallList.add(new int[]{0, 1});
		// While walls list has a size
		
		while (wallList.size() < MAZESIZE*(MAZESIZE-1)) {
			
			// Gets a random wall
			int randomIndex = rd.nextInt(wallList.size());
			boolean bordersVisited = false;
			
			// Checks if it neighbours only one passage
			
			int[] wall = wallList.get(randomIndex);// gets a random wall wall[0] = x, wall[1] = y
			
			
			
			// Check if index in bounds
			
			if (wall[0]-1 >= 0) {
				if (map[wall[0]-1][wall[1]] == ' ' || map[wall[0]-1][wall[1]] == 's') {
					bordersVisited = true;
					
				}
			}
			
			
			
			if (wall[1]-1 >= 0) {
				
				if(map[wall[0]][wall[1]-1] == ' ' || map[wall[0]][wall[1]-1] == 's') {
					if (bordersVisited == true) {
						continue;
					}
					bordersVisited = true;
				}
			}
			
			if (wall[0]+1 < MAZESIZE) {
				
				if (map[wall[0]+1][wall[1]] == ' ' || map[wall[0]+1][wall[1]] == 's') {
					if (bordersVisited == true) {
						continue;
					}
					bordersVisited = true;
				}
			}
			
			
			if (wall[1]+1 < MAZESIZE ) {
				if (map[wall[0]][wall[1]+1] == ' ' || map[wall[0]][wall[1]+1] == 's') {
					if (bordersVisited == true) {
						continue;
					}
					bordersVisited = true;
				}
			}
			
			
			
			
			// If it is neighbouring only one cell, changes wall to a passage
			
			if (bordersVisited == true) {
				
				map[wall[0]][wall[1]] = ' '; // makes wall a passage
				
				wallList.remove(randomIndex);
				
				// Adds neighbouring walls to list
				
				if (wall[0]-1 >= 0 && map[wall[0]-1][wall[1]] != ' ' && map[wall[0]-1][wall[1]] != 't' && map[wall[0]-1][wall[1]] != 's') {
					wallList.add(new int[]{wall[0]-1, wall[1]});
				}
				if (wall[1]-1 >= 0 && map[wall[0]][wall[1]-1] != ' ' && map[wall[0]][wall[1]-1] != 't' && map[wall[0]][wall[1]-1] != 's') {
					wallList.add(new int[] {wall[0], wall[1]-1});
				}
				
				if (wall[0]+1 < MAZESIZE && map[wall[0]+1][wall[1]] != ' ' && map[wall[0]+1][wall[1]] != 't' && map[wall[0]+1][wall[1]] != 's') {
					wallList.add(new int[]{wall[0]+1, wall[1]});
				}
				
				if (wall[1]+1 < MAZESIZE && map[wall[0]][wall[1]+1] != ' ' && map[wall[0]][wall[1]+1] != 't' && map[wall[0]][wall[1]+1] != 's') {
					wallList.add(new int[]{wall[0], wall[1]+1});
				}
				
				if (!(wallList.size() < MAZESIZE*(MAZESIZE-1))) {
					map[wall[0]][wall[1]] = 't';
				}
			
			
			}
			
		}
	}
	
	/** Writes the generated maze to the maze file
	 *  Adds each line to the output file
	 *  Uses FileWriter to add the string to the file
	 */
	public void writeToFile() {
		String outputString = "";
		
		for (int i = 0; i < MAZESIZE; i++) {
			for (int j = 0; j < MAZESIZE; j++) {
				outputString += map[i][j]+"";
			}
			
			outputString += "\n";
		}
		
		// Writes to file
		
		try {
			FileWriter myWriter = new FileWriter("maze.txt");
			myWriter.write(outputString);
			myWriter.close();
		} catch(IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}

