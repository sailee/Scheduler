package scheduler.types;

/**
 * @author Sailee
 *
 */
public class Process {
	private int processID, arrivalTime, burst, totalCPUtime, IO, pendingCPUTime, pendingIOBurst, readyTime, pendingCPUBurst;
	private int finishingTime, IOTime , waitingTime;
	private ProcessStatus status;

	
	public int getReadyTime() {
		return readyTime;
	}

	public void setReadyTime(int readyTime) {
		this.readyTime = readyTime;
	}
	
	public int getPendingIOBurst() {
		return pendingIOBurst;
	}

	public void setPendingIOBurst(int pendingIOBurst) {
		this.pendingIOBurst = pendingIOBurst;
	}

	

	public Process(int arrival, int burst, int cpu, int IO, int ID)
	{
		readyTime = arrival;
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

	public void finished(int time) {
		finishingTime = time;				
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

	public void run(){
		pendingCPUTime --;
		pendingCPUBurst --;
	}

	public ProcessStatus getStatus() {
		return status;
	}

	public void setStatus(ProcessStatus stat) {
		status = stat;
	}
	
	public String displayStatus()
	{
		String str = "";
		
		if(status == ProcessStatus.running)
		{
			str = Integer.toString(pendingCPUBurst);
		}
		else if(status == ProcessStatus.blocked)
		{
			str = Integer.toString(pendingIOBurst+1);
		}
		else
		{
			str = "0";
		}
			
		
		if(status == ProcessStatus.unstarted || status == ProcessStatus.terminated)
			return status.toString() +"\t" + str;
		return  status.toString() +"\t\t" + str;
	}

	public String toString() {
		String str = "\nProcess " +processID +":\n\t(A,B,C,IO) = ("+ arrivalTime+","+burst+","+totalCPUtime+","+IO+")\n\tFinishing time: "+
				finishingTime + 
				//"\n\tPending time: " + pendingCPUTime+ 
				"\n\tTurnaround time: " + getTurnaroundTime() + "\n\tI/O time: " + IOTime + "\n\tWaiting time: " + 
				waitingTime;

		return str;
	}

	public boolean isComplete()
	{
		if(finishingTime >= 0)
			return true;
		return false;
	}

	public void picked() {
		waitingTime -=1;		
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
