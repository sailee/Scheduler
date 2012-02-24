package scheduler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import scheduler.types.Process;

public class Main {

	private static HashMap<Integer, ArrayList<scheduler.types.Process>> processes;
	//	private static Queue<scheduler.types.Process> ready, blocked;
	//	private static scheduler.types.Process running;

	/**
	 * @param args Path where the input is located
	 */
	public static void main(String[] args) 
	{
		try
		{
			if (args.length > 0) 
			{			
				fetchProcessList(args[0]);
			} 
			else
				fetchProcessList("test1.txt");

			System.out.println(processes);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}

	}

	private static void fetchProcessList(String fileName) throws Exception
	{
		FileReader fread;
		Scanner scan;
		processes = new HashMap<Integer, ArrayList<Process>>();

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
					
					Process p = new Process(arrival,burst, cpu, IO);

					if(!processes.containsKey(arrival))
					{
						processes.put(arrival, new ArrayList<Process>());
					}
					processes.get(arrival).add(p);					
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
