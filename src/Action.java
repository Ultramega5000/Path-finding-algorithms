
public class Action {
	
	
	
	private String name;
	private double qValue;
	private int bearing;

	public double getqValue() {
		return qValue;
	}

	public void setqValue(double qValue) {
		this.qValue = qValue;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	// Constructor

	/** Variables from constructor set 
	 * q-value set to default
	 * @param name
	 */
	public Action(String name) {
		
		this.name = name;
		qValue = 100;
		bearing = generateBearing();
	}
	
	
	/** Returns the bearing of the action by translating its name in to a bearing
	 * Uses four if statements, that each check for the name of the action, if it is left, bearing is 270. 
	 * If it is right, bearing is 90 
	 * If it is up, bearing is 0
	 * If it isn't up, down or right, bearing is 180
	 * @return An integer bearing
	 */
	public int generateBearing() {
		if (name == "left") {
			return 270;
		} 
		else if (name == "right") {
			return 90;
		}
		else if (name == "up") {
			return 0;
		}
		else {
			return 180;
		}
	}

	public int getBearing() {
		return bearing;
	}

	public void setBearing(int bearing) {
		this.bearing = bearing;
	}


}
