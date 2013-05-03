package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import java.util.*;

import uk.ac.aber.rcs.cs211.schedulersim.*;

/**
 * A High response ratio next algorithm.
 * This will calculate priority based upon time left for completion and time spent waiting.
 * This stops the issue of starvation by taking into account the wait time.
 * This will happen until all processes are complete. 
 * @author cpm4(edited version of uk/ac/aber/rcs/cs211/schedulersim/scheduler/FirstComeFirstServed.java)
 * @see uk.ac.aber.rcs.cs211.schedulersim.Simulator
 *
 */
public class HighResponseRatioNext implements Scheduler {

	protected ArrayList<Job> queue;
	private int numberOfJobs;
	private ArrayList<Double> waitTime;
	private ArrayList<Double> newPriority;
	
	public HighResponseRatioNext () {
		this.queue = new ArrayList<Job>();
		this.numberOfJobs=0;
		
		this.waitTime = new ArrayList<Double>();
		this.newPriority = new ArrayList<Double>();
	}
	
	/**
	 * Checks job adding to see if higher priority than the job at the head of the queue and if it is put at
	 * front of the queue
	 */
	public void addNewJob(Job job) throws SchedulerException {
		if (this.queue.contains(job)) throw new SchedulerException("Job already on Queue");
		
		//each job gets a wait time of zero to start
		double timeRem = job.getLength() - job.getProgramCounter();
		//wait time 0 as new job has not waited
		double priority = (0 + timeRem)/timeRem;
		
		//checks priority of newly added job to see if greater than job at head of queue
		if(!(this.queue.isEmpty()) && priority > this.newPriority.get(0))
		{
			this.queue.add(this.numberOfJobs, this.queue.get(0));
			this.waitTime.add(this.numberOfJobs, 0.0);
			double newTime = this.queue.get(this.numberOfJobs).getLength() - this.queue.get(this.numberOfJobs).getProgramCounter();
			double newPriority = (0 + newTime)/newTime;
			this.newPriority.add(this.numberOfJobs, newPriority);
			
			this.queue.set(0, job);
			this.waitTime.set(0, 0.0);
			this.newPriority.set(0, priority);
			this.numberOfJobs++;
		}else{
			this.waitTime.add(this.numberOfJobs, 0.0);
			this.queue.add(this.numberOfJobs, job);
			this.newPriority.add(this.numberOfJobs, priority);
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
	 * This will create new updated priorities for each item in the queue and update
	 * their position based on their new priority. The priority is calculated by
	 * taking into account the remaining time of the job and the weight time. This
	 * avoids starvation.
	 */
	public void returnJob(Job job) throws SchedulerException {
		if (!this.queue.contains(job)) throw new SchedulerException("Job not on Queue");

		//sets up to date priorities and save 
		for(int i = 0; i < this.numberOfJobs-1; i++)
		{
			double priority = 0.0;
			//increase all but first jobs wait time
			if(i != 0)
			{
				this.waitTime.set(i, this.waitTime.get(i)+1);
			}
			
			//updates priority
			int timeRem = this.queue.get(i).getLength() - this.queue.get(i).getProgramCounter();
			if(timeRem != 0)
			{
				priority = (this.waitTime.get(i)+timeRem)/timeRem;
				this.newPriority.set(i, priority);
			}
			
			//updates the highest priority job position
			if(this.newPriority.get(i) > this.newPriority.get(0))
			{
				Job tempJob = this.queue.get(0);
				
				this.queue.set(0, this.queue.get(i));
				this.waitTime.set(0, 0.0);
				double newTime = this.queue.get(0).getLength() - this.queue.get(0).getProgramCounter();
				double newPriority = (0 + newTime)/newTime;
				this.newPriority.set(0, newPriority);
				
				this.queue.set(i, tempJob);
				this.waitTime.set(i, 0.0);
				this.newPriority.set(i, priority);
			}
		}
	}

	
	/**
	 * Removes job passes to it, decreases the total number of jobs and resets the clock allowance
	 */
	public void removeJob(Job job) throws SchedulerException {
		if (!this.queue.contains(job)) throw new SchedulerException("Job not on Queue");
		this.queue.remove(job);
		this.numberOfJobs--;
	}

	/**
	 * Resets schedulers values
	 */
	public void reset() {
		this.queue.clear();
		this.numberOfJobs=0;
		this.waitTime.clear();
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
