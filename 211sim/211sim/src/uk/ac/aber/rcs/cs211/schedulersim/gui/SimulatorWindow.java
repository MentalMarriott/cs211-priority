package uk.ac.aber.rcs.cs211.schedulersim.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class SimulatorWindow {

	public JFrame mainWindow;
	JPanel panel;
	
	public QueuePanel wait;
	public QueuePanel ready;
	public QueuePanel blocked;
	public QueuePanel finished;
	public CpuPanel cpu;
	JButton step;
	JButton run;
	JButton pause;
	
	public SimulatorWindow() {
		mainWindow = new JFrame("CS211 Scheduler Simulator");
//		mainWindow.getContentPane().setLayout(new GridLayout());
		mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainWindow.setJMenuBar(buildMenu());
		panel = new JPanel(new GridBagLayout());
		mainWindow.getContentPane().add(panel);
		buildInterface(panel);
		mainWindow.pack();
		mainWindow.setVisible(true);
	}
	
	private void buildInterface(JPanel panel) {
		wait=new QueuePanel("Waiting to Start","getWaitString");
		panel.add(wait,Util.makeGBC(1,1,1,3) );
		finished=new QueuePanel("Completed Jobs","getFinishString");
		panel.add(finished,Util.makeGBC(1,4,1,3));
		
		step = new JButton("Next Step");
		step.setActionCommand("step");
		panel.add(step,Util.makeGBC(4,1,1,1));
		run = new JButton("Run");
		run.setActionCommand("run");
		panel.add(run,Util.makeGBC(5,1,1,1));
		pause = new JButton("Pause");
		pause.setActionCommand("pause");
		panel.add(pause,Util.makeGBC(3,1,1,1));
		
		ready=new QueuePanel("Ready to Run","getReadyString");
		panel.add(ready,Util.makeGBC(7,1,1,3));
		blocked=new QueuePanel("Blocked for I/O","getBlockedString");
		panel.add(blocked,Util.makeGBC(7,4,1,3));
	
		cpu=new CpuPanel();
		panel.add(cpu,Util.makeGBC(2,2,5,3));
	}
	
	JMenuItem fileOpen;
	JMenuItem selectScheduler;
	JMenuItem exit;
	JMenuItem about;
	JMenuItem lastFile;
	
	JMenuItem nextStep;
	JMenuItem runMenu;
	JMenuItem pauseMenu;
	
	private JMenuBar buildMenu() {
		
		JMenuBar menu = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileOpen = new JMenuItem("Open data file...");
		fileOpen.setActionCommand("open");
		fileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O ,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		lastFile = new JMenuItem("Reload last data file");
		lastFile.setActionCommand("openlast");
		lastFile.setEnabled(false);
		lastFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		selectScheduler = new JMenuItem("Select Scheduler...");
		selectScheduler.setActionCommand("sched");
		selectScheduler.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S ,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		exit = new JMenuItem("Exit");
		exit.setActionCommand("exit");

		fileMenu.add(fileOpen);
		fileMenu.add(lastFile);
		fileMenu.add(selectScheduler);
		if (System.getProperty("mrj.version")==null) {
			fileMenu.add(new JSeparator());
			fileMenu.add(exit);
		}
		menu.add(fileMenu);
		
		
		JMenu simulateMenu = new JMenu("Simulate");
		nextStep = new JMenuItem ("Next Step");
		nextStep.setActionCommand("step");
		nextStep.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		runMenu = new JMenuItem ("Go Run");
		runMenu.setActionCommand("run");
		runMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		pauseMenu = new JMenuItem ("Pause");
		pauseMenu.setActionCommand("pause");
		pauseMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		simulateMenu.add(nextStep);
		simulateMenu.add(runMenu);
		simulateMenu.add(pauseMenu);
		menu.add(simulateMenu);
		
		
		JMenu helpMenu = new JMenu("Help");
		menu.add(helpMenu);
		about = new JMenuItem("About SimulatorScheduler");
		about.setActionCommand("about");
		helpMenu.add(about);
		return menu;
	}
	
	public void lastFileEnable(boolean b) {
		lastFile.setEnabled(b);
	}
	
	public void addWindowListener(WindowListener wl) {
		mainWindow.addWindowListener(wl);
	}

	public void addActionListener(ActionListener al) {
		step.addActionListener(al);
		run.addActionListener(al);
		pause.addActionListener(al);
		nextStep.addActionListener(al);
		pauseMenu.addActionListener(al);
		runMenu.addActionListener(al);
	}
	
	public void addMenuListener(ActionListener al) {
		fileOpen.addActionListener(al);
		lastFile.addActionListener(al);
		selectScheduler.addActionListener(al);
		exit.addActionListener(al);
		about.addActionListener(al);
	}
	
}
