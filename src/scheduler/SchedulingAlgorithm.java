package scheduler;

import java.util.Iterator;
import java.util.TreeMap;

import scheduler.types.Process;
import scheduler.types.ProcessStatus;

public class SchedulingAlgorithm {
	protected TreeMap<Integer, Process> unstarted, blocked;
	protected TreeMap<Integer, Process> complete, processes;
	protected scheduler.types.Process running;
	protected RandomNos rnd;
	protected int cpuBurst;
	protected Double clock;
	protected int processCount;
	protected TreeMap<Double, TreeMap<Integer, Process>> ready;

	public SchedulingAlgorithm()
	{
		unstarted = new TreeMap<Integer, Process>();
		ready = new TreeMap<Double,TreeMap<Integer,Process>>();
		blocked = new TreeMap<Integer, Process>();
		complete = new TreeMap<Integer, Process>();
		processes = new TreeMap<Integer, Process>();
		rnd = new RandomNos();
		running = null;

	}

	public SchedulingAlgorithm(TreeMap<Integer, Process> procs) {		

		unstarted = procs;		
		ready = new TreeMap<Double,TreeMap<Integer,Process>>();
		blocked = new TreeMap<Integer, Process>();
		complete = new TreeMap<Integer, Process>();
		processes = new TreeMap<Integer, Process>();

		Iterator<Integer> itr = procs.keySet().iterator();

		while(itr.hasNext())
		{
			Integer key = itr.next();
			Process proc = procs.get(key);

			processes.put(proc.getProcessID(), proc);
		}

		rnd = new RandomNos();
		running = null;
		processCount = unstarted.size();
	}

	public void perform()
	{		
		boolean ranOneProcess = true;

		for(clock = 0D; complete.size() < processCount ;clock = clock+1D)
		{
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
		displayStatus();
		displayProcessDetails();
	}	

	private void displayStatus()
	{
		Iterator<Integer> itr = processes.keySet().iterator();
		String str = "Before cycle\t" +clock + ": ";

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
		return true;
	}
	
	protected void processReady()
	{
	}

	protected void processBlocked()
	{
	}

	private void displayProcessDetails() 
	{
		System.out.println("\nScheduling alogirthm used was : FCFS\n");
		Iterator<Integer> itr = complete.keySet().iterator();		 

		while(itr.hasNext())
		{
			Integer key = itr.next();				 

			System.out.println(complete.get(key));
		}
	}


}
