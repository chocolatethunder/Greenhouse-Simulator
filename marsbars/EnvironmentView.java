/***

Environment View contains all the GUI elements for the Greenhouse environment external 
effects panel in the main GUI. Uses JPanel to organize all the elements. 

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
GUI elements for the Greenhouse environment external effects panel 
*/
public class EnvironmentView extends JPanel {
	
	// Universals

	private JLabel currTempLabel 	= new JLabel("Environment Temperature (\u00b0C)");
	private JTextField startTemp 	= new JTextField(5);
	private JLabel extTempLabel 	= new JLabel("Temperature Effect (\u00b0C/min)");
	private JSlider tempRange 		= new JSlider(JSlider.HORIZONTAL, -5, 5, 0);
	
	private JLabel currHumidLabel 	= new JLabel("Environment Humidity (%%)");
	private JTextField startHumid 	= new JTextField(5);
	private JLabel extHumidLabel 	= new JLabel("Humidity Effect (%/min)");
	private JSlider humidRange 		= new JSlider(JSlider.HORIZONTAL, -5, 5, 0);
	
	private JLabel currMoistLabel 	= new JLabel("Environment Soil Moisture (%%)");
	private JTextField startMoist 	= new JTextField(5);
	private JLabel extMoistLabel 	= new JLabel("Soil Moisture Effect (%/min)");
	private JSlider moistRange 		= new JSlider(JSlider.HORIZONTAL, -5, 5, 0);
	
	private JLabel refreshRateLabel = new JLabel("Refresh Rate (Sec)");	
	private JSlider refreshRate 	= new JSlider(JSlider.HORIZONTAL, 1, 10, 5);
	
	private Font fontTextField = new Font("Arial",Font.BOLD,24);
	
	/***
	Contructor that sets the basic GUI elements for the Panel
	*/
	public EnvironmentView() {	
		
		// Set the padding of the Panel
		this.setBorder(new EmptyBorder(10, 10, 10, 10));	
		this.setLayout(new GridLayout(5,1,10,0));

		// Setup fonts
		startTemp.setFont(fontTextField);
		startHumid.setFont(fontTextField);
		startMoist.setFont(fontTextField);		
		
		JPanel panel1 = new JPanel(new GridLayout(2,2));	
		JPanel panel2 = new JPanel(new GridLayout(2,2));
		JPanel panel3 = new JPanel(new GridLayout(2,2));		
		JPanel panel4 = new JPanel(new GridLayout(2,2));
		JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		// Temperature Inputs
		panel1.add(currTempLabel);		
		panel1.add(extTempLabel);		
		panel1.add(startTemp);
		this.tempRange.setMinorTickSpacing(1);
		this.tempRange.setMajorTickSpacing(1);
		this.tempRange.setPaintTicks(true);
		this.tempRange.setPaintLabels(true);
		panel1.add(tempRange);
	
		// Humidity Inputs
		panel2.add(currHumidLabel);		
		panel2.add(extHumidLabel);		
		panel2.add(startHumid);
		this.humidRange.setMinorTickSpacing(1);
		this.humidRange.setMajorTickSpacing(1);
		this.humidRange.setPaintTicks(true);
		this.humidRange.setPaintLabels(true);
		panel2.add(humidRange);
		
		// Soil Moisture Inputs
		panel3.add(currMoistLabel);		
		panel3.add(extMoistLabel);		
		panel3.add(startMoist);
		this.moistRange.setMinorTickSpacing(1);
		this.moistRange.setMajorTickSpacing(1);
		this.moistRange.setPaintTicks(true);
		this.moistRange.setPaintLabels(true);
		panel3.add(moistRange);	

		// Refresh rate of the current greenhouse environment 
		panel5.add(refreshRateLabel);
		panel5.add(new JLabel());
		this.refreshRate.setValue(2);
		this.refreshRate.setMinorTickSpacing(1);
		this.refreshRate.setMajorTickSpacing(1);
		this.refreshRate.setPaintTicks(true);
		this.refreshRate.setPaintLabels(true);
		panel5.add(refreshRate);
		panel5.add(new JLabel());
		
		add(panel1);
		add(panel2);
		add(panel3);
		add(panel4);
		add(panel5);
		
	}
	
	// GET
	
	/***
	Get the starting temperature of the greenhouse specified by the user, convert it into a double, and return it.
	@return The starting temperature specified by the user
	*/
	public double getStartTemp() {
		try { 
			return Double.parseDouble(this.startTemp.getText());
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Please enter a valid start temperature");
		}
	}
	
	/***
	Returns the rate at which the external temperature is affecting the internal greenhouse temperature
	@return External temperature effect rate
	*/
	public double getExtTempEffect() {
		return tempRange.getValue();
	}
	
	/***
	Get the starting humidity of the greenhouse specified by the user, convert it into a double, and return it.
	@return The starting humdity specified by the user
	*/
	public double getStartHumidity() {
		try { 
			return Double.parseDouble(this.startHumid.getText());
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Please enter a valid start humidity");
		}
	}
	
