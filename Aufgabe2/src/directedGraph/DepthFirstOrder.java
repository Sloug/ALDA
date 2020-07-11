// O. Bittel;
// 22.02.2017
package directedGraph;

import java.util.*;

/**
 * Klasse für Tiefensuche.
 *
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class DepthFirstOrder<V> {

    private final List<V> preOrder = new LinkedList<>();
    private final List<V> postOrder = new LinkedList<>();
    private final DirectedGraph<V> myGraph;
    private int numberOfDFTrees = 0;
    private Set<V> strongComponent = null;
    private Map<Integer, Set<V>> strongComponents = null;

    void visitAllNodes() {
        for (V v : myGraph.getVertexSet()) {
            if (!preOrder.contains(v)) {
                visit(v);
                numberOfDFTrees++;
            }
        }
    }

    void visit(V v) {
        if (!preOrder.contains(v))
            preOrder.add(v);
        if (strongComponent != null)
            strongComponent.add(v);
        for (V w : myGraph.getSuccessorVertexSet(v)) {
            if (!preOrder.contains(w)) {
                preOrder.add(w);
                if (strongComponent != null)
                    strongComponent.add(w);
                visit(w);
                if (!postOrder.contains(w))
                    postOrder.add(w);
            }
        }
        if (!postOrder.contains(v))
            postOrder.add(v);
    }

    void visitAllNodesOrder(List<V> order) {
        strongComponents = new TreeMap<>();
        for (V v : order) {
            if (!preOrder.contains(v)) {
                strongComponent = new HashSet<>();
                visit(v);
                strongComponents.put(numberOfDFTrees, strongComponent);
                numberOfDFTrees++;
            }
        }
    }

    public DepthFirstOrder(DirectedGraph g, List order) {
        myGraph = g;
        visitAllNodesOrder(order);
    }
    /**
     * Führt eine Tiefensuche für g durch.
     *
     * @param g gerichteter Graph.
     */
    public DepthFirstOrder(DirectedGraph<V> g) {
        myGraph = g;
        visitAllNodes();
    }

    public Map<Integer, Set<V>> getStrongComponents() {
        return strongComponents;
    }

    /**
     * Liefert eine nicht modifizierbare Liste (unmodifiable view) mit einer
     * Pre-Order-Reihenfolge zurück.
     *
     * @return Pre-Order-Reihenfolge der Tiefensuche.
     */
    public List<V> preOrder() {
        return Collections.unmodifiableList(preOrder);
    }

    /**
     * Liefert eine nicht modifizierbare Liste (unmodifiable view) mit einer
     * Post-Order-Reihenfolge zurück.
     *
     * @return Post-Order-Reihenfolge der Tiefensuche.
     */
    public List<V> postOrder() {
        return Collections.unmodifiableList(postOrder);
    }

    /**
     *
     * @return Anzahl der Bäume des Tiefensuchwalds.
     */
    public int numberOfDFTrees() {
        return numberOfDFTrees;
    }

    public static void main(String[] args) {
        DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
        g.addEdge(1, 2);
        g.addEdge(2, 5);
        g.addEdge(5, 1);
        g.addEdge(2, 6);
        g.addEdge(3, 7);
        g.addEdge(4, 3);
        g.addEdge(4, 6);
        //g.addEdge(7,3);
        g.addEdge(7, 4);

        DepthFirstOrder<Integer> dfs = new DepthFirstOrder<>(g);
        System.out.println(dfs.numberOfDFTrees());	// 2
        System.out.println(dfs.preOrder());		// [1, 2, 5, 6, 3, 7, 4]
        System.out.println(dfs.postOrder());		// [5, 6, 2, 1, 4, 7, 3]

    }
}
