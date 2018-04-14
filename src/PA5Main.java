import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

/*
 * Aaron Garcia PA5Main.java, PA5-HashMapImpl
 * 
 * Description: This program takes flight information and outputs
 * flight data based on the input command given.
 * 
 * Use: This program takes 3 or 4 command line arguments
 * Argument 1 should be a correctly formated csv
 * Argument 2 should be a command: MAX, LIMIT, DEPARTURES, DEBUG
 * Argument 3 should be an integer, and is only used for the LIMIT command
 * 
 * Output will be printed to the console
 * Output varies with each command details at 
 * https://github.com/UACS210Spring2018/PA-and-Section-Writeups/tree/master/PA5-HashMapImpl
 */
public class PA5Main {
    public static void main(String[] args) {
        Scanner inFile = null;
        try {
            inFile = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        inFile.nextLine(); // passes column title line

        // Determines which helper function to use based on passed command
        if (args[1].equals("MAX")) {
            handleMax(inFile);
        } else if (args[1].equals("DEPARTURES")) {
            handleDepartures(inFile);
        } else if (args[1].equals("LIMIT")) {
            handleLimit(inFile, Integer.parseInt(args[2]));
        } else if (args[1].equals("DEBUG")) {
            handleDebug(inFile);
        }
        inFile.close();
    }

    // Helper function for LIMIT command
    private static void handleLimit(Scanner inFile, int limit) {
        MyHashMap<String, Integer> airportCounts = createStringIntMap(inFile);
        ArrayList<String> aboveLimit = new ArrayList<String>();

        // parses dictionary to find airports with more than limit flights
        for (String s : airportCounts.keySet()) {
            if (airportCounts.get(s) > limit) {
                aboveLimit.add(s);
            }
        }

        // Alphabetical sort of list
        Collections.sort(aboveLimit);

        // Prints one airport per line, along with the number of flights the
        // airport has
        for (String s : aboveLimit) {
            System.out.println(s + " - " + airportCounts.get(s));
        }
    }

    // Helper function for the DEPARTURES command
    private static void handleDepartures(Scanner inFile) {
        MyHashMap<String, HashSet<String>> airportDepartures = createStringHashSetMap(inFile);
        ArrayList<String> departingFrom = new ArrayList<String>();
        ArrayList<String> arrivingTo = new ArrayList<String>();

        // Adds all airports that have departures to a list and sorts the list
        departingFrom.addAll(airportDepartures.keySet());
        Collections.sort(departingFrom);

        // Prints all the destination airports for each airport in departingFrom list
        for (String departing : departingFrom) {
            arrivingTo.clear();
            // Adds all destination airports to list and sorts alphabetically
            arrivingTo.addAll(airportDepartures.get(departing));
            Collections.sort(arrivingTo);

            System.out.print(departing + " flys to"); // Prints departing airport

            // Prints destinations for departing airport
            for (String arriving : arrivingTo) {
                System.out.print(" " + arriving);
            }

            System.out.println();
        }
    }

    // Helper function for MAX command
    private static void handleMax(Scanner inFile) {
        MyHashMap<String, Integer> airportCounts = createStringIntMap(inFile);

        ArrayList<String> maxes = findMax(airportCounts);
        Collections.sort(maxes);

        System.out.print("MAX FLIGHTS " + airportCounts.get(maxes.get(0)) + " :");

        for (String s : maxes) {
            System.out.print(" " + s);
        }
    }

    private static void handleDebug(Scanner inFile) {
        MyHashMap<String, Integer> collisions = createStringIntMap(inFile);

        System.out.println(collisions.debug());
    }

    // Finds the airport(s) with the largest amount of flights
    private static ArrayList<String> findMax(MyHashMap<String, Integer> airportCounts) {
        int max = 0;
        ArrayList<String> maxes = new ArrayList<String>();

        for (String s : airportCounts.keySet()) {
            if (airportCounts.get(s) > max) { // If new max found
                maxes.clear();
                maxes.add(s);

                max = airportCounts.get(s);
            } else if (airportCounts.get(s) == max) { // If tie, don't clear
                                                      // list, but add new
                                                      // airport
                maxes.add(s);
            }
        }
        return maxes;
    }

    // Creates a HashMap that maps Strings to integers
    private static MyHashMap<String, Integer> createStringIntMap(Scanner inFile) {
        MyHashMap<String, Integer> airportCounts = new MyHashMap<String, Integer>();
        String[] line;

        while (inFile.hasNextLine()) {
            line = isValidLine(inFile.nextLine().trim());

            if (line == null) {
                continue;
            }

            // If map already contains both airports
            if (airportCounts.containsKey(line[2]) && airportCounts.containsKey(line[4])) {
                airportCounts.put(line[2], airportCounts.get(line[2]) + 1);
                airportCounts.put(line[4], airportCounts.get(line[4]) + 1);
            } else if (airportCounts.containsKey(line[2])) { // If map contains departing airport only
                airportCounts.put(line[2], airportCounts.get(line[2]) + 1);
                airportCounts.put(line[4], new Integer(1));
            } else if (airportCounts.containsKey(line[4])) { // If map contains destination airport only
                airportCounts.put(line[4], airportCounts.get(line[4]) + 1);
                airportCounts.put(line[2], new Integer(1));
            } else { // If map contains neither departing or destination airports
                airportCounts.put(line[2], new Integer(1));
                airportCounts.put(line[4], new Integer(1));
            }
        }
        return airportCounts;
    }

    // Creates a HashMap that maps Strings to a String HashSet
    // HashSets are used to exclude duplicate airports
    private static MyHashMap<String, HashSet<String>> createStringHashSetMap(Scanner inFile) {
        MyHashMap<String, HashSet<String>> airportDepartures = new MyHashMap<String, HashSet<String>>();
        String[] line;

        while (inFile.hasNextLine()) {
            line = isValidLine(inFile.nextLine().trim());

            if (line == null) {
                continue;
            }

            // If departing airport already in map, add destination to its set
            if (airportDepartures.containsKey(line[2])) {
                airportDepartures.get(line[2]).add(line[4]);
            } else {
                HashSet<String> tempSet = new HashSet<String>();
                tempSet.add(line[4]);
                airportDepartures.put(line[2], tempSet);
            }
        }
        return airportDepartures;
    }

    private static String[] isValidLine(String line) {
        if (!line.contains(",")) {
            return null;
        }
        return line.split(",");
    }
}