	/***
	Returns the rate at which the external humidity is affecting the internal greenhouse humidity
	@return External humidity effect rate
	*/
	public double getExtHumidEffect() {
		return humidRange.getValue();
	}
	
	/***
	Get the starting soil moisture level of the greenhouse specified by the user, convert it into a double, and return it.
	@return The starting soil moisture level specified by the user
	*/
	public double getStartMoisture() {
		try { 
			return Double.parseDouble(this.startMoist.getText());
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Please enter a valid start moisture");
		}
	}
	
	/***
	Returns the rate at which the external soil moisture is affecting the internal greenhouse soil moisture
	@return External soil moisture effect rate
	*/
	public double getExtMoistEffect() {
		return moistRange.getValue();
	}
	
	/***
	Returns the rate at which the current thread refreshes the greenhouse environment data
	@return Thread refresh rate
	*/
	public int getRefreshRate() {
		return this.refreshRate.getValue();
	}

	// SET	
	
	/***
	For simulation playback. Display the starting temperature for the greenhouse in the GUI
	@param temp Starting temperature of the greenhouse
	*/
	public void setStartTemp(double temp) {
		this.startTemp.setText(String.format("%.2f", temp));
	}
	
	/***
	For simulation playback. Display the rate at which the external temperature is effecting in the GUI
	@param rate External temperature effect rate in C/Min
	*/
	public void setExtTempEffect(double rate) {
		this.tempRange.setValue((int)rate);
	}
	
	/***
	For simulation playback. Display the starting humidity for the greenhouse in the GUI
	@param humid Starting humdity of the greenhouse
	*/
	public void setStartHumid(double humid) {
		this.startHumid.setText(String.format("%.2f", humid));
	}
	
	/***
	For simulation playback. Display the rate at which the external humidity is effecting in the GUI
	@param rate External humidity effect rate in %/Min
	*/
	public void setExtHumidEffect(double rate) {
		this.humidRange.setValue((int)rate);
	}
	
	/***
	For simulation playback. Display the starting soil moisture for the greenhouse in the GUI
	@param moist Starting soil moisture of the greenhouse
	*/
	public void setStartMoist(double moist) {
		this.startMoist.setText(String.format("%.2f", moist));
	}
	
	/***
	For simulation playback. Display the rate at which the external soil moisture is effecting in the GUI
	@param rate External soil moisture effect rate in %/Min
	*/
	public void setExtMoistEffect(double rate) {
		this.moistRange.setValue((int)rate);
	}
	
	/***
	For simulation playback. Display the rate for which the GUI needs to update
	@param rate GUI refresh rate
	*/
	public void setRefreshRate(double rate) {
		this.refreshRate.setValue((int) rate);
	}
	
	/***
	Disables the start temp, humidity, and soil moisture input fields when the simulation starts
	@param value Are the panel values editable or not
	*/
	public void editable(boolean value) {
		this.startTemp.setEditable(value);
		this.startHumid.setEditable(value);
		this.startMoist.setEditable(value);
	}
	
	/***
	Disables the entire GUI for the purposes of saved simulation playback.
	@param value Disable the GUI Elements or not.
	*/
	public void enableGUI(boolean value) {
		this.startTemp.setEditable(value);
		this.startHumid.setEditable(value);
		this.startMoist.setEditable(value);
		this.tempRange.setEnabled(value);
		this.humidRange.setEnabled(value);
		this.moistRange.setEnabled(value);
		this.refreshRate.setEnabled(value);
	}
	
	// LISTENERS
	
	/***
	Listens to see if the user has changed the external temperature effect rate
	@param theListener The object that is executes the changes are triggered.
	*/
	public void updateExtTempRate(ChangeListener theListener) {
		tempRange.addChangeListener(theListener);
	}
	
	/***
	Listens to see if the user has changed the external humidity effect rate
	@param theListener The object that is executes the changes are triggered.
	*/
	public void updateExtHumidRate(ChangeListener theListener) {
		humidRange.addChangeListener(theListener);
	}
	
	/***
	Listens to see if the user has changed the external soil moisture effect rate
	@param theListener The object that is executes the changes are triggered.
	*/
	public void updateExtMoistRate(ChangeListener theListener) {
		moistRange.addChangeListener(theListener);
	}
	
	/***
	Listens to see if the user has changed the rate at which the changes to the 
	greenhouse environment take place.
	@param theListener The object that is executes the changes are triggered.
	*/
	public void updateRefreshRate(ChangeListener theListener) {
		refreshRate.addChangeListener(theListener);
	}
	
	// ERROR Display
	
	/***
	Opens a dialog box with the error message specified for the Environment Control Panel
	@param errorMsg The error message
	*/
	public void displayError(String errorMsg) {
		JOptionPane.showMessageDialog(this,errorMsg);
	}
	
}