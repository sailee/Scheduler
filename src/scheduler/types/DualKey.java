package scheduler.types;

import java.util.Comparator;

/**
 * @author Sailee
 *
 * @param <A> Data type of first Key
 * @param <B> Data type of second Key
 * 
 * This class encapsulates Keys with two values. 
 * It contains methods to get and set values of individual keys, set comparators for individual keys, check for equality of two objects with dual keys, etc 
 */
public class DualKey <A,B> implements Comparable<DualKey<A,B>>
{	
	private A firstKey;
	private B secondKey;
	private Comparator<A> firstComparator;
	private Comparator<B> secondComparator;

	/**
	 * @param key1 Value of first Key
	 * @param key2 Value of second Key
	 * @param first Comparator to compare first keys
	 * @param second Comparator to compare second keys
	 */
	public DualKey(A key1, B key2, Comparator<A> first, Comparator<B>second)
	{
		if(first.equals(null) || second.equals(null))
		{
			System.out.println("Could not instantiate DualKey");
		}
		else
		{
			this.firstKey = key1;
			this.secondKey = key2;
			/*
			 * The implementor should ensure that the values of first and second comparator are non-null
			 */
			this.setFirstComparator(first);
			this.setSecondComparator(second);
		}
	}

	/**
	 * @return Gives the value of First Key
	 */
	public A getArrivalTime()
	{
		return this.firstKey;
	}

	/**
	 * @return Gives the value of second Key
	 */
	public B getProcessID()
	{
		return this.secondKey;
	}

	/**
	 * @param firstComparator Sets the comparator comparing first keys 
	 */
	public void setFirstComparator(Comparator<A> firstComparator) {
		this.firstComparator = firstComparator;
	}

	/**
	 * @return Gives the value of comparator that compares first Keys
	 */
	Comparator<A> getFirstComparator() {
		return firstComparator;
	}

	/**
	 * @param secondComparator Sets the comparator comparing second keys
	 */
	public void setSecondComparator(Comparator<B> secondComparator) {
		this.secondComparator = secondComparator;
	}

	/**
	 * @return the secondComparator Gives the value of comparator that compares second keys
	 */
	Comparator<B> getSecondComparator() {
		return secondComparator;
	}


	public boolean equals(Object o)
	{
		try
		{		
			if (o == null) 
				return false;
			if (this.getClass() != o.getClass()) 
				return false;
			else 
			{
				DualKey<A, B> entry= (DualKey<A, B>) o;
				return (firstKey.equals(entry.firstKey) && secondKey.equals(entry.secondKey));
			}		    			
		}
		catch (Exception ex)
		{
			System.err.println("Error: Object Equality Check in DualKey Class");
		}

		return false;
	}


	@Override
	public int compareTo(DualKey<A, B> arg0) {
		//check for equality of two objects
		if (this.equals(arg0)) 
			return 0;

		//If first keys are equal, compare second keys using the comparator specified for the second key
		if (this.getArrivalTime() == arg0.getArrivalTime())
		{
			return getSecondComparator().compare(this.getProcessID(), arg0.getProcessID());
		}

		//If first keys are not equal, compare the first keys using the comparator specified for the first key
		return getFirstComparator().compare(this.getArrivalTime(), arg0.getArrivalTime());		
	}

	@Override
	public String toString()
	{
		return "Key1 = " + this.getArrivalTime().toString() + " , Key2 = " + getProcessID().toString();
	}
}
