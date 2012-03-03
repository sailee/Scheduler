package scheduler;

import java.util.Iterator;
import java.util.TreeMap;
import scheduler.types.Process;
import scheduler.types.ProcessStatus;


public class HighestPenaltyRatioNext extends SchedulingAlgorithm {

	public HighestPenaltyRatioNext(TreeMap<Integer,Process> procs) {		
		super(procs);
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

	@Override
	protected void processReady()
	{
		int ProcID;
		Process proc;
		
		ready = recomputePenalties();
				
		Iterator<Double> itr = ready.descendingKeySet().iterator();

		while(itr.hasNext())
		{
			Double key = itr.next();
			TreeMap<Integer, Process> map = ready.get(key);

			Iterator<Integer> mapItr = map.keySet().iterator();

			while(mapItr.hasNext())
			{
				ProcID = mapItr.next();
				proc = map.get(ProcID);

				if(running == null)
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


	private TreeMap<Double, TreeMap<Integer, Process>> recomputePenalties() {
		TreeMap<Double, TreeMap<Integer, Process>> temp = new TreeMap<Double, TreeMap<Integer,Process>>(); 
		int ProcID;
		Process proc;

		Iterator<Double> itr = ready.keySet().iterator();

		while(itr.hasNext())
		{
			Double key = itr.next();
			TreeMap<Integer, Process> map = ready.get(key);

			Iterator<Integer> mapItr = map.keySet().iterator();

			while(mapItr.hasNext())
			{
				ProcID = mapItr.next();
				proc = map.get(ProcID);

				//r = T/t; where T is the wall clock time this process has been in system and t is the running time of the process to date.
				Double penaltyRatio = (clock - proc.getArrivalTime())/Math.max(1D, proc.getTotalCPUtime() - proc.getPendingCPUTime()); 
				if(temp.containsKey(penaltyRatio))
				{
					temp.get(penaltyRatio).put(ProcID, proc);
				}
				else
				{
					TreeMap<Integer, Process> m = new TreeMap<Integer,Process>();
					m.put(ProcID, proc);

					temp.put(penaltyRatio, m);
				}
			}

		}
		return temp;
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

