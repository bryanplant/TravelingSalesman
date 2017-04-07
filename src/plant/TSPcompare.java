package plant;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.Timer;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.PrimMST;
import edu.princeton.cs.algs4.StdDraw;

public class TSPcompare {

	private Random rand = new Random();
	private double[][] v;
	private ArrayList<Integer> path;
	private double totalDist;
	private ArrayList<ArrayList<Integer>> lines;

	public TSPcompare(int vNum){
		StdDraw.setCanvasSize(800, 800);
        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-10, 110);

        StdDraw.setPenColor(Color.BLACK);

		v = new double[vNum][2];
		lines = new ArrayList<>(vNum);

		for(int i = 0; i < vNum; i++){
			double x = rand.nextDouble()*100;
			double y = rand.nextDouble()*100;
			v[i][0] = x;
			v[i][1] = y;
			if(i == 0){
				StdDraw.setPenColor(new Color(13, 255, 19, 255));
				StdDraw.filledCircle(x, y, 1.25);
				StdDraw.setPenColor(Color.BLACK);
			}
			else{
				StdDraw.filledCircle(x, y, .75);
			}
			if(vNum<100){
				StdDraw.text(x, y-3, Integer.toString(i));
				if(i == 0){
					StdDraw.text(x, y-5, "(Start)");
				}
			}
			lines.add(new ArrayList<Integer>());
		}

		StdDraw.text(25, 108, "KEY:");
		StdDraw.text(35, 108, "Greedy");
		StdDraw.text(43.5, 105, "Twice-Around-The-Tree");
		StdDraw.setPenColor(Color.RED);
		StdDraw.filledRectangle(30, 108.5, 1, 1);
		StdDraw.setPenColor(Color.BLUE);
		StdDraw.filledRectangle(30, 105.5, 1, 1);

