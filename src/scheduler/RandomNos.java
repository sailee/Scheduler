package scheduler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class RandomNos {
	
	private Scanner scan;
	
	public RandomNos()
	{
		FileReader fread;
		
		try 
		{
			fread = new FileReader("ResourceFiles\\RandomNumbers.txt");
			scan = new Scanner(fread);
		}
		catch (FileNotFoundException ex)
		{
			System.out.println("Could not find resource file for generating random numbers");
		}
	}
	
	public int randomOS(int number)
	{
		int nextRandom = scan.nextInt();
		//System.out.println(nextRandom);
		return 1+(nextRandom%number);
		//return nextRandom;
	}
}
