package TelNetApplication;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TelNet {
    private final int lbg;
    private final HashMap<TelKnoten, Integer> knoten;
    private List<TelVerbindung> optTelNet;
    private Double scalerX = null;
    private Double scalerY = null;

    public TelNet(int lbg) {
        this.lbg = lbg;
        knoten = new HashMap<>();
        optTelNet = null;
    }

    public boolean addTelKnoten(int x, int y) {
        TelKnoten tmp = new TelKnoten(x, y);
        if (knoten.containsKey(tmp))
            return false;
        else  {
            knoten.put(new TelKnoten(x,y), knoten.size());
            return true;
        }
    }

    public boolean computeOptTelNet() {
        UnionFind forest = new UnionFind(size());
        PriorityQueue<TelVerbindung> edges = new PriorityQueue<>();
        for (TelKnoten v : knoten.keySet()) {
            for (TelKnoten w : knoten.keySet()) {
                if (v != w) {
                    int totalcost = Math.abs(v.x - w.x) + Math.abs(v.y - w.y);
                    if (totalcost <= lbg)
                        edges.add(new TelVerbindung(w, v, totalcost));
                }
            }

        }
        List<TelVerbindung> minSpanTree = new ArrayList<>();

        while (forest.size() != 1 && !edges.isEmpty()) {
            TelVerbindung v = edges.poll();
            int t1 = forest.find(knoten.get(v.u));
            int t2 = forest.find(knoten.get(v.v));
            if (t1 != t2) {
                forest.union(t1,t2);
                minSpanTree.add(v);
            }
        }
        if (edges.isEmpty() && forest.size() != 1)
            return false;
        else {
            optTelNet = minSpanTree;
            return true;
        }
    }

    public void generateRandomTelNet(int n, int xMax, int yMax) {
        Random random = new Random();
        for(int i = 0; i < n;) {
            if (addTelKnoten(random.nextInt(xMax), random.nextInt(yMax)))
                i++;
        }
    }

    private double scaleX(double x) {
        if (scalerX == null)
            throw new IllegalArgumentException();
        return x / scalerX;
    }

    private double scaleY(double y) {
        if (scalerY == null)
            throw new IllegalArgumentException();
        return y / scalerY;
    }

    public void drawOptTelNet(int xMax, int yMax) {
        if (optTelNet == null)
            throw new IllegalArgumentException();
        scalerX = (double) xMax;
        scalerY = (double) yMax;
        StdDraw.setXscale(0, xMax);
        StdDraw.setYscale(0, yMax);
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(Color.BLUE);

        for (TelKnoten k : knoten.keySet()) {
            StdDraw.point(scaleX(k.x), scaleY(k.y));
        }

        StdDraw.setPenRadius(0.006);
        StdDraw.setPenColor(Color.RED);

        for (TelVerbindung v : optTelNet) {
            StdDraw.line(scaleX(v.u.x), scaleY(v.v.y), scaleX(v.v.x), scaleY(v.v.y));
            StdDraw.line(scaleX(v.u.x), scaleY(v.u.y), scaleX(v.u.x), scaleY(v.v.y));
        }
    }

    public List<TelVerbindung> getOptTelNet() throws IllegalStateException {
        if (optTelNet == null)
            throw new IllegalStateException();
        return optTelNet;
    }

    public int getOptTelNetKosten() throws IllegalStateException {
        if (optTelNet == null)
            throw new IllegalStateException();
        return optTelNet.stream().mapToInt(connection -> connection.c).sum();
    }

    public int size() {
        return knoten.size();
    }

    @Override
    public String toString() {
        return  knoten.entrySet().stream().map(entry -> entry.getValue() + ": " + entry.getKey() + "\n").collect(Collectors.joining());
    }

    public static void main(String[] args) {
//        test();
        testRandom();
    }

    private static void test() {
        TelNet test = new TelNet(7);
        test.addTelKnoten(1,1);
        test.addTelKnoten(3,1);
        test.addTelKnoten(4,2);
        test.addTelKnoten(3,4);
        test.addTelKnoten(7,5);
        test.addTelKnoten(2,6);
        test.addTelKnoten(4,7);

        testOutput(test, 7);
    }

    private static void testRandom() {
        TelNet test = new TelNet(100);
        test.generateRandomTelNet(1000, 1000, 1000);

        testOutput(test, 1000);
    }

    private static void testOutput(TelNet telNet, int scale) {
        boolean tmp = telNet.computeOptTelNet();
        assert tmp;
        System.out.println(telNet.getOptTelNet());
        System.out.println(telNet.getOptTelNetKosten());
        System.out.println(telNet);
        telNet.drawOptTelNet(scale,scale);
    }
}
