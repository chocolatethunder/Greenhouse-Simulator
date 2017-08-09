/***

Temperature View contains all the GUI elements for the Greenhouse control panel in the main GUI. 
Uses JPanel to organize all the elements. 

@author Saurabh Tomar

Written for CPSC 233

*/

package marsbars;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

/***
GUI elements for the Greenhouse temperature control panel 
*/
public class TemperatureView extends JPanel {
	
	// Universals

	private JLabel currentTempLabel = new JLabel("Current Temperature (\u00b0C)");
	private JTextField currentTemp 	= new JTextField(5);
	private JLabel prefTemplabel 	= new JLabel("Preferred Temperature Range (\u00b0C)");
	private RangeSlider tempRange 	= new RangeSlider();	
	private JLabel coolingRateLabel = new JLabel("Cooling Rate (\u00b0C/min)");
	private JTextField coolingRate 	= new JTextField();
	private JLabel heatingRateLabel = new JLabel("Heating Rate (\u00b0C/min)");
	private JTextField heatingRate 	= new JTextField();
	private JLabel refreshRateLabel = new JLabel("Refresh Rate (Sec)");	
	private JSlider tempRefresh 	= new JSlider(JSlider.HORIZONTAL, 1, 10, 5);
	private JLabel controllerStatus = new JLabel("Off");
	
