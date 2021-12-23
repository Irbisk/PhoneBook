package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File directory = new File("C:\\Users\\irbis\\Desktop\\Phone BOOK\\directory.txt");
        File find = new File("C:\\Users\\irbis\\Desktop\\Phone BOOK\\find.txt");
        ArrayList<String> phoneBook = new ArrayList<>();
        ArrayList<String> searchItems = new ArrayList<>();
        ArrayList<String> phoneBookForHash = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(directory);
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                phoneBook.add(s);
                phoneBookForHash.add(s);
            }
            scanner = new Scanner(find);
            while (scanner.hasNextLine()) {
                searchItems.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        start(phoneBook, searchItems, phoneBookForHash);

    }

    public static void start(ArrayList<String> phoneBook, ArrayList<String> searchItems, ArrayList<String> phoneBookForHash) {

        System.out.println("Start searching (linear search)...");
        long startTime = System.currentTimeMillis();
        int searchNumber = linearSearch(phoneBook, searchItems);
        long endTime = System.currentTimeMillis();
        int totalMs = (int) (endTime - startTime);
        System.out.print("Found " + searchNumber + " / " + searchItems.size() + " entries. ");
        System.out.println("Time taken: " + countTime(totalMs));



        System.out.println("\nStart searching (bubble sort + jump search)...");
        int sortingTimeMs = 0;
        int searchingTimeMs = 0;
        startTime = System.currentTimeMillis();
        boolean bubbleSortSuccessful = bubbleSort(phoneBook, totalMs);
        endTime = System.currentTimeMillis();
        sortingTimeMs = (int) (endTime - startTime);
        if (bubbleSortSuccessful) {
            startTime = System.currentTimeMillis();
            System.out.print("Found " + jumpSearch(phoneBook, searchItems) + " / " + searchItems.size() + " entries.");
            endTime = System.currentTimeMillis();
            searchingTimeMs = (int) (endTime - startTime);
            int totalTimeMs = (int) (sortingTimeMs + searchingTimeMs);
            System.out.println("Time taken: " + countTime(totalTimeMs));
            System.out.println("Sorting time: " + countTime(sortingTimeMs));
            System.out.println("Searching time: " + countTime(searchingTimeMs));
        } else {
            startTime = System.currentTimeMillis();
            searchNumber = linearSearch(phoneBook, searchItems);
            endTime = System.currentTimeMillis();
            searchingTimeMs = (int) (endTime - startTime);
            int totalTimeMs = (int) (sortingTimeMs + searchingTimeMs);
            System.out.print("Found " + searchNumber + " / " + searchItems.size() + " entries.");
            System.out.println("Time taken: " + countTime(totalTimeMs));
            System.out.println("Sorting time: " + countTime(sortingTimeMs) + "- STOPPED, moved to linear search");
            System.out.println("Searching time: " + countTime(searchingTimeMs));
        }


        System.out.println("\nStart searching (quick sort + binary search)...");
        startTime = System.currentTimeMillis();
        int low = 0;
        int high = phoneBook.size() - 1;
        quickSortRecursionArray(phoneBook, low, high);
        endTime = System.currentTimeMillis();
        sortingTimeMs = (int) (endTime - startTime);
        startTime = System.currentTimeMillis();
        searchNumber =  binarySearch(phoneBook, searchItems);
        endTime = System.currentTimeMillis();
        searchingTimeMs = (int) (endTime - startTime);
        int totalTimeMs = searchingTimeMs + sortingTimeMs;
        System.out.print("Found " + searchNumber + " / " + searchItems.size() + " entries.");
        System.out.println("Time taken: " + countTime(totalTimeMs));
        System.out.println("Sorting time: " + countTime(sortingTimeMs));
        System.out.println("Searching time: " + countTime(searchingTimeMs));

        System.out.println("\nStart searching (hash table)...");
        startTime = System.currentTimeMillis();
        Hashtable<String, String> hashtable = hashTableCreation(phoneBookForHash);
        endTime = System.currentTimeMillis();
        sortingTimeMs = (int) (endTime - startTime);
        startTime = System.currentTimeMillis();
        searchNumber = hashTableSearch(searchItems, hashtable);
        endTime = System.currentTimeMillis();
        searchingTimeMs = (int) (endTime - startTime);
        totalTimeMs = searchingTimeMs + sortingTimeMs;
        System.out.print("Found " + searchNumber + " / " + searchItems.size() + " entries.");
        System.out.println("Time taken: " + countTime(totalTimeMs));
        System.out.println("Creating time: " + countTime(sortingTimeMs));
        System.out.println("Searching time: " + countTime(searchingTimeMs));


    }

    public static Hashtable<String, String> hashTableCreation(ArrayList<String> phoneBookForHash) {
        Hashtable<String, String> hashtable = new Hashtable<>();
        for (int i = 0; i < phoneBookForHash.size(); i++) {
            int spaceIndex = phoneBookForHash.get(i).indexOf(" ") + 1;
            String key = phoneBookForHash.get(i).substring(spaceIndex);
            String value = phoneBookForHash.get(i).substring(0, spaceIndex);
            hashtable.put(key, value);
        }
        return hashtable;
    }

    public static int hashTableSearch (ArrayList<String> searchedItems, Hashtable<String, String> hashTable) {
        int count = 0;
        for (int i = 0; i < searchedItems.size(); i++) {
            if (hashTable.containsKey(searchedItems.get(i))) {
                count++;
            }
        }
        return count;
    }

    public static int binarySearch(ArrayList<String> phoneBook, ArrayList<String> valuesToFind) {
        for (int i = 0; i < phoneBook.size(); i++) {
            int spaceIndex = phoneBook.get(i).indexOf(" ") + 1;
            String name = phoneBook.get(i).substring(spaceIndex);
            phoneBook.set(i, name);
        }
        int countFoundElements = 0;
        for (int i = 0; i < valuesToFind.size(); i++) {
            int left = 0;
            int right = phoneBook.size() - 1;
            while (left <= right) {
                int middle = (left + right) / 2;
                if (phoneBook.get(middle).contains(valuesToFind.get(i))) {
                    countFoundElements++;
                    break;
                } else if (phoneBook.get(middle).compareTo(valuesToFind.get(i)) > 0) {
                    right = middle - 1;
                } else {
                    left = middle + 1;
                }
            }
        }
        return countFoundElements;
    }

    public static void quickSortRecursionArray(ArrayList<String> phoneBook, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(phoneBook, low, high);

            quickSortRecursionArray(phoneBook, low, pivotIndex - 1);
            quickSortRecursionArray(phoneBook, pivotIndex + 1, high);
        }
    }

    public static int partition(ArrayList<String> phoneBook, int low, int high) {
        String pivotLine = phoneBook.get(high);
        String pivot = pivotLine.substring(pivotLine.indexOf(" "));

        int i = low - 1;

        for (int j = low; j < high; j++) {
            String nameLine = phoneBook.get(j);
            String name = nameLine.substring(nameLine.indexOf(" "));

            if (name.compareTo(pivot) < 1) {
                i++;

                String swapTemp = phoneBook.get(i);
                phoneBook.set(i, phoneBook.get(j));
                phoneBook.set(j, swapTemp);
            }
        }

        String swapTemp = phoneBook.get(i + 1);
        phoneBook.set(i + 1, phoneBook.get(high));
        phoneBook.set(high, swapTemp);

        return i + 1;
    }



    public static String getPhoneBookName (ArrayList<String> phoneBook, int index) {
        String phoneBookLine = phoneBook.get(index);
        String phoneBookName = "";
        if (phoneBookLine.indexOf(" ") != phoneBookLine.lastIndexOf(" ")) {
            phoneBookName = phoneBookLine.substring(phoneBookLine.indexOf(" ") + 1, phoneBookLine.lastIndexOf(" "));
        } else {
            phoneBookName = phoneBookLine.substring(phoneBookLine.indexOf(" ") + 1);
        }
        return phoneBookName;
    }

    public static int jumpSearch(ArrayList<String> phoneBook, ArrayList<String> searchItems) {
        int n = (int) Math.floor(Math.sqrt(phoneBook.size()));

        int searchNumber = 0;

        for (int i = 0; i < searchItems.size(); i++) {
            String searchName = searchItems.get(i).substring(0, searchItems.get(i).indexOf(" "));
            boolean found = false;
            for (int j = 0; !found || j > phoneBook.size(); j += n) {
                String phoneBookName = getPhoneBookName(phoneBook, j);
                if (searchName.compareTo(phoneBookName) == 0) {
                    found = true;
                    searchNumber++;
                } else if (searchName.compareTo(phoneBookName) < 0) {
                    for (int k = j; k > j - n; k--) {
                        phoneBookName = getPhoneBookName(phoneBook, k);
                        if (searchName.compareTo(phoneBookName) == 0) {
                            found = true;
                            searchNumber++;
                            break;
                        }
                    }
                }

                if (j + n > phoneBook.size()) {
                    n = phoneBook.size() - j;
                }

            }
        }
        return searchNumber;
    }

    public static int linearSearch (ArrayList<String> phoneBook, ArrayList<String> searchItems) {
        int searchNumber = 0;
        for (int i = 0; i < searchItems.size(); i++) {
            for (int j = 0; j < phoneBook.size(); j++) {
                if (phoneBook.get(j).contains(searchItems.get(i))) {
                    searchNumber++;
                    break;
                }
            }
        }
        return searchNumber;
    }

    public static String countTime(int totalMs) {
        int min = totalMs / 60000;
        int sec = totalMs % 60000 / 1000;
        int ms = totalMs % 1000;
        return String.format("%d min. %d sec. %d ms.", min, sec, ms);
    }

    public static boolean bubbleSort(ArrayList<String> phoneBook, int totalTime) {
        boolean sorting = true;
        boolean completed = true;
        long startTime = System.currentTimeMillis();
        while (sorting) {
            sorting = false;
            for (int i = 1; i < phoneBook.size(); i++) {
                String a = phoneBook.get(i - 1);
                String b = phoneBook.get(i);
                String aName = "";
                String bName = "";
                if (a.indexOf(" ") != a.lastIndexOf(" ")) {
                    aName = a.substring(a.indexOf(" ") + 1, a.lastIndexOf(" "));
                } else {
                    aName = a.substring(a.indexOf(" ") + 1);
                }
                if (b.indexOf(" ") != b.lastIndexOf(" ")) {
                    bName = b.substring(b.indexOf(" ") + 1, b.lastIndexOf(" "));
                } else {
                    bName = b.substring(b.indexOf(" ") + 1);
                }

                if (!checkSortedName(aName, bName)) {
                    phoneBook.add(i - 1, b);
                    phoneBook.remove(i + 1);
                    sorting = true;
                }
                long endTime = System.currentTimeMillis();
                int totalMs = (int) (endTime - startTime);
                if (totalMs > 10 * totalTime) {
                    completed = false;
                    sorting = false;
                    break;
                }
            }
        }
        return completed;
    }
    public static boolean checkSortedName(String a, String b) {
        return a.compareTo(b) < 0;
    }

}
