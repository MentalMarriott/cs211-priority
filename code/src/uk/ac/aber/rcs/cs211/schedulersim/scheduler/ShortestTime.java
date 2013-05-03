package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import java.util.*;
import uk.ac.aber.rcs.cs211.schedulersim.*;

/**
 * A first come, first served scheduling algorithm.
 * It will keep re-presenting the same job each time getNextjob is called, until
 * that job is removed from the queue, either because it has finished, or it gets
 * blocked for I/O. 
 * @author cpm4(edited version of uk/ac/aber/rcs/cs211/schedulersim/scheduler/FirstComeFirstServed.java)
 * @see uk.ac.aber.rcs.cs211.schedulersim.Simulator
 *
 */
public class ShortestTime implements Scheduler {

	protected ArrayList<Job> queue;
	private int numberOfJobs;
	
	public ShortestTime () {
		this.queue = new ArrayList<Job>();
		this.numberOfJobs=0;
	}
	
	/**
	 * When adding job checks to see how much time will take and if less than
	 * first in queue it will be put to the head of the queue.
	 */
	public void addNewJob(Job job) throws SchedulerException {
		if (this.queue.contains(job)) throw new SchedulerException("Job already on Queue");

		if(!(this.queue.isEmpty()) && (job.getLength()) < (this.queue.get(0).getLength()))
		{
			Job tempJob = this.queue.get(0);
			this.queue.set(0, job);
			this.queue.add(this.numberOfJobs, tempJob);			
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
	 * Organises queue in order of shortest job first
	 */
	public void returnJob(Job job) throws SchedulerException {
		if (!this.queue.contains(job)) throw new SchedulerException("Job not on Queue");
		
		for(int i = 0; i < this.numberOfJobs; i++)
		{
			int length = this.queue.get(i).getLength();
			int queueHead = this.queue.get(0).getLength();
			if(length < queueHead)
			{
				Job tempJob = this.queue.get(0);
				this.queue.set(0, this.queue.get(i));
				this.queue.set(this.queue.indexOf(this.queue.get(i)), tempJob);					
			}
		}
	
	}

	/**
	 * removes the job given and resorts the queue to put shortest job at front
	 */
	public void removeJob(Job job) throws SchedulerException {
		if (!this.queue.contains(job)) throw new SchedulerException("Job not on Queue");
		
		this.queue.remove(job);
		this.numberOfJobs--;
		
		for(int i = 0; i < this.numberOfJobs; i++)
		{
			int timeRem = this.queue.get(i).getLength();
			int queueHead = this.queue.get(0).getLength();
			if(timeRem < queueHead)
			{
				Job tempJob = this.queue.get(0);
				this.queue.set(0, this.queue.get(i));
				this.queue.set(i, tempJob);					
			}
		}
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
