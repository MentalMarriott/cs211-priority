package uk.ac.aber.rcs.cs211.schedulersim;

import java.util.ArrayList;

import uk.ac.aber.rcs.cs211.schedulersim.scheduler.SchedulerException;

/**
 * This AbstractScheduler class actually implements a First-Come, First-Served scheduling
 * algorithm.
 * 
 * It is intended as the base class for other Schedulers, and should replace the Scheduler interface
 * before this assignment is set next time so that the queues are maintained properly. Otherwise
 * students tend to keep queues in a random order and getNextJob finds the correct job to return.
 * @author rcs
 * @version 1.0 25-5-10 Initial idea with final getNextJob
 *
 */

public class AbstractScheduler implements Scheduler {

	protected ArrayList<Job> queue;
	protected int numberOfJobs;

	public AbstractScheduler () {
		this.queue = new ArrayList<Job>();
		this.numberOfJobs=0;
	}

	public void addNewJob(Job job) throws SchedulerException {
		if (this.queue.contains(job)) throw new SchedulerException("Job already on Queue");
		this.queue.add(this.numberOfJobs, job);
		this.numberOfJobs++;
	}

	/**
	 * Used to display the queue on screen.
	 * Should reflect the order in which jobs will be called to execute.
	 */
	public Job[] getJobList() {
		Job[] jobs = new Job[queue.size()];
		return queue.toArray(jobs);
	}

	/**
	 * Returns the next job at the head of the ready queue.
	 * This method should only ever do this - the queue should be kept in the correct order when things are
	 * added and removed.
	 */
	public final Job getNextJob() throws SchedulerException {
		if (this.numberOfJobs<1) throw new SchedulerException("Empty Queue");
		return this.queue.get(0);
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

	public void returnJob(Job job) throws SchedulerException {
		if (!this.queue.contains(job)) throw new SchedulerException("Job not on Queue");
		// nothing to do in this implementation.
	}

}
