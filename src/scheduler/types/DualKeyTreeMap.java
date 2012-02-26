package scheduler.types;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import scheduler.Interfaces.DualKeyMap;
import scheduler.Interfaces.SortedDualKeyMap;


/**
 * @author Sailee
 *
 * @param <K1> Data type of First Key
 * @param <K2> Data type of Second Key
 * @param <V> Data type of Value
 */
public class DualKeyTreeMap<K1,K2,V> implements SortedDualKeyMap<K1, K2, V> {

	SortedMap<DualKey<K1,K2>,V> treeMap;
	Comparator<K1> firstComparator;
	Comparator<K2> secondComparator;

	/**
	 * @param first Comparator comparing first value in the Dual Key
	 * @param second Comparator comparing second value in the Dual Key
	 * 
	 * Constructor for instantiating objects of DualKeyTreeMap type
	 */
	public DualKeyTreeMap(Comparator<K1> first, Comparator<K2> second)
	{
		this.treeMap = new TreeMap<DualKey<K1,K2>, V>();
		this.firstComparator = first;
		this.secondComparator = second;
	}

	/**
	 * @return comparator comparing the values of first key, in the key pair
	 * 
	 * Gets the comparator comparing values of first key in the key pair
	 */
	public Comparator<K1> getFirstComparator() {
		return firstComparator;
	}

	/**
	 * @param first comparator comparing the values of first key in the key pair
	 * 
	 * Sets the comparator comparing the values of first key in the key pair
	 */
	public void setFirstComparator(Comparator<K1> first) {
		this.firstComparator = first;
	}

	/**
	 * @return comparator comparing the values of second key in the key pair
	 * 
	 * Returns comparator comparing the values of second key in the key pair
	 */
	public Comparator<K2> getSecondComparator() {
		return secondComparator;
	}

	/**
	 * @param second comparator comparing the values of second key in the key pair
	 * 
	 * Sets the  comparator comparing the values of second key in the key pair
	 */
	public void setSecondComparator(Comparator<K2> second) {
		this.secondComparator = second;
	}

	@Override
	public V put(K1 firstKey, K2 secondKey, V value) {
		try 
		{
			DualKey<K1,K2> key = new DualKey<K1, K2>(firstKey, secondKey, this.firstComparator, this.secondComparator);
			return this.treeMap.put(key, value);
		} 
		catch(Exception ex)
		{
			System.out.println("Error in putting specified key in the DualKeyTreeMap");
		}
		return null;
	}

	@Override
	public V get(Object key) {
		try
		{
			return this.treeMap.get(key);
		}
		catch(Exception ex)
		{
			System.out.println("Error in getting specified key in the DualKeyTreeMap");
		}
		return null;
	}

	@Override
	public V remove(Object key) {
		try
		{
			return this.treeMap.remove(key);
		}
		catch(Exception ex)
		{
			System.out.println("Error in removing specified value from the DualKeyTreeMap");
		}
		return null;
	}

