package uk.ac.aber.rcs.cs211.schedulersim;

import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javax.swing.*;

import uk.ac.aber.rcs.cs211.schedulersim.gui.*;

public class SimulatorApplication {

	Simulator sim;
	Scheduler scheduler;
	ArrayList<Job> jobQueue;
	Job[] finishedList;
	SimulatorWindow sw;
	String lastFilename;

	boolean singleStep=false;
	boolean running=false;
	boolean dataFileLoaded=false;

	ResourceBundle options = ResourceBundle.getBundle("Schedulers");

	public SimulatorApplication() {

		SimActionListener sal = new SimActionListener();
		
		sw= new SimulatorWindow();
		sw.addWindowListener(new SimWindowListener());
		sw.addActionListener(sal);
		sw.addMenuListener(new MenuListener());

		while(true) {

			while (scheduler==null || dataFileLoaded==false) {
//System.out.println("Doing nothing");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {}
			}
			
			if (sim==null) {
				sim = new Simulator(scheduler,jobQueue);
			}

			while (!sim.hasfinished()) {
				while ((singleStep || running) && (!sim.hasfinished())) {
					singleStep = false;
					sim.singleStep();
					updateDisplay();
					if (running) {
						try {
//System.out.println("Waiting");
							Thread.sleep(50);
						} catch (InterruptedException e) {}
					}
				}
			}
			
			if (dataFileLoaded) {
				finishedList = sim.getFinishedQueue();
				System.out.println(makeResultsSummary(finishedList));
			}
			
			//reset for the next data file.
			dataFileLoaded=false;
			sim=null;
			running=false;
			sw.lastFileEnable(true);
		}
	}

	public String makeResultsSummary(Job[] l) {
		StringBuffer retval=new StringBuffer();

		retval.append("\nSimulator run ");
		retval.append(new Date(System.currentTimeMillis()).toString());
		retval.append("\n");
		retval.append(scheduler.getClass().getName());
		retval.append("\nDataFile name:\t");
		retval.append(lastFilename);
		retval.append("\n");
		
		Job temp;
		int total=0;
		for (int i=0; i<l.length; i++) {
			temp=l[i];
			retval.append(temp.getName());
			retval.append(":");
			retval.append(temp.getPriority());
			retval.append("\t");
			retval.append(temp.getElapsedDuration());
			total+=temp.getElapsedDuration();
			retval.append("\n");
		}
		retval.append("Mean elapsed duration\t");
		retval.append(total/l.length);    //  FIXME!  - Length may be zero!
		retval.append("\n");		
		retval.append("Total CPU time\t");
		retval.append(sim.getCpuTicks());
		retval.append("\n");
		retval.append("Total Context Switches\t");
		retval.append(sim.getContextSwitchCount());
		retval.append("\n");
		retval.append("Total Idle time\t");
		retval.append(sim.getIdleTimeCount());
		retval.append("\n");
	
		return retval.toString();
	}
	
	
	public void updateDisplay() {
		if (sim!=null) {
			scheduler = sim.getScheduler();
		} 
		if (scheduler!=null) {
			String schedName=scheduler.getClass().getName();
			sw.cpu.updateScheduler(schedName,
					options.getString(schedName));
		} else {
			sw.cpu.updateScheduler("none loaded","none loaded");
		}
		if (sim !=null) {
//			System.out.println("Hello");
			sw.blocked.update(sim.getBlockedQueue());
			sw.finished.update(sim.getFinishedQueue());
			sw.wait.update(sim.getWaitQueue());

			sw.cpu.updateCycles(sim.getCpuTicks());
			if (scheduler!=null) {
				sw.ready.update(sim.getReadyQueue());
				sw.cpu.updateJob(sim.getCurrentJob());
			}

		} else {
//			System.out.println("Empty");
			Job[] none = new Job[0];
			sw.blocked.update(none);
			sw.finished.update(none);
			sw.ready.update(none);
			sw.wait.update(none);

			sw.cpu.updateCycles(0);
			sw.cpu.updateJob(null);
		}
//		sw.mainWindow.pack();

	}

	public void clearQueues() {
		dataFileLoaded=false;
		if (sim != null) {
			this.sim.clearQueues();
		}
		this.updateDisplay();
	}

