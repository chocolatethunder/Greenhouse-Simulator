/***

This Controller class is responsible for the Temperature Controller/Sensor functionality. It sets up the
initial values for Greenhouse temperature and run the simulation on a separate thread.

@author Saurabh Tomar

Written for CPSC 233

*/

package marsbars;

import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;

/***
Controller responsible for the Temperature Controller/Sensor functionality.
*/
public class TemperatureController extends Controller {
	
	private TemperatureModel tempModel;
	private TemperatureView tempView;
	
	/***
	Contructor which takes the Temperature model and GUI in order to pass values to and from the UI and processor
	@param model Temperature model object. Calculations done by the Temperature Sensor
	@param view Temperature view object. Responsible for importing the Temperature sensor GUI into the controller. 
	*/
	public TemperatureController(TemperatureModel model, TemperatureView view) {
		this.tempModel 	= model;
		this.tempView 	= view;
	
		try {
			this.tempView.updateDesiredTemp(new DesiredRangeUpdater());
			this.tempView.updateRefreshRateTemp(new TempRefreshRateUpdater());
			this.tempView.updateHeatingRate(new HeatingRateUpdater());
			this.tempView.updateCoolingRate(new CoolingRateUpdater());
		} catch (Exception e) {
			this.tempView.displayError("Error: " + e.getMessage());
		}
	}
	
	public class DesiredRangeUpdater implements ChangeListener {		
		/***
		Listens to whether the user has changed the desired greenhouse temperature range
		@param e The change event
		*/
		public void stateChanged(ChangeEvent e) throws NumberFormatException {			
			tempModel.setTemperatureRange(tempView.desiredTempUpper(), tempView.desiredTempLower());				
		}			
	}
	
	public class TempRefreshRateUpdater implements ChangeListener {		
		/***
		Listens to whether the user has changed the desired rate at which the sensor refreshes
		the calculations and updates the temperature sensor GUI
		@param e The change event
		*/
		public void stateChanged(ChangeEvent e) throws NumberFormatException {			
			setRefreshRate(tempView.getTempRefreshRate());				
		}	
	}
	
	public class HeatingRateUpdater implements DocumentListener {
		public void changedUpdate(DocumentEvent e) throws NumberFormatException {
		}
		public void removeUpdate(DocumentEvent e) {	
		}
		/***
		Listens to whether the user has entered a new value at which the greenhouse is heating up
		@param e The change event
		*/
		public void insertUpdate(DocumentEvent e) {
			try {
				tempModel.setInternalHeatRate(tempView.getHeatingRate());
			} catch (NumberFormatException ex) {			
				tempView.displayError(ex.getMessage());			
			} catch (Exception ex) {			
				tempView.displayError("Error: " + ex.getMessage());			
			}
		}
	}
	
	public class CoolingRateUpdater implements DocumentListener {
		public void changedUpdate(DocumentEvent e) throws NumberFormatException {
		}
		public void removeUpdate(DocumentEvent e) {	
		}
		/***
		Listens to whether the user has entered a new value at which the greenhouse is cooling down
		@param e The change event
		*/
		public void insertUpdate(DocumentEvent e) {			
			try {
				tempModel.setInternalCoolRate(tempView.getCoolingRate());
			} catch (NumberFormatException ex) {			
				tempView.displayError(ex.getMessage());			
			} catch (Exception ex) {			
				tempView.displayError("Error: " + ex.getMessage());			
			}
		}
	}
	
	/***
	This method takes the initial temperature change rate values and refresh rate from the temperature GUI panel and passes 
	them to the temperature model object for processing. This function is necesarry inorder to prevent the simulation from starting 
	if the user has entered incorrect starting values.
	@return Whether all values were correctly loaded into each sensor or not.
	*/
	public boolean setup() {
		
		if (!this.isOpening()) {
			try {

				this.tempModel.setInternalCoolRate(tempView.getCoolingRate());
				this.tempModel.setInternalHeatRate(tempView.getHeatingRate());
				this.tempModel.setTemperatureRange(tempView.desiredTempUpper(), tempView.desiredTempLower());
				this.setRefreshRate(tempView.getTempRefreshRate());
				return true;
				
			} catch (NumberFormatException e) {			
				this.tempView.displayError(e.getMessage());			
			} catch (Exception e) {			
				this.tempView.displayError("Error: " + e.getMessage());			
			}
			return false;
		} else {
			return true;
		}
	}
	
	/***
	This method starts the thread in real simulation or simulation playback mode. It disables the necessary GUI elements given 
	the simulation mode, reads data from a simulation file, or write simulation to a user specified file. It takes the user specified values 
	of the greenhouse temperature and processing them at the user specified refresh rate. 
	*/
	public void run() {		
		
		// If the thread is supposed to run in simulation playback mode
		if (this.isOpening()) {
			
			// Disable the inputs in order to playback the simulation safely. 
			this.tempView.enableGUI(false);
			
			try {
				
				String line = null;
				
				// Read an independently created Buffer object of the simulation data file to read
				BufferedReader readFileObj = this.getFileToRead();
				
				while ((line = readFileObj.readLine()) != null) {
					
					String[] dataArray = line.split(",");
					
					if (dataArray[0].equals("T")) {
						
						// Display the data on the GUI.
						this.tempView.setCurrTemp(Double.parseDouble(dataArray[1]));
						this.tempView.setDesiredUpperTemp(Double.parseDouble(dataArray[2]));
						this.tempView.setDesiredLowerTemp(Double.parseDouble(dataArray[3]));
						this.tempView.setHeatingRate(Double.parseDouble(dataArray[4]));
						this.tempView.setCoolingRate(Double.parseDouble(dataArray[5]));
						
						if (dataArray[6].equals(1)) {
							this.tempView.setDevice("Heating");
						} else if (dataArray[7].equals(1)) {
							this.tempView.setDevice("Cooling");
						} else {
							this.tempView.setDevice("Off");
						}
						
						this.tempView.setRefreshRate(Double.parseDouble(dataArray[8]));
						
						// Simulate the delay
						waitProcess(1000*Integer.parseInt(dataArray[8]));
						
					}					
					
				}
				
			} catch (IOException e) {
				this.tempView.displayError(e.getMessage());
			} catch (NumberFormatException e) {
				this.tempView.displayError("Incorrect Data. File might be corrupted.");
			} catch (Exception e) {				
				this.tempView.displayError(e.getMessage());
			}
		
		// If the thread is supposed to run the simulation in regular manner.
		} else {
			
			// Run perpetually
			while(true) {
				
				if (this.isSaving()) {
					try {
						this.writeToFile(this.tempModel.toString() + (this.getRefreshRate()/1000) + "\n");
					} catch (IOException e) {
						this.tempView.displayError(e.getMessage());
					}
				}
				
				// Process the current greenhouse temperature via the sensor.
				this.tempModel.sensor();
				this.tempView.setCurrTemp(this.tempModel.getCurrentTemp());
				this.tempView.setDevice(this.tempModel.getDeviceStatus());
				
				// Wait to refresh
				waitProcess(this.getRefreshRate());
				
			}
			
		}			
		
	}
	
}