package uk.ac.aber.rcs.cs211.schedulersim.gui;

import java.awt.GridBagLayout;

import javax.swing.*;

import uk.ac.aber.rcs.cs211.schedulersim.Job;

public class CpuPanel extends JPanel {

	public static final long serialVersionUID=0;	
	
	private JTextField cycles;
	private JTextField procName;
	private JTextField schedName;
	private JTextField schedDesc;
	private JTextField progress;
	
	public CpuPanel() {
		super(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "CPU status"));
		
		this.add(new JLabel("Selected Scheduler"),Util.makeGBC(1, 2, 1, 1));
		schedName = new JTextField("                                         ");
		schedName.setEditable(false);
		this.add(schedName,Util.makeGBC(1, 3, 4, 1));
		this.add(new JLabel("Scheduler Description"),Util.makeGBC(1, 4, 1, 1));
		schedDesc = new JTextField("                                          ");
		schedDesc.setEditable(false);
		this.add(schedDesc,Util.makeGBC(1, 5, 3, 1));
		
		this.add(new JLabel("CPU Cycles:"),Util.makeGBC(2, 6, 1, 1));
		cycles=new JTextField("0000");
		cycles.setEditable(false);
		this.add(cycles,Util.makeGBC(3, 6, 1, 1));
		
		this.add(new JLabel("--------------------------------------------------"),Util.makeGBC(1, 7, 4, 1));
		
		this.add(new JLabel("Current Process name"),Util.makeGBC(1, 8, 1, 1));
		procName=new JTextField("IDLE");
		procName.setEditable(false);
		this.add(procName,Util.makeGBC(2, 8, 1, 1));
		
		this.add(new JLabel("progress:"),Util.makeGBC(3, 8, 1, 1));
		progress=new JTextField("00/00");
		progress.setEditable(false);
		this.add(progress,Util.makeGBC(4, 8, 1, 1));
		
	}
	
	public void updateScheduler(String className, String description) {
		schedName.setText(className);
		schedDesc.setText(description);
	
	}
	
	
	public void updateCycles(int cpu) {
		cycles.setText(Integer.toString(cpu));
	}
	
	public void updateJob(Job job) {
		if (job != null) {
			procName.setText(job.getName());
			progress.setText(job.getReadyString());
		} else {
			procName.setText("IDLE");
			progress.setText("00/00");
		}
	}
	
}
