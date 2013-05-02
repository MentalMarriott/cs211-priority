package uk.ac.aber.rcs.cs211.schedulersim;

public class Job {
	private String name;
	private int priority;
	private int startCycle;
	private int length;
	private int instructions[];
	private int programCounter=0;
	private int blockedTime=0;
	private int finishTime=0;
	
	public Job (String descriptor) {
		String[] temp=descriptor.split(";");
		
		String[] namePrio = temp[0].split(":");
		this.name = namePrio[0];
		this.priority = Integer.parseInt(namePrio[1]);
	
		
		this.startCycle = Integer.parseInt(temp[1]);
		
		String [] instrs = temp[2].split(",");
		instructions = new int[instrs.length];
		length=0;
		for (int i=0;i<instrs.length; i++) {
			instructions[i]=Integer.parseInt(instrs[i]);
			length += instructions[i];
		}
	}


	/**
	 * Returns a char representing the current instruction.
	 * The current instruction is the one referenced by the Program counter.
	 * Returns a 'C' for a cpu instruction
	 * Returns a 'I' for an I/O cycle
	 * @return a character repesenting the type of instruction 
	 */
	public char getInstruction() {
		
		// This is a fix to return a C if we end on an I/O cycle.
		// rcs - 3/6/10
		if (this.programCounter>this.getLength()) {
			return 'C';
		}
		// end of fix
		
		int total=0;
		int i=0;
		for (; i<this.instructions.length && 
				total<=this.programCounter;  i++) {
			total += instructions[i];
		}
		i--;
		return (i%2==0)?'C':'I';
	}

	/**
	 * The amount of time this process has left on the blocked queue.
	 * 
	 * @return 0 if not blocked, otherwise number of cycles left blocked.
	 */
	public int timeToReady() {
		int total=0;
		int i=0;
		for (; i<this.instructions.length && 
				total<this.programCounter;  i++) {
			total += instructions[i];
		}
		i--;
		return (i%2==0)?0:total-this.programCounter;
	}

	/**
	 * Gets the name of this job.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the priority of this job.
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}


	/**
	 * The current program counter.
	 * @return the programCounter
	 */
	public int getProgramCounter() {
		return programCounter;
	}

	/**
	 * Increments the program counter.
	 *
	 */
	public void incrementProgramCounter() {
		programCounter++;
	}
	
	public void incrementBlockedTime() {
		blockedTime++;
	}
	
	/**
	 * The delay before this job starts.
	 * This is the delay before it gets put onto the ready queue.
	 * @return the startCycle
	 */
	public int getStartCycle() {
		return startCycle;
	}

	/**
	 * The overall length of the job.
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	
	
	public boolean isFinished() {
		return programCounter>length;
	}


	/**
	 * @return the finishTime
	 */
	public int getFinishTime() {
		return finishTime;
	}

	public int getBlockedTime() {
		return blockedTime;
	}


	/**
	 * @param finishTime the finishTime to set
	 */
	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
	}
	
	public int getElapsedDuration() {
		if (finishTime==0) return 0;
		return finishTime-startCycle;
	}
	
	public String toString() {
		return name+":"+priority+"\t"+getElapsedDuration();
	}
	
	
	// the strings for the update window displays.
	// They must take no parameters and return a string.
	// They are invoked using reflection from the QueuePanel.
	
	public String getReadyString() {
		return Integer.toString(getProgramCounter())+"/"+
				getLength();
	}

	public String getWaitString() {
		return Integer.toString(getStartCycle());
	}
	
	public String getFinishString() {
		return Integer.toString(getElapsedDuration());
	}
	
	public String getBlockedString() {
		return Integer.toString(timeToReady());
	}
	
}
