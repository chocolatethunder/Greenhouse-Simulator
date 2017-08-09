/***

This Controller class is responsible for the Soil Moisture Controller/Sensor functionality. It sets up the
initial values for Greenhouse soil moisture and run the simulation on a separate thread.

@author Saurabh Tomar

Written for CPSC 233

*/

package marsbars;

import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;

/***
Controller responsible for the Soil Moisture Controller/Sensor functionality.
*/
public class MoistureController extends Controller {
	
	private MoistureModel moistModel;
	private MoistureView moistView;
	
	/***
	Contructor which takes the Soil Moisture model and GUI in order to pass values to and from the UI and processor
	@param model Soil Moisture model object. Calculations done by the Soil Moisture Sensor
	@param view Soil Moisture view object. Responsible for importing the Soil Moisture sensor GUI into the controller. 
	*/
	public MoistureController(MoistureModel model, MoistureView view) {
		this.moistModel = model;
		this.moistView 	= view;
	
		try {
			this.moistView.updateDesiredMoisture(new DesiredRangeUpdater());
			this.moistView.updateRefreshRateMoist(new MoistRefreshRateUpdater());
			this.moistView.updateMoistureRate(new MoistureRateUpdater());
		} catch (Exception e) {
			this.moistView.displayError("Error: " + e.getMessage());
		}
	}
	
	public class DesiredRangeUpdater implements ChangeListener {		
		/***
		Listens to whether the user has changed the desired greenhouse soil moisture range
		@param e The change event
		*/
		public void stateChanged(ChangeEvent e) throws NumberFormatException {			
			moistModel.setMoistureRange(moistView.getDesiredMoistureUpper(), moistView.getDesiredMoistureLower());				
		}			
	}
	
	public class MoistRefreshRateUpdater implements ChangeListener {		
		/***
		Listens to whether the user has changed the desired rate at which the sensor refreshes
		the calculations and updates the soil moisture sensor GUI
		@param e The change event
		*/
		public void stateChanged(ChangeEvent e) throws NumberFormatException {			
			setRefreshRate(moistView.getMoistRefreshRate());				
		}	
	}
	
	public class MoistureRateUpdater implements DocumentListener {
		public void changedUpdate(DocumentEvent e) throws NumberFormatException {
		}
		public void removeUpdate(DocumentEvent e) {	
		}
		/***
		Listens to whether the user has entered a new value at which the greenhouse soil moisture is changing
		@param e The change event
		*/
		public void insertUpdate(DocumentEvent e) {
			try {
				moistModel.setInternalMoistRate(moistView.getMoistureRate());
			} catch (NumberFormatException ex) {			
				moistView.displayError(ex.getMessage());			
			} catch (Exception ex) {			
				moistView.displayError("Error: " + ex.getMessage());			
			}
		}
	}
	
	/***
	This method takes the initial soil moisture change rate values and refresh rate from the soil moisture GUI panel and passes 
	them to the soil moisture model object for processing. This function is necesarry inorder to prevent the simulation from starting 
	if the user has entered incorrect starting values.
	@return Whether all values were correctly loaded into each sensor or not.
	*/
	public boolean setup() {
		
		if(!this.isOpening()) {
			try {

				this.moistModel.setInternalMoistRate(moistView.getMoistureRate());
				this.moistModel.setMoistureRange(moistView.getDesiredMoistureUpper(), moistView.getDesiredMoistureLower());
				this.setRefreshRate(moistView.getMoistRefreshRate());
				return true;
				
			} catch (NumberFormatException e) {			
				this.moistView.displayError(e.getMessage());			
			} catch (Exception e) {			
				this.moistView.displayError("Error: " + e.getMessage());			
			}
			return false;
		} else {
			return true;
		}
	}
	
	/***
	This method starts the thread in real simulation or simulation playback mode. It disables the necessary GUI elements given 
	the simulation mode, reads data from a simulation file, or write simulation to a user specified file. It takes the user specified values 
	of the greenhouse soil moisture and processing them at the user specified refresh rate. 
	*/
	public void run() {
		
		// If the thread is supposed to run in simulation playback mode
		if (this.isOpening()) {
			
			// Disable the inputs in order to playback the simulation safely. 
			this.moistView.enableGUI(false);
			
			try {
				
				String line = null;
				
				// Read an independently created Buffer object of the simulation data file to read
				BufferedReader readFileObj = this.getFileToRead();
				
				while ((line = readFileObj.readLine()) != null) {
					
					String[] dataArray = line.split(",");
					
					if (dataArray[0].equals("M")) {
						
						// Display the data on the GUI.
						this.moistView.setCurrentMoisture(Double.parseDouble(dataArray[1]));
						this.moistView.setDesiredMoistUpper(Double.parseDouble(dataArray[2]));
						this.moistView.setDesiredMoistLower(Double.parseDouble(dataArray[3]));
						this.moistView.setMoistureRate(Double.parseDouble(dataArray[4]));
						
						if (dataArray[5].equals(1)) {
							this.moistView.setDevice("On");
						} else {
							this.moistView.setDevice("Off");
						}
						
						this.moistView.setRefreshRate(Double.parseDouble(dataArray[6]));
						
						// Simulate the delay
						waitProcess(1000*Integer.parseInt(dataArray[6]));
						
					}					
					
				}
				
			} catch (IOException e) {
				this.moistView.displayError(e.getMessage());
			} catch (NumberFormatException e) {
				this.moistView.displayError("Incorrect Data. File might be corrupted.");
			} catch (Exception e) {				
				this.moistView.displayError(e.getMessage());
			}
			
		// If the thread is supposed to run the simulation in regular manner.
		} else {
			
			// Run perpetually
			while(true) {
				
				if (this.isSaving()) {
					try {
						this.writeToFile(this.moistModel.toString() + (this.getRefreshRate()/1000) + "\n");
					} catch (IOException e) {
						this.moistView.displayError(e.getMessage());
					}
				}
				
				// Process the current greenhouse soil moisture via the sensor. 
				this.moistModel.sensor();
				this.moistView.setCurrentMoisture(this.moistModel.getCurrentMoisture());
				this.moistView.setDevice(this.moistModel.getDeviceStatus());
				
				// Wait to refresh
				waitProcess(this.getRefreshRate());
			
			}
			
		}	
		
	}

}