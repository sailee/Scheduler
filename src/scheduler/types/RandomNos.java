package scheduler.types;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class RandomNos {

	private Scanner scan;
	private boolean showRandom;

	public RandomNos(boolean showRandomFlag)
	{
		FileReader fread;

		try 
		{
			fread = new FileReader("ResourceFiles\\RandomNumbers.txt");
			scan = new Scanner(fread);
			showRandom = showRandomFlag;
		}
		catch (FileNotFoundException ex)
		{
			System.out.println("Could not find resource file for generating random numbers");
		}
	}

	public int randomOS(int number, String mode)
	{
		int nextRandom = scan.nextInt();
		if(showRandom)
		{
			if(mode.compareTo("CPU")==0)
			{
				System.out.println("Find burst when choosing ready process to run " + nextRandom);
			}
			else
			{
				System.out.println("Find I/O burst when blocking a process "+ nextRandom);
			}
		}
		return 1+(nextRandom % number);

	}
}
