package scheduler.types;

/**
 * @author Sailee
 *
 */
public class Process {
	private int processID, arrivalTime, burst, totalCPUtime, IO, pendingCPUTime, pendingIOBurst, pendingCPUBurst; 	
	private int finishingTime, IOTime , waitingTime;
	private ProcessStatus status;
	
	public Process(int arrival, int burst, int cpu, int IO, int ID)
	{		
		arrivalTime = arrival;
		this.burst = burst;
		totalCPUtime = cpu;
		this.IO = IO;

		pendingCPUTime = cpu;

		finishingTime = -1;		
		IOTime = 0;
		waitingTime = 0;

		processID = ID;
		status = ProcessStatus.unstarted;
	}
	
	public Process(Process process) {
		arrivalTime = process.arrivalTime;
		this.burst = process.burst;
		totalCPUtime = process.totalCPUtime;
		this.IO = process.IO;

		pendingCPUTime = process.totalCPUtime;

		finishingTime = -1;		
		IOTime = 0;
		waitingTime = 0;

		processID = process.processID;
		status = ProcessStatus.unstarted;
	}

	public int getPendingIOBurst() {
		return pendingIOBurst;
	}

	public void setPendingIOBurst(int pendingIOBurst) {
		this.pendingIOBurst = pendingIOBurst;
	}

	public int getProcessID() {
		return processID;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public int getBurst() {
		return burst;
	}

	public int getTotalCPUtime() {
		return totalCPUtime;
	}

	public int getIO() {
		return IO;
	}

	public int getPendingCPUTime() {
		return pendingCPUTime;
	}

	public void finished(Double clock) {
		finishingTime = clock.intValue();				
	}

	public int getFinishingTime() {
		return finishingTime;
	}

	public int getTurnaroundTime() {
		return finishingTime - arrivalTime;
	}

	public int getIOTime() {
		return IOTime;
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public void performIO()	{
		IOTime++;
		pendingIOBurst --;
	}

	public void hold() {
		waitingTime++;		
	}
	
	public void unHold() {
		waitingTime--;		
	}

	public void run(){
		pendingCPUTime --;
		pendingCPUBurst --;
	}

	public void setStatus(ProcessStatus stat) {
		status = stat;
	}
	
	public ProcessStatus getStatus() {
		return status;
	}
	
	public String displayStatus()
	{
		String str = "";
		
		if(status == ProcessStatus.running)
		{
			str = Integer.toString(pendingCPUBurst+1);
		}
		else if(status == ProcessStatus.blocked)
		{
			str = Integer.toString(pendingIOBurst);
		}
		else
		{
			str = "0";
		}			
		
//		if(status == ProcessStatus.unstarted || status == ProcessStatus.terminated)
//			return status.toString() +"\t" + str;
		return  status.toString() +"\t" + str;
	}

	public String toShortString()
	{
		return " ("+ arrivalTime+","+burst+","+totalCPUtime+","+IO+")";
	}
	
	public String toString() {
		String str = "\nProcess " +processID +":\n\t(A,B,C,IO) = ("+ arrivalTime+","+burst+","+totalCPUtime+","+IO+")\n\tFinishing time: "+
				finishingTime + 
				//"\n\tPending time: " + pendingCPUTime+ 
				"\n\tTurnaround time: " + getTurnaroundTime() + "\n\tI/O time: " + IOTime + "\n\tWaiting time: " + 
				waitingTime;

		return str;
	}

	/**
	 * @return the pendingCPUBurst
	 */
	public int getPendingCPUBurst() {
		return pendingCPUBurst;
	}

	/**
	 * @param pendingCPUBurst the pendingCPUBurst to set
	 */
	public void setPendingCPUBurst(int pendingCPUBurst) {
		this.pendingCPUBurst = pendingCPUBurst;
	}

}
