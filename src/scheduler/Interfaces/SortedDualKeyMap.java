package scheduler.Interfaces;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import scheduler.types.DualKey;


/**
 * @author Sailee
 *
 * @param <K1> Data type of First Key
 * @param <K2> Data type of Second Key
 * @param <V> Data type of Value
 */
public interface SortedDualKeyMap<K1,K2,V> extends DualKeyMap<K1,K2,V>
{	
	/**
	 * Clears the DualKeyMap
	 */
	public void clear() ;

	/**
	 * @param key Value of the key that is to be found in the DualKeyMap
	 * @return Returns true if the key looked up is found in the DualKeyMap
	 * 
	 * Checks if given key is present in the DualKeyMap
	 */
	public boolean containsKey(Object key);

	/**
	 * @param value Value that is to be looked up in the DualKeyMap
	 * @return true if the Value looked up is found
	 * 
	 * Checks if value is present in DualKeyMap
	 */
	public boolean containsValue(Object value);

	/**
	 * @return the value corresponding to the given key
	 * 
	 * Returns the value of the specified key, if present in the Map, else returns null
	 */
	public V get(Object key);

	/**
	 * @return true if the DualKeyMap contains no elements
	 * 
	 * Checks if the Map is empty
	 */
	public boolean isEmpty();

	/**
	 * 
	 */
	public V put(DualKey<K1, K2> key, V value);

	/**
	 * 
	 */
	public void putAll(Map<? extends DualKey<K1, K2>, ? extends V> m);

	/**
	 * 
	 */
	public V remove(Object key);

	/**
	 * 
	 */
	public int size();

	/**
	 * 
	 */
	public Comparator<? super DualKey<K1, K2>> comparator();

	/**
	 * @return Set of all dual key entries present in the Map 
	 * 
	 * Returns the set of all entries present in the Map
	 */
	public Set<java.util.Map.Entry<DualKey<K1, K2>, V>> entrySet();

	/**
	 * @return the first key in the Sorted Dual Key Map
	 * 
	 * Returns the first key of the Sorted Dual Key Map
	 */
	public DualKey<K1, K2> firstKey();

	/**
	 * @param toKey Dual Key till which the Map is to be returned
	 * @return sorted map of all entries till the entry specified in toKey
	 * Returns the first few entries (till the specified entry)
	 * 
	 */
	public SortedMap<DualKey<K1, K2>, V> headMap(DualKey<K1, K2> toKey);

	/**
	 * @return Set of all keys present in the Sorted Dual Key Map
	 * 
	 * Returns set of all keys present in the Map
	 */
	public Set<DualKey<K1, K2>> keySet();

	/**
	 * @return the last key in the Sorted Map
	 * 
	 * Returns the last key present in the Sorted Map
	 */
	public DualKey<K1, K2> lastKey();

	/**
	 * @return the entries within the specified toKey and fromKey
	 * @param fromKey the dual key entry from which the entries in the Map are to be fetched
	 * @param toKey the dual key entry till which the entries in the map are to be fetched
	 * 
	 *  Returns the values in the Map within the specified keys
	 */
	public SortedMap<DualKey<K1, K2>, V> subMap(DualKey<K1, K2> fromKey,DualKey<K1, K2> toKey);	

	/**
	 * @param fromKey the entry from which the entries in the Map are to be fetched
	 * 
	 * Returns the entries in the Map after the specified key 
	 */
	public SortedMap<DualKey<K1, K2>, V> tailMap(DualKey<K1, K2> fromKey);	

	/**
	 * @return the collection of values of all entries within the Map
	 * 
	 * Returns the collection of values of all entries within the Map
	 */
	public Collection<V> values();

}

