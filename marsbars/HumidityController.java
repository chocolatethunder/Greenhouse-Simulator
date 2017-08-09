/***

This Controller class is responsible for the Humidity Controller/Sensor functionality. It sets up the
initial values for Greenhouse humidity and run the simulation on a separate thread.

@author Saurabh Tomar

Written for CPSC 233

*/

package marsbars;

import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;

/***
Controller responsible for the Humidity Controller/Sensor functionality.
*/
public class HumidityController extends Controller {
	
	private HumidityModel humidModel;
	private HumidityView humidView;
	
	/***
	Contructor which takes the Humidity model and GUI in order to pass values to and from the UI and processor
	@param model Humidity model object. Calculations done by the Humidity Sensor
	@param view Humidity view object. Responsible for importing the Humidity sensor GUI into the controller. 
	*/
	public HumidityController(HumidityModel model, HumidityView view) {
		this.humidModel = model;
		this.humidView 	= view;
	
		try {
			this.humidView.updateDesiredHumidity(new DesiredRangeUpdater());
			this.humidView.updateRefreshRateHumid(new HumidRefreshRateUpdater());
			this.humidView.updateHumidityRate(new HumidityRateUpdater());
		} catch (Exception e) {
			this.humidView.displayError("Error: " + e.getMessage());
		}
	}
	
	public class DesiredRangeUpdater implements ChangeListener {		
		/***
		Listens to whether the user has changed the desired greenhouse humidity range
		@param e The change event
		*/
		public void stateChanged(ChangeEvent e) throws NumberFormatException {			
			humidModel.setHumidityRange(humidView.getDesiredHumidityUpper(), humidView.getDesiredHumidityLower());				
		}			
	}
	
	public class HumidRefreshRateUpdater implements ChangeListener {		
		/***
		Listens to whether the user has changed the desired rate at which the sensor refreshes
		the calculations and updates the humidity sensor GUI
		@param e The change event
		*/
		public void stateChanged(ChangeEvent e) throws NumberFormatException {			
			setRefreshRate(humidView.getHumidRefreshRate());				
		}	
	}
	
	public class HumidityRateUpdater implements DocumentListener {
		public void changedUpdate(DocumentEvent e) throws NumberFormatException {
		}
		public void removeUpdate(DocumentEvent e) {	
		}
		/***
		Listens to whether the user has entered a new value at which the greenhouse humidity is changing
		@param e The change event
		*/
		public void insertUpdate(DocumentEvent e) {
			try {
				humidModel.setInternalHumidRate(humidView.getHumidityRate());
			} catch (NumberFormatException ex) {			
				humidView.displayError(ex.getMessage());			
			} catch (Exception ex) {			
				humidView.displayError("Error: " + ex.getMessage());			
			}
		}
	}
	
	/***
	This method takes the initial humidity change rate values and refresh rate from the humidity GUI panel and passes 
	them to the humidity model object for processing. This function is necesarry inorder to prevent the simulation from starting 
	if the user has entered incorrect starting values.
	@return Whether all values were correctly loaded into each sensor or not.
	*/	
	public boolean setup() {
		
		if(!this.isOpening()) {
			try {

				this.humidModel.setInternalHumidRate(humidView.getHumidityRate());
				this.humidModel.setHumidityRange(humidView.getDesiredHumidityUpper(), humidView.getDesiredHumidityLower());
				this.setRefreshRate(humidView.getHumidRefreshRate());
				return true;
				
			} catch (NumberFormatException e) {			
				this.humidView.displayError(e.getMessage());			
			} catch (Exception e) {			
				this.humidView.displayError("Error: " + e.getMessage());			
			}
			return false;
		} else {
			return true;
		}
	}
	
	/***
	This method starts the thread in real simulation or simulation playback mode. It disables the necessary GUI elements given 
	the simulation mode, reads data from a simulation file, or write simulation to a user specified file. It takes the user specified values 
	of the greenhouse humidity and processing them at the user specified refresh rate. 
	*/
	public void run() {
		
		// If the thread is supposed to run in simulation playback mode
		if (this.isOpening()) {
			
			// Disable the inputs in order to playback the simulation safely. 
			this.humidView.enableGUI(false);
			
			try {
				
				String line = null;
				
				// Read an independently created Buffer object of the simulation data file to read
				BufferedReader readFileObj = this.getFileToRead();
				
				while ((line = readFileObj.readLine()) != null) {
					
					String[] dataArray = line.split(",");
					
					if (dataArray[0].equals("H")) {
						
						// Display the data on the GUI.
						this.humidView.setCurrentHumidity(Double.parseDouble(dataArray[1]));
						this.humidView.setDesiredHumidUpper(Double.parseDouble(dataArray[2]));
						this.humidView.setDesiredHumidLower(Double.parseDouble(dataArray[3]));
						this.humidView.setHumidityRate(Double.parseDouble(dataArray[4]));
						
						if (dataArray[5].equals(1)) {
							this.humidView.setDevice("On");
						} else {
							this.humidView.setDevice("Off");
						}
						
						this.humidView.setRefreshRate(Double.parseDouble(dataArray[6]));
						
						// Simulate the delay
						waitProcess(1000*Integer.parseInt(dataArray[6]));
						
					}					
					
				}
				
			} catch (IOException e) {
				this.humidView.displayError(e.getMessage());
			} catch (NumberFormatException e) {
				this.humidView.displayError("Incorrect Data. File might be corrupted.");
			} catch (Exception e) {				
				this.humidView.displayError(e.getMessage());
			}
		
		// If the thread is supposed to run the simulation in regular manner.
		} else {
			
			// Run perpetually
			while(true) {
				
				if (this.isSaving()) {
					try {
						this.writeToFile(this.humidModel.toString() + (this.getRefreshRate()/1000) + "\n");
					} catch (IOException e) {
						this.humidView.displayError(e.getMessage());
					}
				}
				
				// Process the current greenhouse humidity via the sensor. 
				this.humidModel.sensor();
				this.humidView.setCurrentHumidity(this.humidModel.getCurrentHumidity());
				this.humidView.setDevice(this.humidModel.getDeviceStatus());
				
				// Wait to refresh
				waitProcess(this.getRefreshRate());
			
			}
			
		}	
		
	}
	
}