/***

Environment Model is responsible for calculations that are required to simulate the effect of external temp, 
humidty, and soil moisture on the greenhouse environment. 

@author Saurabh Tomar

Written for CPSC 233

*/

package marsbars;

/***
Model responsible for calculations that are required to simulate the effect of external effects on the greenhouse.
*/
public class EnvironmentModel {
	
	private double startTemp = 0.0;
	private double currTemp = 0.0;
	private double externalTempRate = 0.0;
	
	private double startHumid = 0.0;
	private double currHumid = 0.0;
	private double externalHumidRate = 0.0;
	
	private double startMoist = 0.0;
	private double currMoist = 0.0;
	private double externalMoistRate = 0.0;
	
	/**
	Empty Constructor
	*/
	public EnvironmentModel() {
		
	}
	
	// GET
	
	/***
	Returns the current temperature of the greenhouse after being affected by external effects
	@return currTemp Current temperature of GreenHouse
	*/
	public synchronized double getCurrentTemp() {
		return this.currTemp;
	}
	
	/***
	Returns the rate at which the external environment is affecting the temperature of the greenhouse
	i.e. +3 degrees per minute or -4 degrees per min.
	@return externalTempRate Rate at which the external temperature is affecting the greenhouse
	*/
	public synchronized double getExternalTempRate() {
		return this.externalTempRate;
	}
	
	/***
	Returns the current humidity of the greenhouse after being affected by external effects
	@return currHumid Current humidity of GreenHouse
	*/
	public synchronized double getCurrentHumid() {
		return this.currHumid;
	}
	
	/***
	Returns the rate at which the external environment is affecting the humidity of the greenhouse
	i.e. +3% per minute or -4% per min.
	@return externalHumidRate Rate at which the external humidity is affecting the greenhouse
	*/
	public synchronized double getExternalHumidRate() {
		return this.externalHumidRate;
	}
	
	/***
	Returns the current soil moisture of the greenhouse after being affected by external effects
	@return currMoist Current soil moisture of GreenHouse
	*/
	public synchronized double getCurrentMoist() {
		return this.currMoist;
	}
	
	/***
	Returns the rate at which the external environment is affecting the soil moisture of the greenhouse
	i.e. +3% per minute or -4% per min.
	@return externalMoistRate Rate at which the external humidity is affecting the greenhouse
	*/
	public synchronized double getExternalMoistRate() {
		return this.externalMoistRate;
	}
	
	// SET
	
	/***
	Get the current temperature from the Temperature sensor and set the current temperature or 
	the starting temperature depending on whether this is the begining of the simulation or not.
	@param temp Current greenhouse temperature (Initially specified by the user, then by Temperature sensor throughout the simulation)
	@param start Specifies whether current temperature incoming is from the start of the program or not
	*/
	public synchronized void setCurrentTemp(double temp, boolean start) {
		this.currTemp = temp;
		if (start) {
			this.startTemp = temp;
		}
	}
	
	/***
	Sets the current rate at which the greenhouse temperature is changing because of external effects
	@param tempRate External temperature effect rate on greenhouse
	*/
	public synchronized void setExternalTempRate(double tempRate) {
		this.externalTempRate = tempRate;
	}
	
	/***
	Get the current humidity from the Humidity sensor and set the current humidity or 
	the starting humidity depending on whether this is the begining of the simulation or not.
	@param humid Current greenhouse humidity (Intially specified by the user, then by Humidity sensor throughout the simulation)
	@param start Specifies whether current humidity incoming is from the start of the program or not
	*/
	public synchronized void setCurrentHumid(double humid, boolean start) {
		this.currHumid = humid;
		if (start) {
			this.startHumid = humid;
		}
	}
	
	/***
	Sets the current rate at which the greenhouse humidity is changing because of external effects
	@param humidRate External humidity effect rate on greenhouse
	*/
	public synchronized void setExternalHumidRate(double humidRate) {
		this.externalHumidRate = humidRate;
	}
	
	/***
	Get the current soil moisture from the Soil Moisture sensor and set the current soil moisture or 
	the starting soil moisture depending on whether this is the begining of the simulation or not.
	@param moist Current greenhouse soil moisture (Intially specified by the user, then by Soil Moisture sensor throughout the simulation)
	@param start Specifies whether current soil moisture incoming is from the start of the program or not
	*/
	public synchronized void setCurrentMoist(double moist, boolean start) {
		this.currMoist = moist;
		if (start) {
			this.startMoist = moist;
		}
	}
	
	/***
	Sets the current rate at which the greenhouse soil moisture level is changing because of external effects
	@param moistRate External soil moisture level effect rate on greenhouse
	*/
	public synchronized void setExternalMoistRate(double moistRate) {
		this.externalMoistRate = moistRate;
	}
	
	// PROCESS 
	
	/***
	This method is responsible for updating the current greenhouse environment after the effects of external effects intelligently. Each environment property 
	of the greenhouse has a specified maximum and minimum threshold beyond which the external effects will have no effect. For example, the temperature cannot go
	below absolute zero and air cannot be saturated past 100% for humidity. 
	*/
	public synchronized void processEnv() {
		
		// Temperature 
		if ((this.currTemp + this.externalTempRate) >= TemperatureModel.ABSOLUTEZERO && (this.currTemp + this.externalTempRate) <= TemperatureModel.TOASTY) {
			this.currTemp += this.externalTempRate;
		} else if ((this.currTemp + this.externalTempRate) < TemperatureModel.ABSOLUTEZERO) {
			this.currTemp = TemperatureModel.ABSOLUTEZERO;
		} else if ((this.currTemp + this.externalTempRate) > TemperatureModel.TOASTY) {
			this.currTemp = TemperatureModel.TOASTY;
		}
		
		// Humidity
		if ((this.currHumid + this.externalHumidRate) >= HumidityModel.MINHUMIDITY && (this.currHumid + this.externalHumidRate) <= HumidityModel.MAXHUMIDITY) {
			this.currHumid += this.externalHumidRate;
		} else if ((this.currHumid + this.externalHumidRate) < HumidityModel.MINHUMIDITY) {
			this.currHumid = HumidityModel.MINHUMIDITY;
		} else if ((this.currHumid + this.externalHumidRate) > HumidityModel.MAXHUMIDITY) {
			this.currHumid = HumidityModel.MAXHUMIDITY;
		}
		
		// Soil Moisture
		if ((this.currMoist + this.externalMoistRate) >= MoistureModel.MINMOISTURE && (this.currMoist + this.externalMoistRate) <= MoistureModel.MAXMOISTURE) {
			this.currMoist += this.externalMoistRate;
		} else if ((this.currMoist + this.externalMoistRate) < MoistureModel.MINMOISTURE) {
			this.currMoist = MoistureModel.MINMOISTURE;
		} else if ((this.currMoist + this.externalMoistRate) > MoistureModel.MAXMOISTURE) {
			this.currMoist = MoistureModel.MAXMOISTURE;
		}
		
	}
	
	/***
	Creates a well formated data string so that it can be stored into a simulation save file for retrieval 
	*/
	public String toString() {
		return "E," + this.startTemp + "," + this.startHumid + "," + this.startMoist + "," + this.externalTempRate + "," + this.externalHumidRate + "," + this.externalMoistRate + ",";
	}

}