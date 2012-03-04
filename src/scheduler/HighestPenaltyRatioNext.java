package scheduler;

import java.util.Iterator;
import java.util.TreeMap;
import scheduler.types.Process;
import scheduler.types.ProcessStatus;


public class HighestPenaltyRatioNext extends FCFS {

	public HighestPenaltyRatioNext(TreeMap<Integer,Process> procs, Boolean isVerboseFlag, Boolean showRandomFlag) {		
		super(procs, isVerboseFlag, showRandomFlag);
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
}

