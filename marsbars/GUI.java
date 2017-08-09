/***

This class combines all the controller/sensor panel GUIs and adds GUI elements for file control

@author Saurabh Tomar
@author Ernest Yu

Notice: This code is using the Rangeslider class written by Ernest Yu and is being used under Open Source 
Liscencing criteria. Source: https://github.com/ernieyu/Swing-range-slider/tree/master/src/slider

Written for CPSC 233

*/

package marsbars;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/***
This class combines all the controller/sensor panel GUIs and adds GUI elements for file control
*/
public class GUI extends JFrame {
	
	private EnvironmentView envGUI;
	private TemperatureView tempGUI;	
	private HumidityView humidGUI;
	private MoistureView moistGUI;

	private static int WIN_WIDTH = 1380;
	private static int WIN_HEIGHT = 500;
	
	private JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
	
	// Control Menu Items
	private JPanel menuPanel 	= new JPanel(new GridLayout(5,1));	
	private JButton runSim 		= new JButton("Start");
	private JButton resSim 		= new JButton("Resume");
	private JButton stopSim 	= new JButton("Pause");
	private JButton loadSim 	= new JButton("Load");
	private JButton saveSim 	= new JButton("Save");
	private JButton closeSim 	= new JButton("Close");
	
	private JLabel status		= new JLabel("");
	
	/***
	Controller which takes in all the GUI sub components. It sets up the display and dimensions of the Main GUI window. 
	The File and Simulation control buttons are added to the GUI in this constructor.
	@param eGUI Object containing the JPanel of the Environment/External Effect Control Panel
	@param tGUI Object containing the JPanel of the Temperature Control Panel
	@param hGUI Object containing the JPanel of the Humidity Control Panel
	@param mGUI Object containing the JPanel of the Soil Moisture Control Panel
	*/
	public GUI (EnvironmentView eGUI, TemperatureView tGUI, HumidityView hGUI, MoistureView mGUI) {
		
		this.envGUI 	= eGUI;
		this.tempGUI 	= tGUI;
		this.humidGUI 	= hGUI;
		this.moistGUI 	= mGUI;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WIN_WIDTH,WIN_HEIGHT);
		this.setTitle("How many Matt Daemons can survive on Mars.exe? #MakeMattMartianAgain");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		JPanel sublpanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel sublpanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel sublpanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel sublpanel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel sublpanel5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		sublpanel1.add(runSim);
		sublpanel1.add(resSim);
		sublpanel1.add(stopSim);		
		
		resSim.setVisible(false);
		stopSim.setVisible(false);
		
		sublpanel2.add(saveSim);
		sublpanel2.add(loadSim);
		
		sublpanel3.add(closeSim);
		
		sublpanel4.add(new JLabel());
		
		sublpanel5.add(status);
		
		
		menuPanel.add(sublpanel1);
		menuPanel.add(sublpanel2);
		menuPanel.add(sublpanel3);
		menuPanel.add(sublpanel4);
		menuPanel.add(sublpanel5);
		
		menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		runSim.setPreferredSize(new Dimension(145,30));
		resSim.setPreferredSize(new Dimension(145,30));
		stopSim.setPreferredSize(new Dimension(145,30));
		saveSim.setPreferredSize(new Dimension(70,30));
		loadSim.setPreferredSize(new Dimension(70,30));
		closeSim.setPreferredSize(new Dimension(145,30));
		
		mainPanel.add(menuPanel);
		mainPanel.add(this.envGUI);
		mainPanel.add(this.tempGUI);
		mainPanel.add(this.humidGUI);
		mainPanel.add(this.moistGUI);
		
		this.add(mainPanel);
		
	}
	
	/***
	Disable the ability to load and save a simulation after it has been started by
	disabling the UI Buttons. After starting simulation you can only pause, resume, and close.
	*/
	public void updateButtonsWhenRunning() {
		
		this.runSim.setEnabled(false);
		
		this.saveSim.setEnabled(false);
		this.loadSim.setEnabled(false);		
		
		this.runSim.setVisible(false);
		this.resSim.setVisible(false);
		this.stopSim.setVisible(true);
	}
	
	/***
	Update the UI button functionality when simulation is paused. Change the start button to a resume button.
	*/
	public void updateButtonsWhenPaused() {
		
		this.resSim.setEnabled(true);
		
		this.saveSim.setEnabled(false);
		this.loadSim.setEnabled(false);
		
		this.runSim.setVisible(false);
		this.resSim.setVisible(true);
		this.stopSim.setVisible(false);
	}
	
	/***
	Let the user know that the simulation is being saved to file or whether they are in simulation playback mode.
	@param currStatus The message to display on the UI informing user of the simulation mode
	*/
	public void setStatus(String currStatus) {
		this.status.setText(currStatus);
	}
	
	/***
	Disable the load button when save file is selected for simulation data save. The two actions cannot be allowed at the same time. 
	*/
	public void loadMode() {
		this.saveSim.setEnabled(false);
	}
	
	/***
	Disable the save button when a simulation data file is loaded for playback. The two actions cannot be allowed at the same time. 
	*/
	public void saveMode() {
		this.loadSim.setEnabled(false);
	}
	
	// LISTENERS
	
	/***
	Listens to see if the user wants to start the simulation from the beginning
	@param theListener The object that is executes the changes are triggered.
	*/
	public void addStartSimulationListener(ActionListener theListener) {
		runSim.addActionListener(theListener);		
	}
	
	/***
	Listens to see if the user wants to pause the simulation
	@param theListener The object that is executes the changes are triggered.
	*/
	public void addPauseSimulationListener(ActionListener theListener) {
		stopSim.addActionListener(theListener);		
	}
	
	/***
	Listens to see if the user wants to resume the paused simulation
	@param theListener The object that is executes the changes are triggered.
	*/
	public void addResumeSimulationListener(ActionListener theListener) {
		resSim.addActionListener(theListener);		
	}
	
	/***
	Listens to see if the user wants to load a file for simulation playback
	@param theListener The object that is executes the changes are triggered.
	*/
	public void addLoadSimulationListener(ActionListener theListener) {
		loadSim.addActionListener(theListener);		
	}
	
	/***
	Listens to see if the user wants to select a file to save the simulation data
	@param theListener The object that is executes the changes are triggered.
	*/
	public void addSaveSimulationListener(ActionListener theListener) {
		saveSim.addActionListener(theListener);		
	}
	
	/***
	Listens to see if the user wants to exit the simulation program
	@param theListener The object that is executes the changes are triggered.
	*/
	public void addCloseWindowListener(ActionListener theListener) {
		closeSim.addActionListener(theListener);
	}
	
	/***
	Opens a dialog box with the error message specified for the main GUI Panel
	@param errorMsg The error message
	*/
	public void displayError(String errorMsg) {
		JOptionPane.showMessageDialog(this,errorMsg);
	}
	
	
}