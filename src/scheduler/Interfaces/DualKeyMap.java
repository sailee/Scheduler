package scheduler.Interfaces;

import java.util.Collection;
import java.util.Set;

import scheduler.types.DualKey;


/**
 * @author Sailee
 *
 * @param <K1> Data type of First Key
 * @param <K2> Data type of Second Key
 * @param <V> Data type of Value
 */
public interface DualKeyMap<K1,K2,V> 
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
	public boolean containsKey(Object key) ;	

	/**
	 * @param value Value that is to be looked up in the DualKeyMap
	 * @return true if the Value looked up is found
	 * 
	 * Checks if value is present in DualKeyMap
	 */
	public boolean containsValue(Object value) ;	

	/**
	 * @return Set of all dual key entries present in the Map 
	 * 
	 * Returns the set of all entries present in the Map
	 */
	public Set<java.util.Map.Entry<DualKey<K1, K2>, V>> entrySet() ;	

	/**
	 * @return the value corresponding to the given key
	 * 
	 * Returns the value of the specified key, if present in the Map, else returns null
	 */	
	public V get(Object key) ;	

	/**
	 * @return true if the DualKeyMap contains no elements
	 * 
	 * Checks if the Map is empty
	 */
	public boolean isEmpty() ;	

	/**
	 * @return the Set of all Dual keys
	 * 
	 * Returns the set of all Dual keys
	 */
	public Set<DualKey<K1, K2>> keySet();

	/**
	 * @return returns the value of the key entered
	 * @param key Specifies the Dual key to be entered
	 * @param Value Specifies the value to be entered
	 * 
	 * Puts the specified Dual Key - value entry to the DualKey Map
	 */
	public V put(K1 firstKey,K2 secondKey, V value) ;	

	/**
	 * @param m Specifies the Dual Key map whose keys are to be copied into current map
	 * 
	 * Puts all values from specified map to given map
	 */
	public void putAll(DualKeyMap<K1, K2, V> m) ;

	/**
	 * @return value of Dual-key entry that was removed
	 * @param key Dual-key that is to be removed  
	 */
	public V remove(Object key) ;		

	/**
	 * @return size of the DualKey Map
	 * 
	 * Returns the size of the DualKey Map
	 */
	public int size() ;		

	/**
	 * @return Collection of values of all entries present in the map
	 * 
	 * Returns the collections of values of all entries present in the map
	 */
	public Collection<V> values() ;	
}
