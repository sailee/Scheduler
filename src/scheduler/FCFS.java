package scheduler;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

import scheduler.types.DualKey;
import scheduler.types.DualKeyTreeMap;
import scheduler.types.Process;
import scheduler.types.ProcessStatus;

public class FCFS {
	private DualKeyTreeMap<Integer, Integer, Process> unstarted, ready, blocked;
	private TreeMap<Integer, Process> complete, processes;
	private scheduler.types.Process running;
	private RandomNos rnd;
	private int cpuBurst, clock, processCount;
	
	public FCFS(DualKeyTreeMap<Integer, Integer, Process> procs) {
		Comparator<Integer> cmp = new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}

		};

		unstarted = procs;		
		ready =new DualKeyTreeMap<Integer, Integer, Process>(cmp, cmp);
		blocked = new DualKeyTreeMap<Integer, Integer, Process>(cmp, cmp);
		complete = new TreeMap<>();
		processes = new TreeMap<>();
		
		Iterator<DualKey<Integer, Integer>> itr = procs.keySet().iterator();
		
		while(itr.hasNext())
		{
			DualKey<Integer, Integer> key = itr.next();
			
			processes.put(key.getProcessID(), procs.get(key));
		}

		rnd = new RandomNos();
		running = null;
		processCount = unstarted.size();
	}

	public void performFCFS()
	{		
		boolean ranOneProcess;

		for(clock = 0; complete.size() < processCount ;clock++)
		{
			ranOneProcess = false;
			displayStatus();

			ranOneProcess = processRunning();
			processUnstarted();
			processBlocked();			
			processReady();

			if(!ranOneProcess)
			{				
				processRunning();
			}		
		}
		displayStatus();
		displayProcessDetails();
	}	

	private void displayStatus()
	{
		Iterator<Integer> itr = processes.keySet().iterator();
		String str = clock + ": ";
		
		while(itr.hasNext())
		{
			Integer procID = itr.next();			
			
			str = str + "\t\t" + processes.get(procID).getStatus();
		}
		
		System.out.println(str);
	}

	private boolean processRunning()
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
				running.finished(clock+1);
				running.setStatus(ProcessStatus.Complete);								
				complete.put(running.getProcessID(), running);
				
				running = null;		
			}						
		}						
		else						
		{							
			int IOburst = rnd.randomOS(running.getIO());
			System.out.println("IO Burst: " + IOburst);
			running.setPendingIOBurst(IOburst);											
			running.setStatus(ProcessStatus.Blocked);
			
			blocked.put(running.getArrivalTime(), running.getProcessID(), running);			
			running = null;			
			return false;
		}
		return true;
	}

	private void processUnstarted()
	{
		Iterator<DualKey<Integer, Integer>> itr = unstarted.keySet().iterator();

		while(itr.hasNext())
		{
			DualKey<Integer, Integer> key = itr.next();
			Process proc =unstarted.get(key);

			if(proc.getArrivalTime() == clock)
			{
				itr.remove();
				proc.setStatus(ProcessStatus.Ready);
				unstarted.remove(key);
				ready.put(key, proc);
			}
		}
	}
	
	private void processReady()
	{
		Iterator<DualKey<Integer, Integer>> itr = ready.keySet().iterator();

		while(itr.hasNext())
		{
			DualKey<Integer, Integer> key = itr.next();
			Process proc =ready.get(key);

			if(running == null)
			{
				proc.setStatus(ProcessStatus.Running);
				running = proc;
				cpuBurst = rnd.randomOS(proc.getBurst());
				System.out.println("CPU Burst: " + cpuBurst);
				itr.remove();
				ready.remove(key);
			}
			else
			{
				proc.hold();
			}
		}
		
	}
	
	private void processBlocked()
	{
		Iterator<DualKey<Integer, Integer>> itr = blocked.keySet().iterator(); 
		
		while(itr.hasNext())
		{
			DualKey<Integer, Integer> key = itr.next();
			Process proc =blocked.get(key);
			
			if (proc.getPendingIOBurst() > 0)
			{
				proc.performIO();
			}
			else
			{
				itr.remove();
				proc.setStatus(ProcessStatus.Ready);
				blocked.remove(key);
				proc.setReadyTime(clock);
				ready.put(clock, key.getProcessID(), proc);
			}
		}
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
