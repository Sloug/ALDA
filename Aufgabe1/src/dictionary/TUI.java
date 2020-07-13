package dictionary;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class TUI {
    Dictionary<String, String> dictionary = new SortedArrayDictionary();

    private void processCommand(String command) {
        List<String> commandSplit = Arrays.asList(command.split(" "));
        switch (commandSplit.get(0)) {
            case "create":
                create(commandSplit.get(1).charAt(0));
                break;
            case "read":
                read(commandSplit.subList(1, commandSplit.size()));
                break;
            case "p":
                print();
                break;
            case "s":
                search(commandSplit.get(1));
                break;
            case "i":
                insert(commandSplit.get(1), commandSplit.get(2));
                break;
            case "r":
                remove(commandSplit.get(1));
                break;
            case "exit":
                System.exit(0);
        }
    }

    private void remove(String germanWord) {
        dictionary.remove(germanWord);
    }

    private void insert(String germanWord, String englishWord) {
        dictionary.insert(germanWord, englishWord);
    }

    private void search(String germanWord) {
        System.out.println(dictionary.search(germanWord));
    }

    private void print() {
        for (Iterator i = dictionary.iterator(); i.hasNext();) {
            Dictionary.Entry<String, String> entry = (Dictionary.Entry) i.next();
            System.out.println("Deutsch: " + entry.getKey() + " Englisch: " + entry.getValue());
        }
        System.out.println("The dictionary's size is: " + dictionary.size());
    }

    void read(List<String> param) throws IllegalArgumentException {
        Integer n = null;
        String path;
        try {
            n = Integer.parseInt(param.get(0));
            path = param.get(1);
        } catch (Exception e) {
            System.out.println("No number of lines was set!");
            path = param.get(0);
        }
        Scanner in;
        try {
            in = new Scanner(new File(path));
        } catch (Exception e) {
            throw new IllegalArgumentException("This is not a valid path/filename!");
        }
        int i = 0;
        while (in.hasNextLine() && (n == null || i < n)) {
            String line = in.nextLine();
            String[] elements = line.split(" ");
            dictionary.insert(elements[0], elements[1]);
            i++;
        }
    }

    private void printHelpText() {
        System.out.println("Usage of this textual user interface:\n" +
                "Commands:\n" +
                "create [imp] impl stands for one of the dictionary implementations, you can choose between:\n" +
                "   \'s\' for SortedArrayDictionary\n" +
                "   \'h\' for HashDictionary and\n" +
                "   \'b\' for BinarySearchTreeDictionary\n" +
                "read [n] [Filename] n is the number of lines, that would be read, if n is not set, all lines would be read. Filename is the path to the file you want to read\n" +
                "p prints the dictionary\n" +
                "s [germanWord] searches for the english word inside the dictionary\n" +
                "i [germanWord] [englishWord] inserts the word pair into the dictionary\n" +
                "r [germanWord] removes the corresponding word pair from the dictionary\n" +
                "exit closes this program");
    }

    void create(char kind) {
        switch (kind) {
            case 's':
                dictionary = new SortedArrayDictionary();
                break;
            case 'h':
                dictionary = new HashDictionary(7);
                break;
            case 'b':
                dictionary = new BinaryTreeDictionary();
                break;
            default:
                throw new IllegalArgumentException("This is not a valid dicitonary type!");
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        TUI tui = new TUI();
        tui.printHelpText();
        while (true) {
            String command = in.nextLine();
            tui.processCommand(command);
        }

    }
}