		/*for(int i = 0; i < vNum; i++){
			System.out.println("Vert " + i + ": " + v[i][0] + ", " + v[i][1]);
		}
		System.out.println();*/
	}

	public void greedy(){
		boolean[] visited = new boolean[v.length];
		int current = 0;
		totalDist = 0;
		path = new ArrayList<>();


		visited[0] = true;
		path.add(0);

		for(int i = 1; i < v.length; i++){
			int near = 0;
			double nearValue = 1000;

			for(int j = 0; j < v.length; j++){
				if(!visited[j] && j != current){
					double value = Math.sqrt(Math.pow(v[current][0] - v[j][0], 2)+Math.pow(v[current][1] - v[j][1], 2));
					if(value < nearValue){
						nearValue = value;
						near = j;
					}
				}
			}
			current = near;
			visited[current] = true;
			path.add(current);
			totalDist += nearValue;
		}

		path.add(0);
		totalDist += (Math.sqrt(Math.pow(v[path.get(path.size()-2)][0] - v[0][0], 2)+Math.pow(v[path.get(path.size()-2)][1] - v[0][1], 2)));

		drawPath(Color.RED);
	}

	public void twiceAround(){
		EdgeWeightedGraph weightGraph = new EdgeWeightedGraph(v.length);
		for(int i = 0; i < v.length; i++){
			for(int j = 0; j < v.length; j++){
				double weight = Math.sqrt(Math.pow(v[i][0] - v[j][0], 2)+Math.pow(v[i][1] - v[j][1], 2));
				weightGraph.addEdge(new Edge(i, j, weight));
			}
		}
		PrimMST mst = new PrimMST(weightGraph);
		/*for(Edge e : mst.edges()){
			System.out.println("Edge between " + e.either() + " and " + e.other(e.either()) + " is " + e.weight());
		}*/

		boolean[] visited = new boolean[v.length];
		Stack<Integer> s = new Stack<Integer>();
		path = new ArrayList<>();

		s.push(0);
		visited[0] = true;
		path.add(0);
		totalDist = 0;

		while(!s.isEmpty()){
			int i = s.peek();

			boolean found = false;
			int k = 0;
			for(Edge e : mst.edges()){
				try{
					int j = e.other(i);
					if(!visited[j]){
						s.push(j);
						if(!path.contains(j)){
							int last = path.get(path.size()-1);
							totalDist += (Math.sqrt(Math.pow(v[last][0] - v[j][0], 2) + Math.pow(v[last][1] - v[j][1], 2)));
							path.add(j);
						}
						visited[j] = true;
						found = true;
						break;
					}
				}
				catch(Exception ex){
					k = 5;
				}
			}
			if(!found){
				s.pop();
			}
		}

		int last = path.get(path.size()-1);
		totalDist += (Math.sqrt(Math.pow(v[0][0] - v[last][0], 2) + Math.pow(v[0][1] - v[last][1], 2)));
		path.add(0);

		drawPath(Color.BLUE);
	}

	public void drawPath(Color color){
		StdDraw.setPenColor(color);
		for(int i = 0; i < path.size(); i++){
				System.out.printf("%d%s", path.get(i), i == path.size()-1 ? "\n" : " -> ");
				if(i > 0){
					int current = path.get(i);
					int last = path.get(i-1);
					if(lines.get(current).contains(last)){
						double x1 = v[last][0];
						double y1 = v[last][1];
						double x2 = v[current][0];
						double y2 = v[current][1];
						double dx = x2 - x1;
						double dy = y2 - y1;

						double theta = Math.atan2(dy, dx);

						double rad = Math.toRadians(90);
						double newX1 = x1 + .75*Math.cos(rad+theta);
						double newY1 = y1 + .75*Math.sin(rad+theta);
						double newX2 = x2 + .75*Math.cos(rad+theta);
						double newY2 = y2 + .75*Math.sin(rad+theta);

						StdDraw.line(newX1, newY1, newX2, newY2);
						drawArrow(newX2, newX1, newY2, newY1);
					}
					else{
						StdDraw.line(v[last][0], v[last][1], v[current][0], v[current][1]);
						lines.get(current).add(last);
						lines.get(last).add(current);
						drawArrow(v[current][0], v[last][0], v[current][1], v[last][1]);
					}
				}
		}
		System.out.println("Distance traveled: " + totalDist);
	}

	private void drawArrow(double tipX, double tailX, double tipY, double tailY)
	{
	    double arrowLength = 1.75; //can be adjusted
	    double dx = tipX - tailX;
	    double dy = tipY - tailY;

	    double theta = Math.atan2(dy, dx);

	    double rad = Math.toRadians(30); //30 angle, can be adjusted
	    double x = tipX - arrowLength * Math.cos(theta + rad);
	    double y = tipY - arrowLength * Math.sin(theta + rad);

	    double phi2 = Math.toRadians(-30);//-30 angle, can be adjusted
	    double x2 = tipX - arrowLength * Math.cos(theta + phi2);
	    double y2 = tipY - arrowLength * Math.sin(theta + phi2);

	    double[] arrowYs = new double[3];
	    arrowYs[0] = tipY;
	    arrowYs[1] =  y;
	    arrowYs[2] =  y2;


	    double[] arrowXs = new double[3];
	    arrowXs[0] = tipX;
	    arrowXs[1] = x;
	    arrowXs[2] = x2;

	    StdDraw.filledPolygon(arrowXs, arrowYs);
	}

	public static void main(String[] args){
		TSPcompare tsp = new TSPcompare(Integer.parseInt(args[0]));

		long startTime = System.currentTimeMillis();
		tsp.greedy();
		System.out.println("Total time: " + (double)(System.currentTimeMillis()-startTime)/1000 + " seconds\n");

		startTime = System.currentTimeMillis();
		tsp.twiceAround();
		System.out.println("Total time: " + (double)(System.currentTimeMillis()-startTime)/1000 + " seconds\n");
		StdDraw.show();
	}
}
