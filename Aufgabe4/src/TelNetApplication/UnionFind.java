package TelNetApplication;

import com.sun.xml.internal.ws.util.StreamUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

public class UnionFind {
    private int[] unionStruct;

    public UnionFind(int n) {
        unionStruct = new int[n];
        Arrays.fill(unionStruct, -1);
    }

    public int find(int e) {
        while (unionStruct[e] >= 0)
            e = unionStruct[e];
        return e;
    }

    public void union(int s1, int s2) {
        if(unionStruct[s1] >= 0 || unionStruct[s2] >= 0)
            return;
        if (s1 == s2)
            return;

        if (-unionStruct[s1] < -unionStruct[s2])
            unionStruct[s1] = s2;
        else {
            if (-unionStruct[s1] == -unionStruct[s2])
                unionStruct[s1]--;
            unionStruct[s2] = s1;
        }
    }

    public int size() {
        return (int) IntStream.of(unionStruct).filter(x -> x <= -1).count();
    }

    public static void main(String[] args) {
        UnionFind uf = new UnionFind(14);
        System.out.println("size: " + uf.size());          //14
        uf.union(0, 1);
        uf.union(0, 2);
        uf.union(5, 3);
        uf.union(5, 8);
        uf.union(0, 5);


        uf.union(7, 12);
        uf.union(4, 9);
        uf.union(4, 7);

        uf.union(11, 13);
        uf.union(6, 10);
        uf.union(6, 11);

        uf.union(6, 4);


        uf.union(6, 0); //siehe im Skript 9-30

        System.out.println();
        System.out.println();
        System.out.println("size: " + uf.size());          //3
        System.out.println("find 7: " + uf.find(7));    //6
        System.out.println("find 3: " + uf.find(3));    //5
        IntStream.range(0, uf.unionStruct.length)
                .forEach(i ->
                        System.out.print(i + ": " + uf.unionStruct[i] + " | "));
    }
}
