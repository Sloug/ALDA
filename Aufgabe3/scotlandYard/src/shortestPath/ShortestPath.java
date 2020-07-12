// O. Bittel;
// 28.02.2019

package shortestPath;

import directedGraph.*;
import sim.SYSimulation;

import java.util.*;

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 * @author Oliver Bittel
 * @since 27.01.2015
 * @param <V> Knotentyp.
 */
public class ShortestPath<V> {
	
	private SYSimulation sim = null;
	
	private final Map<V,Double> dist; // Distanz für jeden Knoten
	private final Map<V,V> pred; // Vorgänger für jeden Knoten
	private final DirectedGraph<V> myGraph;
	private final Heuristic heuristic;
	private V end = null;


	/**
	 * Konstruiert ein Objekt, das im Graph g k&uuml;rzeste Wege 
	 * nach dem A*-Verfahren berechnen kann.
	 * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
	 * Wird h = null gewählt, dann ist das Verfahren identisch 
	 * mit dem Dijkstra-Verfahren.
	 * @param g Gerichteter Graph
	 * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
	 * dem Dijkstra-Verfahren gesucht.
	 */
	public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
		this.dist = new HashMap<>();
		this.pred = new HashMap<>();
		this.myGraph = g;
		this.heuristic = h;
	}

	/**
	 * Diese Methode sollte nur verwendet werden, 
	 * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
	 * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
	 * <p>
	 * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
	 * <p><blockquote><pre>
	 *    if (sim != null)
	 *       sim.visitStation((Integer) v, Color.blue);
	 * </pre></blockquote>
	 * @param sim SYSimulation-Objekt.
	 */
	public void setSimulator(SYSimulation sim) {
		this.sim = sim;
	}

	/**
	 * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
	 * <p>
	 * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
	 * der als nächstes aus der Kandidatenliste besucht wird, animiert.
	 * @param s Startknoten
	 * @param g Zielknoten
	 */
	public void searchShortestPath(V s, V g) {
		end = g;
		List <V> canidates = new LinkedList<>();
		for (V v : myGraph.getVertexSet()) {
			dist.put(v, Double.POSITIVE_INFINITY);
			pred.put(v, null);
		}
		dist.replace(s, 0.0);
		canidates.add(s);

		while(!canidates.isEmpty()) {
			V v = null;
			if (heuristic == null) {
				for (V x : canidates) {
					if (v == null || dist.get(x) < dist.get(v)) v = x;
				}
			} else {
				v = canidates.get(0);
				for (V x : canidates) {
					if (v == null || dist.get(x) + heuristic.estimatedCost(x, end) < dist.get(v) + heuristic.estimatedCost(v, end)) v = x;
				}
			}
			canidates.remove(v);
			for (V w : myGraph.getSuccessorVertexSet(v)) {
				if (dist.get(w).isInfinite())
					canidates.add(w);
				if (dist.get(v) + myGraph.getWeight(v,w) < dist.get(w)) {
					pred.replace(w, v);
					dist.replace(w, dist.get(v) + myGraph.getWeight(v,w));
				}
			}
		}
	}

	/**
	 * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return kürzester Weg als Liste von Knoten.
	 */
	public List<V> getShortestPath() {
		ArrayDeque<V> queue = new ArrayDeque();
		V v = end;
		queue.addFirst(v);
		while (pred.get(v) != null) {
			queue.addFirst(pred.get(v));
			v = queue.getFirst();
		}
		return new ArrayList<>(queue);
	}

	/**
	 * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return Länge eines kürzesten Weges.
	 */
	public double getDistance() {
		return dist.get(end);
	}

}
