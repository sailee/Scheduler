package scheduler;

import java.util.Iterator;
import java.util.TreeMap;

import scheduler.types.Process;
import scheduler.types.ProcessStatus;
import scheduler.types.RandomNos;

public class FCFS {
	protected TreeMap<Integer, Process> unstarted, blocked;
	protected TreeMap<Integer, Process> complete, processes;
	protected scheduler.types.Process running;
	protected RandomNos rnd;
	protected int cpuBurst;
	protected Double clock;
	protected int processCount;
	protected TreeMap<Double, TreeMap<Integer, Process>> ready;
	private Boolean isVerbose, showRandom;

	public FCFS()
	{
		ready = new TreeMap<Double,TreeMap<Integer,Process>>();
		blocked = new TreeMap<Integer, Process>();
		complete = new TreeMap<Integer, Process>();
		processes = new TreeMap<Integer, Process>();
		rnd = new RandomNos(showRandom);
		running = null;
	}

	public FCFS(TreeMap<Integer, Process> procs, Boolean isVerboseFlag, Boolean showRandomFlag) {		
		isVerbose = isVerboseFlag;
		showRandom = showRandomFlag;

		if(showRandomFlag)
		{
			isVerbose = true;
		}

		getUnstartedProcs(procs);		
		ready = new TreeMap<Double,TreeMap<Integer,Process>>();
		blocked = new TreeMap<Integer, Process>();
		complete = new TreeMap<Integer, Process>();
		processes = new TreeMap<Integer, Process>();

		if(isVerbose)
		{
			Iterator<Integer> itr = unstarted.keySet().iterator();

			while(itr.hasNext())
			{
				Integer key = itr.next();
				Process proc = unstarted.get(key);

				processes.put(proc.getProcessID(), proc);
			}
		}
		rnd = new RandomNos(showRandomFlag);
		running = null;
		processCount = unstarted.size();
	}

	private void getUnstartedProcs(TreeMap<Integer, Process> procs) {
		String str = Integer.toString(procs.size());

		unstarted = new TreeMap<Integer, Process>();

		Iterator<Integer> itr = procs.keySet().iterator();

		while(itr.hasNext())
		{
			Integer key = itr.next();
			Process process = new Process(procs.get(key));
			str = str + process.toShortString();
			key = new Integer(key);

			unstarted.put(key, process);
		}

		System.out.println("\nThe (sorted) input is: " + str);
	}

	public void perform()
	{		
		boolean ranOneProcess = true;

		if(isVerbose || showRandom)
		{
			System.out.println("\nThis detailed printout gives the state and remaining burst for each process\n");
		}

		for(clock = 0D; complete.size() < processCount ;clock = clock+1D)
		{
			if(isVerbose)
				displayStatus();

			if(!ranOneProcess)
			{				
				processRunning();
			}	

			ranOneProcess = false;


			ranOneProcess = processRunning();
			processUnstarted();
			processBlocked();			
			processReady();				
		}
		if(isVerbose)
			displayStatus();
		displayProcessDetails();
	}	

	private void displayStatus()
	{
		Iterator<Integer> itr = processes.keySet().iterator();
		String str = "Before cycle\t" +clock.intValue() + ": ";

		while(itr.hasNext())
		{
			Integer procID = itr.next();			

			str = str + "\t" + processes.get(procID).displayStatus();
		}

		System.out.println(str);
	}

	private void processUnstarted()
	{
		Iterator<Integer> itr = unstarted.keySet().iterator();

		while(itr.hasNext())
		{
			Integer key = itr.next();
			Process proc =unstarted.get(key);

			if(proc.getArrivalTime() == clock)
			{
				itr.remove();
				proc.setStatus(ProcessStatus.ready);
				unstarted.remove(key);
				if(ready.containsKey(clock))
				{
					TreeMap<Integer, Process> map = ready.get(clock);
					map.put(proc.getProcessID(), proc);
				}
				else
				{
					TreeMap<Integer, Process> map = new TreeMap<Integer, Process>();
					map.put(proc.getProcessID(), proc);					
					ready.put(clock,map);
				}
			}
		}
	}

