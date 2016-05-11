package data.utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Pair <K,V> implements Map, Comparator {

	LinkedList<K> keys;
	LinkedList<V> values;
	
	public Pair(){
		keys = new LinkedList<K>();
		values = new LinkedList<V>();
	}
	
	@Override
	public void clear() {
		keys.clear();
		values.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		for(K k:keys)
			if(k.equals(key))
				return true;
		
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		for(V v:values)
			if(v.equals(value))
				return true;
		
		return false;
	}

	@Override
	public Set entrySet() {
		return null;
	}

	@Override
	public Object get(Object key) {
		if(keys.contains(key))
			return values.get(keys.indexOf(key));
		return null;
	}

	public Object getK(int index){
		return keys.get(index);
	}
	
	public Object getV(int index){
		return values.get(index);
	}

	@Override
	public boolean isEmpty() {
		return keys.isEmpty();
	}

	@Override
	public Set keySet() {
		return null;
	}

	public void add(Object key, Object value){
		keys.add((K) key);
		values.add((V) value);
	}
	
	@Override
	public Object put(Object key, Object value) {
		
		int i = keys.indexOf((K) key);
		if(i != -1)
			values.set(i, (V) value);
		
		return null;
	}

	@Override
	public void putAll(Map m) {
	}

	@Override
	public Object remove(Object key) {
		K ret = null;
		if(this.containsKey(key)){
			int i = keys.indexOf(key);
			ret = (K) keys.remove(i);
			values.remove(i);
		}
		return ret;
	}

	@Override
	public int size() {
		return keys.size();
	}

	@Override
	public Collection values() {
		return values;
	}

	@Override
	public int compare(Object k, Object v) {
		
		return k.toString().compareTo(this.toString());
		
	}
	
	public void sort(){
		msort(0, this.keys.size()-1);
	}
	
	private void merge (int lower, int mid, int upper){
		LinkedList<K> kcopy = (LinkedList<K>) this.keys.clone();
		LinkedList<V> vcopy = (LinkedList<V>) this.values.clone();
		LinkedList<K> ktemp = new LinkedList<K>();
		LinkedList<V> vtemp = new LinkedList<V>();
		int i,j,k;
		
		for(i=0,j=lower,k=mid+1; j<=mid || k<=upper; i++){
			if(((j <= mid) && (k > upper || this.keys.get(j).toString().compareTo(this.keys.get(k).toString()) <= 0))){
				vtemp.add(vcopy.get(j));
				ktemp.add(kcopy.get(j++));
			}
			else{
				vtemp.add(vcopy.get(k));
				ktemp.add(kcopy.get(k++));
			}
		}
		for(i=0,j=lower;j<=upper; i++, j++){
			this.keys.set(j,ktemp.get(i));
			this.values.set(j, vtemp.get(i));
		}
	}

	private void msort(int lower, int upper){
		int mid;
		if (upper-lower>0) {
			mid=(lower+upper)/2;
			msort(lower, mid);
			msort(mid+1, upper);
			merge(lower, mid, upper);
		}
	}
	
}
