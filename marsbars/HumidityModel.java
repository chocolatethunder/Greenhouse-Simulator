/***

Humidity Model is responsible for calculations that are required for the humidity sensor 
and the control of the humidifier system.  

Please note that this is only a one way system. The humidifier cannot dehumidify the environment 
for that to occur a dehumidifier device will need to be installed. As per instructions only a one way 
humidifier is provided.

@author Saurabh Tomar

Written for CPSC 233

*/

package marsbars;

/***
Model responsible for calculations that are required for the humidity sensor and humidifier control.
*/
public class HumidityModel {	
	
	public static int MINHUMIDITY = 0;
	public static int MAXHUMIDITY = 100;
	
	private int humidifier = 0;
	
	private double currentHumidity = 0.0;
	private double humidityUpper = 0.0;
	private double humidityLower = 0.0;
	private double internalHumidityRate = 0.0;
	
	public HumidityModel() {
		
	}
	
	// SET
	
	/***
	Sets the user desired upper and lower range of humidity for the sensor to maintain upto
	@param humidUpper The maximum humidity level of the desired humidity range
	@param humidLower The minimum humidity level of the desired humidity range
	*/
	public synchronized void setHumidityRange(double humidUpper, double humidLower) {
		this.humidityUpper = humidUpper;
		this.humidityLower = humidLower;		
	}
	
	/***
	Set the current humidity level of the greenhouse
	@param currHumid Humidity of the greenhouse to set to
	@throws Exception A custom error that prompts the user if desired humdity is beyond the threshold range. 
	*/
	public synchronized void setCurrentHumidity(double currHumid) throws Exception {
		if (currHumid >= MINHUMIDITY && currHumid <= MAXHUMIDITY) {
			this.currentHumidity = currHumid;
		} else {
			throw new Exception("Humidity Out of Bounds. Please enter a value between 0% and 100%");
		}
	}
	
	/***
	Set the current rate at which the humidifier raises the humidity of the greenhouse
	@param rate Rate of increase in humidity (%/min)
	@throws Exception Prompt the user that humidity rate of change can only be positive because there is no dehumidifier
	*/
	public synchronized void setInternalHumidRate(double rate) throws Exception {
		if (rate > 0) { 
			this.internalHumidityRate = rate;
		} else {
			throw new Exception("Humidity Rate must be a positive number.");
		}
	}
	
	// GET
	
	/***
	Returns the current humidity of the environment as picked up by the humidity sensor
	@return currentHumidity Current greenhouse humidity level
	*/
	public synchronized double getCurrentHumidity() {
		return this.currentHumidity;
	}
	
	/***
	Returns whether or not the humidifier has been turned on or off by the sensor system
	@return A message to display on the GUI (On/Off)
	*/
	public synchronized String getDeviceStatus(){
		if (this.humidifier == 1) {
			return "On";
		}
		return "Off";
	}
	
	// PROCESS
	
	/***
	This is the sensor method to check the humidity level of the greenhouse and update the humidity level according 
	to the rate of change that the user has specified. It also controls the humidifier in order to increase(only) the 
	level of humidity to the desired range specified by the user. 
	*/
	public synchronized void sensor() {
		
		if (this.currentHumidity < this.humidityLower && this.currentHumidity >= MINHUMIDITY) {			
			this.humidifier = 1;			
			this.currentHumidity += this.internalHumidityRate;			
		} else if (this.currentHumidity > this.humidityUpper && this.currentHumidity <= MAXHUMIDITY) {
			this.humidifier = 0;	
		} else {
			this.humidifier = 0;
		}
		
	}
	
	/***
	Creates a well formated data string so that it can be stored into a simulation save file for retrieval 
	*/
	public String toString() {
		return "H," + this.currentHumidity + "," + (int)this.humidityUpper + "," + (int)this.humidityLower + "," + this.internalHumidityRate + "," + (int)this.humidifier + ",";
	}

}