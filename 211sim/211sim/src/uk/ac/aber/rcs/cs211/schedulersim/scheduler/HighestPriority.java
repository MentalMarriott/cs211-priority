package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import java.util.*;
import uk.ac.aber.rcs.cs211.schedulersim.*;

/**
 * A Highest priority first scheduler will get the job with the highest priority and stick it at the 
 * front of the queue. If a new job is added with a higher priority than the job at the head of the 
 * queue it is then put to the front. When a job is removed the queue is updated with next highest
 * priority.
 * @author cpm4(edited version of uk/ac/aber/rcs/cs211/schedulersim/scheduler/FirstComeFirstServed.java)
 * @see uk.ac.aber.rcs.cs211.schedulersim.Simulator
 *
 */
public class HighestPriority implements Scheduler {

	protected ArrayList<Job> queue;
	private int numberOfJobs;
	
	public HighestPriority () {
		this.queue = new ArrayList<Job>();
		this.numberOfJobs=0;
	}
	
	/**
	 * When adding a new job it checks to see if priority is higher than the job at the start
	 * of the queue
	 */
	public void addNewJob(Job job) throws SchedulerException {
		if (this.queue.contains(job)) throw new SchedulerException("Job already on Queue");

		if(!(this.queue.isEmpty()) && job.getPriority() > this.queue.get(0).getPriority())
		{
			Job tempJob = this.queue.get(0);
			this.queue.set(0, job);
			this.queue.add(tempJob);
			this.numberOfJobs++;
		}else{
			this.queue.add(this.numberOfJobs, job);
			this.numberOfJobs++;	
		}	
		
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
	 * This re organises the queue depending on what has the highest priority
	 */
	public void returnJob(Job job) throws SchedulerException {
		if (!this.queue.contains(job)) throw new SchedulerException("Job not on Queue");
		
		for(int i = 0; i < this.numberOfJobs; i++)
		{
			if(this.queue.get(i).getPriority() > this.queue.get(0).getPriority())
			{
				Job tempJob = this.queue.get(0);
				int location = this.queue.indexOf(this.queue.get(i));
				this.queue.set(0, this.queue.get(i));
				this.queue.set(location, tempJob);
			}
		}
	}

	/**
	 * Removes job passes to it, decreases the total number of jobs and resorts priority
	 */
	public void removeJob(Job job) throws SchedulerException {
		if (!this.queue.contains(job)) throw new SchedulerException("Job not on Queue");
		this.queue.remove(job);
		this.numberOfJobs--;
		for(int i = 0; i < this.numberOfJobs; i++)
		{
			if(this.queue.get(i).getPriority() > this.queue.get(0).getPriority())
			{
				Job tempJob = this.queue.get(0);
				int location = this.queue.indexOf(this.queue.get(i));
				this.queue.set(0, this.queue.get(i));
				this.queue.set(location, tempJob);
			}
		}
	}

	/**
	 * Resets schedulers values
	 */
	public void reset() {
		this.queue.clear();
		this.numberOfJobs=0;
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
