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
public class RoundRobin extends FCFS {
	private int quantum;
	public RoundRobin(TreeMap<Integer, Process> procs, int quantum, Boolean isVerboseFlag, Boolean showRandomFlag) {		

		super(procs, isVerboseFlag, showRandomFlag);
		this.quantum = quantum;		
	}

	@Override
	protected void processRunning()
	{		
		if(running == null)
			return ;

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
			return ;
		}
		else
		{
			running.setStatus(ProcessStatus.ready);
			return;
		}

		return;
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
}
