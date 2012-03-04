package scheduler;

import java.util.Iterator;
import java.util.TreeMap;

import scheduler.types.Process;
import scheduler.types.ProcessStatus;

public class Uniprogrammed extends FCFS {	

	public Uniprogrammed(TreeMap<Integer, Process> procs, Boolean isVerboseFlag, Boolean showRandomFlag) {		

		super(procs, isVerboseFlag, showRandomFlag);	
	}
	
	@Override
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

			if(proc.getPendingIOBurst() == 0)
			{
				itr.remove();
				proc.setStatus(ProcessStatus.running);
				blocked.remove(key);
				
				cpuBurst = rnd.randomOS(proc.getBurst(),"CPU");
				proc.setPendingCPUBurst(cpuBurst);

//				if(ready.containsKey(-1D))
//				{
//					ready.get(-1D).put(proc.getProcessID(), proc);
//				}
//				else
//				{
//					TreeMap<Integer, Process> map = new TreeMap<Integer, Process>();
//					map.put(proc.getProcessID(), proc);
//					ready.put(-1D, map);
//				}
			}
		}
	}

	@Override
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

				if(running == null && blocked.size() == 0)
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

			if(ready.get(key).size() == 0)
				ready.remove(key);
		}
	}

	@Override
	protected void processRunning()
	{
		if(running == null)
			return;
		
		if(running.getPendingCPUTime() > 0)
		{
			if(cpuBurst > 0 )						
			{														
				cpuBurst --;							
				running.run();
				return;
			}

			if(cpuBurst == 0 && running.getStatus() == ProcessStatus.running)						
			{	
				int IOburst = rnd.randomOS(running.getIO(), "IO");

				running.setPendingIOBurst(IOburst);											
				running.setStatus(ProcessStatus.blocked);

				blocked.put(running.getProcessID(), running);
				//running = null;
				//runNextProcess();
				return;
			}
		}
		//If this was the last CPU unit used, mark process as finished and set status to complete
		if(running.getPendingCPUTime() == 0)							
		{
			running.finished(clock);
			running.setStatus(ProcessStatus.terminated);								
			complete.put(running.getProcessID(), running);
			running = null;

			runNextProcess();
		}

		return;
	}


}
