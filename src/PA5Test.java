import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

/*
 * Aaron Garcia PA5Test.java, PA5HashMapImpl
 * 
 * Description: This file tests the basic functions of the MyHashMap.java class
 * 
 * Use: To use run the class as a JUnit test. All tests should pass.
 */

public class PA5Test {

    @Test
    public void testGet() {
        final MyHashMap<String, Integer> testMap = new MyHashMap<String, Integer>();

        testMap.put("test1", new Integer(1));
        testMap.put("test2", new Integer(2));

        assertTrue(1 == testMap.get("test1"));
        assertTrue(2 == testMap.get("test2"));
    }

    @Test
    public void testPut() {
        final MyHashMap<String, Integer> testMap = new MyHashMap<String, Integer>();

        testMap.put("test1", new Integer(1));
        testMap.put("test2", new Integer(2));
        testMap.put("test1", new Integer(3)); // Test duplicate keys

        assertTrue(3 == testMap.get("test1"));
        assertTrue(2 == testMap.get("test2"));
    }

    @Test
    public void testContainsKey() {
        final MyHashMap<String, Integer> testMap = new MyHashMap<String, Integer>();

        testMap.put("test1", new Integer(1));

        assertTrue(testMap.containsKey("test1"));
        assertFalse(testMap.containsKey("test2")); // Test nonexistent key
    }

    @Test
    public void testKeySet() {
        final MyHashMap<String, Integer> testMap = new MyHashMap<String, Integer>();

        final Set<String> testSet = new TreeSet<String>();
        testSet.add("test1");
        testSet.add("test2");

        testMap.put("test1", new Integer(1));
        testMap.put("test2", new Integer(2));

        assertTrue(testSet.equals(testMap.keySet()));
    }
}
