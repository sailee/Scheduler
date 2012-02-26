package scheduler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.Scanner;

import scheduler.types.DualKeyTreeMap;
import scheduler.types.Process;

public class Main {

	private static DualKeyTreeMap<Integer, Integer, Process> processes;	

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

			FCFS f = new FCFS(processes);
			f.performFCFS();			
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
