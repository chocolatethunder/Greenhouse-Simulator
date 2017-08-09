/***

Temperature Model is responsible for calculations that are required for the temperature sensor 
and the control of the furnace and air conditioning system.

@author Saurabh Tomar

Written for CPSC 233

*/

package marsbars;

/***
Model responsible for calculations that are required for the temperature sensor and furnace and air conditioner control.
*/
public class TemperatureModel {	

	public static double ABSOLUTEZERO = -273.15;
	public static double TOASTY = 300.00;
	
	private int devFurnace = 0;
	private int devAirCon = 0;
	
	private double currentTemp = 0.0;
	private double temperatureUpper = 0.0;
	private double temperatureLower = 0.0;
	private double internalCoolRate = 0.0;
	private double internalHeatRate = 0.0;
	
	public TemperatureModel() {
		
	}
	
	// SET
	
	/***
	Sets the user desired upper and lower range of temperature for the sensor to maintain
	@param tempUpper The maximum temperature level of the desired temperature range
	@param tempLower The minimum temperature level of the desired temperature range
	*/
	public synchronized void setTemperatureRange(double tempUpper, double tempLower) {
		this.temperatureUpper = tempUpper;
		this.temperatureLower = tempLower;		
	}
	
	/***
	Set the current temperature level of the greenhouse
	@param currTemp Temperature of the greenhouse to set to
	@throws Exception A custom error that prompts the user if desired humdity is beyond the threshold range. 
	*/
	public synchronized void setCurrentTemperature(double currTemp) throws Exception {
		if (currTemp >= ABSOLUTEZERO && currTemp <= TOASTY) {
			this.currentTemp = currTemp;
		} else {
			throw new Exception("Temperature Out of Bounds. Please enter a value between " + ABSOLUTEZERO + "\u00b0C and " + TOASTY + "\u00b0C");
		}
	}
	
	/***
	Set the current rate at which the furnace raises the temperature of the greenhouse
	@param rate Rate of increase in temperature (C/min)
	*/
	public synchronized void setInternalCoolRate(double rate) {
		this.internalCoolRate = Math.abs(rate)*(-1);		
	}
	
	/***
	Set the current rate at which the air conditioner lowers the temperature of the greenhouse
	@param rate Rate of decrease in temperature (C/min)
	@throws Exception Prompt the user that temperature rate of change can only be positive because this is a one way device
	*/
	public synchronized void setInternalHeatRate(double rate) throws Exception {
		if (rate > 0) { 
			this.internalHeatRate = rate;
		} else {
			throw new Exception("Heating Rate must be a positive number.");
		}
	}
	
	// GET
	
	/***
	Returns the current temperature of the environment as picked up by the temperature sensor
	@return currentTemp Current greenhouse temperature level
	*/
	public synchronized double getCurrentTemp() {
		return this.currentTemp;
	}
	
	/***
	Returns whether or not the furnace or air conditioner has been turned on or off by the sensor system
	Note: Both devices cannot be on at the same time.
	@return A message to display on the GUI (Heating/Cooling/Off)
	*/
	public synchronized String getDeviceStatus(){
		if (this.devFurnace == 1) {
			return "Heating";
		}
		if (this.devAirCon == 1) {
			return "Cooling";
		}
		if (this.devAirCon == 1 && this.devFurnace == 1) {
			this.turnOffAll();
		}
		return "Off";
	}
	
	// DEVICE CONTROLLER
	
	/***
	Turns the furnace on and shuts the air conditioner off
	*/
	private void turnOnFurnace() {
		this.devFurnace = 1;
		this.devAirCon = 0;
	}
	
	/***
	Turns the airc conditioner on and shuts the furnace off
	*/
	private void turnOnAirCon() {
		this.devFurnace = 0;
		this.devAirCon = 1;
	}
	
	/***
	Turns off all the devices
	*/
	private void turnOffAll() {
		this.devFurnace = 0;
		this.devAirCon = 0;
	}
	
	// PROCESS
	
	/***
	This is the sensor method to check the temperature level of the greenhouse and update the temperature level according 
	to the rate of change that the user has specified. It also controls the furnace and air conditioner in order to increase
	or decrease the temperature level to the desired range specified by the user. 
	*/
	public synchronized void sensor() {
		
		if (this.currentTemp < this.temperatureLower) {			
			this.turnOnFurnace();			
			this.currentTemp += this.internalHeatRate;			
		} else if (this.currentTemp > this.temperatureUpper) {			
			this.turnOnAirCon();			
			this.currentTemp += this.internalCoolRate;			
		} else {
			this.turnOffAll();
		}
		
	}
	
	/***
	Creates a well formated data string so that it can be stored into a simulation save file for retrieval 
	*/
	public String toString() {
		return "T," + this.currentTemp + "," + (int)this.temperatureUpper + "," + (int)this.temperatureLower + "," + this.internalHeatRate + "," + Math.abs(this.internalCoolRate) + "," + this.devFurnace + "," + this.devAirCon + ",";
	}


}