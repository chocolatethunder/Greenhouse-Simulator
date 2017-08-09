/***

Humidity View contains all the GUI elements for the Greenhouse control panel in the main GUI. 
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
GUI elements for the Greenhouse humidity control panel 
*/
public class HumidityView extends JPanel {
	
	// Universals

	private JLabel currentHumidLabel 	= new JLabel("Current Humidty (%)");
	private JTextField currentHumid 	= new JTextField(5);
	private JLabel prefHumidlabel 		= new JLabel("Preferred Humidity Range (%)");
	private RangeSlider humidRange 		= new RangeSlider();	
	private JLabel humidityRateLabel 	= new JLabel("Humidity Rate (%/min)");
	private JTextField humidityRate		= new JTextField(5);
	private JLabel refreshRateLabel 	= new JLabel("Refresh Rate (Sec)");	
	private JSlider humidRefresh 		= new JSlider(JSlider.HORIZONTAL, 1, 10, 5);
	private JLabel controllerStatus 	= new JLabel("Off");
	
	/***
	Contructor that sets the basic GUI elements for the Panel
	*/
	public HumidityView() {	
		
		// Set the padding of the Panel
		this.setBorder(new EmptyBorder(10, 10, 10, 10));		
		this.setLayout(new GridLayout(5,1,0,0));
		
		// Setup fonts
		Font fontTextField = new Font("Arial",Font.BOLD,24);
		Font fontDeviceStatus = new Font("Arial",Font.BOLD,18);
		
		currentHumid.setFont(fontTextField);
		humidityRate.setFont(fontTextField);	
		controllerStatus.setFont(fontDeviceStatus);
		
		JPanel panel1 = new JPanel(new GridLayout(2,1));	
		JPanel panel2 = new JPanel(new GridLayout(2,1));
		JPanel panel3 = new JPanel(new GridLayout(2,1));	
		JPanel panel4 = new JPanel(new GridLayout(2,1));
		JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		// Current Humidity level display
		currentHumid.setEditable(false);
		panel1.add(currentHumidLabel);
		currentHumid.setHorizontalAlignment(JTextField.CENTER);
		panel1.add(currentHumid);		
		
		// Humidity range slider
		panel2.add(prefHumidlabel);
		this.humidRange.setMinimum(0);
		this.humidRange.setMaximum(100);
		this.humidRange.setMinorTickSpacing(5);
		this.humidRange.setMajorTickSpacing(20);
		this.humidRange.setPaintTicks(true);
		this.humidRange.setPaintLabels(true);
		panel2.add(humidRange);

		panel3.add(humidityRateLabel);
		panel3.add(humidityRate);
		
		// Refresh rate of the humidity sensor
		panel4.add(refreshRateLabel);
		this.humidRefresh.setValue(2);
		this.humidRefresh.setMinorTickSpacing(1);
		this.humidRefresh.setMajorTickSpacing(1);
		this.humidRefresh.setPaintTicks(true);
		this.humidRefresh.setPaintLabels(true);
		panel4.add(humidRefresh);		
		
		// Display humidifier status
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
	Get the current humidity level of the greenhouse convert it into a double, and return it.
	@return The current greenhouse temperature
	*/
	public double getCurrentHumidity() {
		try { 
			return Double.parseDouble(this.currentHumid.getText());
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Please enter a valid current humidity");
		}
	}
	
	/***
	Get the maximum humidity level of the humidity range as specified by the user
	@return Maximum humidity level that needs to be maintained
	*/
	public double getDesiredHumidityUpper() {
		return humidRange.getUpperValue();
	}

	/***
	Get the minimum humidity level of the humidity range as specified by the user
	@return Minimum humidity level that needs to be maintained
	*/
	public double getDesiredHumidityLower() {
		return humidRange.getValue();
	}
	
	/***
	Get the current rate at which the humidifier increases the humidity specified by user in the GUI
	@return The user specified humidity rate of change level (%/min)
	*/
	public double getHumidityRate() {
		try { 
			return Double.parseDouble(this.humidityRate.getText());
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Please enter a valid humidity rate.");
		}
	}
	
	/***
	Returns the rate at which the current thread refreshes the greenhouse humidity data
	@return Thread refresh rate
	*/
	public int getHumidRefreshRate() {
		return this.humidRefresh.getValue();
	}

	
	// SET
	
	/***
	Display the specified humidity level on the GUI.
	@param humid Humidity level needed to display
	*/
	public void setCurrentHumidity(double humid) {
		this.currentHumid.setText(String.format("%.2f", humid));
	}
	
	/***
	For simulation playback. Display the maximum humidity level on the range slider
	@param humid Max humidity level needed to display
	*/
	public void setDesiredHumidUpper(double humid) {
		this.humidRange.setUpperValue((int)humid);
	}
	
	/***
	For simulation playback. Display the minimum humidity level on the range slider
	@param humid Min humidity level needed to display
	*/
	public void setDesiredHumidLower(double humid) {
		this.humidRange.setValue((int)humid);
	}
	
	/***
	For simulation playback. Display the rate at which the humidifier increases the humidity in the GUI
	@param rate Humidity change rate in C/Min
	*/
	public void setHumidityRate(double rate) {
		this.humidityRate.setText(String.format("%.2f", rate));
	}
	
	/***
	For simulation playback. Display the rate for which the humidity GUI needs to update
	@param rate Humidity GUI refresh rate
	*/
	public void setRefreshRate(double rate) {
		this.humidRefresh.setValue((int)rate);
	}
	
	/***
	Display the status of the humidifier in the GUI
	@param status Status of the humidifier (On/Off)
	*/
	public void setDevice(String status) {
		this.controllerStatus.setText(status);
	}
	
	/***
	Disables the Humidity GUI for the purposes of saved simulation playback.
	@param value Disable the GUI Elements or not.
	*/
	public void enableGUI(boolean value) {
		this.humidityRate.setEditable(value);
		this.humidRange.setEnabled(value);
		this.humidRefresh.setEnabled(value);
	}
	
	// LISTENERS
	
	/***
	Listens to see if the user has changed the range of humidity level for the
	sensor to maintain in the greenhouse.
	@param theListener The object that is executes the changes are triggered.
	*/
	public void updateDesiredHumidity(ChangeListener theListener) {
		humidRange.addChangeListener(theListener);
	}
	
	/***
	Listens to see if the user has changed the rate at which humidity sensor checks 
	the greenhouse humidity level
	@param theListener The object that is executes the changes are triggered.
	*/	
	public void updateRefreshRateHumid(ChangeListener theListener) {
		humidRefresh.addChangeListener(theListener);
	}
	
	/***
	Listens to see if the user has changed the rate at which humidifier increases
	the humidity of the greenhouse
	@param theListener The object that is executes the changes are triggered.
	*/
	public void updateHumidityRate(DocumentListener theListener) {
		humidityRate.getDocument().addDocumentListener(theListener);
	}
	
	// ERROR Display
	
	/***
	Opens a dialog box with the error message specified for the Humidity Control Panel
	@param errorMsg The error message
	*/
	public void displayError(String errorMsg) {
		JOptionPane.showMessageDialog(this,errorMsg);
	}
	
}