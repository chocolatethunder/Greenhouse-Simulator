/***

This Controller class is all the common methods shared between each device/sensor controllers.
This class handles file reading and writing as well as thread wait management

@author Saurabh Tomar

Written for CPSC 233

*/

package marsbars;

import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;

/***
Responsible for all the common methods shared between each device/sensor controllers.
*/
public class Controller extends Thread {
	
	// File control variables
	private File datafile;
	private FileReader fr;
	private BufferedWriter bw;
	private BufferedReader br;
	
	private volatile boolean runThread = true;
	private volatile boolean saving = false;
	private volatile boolean opening = false;
	
	private int refreshRate = 0;
	
	/***
	An empty controller
	*/
	public Controller() {
		
	}
	
	/***
	This return the refresh rate at which the current thread is refreshing the 
	controller/sensor data.
	@return refreshRate Refresh Rate of the current thread
	*/
	public int getRefreshRate() {
		return this.refreshRate;
	}

	/***
	This returns whether the current simulation is in save mode or not. This is to 
	prevent concurrency between user selecting open and save at the same time. 
	@return saving Saving current data status
	*/	
	public boolean isSaving() {
		return this.saving;
	}
	
	/***
	This returns whether the current simulation reading from a file or not. This is to 
	prevent concurrency between user selecting open and save at the same time. 
	@return saving Opening data from file status
	*/	
	public boolean isOpening() {
		return this.opening;
	}
	
	/***
	Sets the rate at which the controller/sensor updates the simulation data
	@param rate Refresh rate of the current thread
	*/
	public void setRefreshRate(int rate) {
		this.refreshRate = rate*1000;
	}
	
	/***
	Gets the BufferedReader object that has the simulation data file loaded for the current thread.
	@return br The buffer that contains the simulation file to read from
	*/	
	public synchronized BufferedReader getFileToRead() {
		return this.br;
	}	
	
	/***
	This method writes to the simulation save file specified by the user and loaded using
	the saveFile method.
	@param data The string data that needs to be written to the save file
	@throws IOException Throws an error if there is an error in writing to the file
	*/	
	public synchronized void writeToFile(String data) throws IOException {
		this.bw.write(data);
	}
	
	/***
	This method ensurs the safe pause of the currently running thread. It tells the 
	waitProcess method to wait indefinately until the resumeThread method notifies
	the thread to resume again. This overcomes the need to use the depricated stop() method.
	*/
	public synchronized void pauseThread() {		
		this.runThread = false;
	}
	
	/***
	This method notifies the current thread that is currently waiting to resume it's process safely.
	*/
	public synchronized void resumeThread() {		
		this.runThread = true;
		notify();
	}
	
	/***
	This method sets the thread's buffered writer to the user specified save file. This method is designed
	(unlike the write methods) to synchronize writing between other threads so it can share write permissions
	between multiple threads without causing a file lock and locking other threads out.
	@param savefile The buffered read object carrying the user specified save file for the simulation data.
	*/	
	public synchronized void saveFile(BufferedWriter savefile) {
		this.bw = savefile;
		this.saving = true;
	}
	
	/***
	This method creates a new file reader from the user provided simulation data file. This method (unlike the save methods) 
	is designed to create a new object of the save file and run through the data independently. (Sharing the same file object 
	between multiple threads was causing issues with simulation playback from file).
	@param theFile The file object that specifies which file to playback the simulation from
	@throws IOException Throws an error if there is an error in opening the specified simulation playback data file
	*/
	public synchronized void openFile(File theFile) throws IOException {
		this.fr = new FileReader(theFile);
		this.br = new BufferedReader(this.fr);
		this.opening = true;
	}
	
	/***
	This method safely closes the current buffered reader and writer object by checking which file mode the current thread is in.
	@throws IOException Throws and error if there is an error closing the file that the simulation is interacting with
	*/
	public synchronized void closeFile() throws IOException {
		if (this.saving) {
			this.bw.close();
		}
		if (this.opening) {
			this.br.close();
		}
	}
	
	/***
	Responsible for making the thread sleep for the user specified refresh rate in seconds and to pause and resume thread execution
	@param milliseconds The refresh rate in milliseconds
	*/
	public void waitProcess (int milliseconds) {
		
		try {
			Thread.sleep(milliseconds);
			synchronized(this) {
				while(!this.runThread) {
				   wait();
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Unexpected Interruption");
			System.exit(0);			
		}
		
	}
	
}