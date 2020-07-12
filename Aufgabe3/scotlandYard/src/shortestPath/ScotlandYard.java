package shortestPath;

import directedGraph.*;
import java.io.FileNotFoundException;
import sim.SYSimulation;
import java.awt.Color;
import java.io.IOException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;



/**
 * Kürzeste Wege im Scotland-Yard Spielplan mit A* und Dijkstra.
 * @author Oliver Bittel
 * @since 27.02.2019
 */
public class ScotlandYard {

	/**
	 * Fabrikmethode zur Erzeugung eines gerichteten Graphens für den Scotland-Yard-Spielplan.
	 * <p>
	 * Liest die Verbindungsdaten von der Datei ScotlandYard_Kanten.txt.
	 * Für die Verbindungen werden folgende Gewichte angenommen:
	 * U-Bahn = 5, Taxi = 2 und Bus = 3.
	 * Falls Knotenverbindungen unterschiedliche Beförderungsmittel gestatten,
	 * wird das billigste Beförderungsmittel gewählt. 
	 * Bei einer Vebindung von u nach v wird in den gerichteten Graph sowohl 
	 * eine Kante von u nach v als auch von v nach u eingetragen.
	 * @return Gerichteter und Gewichteter Graph für Scotland-Yard.
	 * @throws FileNotFoundException
	 */
	public static DirectedGraph<Integer> getGraph() throws FileNotFoundException {

		DirectedGraph<Integer> sy_graph = new AdjacencyListDirectedGraph<>();
		Scanner in = new Scanner(new File("Aufgabe3/scotlandYard/src/ScotlandYard_Kanten.txt"));

		while (in.hasNextLine()) {
			String[] entry = in.nextLine().split(" ");
			if (entry.length != 3) {
				continue;
			}
			double weight = 0;
			switch (entry[2]) {
				case "UBahn":
					weight = 5;
					break;
				case "Bus":
					weight = 3;
					break;
				case "Taxi":
					weight = 2;
					break;
				default:
					throw new IllegalArgumentException(entry[2] + " is not a transport vehicle!");
			}
			int s = Integer.parseInt(entry[0]);
			int g = Integer.parseInt(entry[1]);

			if (sy_graph.containsEdge(s,g))
				weight = Math.min(weight, sy_graph.getWeight(s,g));
			sy_graph.addEdge(s,g, weight);
			sy_graph.addEdge(g,s, weight);
		}
		
		// Test, ob alle Kanten eingelesen wurden: 
		System.out.println("Number of Vertices:       " + sy_graph.getNumberOfVertexes());	// 199
		System.out.println("Number of directed Edges: " + sy_graph.getNumberOfEdges());	  	// 862
		double wSum = 0.0;
		for (Integer v : sy_graph.getVertexSet())
			for (Integer w : sy_graph.getSuccessorVertexSet(v))
				wSum += sy_graph.getWeight(v,w);
		System.out.println("Sum of all Weights:       " + wSum);	// 1972.0
		
		return sy_graph;
	}


	/**
	 * Fabrikmethode zur Erzeugung einer Heuristik für die Schätzung
	 * der Distanz zweier Knoten im Scotland-Yard-Spielplan.
	 * Die Heuristik wird für A* benötigt.
	 * <p>
	 * Liest die (x,y)-Koordinaten (Pixelkoordinaten) aller Knoten von der Datei
	 * ScotlandYard_Knoten.txt in eine Map ein.
	 * Die zurückgelieferte Heuristik-Funktion estimatedCost
	 * berechnet einen skalierten Euklidischen Abstand.
	 * @return Heuristik für Scotland-Yard.
	 * @throws FileNotFoundException
	 */
	public static Heuristic<Integer> getHeuristic() throws FileNotFoundException {
		return new ScotlandYardHeuristic();
	}

	/**
	 * Scotland-Yard Anwendung.
	 * @param args wird nicht verewendet.
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {

		DirectedGraph<Integer> syGraph = getGraph();
		
		Heuristic<Integer> syHeuristic = null; // Dijkstra
		Heuristic<Integer> syHeuristic2 = getHeuristic(); // A*

		ShortestPath<Integer> sySp = new ShortestPath<Integer>(syGraph,syHeuristic);
		ShortestPath<Integer> sySp2 = new ShortestPath<Integer>(syGraph,syHeuristic2);

		sySp.searchShortestPath(65,157);
		sySp2.searchShortestPath(65,157);
		System.out.println("Distance = " + sySp.getDistance()); // 9.0
		System.out.println("Distance2 = " + sySp2.getDistance());

		sySp.searchShortestPath(1,175);
		sySp2.searchShortestPath(1,175);
		System.out.println("Distance = " + sySp.getDistance()); // 25.0
		System.out.println("Distance2 = " + sySp2.getDistance());

		sySp.searchShortestPath(1,173);
		sySp2.searchShortestPath(1,173);
		System.out.println("Distance = " + sySp.getDistance()); // 22.0
		System.out.println("Distance2 = " + sySp2.getDistance());


		SYSimulation sim;
		try {
			sim = new SYSimulation();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		sySp.setSimulator(sim);
		sySp2.setSimulator(sim);
		sim.startSequence("Shortest path from 1 to 173");

		//sySp.searchShortestPath(65,157); // 9.0
		//sySp.searchShortestPath(1,175); //25.0
		
		sySp.searchShortestPath(1,173); //22.0
		sySp2.searchShortestPath(1,173);
		// bei Heuristik-Faktor von 1/10 wird nicht der optimale Pfad produziert.
		// bei 1/30 funktioniert es.

		System.out.println("Distance = " + sySp.getDistance());
		System.out.println("Distance2 = " + sySp2.getDistance());
		List<Integer> sp = sySp.getShortestPath();
		List<Integer> sp2 = sySp2.getShortestPath();

		int a = -1;
		for (int b : sp) {
			if (a != -1)
			sim.drive(a, b, Color.RED.darker());
			sim.visitStation(b);
			a = b;
		}

		a = -1;
		for (int b : sp2) {
			if (a != -1)
				sim.drive(a, b, Color.GREEN.darker());
			sim.visitStation(b);
			a = b;
		}

        sim.stopSequence();


    }

}

class ScotlandYardHeuristic implements Heuristic<Integer> {
	private Map<Integer,Point> coord; // Ordnet jedem Knoten seine Koordinaten zu

	private static class Point {
		int x;
		int y;
		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public ScotlandYardHeuristic() throws FileNotFoundException {
		coord = new HashMap<>();
		Scanner in = new Scanner(new File("Aufgabe3/scotlandYard/src/ScotlandYard_Knoten.txt"));
		while (in.hasNextLine()) {
			String[] entry = in.nextLine().split("\\s+");
			if (entry.length != 3)
				continue;
			int vertex = Integer.parseInt(entry[0]);
			int x = Integer.parseInt(entry[1]);
			int y = Integer.parseInt(entry[2]);

			Point point = new Point(x,y);
			if (coord.containsKey(vertex))
				coord.replace(vertex, point);
			else
				coord.put(vertex, point);
		}
	}

	public double estimatedCost(Integer u, Integer v) {
		Point a = coord.get(u);
		Point b = coord.get(v);
		return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y -b.y, 2));
	}
}

