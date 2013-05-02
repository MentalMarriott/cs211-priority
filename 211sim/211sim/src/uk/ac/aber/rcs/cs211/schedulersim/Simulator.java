package uk.ac.aber.rcs.cs211.schedulersim;

import java.util.*;

import uk.ac.aber.rcs.cs211.schedulersim.scheduler.SchedulerException;

public class Simulator {

	private Scheduler scheduler;
	private ArrayList<Job> blockedQueue;
	private ArrayList<Job> finishedQueue;
	private ArrayList<Job> waitQueue;
	private int cpuTicks;
	private int idleTime;
	private int contextSwitches;
	private Job lastJob;
	
	
	public Simulator(Scheduler scheduler, ArrayList<Job> waitQueue) {
		this.scheduler=scheduler;
		blockedQueue = new ArrayList<Job>();
		finishedQueue = new ArrayList<Job>();
		this.waitQueue = waitQueue;
	}
	
	public void clearQueues() {
		blockedQueue.clear();
		finishedQueue.clear();
		waitQueue.clear();
		scheduler.reset();
		cpuTicks=0;
		idleTime=0;
		contextSwitches=0;
		lastJob=null;
	}

	public void singleStep () {
		Job job;
		// First see if any processes have completed I/O calls and put them back
		// on the ready queue.
		
		for (int i=0; i<this.blockedQueue.size();i++) {
			job=(Job)blockedQueue.get(i);
			if (job.getInstruction()=='C')
			{
				this.blockedQueue.remove(job);
				try {
					scheduler.addNewJob(job);
				} catch (SchedulerException e) {
					// This means that the job is already on the queue.
					// Somewhat fatal occurrence
					e.printStackTrace();
				}
			} else {
				// reduce the time waiting by one.
				job.incrementProgramCounter();
				// increment the blocked time.
				job.incrementBlockedTime();
			}
		}
		
		// Check to see if any jobs are ready to start.
		for (int i=0; i<waitQueue.size();i++) {
			job=(Job)waitQueue.get(i);
			if (job.getStartCycle()==cpuTicks) {
				waitQueue.remove(job);
				i--;
				try {
					scheduler.addNewJob(job);
				} catch (SchedulerException e) {
					// not too bad 
					e.printStackTrace();
				}
			}
		}
		
		// total number of ticks that the cpu has done for performance monitoring.
		cpuTicks++;

		// now get current job and "execute" an instruction.
		try {
			job = scheduler.getNextJob();
			if (job!=lastJob) {
				contextSwitches++;
				lastJob=job;
			}
		} catch (SchedulerException e) {
			// If we're here, then there are no jobs on the queue.
			// There might be jobs still blocked, so continue the simulation.
			
			idleTime++;
			return;
		}
			
		char instruct = job.getInstruction();
		switch (instruct) {
			case 'C':
				job.incrementProgramCounter();
				try {
					scheduler.returnJob(job);
				} catch (SchedulerException e) {
					// this is pretty fatal.
					e.printStackTrace();
					return;
				}
				break;
			case 'I':
//				job.incrementProgramCounter();
				try {
					scheduler.removeJob(job);
				} catch (SchedulerException e) {
					// this is fatal.
					System.err.println("Not the right job!");
					return;
				}
				blockedQueue.add(job);
				break;
		}
		
		// check if the current job has finished.
		
		if (job.isFinished()) {
			// note the time when the process finished.
			job.setFinishTime(cpuTicks);
			try {
				scheduler.removeJob(job);
			} catch (SchedulerException e) {
				System.err.println("Wrong job!");
			}
			this.finishedQueue.add(job);
		}	
	}

	public boolean hasfinished() {
		return ((scheduler.getJobList().length==0) &&
			(blockedQueue.isEmpty()) && 
			(waitQueue.isEmpty()));
	}
	
	public int getCpuTicks() {
		return cpuTicks;
	}
	
	public Job[] getFinishedQueue() {
		return toJobArray(finishedQueue);
	}
	
	public Job[] getBlockedQueue() {
		return toJobArray(blockedQueue);
	}
	
	public Job[] getWaitQueue() {
		return toJobArray(waitQueue);
	}
	
	public Job[] getReadyQueue() {
		return scheduler.getJobList();
	}
	
	public Job getCurrentJob() {
		Job[] jobs = scheduler.getJobList();
		if (jobs.length>0) {
			return jobs[0];
		} else {
			return null;
		}
	}

	private Job[] toJobArray(ArrayList<Job> l) {
		Job[] j = new Job[l.size()];
		for (int i=0; i<l.size(); i++) {
			j[i]=(Job)l.get(i);
		}
		return j;
	}

	public int getContextSwitchCount() {
		return this.contextSwitches;
	}
	
	public int getIdleTimeCount() {
		return idleTime;
	}
	
	/**
	 * @return the scheduler
	 */
	public Scheduler getScheduler() {
		return scheduler;
	}

	/**
	 * @param scheduler the scheduler to set
	 */
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

}

