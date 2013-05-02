package uk.ac.aber.rcs.cs211.schedulersim;

import uk.ac.aber.rcs.cs211.schedulersim.scheduler.SchedulerException;

public interface Scheduler {

	/**
	 * Get the next Job from the scheduler queue.
	 * This should just return the head of the queue, and not change the order in
	 * any way.
	 * @return the next job to execute.
	 * @throws SchedulerException if there are no jobs on the queue.
	 */
	public Job getNextJob() throws SchedulerException;
	
	/**
	 * Add a new job onto the queue at the relevant position.
	 * This job must not already exist on the queue.
	 * @param job The new process job to be added.
	 * @throws SchedulerException if the job already exists on the queue.
	 */
	public void addNewJob(Job job) throws SchedulerException;
	
	/**
	 * A job has had some processing done, and is now being returned to the queue.
	 * This is where a job should be moved from the front of the queue to the
	 * new position it should be occupying.
	 * @param job A job pre-existing on the queue, being returned.
	 * @throws SchedulerException if the job does not exist on the queue.
	 */
	public void returnJob(Job job) throws SchedulerException;
	
	/**
	 * Remove the Job from the queue.
	 * This may be because the job has finished, or because it has become blocked
	 * waiting for I/O
	 * @param job the job to remove.
	 * @throws SchedulerException if the job does not exist on the queue.
	 */
	public void removeJob(Job job) throws SchedulerException;
	
	/**
	 * Clear the internal data structures. 
	 *
	 */
	public void reset();
	
	/**
	 * Get a list of the jobs currently on the queue in order.
	 * @return An array of jobs in order they are on the queue.
	 */
	public Job[] getJobList();
}
