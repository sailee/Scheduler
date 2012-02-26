package scheduler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

import scheduler.types.DualKey;
import scheduler.types.DualKeyTreeMap;
import scheduler.types.Process;
import scheduler.types.ProcessStatus;

public class Main {

	private static DualKeyTreeMap<Integer, Integer, Process> processes;
	private static scheduler.types.Process running;
	private static RandomNos rnd; 

	/**
	 * @param args Path where the input is located
	 */
	public static void main(String[] args) 
	{
		try
		{
			init();

			if (args.length > 0) 
			{			
				fetchProcessList(args[0]);
			} 
			else
				fetchProcessList("test1.txt");

			performFCFS();

			displayProcessDetails();
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}

	}

	private static void init() {
		Comparator<Integer> cmp = new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}

		};

		processes = new DualKeyTreeMap<Integer, Integer, Process>(cmp, cmp);
	

		rnd = new RandomNos();
		running = null;
	}

	private static void performFCFS()
	{
		int cpuBurst=-1, clock = 0, IOburst = 0, completeProcessCount=0;		

		for(clock = 0; completeProcessCount != processes.size() ;clock++)
		{
			String str = "Before cycle "+ clock;

			Iterator<DualKey<Integer, Integer>> itr = processes.keySet().iterator();
			completeProcessCount = 0;

			while(itr.hasNext())
			{
				DualKey<Integer, Integer> key = itr.next();
				Process proc = processes.get(key);
				str = str + ":\t"+proc.getStatus() + " ";				

				if (proc.getStatus() == ProcessStatus.Complete)
				{
					completeProcessCount ++;				
				}
				else
				{
					if (proc.getStatus() == ProcessStatus.Blocked)
					{						
						if(proc.getPendingIOBurst() > 0)
						{							
							proc.performIO();
						}
						
						if (proc.getPendingIOBurst() == 0)
						{
							proc.setStatus(ProcessStatus.Ready);
						}	

					}
					if (proc.getStatus() ==ProcessStatus.unstarted)
					{						
						if(clock == proc.getArrivalTime())
						{
							proc.setStatus(ProcessStatus.Ready);
						}		
					}
					if (proc.getStatus() ==ProcessStatus.Ready)
					{
						if(running == null)
						{
							proc.setStatus(ProcessStatus.Running);
						}
						else
						{							
							proc.hold();
						}
					}
					if (proc.getStatus() ==ProcessStatus.Running)
					{						
						if(running == null)
						{
							running = proc;						
							cpuBurst = rnd.randomOS(proc.getBurst());							
						}

						if(cpuBurst > 0)
						{							
							cpuBurst --;
							proc.run();

							//If this was the last CPU unit used, mark process as finished and set status to complete
							if(proc.getPendingCPUTime() == 0)
							{
								proc.finished(clock+1);
								proc.setStatus(ProcessStatus.Complete);
								running = null;
							}
						}
						else
						{
							IOburst = rnd.randomOS(proc.getIO());
							proc.setPendingIOBurst(IOburst);				
							proc.setStatus(ProcessStatus.Blocked);
							running = null;			
							
						}			

					}						
				}				
				
			}
			
			//even after going through all processes, if no process is running, try to put the first ready process to running
			if(running == null && completeProcessCount < processes.size())
			{
				Iterator<DualKey<Integer, Integer>> itr1 = processes.keySet().iterator();
				
				while(itr1.hasNext())
				{
					Process proc =processes.get(itr1.next()) ; 
					if(proc.getStatus() == ProcessStatus.Ready)
					{
						proc.picked();
						proc.setStatus(ProcessStatus.Running);
						running = proc;						
						cpuBurst = rnd.randomOS(proc.getBurst());
						cpuBurst -- ;
						proc.run();

						//If this was the last CPU unit used, mark process as finished and set status to complete
						if(proc.getPendingCPUTime() == 0)
						{
							proc.finished(clock+1);
							proc.setStatus(ProcessStatus.Complete);
							running = null;
						}							
					}
				}
			}
			
			System.out.println(str);
		}
	}	


	private static void displayProcessDetails() {
		Iterator<DualKey<Integer, Integer>> itr = processes.keySet().iterator();		 

		while(itr.hasNext())
		{
			DualKey<Integer, Integer> key = itr.next();				 

			System.out.println(processes.get(key));
		}
	}

	private static void fetchProcessList(String fileName) throws Exception
	{
		FileReader fread;
		Scanner scan;		 

		try 
		{
			fread = new FileReader(fileName);
			scan = new Scanner(fread);

			if(scan.hasNext())
			{
				int processcount = scan.nextInt();

				for(int i=0;i<processcount;i++)
				{
					String str = scan.next();
					int arrival, burst, cpu, IO;

					if (str.length() >1 && str.startsWith("("))
							{
						str = str.replace("(", "");
						arrival = Integer.parseInt(str);
							}
					else
					{
						arrival = scan.nextInt();
					}
					burst = scan.nextInt();
					cpu = scan.nextInt();

					str = scan.next();

					if(str.length() > 1 && str.endsWith(")"))
					{
						str = str.replace(")", "");
						IO = Integer.parseInt(str);
					}
					else
					{
						IO = scan.nextInt();
						scan.next();
					}

					Process p = new Process(arrival,burst, cpu, IO,i);

					processes.put(arrival, i, p);										
				}
			}

		} 
		catch (FileNotFoundException e) 
		{
			throw new FileNotFoundException("Invalid file name specified.");			
		}
		catch(Exception ex)
		{
			throw new Exception("Input format incorrect");
		}

	}

}
