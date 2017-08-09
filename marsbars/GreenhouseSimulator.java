/***

This is the main class that brings it all toghether and contains the 
simulation control functions. 

@author Saurabh Tomar

Written for CPSC 233

*/

package marsbars;

import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/***
Main class that brings it all toghether and contains the simulation control functions. 
*/
public class GreenhouseSimulator {
	
	private TemperatureView tempView 		= new TemperatureView();
	private TemperatureModel tempModel 		= new TemperatureModel();
	private TemperatureController tempCont 	= new TemperatureController(tempModel,tempView);
	
	private HumidityView humidView 			= new HumidityView();
	private HumidityModel humidModel 		= new HumidityModel();
	private HumidityController humidCont 	= new HumidityController(humidModel,humidView);
	
	private MoistureView moistView 			= new MoistureView();
	private MoistureModel moistModel 		= new MoistureModel();
	private MoistureController moistCont 	= new MoistureController(moistModel,moistView);
	
	private EnvironmentView envView 		= new EnvironmentView();
	private EnvironmentModel envModel 		= new EnvironmentModel();
	private EnvironmentController envCont 	= new EnvironmentController(envModel,envView,tempModel,humidModel,moistModel);
	
	private GUI mainGUI = new GUI(envView,tempView,humidView,moistView);
	
	private JFileChooser chooser = new JFileChooser();
	
	private File datafile;
	private BufferedWriter bw;
	
	/***
	Constructor that controls the start, stop, resume, load, and, save a simulation. 
	*/
	public GreenhouseSimulator() {		
		
		mainGUI.addStartSimulationListener(new StartSimulator());
		mainGUI.addPauseSimulationListener(new PauseSimulator());
		mainGUI.addResumeSimulationListener(new ResumeSimulator());		
		mainGUI.addLoadSimulationListener(new LoadFile());
		mainGUI.addSaveSimulationListener(new SaveFile());		
		mainGUI.addCloseWindowListener(new CloseWindowListener());
		
		mainGUI.setVisible(true);	
		
	}
	
	/***
	Main method calls Greenhouse constructer to start listenening to user's responses
	@param args Command Line arguments
	*/
	public static void main (String[] args) {		
		new GreenhouseSimulator();		
	}
	
	class StartSimulator implements ActionListener {		
		
		/***
		This method starts the simulation only after it has checked that all the input values
		from the user has been correctly loaded, validated, and sanitized.
		@param e The action event
		*/
		public void actionPerformed(ActionEvent e) {			
			
			try {				
				if (envCont.setup() == true && tempCont.setup() == true && humidCont.setup() == true && moistCont.setup() == true) {
					
					mainGUI.updateButtonsWhenRunning();
					
					envCont.start();
					tempCont.start();					
					humidCont.start();	
					moistCont.start();	
					
				}			
			} catch (Exception er) {				
				mainGUI.displayError(er.getMessage());				
			}
			
		}
		
	}
	
	class PauseSimulator implements ActionListener {		
		
		/***
		This method pauses the simulation. It signals the controller of each sensor to wait thread execution
		@param e The action event
		*/
		public void actionPerformed(ActionEvent e) {			
			mainGUI.updateButtonsWhenPaused();			
			envCont.pauseThread();
			tempCont.pauseThread();
			humidCont.pauseThread();			
			moistCont.pauseThread();			
		}		
	}
	
	class ResumeSimulator implements ActionListener {		
		
		/***
		This method resumes the simulation. It signals the controller of each sensor to notify the thread to resume execution
		@param e The action event
		*/
		public void actionPerformed(ActionEvent e) {			
			mainGUI.updateButtonsWhenRunning();			
			envCont.resumeThread();
			tempCont.resumeThread();
			humidCont.resumeThread();		
			moistCont.resumeThread();		
		}		
	}
	
	class LoadFile implements ActionListener {

		/***
		This method loads up a File Chooser GUI and passes the File object to be opened by individual
		controller thread independently to playback a previous simulation
		@param e The action event
		*/		
		public void actionPerformed(ActionEvent e) {
			if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				datafile = chooser.getSelectedFile();				
				try {
					
					envCont.openFile(datafile);
					tempCont.openFile(datafile);
					humidCont.openFile(datafile);
					moistCont.openFile(datafile);
					
					mainGUI.setStatus("Simulation Playback");
					mainGUI.loadMode();
					
				} catch (IOException er) {
					mainGUI.displayError(er.getMessage());
				}
			}			
		}		
	}
	
	class SaveFile implements ActionListener {		
		
		/***
		This method loads up a File Chooser GUI and passes the shared buffered writer object to to which each 
		controller can write it's data to in a synchronized manner.
		@param e The action event
		*/
		public void actionPerformed(ActionEvent e) {
			if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				datafile = chooser.getSelectedFile();				
				try {

					bw = new BufferedWriter(new FileWriter(datafile));
					
					envCont.saveFile(bw);
					tempCont.saveFile(bw);
					humidCont.saveFile(bw);
					moistCont.saveFile(bw);
					
					mainGUI.setStatus("Saving to File");
					mainGUI.saveMode();
					
				} catch (IOException er) {
					mainGUI.displayError(er.getMessage());
				}
			}			
		}		
	}
	
	class CloseWindowListener implements ActionListener {		
		/***
		This method safely closes all the appropriate buffers and closes the files that the simulation 
		is acting on and closes the program. 
		@param e The action event
		*/
		public void actionPerformed(ActionEvent e) {
			try {
				envCont.closeFile();
				tempCont.closeFile();
				humidCont.closeFile();
				moistCont.closeFile();
				
				System.exit(0);
			} catch (IOException er) {
				mainGUI.displayError(er.getMessage());
			}
		}		
	}
	
}