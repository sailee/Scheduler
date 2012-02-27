package scheduler;

import java.util.Iterator;
import java.util.TreeMap;

import scheduler.types.Process;
import scheduler.types.ProcessStatus;

public class FCFS {
	private TreeMap<Integer, Process> unstarted, blocked;
	private TreeMap<Integer, Process> complete, processes;
	private scheduler.types.Process running;
	private RandomNos rnd;
	private int cpuBurst, clock, processCount;
	private TreeMap<Integer, TreeMap<Integer, Process>> ready;

	public FCFS(TreeMap<Integer, Process> procs) {		

		unstarted = procs;		
		ready = new TreeMap<Integer,TreeMap<Integer,Process>>();
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

	public void performFCFS()
	{		
		boolean ranOneProcess = true;

		for(clock = 0; complete.size() < processCount ;clock++)
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
				running.finished(clock);
				running.setStatus(ProcessStatus.terminated);								
				complete.put(running.getProcessID(), running);

				running = null;		
			}						
		}						
		else						
		{							
			int rndBurst = rnd.randomOS(running.getIO());
			int IOburst = 1 + (rndBurst%running.getIO());

			//System.out.println("\nIO Burst:\t" + rndBurst + "\t"+ IOburst);


			running.setPendingIOBurst(IOburst);											
			running.setStatus(ProcessStatus.blocked);

			blocked.put(running.getProcessID(), running);			
			running = null;			
			return false;
		}
		return true;
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

	private void processReady()
	{
		Iterator<Integer> itr = ready.keySet().iterator();

		while(itr.hasNext())
		{
			Integer key = itr.next();
			TreeMap<Integer, Process> map = ready.get(key);

			Iterator<Integer> mapItr = map.keySet().iterator();
			
			while(mapItr.hasNext())
			{
				int ProcID = mapItr.next();
				Process proc = map.get(ProcID);

				if(running == null)
				{
					proc.setStatus(ProcessStatus.running);
					running = proc;
					
					int rndBurst = rnd.randomOS(proc.getBurst());; 
					cpuBurst = 1 + (rndBurst%proc.getBurst());
					proc.setPendingCPUBurst(cpuBurst);
					//System.out.println("\nCPU Burst:\t" + rndBurst+"\t" + cpuBurst);
					
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

	private void processBlocked()
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
				proc.setReadyTime(clock);
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
		System.out.println("\nScheduling alogirthm used was : FCFS\n");
		Iterator<Integer> itr = complete.keySet().iterator();		 

		while(itr.hasNext())
		{
			Integer key = itr.next();				 

			System.out.println(complete.get(key));
		}
	}

}
