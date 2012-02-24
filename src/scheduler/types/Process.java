/**
 * 
 */
package scheduler.types;

/**
 * @author Sailee
 *
 */
public class Process {
	private int arrivalTime, totalCPUBurst, totalCPUtime, totalIOtime, pendingCPUBurst, pendingCPUTime, pendingIOtime ;
	
	public Process(int arrival, int burst, int cpu, int IO)
	{
		arrivalTime = arrival;
		totalCPUBurst = burst;
		totalCPUtime = cpu;
		totalIOtime = IO;
		
		pendingCPUBurst = burst;
		pendingCPUTime = cpu;
		pendingIOtime = IO;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public int getTotalCPUBurst() {
		return totalCPUBurst;
	}

	public int getTotalCPUtime() {
		return totalCPUtime;
	}

	public int getTotalIOtime() {
		return totalIOtime;
	}

	public int getPendingCPUBurst() {
		return pendingCPUBurst;
	}

	public int getPendingCPUTime() {
		return pendingCPUTime;
	}

	public int getPendingIOtime() {
		return pendingIOtime;
	}

}
