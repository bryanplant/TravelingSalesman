package plant;

import java.util.Random;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.PrimMST;

import java.math.*;

public class TSPcompare {

	Random rand = new Random();
	double[][] v;

	public TSPcompare(int vNum){
		v = new double[vNum][2];

		for(int i = 0; i < vNum; i++){
			v[i][0] = rand.nextDouble()*100;
			v[i][1] = rand.nextDouble()*100;
		}

		for(int i = 0; i < vNum; i++){
			System.out.println("Vert" + i + ": " + v[i][0] + ", " + v[i][1]);
		}
		System.out.println();
	}

	public void greedy(){
		boolean[] visited = new boolean[v.length];
		int i = 0;
		int[] order = new int[v.length+1];
		double totalDist = 0;


		visited[i] = true;
		order[0] = i;

		for(int counter = 1; counter < v.length; counter++){
			int near = 0;
			double nearValue = 1000;

			for(int j = 0; j < v.length; j++){
				if(!visited[j] && j != i){
					double value = Math.sqrt(Math.pow(v[i][0] - v[j][0], 2)+Math.pow(v[i][1] - v[j][1], 2));
					if(value < nearValue){
						nearValue = value;
						near = j;
					System.out.println("New closest node to " + i + " is " + j + " @ " + value);
					}
				}
			}
			i = near;
			visited[i] = true;
			order[counter] = i;
			totalDist += nearValue;

			System.out.println();
		}

		order[v.length] = 0;
		totalDist += (Math.sqrt(Math.pow(v[order[v.length - 1]][0] - v[0][0], 2)+Math.pow(v[order[v.length-1]][1] - v[0][1], 2)));

		for(i = 0; i <= v.length; i++){
				System.out.print(order[i]);
				if(i != v.length)
					System.out.print(" -> ");
		}
		System.out.println("\nDistance traveled: " + totalDist);
	}

	public void twiceAround(){
		EdgeWeightedGraph graph = new EdgeWeightedGraph(v.length);
		for(int i = 0; i < v.length; i++){
			for(int j = 0; j < v.length; j++){
				double weight = Math.sqrt(Math.pow(v[i][0] - v[j][0], 2)+Math.pow(v[i][1] - v[j][1], 2));
				graph.addEdge(new Edge(i, j, weight));
			}
		}
		PrimMST mst = new PrimMST(graph);
		for(Edge e : mst.edges()){
			System.out.println("Edge between " + e.either() + " and " + e.other(e.either()) + " is " + e.weight());
		}
	}

	public static void main(String[] args){
		TSPcompare tsp = new TSPcompare(Integer.parseInt(args[0]));
		//tsp.greedy();
		tsp.twiceAround();
	}
}
