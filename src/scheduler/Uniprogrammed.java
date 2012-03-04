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
		if(blocked.size() == 0)
			return;

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

				if(ready.containsKey(-1D))
				{
					ready.get(-1D).put(proc.getProcessID(), proc);
				}
				else
				{
					TreeMap<Integer, Process> map = new TreeMap<Integer, Process>();
					map.put(proc.getProcessID(), proc);
					ready.put(-1D, map);
				}
			}
		}
	}	
}
