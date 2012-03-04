package scheduler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.TreeMap;

import scheduler.types.Process;

public class Main {

	private static TreeMap<Integer, Process> processes;	
	private static String fileName, algo;
	private static Boolean isVerbose, showRandom;
	private static int quantum;
	/**
	 * @param args Path where the input is located
	 */
	public static void main(String[] args) 
	{
		quantum = 2;
		isVerbose = false; 
		showRandom = false;
		Boolean isSuccess = false;

		try
		{
			init();			

			switch(args.length)
			{
			case 0:
				isSuccess = false;
				break;
			case 1:
				isSuccess = processInput(null,args[0],null);
				break;
			case 2:
				isSuccess = processInput(args[0], args[1], null);
				break;
			case 3:
				isSuccess = processInput(args[0], args[1], args[2]);
				break;
			}			

			if(!isSuccess)
			{
				System.out.println("Invalid arguments passed to main. Please type java scheduler.Main --help.");
				return;
			}
			
			String str = "solution_"+fileName;
			PrintStream out = new PrintStream(new FileOutputStream(str)); 
			System.setOut(out);
			
			fetchProcessList(fileName);		
			
			FCFS algorithm;				
			
			if(algo.equalsIgnoreCase("FCFS"))
			{
				algorithm = new FCFS(processes, isVerbose, showRandom);
			}
			else if(algo.equalsIgnoreCase("Uniprogrammed"))
			{
				algorithm = new Uniprogrammed(processes, isVerbose, showRandom);
			}
			else if(algo.equalsIgnoreCase("Roundrobin"))
			{
				algorithm = new RoundRobin(processes, quantum, isVerbose, showRandom);
			}
			else if(algo.equalsIgnoreCase("HPRN"))
			{
				algorithm = new HighestPenaltyRatioNext(processes, isVerbose, showRandom);
			}
			else
			{
				algorithm = new FCFS(processes, isVerbose, showRandom);
				algorithm.perform();
				algorithm = new Uniprogrammed(processes, isVerbose, showRandom);
				algorithm.perform();
				algorithm = new RoundRobin(processes, quantum, isVerbose, showRandom);
				algorithm.perform();
				algorithm = new HighestPenaltyRatioNext(processes, isVerbose, showRandom);
			}
			algorithm.perform();
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}

	}

	private static boolean processInput(String flags, String filePath, String algorithm) {
		
		if(flags != null && flags.startsWith("--"))
		{
			if(flags.compareToIgnoreCase("--verbose")==0)
				isVerbose = true;
			else if(flags.compareToIgnoreCase("--showrandom")==0)
				showRandom = true;
			else if(flags.compareToIgnoreCase("--help")==0)
				System.out.println("Help stuff");
			else			
				return false;
		}
		else if(flags != null & algorithm==null)
		{
			algorithm = filePath;
			filePath = flags; 	
		}

		if(filePath==null)
			return false;
		else
			fileName = filePath;
		
		if(algorithm == null)
			algo = "all";
		else
			algo = algorithm;
		
		if(algo.equalsIgnoreCase("RoundRobin"))
		{
			quantum = 2;
//			System.out.println("Please enter the value of the quantum (default = 2) :");
//			try {
//				String i =(String) System.in.read();
//				quantum = ;
//				
//			} catch (IOException e) {
//				quantum = 2;
//			}
		}

		return true;

	}

	private static void init() {
		processes = new TreeMap<Integer, Process>();
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

					processes.put(i, p);										
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