	@Override
	public boolean containsKey(Object key) {
		try
		{
			return this.treeMap.containsKey(key);
		}
		catch(Exception ex)
		{
			System.out.println("Error in checking for the specified key in the DualKeyTreeMap");
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		try
		{
			return this.treeMap.containsValue(value);
		}
		catch(Exception ex)
		{
			System.out.println("Error in checking for the specified value in the DualKeyTreeMap");
		}
		return false;
	}

	@Override
	public int size() {
		return this.treeMap.size();
	}

	@Override
	public boolean isEmpty() {
		return this.treeMap.isEmpty();
	}

	@Override
	public void putAll(Map<? extends DualKey<K1, K2>, ? extends V> m) {
		try
		{
			this.treeMap.putAll(m);
		}
		catch(Exception ex)
		{
			System.out.println("Error in putting all values in the DualKeyTreeMap");
		}
	}

	@Override
	public void clear() {
		this.treeMap.clear();		
	}

	@Override
	public Set<DualKey<K1, K2>> keySet() {
		return this.treeMap.keySet();
	}

	@Override
	public Collection<V> values() {
		return this.treeMap.values();
	}

	@Override
	public Set<java.util.Map.Entry<DualKey<K1, K2>, V>> entrySet() {
		return this.treeMap.entrySet();
	}

	@Override
	public Comparator<? super DualKey<K1, K2>> comparator() {
		// TODO Auto-generated method stub
		return this.treeMap.comparator();
	}

	@Override
	public DualKey<K1, K2> firstKey() {
		return this.treeMap.firstKey();
	}

	@Override
	public SortedMap<DualKey<K1, K2>, V> headMap(DualKey<K1, K2> toKey) {
		return this.treeMap.headMap(toKey);
	}

	@Override
	public DualKey<K1, K2> lastKey() {
		return this.treeMap.lastKey();
	}

	@Override
	public SortedMap<DualKey<K1, K2>, V> subMap(DualKey<K1, K2> fromKey,
			DualKey<K1, K2> toKey) {
		return this.treeMap.subMap(fromKey, toKey);
	}

	@Override
	public SortedMap<DualKey<K1, K2>, V> tailMap(DualKey<K1, K2> fromKey) {
		return this.treeMap.tailMap(fromKey);
	}

	@Override
	public String toString()
	{
		Set<Map.Entry<DualKey<K1, K2>, V>> allValues=  this.entrySet();
		Iterator<Entry<DualKey<K1, K2>, V>> itr = allValues.iterator();
		String output = "";

		while(itr.hasNext())
		{
			Map.Entry<DualKey<K1,K2>, V> entry = (Map.Entry<DualKey<K1,K2>, V>) itr.next();
			output = output + entry.getKey().toString() + ", Value = " + entry.getValue().toString()+ "\n";
		}

		return output;
	}

	@Override
	public void putAll(DualKeyMap<K1, K2, V> m) {
		this.treeMap.putAll((Map<? extends DualKey<K1, K2>, ? extends V>) m);

	}

	@Override
	public V put(DualKey<K1, K2> key, V value) {
		return this.treeMap.put(key, value);
	}

	public boolean containsKey(K1 firstKey,K2 secondKey) {
		try
		{
			DualKey<K1,K2> key = new DualKey<K1, K2>(firstKey, secondKey, this.firstComparator, this.secondComparator);
			return this.treeMap.containsKey(key);
		}
		catch(Exception ex)
		{
			System.out.println("Error in checking for the specified key in the DualKeyTreeMap");
		}
		return false;
	}

	/**
	 * @param firstKey first value in the Dual Key
	 * @param secondKey second value in the Dual Key
	 * @return value corresponding to the combined key formed by firstKey and secondKey
	 * 
	 * Removes the entry corresponding to the combined key formed by firstKey and secondKey
	 */
	public V remove(K1 firstKey,K2 secondKey) {
		try
		{
			DualKey<K1, K2> key = new DualKey<K1, K2>(firstKey, secondKey, this.firstComparator, this.secondComparator);
			return this.treeMap.remove(key);
		}
		catch(Exception ex)
		{
			System.out.println("Error in removing specified value from the DualKeyTreeMap");
		}
		return null;
	}

	/**
	 * @param firstKey first value in the Dual Key
	 * @param secondKey second value in the Dual Key
	 * @return value corresponding to the combined key formed by firstKey and secondKey
	 * 
	 * Gets the value corresponding to the combined key formed by firstKey and secondKey
	 */
	public V get(K1 firstKey,K2 secondKey) {
		try
		{
			DualKey<K1, K2> key = new DualKey<K1, K2>(firstKey, secondKey, this.firstComparator, this.secondComparator);
			return this.treeMap.get(key);
		}
		catch(Exception ex)
		{
			System.out.println("Error in getting specified key in the DualKeyTreeMap");
		}
		return null;
	}

	/**
	 * @param firstKey first value in the Dual Key
	 * @param secondKey second value in the Dual Key
	 * @return all entries in the map prior to the key formed by combining firstKey and secondKey
	 * 
	 * Returns a SortedMap containing all elements in the map prior to the key specified by firstKey and secondKey
	 */
	public SortedMap<DualKey<K1, K2>, V> headMap(K1 firstKey, K2 secondKey) {
		DualKey<K1, K2> toKey = new DualKey<K1, K2>(firstKey, secondKey, this.firstComparator, this.secondComparator);
		return this.treeMap.headMap(toKey);
	}

	/**
	 * @param fromKey1 first key in Key1
	 * @param fromKey2 second key in Key1
	 * @param toKey1 first key in Key2
	 * @param toKey2 second key in Key2
	 * @return all entries in the map between keys formed by combining fromKey1, fromKey2 and toKey1, toKey2 respectively
	 */
	public SortedMap<DualKey<K1, K2>, V> subMap(K1 fromKey1,K2 fromKey2, K1 toKey1, K2 toKey2) {
		DualKey<K1, K2> fromKey = new DualKey<K1, K2>(fromKey1, fromKey2, this.firstComparator, this.secondComparator);
		DualKey<K1, K2> toKey = new DualKey<K1, K2>(toKey1, toKey2, this.firstComparator, this.secondComparator);
		return this.treeMap.subMap(fromKey, toKey);
	}

	/**
	 * @param firstKey first value in the Dual Key
	 * @param secondKey second value in the Dual Key
	 * @return all entries in the map lying after the entry specified by the key formed by combining firstKey and secondKey
	 */
	public SortedMap<DualKey<K1, K2>, V> tailMap(K1 firstKey, K2 secondKey) {
		DualKey<K1, K2> fromKey = new DualKey<K1, K2>(firstKey, secondKey, this.firstComparator, this.secondComparator);
		return this.treeMap.tailMap(fromKey);
	}
}
