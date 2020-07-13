package dictionary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class PerformanceTest {
    private static double timeFromNanosToMillis(long nanoTime) {
        return (double) nanoTime / 1000000.0;
    }
    private static long search(TUI tui, List<String> searchList) {
        long t0 = System.nanoTime();
        for (String word : searchList) {
            tui.dictionary.search(word);
        }
        long t1 = System.nanoTime();
        return t1- t0;
    }

    public static void main(String[] args) {
        List<String> successfulSearch8000;
        List<String> successfulSearch15896 = new ArrayList<>();
        List<String> notSuccessfulSearch8000;
        List<String> notSuccessfulSearch15896 = new ArrayList<>();
        String path = "Aufgabe1/dtengl.txt";



        try {
            for (Scanner in = new Scanner(new File(path)); in.hasNextLine();) {
                String line = in.nextLine();
                String[] elements = line.split(" ");
                successfulSearch15896.add(elements[0]);
                notSuccessfulSearch15896.add(elements[1]);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("This is not a valid path/filename!");
        }

        successfulSearch8000 = successfulSearch15896.subList(0, 8001);
        notSuccessfulSearch8000 = notSuccessfulSearch15896.subList(0,8001);

        long t0;
        long t1;
        TUI tui = new TUI();
        List<String> param = new ArrayList<>();
        param.add("8000");
        param.add(path);
        t0 = System.nanoTime();
        tui.read(param);
        t1 = System.nanoTime();
        System.out.println("Zeit zum aufbauen eines SortedArrayDictionary mit 8000 Eintraegen: " + timeFromNanosToMillis(t1 - t0) + "ms");

        System.out.println("Zeit fuer die Erfolgreiche suche im SortedArrayDictionary mit 8000 Eintraegen: " + timeFromNanosToMillis(search(tui, successfulSearch8000)) + "ms");

        System.out.println("Zeit fuer die nicht Erfolgreiche suche im SortedArrayDictionary mit 8000 Eintraegen: " + timeFromNanosToMillis(search(tui, notSuccessfulSearch8000)) + "ms");


        tui = new TUI();
        param = new ArrayList<>();
        param.add(path);
        t0 = System.nanoTime();
        tui.read(param);
        t1 = System.nanoTime();
        System.out.println("Zeit zum aufbauen eines SortedArrayDictionary mit 15896 Eintraegen: " + timeFromNanosToMillis(t1 - t0) + "ms");

        System.out.println("Zeit fuer die Erfolgreiche suche im SortedArrayDictionary mit 15896 Eintraegen: " + timeFromNanosToMillis(search(tui, successfulSearch15896)) + "ms");

        System.out.println("Zeit fuer die nicht Erfolgreiche suche im SortedArrayDictionary mit 15896 Eintraegen: " + timeFromNanosToMillis(search(tui, notSuccessfulSearch15896)) + "ms");


        tui = new TUI();
        tui.create('h');
        param = new ArrayList<>();
        param.add("8000");
        param.add(path);
        t0 = System.nanoTime();
        tui.read(param);
        t1 = System.nanoTime();
        System.out.println("Zeit zum aufbauen eines HashDictionary mit 8000 Eintraegen: " + timeFromNanosToMillis(t1 - t0) + "ms");

        System.out.println("Zeit fuer die Erfolgreiche suche im HashDictionary mit 8000 Eintraegen: " + timeFromNanosToMillis(search(tui, successfulSearch8000)) + "ms");

        System.out.println("Zeit fuer die nicht Erfolgreiche suche im HashDictionary mit 8000 Eintraegen: " + timeFromNanosToMillis(search(tui, notSuccessfulSearch8000)) + "ms");


        tui = new TUI();
        tui.create('h');
        param = new ArrayList<>();
        param.add(path);
        t0 = System.nanoTime();
        tui.read(param);
        t1 = System.nanoTime();
        System.out.println("Zeit zum aufbauen eines HashDictionary mit 15896 Eintraegen: " + timeFromNanosToMillis(t1 - t0) + "ms");

        System.out.println("Zeit fuer die Erfolgreiche suche im HashDictionary mit 15896 Eintraegen: " + timeFromNanosToMillis(search(tui, successfulSearch15896)) + "ms");

        System.out.println("Zeit fuer die nicht Erfolgreiche suche im HashDictionary mit 15896 Eintraegen: " + timeFromNanosToMillis(search(tui, notSuccessfulSearch15896)) + "ms");


        tui = new TUI();
        tui.create('b');
        param = new ArrayList<>();
        param.add("8000");
        param.add(path);
        t0 = System.nanoTime();
        tui.read(param);
        t1 = System.nanoTime();
        System.out.println("Zeit zum aufbauen eines BinarySearchTreeDictionary mit 8000 Eintraegen: " + timeFromNanosToMillis(t1 - t0) + "ms");

        System.out.println("Zeit fuer die Erfolgreiche suche im BinarySearchTreeDictionary mit 8000 Eintraegen: " + timeFromNanosToMillis(search(tui, successfulSearch8000)) + "ms");

        System.out.println("Zeit fuer die nicht Erfolgreiche suche im BinarySearchTreeDictionary mit 8000 Eintraegen: " + timeFromNanosToMillis(search(tui, notSuccessfulSearch8000)) + "ms");

        tui = new TUI();
        tui.create('b');
        param = new ArrayList<>();
        param.add(path);
        t0 = System.nanoTime();
        tui.read(param);
        t1 = System.nanoTime();
        System.out.println("Zeit zum aufbauen eines BinarySearchTreeDictionary mit 15896 Eintraegen: " + timeFromNanosToMillis(t1 - t0) + "ms");

        System.out.println("Zeit fuer die Erfolgreiche suche im BinarySearchTreeDictionary mit 15896 Eintraegen: " + timeFromNanosToMillis(search(tui, successfulSearch15896)) + "ms");

        System.out.println("Zeit fuer die nicht Erfolgreiche suche im BinarySearchTreeDictionary mit 15896 Eintraegen: " + timeFromNanosToMillis(search(tui, notSuccessfulSearch15896)) + "ms");
    }
}