	protected boolean processRunning()
	{
		if(running == null)
			return false;

		if(cpuBurst > 0)						
		{														
			cpuBurst --;							
			running.run();

			//If this was the last CPU unit used, mark process as finished and set status to complete
			if(running.getPendingCPUTime() == 0)							
			{
				running.finished(clock);
				running.setStatus(ProcessStatus.terminated);								
				complete.put(running.getProcessID(), running);

				running = null;		
			}						
		}						
		else						
		{			

			int IOburst = rnd.randomOS(running.getIO(), "IO");

			running.setPendingIOBurst(IOburst);											
			running.setStatus(ProcessStatus.blocked);

			blocked.put(running.getProcessID(), running);			
			running = null;			
			return false;
		}
		return true;
	}

	protected void processReady()
	{
		if(ready.size() == 0)
			return;

		Iterator<Double> itr = ready.keySet().iterator();

		while(itr.hasNext())
		{
			Double key = itr.next();
			TreeMap<Integer, Process> map = ready.get(key);

			Iterator<Integer> mapItr = map.keySet().iterator();

			while(mapItr.hasNext())
			{
				int ProcID = mapItr.next();
				Process proc = map.get(ProcID);

				if(running == null && blocked.size()==0)
				{
					proc.setStatus(ProcessStatus.running);
					running = proc;

					cpuBurst = rnd.randomOS(proc.getBurst(),"CPU");
					proc.setPendingCPUBurst(cpuBurst);					

					mapItr.remove();
					map.remove(ProcID);
				}
				else
				{
					proc.hold();
				}
			}
		}
	}


	protected void processBlocked()
	{
		Iterator<Integer> itr = blocked.keySet().iterator(); 

		while(itr.hasNext())
		{
			Integer key = itr.next();
			Process proc =blocked.get(key);

			if (proc.getPendingIOBurst() > 0)
			{
				proc.performIO();
			}
			else
			{
				itr.remove();
				proc.setStatus(ProcessStatus.ready);
				blocked.remove(key);

				if(ready.containsKey(clock))
				{
					ready.get(clock).put(proc.getProcessID(), proc);
				}
				else
				{
					TreeMap<Integer, Process> map = new TreeMap<Integer, Process>();
					map.put(proc.getProcessID(), proc);
					ready.put(clock, map);
				}
			}
		}
	}

	private void displayProcessDetails() 
	{
		clock--;
		Double totalRunningTime =0D, totalIOTime=0D, turnAroundTime=0D, waitingTime = 0D;
		int processCount = complete.size();
		System.out.println("\nScheduling alogrithm used was : " + this.getClass().toString().replace("class scheduler.", ""));
		
		Iterator<Integer> itr = complete.keySet().iterator();		 

		while(itr.hasNext())
		{
			Integer key = itr.next();
			Process proc = complete.get(key);
			totalRunningTime += proc.getTotalCPUtime(); 
			turnAroundTime += proc.getTurnaroundTime();
			waitingTime +=proc.getWaitingTime();
			totalIOTime += proc.getTurnaroundTime() - proc.getTotalCPUtime() - proc.getWaitingTime();

			System.out.println(proc);
		}

		System.out.println("\nSummary Data:");
		System.out.println("Finishing time: " + clock.intValue());		
		
		System.out.println("CPU Utilization: " + (totalRunningTime/clock));
		System.out.println("I/O Utilization: " + (totalIOTime/clock));
		System.out.println("Throughput: "+ (100*processCount/clock) +" processes per hundred cycles");
		System.out.println("Average turnaround time: " + (turnAroundTime/processCount));
		System.out.println("Average waiting time: " + (waitingTime/processCount));
	}


}
