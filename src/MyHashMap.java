import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

/*
 * Aaron Garcia MyHashMap.java, PA5-HashMapImpl
 * 
 * Description: This file is a basic implementation of a hashmap that can store a String key and any Object value.
 *              The hashmap contains the basic fuctions such as put, get, containsKey, and keySet.
 *              The hashmap only hash ten buckets.
 * 
 * Use: To use this hashmap simply call its constructor, and use the provided methods.
 * 
 */

class MyHashMap<K, V> {
    // HashMap is an array of linkedlists. The linkedlists contain Node objects
    // that store key value pairs.
    private LinkedList<Node<K, V>>[] buckets = new LinkedList[10];

    public MyHashMap() {
        // Populate array
        for (int i = 0; i < 10; ++i) {
            buckets[i] = new LinkedList<Node<K, V>>();
        }
    };

    private int hash(K key) {
        int index = key.hashCode() % 10;
        return Math.abs(index);
    }

    public void put(K key, V value) {
        int index = hash(key);

        // If key is in map, change value
        for (Node<K, V> keyValue : buckets[index]) {
            if (keyValue.key.equals(key)) {
                keyValue.value = value;

                return;
            }
        }

        // If key is not in map, add new Node
        buckets[index].add(0, new Node<K, V>(key, value));

        return;
    }

    // Checks map to see if key exists
    public boolean containsKey(K key) {
        int index = hash(key);

        for (Node<K, V> keyValue : buckets[index]) {
            if (keyValue.key.equals(key)) {
                return true;
            }
        }

        return false;
    }

    // Returns the value associated with a key
    public V get(K key) {
        int index = hash(key);

        for (Node<K, V> keyValue : buckets[index]) {
            if (keyValue.key.equals(key)) {
                return keyValue.value;
            }
        }

        return null;
    }

    // Returns a set of all keys contained in the hashmap
    public Set<K> keySet() {
        Set<K> keySet = new TreeSet<K>();

        for (LinkedList<Node<K, V>> bucket : buckets) {
            for (Node<K, V> keyValue : bucket) {
                keySet.add(keyValue.key);
            }
        }

        return keySet;
    }

    // Creates a the debug string, that outputs the contents of each bucket in
    // the map, the number of collisions for each bucket, and the total number
    // of collisions.
    public String debug() {
        String debugString = "";
        int[] totalConflicts = { 0 }; // Use int array to 'pass' the int by
                                      // reference
        for (int i = 0; i < 10; ++i) {
            debugString += "Index " + i + ": " + findConflicts(buckets[i], totalConflicts)
                    + buckets[i].toString().replace(",", "")
                    + "\n";
        }
        debugString += "Total # of conflicts: " + totalConflicts[0];

        return debugString;
    }

    // Counts the number of collisions are returns a formatted string.
    private String findConflicts(LinkedList<Node<K, V>> bucket,
            int[] totalConflicts) {
        if (bucket.size() < 2) {
            return "(0 conflicts), ";
        } else {
            totalConflicts[0] += bucket.size() - 1;
            return "(" + (bucket.size() - 1) + " conflicts), ";
        }
    }
}

// Node class that stores key value pairs
class Node<K, V> {
    protected K key;
    protected V value;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }

    // This method is called during java's linkedList's toString() call. This
    // method allows the key string to be printed, instead of the Node's object
    // address.
    public String toString() {
        return (String) this.key;
    }
}
