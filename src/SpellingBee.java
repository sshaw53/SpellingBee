import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
// SPELLING BEE BY: SIERRA SHAW
// MARCH 17, 2O23

public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        tryWords("", letters);
    }

    public void tryWords(String word, String letters) {
        // Base case when letters is empty ""
        words.add(word);

        if (letters.length() == 0)
            return;

        // Traverse through length of letters (number of recursive steps depends on length of letters)
        // Add next letter of string to word, remove from letters
        for (int i = 0; i < letters.length(); i++) {
            String newWord = word;
            char chosenLetter = letters.charAt(i);
            newWord += chosenLetter;
            String newLetters = letters.substring(0, i) + letters.substring(i + 1);
            tryWords(newWord, newLetters);
        }
    }
    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // Call a recursive method
        words = mergeSort(words);
    }

    public ArrayList<String> mergeSort(ArrayList<String> words) {
        // Base Case when there is only one element left
        if (words.size() == 1)
            return words;

        // Split the arraylist in half to recurse -> continues to break up the arraylist
        int mid = words.size() / 2;
        ArrayList<String> firstStr = new ArrayList<String>();
        ArrayList<String> secondStr = new ArrayList<String>();
        for (int i = 0; i < words.size(); i++) {
            if (i < mid)
                firstStr.add(words.get(i));
            else
                secondStr.add(words.get(i));
        }
        ArrayList<String> first = mergeSort(firstStr);
        ArrayList<String> second = mergeSort(secondStr);

        // Combines the arraylists to bring it back into a single arraylist
        return combine(first, second);
    }

    public ArrayList<String> combine(ArrayList<String> str1, ArrayList<String> str2) {
        int size1 = str1.size();
        int size2 = str2.size();
        ArrayList<String> combined = new ArrayList<String>();

        // Traverses through the ArrayLists, but must go through the smaller one to avoid
        // resulting in index out of bounds, adds the smaller one to the ArrayList combined
        int i = 0;
        int j = 0;
        while ((i < size1) && (j < size2)) {
            if (str1.get(i).compareTo(str2.get(j)) <= 0) {
                combined.add(str1.get(i));
                i++;
            }
            else {
                combined.add(str2.get(j));
                j++;
            }
        }
        // Once it traverses through one of the arrays fully, it adds the rest of the
        // other arraylist to the combined arraylist
        while (i < size1) {
            combined.add(str1.get(i));
            i++;
        }
        while (j < size2) {
            combined.add(str2.get(j));
            j++;
        }
        return combined;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        int i = 0;
        while(i < words.size()) {
            // Calls binary search to see if the word is in the dictionary
            // If it is not in the dictionary, it will be removed
            if (!binarySearch(words.get(i), 0, DICTIONARY_SIZE - 1))
                words.remove(words.get(i));
            else
                i++;
        }
    }

    public boolean binarySearch(String word, int low, int high) {
        // Base Case is when there is one element to check and it equals the word or doesn't
        if (high - low == 0 && word.equals(DICTIONARY[low]))
            return true;
        if (high < low)
            return false;

        // Compares the word to the middle element: if it is equal to the word, returns true
        // If it is lower, recurse into the lower half of the Dictionary array.
        // If it is higher, recurse into the higher half of the array.
        int mid = (high + low) / 2;
        if (word.equals(DICTIONARY[mid]))
            return true;

        if (word.compareTo(DICTIONARY[mid]) < 0)
            return binarySearch(word, low, mid - 1);
        else
            return binarySearch(word, mid + 1, high);
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
