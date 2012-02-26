package scheduler;

import java.util.Comparator;
import java.util.Iterator;

import scheduler.types.DualKey;
import scheduler.types.DualKeyTreeMap;
import scheduler.types.Process;
import scheduler.types.ProcessStatus;

public class FCFS {
	private static DualKeyTreeMap<Integer, Integer, Process> unstarted, ready, blocked, complete, processes;
	private static scheduler.types.Process running;
	private static RandomNos rnd;
	private static int cpuBurst, clock, processCount;

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
		complete = new DualKeyTreeMap<Integer, Integer, Process>(cmp, cmp);
		processes = new DualKeyTreeMap<Integer, Integer, Process>(cmp, cmp);

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

			if(!ranOneProcess && ready.size() > 0)
			{
				ready.remove(running.getArrivalTime(),running.getProcessID());
				processRunning();
			}		
		}

		displayProcessDetails();
	}	

	private void displayStatus()
	{
		String str = "";
		DualKey<Integer, Integer> key;
		
		processes.clear();
		
		Iterator<DualKey<Integer, Integer>> itr = unstarted.keySet().iterator();		

		while(itr.hasNext())
		{
			key = itr.next();
			processes.put(key, unstarted.get(key));
		}
		
		itr = ready.keySet().iterator();		

		while(itr.hasNext())
		{
			key = itr.next();
			processes.put(key, ready.get(key));
		}
		
		itr = blocked.keySet().iterator();		

		while(itr.hasNext())
		{
			key = itr.next();
			processes.put(key, blocked.get(key));
		}
		
		itr = complete.keySet().iterator();		

		while(itr.hasNext())
		{
			key = itr.next();
			processes.put(key, complete.get(key));
		}
		
		if(running != null)
			processes.put(running.getArrivalTime(), running.getProcessID(), running);
		
		itr = processes.keySet().iterator();
		
		while(itr.hasNext())
		{
			System.out.println(processes.get(itr.next()).getStatus());
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
				running = null;						
				complete.put(running.getArrivalTime(), running.getProcessID(), running);
			}						
		}						
		else						
		{							
			int IOburst = rnd.randomOS(running.getIO());							
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
				//ready.remove(key);
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
				proc.setStatus(ProcessStatus.Ready);
				blocked.remove(key);
				ready.put(key, proc);
			}
		}
	}

	private void displayProcessDetails() 
	{
		Iterator<DualKey<Integer, Integer>> itr = unstarted.keySet().iterator();		 

		while(itr.hasNext())
		{
			DualKey<Integer, Integer> key = itr.next();				 

			System.out.println(unstarted.get(key));
		}
	}

}
