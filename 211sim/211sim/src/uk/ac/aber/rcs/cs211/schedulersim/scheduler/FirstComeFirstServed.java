package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import java.util.*;
import uk.ac.aber.rcs.cs211.schedulersim.*;

/**
 * A first come, first served scheduling algorithm.
 * It will keep re-presenting the same job each time getNextjob is called, until
 * that job is removed from the queue, either because it has finished, or it gets
 * blocked for I/O. 
 * @author rcs
 * @see uk.ac.aber.rcs.cs211.schedulersim.Simulator
 *
 */
public class FirstComeFirstServed implements Scheduler {

	protected ArrayList<Job> queue;
	private int numberOfJobs;
	
	public FirstComeFirstServed () {
		this.queue = new ArrayList<Job>();
		this.numberOfJobs=0;
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

	public void returnJob(Job job) throws SchedulerException {
		if (!this.queue.contains(job)) throw new SchedulerException("Job not on Queue");
		// nothing to do in this implementation.
	}

	public void removeJob(Job job) throws SchedulerException {
		if (!this.queue.contains(job)) throw new SchedulerException("Job not on Queue");
		this.queue.remove(job);
		this.numberOfJobs--;
	}

	public void reset() {
		this.queue.clear();
		this.numberOfJobs=0;
	}

	public Job[] getJobList() {
		Job[] jobs = new Job[queue.size()];
		for (int i=0; i<queue.size(); i++) {
			jobs[i]=this.queue.get(i);
		}
		return jobs;
	}

}