	/***
	Contructor that sets the basic GUI elements for the Panel
	*/
	public TemperatureView() {	
		
		// Set the padding of the Panel
		this.setBorder(new EmptyBorder(10, 10, 10, 10));		
		this.setLayout(new GridLayout(5,1,0,0));
		
		// Setup fonts
		Font fontTextField = new Font("Arial",Font.BOLD,24);
		Font fontDeviceStatus = new Font("Arial",Font.BOLD,18);
		
		currentTemp.setFont(fontTextField);
		coolingRate.setFont(fontTextField);
		heatingRate.setFont(fontTextField);		
		controllerStatus.setFont(fontDeviceStatus);
		
		JPanel panel1 = new JPanel(new GridLayout(2,1));	
		JPanel panel2 = new JPanel(new GridLayout(2,1));
		
		JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));		
		JPanel panel3_sub1 = new JPanel(new GridLayout(2,1));
		JPanel panel3_sub2 = new JPanel(new GridLayout(2,1));
		
		JPanel panel4 = new JPanel(new GridLayout(2,1));		
		JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
		// Current Temperature level display
		currentTemp.setEditable(false);
		panel1.add(currentTempLabel);
		currentTemp.setHorizontalAlignment(JTextField.CENTER);
		panel1.add(currentTemp);		
		
		// Temperature range slider
		panel2.add(prefTemplabel);
		this.tempRange.setMinimum(10);
		this.tempRange.setMaximum(70);
		this.tempRange.setMinorTickSpacing(2);
		this.tempRange.setMajorTickSpacing(10);
		this.tempRange.setPaintTicks(true);
		this.tempRange.setPaintLabels(true);
		panel2.add(tempRange);

		panel3_sub1.add(coolingRateLabel);
		panel3_sub1.add(coolingRate);
		
		panel3_sub2.add(heatingRateLabel);		
		panel3_sub2.add(heatingRate);
		
		panel3.add(panel3_sub1);
		panel3.add(panel3_sub2);
		
		// Refresh rate of the temperature sensor
		panel4.add(refreshRateLabel);
		this.tempRefresh.setValue(2);
		this.tempRefresh.setMinorTickSpacing(1);
		this.tempRefresh.setMajorTickSpacing(1);
		this.tempRefresh.setPaintTicks(true);
		this.tempRefresh.setPaintLabels(true);
		panel4.add(tempRefresh);		
		
		// Display furnace or air conditioner status
		panel5.add(controllerStatus);
		panel5.setBorder(new EmptyBorder(25, 0, 0, 0));
		
		add(panel1);
		add(panel2);
		add(panel3);
		add(panel4);
		add(panel5);
		
	}
	
	// GET
	
	/***
	Get the current temperature level of the greenhouse convert it into a double, and return it.
	@return The current greenhouse temperature
	*/
	public double getCurrentTemp() {
		try { 
			return Double.parseDouble(this.currentTemp.getText());
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Please enter a valid current temperature");
		}
	}
	
	/***
	Get the maximum temperature level of the temperature range as specified by the user
	@return Maximum temperature level that needs to be maintained
	*/
	public double desiredTempLower() {
		return tempRange.getValue();
	}
	
	/***
	Get the minimum temperature level of the temperature range as specified by the user
	@return Minimum temperature level that needs to be maintained
	*/
	public double desiredTempUpper() {
		return tempRange.getUpperValue();
	}
	
	/***
	Get the current rate at which the air conditioner decreases the temperature specified by user in the GUI
	@return The user specified temperature rate of change level (C/min)
	*/
	public double getCoolingRate() {
		try { 
			return Double.parseDouble(this.coolingRate.getText());
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Please enter a valid cooling rate");
		}
	}
	
	/***
	Get the current rate at which the furnace increases the temperature specified by user in the GUI
	@return The user specified temperature rate of change level (C/min)
	*/
	public double getHeatingRate() {
		try { 
			return Double.parseDouble(this.heatingRate.getText());
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Please enter a valid heating rate");
		}
	}
	
	/***
	Returns the rate at which the current thread refreshes the greenhouse temperature data
	@return Thread refresh rate
	*/
	public int getTempRefreshRate() {
		return this.tempRefresh.getValue();
	}

	
	// SET
	
	/***
	Display the specidifed temperature level on the GUI.
	@param temp Temperature level needed to display
	*/
	public void setCurrTemp(double temp) {
		this.currentTemp.setText(String.format("%.2f", temp));
	}
	
	/***
	For simulation playback. Display the maximum temperature level on the range slider
	@param temp Max temperature level needed to display
	*/
	public void setDesiredUpperTemp(double temp) {
		this.tempRange.setUpperValue((int)temp);
	}
	
	/***
	For simulation playback. Display the minimum temperature level on the range slider
	@param temp Min temperature level needed to display
	*/
	public void setDesiredLowerTemp(double temp) {
		this.tempRange.setValue((int)temp);
	}
	
	/***
	For simulation playback. Display the rate at which the air conditioner decreases the temperature in the GUI
	@param rate Temperature change rate in C/Min
	*/
	public void setCoolingRate(double rate) {
		this.coolingRate.setText(String.format("%.2f", rate));
	}
	
	/***
	For simulation playback. Display the rate at which the furnace increases the temperature in the GUI
	@param rate Temperature change rate in C/Min
	*/
	public void setHeatingRate(double rate) {
		this.heatingRate.setText(String.format("%.2f", rate));
	}
	
	/***
	For simulation playback. Display the rate for which the temperature GUI needs to update
	@param rate Temperature GUI refresh rate
	*/
	public void setRefreshRate(double rate) {
		this.tempRefresh.setValue((int)rate);
	}
	
	/***
	Display the status of the furnace of air conditioner in the GUI
	@param status Status of the furnace or air conditioner (Heating/Cooling/Off)
	*/
	public void setDevice(String status) {
		this.controllerStatus.setText(status);
	}
	
	/***
	Disables the Temperature GUI for the purposes of saved simulation playback.
	@param value Disable the GUI Elements or not.
	*/
	public void enableGUI(boolean value) {
		this.coolingRate.setEditable(value);
		this.heatingRate.setEditable(value);
		this.tempRange.setEnabled(value);
		this.tempRefresh.setEnabled(value);
	}
	
	// LISTENERS
	
	/***
	Listens to see if the user has changed the range of temperature level for the
	sensor to maintain in the greenhouse.
	@param theListener The object that is executes the changes are triggered.
	*/
	public void updateDesiredTemp(ChangeListener theListener) {
		tempRange.addChangeListener(theListener);
	}
	
	/***
	Listens to see if the user has changed the rate at which furnace or air conditioner increases
	the temperature of the greenhouse
	@param theListener The object that is executes the changes are triggered.
	*/
	public void updateRefreshRateTemp(ChangeListener theListener) {
		tempRefresh.addChangeListener(theListener);
	}
	
	/***
	Listens to see if the user has changed the rate at which furnace increases
	the temperature of the greenhouse
	@param theListener The object that is executes the changes are triggered.
	*/
	public void updateHeatingRate(DocumentListener theListener) {
		heatingRate.getDocument().addDocumentListener(theListener);
	}
	
	/***
	Listens to see if the user has changed the rate at which air conditioner decreases
	the temperature of the greenhouse
	@param theListener The object that is executes the changes are triggered.
	*/
	public void updateCoolingRate(DocumentListener theListener) {
		coolingRate.getDocument().addDocumentListener(theListener);
	}
	
	// ERROR Display
	
	/***
	Opens a dialog box with the error message specified for the Moisture Control Panel
	@param errorMsg The error message
	*/
	public void displayError(String errorMsg) {
		JOptionPane.showMessageDialog(this,errorMsg);
	}
	
}