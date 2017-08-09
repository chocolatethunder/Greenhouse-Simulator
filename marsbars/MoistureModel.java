/***

Soil Moisture Model is responsible for calculations that are required for the soil moisture sensor 
and the control of the sprinkler system.  

Please note that this is only a one way system. The sprinkler cannot remove mositure from the environment.
As per instructions only a one way sprinkler device is provided.

@author Saurabh Tomar

Written for CPSC 233

*/

package marsbars;

/***
Model responsible for calculations that are required for the soil moisture sensor and sprinkler control.
*/
public class MoistureModel {	
	
	public static int MINMOISTURE = 0;
	public static int MAXMOISTURE = 100;
	
	private int sprinkler = 0;
	
	private double currentMoisture = 0.0;
	private double moistureUpper = 0.0;
	private double moistureLower = 0.0;
	private double internalMoistureRate = 0.0;
	
	public MoistureModel() {
		
	}
	
	// SET
	
	/***
	Sets the user desired upper and lower range of soil moisture for the sensor to maintain upto
	@param moistUpper The maximum soil moisture level of the desired soil moisture range
	@param moistLower The minimum soil moisture level of the desired soil moisture range
	*/
	public synchronized void setMoistureRange(double moistUpper, double moistLower) {
		this.moistureUpper = moistUpper;
		this.moistureLower = moistLower;		
	}
	
	/***
	Set the current soil moisture level of the greenhouse
	@param currMoist Soil Moisture level of the greenhouse to set to
	@throws Exception A custom error that prompts the user if desired humdity is beyond the threshold range. 
	*/
	public synchronized void setCurrentMoisture(double currMoist) throws Exception {
		if (currMoist >= MINMOISTURE && currMoist <= MAXMOISTURE) {
			this.currentMoisture = currMoist;
		} else {
			throw new Exception("Moisture Out of Bounds. Please enter a value between 0% and 100%");
		}
	}
	
	/***
	Set the current rate at which the sprinkler raises the soil moisture of the greenhouse
	@param rate Rate of increase in soil moisture (%/min)
	@throws Exception Prompt the user that soil moisture rate of change can only be positive because there is no device to remove moisture
	*/
	public synchronized void setInternalMoistRate(double rate) throws Exception {
		if (rate > 0) { 
			this.internalMoistureRate = rate;
		} else {
			throw new Exception("Moisture Rate must be a positive number.");
		}
	}
	
	// GET
	
	/***
	Returns the current soil moisture of the environment as picked up by the soil moisture sensor
	@return currentMoisture Current greenhouse soil moisture level
	*/
	public synchronized double getCurrentMoisture() {
		return this.currentMoisture;
	}
	
	/***
	Returns whether or not the sprinkler has been turned on or off by the sensor system
	@return A message to display on the GUI (On/Off)
	*/
	public synchronized String getDeviceStatus(){
		if (this.sprinkler == 1) {
			return "On";
		}
		return "Off";
	}
	
	// PROCESS
	
	/***
	This is the sensor method to check the soil moisture level of the greenhouse and update the soil moisture level according 
	to the rate of change that the user has specified. It also controls the sprinkler in order to increase(only) the 
	level of soil moisture to the desired range specified by the user. 
	*/
	public synchronized void sensor() {
		
		if (this.currentMoisture < this.moistureLower && this.currentMoisture >= MINMOISTURE) {			
			this.sprinkler = 1;			
			this.currentMoisture += this.internalMoistureRate;			
		} else if (this.currentMoisture > this.moistureUpper && this.currentMoisture <= MAXMOISTURE) {
			this.sprinkler = 0;	
		} else {
			this.sprinkler = 0;
		}
		
	}
	
	/***
	Creates a well formated data string so that it can be stored into a simulation save file for retrieval 
	*/
	public String toString() {
		return "M," + this.currentMoisture + "," + (int)this.moistureUpper + "," + (int)this.moistureLower + "," + this.internalMoistureRate + "," + (int)this.sprinkler + ",";
	}

}