	public void openFile(String filename) {
		clearQueues();
		try {
			jobQueue = SimulatorLoader.Load(filename);
		} catch (Exception e) {
			System.err.println("Could not load file "+filename);
			e.printStackTrace();
			System.exit(1);
		}
//		System.out.println("JobQueue has n items = "+jobQueue.size());
		updateDisplay();
		lastFilename = filename;
		dataFileLoaded=true;
		
//		System.out.println(lastFilename+" "+dataFileLoaded);
	}


	public void quit() {
		int reply = JOptionPane.showConfirmDialog(sw.mainWindow, 
				"Are you really sure that you want to quit now?", 
				"Confirm Quit", 
				JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.INFORMATION_MESSAGE);
		if (reply==0) {
			System.exit(0);
		}
	}

	public Scheduler loadScheduler() {
		Scheduler scheduler = null;

		String key;
		ArrayList<String> choices = new ArrayList<String>();

		for (Enumeration<String> e=options.getKeys(); e.hasMoreElements();) {
			key=e.nextElement();
			choices.add(key);
		}

		String s = (String)JOptionPane.showInputDialog(
				sw.mainWindow,
				"Choose the scheduler\n",
				"Choose Scheduler",
				JOptionPane.PLAIN_MESSAGE,
				null,
				choices.toArray(),
				null);

		if (s==null) {
			return null;
		}
		try {
			scheduler = (Scheduler)Class.forName(s).getConstructor().newInstance();
			return scheduler;
		} catch (ClassNotFoundException e) {
			System.err.println("Class '"+s+"' not found");
		} catch (InstantiationException e) {
			System.err.println("Class could not be instantiated");
		} catch (IllegalAccessException e) {
			System.err.println("Illegal Access");
		} catch (InvocationTargetException e) {
			System.err.println("Invocation Target Problem");
		} catch (NoSuchMethodException e) {
			System.err.println("Constructor not present");
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (System.getProperty("mrj.version")!=null) {
			System.setProperty("com.apple.mrj.application.apple.menu.about.name","211Sim");
			System.setProperty("apple.laf.useScreenMenuBar","true");
		}
		new SimulatorApplication();

	}

	class SimActionListener implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			if (!dataFileLoaded) {
				JOptionPane.showMessageDialog(sw.mainWindow,
						"You must load a datafile before you can run.",
						"No file loaded",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (scheduler==null) {
				JOptionPane.showMessageDialog(sw.mainWindow,
						"You must select a scheduler before you can run.",
						"No scheduler",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			String event = ae.getActionCommand();
			if (event.equals("run")) {
				sw.lastFileEnable(false);
				running=true;
			} else if (event.equals("step")) {
				sw.lastFileEnable(false);
				singleStep=true;
			} else if (event.equals("pause")) {
				sw.lastFileEnable(false);
				running=false;
			}	
		}
	}

	class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			String event = ae.getActionCommand();
			if (event.equals("exit")) {
				quit();
			} else if (event.equals("about")) {
				JOptionPane.showMessageDialog(sw.mainWindow,
						"The Scheduler Simulator for CS211 2006-2007\n" +
						"  written by Richard Shipman, Aberystwyth\n" +
						"rcs@aber.ac.uk   http://richard.shipman.me.uk/", 
						"About Scheduler Simulator", 
						JOptionPane.INFORMATION_MESSAGE);
			} else if (event.equals("sched")) {
				scheduler=loadScheduler();
				if (sim!=null) {
					sim.setScheduler(scheduler);
				}
				updateDisplay();
			} else if (event.equals("open")) {
				JFileChooser chooser=new JFileChooser(lastFilename);
				chooser.setFileFilter(new JobFileFilter());
				int returnVal = chooser.showOpenDialog(sw.mainWindow);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					openFile(chooser.getSelectedFile().getAbsolutePath());
				}
			} else if (event.equals("openlast")) {
				openFile(lastFilename);
			} else {
				System.out.println("Unrecognised command "+event);
			}
		}
	}

	class SimWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent arg0) {
			super.windowClosing(arg0);
			quit();
		}
	}
}
