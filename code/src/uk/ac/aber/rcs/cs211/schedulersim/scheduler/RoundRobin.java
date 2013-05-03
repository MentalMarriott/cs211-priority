package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import java.util.*;
import uk.ac.aber.rcs.cs211.schedulersim.*;

/**
 * A round robin scheduling algorithm.
 * It will keep re-presenting the same job each time getNextjob is called while it has clock
 * allowance left, if that job is removed from the queue, either because it has finished, or it gets
 * blocked for I/O then the next process gets presented with the clock allowance reset to default.
 * If the clock allowance runs out for a process and that process is incomplete then it will be
 * moved to the back of the queue and have to wait for its next scheduled process time.
 * This will happen until all processes are complete. 
 * @author cpm4(edited version of uk/ac/aber/rcs/cs211/schedulersim/scheduler/FirstComeFirstServed.java)
 * @see uk.ac.aber.rcs.cs211.schedulersim.Simulator
 *
 */
public class RoundRobin implements Scheduler {

	protected ArrayList<Job> queue;
	private int numberOfJobs;
	private int clockAllowance;
	
	public RoundRobin () {
		this.queue = new ArrayList<Job>();
		this.numberOfJobs=0;
		this.clockAllowance =2;
	}
	
	public void addNewJob(Job job) throws SchedulerException {
		if (this.queue.contains(job)) throw new SchedulerException("Job already on Queue");
		this.queue.add(this.numberOfJobs, job);
		this.numberOfJobs++;
	}

	/**
	 * Returns the next job at the head of the ready queue.
	 * This method should only ever do this - the queue should be kept in the correct order when things are
	 * added and removed.
	 * 
	 * Think about making an abstract class rather then an interface, and make this method final.
	 */
	public Job getNextJob() throws SchedulerException {
		Job lastJobReturned;
		if (this.numberOfJobs<1) throw new SchedulerException("Empty Queue");
		lastJobReturned = (Job)this.queue.get(0);
		return lastJobReturned;
	}
	

	/**
	 * This re organises the queue depending on what is scheduled to be next
	 */
	public void returnJob(Job job) throws SchedulerException {
		if (!this.queue.contains(job)) throw new SchedulerException("Job not on Queue");
		if(this.clockAllowance == 0)
		{
			this.queue.remove(job);
			this.queue.add(numberOfJobs-1, job);
			this.clockAllowance = 2;
		}else{
			this.clockAllowance--;
		}
	}

	/**
	 * Removes job passes to it, decreases the total number of jobs and resets the clock allowance
	 */
	public void removeJob(Job job) throws SchedulerException {
		if (!this.queue.contains(job)) throw new SchedulerException("Job not on Queue");
		this.queue.remove(job);
		this.numberOfJobs--;
		this.clockAllowance = 2;
	}

	/**
	 * Resets schedulers values
	 */
	public void reset() {
		this.queue.clear();
		this.numberOfJobs=0;
		this.clockAllowance=2;
	}

	/**
	 * Returns a list of Jobs
	 */
	public Job[] getJobList() {
		Job[] jobs = new Job[queue.size()];
		for (int i=0; i<queue.size(); i++) {
			jobs[i]=this.queue.get(i);
		}
		return jobs;
	}

}
