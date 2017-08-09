/***

Soil Moisture View contains all the GUI elements for the Greenhouse control panel in the main GUI. 
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
GUI elements for the Greenhouse soil moisture control panel 
*/
public class MoistureView extends JPanel {
	
	// Universals

	private JLabel currentMoistLabel 	= new JLabel("Current Soil Moisture (%)");
	private JTextField currentMoist 	= new JTextField(5);
	private JLabel prefMoistlabel 		= new JLabel("Preferred Soil Moisture Range (%)");
	private RangeSlider moistRange 		= new RangeSlider();	
	private JLabel moistureRateLabel 	= new JLabel("Soil Moisture Rate (%/min)");
	private JTextField moistureRate		= new JTextField(5);
	private JLabel refreshRateLabel 	= new JLabel("Refresh Rate (Sec)");	
	private JSlider moistRefresh 		= new JSlider(JSlider.HORIZONTAL, 1, 10, 5);
	private JLabel controllerStatus 	= new JLabel("Off");
	
	/***
	Contructor that sets the basic GUI elements for the Panel
	*/
	public MoistureView() {	
		
		// Set the padding of the Panel
		this.setBorder(new EmptyBorder(10, 10, 10, 10));		
		this.setLayout(new GridLayout(5,1,0,0));
		
		// Setup fonts
		Font fontTextField = new Font("Arial",Font.BOLD,24);
		Font fontDeviceStatus = new Font("Arial",Font.BOLD,18);
		
		currentMoist.setFont(fontTextField);
		moistureRate.setFont(fontTextField);	
		controllerStatus.setFont(fontDeviceStatus);
		
		JPanel panel1 = new JPanel(new GridLayout(2,1));	
		JPanel panel2 = new JPanel(new GridLayout(2,1));
		JPanel panel3 = new JPanel(new GridLayout(2,1));	
		JPanel panel4 = new JPanel(new GridLayout(2,1));
		JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		
		// Current Soil Moisture level display
		currentMoist.setEditable(false);
		panel1.add(currentMoistLabel);
		currentMoist.setHorizontalAlignment(JTextField.CENTER);
		panel1.add(currentMoist);		
		
		// Soil moisture range slider
		panel2.add(prefMoistlabel);
		this.moistRange.setMinimum(0);
		this.moistRange.setMaximum(100);
		this.moistRange.setMinorTickSpacing(5);
		this.moistRange.setMajorTickSpacing(20);
		this.moistRange.setPaintTicks(true);
		this.moistRange.setPaintLabels(true);
		panel2.add(moistRange);
		
		panel3.add(moistureRateLabel);
		panel3.add(moistureRate);
		
		// Refresh rate of the soil moisture sensor
		panel4.add(refreshRateLabel);
		this.moistRefresh.setValue(2);
		this.moistRefresh.setMinorTickSpacing(1);
		this.moistRefresh.setMajorTickSpacing(1);
		this.moistRefresh.setPaintTicks(true);
		this.moistRefresh.setPaintLabels(true);
		panel4.add(moistRefresh);		
		
		// Display sprinkler status
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
	Get the current soil moisture level of the greenhouse convert it into a double, and return it.
	@return The current greenhouse temperature
	*/
	public double getCurrentMoisture() {
		try { 
			return Double.parseDouble(this.currentMoist.getText());
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Please enter a valid current soil moisture");
		}
	}
	
	/***
	Get the maximum soil moisture level of the soil moisture range as specified by the user
	@return Maximum soil moisture level that needs to be maintained
	*/
	public double getDesiredMoistureUpper() {
		return moistRange.getUpperValue();
	}
	
	/***
	Get the minimum soil moisture level of the soil moisture range as specified by the user
	@return Minimum soil moisture level that needs to be maintained
	*/
	public double getDesiredMoistureLower() {
		return moistRange.getValue();
	}
	
	/***
	Get the current rate at which the sprinkler increases the soil moisture specified by user in the GUI
	@return The user specified soil moisture rate of change level (%/min)
	*/
	public double getMoistureRate() {
		try { 
			return Double.parseDouble(this.moistureRate.getText());
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Please enter a valid moisture rate");
		}
	}
	
	/***
	Returns the rate at which the current thread refreshes the greenhouse soil moisture data
	@return Thread refresh rate
	*/
	public int getMoistRefreshRate() {
		return this.moistRefresh.getValue();
	}

	
	// SET
	
	/***
	Display the specified soil moisture level on the GUI.
	@param moist Soil Moisture level needed to display
	*/
	public void setCurrentMoisture(double moist) {
		this.currentMoist.setText(String.format("%.2f", moist));
	}
	
	/***
	For simulation playback. Display the maximum soil moisture level on the range slider
	@param moist Max soil moisture level needed to display
	*/
	public void setDesiredMoistUpper(double moist) {
		this.moistRange.setUpperValue((int)moist);
	}
	
	/***
	For simulation playback. Display the minimum soil moisture level on the range slider
	@param moist Min soil moisture level needed to display
	*/
	public void setDesiredMoistLower(double moist) {
		this.moistRange.setValue((int)moist);
	}
	
	/***
	For simulation playback. Display the rate at which the sprinkler increases the soil moisture in the GUI
	@param rate Soil Moisture change rate in C/Min
	*/
	public void setMoistureRate(double rate) {
		this.moistureRate.setText(String.format("%.2f", rate));
	}
	
	/***
	For simulation playback. Display the rate for which the soil moisture GUI needs to update
	@param rate Soil Moisture GUI refresh rate
	*/
	public void setRefreshRate(double rate) {
		this.moistRefresh.setValue((int)rate);
	}
	
	/***
	Display the status of the sprinkler in the GUI
	@param status Status of the sprinkler (On/Off)
	*/
	public void setDevice(String status) {
		this.controllerStatus.setText(status);
	}
	
	/***
	Disables the Soil Moisture GUI for the purposes of saved simulation playback.
	@param value Disable the GUI Elements or not.
	*/
	public void enableGUI(boolean value) {
		this.moistureRate.setEditable(value);
		this.moistRange.setEnabled(value);
		this.moistRefresh.setEnabled(value);
	}
	
	// LISTENERS
	
	/***
	Listens to see if the user has changed the range of soil moisture level for the
	sensor to maintain in the greenhouse.
	@param theListener The object that is executes the changes are triggered.
	*/
	public void updateDesiredMoisture(ChangeListener theListener) {
		moistRange.addChangeListener(theListener);
	}
	
	/***
	Listens to see if the user has changed the rate at which soil moisture sensor checks 
	the greenhouse soil moisture level
	@param theListener The object that is executes the changes are triggered.
	*/
	public void updateRefreshRateMoist(ChangeListener theListener) {
		moistRefresh.addChangeListener(theListener);
	}
	
	/***
	Listens to see if the user has changed the rate at which sprinkler increases
	the soil moisture of the greenhouse
	@param theListener The object that is executes the changes are triggered.
	*/
	public void updateMoistureRate(DocumentListener theListener) {
		moistureRate.getDocument().addDocumentListener(theListener);
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