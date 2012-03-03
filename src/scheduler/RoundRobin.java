/**
 * 
 */
package scheduler;

import java.util.Iterator;
import java.util.TreeMap;

import scheduler.types.Process;
import scheduler.types.ProcessStatus;

/**
 * @author Sailee
 *
 */
public class RoundRobin extends SchedulingAlgorithm {
	private int quantum;
	public RoundRobin(TreeMap<Integer, Process> procs, int quantum) {		

		super(procs);
		this.quantum = quantum;		
	}

	@Override
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
		else if(cpuBurst == 0)						
		{	
			int IOburst = rnd.randomOS(running.getIO(), "IO");

			running.setPendingIOBurst(IOburst);											
			running.setStatus(ProcessStatus.blocked);

			blocked.put(running.getProcessID(), running);			
			running = null;			
			return false;
		}
		else
		{
			running.setStatus(ProcessStatus.ready);
			return false;
		}
		
		return true;
	}

	
	@Override
	protected void processReady()
	{
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

				if(running == null)
				{
					proc.setStatus(ProcessStatus.running);
					running = proc;

					cpuBurst = rnd.randomOS(proc.getBurst(),"CPU");					
					
					proc.setPendingCPUBurst(cpuBurst);				
					
					if(cpuBurst > quantum)
						cpuBurst = quantum;

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
}
