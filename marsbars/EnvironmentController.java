/***

This Controller class is responsible for the Greenhouse Environment start parameters and the external effects
on the Greenhouse. This controller manages the interaction between Environment Model and it's View and updates 
the Environment temp, moisture, and humidity parameters at user specified refresh rates.

@author Saurabh Tomar

Written for CPSC 233

*/

package marsbars;

import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;

/***
Responsible for the Greenhouse Environment start parameters and the external effects on the Greenhouse.
*/
public class EnvironmentController extends Controller {
	
	private EnvironmentModel envModel;
	private EnvironmentView envView;
	
	private TemperatureModel tempModel;
	private HumidityModel humidModel;
	private MoistureModel moistModel;
	
	/***
	Contructor that imports the model objects that are needed to manipulate the current temp, moisture, and humidity of the greenhouse.
	@param model The Environment Model that contains all the methods to manipulate the Greenhouse environment
	@param view The GUI for the Environment sensor
	@param tmodel Temperature methods that are responsible for temperature sensor function
	@param hmodel Humidity methods that are responsible for humidity sensor function
	@param mmodel Soil Moisture methods that are responsible for soil moisture sensor function
	*/
	public EnvironmentController(EnvironmentModel model, EnvironmentView view, TemperatureModel tmodel, HumidityModel hmodel, MoistureModel mmodel) {
		this.envModel 	= model;
		this.envView 	= view;
		this.tempModel	= tmodel; 
		this.humidModel	= hmodel;
		this.moistModel	= mmodel;

		try {
			
			// Pass the appropriate objects to the GUI to update on change
			this.envView.updateRefreshRate(new UpdateRefreshRateEnv());
			this.envView.updateExtTempRate(new UpdateExternalTempRate());
			this.envView.updateExtHumidRate(new UpdateExternalHumidRate());
			this.envView.updateExtMoistRate(new UpdateExternalMoistRate());
			
		} catch (Exception e) {
			this.envView.displayError("Error: " + e.getMessage());
		}
	}
	
	public class UpdateRefreshRateEnv implements ChangeListener {
		/***
		Listens to whether the user has changed the refresh rate at which this controller updates other
		three sensor's data from the external effects
		@param e The change event
		*/
		public void stateChanged(ChangeEvent e) throws NumberFormatException {			
			setRefreshRate(envView.getRefreshRate());				
		}	
	}
	
	public class UpdateExternalTempRate implements ChangeListener {	
		/***
		Listens to whether the user has changed the external effects affecting the greenhouse temperature
		@param e The change event
		*/	
		public void stateChanged(ChangeEvent e) throws NumberFormatException {			
			envModel.setExternalTempRate(envView.getExtTempEffect());				
		}	
	}
	
	public class UpdateExternalHumidRate implements ChangeListener {
		/***
		Listens to whether the user has changed the external effects affecting the greenhouse humidity
		@param e The change event
		*/	
		public void stateChanged(ChangeEvent e) throws NumberFormatException {			
			envModel.setExternalHumidRate(envView.getExtHumidEffect());				
		}	
	}
	
	public class UpdateExternalMoistRate implements ChangeListener {
		/***
		Listens to whether the user has changed the external effects affecting the greenhouse soil moisture content
		@param e The change event
		*/			
		public void stateChanged(ChangeEvent e) throws NumberFormatException {			
			envModel.setExternalMoistRate(envView.getExtMoistEffect());				
		}	
	}
	
	/***
	This method initializes the values and sends them to the environment model and each sensor's model respectively where
	they will be processed upon running the simulation. This function is necesarry inorder to prevent the simulation from starting 
	if the user has entered incorrect starting values.
	@return Whether all values were correctly loaded into each sensor or not.
	*/	
	public boolean setup() {
		
		if (!this.isOpening()) {			
			try {
				
				// The the environment controller with the initial values 
				this.envModel.setCurrentTemp(envView.getStartTemp(),true);
				this.envModel.setCurrentHumid(envView.getStartHumidity(),true);
				this.envModel.setCurrentMoist(envView.getStartMoisture(),true);
				
				// Set each senson with the respective initial values
				this.tempModel.setCurrentTemperature(envView.getStartTemp());
				this.humidModel.setCurrentHumidity(envView.getStartHumidity());
				this.moistModel.setCurrentMoisture(envView.getStartMoisture());
				
				this.setRefreshRate(envView.getRefreshRate());
				return true;
				
			} catch (NumberFormatException e) {			
				this.envView.displayError(e.getMessage());			
			} catch (Exception e) {			
				this.envView.displayError("Error: " + e.getMessage());			
			}
			return false;		
		} else {			
			return true;			
		}
	}
	
	/***
	This method starts the thread in real simulation or simulation playback mode. It disables the necessary GUI elements given 
	the simulation mode, reads data from a simulation file, or write simulation to a user specified file. It also takes and updates 
	each sensor's current temp, humidity, and moisture settings, updates with with the external effects affecting the greenhouse and updates 
	each sensor's data for the greenhouse. 
	*/
	public void run() {
		
		// Disables the input field for start values so the user doesn't cause havok
		this.envView.editable(false);
		
		// If the thread is supposed to run in simulation playback mode 
		if (this.isOpening()) {
			
			// Disable the inputs in order to playback the simulation safely. 
			this.envView.enableGUI(false);
			
			try {
				
				String line = null;
				
				// Read an independently created Buffer object of the simulation data file to read
				BufferedReader readFileObj = this.getFileToRead();
				
				while ((line = readFileObj.readLine()) != null) {
					
					String[] dataArray = line.split(",");
					
					if (dataArray[0].equals("E")) {
						
						// Display the data on the GUI.
						this.envView.setStartTemp(Double.parseDouble(dataArray[1]));
						this.envView.setStartHumid(Double.parseDouble(dataArray[2]));
						this.envView.setStartMoist(Double.parseDouble(dataArray[3]));
						
						this.envView.setExtTempEffect(Double.parseDouble(dataArray[4]));
						this.envView.setExtHumidEffect(Double.parseDouble(dataArray[5]));
						this.envView.setExtMoistEffect(Double.parseDouble(dataArray[6]));						
						
						this.envView.setRefreshRate(Double.parseDouble(dataArray[7]));
						
						// Simulate the delay
						waitProcess(1000*Integer.parseInt(dataArray[7]));
						
					}					
					
				}
				
			} catch (IOException e) {
				this.envView.displayError(e.getMessage());
			} catch (NumberFormatException e) {
				this.envView.displayError("Incorrect Data. File might be corrupted.");
			} catch (Exception e) {				
				this.envView.displayError(e.getMessage());
			}
		
		// If the thread is supposed to run the simulation in regular manner.
		} else {
			
			// Run perpetually
			while(true) {
				
				// Capture the data from the each sensor's model to update it's current settings
				this.envModel.setCurrentTemp(tempModel.getCurrentTemp(),false);
				this.envModel.setCurrentHumid(humidModel.getCurrentHumidity(),false);
				this.envModel.setCurrentMoist(moistModel.getCurrentMoisture(),false);
				
				// Change the current environment settings given the user specified external effects.
				this.envModel.processEnv();
				
				try {
					
					// Checks if in save mode and writes to the file.
					if (this.isSaving()) {
						try {
							this.writeToFile(this.envModel.toString() + (this.getRefreshRate()/1000) + "\n");
						} catch (IOException e) {
							this.envView.displayError(e.getMessage());
						}
					}
					
					// Update each sensor with new information updated after the external effects have been applied.
					this.tempModel.setCurrentTemperature(this.envModel.getCurrentTemp());
					this.humidModel.setCurrentHumidity(this.envModel.getCurrentHumid());
					this.moistModel.setCurrentMoisture(this.envModel.getCurrentMoist());
					
					
				} catch (NumberFormatException e) {			
					this.envView.displayError(e.getMessage());			
				} catch (Exception e) {			
					this.envView.displayError("Error: " + e.getMessage());			
				}
				
				// Wait to refresh
				waitProcess(this.getRefreshRate());
				
			}
			
		}	
		
	}

}